package cc.easyandroid.simple;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import cc.easyandroid.easyhttp.core.retrofit.Call;
import cc.easyandroid.easymvp.presenter.EasyWorkPresenter;
import cc.easyandroid.easymvp.view.ISimpleCallView;
import cc.easyandroid.easyutils.EasyToast;

public class StringActivity extends Activity implements ISimpleCallView<String> {
    EasyWorkPresenter<String> presenter = new EasyWorkPresenter<>();
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_string);
        textView = (TextView) findViewById(R.id.text);
        presenter.attachView(this);
        presenter.execute();
    }

    @Override
    public Call<String> onCreateCall(int presenterId, Bundle bundle) {
        return HttpUtils.creatGetCall("http://www.baidu.com", presenter);
    }

    @Override
    public void onStart(int presenterId) {
        EasyToast.showShort(getApplicationContext(), "onStart");
    }

    @Override
    public void onCompleted(int presenterId) {
        EasyToast.showShort(getApplicationContext(), "onCompleted");
    }

    @Override
    public void onError(int presenterId, Throwable e) {
        textView.setText("出错"+e.getMessage());
        EasyToast.showShort(getApplicationContext(), "onError" + e.getMessage());
    }

    @Override
    public void deliverResult(int presenterId, String results) {
        textView.setText(results);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }
}
