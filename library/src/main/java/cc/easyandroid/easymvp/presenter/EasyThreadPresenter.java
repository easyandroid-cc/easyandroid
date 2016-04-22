package cc.easyandroid.easymvp.presenter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import cc.easyandroid.easymvp.PresenterLoader;
import cc.easyandroid.easymvp.kabstract.KPresenter;
import cc.easyandroid.easymvp.utils.EARunnable;
import cc.easyandroid.easymvp.view.ISimpleThreadView;

public class EasyThreadPresenter<T> extends KPresenter<ISimpleThreadView<T>, T> {
    protected EARunnable<T> eaRunnable;

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
        if (eaRunnable != null && !eaRunnable.isCancel()) {
            eaRunnable.cancel();
        }
    }

    public void execute(final Bundle bundle) {
        cancel();// 先取消之前的事件
        eaRunnable = new EARunnable<T>(mController, mainExecutor) {
            @Override
            public PresenterLoader<T> creatPresenterLoader() {
                return getView().onCreatePresenterLoader(getPresenterId(), bundle);
            }
        };
        ioExecutor.execute(eaRunnable);
    }

    public void execute() {
        execute(null);
    }

    static final String THREAD_PREFIX = "EasyAndroid-";
    static final String IDLE_THREAD_NAME = THREAD_PREFIX + "Idle";

    static Executor ioExecutor() {
        return Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(final Runnable r) {
                return new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                        r.run();
                    }
                }, IDLE_THREAD_NAME);
            }
        });
    }

    public static final Executor ioExecutor = ioExecutor();
    public static final Executor mainExecutor = new MainThreadExecutor();

    Executor defaultCallbackExecutor() {
        return new MainThreadExecutor();
    }

    static class MainThreadExecutor implements Executor {
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable r) {
            handler.post(r);
        }
    }
}
