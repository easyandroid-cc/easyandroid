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
package cc.easyandroid.easyhttp.core.retrofit;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import cc.easyandroid.easycache.volleycache.Cache;
import cc.easyandroid.easyhttp.core.StateCodeHandler;

/**
 * A {@linkplain Converter.Factory converter} which uses Gson for JSON.
 */
public final class GsonConverterFactory implements Converter.Factory {
    /**
     * Create an instance using a default {@link Gson} instance for conversion. Encoding to JSON and decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static GsonConverterFactory create() {
        return create(new Gson(), null, null);
    }

    /**
     * Create an instance using {@code gson} for conversion. Encoding to JSON and decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static GsonConverterFactory create(Gson gson, Cache cache, StateCodeHandler stateCodeProcessing) {
        return new GsonConverterFactory(gson, cache, stateCodeProcessing);
    }

    public static GsonConverterFactory create(Cache cache) {
        return create(new Gson(), cache, null);
    }

    private final Gson gson;
    private final Cache cache;
    private final StateCodeHandler stateCodeProcessing;

    private GsonConverterFactory(Gson gson, Cache cache, StateCodeHandler stateCodeProcessing) {
        if (gson == null)
            throw new NullPointerException("gson == null");
        this.gson = gson;
        this.cache = cache;
        this.stateCodeProcessing = stateCodeProcessing;
    }

    /**
     * Create a converter for {@code type}.
     */
    @Override
    public Converter<?> get(Type type) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonConverter<>(adapter, cache, stateCodeProcessing);
    }
}
