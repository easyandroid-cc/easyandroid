package cc.easyandroid.easyhttp.config;

import android.content.Context;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import cc.easyandroid.easycache.CacheUtils;
import cc.easyandroid.easycache.EasyHttpCache;
import cc.easyandroid.easyhttp.core.StateCodeHandler;
import okhttp3.Cache;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;

public class EAConfiguration {
    private Gson mGson;
//    private DiskBasedCache mCache;
    private CookieJar mCookieJar;
    private OkHttpClient mOkHttpClient;
    private StateCodeHandler stateCodeProcessing;

    public StateCodeHandler getStateCodeProcessing() {
        return stateCodeProcessing;
    }

    public Gson getGson() {
        return mGson;
    }

//    public DiskBasedCache getCache() {
//        return mCache;
//    }

    public CookieJar getCookieJar() {
        return mCookieJar;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    private EAConfiguration(Builder builder) {
        mGson = builder.gson;
//        mCache = builder.valleyCache;
//        mCookieStore = builder.cookieStore;
        mOkHttpClient = builder.okHttpClient;
        stateCodeProcessing = builder.stateCodeProcessing;
    }

    public static class Builder {
        private Gson gson;
//        private final DiskBasedCache valleyCache;
        private final Cache okHttpCache;
        private OkHttpClient okHttpClient;
        private StateCodeHandler stateCodeProcessing;

        public Builder setGson(Gson gson) {
            this.gson = gson;
            return this;
        }

        public Builder setStateCodeProcessing(StateCodeHandler stateCodeProcessing) {
            this.stateCodeProcessing = stateCodeProcessing;
            return this;
        }

        public Builder setOkHttpClient(OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
            return this;
        }

        public Builder(Context context) {
            // 根据volley 缓存cache修改 ，不需要http协议就可保存
            EasyHttpCache.getInstance().initialize(context.getApplicationContext());
//            valleyCache = new DiskBasedCache(CacheUtils.getDiskCacheDir(context.getApplicationContext(), "volleycache"));// retrofit缓存
//            valleyCache.initialize();
            okHttpCache = new Cache(CacheUtils.getDiskCacheDir(context.getApplicationContext(), "okhttpcache"), 10 * 1024 * 1024);
            cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
        }
        ClearableCookieJar cookieJar;//

        public EAConfiguration build() {
            if (gson == null) {
                gson = new Gson();
            }
            cookieJar.clear();
            if (okHttpClient == null) {
                okHttpClient = new OkHttpClient.Builder()//
                        .connectTimeout(15 * 1000, TimeUnit.MILLISECONDS)//
                        .readTimeout(20 * 1000, TimeUnit.MILLISECONDS)//
                        .followRedirects(true)//
                        .cookieJar(cookieJar)//
                        .cache(okHttpCache)//  OkHttpClient缓存
                        .build();
            }
            return new EAConfiguration(this);
        }
    }

}
