package cc.easyandroid.simple.clean.usecase;

import java.util.ArrayList;

import cc.easyandroid.easyclean.UseCase;
import cc.easyandroid.easydb.core.EasyDbObject;
import cc.easyandroid.simple.clean.repository.DbDataSource;
import cc.easyandroid.simple.clean.repository.DbRepository;

/**
 * Created by cgpllx on 2016/8/16.
 */
public class DeleteDatasFormDbUseCase<T extends EasyDbObject> extends UseCase<DeleteDatasFormDbUseCase.RequestValues, DeleteDatasFormDbUseCase.ResponseValue> {
    public final DbRepository dbRepository;

    public DeleteDatasFormDbUseCase(DbRepository dbRepository) {
        this.dbRepository = dbRepository;
    }

    @Override
    protected void executeUseCase(final RequestValues values) {
        boolean seccess = this.dbRepository.deleteAll(values.getTabeName());
        if (seccess) {
            getUseCaseCallback().onSuccess(new ResponseValue(true));
        } else {
            getUseCaseCallback().onError(null);
        }

    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final String mTabeName;

        public RequestValues(String tabeName) {
            mTabeName = tabeName;
        }

        public String getTabeName() {
            return mTabeName;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private boolean seccess;

        public ResponseValue(boolean seccess) {
            this.seccess = seccess;
        }

        public boolean getDatas() {
            return seccess;
        }
    }
}