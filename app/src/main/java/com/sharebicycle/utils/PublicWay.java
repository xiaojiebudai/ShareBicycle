package com.sharebicycle.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.sharebicycle.activity.LoginActivity;
import com.sharebicycle.activity.SetPasswordActivity;
import com.sharebicycle.activity.VerifyPhoneNumberActivity;
import com.sharebicycle.activity.WebViewActivity;

/**
 * 开启界面类
 *
 * @author zxj
 * @description
 */
public class PublicWay {
    /**
     * 打开网页
     */
    public static void startWebViewActivity(Activity act, String title,
                                            String webHtml, int model) {
        Intent intent = new Intent(act, WebViewActivity.class);
        intent.putExtra(Consts.KEY_DATA, webHtml);
        intent.putExtra(Consts.TITLE, title);
        intent.putExtra(Consts.KEY_MODULE, model);
        act.startActivity(intent);
    }

    /**
     * 打开网页
     */
    public static void startWebViewActivityForResult(Activity act,
                                                     String title, String webHtml, int model, int requestCode) {
        Intent intent = new Intent(act, WebViewActivity.class);
        intent.putExtra(Consts.KEY_DATA, webHtml);
        intent.putExtra(Consts.TITLE, title);
        intent.putExtra(Consts.KEY_MODULE, model);
        act.startActivityForResult(intent, requestCode);
    }
    /**
     * @param act  找回密码
     * @param mode
     */
    public static void startVerifyPhoneNumberActivity(Activity act, int mode,
                                                      String phoneNum, int requestCode) {
        Intent intent = new Intent(act, VerifyPhoneNumberActivity.class);
        if (mode == VerifyPhoneNumberActivity.PAY) {
            intent.putExtra(Consts.KEY_DATA, phoneNum);
        }
        intent.putExtra(Consts.KEY_MODULE, mode);
        act.startActivityForResult(intent, requestCode);
    }

    /**
     * @param act  找回密码
     * @param mode
     */
    public static void startVerifyPhoneNumberActivity(Fragment act, int mode,
                                                      String phoneNum, int requestCode) {
        Intent intent = new Intent(act.getActivity(), VerifyPhoneNumberActivity.class);
        if (mode == VerifyPhoneNumberActivity.PAY) {
            intent.putExtra(Consts.KEY_DATA, phoneNum);
        }
        intent.putExtra(Consts.KEY_MODULE, mode);
        act.startActivityForResult(intent, requestCode);
    }
    /**
     * 验证数据到修改密码
     */
    public static void startSetPasswordActivity(Activity act, String code,
                                                String phone, int mode) {
        Intent intent = new Intent(act, SetPasswordActivity.class);
        intent.putExtra(Consts.KEY_MODULE, mode);
        if (mode == SetPasswordActivity.LOGIN) {
            intent.putExtra(SetPasswordActivity.DATA_CODE, code);
            intent.putExtra(SetPasswordActivity.DATA_PHONE, phone);
        } else {
            intent.putExtra(SetPasswordActivity.DATA_CODE, code);
        }
        act.startActivity(intent);
    }
    /**
     * 注册带数据到登陆
     */
    public static void startLoginActivity(Activity act, String phone, String psw) {
        Intent intent = new Intent(act, LoginActivity.class);
        intent.putExtra(LoginActivity.DATA_PHONE, phone);
        intent.putExtra(LoginActivity.DATA_PSW, psw);
        act.startActivity(intent);
    }
}
