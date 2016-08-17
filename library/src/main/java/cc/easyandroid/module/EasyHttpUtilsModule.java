package cc.easyandroid.module;

import android.content.Context;

import cc.easyandroid.easyhttp.EasyHttpUtils;
import cc.easyandroid.easyhttp.config.EAConfiguration;

public interface EasyHttpUtilsModule {


    void applyOptions(Context context, EAConfiguration.Builder builder);

    void registerComponents(Context context, EasyHttpUtils easyHttpUtils);
}

