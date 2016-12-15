package cc.easyandroid.simple.his;

import android.support.v4.app.Fragment;

import java.util.List;

import cc.easyandroid.easyui.pojo.EasyTab;

/**
 * @author cgpllx1@qq.com (www.kubeiwu.com)
 * @date 2014-8-20
 */
public abstract class EasyTabBaseFragment extends Fragment {

	public TabConfig onCreatTabConfig() {
		return TabConfig.getSimpleInstance();
	}

	/**
	 * EasyCall addTab() Method To increase tab;
	 */
	public abstract List<EasyTab> onCreatTab();

	void creatTab() {
		List<EasyTab> eaTabs = onCreatTab();
		for (EasyTab eaTab : eaTabs) {
			addTab(eaTab);
		}
	}

	abstract void addTab(EasyTab eaTab);

}
