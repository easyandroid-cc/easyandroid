package cc.easyandroid.simple.clean.usecase;

import android.support.annotation.NonNull;

import cc.easyandroid.easyclean.UseCase;

/**
 * Created by cgpllx on 2016/8/16.
 */
public class DbUseCase extends UseCase<DbUseCase.RequestValues, DbUseCase.ResponseValue> {


    public DbUseCase(TasksRepository tasksRepository) {
        mTasksRepository = checkNotNull(tasksRepository, "tasksRepository cannot be null!");
    }

    @Override
    protected void executeUseCase(final RequestValues values) {
        mTasksRepository.getTask(values.getTaskId(), new TasksDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                ResponseValue responseValue = new ResponseValue(task);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final String mTaskId;

        public RequestValues(@NonNull String taskId) {
            mTaskId = checkNotNull(taskId, "taskId cannot be null!");
        }

        public String getTaskId() {
            return mTaskId;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private Task mTask;

        public ResponseValue(@NonNull Task task) {
        }

        public Task getTask() {
            return mTask;
        }
    }
}