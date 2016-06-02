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
package cc.easyandroid.easyclean.interactors.data;

import cc.easyandroid.easycore.EasyHttpStateCallback;

public interface EasyHttpDataSource<T> {


//    interface EasyHttpCallback<T> {
//        /**
//         * 请求成功
//         */
//        void onResponse(EasyResponse<T> easyResponse);
//
//        /**
//         * 请求失败
//         */
//        void onFailure(Throwable t);
//
//    }

    void executeEasyHttpRequest(EasyHttpStateCallback<T> easyHttpCallback);
}
