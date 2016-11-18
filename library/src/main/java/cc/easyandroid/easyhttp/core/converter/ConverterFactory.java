/*
 * Copyright (C) 2015 Square, Inc.
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

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import cc.easyandroid.easycache.EasyHttpCache;

/**
 */
public final class ConverterFactory {


    /**
     * Create an instance using {@code gson} for conversion. Encoding to JSON and decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static ConverterFactory create(Gson gson, EasyHttpCache mEasyHttpCache) {
        return new ConverterFactory(gson, mEasyHttpCache);
    }


    private final Gson gson;
    private final EasyHttpCache mEasyHttpCache;

    private ConverterFactory(Gson gson, EasyHttpCache easyHttpCache) {
        if (gson == null)
            throw new NullPointerException("gson == null");
        if (easyHttpCache == null)
            throw new NullPointerException("easyHttpCache == null");
        this.gson = gson;
        this.mEasyHttpCache = easyHttpCache;
    }

    /**
     * Create a converter for {@code type}.
     */
    public Converter<?> getGsonConverter(Type type) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonConverter<>(adapter,mEasyHttpCache);
    }

    /**
     * Create a converter for {@code type}.
     */
    public Converter<?> getStringConverter() {
        return new StringConverter(mEasyHttpCache);
    }

}
