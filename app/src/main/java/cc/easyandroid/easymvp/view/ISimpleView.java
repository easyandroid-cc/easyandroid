package cc.easyandroid.easymvp.view;

public interface ISimpleView<T> extends IView {
	
	void onStart(int presenterId);

	void onCompleted(int presenterId);

	void onError(int presenterId, String errorDesc);

	void deliverResult(int presenterId, final T results);

}
