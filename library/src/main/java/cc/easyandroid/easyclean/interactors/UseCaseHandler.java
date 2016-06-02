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


import java.io.IOException;

import cc.easyandroid.easymvp.exception.EasyException;

/**
 * Runs {@link UseCase}s using a {@link UseCaseScheduler}.
 */
public class UseCaseHandler {

    private static UseCaseHandler INSTANCE;

    private final UseCaseScheduler mUseCaseScheduler;

    public UseCaseHandler(UseCaseScheduler useCaseScheduler) {
        mUseCaseScheduler = useCaseScheduler;
    }

    public <T extends UseCase.RequestValues, R extends UseCase.ResponseValue> void execute(
            final UseCase<T, R> useCase, T values, UseCase.UseCaseCallback<R> callback) {
        useCase.setRequestValues(values);
        useCase.setUseCaseCallback(new UiCallbackWrapper(callback, this));

        // The network request might be handled in a different thread so make sure
        // Espresso knows
        // that the app is busy until the response is handled.

        mUseCaseScheduler.execute(new Runnable() {
            @Override
            public void run() {
                useCase.run();
            }
        });
 okhttp3.Call rawCall=null;
        try {
            rawCall.execute();
            rawCall.cancel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <V extends UseCase.ResponseValue> void notifyResponse(final V response, final UseCase.UseCaseCallback<V> useCaseCallback) {
        mUseCaseScheduler.notifyResponse(response, useCaseCallback);
    }

    private <V extends UseCase.ResponseValue> void notifyError(final EasyException e, final UseCase.UseCaseCallback<V> useCaseCallback) {
        mUseCaseScheduler.onError(e, useCaseCallback);
    }

    private static final class UiCallbackWrapper<V extends UseCase.ResponseValue> implements UseCase.UseCaseCallback<V> {
        private final UseCase.UseCaseCallback<V> mCallback;
        private final UseCaseHandler mUseCaseHandler;

        public UiCallbackWrapper(UseCase.UseCaseCallback<V> callback, UseCaseHandler useCaseHandler) {
            mCallback = callback;
            mUseCaseHandler = useCaseHandler;
        }

        @Override
        public void onSuccess(V response) {
            mUseCaseHandler.notifyResponse(response, mCallback);
        }

        @Override
        public void onError(EasyException e) {
            mUseCaseHandler.notifyError(e, mCallback);
        }
    }

    public static UseCaseHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UseCaseHandler(new UseCaseThreadPoolScheduler());
        }
        return INSTANCE;
    }
}
