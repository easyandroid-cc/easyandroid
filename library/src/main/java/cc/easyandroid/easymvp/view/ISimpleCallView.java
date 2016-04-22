package cc.easyandroid.easymvp.view;

import android.os.Bundle;

/**
 * 这里的泛型必须填写真实的类型，不能再使用泛型，否则因为泛型的擦除问题导致无法活到到真实的类型
 * 
 * @param <T>
 *            T不能再使用泛型
 */
public interface ISimpleCallView<T> extends ISimpleView<T> {
	cc.easyandroid.easyhttp.core.retrofit.Call<T> onCreateCall(int presenterId, Bundle bundle);
}
