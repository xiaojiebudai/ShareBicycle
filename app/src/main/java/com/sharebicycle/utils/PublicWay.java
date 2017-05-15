package com.sharebicycle.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.sharebicycle.activity.IdentityAuthenticationResultActivity;
import com.sharebicycle.activity.ImageSelectActivity;
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
    /**
     * 选择图片
     *
     * @param fragment
     * @param requestCode
     * @param doCrop      裁剪
     * @param mode        模式
     * @author xl
     * @date:2016-8-2下午4:05:51
     * @description 支持单图选择模式
     */
    public static void startImageSelectActivity(Fragment fragment,
                                                int requestCode, boolean doCrop, boolean isSquare, boolean isIdCard, int mode) {
        Intent intent = new Intent(fragment.getActivity(),
                ImageSelectActivity.class);
        intent.putExtra(ImageSelectActivity.KEY_MODE, mode);
        intent.putExtra(ImageSelectActivity.KEY_DO_CROP, doCrop);
        intent.putExtra(ImageSelectActivity.KEY_ISIDCARD, isIdCard);
        intent.putExtra(ImageSelectActivity.KEY_IS_SQUARE, isSquare);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * 选择图片
     *
     * @param requestCode
     * @param doCrop      裁剪
     * @param mode        模式
     * @author xl
     * @date:2016-8-2下午4:06:03
     * @description 支持单图选择模式
     */
    public static void startImageSelectActivity(Activity act, int requestCode,
                                                boolean doCrop, boolean isSquare, boolean isIdCard, int mode) {
        Intent intent = new Intent(act, ImageSelectActivity.class);
        intent.putExtra(ImageSelectActivity.KEY_MODE, mode);
        intent.putExtra(ImageSelectActivity.KEY_DO_CROP, doCrop);
        intent.putExtra(ImageSelectActivity.KEY_ISIDCARD, isIdCard);
        intent.putExtra(ImageSelectActivity.KEY_IS_SQUARE, isSquare);
        act.startActivityForResult(intent, requestCode);
    }
    /**
     * 认证结果
     *
     * @param model
     */
    public static void startIdentityAuthenticationResultActivity(Activity act,
                                                                 int model, String data) {
        Intent intent = new Intent(act,
                IdentityAuthenticationResultActivity.class);
        intent.putExtra(Consts.KEY_MODULE, model);
        intent.putExtra(Consts.KEY_DATA, data);
        act.startActivity(intent);
    }
}
