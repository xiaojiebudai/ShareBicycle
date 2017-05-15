package com.sharebicycle.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sharebicycle.dialog.CommonDialog;
import com.sharebicycle.www.R;


/**
 * 弹窗工具类
 * 
 * @author xl
 * @date:2016-8-2下午4:49:53
 * @description
 */
public class DialogUtils {

	/** 等待弹窗 */
	public static Dialog getWaitDialog(Activity context, boolean cancelable) {

		final Dialog dialog = new Dialog(context, R.style.DialogStyle);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.custom_progress_dialog);
		dialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
								 KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					dialog.dismiss();
					return true;
				}
				return false;
			}
		});
		dialog.setCancelable(cancelable);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();

		int screenW = context.getResources().getDisplayMetrics().widthPixels;
		lp.width = (int) (0.6 * screenW);
		TextView titleTxtv = (TextView) dialog.findViewById(R.id.dialogText);
		titleTxtv.setText(R.string.loading);
		return dialog;
	}

	public static int getScreenWidth(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	public static int getScreenHeight(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	/**
	 * 通用类型dialog
	 * 
	 * @author xl
	 * @date:2016-4-12下午6:01:29
	 * @description
	 * @param context
	 * @return
	 */
	public static CommonDialog getCommonDialog(Activity context, String content) {
		CommonDialog dialog = new CommonDialog(context, R.style.DialogStyle);
		dialog.setContentView(R.layout.dialog_style_common);
		dialog.getWindow().getAttributes().width = (int) (getScreenWidth(context) * 0.7);
		((TextView) dialog.findViewById(R.id.tv_content)).setText(content);
		return dialog;
	}

	public static CommonDialog getCommonDialog(Activity context, int contentRes) {
		CommonDialog dialog = new CommonDialog(context, R.style.DialogStyle);
		dialog.setContentView(R.layout.dialog_style_common);
		dialog.getWindow().getAttributes().width = (int) (getScreenWidth(context) * 0.7);
		((TextView) dialog.findViewById(R.id.tv_content)).setText(contentRes);
		return dialog;
	}

	/**
	 * 通用类型的对话框
	 * 
	 * @author xl
	 * @date:2016-5-13上午9:43:21
	 * @description 二次确认,取消/确定按钮
	 * @param act
	 * @param content
	 * @return
	 */
	public static CommonDialog getCommonDialogTwiceConfirm(Activity act,
														   String content, boolean cancelable) {
		final CommonDialog dialog = getCommonDialog(act, content);
		dialog.getButtonLeft().setText(R.string.cancel);
		dialog.getButtonLeft().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.getButtonRight().setText(R.string.sure);
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
								 KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					dialog.dismiss();
					return true;
				}
				return false;
			}
		});
		dialog.setCancelable(cancelable);
		return dialog;
	}
}
