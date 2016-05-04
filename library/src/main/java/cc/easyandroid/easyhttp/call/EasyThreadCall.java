package cc.easyandroid.easyhttp.call;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.concurrent.Executor;

import cc.easyandroid.easycore.EasyCall;
import cc.easyandroid.easycore.EasyExecutor;
import cc.easyandroid.easycore.EasyHttpStateCallback;
import cc.easyandroid.easyhttp.core.EasyRunnable;
import cc.easyandroid.easyhttp.core.retrofit.EasyResponse;
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


    static class MainThreadExecutor implements Executor {
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable r) {
            handler.post(r);
        }
    }

    @Override
    public EasyResponse<T> execute() throws IOException {
        // 暂时不实现这个方法
        return null;
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
