package cc.easyandroid.simple.clean;

import cc.easyandroid.easyclean.UseCase;
import cc.easyandroid.easyclean.UseCaseHandler;
import cc.easyandroid.easyclean.domain.easyhttp.EasyHttpContract;
import cc.easyandroid.easyclean.domain.easyhttp.EasyHttpUseCase;
import cc.easyandroid.easyclean.presentation.presenter.EasyHttpPresenter;
import cc.easyandroid.easyclean.presentation.presenter.base.EasyBasePresenter;
import cc.easyandroid.easyclean.repository.EasyHttpRepository;
import cc.easyandroid.easydb.core.EasyDbObject;
import cc.easyandroid.simple.clean.usecase.DeleteByIdFromDbUseCase;
import cc.easyandroid.simple.clean.usecase.DeleteDatasFromDbUseCase;
import cc.easyandroid.simple.clean.usecase.GetDatasFromDbUseCase;
import cc.easyandroid.simple.clean.usecase.InsertDataFromDbUseCase;
import cc.easyandroid.simple.clean.usecase.InsertDatasFromDbUseCase;

/**
 * Created by cgpllx on 2016/6/3.
 */
public class SimplePresenter1<D1, D2, D3 extends EasyDbObject> extends EasyBasePresenter<SimpleContract.View<D1>> implements SimpleContract.Presenter<D1> {
    private final UseCaseHandler mUseCaseHandler = UseCaseHandler.getInstance();
    EasyHttpPresenter<String> easyHttpPresenter1;
    EasyHttpPresenter<String> easyHttpPresenter2;

    public SimplePresenter1(DeleteByIdFromDbUseCase deleteByIdFromDbUseCase,//
                            DeleteDatasFromDbUseCase deleteDatasFromDbUseCase,//
                            GetDatasFromDbUseCase<D3> getDatasFromDbUseCase,//
                            InsertDataFromDbUseCase<D3> insertDataFromDbUseCase,//
                            InsertDatasFromDbUseCase<D3> insertDatasFromDbUseCase) {
        mDeleteByIdFromDbUseCase = deleteByIdFromDbUseCase;
        mDeleteDatasFromDbUseCase = deleteDatasFromDbUseCase;
        mGetDatasFromDbUseCase = getDatasFromDbUseCase;
        mInsertDataFromDbUseCase = insertDataFromDbUseCase;
        mInsertDatasFromDbUseCase = insertDatasFromDbUseCase;
        easyHttpPresenter1 = new EasyHttpPresenter(new EasyHttpUseCase(new EasyHttpRepository()));
        easyHttpPresenter2 = new EasyHttpPresenter(new EasyHttpUseCase(new EasyHttpRepository()));
        setupPresenter1View();
        setupPresenter2View();
    }

    private final DeleteByIdFromDbUseCase mDeleteByIdFromDbUseCase;
    private final DeleteDatasFromDbUseCase mDeleteDatasFromDbUseCase;
    private final GetDatasFromDbUseCase<D3> mGetDatasFromDbUseCase;
    private final InsertDataFromDbUseCase<D3> mInsertDataFromDbUseCase;
    private final InsertDatasFromDbUseCase<D3> mInsertDatasFromDbUseCase;

    public void exe1(EasyHttpUseCase.RequestValues<D1> requestValues) {
        easyHttpPresenter1.setRequestValues(requestValues);
        easyHttpPresenter1.execute();
    }

    public void exe2(EasyHttpUseCase.RequestValues<D2> requestValues) {
        easyHttpPresenter2.setRequestValues(requestValues);
        easyHttpPresenter2.execute();
    }

    public void exe3(DeleteByIdFromDbUseCase.RequestValues requestValues) {
        mUseCaseHandler.execute(mDeleteByIdFromDbUseCase, requestValues, new UseCase.UseCaseCallback<DeleteByIdFromDbUseCase.ResponseValue>() {
            @Override
            public void onSuccess(DeleteByIdFromDbUseCase.ResponseValue response) {

            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }

    public void exe4(DeleteDatasFromDbUseCase.RequestValues requestValues) {
        mUseCaseHandler.execute(mDeleteDatasFromDbUseCase, requestValues, new UseCase.UseCaseCallback<DeleteDatasFromDbUseCase.ResponseValue>() {
            @Override
            public void onSuccess(DeleteDatasFromDbUseCase.ResponseValue response) {

            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }

    public void exe5(GetDatasFromDbUseCase.RequestValues requestValues) {
        mUseCaseHandler.execute(mGetDatasFromDbUseCase, requestValues, new UseCase.UseCaseCallback<GetDatasFromDbUseCase.ResponseValue<D3>>() {
            @Override
            public void onSuccess(GetDatasFromDbUseCase.ResponseValue<D3> response) {

            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }

    public void exe6(InsertDataFromDbUseCase.RequestValues requestValues) {
        mUseCaseHandler.execute(mInsertDataFromDbUseCase, requestValues, new UseCase.UseCaseCallback<InsertDataFromDbUseCase.ResponseValue>() {
            @Override
            public void onSuccess(InsertDataFromDbUseCase.ResponseValue response) {

            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }

    public void exe7(InsertDatasFromDbUseCase.RequestValues requestValues) {
        mUseCaseHandler.execute(mInsertDatasFromDbUseCase, requestValues, new UseCase.UseCaseCallback<InsertDatasFromDbUseCase.ResponseValue>() {
            @Override
            public void onSuccess(InsertDatasFromDbUseCase.ResponseValue response) {

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
