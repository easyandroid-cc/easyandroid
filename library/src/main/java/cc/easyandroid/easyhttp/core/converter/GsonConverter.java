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

import com.google.gson.TypeAdapter;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import cc.easyandroid.easycache.EasyHttpCache;
import cc.easyandroid.easycore.EAResult;
import cc.easyandroid.easyhttp.core.StateCodeHandler;
import cc.easyandroid.easylog.EALog;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;

public final class GsonConverter<T> implements Converter<T> {
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    public static final String UTF8 = "UTF-8";
    private final TypeAdapter<T> typeAdapter;
//    private final Cache cache;
    private final StateCodeHandler stateCodeProcessing;

    public GsonConverter(TypeAdapter<T> adapter,  StateCodeHandler stateCodeProcessing) {
        this.typeAdapter = adapter;
//        this.cache = cache;
        this.stateCodeProcessing = stateCodeProcessing;
    }

//    public Cache getCache() {
//        return cache;
//    }

    public T fromBody(ResponseBody value, Request request, boolean fromNetWork) throws IOException {
        String string = value.string();
        EALog.d("Network request string : %1$s", string);
        System.out.println("easyandroid= " + string);
        Reader reader = new InputStreamReader((new ByteArrayInputStream(string.getBytes(UTF8))), Util.UTF_8);
        try {
            T t = typeAdapter.fromJson(reader);
            EALog.d(" Finally converted to : %1$s", t.toString());
            String mimeType = value.contentType().toString();
            parseCache(request, t, string, mimeType, fromNetWork);
            parseStateCode(t);
            return t;
        } finally {
            closeQuietly(reader);
        }
    }

    private void parseStateCode(T object) {
        if (object instanceof EAResult) {
            if (stateCodeProcessing != null) {
                EAResult eaResult = (EAResult) object;
                if (eaResult != null) {
                    stateCodeProcessing.handleCode(eaResult.getEACode());
                }
            }
        }
    }

    private void parseCache(Request request, T object, String string, String mimeType, boolean fromNetWork) throws UnsupportedEncodingException {
        if(fromNetWork){
            EasyHttpCache.getInstance().put(request,object,string.getBytes(UTF8));
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
