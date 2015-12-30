package cc.easyandroid.easymvp.presenter;

import android.os.Bundle;

import cc.easyandroid.easymvp.PresenterLoader;
import cc.easyandroid.easymvp.kabstract.KRxJavaPresenter;
import cc.easyandroid.easymvp.view.ISimpleThreadView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
					PresenterLoader<T> presenterLoader = getView().onCreatePresenterLoader(getPresenterId(), bundle);
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