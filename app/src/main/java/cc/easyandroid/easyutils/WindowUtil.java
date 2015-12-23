package cc.easyandroid.easyutils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

public class WindowUtil {
	/**
	 * 屏幕的宽高 dm.widthPixel dm.heightPixels
	 * 
	 * @param context
	 * @return
	 */
	public static DisplayMetrics getDisplayMetrics(Context context) {
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(dm);
		return dm;
	}
	
	/**
	 * 获取当前view的绝对坐标
	 * @param view
	 * @return
	 */
	public static int[] getViewInTheWindowPosition(View view) {
		int[] location = new int[2];
		if (view != null && view.getTag() != null) {
			view.getLocationInWindow(location); // 获取在当前窗口内的绝对坐标
			view.getLocationOnScreen(location);// 获取在整个屏幕内的绝对坐标
		}
		return location;
	}
}
