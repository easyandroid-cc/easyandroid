package cc.easyandroid.easymvp.presenter;

import android.os.Bundle;
import android.support.v4.content.Loader;
import cc.easyandroid.easymvp.kabstract.KLoaderPresenterAbstract;
import cc.easyandroid.easymvp.view.ISimpleLoaderView;

/**
 * loader线程的返回值
 * 
 * @author Administrator
 *
 * @param <T>
 */
public class EasyLoaderPresenter<T> extends KLoaderPresenterAbstract<ISimpleLoaderView<T>, T> {

	@Override
	public Loader<T> onCreateLoader(int arg0, Bundle bundle) {
		mController.start(arg0);
		return getView().onCreateLoader(arg0, bundle);
	}

}
 