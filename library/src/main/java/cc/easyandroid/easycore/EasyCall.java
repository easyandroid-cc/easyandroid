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
package cc.easyandroid.easycore;

import java.io.IOException;

import cc.easyandroid.easyhttp.core.retrofit.EasyResponse;

/**
 * An invocation of a Retrofit method that sends a request to a webserver and returns a response.
 * Each easyCall yields its own HTTP request and response pair. Use {@link #clone} to make multiple
 * calls with the same parameters to the same webserver; this may be used to implement polling or
 * to retry a failed easyCall.
 *
 * <p>Calls may be executed synchronously with {@link #execute}, or asynchronously with {@link
 * #enqueue}. In either case the easyCall can be canceled at any time with {@link #cancel}. A easyCall that
 * is busy writing its request or reading its response may receive a {@link IOException}; this is
 * working as designed.
 */
public interface EasyCall<T> extends Cloneable {
  EasyResponse<T> execute() throws IOException;//当前线程执行
  void enqueue(EasyHttpStateCallback<T> callback);//子线程执行
  void cancel();
  EasyCall<T> clone();
  boolean isCancel();
}
