package cc.easyandroid.easymvp.view;

/**
 * 这里的泛型必须填写真实的类型，不能再使用泛型，否则因为泛型的擦除问题导致无法活到到真实的类型
 *
 * @param <T> T不能再使用泛型
 */
public interface ISimplePregressCallView<T> extends ISimpleThreadView<T> {

    void onProgressUpdate(Integer... values);
}
