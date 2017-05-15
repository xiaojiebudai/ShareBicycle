package com.sharebicycle.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharebicycle.utils.DialogUtils;


/**
 * fragment基类
 * 
 * @author xl
 * @date:2016-7-25上午10:56:48
 * @description
 */
public abstract class FatherFragment extends Fragment {

	protected View mGroup;
	protected LayoutInflater mInflater;
	/**
	 * 设置布局id
	 * 
	 * @return
	 */
	protected abstract int getLayoutId();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		mInflater = inflater;
		if (mGroup == null) {
			mGroup = inflater.inflate(getLayoutId(), container, false);
			initView();
		}
		return mGroup;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	/** 初始化控件 */
	protected abstract void initView();

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	/** 开放的刷新方法 */
	public void onRefresh() {

	}
}
