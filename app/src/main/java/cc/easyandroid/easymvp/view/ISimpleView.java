package cc.easyandroid.easymvp.view;

public interface ISimpleView<T> extends IView {
	
	void onStart(int presenterId);

	void onCompleted(int presenterId);

	/**
	 *使用 e.getMessage() 获取信息
	 * @param presenterId
	 * @param e
	 */
	void onError(int presenterId, Throwable e);

	void deliverResult(int presenterId, final T results);

}
