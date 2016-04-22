package cc.easyandroid.easymvp.kabstract;

import android.os.Bundle;

import cc.easyandroid.easymvp.utils.RxUtils;
import cc.easyandroid.easymvp.view.ISimpleView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public abstract class KRxJavaPresenter<V extends ISimpleView<T>, T> extends KPresenter<V, T> {

	protected KSubscriber subscriber;

	@Override
	protected void onCancel() {
		super.onCancel();
		unsubscribe();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unsubscribe();
	}

	private void unsubscribe() {
		RxUtils.unsubscribe(subscriber);
	}

	public abstract Observable<T> creatObservable(Bundle bundle);

	// observable.cache() //观察者 会回调多次，但是只会调用一次网络
	@Deprecated
	public void loadData(Bundle bundle) {
		execute(bundle);
	}

	public void execute(Bundle bundle) {
		cancel();// 先取消之前的事件
		Observable<T> observable = creatObservable(bundle).subscribeOn(Schedulers.io())//
				.observeOn(AndroidSchedulers.mainThread());
		if (observable == null) {
			throw new IllegalArgumentException("please Override onCreatObservable method, And can not be null，");
		}
		subscriber = new KSubscriber(this.mController);
		observable.subscribe(subscriber);
	}

	public void execute() {
		execute(null);
	}

	@Deprecated
	public void loadData() {
		execute(null);
	}

	public class KSubscriber extends Subscriber<T> {
		IController<T> mController;

		public KSubscriber(IController<T> controller) {
			this.mController = controller;
		}

		@Override
		public void onStart() {
			super.onStart();
			this.mController.start();
		}

		@Override
		public void onNext(T s) {
			this.mController.deliverResult(s);
		}

		@Override
		public void onCompleted() {
			this.mController.completed();
		}

		@Override
		public void onError(Throwable e) {
			this.mController.error(e);
		}
	}
}