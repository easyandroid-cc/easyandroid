package cc.easyandroid.easyhttp;

import com.google.gson.Gson;

import java.io.File;
import java.lang.reflect.Type;

import cc.easyandroid.easycache.EasyHttpCache;
import cc.easyandroid.easycore.EasyCall;
import cc.easyandroid.easyhttp.config.EAConfiguration;
import cc.easyandroid.easyhttp.core.converter.Converter;
import cc.easyandroid.easyhttp.core.converter.ConverterFactory;
import cc.easyandroid.easymvp.call.OkHttpDownLoadEasyCall;
import cc.easyandroid.easymvp.call.OkHttpEasyCall;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@Deprecated
public class EasyHttpUtils {
    static final EasyHttpUtils mInstance = new EasyHttpUtils();
    private OkHttpClient mOkHttpClient;
    private Gson mGson;
    private EasyHttpCache mEasyHttpCache;

    private EasyHttpUtils() {

    }

    /**
     * @return EasyHttpUtils
     */
    public static EasyHttpUtils getInstance() {
        return mInstance;
    }

    public void init(EAConfiguration config) {
        if (config == null) {
            new IllegalArgumentException("EAConfiguration config can not is null");
        }
        mGson = config.getGson();
        mOkHttpClient = config.getOkHttpClient();
        mEasyHttpCache = config.getEasyHttpCache();

    }


    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public Gson getGson() {
        return mGson;
    }

    public <T> EasyCall<T> executeHttpRequestToCall(Request request, Type type) {
        checkNull(mOkHttpClient);
        Converter responseConverter;
        if (type == String.class) {
            responseConverter = getConverterFactory().getStringConverter();
        } else {
            responseConverter = getConverterFactory().getGsonConverter(type);
        }
        EasyCall<T> easyCall = new OkHttpEasyCall<>(mOkHttpClient, responseConverter, request,null);

        return easyCall;
    }

    public <T> EasyCall<T> executeHttpRequestToCall(Request request, Converter<T> responseConverter) {
        checkNull(mOkHttpClient);
        EasyCall<T> easyCall = new OkHttpEasyCall<>(mOkHttpClient, responseConverter, request,null);
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
        EasyCall<T> easyCall = new OkHttpEasyCall<T>(client, responseConverter, request,null);

        return easyCall;
    }

    public EasyCall<OkHttpDownLoadEasyCall.DownLoadResult> executeHttpRequestToDownLoadCall(OkHttpClient client, Request request, File file) {
        checkNull(client);
        EasyCall<OkHttpDownLoadEasyCall.DownLoadResult> easyCall = new OkHttpDownLoadEasyCall(client, request, file);
        return easyCall;
    }


    ConverterFactory converterFactory;// =KGsonConverterFactory.create(mGson,

    public ConverterFactory getConverterFactory() {
        if (converterFactory == null) {
            synchronized (EasyHttpUtils.class) {
                if (converterFactory == null) {
                    checkNull(mGson);
                    converterFactory = ConverterFactory.create(mGson, mEasyHttpCache);
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

}
