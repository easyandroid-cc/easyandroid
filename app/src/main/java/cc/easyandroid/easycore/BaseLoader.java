package cc.easyandroid.easycore;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public abstract class BaseLoader<T> extends AsyncTaskLoader<T> {

	public BaseLoader(Context context) {
		super(context);
	}

	private T mListData;

	@Override
	public abstract T loadInBackground();

	@Override
	public void deliverResult(T data) {
		super.deliverResult(data);
		if (isReset()) {
			if (data != null) {
				onReleaseResources(data);
			}
		}
		T oldList = data;
		if (data != null) {
			mListData = data;
		}
		if (isStarted()) {
			super.deliverResult(data);
		}

		if (oldList != null) {
			onReleaseResources(oldList);
		}

	}

	protected void onReleaseResources(T data) {
		// Log.i(TAG, "onReleaseResources");
		// For a simple List<> there is nothing to do. For something
		// like a Cursor, we would close it here.
	}

	@Override
	protected void onStartLoading() {
		super.onStartLoading();

		if (mListData != null) {
			deliverResult(mListData);
		}
		if (takeContentChanged() || mListData == null) {//防止重复调用onloaderfinish
			forceLoad();
		}
	}

	@Override
	public void onCanceled(T data) {
		super.onCanceled(data);
	}

	@Override
	protected void onReset() {
		super.onReset();

		onStopLoading();
		if (mListData != null) {
			onReleaseResources(mListData);
			mListData = null;// 释放资源
		}
	}

	@Override
	protected void onStopLoading() {
		super.onStopLoading();
		cancelLoad();
	}

}
