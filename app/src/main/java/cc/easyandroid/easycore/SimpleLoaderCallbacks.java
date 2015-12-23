package cc.easyandroid.easycore;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

public class SimpleLoaderCallbacks<T> implements LoaderCallbacks<T> {

	@Override
	public Loader<T> onCreateLoader(int id, Bundle args) {
		return null;
	}

	@Override
	public void onLoadFinished(Loader<T> loader, T data) {

	}

	@Override
	public void onLoaderReset(Loader<T> loader) {

	}

}