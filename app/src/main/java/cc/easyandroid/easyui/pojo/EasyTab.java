package cc.easyandroid.easyui.pojo;

import android.os.Bundle;
import android.view.View;

public class EasyTab {
	private String tabSpec;
	private View tabView;
	private Class<?> yourFragment;
	private Bundle bundle;

	public EasyTab(String tabSpec, View tabView, Class<?> yourFragment, Bundle bundle) {
		super();
		this.tabSpec = tabSpec;
		this.tabView = tabView;
		this.yourFragment = yourFragment;
		this.bundle = bundle;
	}

	public String getTabSpec() {
		return tabSpec;
	}

	public View getTabView() {
		return tabView;
	}

	public Class<?> getYourFragment() {
		return yourFragment;
	}

	public Bundle getBundle() {
		return bundle;
	}

}
