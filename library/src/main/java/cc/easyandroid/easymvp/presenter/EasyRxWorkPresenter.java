package cc.easyandroid.easymvp.presenter;

import rx.Observable;
import android.os.Bundle;
import cc.easyandroid.easymvp.kabstract.KRxJavaPresenter;
import cc.easyandroid.easymvp.view.ISimpleNetWorkView;

public class EasyRxWorkPresenter<T> extends KRxJavaPresenter<ISimpleNetWorkView<T>, T> {

	@Override
	public Observable<T> creatObservable(Bundle bundle) {
		return getView().onCreatObservable(getPresenterId(), bundle);
	}
	
}
