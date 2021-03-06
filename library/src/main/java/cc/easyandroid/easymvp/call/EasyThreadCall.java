package cc.easyandroid.easymvp.call;

import cc.easyandroid.easycore.EasyCall;
import cc.easyandroid.easycore.EasyExecutor;
import cc.easyandroid.easycore.EasyHttpStateCallback;
import cc.easyandroid.easycore.EasyRunnable;
import cc.easyandroid.easymvp.PresenterLoader;
import okhttp3.Request;

/**
 *
 */
public class EasyThreadCall<T> implements EasyCall<T> {
    protected EasyRunnable easyRunnable;
    private boolean executed; // Guarded by this.
    private volatile boolean canceled;
    private final PresenterLoader<T> loader;

    public EasyThreadCall(PresenterLoader<T> loader) {
        this.loader = loader;
    }

    @Override
    public void enqueue(EasyHttpStateCallback<T> callback, String tag) {
        synchronized (this) {
            if (executed)
                throw new IllegalStateException("Already enqueue");
            executed = true;
        }
        EasyRunnable originalRunnable = new EasyRunnable(loader, callback);
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

    @Override
    public EasyCall<T> clone() {
        return new EasyThreadCall(loader);
    }

    @Override
    public boolean isExecuted() {
        return executed;
    }

    @Override
    public Request request() {//网络请求才用到
        return null;
    }
}
