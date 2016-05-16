package cc.easyandroid.easymvp.kabstract;

public interface IController<T> {
    /**
     * @hide
     */
    void start(Object tag);

    /**
     * @hide
     */
    void completed(Object tag);

    /**
     * @hide
     */
    void error(Object tag, Throwable e);

    /**
     * @hide
     */
    void deliverResult(Object tag, final T results);
}
