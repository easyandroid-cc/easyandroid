package cc.easyandroid.easyhttp.config;

import android.content.Context;

import com.google.gson.Gson;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import cc.easyandroid.easycache.CacheUtils;
import cc.easyandroid.easycache.volleycache.DiskBasedCache;
import cc.easyandroid.easyhttp.cookiestore.CookieStore;
import cc.easyandroid.easyhttp.cookiestore.PersistentCookieStore;
import cc.easyandroid.easyhttp.core.StateCodeHandler;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.internal.JavaNetCookieJar;

public class EAConfiguration {
    private Gson mGson;
    private DiskBasedCache mCache;
    private CookieStore mCookieStore;
    private OkHttpClient mOkHttpClient;
    private StateCodeHandler stateCodeProcessing;

    public StateCodeHandler getStateCodeProcessing() {
        return stateCodeProcessing;
    }

    public Gson getGson() {
        return mGson;
    }

    public DiskBasedCache getCache() {
        return mCache;
    }

    public CookieStore getCookieStore() {
        return mCookieStore;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    private EAConfiguration(Builder builder) {
        mGson = builder.gson;
        mCache = builder.valleyCache;
        mCookieStore = builder.cookieStore;
        mOkHttpClient = builder.okHttpClient;
        stateCodeProcessing = builder.stateCodeProcessing;
    }

    public static class Builder {
        private Gson gson;
        private final DiskBasedCache valleyCache;
        private final Cache okHttpCache;
        private final CookieStore cookieStore;
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
            valleyCache = new DiskBasedCache(CacheUtils.getDiskCacheDir(context.getApplicationContext(), "volleycache"));// retrofit缓存
            valleyCache.initialize();
//            new CookieJarImpl(new CookieManager(new PersistentCookieStore(context.getApplicationContext()),
//                                                CookiePolicy.ACCEPT_ALL)));
            cookieStore = new PersistentCookieStore(context.getApplicationContext());
            okHttpCache = new Cache(CacheUtils.getDiskCacheDir(context.getApplicationContext(), "okhttpcache"), 10 * 1024 * 1024);
              cookieHandler = new CookieManager(
                    new cc.easyandroid.easyhttp.config.PersistentCookieStore(context), CookiePolicy.ACCEPT_ALL);

        }
        CookieHandler cookieHandler;
        public EAConfiguration build() {
            if (gson == null) {
                gson = new Gson();
            }
            if (okHttpClient == null) {
                //okHttpClient = new OkHttpClient();

                okHttpClient = new OkHttpClient.Builder()//
                        .connectTimeout(15 * 1000, TimeUnit.MILLISECONDS)//
                        .readTimeout(20 * 1000, TimeUnit.MILLISECONDS)//
                        .followRedirects(true)//
//                        .cookieJar(new CookieJarImpl(cookieStore))//
                        .cookieJar(new JavaNetCookieJar(cookieHandler))//
                        .cache(okHttpCache)//  OkHttpClient缓存
                        .build();
            }
            return new EAConfiguration(this);
        }
    }

}
