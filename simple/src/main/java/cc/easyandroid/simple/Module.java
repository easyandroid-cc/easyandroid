package cc.easyandroid.simple;

import android.content.Context;

import cc.easyandroid.easyhttp.EasyHttpUtils;
import cc.easyandroid.easyhttp.config.EAConfiguration;
import cc.easyandroid.module.EasyHttpUtilsModule;

/**
 * Created by cgpllx on 2016/8/18.
 */
public class Module implements EasyHttpUtilsModule {

    @Override
    public void applyOptions(Context context, EAConfiguration.Builder builder) {
//        EAConfiguration eaConfiguration = new EAConfiguration.Builder(this).build();
//        EasyHttpUtils.getInstance().init(eaConfiguration);

    }

    @Override
    public void registerComponents(Context context, EasyHttpUtils easyHttpUtils) {

    }
}
