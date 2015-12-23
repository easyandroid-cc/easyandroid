package cc.easyandroid.easymvp.kabstract;

import cc.easyandroid.easymvp.view.IView;

public interface Presenter<V extends IView> {

	public void initialize();

	public void resume();

	public void pause();

	public void destroy();

	public void cancel();

	public int getPresenterId() ;
	
 

}