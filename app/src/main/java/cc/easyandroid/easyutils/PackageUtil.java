/**   
 * @Title: PackageUtil.java
 * @Package me.pc.mobile.helper.util
 * @Description: TODO
 * @author SilentKnight || happychinapc@gmail.com
 * @date 2014 2014-11-25 上午10:41:46
 * @version V1.0.0 
 */
package cc.easyandroid.easyutils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * @author SilentKnight
 * 
 */
public final class PackageUtil {
	private PackageUtil() {
	}

	public static String getAppVersionName(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	public static int getAppVersionCode(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			return 0;
		}
	}
}
