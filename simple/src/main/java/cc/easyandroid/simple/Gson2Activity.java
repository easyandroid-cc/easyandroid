package cc.easyandroid.simple;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import cc.easyandroid.easyclean.domain.easyhttp.EasyHttpContract;
import cc.easyandroid.easyclean.domain.easyhttp.EasyHttpUseCase;
import cc.easyandroid.easyclean.presentation.presenter.EasyHttpPresenter;
import cc.easyandroid.easyclean.repository.EasyHttpRepository;
import cc.easyandroid.easycore.EasyCall;
import cc.easyandroid.easyutils.EasyToast;
import cc.easyandroid.easyutils.TypeUtils;
import cc.easyandroid.simple.pojo.PagingResult;
import cc.easyandroid.simple.pojo.PriceInfo;
import cc.easyandroid.simple.pojo.QfangResult;

public class Gson2Activity extends Activity implements EasyHttpContract.View<QfangResult<PagingResult<PriceInfo>>> {
    EasyHttpPresenter<QfangResult<PagingResult<PriceInfo>>> presenter = new EasyHttpPresenter(new EasyHttpUseCase(new EasyHttpRepository()));
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_string);
        textView = (TextView) findViewById(R.id.text);
        presenter.attachView(this);
        EasyCall<QfangResult<PagingResult<PriceInfo>>> easyCall = HttpUtils.creatGetCall(url, TypeUtils.newInstance(this).getViewType());
        presenter.execute(new EasyHttpUseCase.RequestValues("测试1", easyCall));
    }

    String url = "http://hk.qfang.com/qfang-api/mobile/common/query/querySalePriceCondition";


    @Override
    public void onStart(Object presenterId) {
        EasyToast.showShort(getApplicationContext(), "onStart");
        String ddd = (String) presenterId;
        System.out.println("cgp onStart tag=" + ddd);
    }

    @Override
    public void onError(Object presenterId, Throwable e) {
        textView.setText("出错" + e.getMessage());
        EasyToast.showShort(getApplicationContext(), "onError" + e.getMessage());
        System.out.println("cgp onError tag=" + presenterId);
    }

    @Override
    public void onSuccess(Object presenterId, QfangResult<PagingResult<PriceInfo>> results) {
        List<PriceInfo> lists = results.getData().getList();
        textView.setText(presenterId.toString() + "==" + lists);
        System.out.println("cgp deliverResult tag=" + presenterId);
        ;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
