package cc.easyandroid.simple.his;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cc.easyandroid.easyui.pojo.EasyTab;

/**
 * Tab+Fragment
 * 
 * @author 耳东
 *
 */
public abstract class EasyFragmentTabs extends EasyTabBaseFragment {
	protected FragmentTabHost mFragmentTabHost;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		TabConfig tabConfig = onCreatTabConfig();

		if (tabConfig == null) {
			tabConfig = TabConfig.getSimpleInstance();
		}
		View view = ViewFactory.getFragmentTabHostView(inflater.getContext(), tabConfig.getTabGravity());
		mFragmentTabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
		mFragmentTabHost.setup(view.getContext(), getChildFragmentManager(), EasyR.id.realtabcontent);
		creatTab();
		int tabcount = mFragmentTabHost.getTabWidget().getChildCount();
		if (tabcount == 0) {
			throw new IllegalArgumentException("Please in the onCreatTab() method to addTab ");
		}
		mFragmentTabHost.getTabWidget().setBackgroundResource(tabConfig.getWidgetBackgroundResource());
		return view;
	}
	/**
	 * eg:EATab eaTab=new EATab(tabSpec, tabView, yourFragment.class, bundle);
	 */
	  void addTab(EasyTab eaTab) {
		mFragmentTabHost.addTab(mFragmentTabHost.newTabSpec(eaTab.getTabSpec())//
				.setIndicator(eaTab.getTabView()), eaTab.getYourFragment(), eaTab.getBundle());
	}

}
