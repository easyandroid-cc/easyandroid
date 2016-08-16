package cc.easyandroid.simple.clean;

import cc.easyandroid.easyclean.UseCase;
import cc.easyandroid.easyclean.UseCaseHandler;
import cc.easyandroid.easyclean.domain.easyhttp.EasyHttpContract;
import cc.easyandroid.easyclean.domain.easyhttp.EasyHttpUseCase;
import cc.easyandroid.easyclean.domain.simple.SimpleContract;
import cc.easyandroid.easyclean.presentation.presenter.EasyHttpPresenter;
import cc.easyandroid.easyclean.presentation.presenter.base.EasyBasePresenter;
import cc.easyandroid.easyclean.repository.EasyHttpRepository;
import cc.easyandroid.easydb.core.EasyDbObject;
import cc.easyandroid.simple.clean.usecase.GetDatasFormDbUseCase;

/**
 * Created by Administrator on 2016/6/3.
 */
public class SimplePresenter1<D1, D2, D3 extends EasyDbObject> extends EasyBasePresenter<SimpleContract.View<D1>> implements SimpleContract.Presenter<D1> {
    private final UseCaseHandler mUseCaseHandler = UseCaseHandler.getInstance();
    EasyHttpPresenter<String> easyHttpPresenter1;
    EasyHttpPresenter<String> easyHttpPresenter2;

    public SimplePresenter1(EasyHttpUseCase<D1> easyHttpUseCase,GetDatasFormDbUseCase<D3> getDatasFormDbUseCase) {
        mGetDatasFormDbUseCase=getDatasFormDbUseCase;
        easyHttpPresenter1 = new EasyHttpPresenter(new EasyHttpUseCase(new EasyHttpRepository()));
        easyHttpPresenter2 = new EasyHttpPresenter(new EasyHttpUseCase(new EasyHttpRepository()));
        setupPresenter1View();
        setupPresenter2View();


    }

    private final GetDatasFormDbUseCase<D3> mGetDatasFormDbUseCase;

    public void exe1(EasyHttpUseCase.RequestValues<D1> requestValues) {
        easyHttpPresenter1.setRequestValues(requestValues);
        easyHttpPresenter1.execute();
    }

    public void exe2(EasyHttpUseCase.RequestValues<D2> requestValues) {
        easyHttpPresenter2.setRequestValues(requestValues);
        easyHttpPresenter2.execute();
    }

    public void exe3(GetDatasFormDbUseCase.RequestValues requestValues) {
        mUseCaseHandler.execute(mGetDatasFormDbUseCase, requestValues, new UseCase.UseCaseCallback<GetDatasFormDbUseCase.ResponseValue<D3>>() {
            @Override
            public void onSuccess(GetDatasFormDbUseCase.ResponseValue<D3> response) {

            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void setupPresenter1View() {
        easyHttpPresenter1.attachView(new EasyHttpContract.View<String>() {
            @Override
            public void onStart(Object tag) {
                getView().setTitle("title");
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

    @Override
    protected void onDetachView() {
        super.onDetachView();
        easyHttpPresenter1.detachView();
        easyHttpPresenter2.detachView();
    }
}
