package com.sharebicycle.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.sharebicycle.www.R;


/**
 * Toast类
 * 
 * @author xl
 * @date:2016-7-25下午12:54:02
 * @description
 */
public class WWToast {
	// Toast
	private static Toast toast;

	/** 自定义样式 */
	private static Toast custom;

	private static Context context;

	/**
	 * 全局初始化
	 * 
	 * @author xl
	 * @date:2016-1-27下午2:34:10
	 * @description
	 * @param context
	 */
	public static void init(Context context) {
		if (toast == null) {
			toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			custom = new Toast(context);
			custom.setView(LayoutInflater.from(context).inflate(
					R.layout.custom_toast, null));
			custom.setGravity(Gravity.CENTER, 0, 0);
			custom.setDuration(Toast.LENGTH_SHORT);
			WWToast.context = context;
		}
	}

	/**
	 * 短时间显示全局toast
	 * 
	 * @author xl
	 * @date:2016-1-27下午2:39:13
	 * @description
	 * @param msg
	 */
	public static void showShort(String message) {
		if (toast != null) {
			toast.setText(message);
			toast.show();
		}
	}

	/**
	 * * 短时间显示全局toast
	 * 
	 * @author xl
	 * @date:2016-3-10下午1:49:07
	 * @description
	 * @param messageId
	 */
	public static void showShort(int messageId) {
		if (toast != null) {
			toast.setText(messageId);
			toast.show();
		}
	}

	/**
	 * 短时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showShort(Context context, CharSequence message) {
		if (null == toast) {
			toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
			 toast.setGravity(Gravity.CENTER, 0, 0);
		}
		toast.setText(message);
		toast.show();
	}

	/**
	 * 短时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showShort(Context context, int message) {
		if (null == toast) {
			toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
			 toast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			toast.setText(message);
		}
		toast.show();
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showLong(Context context, CharSequence message) {
		if (null == toast) {
			toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
			 toast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			toast.setText(message);
		}
		toast.show();
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showLong(Context context, int message) {
		if (null == toast) {
			toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
			 toast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			toast.setText(message);
		}
		toast.show();
	}

	/**
	 * 自定义显示Toast时间
	 * 
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void show(Context context, CharSequence message, int duration) {
		if (null == toast) {
			toast = Toast.makeText(context, message, duration);
			 toast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			toast.setText(message);
		}
		toast.show();
	}

	/**
	 * 自定义显示Toast时间
	 * 
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void show(Context context, int message, int duration) {
		if (null == toast) {
			toast = Toast.makeText(context, message, duration);
		} else {
			toast.setText(message);
		}
		toast.show();
	}

	/** Hide the toast, if any. */
	public static void hideToast() {
		if (null != toast) {
			toast.cancel();
		}
	}
}
