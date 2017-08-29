package cc.easyandroid.easyhttp.retrofit2;

import java.io.File;
import java.io.IOException;

import cc.easyandroid.easycore.EAResult;
import cc.easyandroid.easycore.EasyCall;
import cc.easyandroid.easycore.EasyExecutor;
import cc.easyandroid.easycore.EasyHttpStateCallback;
import cc.easyandroid.easycore.EasyResponse;
import cc.easyandroid.easyhttp.core.Utils;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * RetrofitCall 转换为EasyCall
 */
public class RetrofitCallToDownloadEasyCall<T> implements EasyCall<RetrofitCallToDownloadEasyCall.EasyDownLoadResult> {
    private final retrofit2.Call rawCall;
    private boolean executed; // Guarded by this.
    private File file; // Guarded by this.
    private volatile boolean canceled;

    public RetrofitCallToDownloadEasyCall(retrofit2.Call call, File file) {
        this.rawCall = call;
        this.file = file;
    }

    @Override
    public void enqueue(final EasyHttpStateCallback<RetrofitCallToDownloadEasyCall.EasyDownLoadResult> callback, String tag) {
        synchronized (this) {
            if (executed)
                throw new IllegalStateException("Already enqueue");
            executed = true;
        }
        exeRequest(callback, tag);
    }

    /**
     * @param callback
     */
    private void exeRequest(final EasyHttpStateCallback<EasyDownLoadResult> callback, String tag) {
        if (canceled) {
            rawCall.cancel();
            return;
        }
        if (rawCall instanceof ICacheModeInject) {//
            ICacheModeInject cacheModeInject = (ICacheModeInject) rawCall;
            cacheModeInject.setCacheMode(tag);//将手动设置的缓存模式传人
        }
        rawCall.enqueue(new retrofit2.Callback<T>() {
            private void callFailure(final Throwable e) {
                e.printStackTrace();
                EasyExecutor.getMainExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(e);
                    }
                });
            }

            private void callSuccess(final EasyResponse<RetrofitCallToDownloadEasyCall.EasyDownLoadResult> easyResponse) {
                EasyExecutor.getMainExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onResponse(easyResponse);
                    }
                });
            }

            @Override
            public void onFailure(retrofit2.Call call, Throwable e) {//thread run
                e.printStackTrace();
                if (canceled) {
                    return;
                }
                callFailure(e);
            }

            @Override
            public void onResponse(retrofit2.Call<T> rawCall, retrofit2.Response<T> rawResponse) {
                if (canceled) {
                    return;
                }
                try {
                    T rawBody = rawResponse.body();
                    ResponseBody responseBody = (ResponseBody) rawBody;
                    EasyResponse<EasyDownLoadResult> easyResponse ;
                    if (rawResponse.isSuccessful()) {
                        boolean writtenToDisk = Utils.writeResponseBodyToDisk(responseBody, file, RetrofitCallToDownloadEasyCall.this);
                        easyResponse = EasyResponse.success(EasyDownLoadResult.createDownLoadResult(writtenToDisk));
                    } else {
                        easyResponse = EasyResponse.error(-1, " not isSuccessful");
                    }
                    if (canceled) {
                        return;
                    }
                    callSuccess(easyResponse);
                } catch (Throwable e) {
                    e.printStackTrace();
                    callFailure(e);
                    return;
                }
            }
        });
    }

    @Deprecated
    public EasyResponse<T> execute() throws IOException {
        //not use
        return null;
    }

    @Override
    public void cancel() {
        canceled = true;
        retrofit2.Call rawCall = this.rawCall;
        if (rawCall != null) {
            rawCall.cancel();
        }
    }

    @Override
    public boolean isCancel() {
        return canceled;
    }

    @Override
    public EasyCall<RetrofitCallToDownloadEasyCall.EasyDownLoadResult> clone() {
        return new RetrofitCallToDownloadEasyCall<>(rawCall.clone(),file);
    }

    @Override
    public boolean isExecuted() {
        return executed;
    }

    @Override
    public Request request() {
        return rawCall.request();
    }
    public static class EasyDownLoadResult implements EAResult {
        private boolean success;

        public void setSuccess(boolean success) {
            this.success = success;
        }

        @Override
        public boolean isSuccess() {
            return success;
        }

        @Override
        public String getEADesc() {
            return "";
        }

        @Override
        public String getEACode() {
            return "";
        }

        public static EasyDownLoadResult createDownLoadResult(boolean success) {
            EasyDownLoadResult downLoadResult = new EasyDownLoadResult();
            downLoadResult.setSuccess(success);
            return downLoadResult;
        }
    }
}