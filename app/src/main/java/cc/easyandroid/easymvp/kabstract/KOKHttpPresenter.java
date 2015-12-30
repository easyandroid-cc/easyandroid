package cc.easyandroid.easymvp.kabstract;

import android.os.Bundle;
import cc.easyandroid.easyhttp.core.retrofit.Call;
import cc.easyandroid.easyhttp.core.retrofit.Callback;
import cc.easyandroid.easyhttp.core.retrofit.Response;
import cc.easyandroid.easyhttp.pojo.EAResult;
import cc.easyandroid.easymvp.view.ISimpleView;

public abstract class KOKHttpPresenter<V extends ISimpleView<T>, T> extends KPresenter<V, T> {
	protected Call<T> call;

	@Override
	protected void onCancel() {
		super.onCancel();
		cancelRequest();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		cancelRequest();
	}

	private void cancelRequest() {
		if (call != null) {
			call.cancel();
		}
	}

	public abstract Call<T> createCall(Bundle bundle);

	// observable.cache() //观察者 会回调多次，但是只会调用一次网络
	public void execute(Bundle bundle) {
		cancel();// 先取消之前的事件
		Call<T> originalCall = createCall(bundle);
		if (originalCall == null) {
			throw new IllegalArgumentException("please Override onCreateCall method, And can not be null，");
		}
		call = originalCall.clone();

		call.enqueue(new kCallback(mController));
	 
	}

	public void execute() {
		execute(null);
	}

	public class kCallback implements Callback<T> {
		IController<T> mController;

		public kCallback(IController<T> controller) {
			this.mController = controller;
		}

		@Override
		public void onResponse(Response<T> response) {
			T t = response.body();
			if (t != null && t instanceof EAResult) {
				EAResult kResult = (EAResult) t;
				if (kResult == null || !kResult.isSuccess()) {
//					kResult.
					mController.error(kResult != null ? kResult.getFailureDesc() : "服务器或网络异常");
					return;
				}
			}
			mController.deliverResult(t);
			mController.completed();
		}

		@Override
		public void onFailure(Throwable t) {
			mController.error(t.getMessage());
		}

		@Override
		public void onstart() {
			mController.start();
		}
	}
}
