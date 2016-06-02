/*
 * Copyright 2016, The Android Open Source Project
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

package cc.easyandroid.easyclean.interactors;

import cc.easyandroid.easycore.EAResult;
import cc.easyandroid.easycore.EasyCall;
import cc.easyandroid.easycore.EasyHttpStateCallback;
import cc.easyandroid.easycore.EasyResponse;
import cc.easyandroid.easylog.EALog;
import cc.easyandroid.easymvp.exception.EasyException;

/**
 * Fetches the list of tasks.
 */
public class EasyHttpUseCase<T> extends UseCase<EasyHttpUseCase.RequestValues, EasyHttpUseCase.ResponseValue> {

    protected EasyCall<T> easyCall;

    private void cancelRequest() {
        if (easyCall != null && !easyCall.isCancel()) {
            easyCall.cancel();
        }
    }
    @Override
    protected void executeUseCase(final RequestValues values) {
        cancelRequest();
        EasyCall<T> originalEasyCall=values.getEasyCall();
        easyCall = originalEasyCall;
        easyCall.enqueue(new EasyHttpStateCallback<T>() {
            @Override
            public void onResponse(EasyResponse<T> easyResponse) {
                T t = easyResponse != null ? easyResponse.body() : null;
                String defaultMessage = easyResponse != null ? easyResponse.message() : "";//"服务器或网络异常";
                if (t == null) {
                    EALog.e("EasyAndroid", "t==null");
                    onFailure(new EasyException(defaultMessage));
                    return;
                } else if (t instanceof EAResult) {
                    EAResult kResult = (EAResult) t;
                    if (kResult == null || !kResult.isSuccess()) {
                        String errorMessage = kResult != null ? kResult.getEADesc() : defaultMessage;
                        onFailure(new EasyException(errorMessage));
                        return;
                    }
                }
                getUseCaseCallback().onSuccess(new ResponseValue(values.getTag(), t));
            }

            @Override
            public void onFailure(Throwable t) {
                getUseCaseCallback().onError(t);
            }

            @Override
            public void start() {

            }
        });
    }

    public static final class RequestValues<T> implements UseCase.RequestValues {
        private final EasyCall<T> easyCall;
        private final Object tag;

        public RequestValues(Object tag, EasyCall<T> easyCall) {
            this.tag = tag;
            this.easyCall = easyCall;
        }

        public Object getTag() {
            return tag;
        }

        public EasyCall<T> getEasyCall() {
            return easyCall;
        }

    }

    public static final class ResponseValue<T> implements UseCase.ResponseValue {

        private final T data;
        private final Object tag;

        public ResponseValue(Object tag, T data) {
            this.data = data;
            this.tag = tag;
        }

        public Object getTag() {
            return tag;
        }

        public T getData() {
            return data;
        }
    }
}
