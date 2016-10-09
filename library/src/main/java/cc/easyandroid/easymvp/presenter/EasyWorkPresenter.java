package cc.easyandroid.easymvp.presenter;

import android.os.Bundle;

import cc.easyandroid.easycore.EasyCall;
import cc.easyandroid.easymvp.kabstract.KOKHttpPresenter;
import cc.easyandroid.easymvp.view.ISimpleCallView;
@Deprecated
/**
 * 请使用easyclean中presenter
 */
public class EasyWorkPresenter<T> extends KOKHttpPresenter<ISimpleCallView<T>, T> {

    @Override
    protected EasyCall<T> createCall(Object tag,Bundle bundle) {
        return getView().onCreateCall(tag, bundle);
    }

}
