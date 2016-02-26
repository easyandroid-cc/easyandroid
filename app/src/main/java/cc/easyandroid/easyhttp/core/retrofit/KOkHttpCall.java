/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.easyandroid.easyhttp.core.retrofit;

import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import cc.easyandroid.easycache.volleycache.Cache;
import cc.easyandroid.easycache.volleycache.Cache.Entry;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

public abstract class KOkHttpCall<T> implements Call<T> {
    protected final OkHttpClient client;
    // private final RequestFactory requestFactory;
    protected final Converter<T> responseConverter;
    // private final Object[] args;

    private volatile com.squareup.okhttp.Call rawCall;
    private boolean executed; // Guarded by this.
    private volatile boolean canceled;

    public KOkHttpCall(OkHttpClient client, Converter<T> responseConverter) {
        this.client = client;
        // this.requestFactory = requestFactory;
        this.responseConverter = responseConverter;
        // this.args = args;
    }

    // We are a final type & this saves clearing state.
    @Override
    public abstract KOkHttpCall<T> clone();

    static final String THREAD_PREFIX = "Retrofit-";
    static final String IDLE_THREAD_NAME = THREAD_PREFIX + "Idle";

    static Executor defaultHttpExecutor() {
        return Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(final Runnable r) {
                return new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Process.setThreadPriority(THREAD_PRIORITY_BACKGROUND);
                        r.run();
                    }
                }, IDLE_THREAD_NAME);
            }
        });
    }

    public static final Executor threadExecutor = defaultHttpExecutor();
    public static final Executor mainCallbackExecutor = new MainThreadExecutor();

    static class MainThreadExecutor implements Executor {
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable r) {
            handler.post(r);
        }
    }

    private Response<T> execCacheRequest(Request request) {
        if (responseConverter instanceof GsonConverter) {
            GsonConverter<T> converter = (GsonConverter<T>) responseConverter;
            Cache cache = converter.getCache();
            if (cache == null) {
                return null;
            }
            Entry entry = cache.get(request.urlString());// 充缓存中获取entry
            if (entry == null) {
                return null;
            }
            if (entry.isExpired()) {// 缓存过期了
                return null;
            }
            if (entry.data != null) {// 如果有数据就使用缓存
                MediaType contentType = MediaType.parse(entry.mimeType);
                byte[] bytes = entry.data;
                try {
                    com.squareup.okhttp.Response rawResponse = new com.squareup.okhttp.Response.Builder()//
                            .code(200).request(request).protocol(Protocol.HTTP_1_1).body(ResponseBody.create(contentType, bytes)).build();
                    return parseResponse(rawResponse, request, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public void enqueue(final Callback<T> callback) {
        synchronized (this) {
            if (executed)
                throw new IllegalStateException("Already executed");
            executed = true;
        }
        final Request request = createRequest();
        String cacheMode = getCacheMode(request);
        // ----------------------------------------------------------------------cgp
        if (!TextUtils.isEmpty(cacheMode)) {
            switch (cacheMode) {
                case CacheMode.LOAD_NETWORK_ELSE_CACHE:// 先网络然后再缓存
                    exeRequest(callback, request, true);
                    return;
                case CacheMode.LOAD_CACHE_ELSE_NETWORK:// 先缓存再网络
                    // ---------------------从缓存中取
                    Response<T> response = execCacheRequest(request);
                    if (response != null) {
                        callback.onResponse(response);
                        return;
                    }
                    // ---------------------从缓存中取
                    // 如果缓存没有就跳出，执行网络请求
                case CacheMode.LOAD_DEFAULT:
                case CacheMode.LOAD_NETWORK_ONLY:
                default:
                    break;// 直接跳出
            }
        }
        // ----------------------------------------------------------------------cgp
        exeRequest(callback, request, false);
    }

    private void exeRequest(final Callback<T> callback, final Request request, final boolean loadnetElseCache) {
        com.squareup.okhttp.Call rawCall;
        try {
            rawCall = client.newCall(request);
        } catch (Throwable t) {
            callback.onFailure(t);
            return;
        }
        if (canceled) {
            rawCall.cancel();
        }
        this.rawCall = rawCall;
        callback.onstart();
        rawCall.enqueue(new com.squareup.okhttp.Callback() {
            private void callFailure(final Throwable e) {
                try {
                    mainCallbackExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure(e);
                        }
                    });
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }

            private void callSuccess(final Response<T> response) {
                try {
                    mainCallbackExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            callback.onResponse(response);
                        }
                    });
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }

            @Override
            public void onFailure(final Request request, IOException e) {
                if (canceled) {
                    return;
                }
                //没有网络会进入onFailure
                if (loadnetElseCache) {
                    threadExecutor.execute(new CallbackRunnable<T>(callback, mainCallbackExecutor) {
                        @Override
                        public Response<T> obtainResponse() {//这里走缓存
                            return execCacheRequest(request);
                        }
                    });
                    return;
                }
                callFailure(e);
            }

            @Override
            public void onResponse(com.squareup.okhttp.Response rawResponse) {

                if (canceled) {
                    return;
                }
                Response<T> response;
                try {
                    response = parseResponse(rawResponse, request, loadnetElseCache);
                } catch (Throwable e) {
                    if (loadnetElseCache) {
                        onFailure(request, new IOException("解析结果错误"));
                        return;
                    }
                    callFailure(e);
                    return;
                }
                callSuccess(response);
            }
        });
    }

    public Response<T> execute() throws IOException {
        synchronized (this) {
            if (executed)
                throw new IllegalStateException("Already executed");
            executed = true;
        }
        final Request request = createRequest();

        com.squareup.okhttp.Call rawCall = client.newCall(request);
        if (canceled) {
            rawCall.cancel();
        }
        this.rawCall = rawCall;
        // ----------------------------------------------------------------------cgp
        String cacheMode = getCacheMode(request);
        if (!TextUtils.isEmpty(cacheMode)) {
            cacheMode = cacheMode.trim().toLowerCase(Locale.CHINA);
            switch (cacheMode) {
                case CacheMode.LOAD_NETWORK_ELSE_CACHE:// 先网络然后再缓存
                    Response<T> response;
                    try {
                        response = parseResponse(rawCall.execute(), request, true);//如果失败去加载缓存，true 会拋异常
                    } catch (Exception e) {
                        response = execCacheRequest(request);
                    }
                    return response;
                case CacheMode.LOAD_CACHE_ELSE_NETWORK:// 先缓存再网络
                    // ---------------------充缓存中取
                    response = execCacheRequest(request);
                    if (response != null) {
                        return response;
                    }
                    // ---------------------充缓存中取
                    // 如果缓存没有就跳出，执行网络请求
                case CacheMode.LOAD_DEFAULT:
                case CacheMode.LOAD_NETWORK_ONLY:
                default:
                    break;// 直接跳出
            }
        }
        // ----------------------------------------------------------------------cgp
        return parseResponse(rawCall.execute(), request, false);
    }

    // private com.squareup.okhttp.Call createRawCall() {
    // return client.newCall(requestFactory.create(args));
    // }
    public abstract Request createRequest();

    private String getCacheMode(Request request) {
        return request.header("Cache-Mode");
    }

    /**
     * @param rawResponse
     * @param request
     * @param ifFailedToLoadTheCache 如果失败去加载缓存,这里注意返回码的位置
     * @return
     * @throws IOException
     */
    private Response<T> parseResponse(com.squareup.okhttp.Response rawResponse, Request request, boolean ifFailedToLoadTheCache) throws IOException {
        ResponseBody rawBody = rawResponse.body();
        // rawResponse.r
        // Remove the body's source (the only stateful object) so we can pass
        // the response along.
        rawResponse = rawResponse.newBuilder().body(new NoContentResponseBody(rawBody.contentType(), rawBody.contentLength())).build();

        int code = rawResponse.code();
        if (code < 200 || code >= 300) {
            if (!ifFailedToLoadTheCache) {
                try {
                    // Buffer the entire body to avoid future I/O.
                    ResponseBody bufferedBody = Utils.readBodyToBytesIfNecessary(rawBody);
                    return Response.error(bufferedBody, rawResponse);
                } finally {
                    Utils.closeQuietly(rawBody);
                }
            } else {
                throw new IOException("code < 200 || code >= 300");
            }
        }
        if (code == 204 || code == 205) {
            if (!ifFailedToLoadTheCache) {
                return Response.success(null, rawResponse);
            }else{
                throw new IOException("code == 204 || code == 205");
            }
        }

        ExceptionCatchingRequestBody catchingBody = new ExceptionCatchingRequestBody(rawBody);
        try {

            T body;
            if (responseConverter instanceof GsonConverter) {
                GsonConverter<T> converter = (GsonConverter<T>) responseConverter;
                body = converter.fromBody(catchingBody, request);
            } else {
                body = responseConverter.fromBody(catchingBody);
            }
            return Response.success(body, rawResponse);
        } catch (RuntimeException e) {
            // If the underlying source threw an exception, propagate that
            // rather than indicating it was
            // a runtime exception.
            catchingBody.throwIfCaught();
            throw e;
        }
    }

    @Override
    public void cancel() {
        canceled = true;
        com.squareup.okhttp.Call rawCall = this.rawCall;
        if (rawCall != null) {
            rawCall.cancel();
        }
    }

    @Override
    public boolean isCancel() {
        return canceled;
    }
}
