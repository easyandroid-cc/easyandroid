/*
 * Copyright (C) 2012 Square, Inc.
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
package cc.easyandroid.easymvp.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

import cc.easyandroid.easymvp.PresenterLoader;
import cc.easyandroid.easymvp.kabstract.IController;

public abstract class EARunnable<T> implements Runnable {
	private final IController<T> callback;
	private final Executor callbackExecutor;
	private final AtomicBoolean mCancelled = new AtomicBoolean();

	public EARunnable(IController<T> callback, Executor callbackExecutor) {
		this.callback = callback;
		this.callbackExecutor = callbackExecutor;
	}

	public void cancel() {
		mCancelled.set(true);
	}

	public boolean isCancel() {
		return mCancelled.get();
	}

	@Override
	public final void run() {
		try {
			if (isCancel()) {
				return;
			}
			callbackExecutor.execute(new Runnable() {
				@Override
				public void run() {
					if (!isCancel()) {
						callback.start();
					}
				}
			});
			if (!isCancel()) {
				return;
			}
			final T wrapper = creatPresenterLoader().loadInBackground();
			if (!isCancel()) {
				return;
			}
			callbackExecutor.execute(new Runnable() {
				@Override
				public void run() {
					if (!isCancel()) {
						callback.deliverResult(wrapper);
						callback.completed();
					}
				}
			});
		} catch (Exception e) {
			if (!isCancel()) {
				return;
			}
			final Exception handled = e;
			callbackExecutor.execute(new Runnable() {
				@Override
				public void run() {
					if (!isCancel()) {
						callback.error(handled.getMessage());
					}
				}
			});
		}
	}

	public abstract PresenterLoader<T> creatPresenterLoader();
}
