package cc.easyandroid.easyui.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import android.widget.BaseAdapter;

public abstract class ArrayAdapter<T> extends BaseAdapter {

	public List<T> mItems = new ArrayList<T>();

	public ArrayAdapter() {
		this(null);
	}

	public ArrayAdapter(List<T> items) {
		mItems = new ArrayList<T>();
		if (items != null) {
			mItems.addAll(items);
		}
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public T getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void addAll(Collection<? extends T> items) {
		mItems.addAll(items);
		notifyDataSetChanged();
	}

	public void addAll(@SuppressWarnings("unchecked") T... items) {
		Collections.addAll(mItems, items);
		notifyDataSetChanged();
	}

	public void addAll(int position, Collection<? extends T> items) {
		mItems.addAll(position, items);
		notifyDataSetChanged();
	}

	public void addAll(int position, @SuppressWarnings("unchecked") T... items) {
		for (int i = position; i < (items.length + position); i++) {
			mItems.add(i, items[i]);
		}
		notifyDataSetChanged();
	}

	public void clear() {
		if (mItems != null) {
			mItems.clear();
			notifyDataSetChanged();
		}
	}

	public void remove(int position) {
		mItems.remove(position);
		notifyDataSetChanged();
	}

	public void setmItems(List<T> items) {
		if (items == null || items.size() == 0) {
			return;
		}
		this.mItems.clear();
		this.mItems.addAll(items);
		notifyDataSetChanged();
	}

	public void removePositions(Collection<Integer> positions) {
		ArrayList<Integer> positionsList = new ArrayList<Integer>(positions);
		Collections.sort(positionsList);
		Collections.reverse(positionsList);
		for (int position : positionsList) {
			mItems.remove(position);
		}
		notifyDataSetChanged();
	}

	public void removeAll(Collection<T> items) {
		mItems.removeAll(items);
		notifyDataSetChanged();
	}

	public void removeAll() {
		mItems.removeAll(mItems);
		notifyDataSetChanged();
	}
}
