package cc.easyandroid.easymvp.view;

public interface ISimpleView<T> extends IView {
	
	void onStart(Object tag);

	void onCompleted(Object tag);

	/**
	 *使用 e.getMessage() 获取信息
	 * @param tag
	 * @param e
	 */
	void onError(Object tag, Throwable e);

	void deliverResult(Object tag, final T results);

}
