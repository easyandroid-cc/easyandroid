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

import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * Convert objects to and from their representation as HTTP bodies. Register a
 * converter with Retrofit using
 */
public interface Converter<T> {
    /**
     * @param value
     * @param request
     * @param fromNetWork 是否来自网络一般来自网络的才进行缓存
     * @return
     * @throws IOException
     */
    T fromBody(ResponseBody value, Request request,boolean fromNetWork) throws IOException;

//    Cache getCache();
}
