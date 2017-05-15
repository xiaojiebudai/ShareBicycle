package com.sharebicycle.activity;

import android.content.Intent;

import com.sharebicycle.www.R;


public class WelcomeActivity extends FatherActivity {
	private static final int sleepTime = 2000;
	@Override
	protected int getLayoutId() {
		return R.layout.activity_welcome;
	}

	@Override
	protected void initValues() {

	}

	@Override
	protected void initView() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
				}
				startActivity(new Intent(WelcomeActivity.this,
						MainActivity.class));
				finish();
			}
		}).start();
	}

	@Override
	protected void doOperate() {
		// TODO Auto-generated method stub

	}

}
