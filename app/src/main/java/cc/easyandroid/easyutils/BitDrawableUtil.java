/**   
 * @Title: DrawableUtil.java 
 * @Package me.pc.mobile.helper.util 
 * @Description: TODO
 * @author SilentKnight || happychinapc[at]gmail[dot]com   
 * @date 2014 2014年11月28日 上午11:39:29 
 * @version V1.0.0   
 */
package cc.easyandroid.easyutils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

 
public final class BitDrawableUtil {
	private BitDrawableUtil() {
	}

	public static Drawable from(Bitmap bitmap) {
		BitmapDrawable bitDrawable = new BitmapDrawable(bitmap);
		return bitDrawable;
	}

	public static Bitmap from(Drawable drawable) {
		BitmapDrawable bitDrawable = (BitmapDrawable) drawable;
		return bitDrawable.getBitmap();
	}
}
