package cc.easyandroid.easyclean.repository;

import java.io.IOException;
import java.lang.reflect.Type;

import cc.easyandroid.easycore.EasyCall;
import cc.easyandroid.easycore.EasyHttpStateCallback;
import cc.easyandroid.easycore.EasyResponse;
import cc.easyandroid.easyhttp.EasyHttpUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * http的数据仓库，其实数据最终来自Model也就是这里的easycall
 */
public class EasyWorkRepository implements EasyWorkDataSource {

    public <T> void executeRequest(EasyCall<T> easyCall, final HttpRequestCallback<T> callback) {
        easyCall.enqueue(new EasyHttpStateCallback<T>() {
            @Override
            public void onResponse(EasyResponse<T> easyResponse) {
                callback.onResponse(easyResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onFailure(t);
            }
        }, null);
    }

    public <T> void executeRequest(EasyCall<T> easyCall, final HttpRequestCallback<T> callback, String cacheMode) {
        easyCall.enqueue(new EasyHttpStateCallback<T>() {
            @Override
            public void onResponse(EasyResponse<T> easyResponse) {
                callback.onResponse(easyResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onFailure(t);
            }
        }, cacheMode);
    }

    @Override
    public <T> EasyCall<T> executeRequest(Request request, Type type, final HttpRequestCallback<T> callback) {
        EasyCall<T> easyCall = EasyHttpUtils.getInstance().executeHttpRequestToCall(request, type);
        easyCall.enqueue(new EasyHttpStateCallback<T>() {
            @Override
            public void onResponse(EasyResponse<T> easyResponse) {
                callback.onResponse(easyResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onFailure(t);
            }
        }, null);
        return easyCall;
    }

    public void executeCall(okhttp3.Call call) {
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }
}
