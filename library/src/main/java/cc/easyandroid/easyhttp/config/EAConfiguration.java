package cc.easyandroid.easyhttp.config;

import android.content.Context;

import com.google.gson.Gson;

import cc.easyandroid.easycache.EasyHttpCache;
import okhttp3.OkHttpClient;

public class EAConfiguration {
    private Gson mGson;
    private OkHttpClient mOkHttpClient;
    private EasyHttpCache mEasyHttpCache;
    public Gson getGson() {
        return mGson;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public EasyHttpCache getEasyHttpCache() {
        return mEasyHttpCache;
    }

    private EAConfiguration(Builder builder) {
        mGson = builder.gson;
        mOkHttpClient = builder.okHttpClient;
        mEasyHttpCache = builder.easyHttpCache;
    }

    public static class Builder {
        private Gson gson;
        private OkHttpClient okHttpClient;
        private EasyHttpCache easyHttpCache;

        public Builder setGson(Gson gson) {
            this.gson = gson;
            return this;
        }

        public Builder setOkHttpClient(OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
            return this;
        }

        public Builder(Context context) {
            // 根据volley 缓存cache修改 ，不需要http协议就可保存
//            easyHttpCache = EasyHttpCache.getInstance().initialize(context.getApplicationContext());
            easyHttpCache =new EasyHttpCache(context.getApplicationContext());
        }

        public EAConfiguration build() {
            if (gson == null) {
                gson = new Gson();
            }
            checkNull(okHttpClient);
            return new EAConfiguration(this);
        }

        private void checkNull(Object object) {
            if (object == null) {
                new IllegalArgumentException("please call setOkHttpClient");
            }
        }
    }
}
