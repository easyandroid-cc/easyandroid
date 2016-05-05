package cc.easyandroid.easyhttp.core;

import android.support.annotation.NonNull;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;

import cc.easyandroid.easyhttp.core.progress.ProgressListener;
import cc.easyandroid.easyhttp.core.progress.ProgressRequestBody;
import cc.easyandroid.easylog.EALog;
import cc.easyandroid.easyutils.ArrayUtils;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * okhttp Request factory
 */
public class OKHttp3RequestFactory {
    //创建get request ------------start

    /**
     * 创建get的request
     *
     * @param url     http url
     * @param headers http header
     * @return
     */
    public static Request createGetRequest(String url, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder().url(url);
        if (!ArrayUtils.isEmpty(headers)) {
            for (String key : headers.keySet()) {
                EALog.d("OKHttp3RequestFactory header key = %1$s ---- value = %2$s", key, headers.get(key));
                builder.addHeader(key, headers.get(key));
            }
        }
        return builder.build();
    }

    public static Request createGetRequest(String url) {
        return createGetRequest(url, null);
    }
    //创建get request----------------- end


    //创建post request----------------- start

    /**
     * 创建post的request
     *
     * @param url     http url
     * @param headers http header
     * @param paras   http paras
     * @return
     */
    public static Request createPostRequest(String url, Map<String, String> headers, Map<String, String> paras) {
        FormBody.Builder fbBuilder = new FormBody.Builder();
        if (paras != null && paras.size() > 0) {
            for (String key : paras.keySet()) {
                EALog.d("OKHttp3RequestFactory para key = %1$s ---- value = %2$s", key, paras.get(key));
                fbBuilder.add(key, paras.get(key));
            }
        }
        RequestBody requestBody = fbBuilder.build();
        Request.Builder builder = new Request.Builder().url(url).post(requestBody);
        //add headers
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                EALog.d("OKHttp3RequestFactory header key = %1$s ---- value = %2$s", key, headers.get(key));
                builder.addHeader(key, headers.get(key));
            }
        }
        return builder.build();
    }

    public static Request createPostRequest(String url, Map<String, String> paras) {
        return createPostRequest(url, null, paras);
    }

    //创建post request----------------- end


    //创建post 多文件上传 request----------------- start
    public static Request createMultipartRequest(String url, Map<String, String> headers, Map<String, String> params, Map<String, File> files) {
        MultipartBody.Builder mbBuilder = getMultipartBuilder(headers, params, files);
        RequestBody requestBody = mbBuilder.build();
        Request.Builder requestBuilder = new Request.Builder().url(url).post(requestBody);
        //add headers
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                EALog.d("header key = %1$s ---- value = %2$s", key, headers.get(key));
                requestBuilder.addHeader(key, headers.get(key));
            }
        }
        return requestBuilder.build();
    }

    /**
     * 上传文件有进度提示
     *
     * @param url
     * @param headers
     * @param params
     * @param files
     * @param listener
     * @return
     */
    public static Request createMultipartRequest(String url, Map<String, String> headers, Map<String, String> params, Map<String, File> files, ProgressListener listener) {
        MultipartBody.Builder mbBuilder = getMultipartBuilder(headers, params, files);
        RequestBody requestBody = new ProgressRequestBody(mbBuilder.build(), listener);
        Request.Builder requestBuilder = new Request.Builder().url(url).post(requestBody);
        //add headers
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                EALog.d("header key = %1$s ---- value = %2$s", key, headers.get(key));
                requestBuilder.addHeader(key, headers.get(key));
            }
        }
        return requestBuilder.build();
    }

    @NonNull
    private static MultipartBody.Builder getMultipartBuilder(Map<String, String> headers, Map<String, String> params, Map<String, File> files) {
        MultipartBody.Builder mbBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                EALog.d("OKHttp3RequestFactory createMultipartRequest header key = %1$s ---- value = %2$s", key, params.get(key));
                mbBuilder.addFormDataPart(key, params.get(key));
            }
        }
        if (files != null && files.size() > 0) {// 文件
            RequestBody fileBody;
            for (String key : files.keySet()) {
                File file = files.get(key);
                String fileName = file.getName();
                EALog.d("OKHttp3RequestFactory createMultipartRequest header key = %1$s ---- value = %2$s", key, headers.get(key));
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                mbBuilder.addFormDataPart(key, fileName, fileBody);
            }
            fileBody = null;
        }
        return mbBuilder;
    }

    private static String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
    //创建post 多文件上传 request----------------- end


}
