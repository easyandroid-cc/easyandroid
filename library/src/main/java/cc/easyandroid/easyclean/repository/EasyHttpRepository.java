package cc.easyandroid.easyclean.repository;

import java.lang.reflect.Type;

import cc.easyandroid.easycore.EasyCall;
import cc.easyandroid.easycore.EasyHttpStateCallback;
import cc.easyandroid.easycore.EasyResponse;
import cc.easyandroid.easyhttp.EasyHttpUtils;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/6/3.
 */
public class EasyHttpRepository implements EasyHttpDataSource {

    public <T> void executeRequest(EasyCall<T> easyCall, EasyHttpStateCallback<T> callback) {
        easyCall.enqueue(callback);
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

            @Override
            public void start() {

            }
        });
        return easyCall;
    }
}
