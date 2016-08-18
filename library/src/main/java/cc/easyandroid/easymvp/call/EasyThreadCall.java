package cc.easyandroid.easymvp.call;

import cc.easyandroid.easycore.EasyCall;
import cc.easyandroid.easycore.EasyExecutor;
import cc.easyandroid.easycore.EasyHttpStateCallback;
import cc.easyandroid.easycore.EasyRunnable;
import cc.easyandroid.easymvp.PresenterLoader;

/**
 *
 */
public abstract class EasyThreadCall<T> implements EasyCall<T>, PresenterLoader<T> {
    protected EasyRunnable easyRunnable;
    private boolean executed; // Guarded by this.
    private volatile boolean canceled;

    public EasyThreadCall() {
    }

    @Override
    public void enqueue(EasyHttpStateCallback<T> callback,String tag) {
        synchronized (this) {
            if (executed)
                throw new IllegalStateException("Already enqueue");
            executed = true;
        }
        EasyRunnable originalRunnable = new EasyRunnable(this, callback);
        this.easyRunnable = originalRunnable;
        EasyExecutor.getThreadExecutor().execute(originalRunnable);
    }

    @Override
    public void cancel() {
        canceled = true;
        if (easyRunnable != null) {
            easyRunnable.cancel();
        }
    }

    @Override
    public boolean isCancel() {
        return canceled;
    }

    public abstract T loadInBackground() throws Exception;
}
