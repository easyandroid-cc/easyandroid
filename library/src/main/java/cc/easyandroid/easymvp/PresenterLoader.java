package cc.easyandroid.easymvp;

public interface PresenterLoader<T> {
    T loadInBackground() throws Exception;
}
