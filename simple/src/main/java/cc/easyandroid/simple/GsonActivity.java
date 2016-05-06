package cc.easyandroid.simple;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import cc.easyandroid.easycore.EasyCall;
import cc.easyandroid.easymvp.presenter.EasyWorkPresenter;
import cc.easyandroid.easymvp.view.ISimpleCallView;
import cc.easyandroid.easyutils.EasyToast;
import cc.easyandroid.simple.pojo.PagingResult;
import cc.easyandroid.simple.pojo.PriceInfo;
import cc.easyandroid.simple.pojo.QfangResult;

public class GsonActivity extends Activity implements ISimpleCallView<QfangResult<PagingResult<PriceInfo>>> {
    EasyWorkPresenter<QfangResult<PagingResult<PriceInfo>>> presenter = new EasyWorkPresenter<>();
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_string);
        textView = (TextView) findViewById(R.id.text);
        presenter.attachView(this);
        presenter.execute();
    }

    String url = "http://hk.qfang.com/qfang-api/mobile/common/query/querySalePriceCondition";

    @Override
    public EasyCall<QfangResult<PagingResult<PriceInfo>>> onCreateCall(int presenterId, Bundle bundle) {
        return HttpUtils.creatGetCall(url, presenter);
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
        textView.setText("出错" + e.getMessage());
        EasyToast.showShort(getApplicationContext(), "onError" + e.getMessage());
    }

    @Override
    public void deliverResult(int presenterId, QfangResult<PagingResult<PriceInfo>> results) {
        List<PriceInfo> lists = results.getData().getList();
        textView.setText(lists.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
