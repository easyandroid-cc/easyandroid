package cc.easyandroid.easymvp.kabstract;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import cc.easyandroid.easymvp.view.ISimpleView;

public abstract class KLoaderPresenterAbstract<V extends ISimpleView<T>, T> extends KPresenter<V, T> implements LoaderCallbacks<T> {

	public void loadData(LoaderManager loaderManager, Bundle bundle) {
		loaderManager.initLoader(getPresenterId(), bundle, this);
	}

	public void restartLoadData(LoaderManager loaderManager, Bundle bundle) {
		loaderManager.restartLoader(getPresenterId(), bundle, this);
	}

	@Override
	public void onLoadFinished(Loader<T> arg0, T arg1) {
		mController.deliverResult(arg1);
		mController.completed();
	}

	@Override
	public void onLoaderReset(Loader<T> arg0) {

	}

	public void destroyData(LoaderManager loaderManager) {
		loaderManager.destroyLoader(getPresenterId());
//		destroy();
		detachView();
	}
}
