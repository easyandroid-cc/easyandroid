package cc.easyandroid.easyhttp.call;

import android.os.*;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import cc.easyandroid.easycore.EasyCall;
import cc.easyandroid.easycore.EasyHttpStateCallback;
import cc.easyandroid.easyhttp.core.EasyRunnable;
import cc.easyandroid.easyhttp.core.retrofit.EasyResponse;
import cc.easyandroid.easymvp.PresenterLoader;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

/**
 *
 */
public class EasyThreadCall<T> implements EasyCall<T> {
    protected final PresenterLoader<T> loader;
    protected EasyRunnable easyRunnable;

    public EasyThreadCall(PresenterLoader<T> loader) {
        this.loader = loader;
    }

    public static final Executor threadExecutor = defaultHttpExecutor();
    public static final Executor mainCallbackExecutor = new MainThreadExecutor();

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
        this.easyRunnable = new EasyRunnable(loader, callback, mainCallbackExecutor);
        threadExecutor.execute(this.easyRunnable);
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
        return easyRunnable.isCancel();
    }

    static Executor defaultHttpExecutor() {
        return Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(final Runnable r) {
                return new Thread(new Runnable() {
                    @Override
                    public void run() {
                        android.os.Process.setThreadPriority(THREAD_PRIORITY_BACKGROUND);
                        r.run();
                    }
                }, IDLE_THREAD_NAME);
            }
        });
    }

    static final String THREAD_PREFIX = "EasyAndroid-";
    static final String IDLE_THREAD_NAME = THREAD_PREFIX + "Idle";
}
