package com.sharebicycle;

import android.app.Application;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.sharebicycle.activity.MainActivity;
import com.sharebicycle.utils.Consts;
import com.sharebicycle.utils.SharedPreferenceUtils;
import com.sharebicycle.utils.WWToast;

import org.xutils.x;

import cockroach.Cockroach;


public class MyApplication extends Application {
	/** 用户id */
	private static String userId;
	/** sessionId */
	private String sessionId;

	private static MyApplication instance;

	public String getSessionId() {
		if (sessionId == null) {
			sessionId = SharedPreferenceUtils.getInstance().getSessionId();
		}
		return sessionId;
	}

	/** 用于登录用 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
		SharedPreferenceUtils.getInstance().saveSessionId(sessionId);
		isSessionPast = false;// 设置id的时候(登录)sessionPast重置
	}

	/** session失效 */
	private boolean isSessionPast;

	public void dealSessionPast() {
		if (isSessionPast) {
			return;
		} else {
			isSessionPast = true;
			// TODO:弹窗?跳界面
			startActivity(new Intent(this, MainActivity.class).putExtra(
					Consts.KEY_SESSION_ERROR, true).setFlags(
					Intent.FLAG_ACTIVITY_NEW_TASK));

		}
	}

	/** 暂用于清除 */
	public void updataSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
//		install();
		SharedPreferenceUtils.init(this);
		x.Ext.init(this);
		x.Ext.setDebug(true);
		WWToast.init(this);
		sessionId = SharedPreferenceUtils.getInstance().getSessionId();
	}
	//保证应用不crash
	private void install() {

		Cockroach.install(new Cockroach.ExceptionHandler() {

			// handlerException内部建议手动try{  你的异常处理逻辑  }catch(Throwable e){ } ，以防handlerException内部再次抛出异常，导致循环调用handlerException

			@Override
			public void handlerException(final Thread thread, final Throwable throwable) {
				new Handler(Looper.getMainLooper()).post(new Runnable() {
					@Override
					public void run() {
						try {
							WWToast.showShort("系统异常，请退出应用重试");
						} catch (Throwable e) {

						}
					}
				});
			}
		});
	}
	public static MyApplication getInstance() {
		return instance;
	}

	/**
	 * 判断登录状态
	 * 
	 * @return
	 */
	public static boolean isLogin() {
		String userId = SharedPreferenceUtils.getInstance().getSessionId();
		if (!TextUtils.isEmpty(userId)) {
			return true;
		}
		return false;
	}
}
