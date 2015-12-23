package cc.easyandroid.easyhttp.core;

import java.io.IOException;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class OkHttpGetUtils extends OkHttpUtils {
	public OkHttpGetUtils(OkHttpClient client) {
		super(client);
	}
	/**
	 * 同步的Get请求
	 */
	public Response get(String url) throws IOException {
		final Request request = buildGetRequest(url);
		Call call = client.newCall(request);
		Response execute = call.execute();
		return execute;
	}

	/**
	 * 同步的Get请求
	 */
	public String getAsString(String url) throws IOException {
		Response execute = get(url);
		return execute.body().string();
	}

	/**
	 * 异步的get请求
	 */
	public void getAsyn(String url, com.squareup.okhttp.Callback responseCallback) {
		Request request = buildGetRequest(url);
		client.newCall(request).enqueue(responseCallback);
	}

	public Request buildGetRequest(String url) {
		return new Request.Builder().url(url).build();
	}
}
