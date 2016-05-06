package cc.easyandroid.easymvp.kabstract;

import cc.easyandroid.easymvp.view.IView;

public interface Presenter<V extends IView> {

    void cancel();

    int getPresenterId();

    void attachView(V view);

    void detachView();
}