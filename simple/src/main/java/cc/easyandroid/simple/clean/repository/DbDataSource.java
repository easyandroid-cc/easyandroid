package cc.easyandroid.simple.clean.repository;

/**
 * Created by cgpllx on 2016/8/16.
 */
public interface DbDataSource {

    <T> T getObject(Class<T> clazz);

}
