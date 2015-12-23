package cc.easyandroid.easyutils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.Toast;

public class Utils {
	/**
	 * RGB颜色 转 HEX颜色 *
	 * 
	 * @param red
	 * @param green
	 * @param blue
	 * @return
	 */
	public static String toHEX(int red, int green, int blue) {
		if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
			return "";
		}
		String redStr = Integer.toHexString(red);
		String greenStr = Integer.toHexString(green);
		String blueStr = Integer.toHexString(blue);
		return ("#" + redStr + greenStr + blueStr).toUpperCase(Locale.US);
	}

	/** Print an on-screen message to alert the user */
	public static void toaster(Context context, int stringId) {
		Toast.makeText(context, stringId, Toast.LENGTH_SHORT).show();
	}

	public static int convertPxToDp(Context context, int px) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		float logicalDensity = metrics.density;
		int dp = Math.round(px / logicalDensity);
		return dp;
	}

	public static int convertDpToPx(Context context, int dp) {
		return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()));
	}

	public static String readAsset(Context context, String assetName, String defaultS) {
		try {
			InputStream is = context.getResources().getAssets().open(assetName);
			BufferedReader r = new BufferedReader(new InputStreamReader(is, "UTF8"));
			StringBuilder sb = new StringBuilder();
			String line = r.readLine();
			if (line != null) {
				sb.append(line);
				line = r.readLine();
				while (line != null) {
					sb.append('\n');
					sb.append(line);
					line = r.readLine();
				}
			}
			return sb.toString();
		} catch (IOException e) {
			return defaultS;
		}
	}

	/**
	 * Get a resource id from an attribute id.
	 * 
	 * @param context
	 * @param attrId
	 * @return the resource id
	 */
	public static int getResourceFromAttribute(Context context, int attrId) {
		TypedArray a = context.getTheme().obtainStyledAttributes(new int[] { attrId });
		int resId = a.getResourceId(0, 0);
		a.recycle();
		return resId;
	}

	public static void showExitDialog(Context context) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).setTitle("提示").setMessage("确定要退出吗?").setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		}).setNegativeButton("取消", null).create();

		alertDialog.show();
	}

	// 1、展开、收起状态栏 用途：可用于点击Notifacation之后收起状态栏
	public static final void collapseStatusBar(Context ctx) {
		Object sbservice = ctx.getSystemService("statusbar");
		try {
			Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
			Method collapse;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
				collapse = statusBarManager.getMethod("collapsePanels");
			} else {
				collapse = statusBarManager.getMethod("collapse");
			}
			collapse.invoke(sbservice);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void expandStatusBar(Context ctx) {
		Object sbservice = ctx.getSystemService("statusbar");
		try {
			Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
			Method expand;
			if (Build.VERSION.SDK_INT >= 17) {
				expand = statusBarManager.getMethod("expandNotificationsPanel");
			} else {
				expand = statusBarManager.getMethod("expand");
			}
			expand.invoke(sbservice);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
