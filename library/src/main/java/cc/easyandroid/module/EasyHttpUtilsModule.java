package cc.easyandroid.module;

import android.content.Context;

import cc.easyandroid.easyhttp.EasyHttp;

public interface EasyHttpUtilsModule {


    void applyOptions(Context context, EasyHttp.Builder builder);

    void registerComponents(Context context, EasyHttp easyHttp);
}

