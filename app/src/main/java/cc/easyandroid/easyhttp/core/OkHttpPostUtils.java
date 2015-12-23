package cc.easyandroid.easyhttp.core;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

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
        FormEncodingBuilder builder = new FormEncodingBuilder();
        if (paras != null && paras.size() > 0) {
            for (String key : paras.keySet()) {
                System.out.println("key=" + key+"-----value="+paras.get(key));
                builder.add(key, paras.get(key));
            }
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder().url(url).post(requestBody).build();
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
