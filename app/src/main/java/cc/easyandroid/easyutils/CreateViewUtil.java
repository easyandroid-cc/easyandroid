package cc.easyandroid.easyutils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CreateViewUtil {
	public static View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View rootView, int resource) {
		if (rootView == null) {
			rootView = inflater.inflate(resource, container, false);
		} else {
			ViewGroup group = (ViewGroup) rootView.getParent();
			if (group != null) {
				group.removeView(rootView);
			}
		}
		return rootView;
	}

}