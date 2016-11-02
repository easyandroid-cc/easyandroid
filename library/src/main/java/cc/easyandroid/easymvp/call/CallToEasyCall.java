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
package cc.easyandroid.easymvp.call;

import android.text.TextUtils;

import java.io.IOException;

import cc.easyandroid.easycache.EasyHttpCache;
import cc.easyandroid.easycore.EasyCall;
import cc.easyandroid.easycore.EasyExecutor;
import cc.easyandroid.easycore.EasyHttpStateCallback;
import cc.easyandroid.easycore.EasyResponse;
import cc.easyandroid.easyhttp.core.CacheMode;
import cc.easyandroid.easyhttp.core.Utils;
import cc.easyandroid.easyhttp.core.converter.Converter;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;

public class CallToEasyCall<T> implements EasyCall<T> {
    protected final Converter<T> responseConverter;
    private final okhttp3.Call rawCall;
    private boolean executed; // Guarded by this.
    private volatile boolean canceled;

    public CallToEasyCall(okhttp3.Call call, Converter<T> responseConverter) {
        this.rawCall = call;
        this.responseConverter = responseConverter;
    }

    private EasyResponse<T> execCacheRequest(Request request) {
        try {
            ResponseBody responseBody = EasyHttpCache.getInstance().get(request);
            if (responseBody == null) {
                return null;
            }
            okhttp3.Response rawResponse = new okhttp3.Response.Builder()//
                    .code(200).request(request).protocol(Protocol.HTTP_1_1).body(responseBody).build();
            return parseResponse(rawResponse, request, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void enqueue(final EasyHttpStateCallback<T> callback, String tag) {
        synchronized (this) {
            if (executed)
                throw new IllegalStateException("Already enqueue");
            executed = true;
        }
        final Request request = rawCall.request();
        String cacheMode = getCacheMode(request);
        // ----------------------------------------------------------------------cgp
        if (!TextUtils.isEmpty(cacheMode)) {
            switch (cacheMode) {
                case CacheMode.LOAD_NETWORK_ELSE_CACHE:// 先网络然后再缓存
                    exeRequest(callback, request, true);
                    return;
                case CacheMode.LOAD_CACHE_ELSE_NETWORK:// 先缓存再网络
                    // ---------------------从缓存中取
                    EasyExecutor.getThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            final EasyResponse<T> easyResponse = execCacheRequest(request);
                            if (easyResponse == null) {
                                exeRequest(callback, request, false);
                            } else {
                                EasyExecutor.getMainExecutor().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onResponse(easyResponse);
                                    }
                                });
                            }
                        }
                    });
                    return;
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

    private void exeRequest(final EasyHttpStateCallback<T> callback, final Request request, final boolean loadnetElseCache) {
        if (canceled) {
            rawCall.cancel();
            return;
        }
        rawCall.enqueue(new okhttp3.Callback() {
            private void callFailure(final Throwable e) {
                e.printStackTrace();
                EasyExecutor.getMainExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(e);
                    }
                });
            }

            private void callSuccess(final EasyResponse<T> easyResponse) {
                EasyExecutor.getMainExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onResponse(easyResponse);
                    }
                });
            }

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {//thread run
                e.printStackTrace();
                if (canceled) {
                    return;
                }
                //没有网络会进入onFailure
                if (loadnetElseCache) {
                    final EasyResponse<T> wrapper = execCacheRequest(request);//这里走缓存
                    EasyExecutor.getMainExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            callback.onResponse(wrapper);
                        }
                    });
                    return;
                }
                callFailure(e);
            }

            @Override
            public void onResponse(okhttp3.Call rawCall, okhttp3.Response rawResponse) {
                if (canceled) {
                    return;
                }
                EasyResponse<T> easyResponse;
                try {
                    easyResponse = parseResponse(rawResponse, request, loadnetElseCache, true);
                } catch (Throwable e) {
                    if (loadnetElseCache) {
                        e.printStackTrace();
                        onFailure(rawCall, new IOException("解析结果错误"));
                        return;
                    }
                    callFailure(e);
                    return;
                }
                callSuccess(easyResponse);
            }
        });
    }

    @Deprecated
    public EasyResponse<T> execute() throws IOException {

        return null;
    }

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
    private EasyResponse<T> parseResponse(okhttp3.Response rawResponse, Request request, boolean ifFailedToLoadTheCache, boolean fromNetWork) throws IOException {
        ResponseBody rawBody = rawResponse.body();
        // rawResponse.r
        // Remove the body's source (the only stateful object) so we can pass
        // the response along.
        rawResponse = rawResponse.newBuilder().body(new NoContentResponseBody(rawBody.contentType(), rawBody.contentLength())).build();

        int code = rawResponse.code();
        if (code < 200 || code >= 300) {
            if (!ifFailedToLoadTheCache) {
                try {
                    return EasyResponse.error(code, rawResponse.message());
                } finally {
                    Utils.closeQuietly(rawBody);
                }
            } else {
                throw new IOException("code < 200 || code >= 300  code=" + code);
            }
        }
        if (code == 204 || code == 205) {
            if (!ifFailedToLoadTheCache) {
                return EasyResponse.success(null);
            } else {
                throw new IOException("code == 204 || code == 205");
            }
        }

        ExceptionCatchingRequestBody catchingBody = new ExceptionCatchingRequestBody(rawBody);
        try {
            T body = responseConverter.fromBody(catchingBody, request, fromNetWork);
            return EasyResponse.success(body);
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
        okhttp3.Call rawCall = this.rawCall;
        if (rawCall != null) {
            rawCall.cancel();
        }
    }

    @Override
    public boolean isCancel() {
        return canceled;
    }

    @Override
    public EasyCall<T> clone() {
        return new CallToEasyCall<>(rawCall, responseConverter);
    }

    @Override
    public boolean isExecuted() {
        return executed;
    }

    @Override
    public Request request() {
        return rawCall.request();
    }

    static final class ExceptionCatchingRequestBody extends ResponseBody {
        private final ResponseBody delegate;
        IOException thrownException;

        ExceptionCatchingRequestBody(ResponseBody delegate) {
            this.delegate = delegate;
        }

        @Override
        public MediaType contentType() {
            return delegate.contentType();
        }

        @Override
        public long contentLength() {
            return delegate.contentLength();
        }

        @Override
        public BufferedSource source() {
            return Okio.buffer(new ForwardingSource(delegate.source()) {
                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    try {
                        return super.read(sink, byteCount);
                    } catch (IOException e) {
                        thrownException = e;
                        throw e;
                    }
                }
            });
        }

        @Override
        public void close() {
            delegate.close();
        }

        void throwIfCaught() throws IOException {
            if (thrownException != null) {
                throw thrownException;
            }
        }
    }

    static final class NoContentResponseBody extends ResponseBody {
        private final MediaType contentType;
        private final long contentLength;

        NoContentResponseBody(MediaType contentType, long contentLength) {
            this.contentType = contentType;
            this.contentLength = contentLength;
        }

        @Override
        public MediaType contentType() {
            return contentType;
        }

        @Override
        public long contentLength() {
            return contentLength;
        }

        @Override
        public BufferedSource source() {
            throw new IllegalStateException("Cannot read raw response body of a converted body.");
        }
    }
}
