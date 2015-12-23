package cc.easyandroid.easyutils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class InstallBroadcastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		//接收安装广播   
		if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {//安装了应用
			String packageName = intent.getDataString();
//			System.out.println("安装了:" + packageName + "包名的程序");
			
			
		} else if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {//接收卸载广播
			String packageName = intent.getDataString();
			packageName = packageName.replace("package:", "");
//			System.out.println("卸载了:" + packageName + "包名的程序");
	 
			
		} else if (intent.getAction().equals("com.konka.appdownload.ApkSearchUtils")) {

		}
	}
}
