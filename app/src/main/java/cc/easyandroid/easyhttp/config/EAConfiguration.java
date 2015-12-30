package cc.easyandroid.easyhttp.config;

import android.content.Context;

import com.google.gson.Gson;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.util.concurrent.TimeUnit;

import cc.easyandroid.easycache.CacheUtils;
import cc.easyandroid.easycache.volleycache.DiskBasedCache;
import cc.easyandroid.easyhttp.cookiestore.PersistentCookieStore;
import cc.easyandroid.easyhttp.core.StateCodeHandler;

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
            if (okHttpClient.getCache() == null) {
                okHttpClient.setCache(okHttpCache);// OkHttpClient缓存
            }
            return this;
        }

        public Builder(Context context) {
            // 根据volley 缓存cache修改 ，不需要http协议就可保存
            valleyCache = new DiskBasedCache(CacheUtils.getDiskCacheDir(context.getApplicationContext(), "volleycache"));// retrofit缓存
            valleyCache.initialize();
            cookieStore = new PersistentCookieStore(context.getApplicationContext());
            okHttpCache = new Cache(CacheUtils.getDiskCacheDir(context.getApplicationContext(), "okhttpcache"), 10 * 1024 * 1024);
        }

        public EAConfiguration build() {
            if (gson == null) {
                gson = new Gson();
            }
            if (okHttpClient == null) {
                okHttpClient = new OkHttpClient();
                okHttpClient.setConnectTimeout(15 * 1000, TimeUnit.MILLISECONDS);
                okHttpClient.setFollowRedirects(true);
                okHttpClient.setReadTimeout(20 * 1000, TimeUnit.MILLISECONDS);
                okHttpClient.setCookieHandler(new CookieManager(cookieStore, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
                okHttpClient.setCache(okHttpCache);// OkHttpClient缓存
            }

            return new EAConfiguration(this);
        }
    }

}
