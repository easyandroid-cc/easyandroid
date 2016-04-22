package cc.easyandroid.easysimple;

import cc.easyandroid.EasyHttpApiFactory;

public class ApiProvider {
	private static final Api API = EasyHttpApiFactory.getInstance().getApi(
			Api.class, "http://xf.qfang.com");

	public static Api getInstance() {
		return API;
	}
	
}
