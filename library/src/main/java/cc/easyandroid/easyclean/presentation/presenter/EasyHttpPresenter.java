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

package cc.easyandroid.easyclean.presentation.presenter;

import cc.easyandroid.easyclean.UseCase;
import cc.easyandroid.easyclean.UseCaseHandler;
import cc.easyandroid.easyclean.domain.easyhttp.EasyHttpContract;
import cc.easyandroid.easyclean.domain.easyhttp.EasyHttpUseCase;
import cc.easyandroid.easyclean.presentation.presenter.base.EasyBasePresenter;
import cc.easyandroid.easylog.EALog;
import cc.easyandroid.easyutils.TypeUtils;

/**
 * Listens to user actions from the UI ({@link EasyHttpContract}), retrieves the data and updates
 * the UI as required. view by  attachView(view) method incoming;
 */
public class EasyHttpPresenter<T> extends EasyBasePresenter<EasyHttpContract.View<T>> implements EasyHttpContract.Presenter<T> {

    protected final UseCaseHandler mUseCaseHandler = UseCaseHandler.getInstance();
    private final EasyHttpUseCase<T> mEasyHttpUseCase;


    public EasyHttpPresenter(EasyHttpUseCase<T> easyHttpUseCase) {
        mEasyHttpUseCase = easyHttpUseCase;
    }

    /**
     * 用户传入RequestValues执行网络请求
     *
     * @param requestValues 用户请求的参数
     */
    @Override
    public void execute(EasyHttpUseCase.RequestValues requestValues) {//这里是执行网络请求
        setRequestValues(requestValues);
        handleRequest(requestValues);
    }

    @Override
    public void reExecute() {
        execute();
    }

    private EasyHttpUseCase.RequestValues mRequestValues;

    private void handleRequest(final EasyHttpUseCase.RequestValues requestValues) {
        if (isViewAttached())
            getView().onStart(requestValues.getTag());
        //mEasyHttpUseCase 自己会判断是否有call在运行，如果有，他会自己取消之前的
        mUseCaseHandler.execute(mEasyHttpUseCase, requestValues, new UseCase.UseCaseCallback<EasyHttpUseCase.ResponseValue<T>>() {
            @Override
            public void onSuccess(EasyHttpUseCase.ResponseValue<T> response) {
                if (isViewAttached())
                    getView().onSuccess(requestValues.getTag(), response.getData());
            }

            @Override
            public void onError(Throwable t) {
                if (isViewAttached())
                    getView().onError(requestValues.getTag(), t);
            }
        });
    }

    public void setRequestValues(EasyHttpUseCase.RequestValues requestValues) {
        mRequestValues = requestValues;
    }

    @Override
    public void execute() {
        if (mRequestValues != null) {
            handleRequest(mRequestValues);
        } else {
            throw new IllegalArgumentException("must be call setRequestValues(EasyHttpUseCase.RequestValues<T> requestValues) method");
        }
        EALog.e("EasyHttpPresenter", "execute");
    }

    @Override
    protected void onDetachView() {
        cancel();
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        mEasyHttpUseCase.cancle();
    }

    @Override
    protected void onAttachView(EasyHttpContract.View<T> view) {
        super.onAttachView(view);
        TypeUtils.newInstance(view).getViewType();
    }
}
