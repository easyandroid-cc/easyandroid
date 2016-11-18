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
package cc.easyandroid.easyhttp.core.converter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import cc.easyandroid.easycache.EasyHttpCache;
import okhttp3.Request;
import okhttp3.ResponseBody;

public final class StringConverter implements Converter<java.lang.String> {
    public static final String UTF8 = "UTF-8";
    public final EasyHttpCache mEasyHttpCache;

    public StringConverter(EasyHttpCache easyHttpCache) {
        this.mEasyHttpCache = easyHttpCache;
    }


    @Override
    public String fromBody(ResponseBody value, Request request, boolean fromNetWork) throws IOException {
        String string = value.string();
        try {
            String mimeType = value.contentType().toString();
            parseCache(request, string, mimeType, fromNetWork);
            return string;
        } finally {
        }
    }

    private void parseCache(Request request, String string, String mimeType, boolean fromNetWork) throws UnsupportedEncodingException {
        if (fromNetWork) {
            mEasyHttpCache.put(request, string, string.getBytes(UTF8));
        }
    }
}
