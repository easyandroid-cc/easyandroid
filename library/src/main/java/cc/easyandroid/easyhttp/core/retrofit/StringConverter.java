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

import com.squareup.okhttp.Request;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import cc.easyandroid.easycache.volleycache.Cache;
import cc.easyandroid.easylog.EALog;

public final class StringConverter implements Converter<java.lang.String> {
    public static final String UTF8 = "UTF-8";
    private final Cache cache;

    public StringConverter(Cache cache) {
        this.cache = cache;
    }

    public Cache getCache() {
        return cache;
    }

    @Override
    public String fromBody(ResponseBody body) throws IOException {
        try {
            return body.string();
        } finally {
        }
    }

    public String fromBody(ResponseBody value, Request request) throws IOException {
        String string = value.string();
        try {
            String mimeType = value.contentType().toString();
            parseCache(request, string, mimeType);
            return string;
        } finally {
        }
    }

    private void parseCache(Request request, String string, String mimeType) throws UnsupportedEncodingException {
        com.squareup.okhttp.CacheControl cacheControl = request.cacheControl();
        if (cacheControl != null) {
            if (!cacheControl.noCache() && !cacheControl.noStore()) {
                long now = System.currentTimeMillis();
                long maxAge = cacheControl.maxAgeSeconds();
                long softExpire = now + maxAge * 1000;
                EALog.d("缓存时长: %1$s秒", (softExpire - now) / 1000 + "");
                Cache.Entry entry = new Cache.Entry();
                entry.softTtl = softExpire;
                entry.ttl = entry.softTtl;
                entry.mimeType = mimeType;
                entry.data = string.getBytes(UTF8);
                cache.put(request.urlString(), entry);
            }
        }
    }
}
