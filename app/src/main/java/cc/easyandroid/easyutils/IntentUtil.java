/**   
 * @Title: IntentUtil.java
 * @Package me.pc.mobile.helper.util
 * @Description: TODO
 * @author SilentKnight || happychinapc@gmail.com
 * @date 2014 2014-11-25 上午11:29:05
 * @version V1.0.0 
 */
package cc.easyandroid.easyutils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

/**
 * @author SilentKnight
 * 
 */
public final class IntentUtil {
	private IntentUtil() {
	}

	public static void defaultStart(Context context, Class<?> targetClzz) {
		context.startActivity(new Intent(context, targetClzz));
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public static void startWithBundle(Context context, Class<?> targetClzz,
			Bundle data) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			context.startActivity(new Intent(context, targetClzz), data);
		} else {
			Intent intent = new Intent(context, targetClzz);
			intent.putExtras(data);
			context.startActivity(intent);
		}

	}
}
