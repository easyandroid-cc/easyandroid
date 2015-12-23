package cc.easyandroid.easyutils;

import android.content.Context;

/**
 
 */
public final class DisplayUtils {
	private DisplayUtils() {
	}

	public static int px2Dp(Context ctx, float pxValue) {
		final float density = ctx.getResources().getDisplayMetrics().density;
		return (int) (pxValue / density + 0.5f);
	}

	public static int dp2Px(Context ctx, float dpValue) {
		final float density = ctx.getResources().getDisplayMetrics().density;
		return (int) (dpValue * density + 0.5f);
	}

	public static int sp2Px(Context ctx, float spValue) {
		final float scaledDensity = ctx.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * scaledDensity + 0.5f);
	}

	public static int px2Sp(Context ctx, float pxValue) {
		final float scaledDensity = ctx.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / scaledDensity + 0.5f);
	}

	public static int dp2Sp(Context ctx, float dpValue) {
		final float density = ctx.getResources().getDisplayMetrics().scaledDensity;
		final float scaledDensity = ctx.getResources().getDisplayMetrics().scaledDensity;
		return (int) (dpValue * density / scaledDensity + 0.5f);
	}

	public static int sp2Dp(Context ctx, float spValue) {
		final float density = ctx.getResources().getDisplayMetrics().scaledDensity;
		final float scaledDensity = ctx.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * scaledDensity / density + 0.5f);
	}
}
