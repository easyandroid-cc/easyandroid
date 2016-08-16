package cc.easyandroid.simple.clean.repository;

import java.util.ArrayList;

/**
 * Created by cgpllx on 2016/8/16.
 */
public interface DbDataSource {

    <T> T getObject(Class<T> clazz);
    interface LoadDatasCallback<T> {

        void ondDatasLoaded(ArrayList<T> tasks);

        void onDataNotAvailable();
    }

//    interface GetTaskCallback {
//
//        void onTaskLoaded(Task task);
//
//        void onDataNotAvailable();
//    }
}
