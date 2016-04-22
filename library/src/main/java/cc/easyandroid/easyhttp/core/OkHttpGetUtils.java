package cc.easyandroid.easyhttp.core;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Map;

import cc.easyandroid.easylog.EALog;
import cc.easyandroid.easyutils.ArrayUtils;

public class OkHttpGetUtils extends OkHttpUtils {
    public OkHttpGetUtils(OkHttpClient client) {
        super(client);
    }

    /**
     * 同步的Get请求
     */
    public Response get(String url,Map<String, String> headers) throws IOException {
        final Request request = buildGetRequest(url,headers);
        Call call = client.newCall(request);
        Response execute = call.execute();
        return execute;
    }

    /**
     * 同步的Get请求
     */
    public String getAsString(String url) throws IOException {
        Response execute = get(url,null);
        return execute.body().string();
    }
    /**
     * 同步的Get请求
     */
    public String getAsString(String url,Map<String, String> headers) throws IOException {
        Response execute = get(url,headers);
        return execute.body().string();
    }

    /**
     * 	@Headers({ "Cache-Control: max-age=340000" //
    , "Cache-Mode:cache-else-network"//
     * 异步的get请求
     */
    public void getAsyn(String url, com.squareup.okhttp.Callback responseCallback) {
        Request request = buildGetRequest(url);
        client.newCall(request).enqueue(responseCallback);
    }

    public Request buildGetRequest(String url) {
        return buildGetRequest(url, null);
    }

    public Request buildGetRequest(String url, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder().url(url);
        if (!ArrayUtils.isEmpty(headers)) {
            for (String key : headers.keySet()) {
                EALog.d("header===key = %1$s ---- value = %2$s", key, headers.get(key));
                builder.addHeader(key, headers.get(key));
            }
        }
        return builder.build();
    }
}
