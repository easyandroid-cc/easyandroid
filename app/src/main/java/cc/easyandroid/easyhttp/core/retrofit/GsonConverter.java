/*
 * Copyright (C) 2012 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.easyandroid.easyhttp.core.retrofit;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import cc.easyandroid.easycache.volleycache.Cache;
import cc.easyandroid.easyhttp.pojo.EAResult;

import com.google.gson.TypeAdapter;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.internal.Util;

public final class GsonConverter<T> implements Converter<T> {
	public static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
	public static final String UTF8 = "UTF-8";
	private final TypeAdapter<T> typeAdapter;
	private final Cache cache;

	public GsonConverter(TypeAdapter<T> adapter, Cache cache) {
		this.typeAdapter = adapter;
		this.cache = cache;
	}

	public Cache getCache() {
		return cache;
	}

	@Override
	public T fromBody(ResponseBody body) throws IOException {
		Reader reader = body.charStream();
		try {
			return typeAdapter.fromJson(reader);
		} finally {
			closeQuietly(reader);
		}
	}

	public T fromBody(ResponseBody value, Request request) throws IOException {
		String string = value.string();
		System.out.println("网络请求到的字符串:" + string);
		Reader reader = new InputStreamReader((new ByteArrayInputStream(string.getBytes(UTF8))), Util.UTF_8);
		try {
			T t = typeAdapter.fromJson(reader);
			System.out.println("转换的最终对象：" + t);
			String mimeType = value.contentType().toString();
			parseCache(request, t, string, mimeType);
			return t;
		} finally {
			closeQuietly(reader);
		}
	}

	private void parseCache(Request request, T object, String string, String mimeType) throws UnsupportedEncodingException {
		com.squareup.okhttp.CacheControl cacheControl = request.cacheControl();
		if (cacheControl != null) {
			if (!cacheControl.noCache() && !cacheControl.noStore()) {
				if (object instanceof EAResult) {
					EAResult kResult = (EAResult) object;
					if (kResult != null && kResult.isSuccess()) {
						long now = System.currentTimeMillis();
						long maxAge = cacheControl.maxAgeSeconds();
						long softExpire = now + maxAge * 1000;
						System.out.println("缓存时长:" + (softExpire - now) / 1000 + "秒");
						Cache.Entry entry = new Cache.Entry();
						entry.softTtl = softExpire;
						entry.ttl = entry.softTtl;
						// entry.serverDate = serverDate;
						// entry.responseHeaders = headers;
						entry.mimeType = mimeType;
						System.out.println("request.cacheControl()==" + request.cacheControl());
						entry.data = string.getBytes(UTF8);
						cache.put(request.urlString(), entry);
					}
				}
			}
		}
	}

	static void closeQuietly(Closeable closeable) {
		if (closeable == null)
			return;
		try {
			closeable.close();
		} catch (IOException ignored) {
		}
	}


}
