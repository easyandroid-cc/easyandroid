package cc.easyandroid.module;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

/**
 */
public final class ManifestParser {
    private static final String GLIDE_MODULE_VALUE = "EasyHttpUtilsModule";

    private final Context context;

    public ManifestParser(Context context) {
        this.context = context;
    }

    public List<EasyHttpUtilsModule> parse() {
        List<EasyHttpUtilsModule> modules = new ArrayList<EasyHttpUtilsModule>();
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                for (String key : appInfo.metaData.keySet()) {
                    if (GLIDE_MODULE_VALUE.equals(appInfo.metaData.get(key))) {
                        modules.add(parseModule(key));
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Unable to find metadata to parse GlideModules", e);
        }

        return modules;
    }

    private static EasyHttpUtilsModule parseModule(String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unable to find EasyHttpUtilsModule implementation", e);
        }

        Object module;
        try {
            module = clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Unable to instantiate EasyHttpUtilsModule implementation for " + clazz, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to instantiate EasyHttpUtilsModule implementation for " + clazz, e);
        }

        if (!(module instanceof EasyHttpUtilsModule)) {
            throw new RuntimeException("Expected instanceof EasyHttpUtilsModule, but found: " + module);
        }
        return (EasyHttpUtilsModule) module;
    }
}