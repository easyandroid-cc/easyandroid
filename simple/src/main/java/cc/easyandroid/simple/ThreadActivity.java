package cc.easyandroid.simple;

import android.app.Activity;
import android.os.Bundle;

import cc.easyandroid.easycore.EasyCall;
import cc.easyandroid.easymvp.call.EasyThreadCall;
import cc.easyandroid.easymvp.presenter.EasyWorkPresenter;
import cc.easyandroid.easymvp.view.ISimpleCallView;

public class ThreadActivity extends Activity implements ISimpleCallView<String> {
    EasyWorkPresenter<String> presenter = new EasyWorkPresenter<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        presenter.attachView(this);
    }

    @Override
    public EasyCall<String> onCreateCall(int presenterId, Bundle bundle) {
        return new EasyThreadCall<String>() {
            @Override
            public String loadInBackground() throws Exception {
                return "测试";
            }
        };
    }

    @Override
    public void onStart(int presenterId) {

    }

    @Override
    public void onCompleted(int presenterId) {

    }

    @Override
    public void onError(int presenterId, Throwable e) {

    }

    @Override
    public void deliverResult(int presenterId, String results) {
        System.out.println();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
