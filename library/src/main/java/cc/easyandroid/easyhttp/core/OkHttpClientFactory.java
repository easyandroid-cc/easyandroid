package cc.easyandroid.easyhttp.core;

import android.content.Context;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.util.concurrent.TimeUnit;

import cc.easyandroid.easycache.CacheUtils;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by cgpllx on 2016/10/9.
 */
public class OkHttpClientFactory {
    /**
     * 创建一个通用的client
     *
     * @param context context
     * @return OkHttpClient
     */
    public static OkHttpClient getGenericClient(Context context) {
        Cache okHttpCache = new Cache(CacheUtils.getDiskCacheDir(context.getApplicationContext(), "okhttpcache"), 10 * 1024 * 1024);
        PersistentCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
        cookieJar.clear();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()//
                .connectTimeout(15 * 1000, TimeUnit.MILLISECONDS)//
                .readTimeout(20 * 1000, TimeUnit.MILLISECONDS)//
                .followRedirects(true)//
                .cookieJar(cookieJar)//
                .cache(okHttpCache)//  OkHttpClient缓存
                .build();
        return okHttpClient;
    }
}
