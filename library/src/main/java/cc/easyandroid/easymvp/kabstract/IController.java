package cc.easyandroid.easymvp.kabstract;

public interface IController<T> {

    void start(Object tag);


    void completed(Object tag);


    void error(Object tag, Throwable e);


    void deliverResult(Object tag, final T results);
}
