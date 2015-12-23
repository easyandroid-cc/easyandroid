/*
 * Copyright (C) 2015 Square, Inc.
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
package cc.easyandroid.easyhttp.core.retrofit;

import java.lang.reflect.Type;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;
import cc.easyandroid.easyhttp.pojo.EAResult;
import cc.easyandroid.easymvp.exception.MvpException;

/**
 * TODO docs
 */
public class EACallAdapterFactory {

	static final class CallOnSubscribe<T> implements Observable.OnSubscribe<Response<T>> {
		private final Call<T> originalCall;

		private CallOnSubscribe(Call<T> originalCall) {
			this.originalCall = originalCall;
		}

		@Override
		public void call(final Subscriber<? super Response<T>> subscriber) {
			// Since Call is a one-shot type, clone it for each new subscriber.
			final Call<T> call = originalCall.clone();

			// Attempt to cancel the call if it is still in-flight on
			// unsubscription.
			subscriber.add(Subscriptions.create(new Action0() {
				@Override
				public void call() {
					call.cancel();
				}
			}));

			call.enqueue(new Callback<T>() {
				@Override
				public void onResponse(Response<T> response) {
					if (subscriber.isUnsubscribed()) {
						return;
					}
					try {
						T t = response.body();
						if (t != null && t instanceof EAResult) {
							EAResult kResult = (EAResult) t;
							if (kResult == null || !kResult.isSuccess()) {
								subscriber.onError(new MvpException(kResult != null ? kResult.getFailureDesc() : "服务器或网络异常"));
								return;
							}
						}
						subscriber.onNext(response);
					} catch (Throwable t) {
						subscriber.onError(t);
						return;
					}
					subscriber.onCompleted();
				}

				@Override
				public void onFailure(Throwable t) {
					if (subscriber.isUnsubscribed()) {
						return;
					}
					subscriber.onError(t);
				}

				@Override
				public void onstart() {
					
				}
			});
		}
	}

	public static final class SimpleCallAdapter<T> {
		private final Type responseType;

		public SimpleCallAdapter(Type responseType) {
			this.responseType = responseType;
		}

		public Type responseType() {
			return responseType;
		}

		public Observable<T> adapt(Call<T> call) {
			return Observable.create(new CallOnSubscribe<>(call)) //
					.flatMap(new Func1<Response<T>, Observable<T>>() {
						@Override
						public Observable<T> call(Response<T> response) {
							if (response.isSuccess()) {
								return Observable.just(response.body());
							}
							return Observable.error(new KHttpException(response));
						}
					});
		}
	}

}
