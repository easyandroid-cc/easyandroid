package cc.easyandroid.simple;

import android.os.Bundle;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import cc.easyandroid.easyhttp.EasyHttpUtils;
import cc.easyandroid.easyhttp.core.retrofit.CacheMode;
import cc.easyandroid.easyhttp.core.retrofit.Call;
import cc.easyandroid.easylog.EALog;
import cc.easyandroid.easymvp.presenter.EasyWorkPresenter;
import cc.easyandroid.easyutils.EasyUtils;

/**
 * http工具類，根據需要自己添加
 */
public class HttpUtils {
    private  static String TAG="qfangwanghk";
    public static <T> Call<T> creatPostCall(String url, Bundle bundle, EasyWorkPresenter<T> presenter) {
        Request request = EasyHttpUtils.getInstance().getOkHttpPostUtils().buildPostFormRequest(url, EasyUtils.BundleToMap(bundle));
        return EasyHttpUtils.getInstance().executeHttpRequestToCall(request, presenter.getDeliverResultType());
    }

    public static <T> Call<T> creatPostCall(String url, Bundle bundle, EasyWorkPresenter<T> presenter, Map<String, String> header) {
//        EasyHttpUtils.getInstance().getOkHttpClient().set
        Request request = EasyHttpUtils.getInstance().getOkHttpPostUtils().buildPostFormRequest(url, header, EasyUtils.BundleToMap(bundle));
        return EasyHttpUtils.getInstance().executeHttpRequestToCall(request, presenter.getDeliverResultType());
    }

    public static <T> Call<T> creatPostFileCall(String url, Bundle bundle, EasyWorkPresenter<T> presenter, Map<String, File> files, Map<String, String> header) {
//        Request request = EasyHttpUtils.getInstance().getOkHttpUpLoadUtil().buildMultipartFormRequest(url, files, EasyUtils.BundleToMap(bundle));
        Request request =  buildMultipartFormRequest(url, header, files, EasyUtils.BundleToMap(bundle));
        return EasyHttpUtils.getInstance().executeHttpRequestToCall(request, presenter.getDeliverResultType());
    }

    public static <T> Call<T> creatPostCall(String url, Map<String, String> map, EasyWorkPresenter<T> presenter) {
        Request request = EasyHttpUtils.getInstance().getOkHttpPostUtils().buildPostFormRequest(url, map);
        return EasyHttpUtils.getInstance().executeHttpRequestToCall(request, presenter.getDeliverResultType());
    }

    public static <T> Call<T> creatGetCallNoCache(String url, EasyWorkPresenter<T> presenter) {
        Request request = EasyHttpUtils.getInstance().getOkHttpGetUtils().buildGetRequest(url);
        return EasyHttpUtils.getInstance().executeHttpRequestToCall(request, presenter.getDeliverResultType());
    }

    /**
     * cache
     *
     * @param url
     * @param presenter
     * @param <T>
     * @return
     */
    public static <T> Call<T> creatGetCall(String url, EasyWorkPresenter<T> presenter) {
        HashMap<String, String> map = new HashMap<>();
        map.put("Cache-Mode", CacheMode.LOAD_NETWORK_ELSE_CACHE);
        map.put("Cache-Control", "max-age=340000");
        Request request = EasyHttpUtils.getInstance().getOkHttpGetUtils().buildGetRequest(url, map);
        return EasyHttpUtils.getInstance().executeHttpRequestToCall(request, presenter.getDeliverResultType());
    }
    public static <T> Call<T> creatGetCallFirstCache(String url, EasyWorkPresenter<T> presenter) {
        HashMap<String, String> map = new HashMap<>();
        map.put("Cache-Mode", CacheMode.LOAD_CACHE_ELSE_NETWORK);
        map.put("Cache-Control", "max-age=340000");
        Request request = EasyHttpUtils.getInstance().getOkHttpGetUtils().buildGetRequest(url, map);
        return EasyHttpUtils.getInstance().executeHttpRequestToCall(request, presenter.getDeliverResultType());
    }

    public static void postAsyn(String url, Map<String, File> files, Map<String, String> params, Callback responseCallback) {
        EasyHttpUtils.getInstance().getOkHttpUpLoadUtil().postAsyn(url, files, params, responseCallback);
    }

    public static <T> Call<T> creatGetCallUpLoadCall(String url, Map<String, File> files, Map<String, String> params, EasyWorkPresenter<T> presenter) {
        Request request = EasyHttpUtils.getInstance().getOkHttpUpLoadUtil().buildMultipartFormRequest(url, files, params);
        return EasyHttpUtils.getInstance().executeHttpRequestToCall(request, presenter.getDeliverResultType());
    }

    /**
     * 下载
     *
     * @param url         文件地址
     * @param destFileDir 保存的文件夹
     */
    public static void downFile(String url, String destFileDir) {
        EasyHttpUtils.getInstance().getOkHttpDownloadUtils().downloadAsyn(url, destFileDir);
    }



    public static String creatPostString(String url, Map<String, String> map) {
        try {
            return EasyHttpUtils.getInstance().getOkHttpPostUtils().post(url, map).body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }
    public static Request buildMultipartFormRequest(String url, Map<String, String> headers,Map<String, File> files, Map<String, String> params) {

        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                Log.e(TAG," key  "+ key);
                Log.e(TAG," params.get(key)   "+ params.get(key));

                builder.addFormDataPart(key, params.get(key));
            }
        }

        if (files != null && files.size() > 0) {// 文件
            RequestBody fileBody = null;
            for (String key : files.keySet()) {
                File file = files.get(key);
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                builder.addFormDataPart(key, fileName, fileBody);
            }
        }

        RequestBody requestBody = builder.build();
        Request.Builder requestBuilder=new Request.Builder().url(url).post(requestBody);
        //add headers
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                EALog.d("header key = %1$s ---- value = %2$s", key, headers.get(key));
                requestBuilder.addHeader(key, headers.get(key));
            }
        }
        return requestBuilder.build();
    }
    private static String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
}
