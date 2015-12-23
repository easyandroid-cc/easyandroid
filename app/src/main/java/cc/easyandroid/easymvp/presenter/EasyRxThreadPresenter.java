package cc.easyandroid.easymvp.presenter;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import android.os.Bundle;
import cc.easyandroid.easymvp.PresenterLoader;
import cc.easyandroid.easymvp.kabstract.KRxJavaPresenter;
import cc.easyandroid.easymvp.view.ISimpleThreadView;

public class EasyRxThreadPresenter<T> extends KRxJavaPresenter<ISimpleThreadView<T>, T> {

	@Override
	public Observable<T> creatObservable(Bundle bundle) {
		return getObservable(bundle);
	}

	protected Observable<T> getObservable(final Bundle bundle) {
		return Observable.create(new Observable.OnSubscribe<T>() {

			@Override
			public void call(Subscriber<? super T> sub) {
				if (!sub.isUnsubscribed()) {
					PresenterLoader<T> presenterLoader = getView().onCreatPresenterLoader(getPresenterId(), bundle);
					try {
						T t = presenterLoader.loadInBackground();
						sub.onNext(t);
						sub.onCompleted();
					} catch (Exception e) {
						sub.onError(e);
					}
				}
			}
		}).subscribeOn(Schedulers.io())//
				.observeOn(AndroidSchedulers.mainThread());
	}
}  