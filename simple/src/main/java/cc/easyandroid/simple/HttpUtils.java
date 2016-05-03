package cc.easyandroid.simple;

import android.os.Bundle;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cc.easyandroid.easycore.EasyCall;
import cc.easyandroid.easyhttp.EasyHttpUtils;
import cc.easyandroid.easyhttp.core.OKHttp3RequestFactory;
import cc.easyandroid.easyhttp.core.down.ProgressListener;
import cc.easyandroid.easyhttp.core.down.ProgressResponseBody;
import cc.easyandroid.easyhttp.core.retrofit.CacheMode;
import cc.easyandroid.easymvp.presenter.EasyWorkPresenter;
import cc.easyandroid.easyutils.EasyUtils;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * http工具類，根據需要自己添加
 */
public class HttpUtils {

    public static <T> EasyCall<T> creatPostCall(String url, Bundle bundle, EasyWorkPresenter<T> presenter) {
        okhttp3.Request request = OKHttp3RequestFactory.createPostRequest(url, EasyUtils.BundleToMap(bundle));
        return EasyHttpUtils.getInstance().executeHttpRequestToCall(request, presenter.getDeliverResultType());
    }

    public static <T> EasyCall<T> creatPostCall(String url, Bundle bundle, EasyWorkPresenter<T> presenter, Map<String, String> header) {
        okhttp3.Request request = OKHttp3RequestFactory.createPostRequest(url, header, EasyUtils.BundleToMap(bundle));
        return EasyHttpUtils.getInstance().executeHttpRequestToCall(request, presenter.getDeliverResultType());
    }

    /**
     * post请求 上传图片
     *
     * @param url       地址
     * @param bundle    参数
     * @param presenter
     * @param files
     * @param header
     * @param <T>
     * @return
     */
    public static <T> EasyCall<T> creatPostFileCall(String url, Bundle bundle, EasyWorkPresenter<T> presenter, Map<String, File> files, Map<String, String> header) {
        okhttp3.Request request = OKHttp3RequestFactory.createMultipartRequest(url, header, EasyUtils.BundleToMap(bundle), files);
        return EasyHttpUtils.getInstance().executeHttpRequestToCall(request, presenter.getDeliverResultType());
    }

    /**
     * post请求
     *
     * @param url
     * @param paras     参数
     * @param presenter
     * @param <T>
     * @return
     */
    public static <T> EasyCall<T> creatPostCall(String url, Map<String, String> paras, EasyWorkPresenter<T> presenter) {
        okhttp3.Request request = OKHttp3RequestFactory.createPostRequest(url, paras);
        return EasyHttpUtils.getInstance().executeHttpRequestToCall(request, presenter.getDeliverResultType());
    }

    /**
     * get请求
     *
     * @param url
     * @param presenter
     * @param <T>
     * @return
     */
    public static <T> EasyCall<T> creatGetCallNoCache(String url, EasyWorkPresenter<T> presenter) {
        okhttp3.Request request = OKHttp3RequestFactory.createGetRequest(url);
        return EasyHttpUtils.getInstance().executeHttpRequestToCall(request, presenter.getDeliverResultType());
    }

    /**
     * 先网络后缓存
     *
     * @param url
     * @param presenter
     * @param <T>
     * @return
     */
    public static <T> EasyCall<T> creatGetCall(String url, EasyWorkPresenter<T> presenter) {
        HashMap<String, String> header = new HashMap<>();
        header.put("Cache-Mode", CacheMode.LOAD_NETWORK_ELSE_CACHE);
        header.put("Cache-Control", "max-age=340000");
        okhttp3.Request request = OKHttp3RequestFactory.createGetRequest(url, header);
        return EasyHttpUtils.getInstance().executeHttpRequestToCall(request, presenter.getDeliverResultType());
    }


    /**
     * 先缓存后网络
     *
     * @param url
     * @param presenter
     * @param <T>
     * @return
     */
    public static <T> EasyCall<T> creatGetCallFirstCache(String url, EasyWorkPresenter<T> presenter) {
        HashMap<String, String> header = new HashMap<>();
        header.put("Cache-Mode", CacheMode.LOAD_CACHE_ELSE_NETWORK);
        header.put("Cache-Control", "max-age=340000");
        okhttp3.Request request = OKHttp3RequestFactory.createGetRequest(url, header);
        return EasyHttpUtils.getInstance().executeHttpRequestToCall(request, presenter.getDeliverResultType());
    }

    /**
     * 上传文件
     *
     * @param url
     * @param header
     * @param params
     * @param files
     * @param presenter
     * @param <T>
     * @return
     */
    public static <T> EasyCall<T> creatGetCallUpLoadCall(String url, Map<String, String> header, Map<String, String> params, Map<String, File> files, EasyWorkPresenter<T> presenter) {
        okhttp3.Request request = OKHttp3RequestFactory.createMultipartRequest(url, header, params, files);
        return EasyHttpUtils.getInstance().executeHttpRequestToCall(request, presenter.getDeliverResultType());
    }

    /**
     * 上传文件，有进度显示
     * @param url
     * @param header
     * @param params
     * @param files
     * @param presenter
     * @param listener
     * @param <T>
     * @return
     */
    public static <T> EasyCall<T> creatGetCallUpLoadCall(String url, Map<String, String> header, Map<String, String> params, Map<String, File> files, EasyWorkPresenter<T> presenter, final ProgressListener listener) {
        okhttp3.Request request = OKHttp3RequestFactory.createMultipartRequest(url, header, params, files, listener);
        return EasyHttpUtils.getInstance().executeHttpRequestToCall(request, presenter.getDeliverResultType());
    }

    /**
     * 下载文件，带进度
     *
     * @param url
     * @param header
     * @param presenter
     * @param listener  进度监听接口
     * @param <T>
     * @return
     */
    public static <T> EasyCall<T> creatGetDownLoadCall(String url, Map<String, String> header, EasyWorkPresenter<T> presenter, final ProgressListener listener) {
        okhttp3.Request request = OKHttp3RequestFactory.createGetRequest(url, header);
        OKHttp3RequestFactory.createGetRequest(url, header);
        OkHttpClient client = EasyHttpUtils.getInstance().getOkHttpClient().newBuilder().addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), listener))
                        .build();
            }
        }).build();
        return EasyHttpUtils.getInstance().executeHttpRequestToCall(client, request, presenter.getDeliverResultType());
    }

    /**
     * 下载
     *
     * @param url         文件地址
     * @param destFileDir 保存的文件夹
     */
    public static void downFile(String url, String destFileDir) {
//        EasyHttpUtils.getInstance().getOkHttpDownloadUtils().downloadAsyn(url, destFileDir);
    }

    private static String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

}
