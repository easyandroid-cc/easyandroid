package cc.easyandroid.easymvp.presenter;

import android.os.Bundle;

import cc.easyandroid.easycore.EasyExecutor;
import cc.easyandroid.easymvp.PresenterLoader;
import cc.easyandroid.easymvp.kabstract.KPresenter;
import cc.easyandroid.easymvp.utils.EARunnable;
import cc.easyandroid.easymvp.view.ISimpleThreadView;

@Deprecated
public class EasyThreadPresenter<T> extends KPresenter<ISimpleThreadView<T>, T> {
    protected EARunnable<T> eaRunnable;

    @Override
    protected void onCancel() {
        super.onCancel();
        cancelRequest();
    }

    @Override
    protected void onDetachView() {
        super.onDetachView();
        cancelRequest();
    }

    private void cancelRequest() {
        if (eaRunnable != null && !eaRunnable.isCancel()) {
            eaRunnable.cancel();
        }
    }

    public void execute(final Bundle bundle) {
        cancel();// 先取消之前的事件
        eaRunnable = new EARunnable<T>(mController, EasyExecutor.getMainExecutor()) {
            @Override
            public PresenterLoader<T> creatPresenterLoader() {
                return getView().onCreatePresenterLoader(getPresenterId(), bundle);
            }
        };
        EasyExecutor.getThreadExecutor().execute(eaRunnable);
    }

    public void execute() {
        execute(null);
    }

}
