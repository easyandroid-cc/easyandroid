package cc.easyandroid.easymvp.presenter;

import android.os.Bundle;

import cc.easyandroid.easycore.EasyCall;
import cc.easyandroid.easymvp.kabstract.KOKHttpPresenter;
import cc.easyandroid.easymvp.view.ISimpleCallView;

public class EasyWorkPresenter<T> extends KOKHttpPresenter<ISimpleCallView<T>, T> {

    @Override
    protected EasyCall<T> createCall(Bundle bundle) {
        return getView().onCreateCall(getPresenterId(), bundle);
    }

}
