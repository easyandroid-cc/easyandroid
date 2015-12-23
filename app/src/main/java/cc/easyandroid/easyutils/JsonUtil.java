package cc.easyandroid.easyutils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {
	public static String getString(JSONObject jsonObject, String key) {
		try {
			return jsonObject.has(key) ? jsonObject.getString(key) : "";
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static int getInt(JSONObject jsonObject, String key) {
		try {
			return jsonObject.has(key) ? jsonObject.getInt(key) : 0;
		} catch (JSONException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static long getLong(JSONObject jsonObject, String key) {
		try {
			return jsonObject.has(key) ? jsonObject.getLong(key) : 0;
		} catch (JSONException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static double getDouble(JSONObject jsonObject, String key) {
		try {
			return jsonObject.has(key) ? jsonObject.getDouble(key) : 0;
		} catch (JSONException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static boolean getBoolean(JSONObject jsonObject, String key) {
		try {
			return jsonObject.has(key) ? jsonObject.getBoolean(key) : false;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static JSONObject getJSONObject(JSONObject jsonObject, String key) {
		try {
			return jsonObject.has(key) ? jsonObject.getJSONObject(key) : null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static JSONArray getJSONArray(JSONObject jsonObject, String key) {
		try {
			return jsonObject.has(key) ? jsonObject.getJSONArray(key) : null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}
