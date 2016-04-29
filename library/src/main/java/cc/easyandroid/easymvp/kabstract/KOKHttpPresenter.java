package cc.easyandroid.easymvp.kabstract;

import android.os.Bundle;
import android.util.Log;

import cc.easyandroid.easycore.EasyCall;
import cc.easyandroid.easycore.EasyHttpStateCallback;
import cc.easyandroid.easyhttp.core.retrofit.EasyResponse;
import cc.easyandroid.easycore.EAResult;
import cc.easyandroid.easymvp.exception.MvpException;
import cc.easyandroid.easymvp.view.ISimpleView;

public abstract class KOKHttpPresenter<V extends ISimpleView<T>, T> extends KPresenter<V, T> {
    protected EasyCall<T> easyCall;

    @Override
    protected void onCancel() {
        super.onCancel();
        cancelRequest();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelRequest();
    }

    private void cancelRequest() {
        if (easyCall != null) {
            easyCall.cancel();
        }
    }

    protected abstract EasyCall<T> createCall(Bundle bundle);

    public void execute(Bundle bundle) {
        cancel();// 先取消之前的事件
        EasyCall<T> originalEasyCall = createCall(bundle);
        if (originalEasyCall == null) {
            throw new IllegalArgumentException("please Override onCreateCall method, And can not be null，");
        }
        easyCall = originalEasyCall.clone();

        easyCall.enqueue(new kEasyHttpStateCallback(mController));

    }

    public void execute() {
        execute(null);
    }

    public class kEasyHttpStateCallback implements EasyHttpStateCallback<T> {
        IController<T> mController;

        public kEasyHttpStateCallback(IController<T> controller) {
            this.mController = controller;
        }

        @Override
        public void onResponse(EasyResponse<T> easyResponse) {
            T t = easyResponse != null ? easyResponse.body() : null;
            String defaultMessage = "";//"服务器或网络异常";
            if (t == null) {
                Log.e("EasyAndroid", "t==null");
                error(defaultMessage);
                return;
            } else if (t instanceof EAResult) {
                EAResult kResult = (EAResult) t;
                if (kResult == null || !kResult.isSuccess()) {
                    String errorMessage = kResult != null ? kResult.getEADesc() : defaultMessage;
                    error(errorMessage);
                    return;
                }
            }
            mController.deliverResult(t);
            mController.completed();
        }

        private void error(String errorMessage) {
            MvpException mvpException = new MvpException(errorMessage);
            onFailure(mvpException);
        }

        @Override
        public void onFailure(Throwable t) {
            mController.error(t);
        }

        @Override
        public void onstart() {
            mController.start();
        }
    }
}
