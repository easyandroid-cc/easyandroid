package cc.easyandroid.easymvp;

public interface PresenterLoader<T> {

	public T loadInBackground() throws Exception;
}
