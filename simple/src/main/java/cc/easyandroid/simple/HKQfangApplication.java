package cc.easyandroid.simple;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import java.net.CookieManager;
import java.net.CookiePolicy;

import cc.easyandroid.easyhttp.EasyHttpUtils;
import cc.easyandroid.easyhttp.config.EAConfiguration;

/**
 * app
 */
public class HKQfangApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        initEasyAndroid();
    }

    private void initEasyAndroid() {
        EAConfiguration eaConfiguration = new EAConfiguration.Builder(this).build();
        EasyHttpUtils.getInstance().init(eaConfiguration);
        EasyHttpUtils.getInstance().getOkHttpClient().setCookieHandler((new CookieManager(eaConfiguration.getCookieStore(), CookiePolicy.ACCEPT_ALL)));
    }


}
