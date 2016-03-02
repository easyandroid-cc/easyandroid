package cc.easyandroid.easyhttp.core;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import cc.easyandroid.easylog.EALog;

public class OkHttpPostUtils extends OkHttpUtils {
    public OkHttpPostUtils(OkHttpClient client) {
        super(client);
    }

    private final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream;charset=utf-8");
    private final MediaType MEDIA_TYPE_STRING = MediaType.parse("text/plain;charset=utf-8");

    /**
     * 同步的Post请求
     */
    public Response post(String url, Map<String, String> paras) throws IOException {
        Request request = buildPostFormRequest(url, paras);
        Response response = client.newCall(request).execute();
        return response;

    }

    public Request buildPostRequest(String url, RequestBody body) {
        Request request = new Request.Builder().url(url).post(body).build();
        return request;
    }

    public Request buildPostFormRequest(String url, Map<String, String> paras) {
        return buildPostFormRequest(url, null, paras);
    }

    /**
     * post 请求构建request
     *
     * @param url     请求地址
     * @param headers 请求头
     * @param paras   请求参数
     * @return
     */
    public Request buildPostFormRequest(String url, Map<String, String> headers, Map<String, String> paras) {
        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        //add prars
        if (paras != null && paras.size() > 0) {
            for (String key : paras.keySet()) {
                EALog.d("para key = %1$s ---- value = %2$s", key, paras.get(key));
                formEncodingBuilder.add(key, paras.get(key));
            }
        }
        RequestBody requestBody = formEncodingBuilder.build();
        Request.Builder builder = new Request.Builder().url(url).post(requestBody);
        //add headers
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                EALog.d("header key = %1$s ---- value = %2$s", key, headers.get(key));
                builder.addHeader(key, headers.get(key));
            }
        }
        return builder.build();
    }

    /**
     * 异步的post请求
     */
    public void postAsyn(String url, Map<String, String> params, com.squareup.okhttp.Callback responseCallback) {
        Request request = buildPostFormRequest(url, params);
        client.newCall(request).enqueue(responseCallback);
    }

    /**
     * 同步的Post请求:直接将bodyStr以写入请求体
     */
    public Response post(String url, String bodyStr) throws IOException {
        RequestBody body = RequestBody.create(MEDIA_TYPE_STRING, bodyStr);
        Request request = buildPostRequest(url, body);
        Response response = client.newCall(request).execute();
        return response;
    }

    /**
     * 同步的Post请求:直接将bodyFile以写入请求体
     */
    public Response post(String url, File bodyFile) throws IOException {
        RequestBody body = RequestBody.create(MEDIA_TYPE_STREAM, bodyFile);
        Request request = buildPostRequest(url, body);
        Response response = client.newCall(request).execute();
        return response;
    }

    /**
     * 同步的Post请求
     */
    public Response post(String url, byte[] bodyBytes) throws IOException {
        RequestBody body = RequestBody.create(MEDIA_TYPE_STREAM, bodyBytes);
        Request request = buildPostRequest(url, body);
        Response response = client.newCall(request).execute();
        return response;
    }

    /**
     * 直接将bodyStr以写入请求体---1
     */
    public void postAsyn(String url, String bodyStr, MediaType type, com.squareup.okhttp.Callback responseCallback) {
        // MediaType type = MediaType.parse("text/plain;charset=utf-8");
        RequestBody body = RequestBody.create(type, bodyStr);
        Request request = buildPostRequest(url, body);
        client.newCall(request).enqueue(responseCallback);
    }

    /**
     * 直接将bodyStr以写入请求体--2
     */
    public void postAsyn(String url, String bodyStr, com.squareup.okhttp.Callback responseCallback) {
        postAsyn(url, bodyStr, MEDIA_TYPE_STRING, responseCallback);
    }

    /**
     * 直接将bodyFile以写入请求体---1
     */
    public void postAsyn(String url, File bodyFile, MediaType type, com.squareup.okhttp.Callback responseCallback) {
        RequestBody body = RequestBody.create(type, bodyFile);
        Request request = new Request.Builder().url(url).post(body).build();
        client.newCall(request).enqueue(responseCallback);
    }

    /**
     * 直接将bodyFile以写入请求体---2
     */
    public void postAsyn(String url, File bodyFile, com.squareup.okhttp.Callback responseCallback) {
        postAsyn(url, bodyFile, MEDIA_TYPE_STREAM, responseCallback);
    }

    /**
     * 直接将bodyBytes以写入请求体--1
     */
    public void postAsyn(String url, byte[] bodyBytes, MediaType type, com.squareup.okhttp.Callback responseCallback) {
        RequestBody body = RequestBody.create(type, bodyBytes);
        Request request = new Request.Builder().url(url).post(body).build();
        client.newCall(request).enqueue(responseCallback);
    }

    /**
     * 直接将bodyBytes以写入请求体--2
     */
    public void postAsyn(String url, byte[] bodyBytes, com.squareup.okhttp.Callback responseCallback) {
        postAsyn(url, bodyBytes, MEDIA_TYPE_STREAM, responseCallback);
    }
}
