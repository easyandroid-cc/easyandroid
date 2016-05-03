package cc.easyandroid.easymvp.view;

/**
 *
 * @param <T> T不能再使用泛型
 */
public interface ISimplePregressCallView<T> extends ISimpleThreadView<T> {

    void onProgressUpdate(Integer... values);
}
