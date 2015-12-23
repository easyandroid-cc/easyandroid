package cc.easyandroid.easyhttp.core;

import cc.easyandroid.easyhttp.core.retrofit.Converter;
import cc.easyandroid.easyhttp.core.retrofit.KOkHttpCall;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

public class EAOkHttpCall<T> extends KOkHttpCall<T> {
	private Request request;

	public EAOkHttpCall(OkHttpClient client, Converter<T> responseConverter, Request request) {
		super(client, responseConverter);
		this.request = request;
	}

	@Override
	public Request createRequest() {
		return request;
	}
	// We are a final type & this saves clearing state.
	@Override
	public KOkHttpCall<T> clone() {
		return new EAOkHttpCall<>(client, responseConverter,request);
	}
}
