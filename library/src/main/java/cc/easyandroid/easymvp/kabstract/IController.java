package cc.easyandroid.easymvp.kabstract;

public interface IController<T> {
	/**
     * @hide
     */
	void start();
	/**
     * @hide
     */
	void completed();
	/**
     * @hide
     */
	void error(Throwable e);
	/**
     * @hide
     */
	void deliverResult(final T results);
}
