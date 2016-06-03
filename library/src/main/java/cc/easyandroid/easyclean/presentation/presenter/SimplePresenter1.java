package cc.easyandroid.easyclean.presentation.presenter;

import cc.easyandroid.easyclean.UseCaseHandler;
import cc.easyandroid.easyclean.domain.easyhttp.EasyHttpContract;
import cc.easyandroid.easyclean.domain.easyhttp.EasyHttpUseCase;
import cc.easyandroid.easyclean.domain.simple.SimpleContract;
import cc.easyandroid.easyclean.presentation.presenter.base.EasyBasePresenter;
import cc.easyandroid.easyclean.repository.EasyHttpRepository;

/**
 * Created by Administrator on 2016/6/3.
 */
public class SimplePresenter1<T> extends EasyBasePresenter<SimpleContract.View<T>> implements SimpleContract.Presenter<T>,EasyHttpContract.View<String> {
    private final UseCaseHandler mUseCaseHandler = UseCaseHandler.getInstance();
    EasyHttpPresenter<String> easyHttpPresenter;
    public SimplePresenter1(EasyHttpUseCase<T> easyHttpUseCase) {
//        super(easyHttpUseCase);
        easyHttpPresenter=new EasyHttpPresenter(new EasyHttpUseCase(new EasyHttpRepository()));
        easyHttpPresenter.attachView(this);
    }

    @Override
    public void start() {

    }



    @Override
    public void onStart(Object tag) {

    }

    @Override
    public void onError(Object tag, Throwable e) {

    }

    @Override
    public void onSuccess(Object tag, String results) {

    }
}
