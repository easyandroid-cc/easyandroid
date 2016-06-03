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

package cc.easyandroid.easyclean.presentation.presenter.base;

import cc.easyandroid.easyclean.presentation.view.IEasyView;

public class EasyBasePresenter<V extends IEasyView> implements EasyIPresenter<V> {
    private V mEasyView;

    @Override
    public void cancel() {

    }

    @Override
    public int getPresenterId() {
        return 0;
    }

    @Override
    public void attachView(V view) {
        this.mEasyView = view;
        onAttachView();
    }

    protected V getView(){
        return mEasyView;
    }
    @Override
    public void detachView() {
        if (mEasyView != null) {
            mEasyView = null;
        }
        onDetachView();
    }

    private void onAttachView() {

    }

    private void onDetachView() {

    }

    @Override
    public void execute() {

    }
}
