package cc.easyandroid.easyhttp;

import android.content.Context;

import com.google.gson.Gson;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

import cc.easyandroid.easycache.EasyHttpCache;
import cc.easyandroid.easycore.EasyCall;
import cc.easyandroid.easyhttp.core.converter.Converter;
import cc.easyandroid.easyhttp.core.converter.ConverterFactory;
import cc.easyandroid.easymvp.call.OkHttpDownLoadEasyCall;
import cc.easyandroid.easymvp.call.OkHttpEasyCall;
import cc.easyandroid.module.EasyHttpUtilsModule;
import cc.easyandroid.module.ManifestParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 *
 */
public class EasyHttp {
    private OkHttpClient mOkHttpClient;
    private Gson mGson;
    private EasyHttpCache mEasyHttpCache;

    private EasyHttp(Builder builder) {
        mOkHttpClient = builder.okHttpClient;
        mGson = builder.gson;
        mEasyHttpCache = builder.easyHttpCache;
        converterFactory = ConverterFactory.create(mGson, mEasyHttpCache);
    }


    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public Gson getGson() {
        return mGson;
    }

    public EasyHttpCache getEasyHttpCache() {
        return mEasyHttpCache;
    }

    public <T> EasyCall<T> executeHttpRequestToCall(Request request, Type type) {
        checkNull(mOkHttpClient);
        Converter responseConverter;
        if (type == String.class) {
            responseConverter = getConverterFactory().getStringConverter();
        } else {
            responseConverter = getConverterFactory().getGsonConverter(type);
        }
        EasyCall<T> easyCall = new OkHttpEasyCall<>(mOkHttpClient, responseConverter, request,mEasyHttpCache);

        return easyCall;
    }

    public <T> EasyCall<T> executeHttpRequestToCall(Request request, Converter<T> responseConverter) {
        checkNull(mOkHttpClient);
        EasyCall<T> easyCall = new OkHttpEasyCall<>(mOkHttpClient, responseConverter, request,mEasyHttpCache);
        return easyCall;
    }

    /**
     * 使用自己的 OkHttpClient
     *
     * @param client  client
     * @param request request
     * @param type    type
     * @param <T>     type
     * @return
     */
    public <T> EasyCall<T> executeHttpRequestToCall(OkHttpClient client, Request request, Type type) {
        checkNull(client);
        Converter responseConverter;
        if (type == String.class) {
            responseConverter = getConverterFactory().getStringConverter();
        } else {
            responseConverter = getConverterFactory().getGsonConverter(type);
        }
        EasyCall<T> easyCall = new OkHttpEasyCall<T>(client, responseConverter, request,mEasyHttpCache);

        return easyCall;
    }

    public EasyCall<OkHttpDownLoadEasyCall.DownLoadResult> executeHttpRequestToDownLoadCall(OkHttpClient client, Request request, File file) {
        checkNull(client);
        EasyCall<OkHttpDownLoadEasyCall.DownLoadResult> easyCall = new OkHttpDownLoadEasyCall(client, request, file);
        return easyCall;
    }


    ConverterFactory converterFactory;

    public ConverterFactory getConverterFactory() {
        return converterFactory;
    }

    private void checkNull(Object object) {
        if (object == null) {
            new IllegalArgumentException("请先初始化EasyHttpUtils");
        }
    }

    private static EasyHttp easyHttp;

    /**
     * Get the singleton.
     *
     * @return the singleton
     */
    public static EasyHttp get(Context context) {
        if (easyHttp == null) {
            synchronized (EasyHttp.class) {
                if (easyHttp == null) {
                    Context applicationContext = context.getApplicationContext();
                    List<EasyHttpUtilsModule> modules = new ManifestParser(applicationContext).parse();

                    EasyHttp.Builder builder = new EasyHttp.Builder(applicationContext);
                    for (EasyHttpUtilsModule module : modules) {
                        module.applyOptions(applicationContext, builder);
                    }
                    easyHttp = builder.createEasyHttp();

                    for (EasyHttpUtilsModule module : modules) {
                        module.registerComponents(applicationContext, easyHttp);
                    }
                }
            }
        }
        return easyHttp;
    }

    public static class Builder {
        Gson gson;
        OkHttpClient okHttpClient;
        EasyHttpCache easyHttpCache;
        private final Context context;

        public Builder setGson(Gson gson) {
            this.gson = gson;
            return this;
        }

        public Builder setOkHttpClient(OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
            return this;
        }

        public void setEasyHttpCache(EasyHttpCache easyHttpCache) {
            this.easyHttpCache = easyHttpCache;
        }

        public Builder(Context context) {
            this.context = context.getApplicationContext();
        }

        EasyHttp createEasyHttp() {
            if (gson == null) {
                gson = new Gson();
            }
            checkNull(okHttpClient);
            if (easyHttpCache == null) {
                // 根据volley 缓存cache修改 ，不需要http协议就可保存
                easyHttpCache = new EasyHttpCache(context);
            }
            return new EasyHttp(this);
        }

        private void checkNull(Object object) {
            if (object == null) {
                new IllegalArgumentException("please call setOkHttpClient");
            }
        }
    }
}
