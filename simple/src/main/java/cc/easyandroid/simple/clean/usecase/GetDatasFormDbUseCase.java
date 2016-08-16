package cc.easyandroid.simple.clean.usecase;

import java.util.ArrayList;

import cc.easyandroid.easyclean.UseCase;
import cc.easyandroid.easydb.core.EasyDbObject;
import cc.easyandroid.simple.clean.repository.DbDataSource;
import cc.easyandroid.simple.clean.repository.DbRepository;

/**
 * Created by cgpllx on 2016/8/16.
 */
public class GetDatasFormDbUseCase<T extends EasyDbObject> extends UseCase<GetDatasFormDbUseCase.RequestValues, GetDatasFormDbUseCase.ResponseValue<T>> {
    public final DbRepository dbRepository;

    public GetDatasFormDbUseCase(DbRepository dbRepository) {
        this.dbRepository = dbRepository;
    }

    @Override
    protected void executeUseCase(final RequestValues values) {
        this.dbRepository.getAll(values.getTaskId(), new DbDataSource.LoadDatasCallback<T>() {
            @Override
            public void ondDatasLoaded(ArrayList<T> tasks) {
                ResponseValue responseValue = new ResponseValue(tasks);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError(null);
            }
        });

    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final String mTabeName;

        public RequestValues(String tabeName) {
            mTabeName = tabeName;
        }

        public String getTaskId() {
            return mTabeName;
        }
    }

    public static final class ResponseValue<T extends EasyDbObject> implements UseCase.ResponseValue {

        private ArrayList<T> mList;

        public ResponseValue(ArrayList<T> list) {
            this.mList = list;
        }

        public ArrayList<T> getDatas() {
            return mList;
        }
    }
}