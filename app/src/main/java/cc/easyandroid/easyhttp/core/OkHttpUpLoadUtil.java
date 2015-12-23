package cc.easyandroid.easyhttp.core;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/**
 * 文件上传工具类
 * 
 * @author chenguoping
 *
 */
public class OkHttpUpLoadUtil extends OkHttpUtils {
	public OkHttpUpLoadUtil(OkHttpClient client) {
		super(client);
	}

	/**
	 * 同步基于post的文件上传:上传多个文件以及携带key-value对：主方法
	 */
	public Response post(String url, Map<String, File> files, Map<String, String> params) throws IOException {
		Request request = buildMultipartFormRequest(url, files, params);
		return client.newCall(request).execute();
	}

	/**
	 * 异步基于post的文件上传:主方法
	 */
	public void postAsyn(String url, Map<String, File> files, Map<String, String> params, Callback responseCallback) {
		Request request = buildMultipartFormRequest(url, files, params);
		client.newCall(request).enqueue(responseCallback);
	}

	public Request buildMultipartFormRequest(String url, Map<String, File> files, Map<String, String> params) {

		MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
		if (params != null && params.size() > 0) {
			for (String key : params.keySet()) {
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
		return new Request.Builder().url(url).post(requestBody).build();
	}

	/**
	 * 比较原始的方法，暂时不用
	 * 
	 * @param url
	 * @param files
	 * @param params
	 * @return
	 */
	public Request buildMultipartFormRequest1(String url, Map<String, File> files, Map<String, String> params) {
		MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);

		for (String key : params.keySet()) {
			builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""), RequestBody.create(null, params.get(key)));
		}
		RequestBody fileBody = null;
		for (String key : files.keySet()) {
			File file = files.get(key);
			String fileName = file.getName();
			fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
			// TODO 根据文件名设置contentType
			builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\"; filename=\"" + fileName + "\""), fileBody);
		}

		RequestBody requestBody = builder.build();
		return new Request.Builder().url(url).post(requestBody).build();
	}

	private String guessMimeType(String path) {
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		String contentTypeFor = fileNameMap.getContentTypeFor(path);
		if (contentTypeFor == null) {
			contentTypeFor = "application/octet-stream";
		}
		return contentTypeFor;
	}
}
