package cc.easyandroid.easyui.fragment;

import android.support.v4.app.Fragment;

public class EasyLazyLoadFragment extends Fragment {

	protected boolean isVisible;

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			isVisible = true;
			onVisible();
		} else {
			isVisible = false;
		}
	}

	protected void onVisible() {
		if (!loaded) {
			loaded = true;
			lazyLoad();
		}
	}

	private boolean loaded = false;

	protected void lazyLoad() {
	};
}
