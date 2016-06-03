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
public class SimplePresenter1<T> extends EasyBasePresenter<SimpleContract.View<T>> implements SimpleContract.Presenter<T> {
    private final UseCaseHandler mUseCaseHandler = UseCaseHandler.getInstance();
    EasyHttpPresenter<String> easyHttpPresenter1;
    EasyHttpPresenter<String> easyHttpPresenter2;

    public SimplePresenter1(EasyHttpUseCase<T> easyHttpUseCase) {
        easyHttpPresenter1 = new EasyHttpPresenter(new EasyHttpUseCase(new EasyHttpRepository()));
        setupPresenter1View();
        setupPresenter2View();

    }

    private void setupPresenter1View() {
        easyHttpPresenter1.attachView(new EasyHttpContract.View<String>() {
            @Override
            public void onStart(Object tag) {

            }

            @Override
            public void onError(Object tag, Throwable e) {

            }

            @Override
            public void onSuccess(Object tag, String results) {

            }
        });
    }

    private void setupPresenter2View() {
        easyHttpPresenter2.attachView(new EasyHttpContract.View<String>() {
            @Override
            public void onStart(Object tag) {

            }

            @Override
            public void onError(Object tag, Throwable e) {

            }

            @Override
            public void onSuccess(Object tag, String results) {

            }
        });
    }

    @Override
    public void start() {

    }

}
