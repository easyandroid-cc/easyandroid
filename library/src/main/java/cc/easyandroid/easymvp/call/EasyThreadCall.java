package cc.easyandroid.easymvp.call;

import cc.easyandroid.easycore.EasyCall;
import cc.easyandroid.easycore.EasyExecutor;
import cc.easyandroid.easycore.EasyHttpStateCallback;
import cc.easyandroid.easyhttp.core.EasyRunnable;
import cc.easyandroid.easymvp.PresenterLoader;

/**
 *
 */
public class EasyThreadCall<T> implements EasyCall<T> {
    protected final PresenterLoader<T> loader;
    protected EasyRunnable easyRunnable;

    public EasyThreadCall(PresenterLoader<T> loader) {
        this.loader = loader;
    }

    @Override
    public void enqueue(EasyHttpStateCallback<T> callback) {
        this.easyRunnable = new EasyRunnable(loader, callback, EasyExecutor.getMainExecutor());
        EasyExecutor.getThreadExecutor().execute(this.easyRunnable);
    }

    @Override
    public void cancel() {
        easyRunnable.cancel();
    }

    @Override
    public EasyCall<T> clone() {
        return new EasyThreadCall(loader);
    }

    @Override
    public boolean isCancel() {
        if (easyRunnable != null) {
            return easyRunnable.isCancel();
        }
        return true;
    }

}
