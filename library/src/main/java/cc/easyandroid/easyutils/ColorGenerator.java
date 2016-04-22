/**   
 * @Title: ColorGenerator.java 
 * @Package me.pc.mobile.helper.v14.util 
 * @Description: TODO
 * @author SilentKnight || happychinapc[at]gmail[dot]com   
 * @date 2015 2015年4月13日 下午4:27:40 
 * @version V1.0.0   
 */
package cc.easyandroid.easyutils;

import java.util.Random;

import android.graphics.Color;

/**
 * @ClassName: ColorGenerator
 * @Description: TODO
 * @author SilentKnight || happychinapc@gmail.com
 * @date 2015年4月13日 下午4:27:40
 * 
 */
public final class ColorGenerator {
	public static final int RGB = 0XFF + 1;

	public static int genRandomColor() {
		Random rand = new Random();
		int redClr = rand.nextInt(RGB);
		int greenClr = rand.nextInt(RGB);
		int blueClr = rand.nextInt(RGB);
		return Color.rgb(redClr, greenClr, blueClr);
	}
}
