package cc.easyandroid.easyhttp;

import android.content.Context;

import com.google.gson.Gson;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

import cc.easyandroid.easycache.volleycache.Cache;
import cc.easyandroid.easycache.volleycache.DiskBasedCache;
import cc.easyandroid.easycore.EasyCall;
import cc.easyandroid.easyhttp.config.EAConfiguration;
import cc.easyandroid.easyhttp.core.StateCodeHandler;
import cc.easyandroid.easyhttp.core.converter.Converter;
import cc.easyandroid.easyhttp.core.converter.ConverterFactory;
import cc.easyandroid.easymvp.call.OkHttpDownLoadEasyCall;
import cc.easyandroid.easymvp.call.OkHttpEasyCall;
import cc.easyandroid.module.EasyHttpUtilsModule;
import cc.easyandroid.module.ManifestParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class EasyHttpUtils {
    static final EasyHttpUtils mInstance = new EasyHttpUtils();
    private OkHttpClient mOkHttpClient;
    public Gson mGson;
    private StateCodeHandler stateCodeProcessing;
    private DiskBasedCache easyHttpCache;

    private EasyHttpUtils() {
    }

    public static EasyHttpUtils getInstance() {
        return mInstance;
    }

    public void init(EAConfiguration config) {
        if (config == null) {
            new IllegalArgumentException("EAConfiguration config can not is null");
        }
        easyHttpCache = config.getCache();
        mGson = config.getGson();
        mOkHttpClient = config.getOkHttpClient();
        stateCodeProcessing = config.getStateCodeProcessing();

        // cookie enabled
//        mOkHttpClient.setHostnameVerifier(new HostnameVerifier() {
//            @Override
//            public boolean verify(String hostname, SSLSession session) {
//                return true;
//            }
//        });
    }


    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public Cache getCache() {
        return easyHttpCache;
    }

    public <T> EasyCall<T> executeHttpRequestToCall(Request request, Type type) {
        checkNull(mOkHttpClient);
        Converter responseConverter;
        if (type == String.class) {
            responseConverter = getConverterFactory().getStringConverter();
        } else {
            responseConverter = getConverterFactory().getGsonConverter(type);
        }
        EasyCall<T> easyCall = new OkHttpEasyCall<T>(mOkHttpClient, responseConverter, request);

        return easyCall;
    }

    /**
     * 使用就的 OkHttpClient
     *
     * @param client
     * @param request
     * @param type
     * @param <T>
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
        EasyCall<T> easyCall = new OkHttpEasyCall<T>(client, responseConverter, request);

        return easyCall;
    }

    public EasyCall<OkHttpDownLoadEasyCall.DownLoadResult> executeHttpRequestToDownLoadCall(OkHttpClient client, Request request, File file) {
        checkNull(client);
        EasyCall<OkHttpDownLoadEasyCall.DownLoadResult> easyCall = new OkHttpDownLoadEasyCall(client, request, file);
        return easyCall;
    }


    ConverterFactory converterFactory;// =KGsonConverterFactory.create(mGson,
    // easyHttpCache);

    public ConverterFactory getConverterFactory() {
        if (converterFactory == null) {
            synchronized (EasyHttpUtils.class) {
                if (converterFactory == null) {
                    checkNull(mGson);
                    checkNull(easyHttpCache);
                    converterFactory = ConverterFactory.create(mGson, easyHttpCache, stateCodeProcessing);
                }
            }
        }
        return converterFactory;
    }

    private void checkNull(Object object) {
        if (object == null) {
            new IllegalArgumentException("请先初始化EasyHttpUtils");
        }
    }

    private static EasyHttpUtils easyHttpUtils;
    /**
     * Get the singleton.
     *
     * @return the singleton
     */
    public static EasyHttpUtils get(Context context) {
        if (easyHttpUtils == null) {
            synchronized (EasyHttpUtils.class) {
                if (easyHttpUtils == null) {
                    Context applicationContext = context.getApplicationContext();
                    List<EasyHttpUtilsModule> modules = new ManifestParser(applicationContext).parse();

                    EAConfiguration.Builder builder = new EAConfiguration.Builder(applicationContext);
                    for (EasyHttpUtilsModule module : modules) {
                        module.applyOptions(applicationContext, builder);
                    }
                    easyHttpUtils =  new EasyHttpUtils();
                    easyHttpUtils.init(builder.build());

                    for (EasyHttpUtilsModule module : modules) {
                        module.registerComponents(applicationContext, easyHttpUtils);
                    }
                }
            }
        }
        return easyHttpUtils;
    }
}
