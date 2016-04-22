package cc.easyandroid.easyutils;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;

public class EasyUtils {
	/**
	 * 只能转换key和value都是String 类型的
	 * 
	 * @param bundle
	 * @return
	 */
	public static Map<String, String> BundleToMap(Bundle bundle) {
		Map<String, String> params = new HashMap<>();
		for (String key : bundle.keySet()) {
			params.put(key, bundle.getString(key));
		}
		return params;
	}
}
