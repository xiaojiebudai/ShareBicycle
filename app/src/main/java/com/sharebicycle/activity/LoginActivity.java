package com.sharebicycle.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.sharebicycle.MyApplication;
import com.sharebicycle.api.ApiUser;
import com.sharebicycle.utils.ParamsUtils;
import com.sharebicycle.utils.PublicWay;
import com.sharebicycle.utils.SharedPreferenceUtils;
import com.sharebicycle.utils.TimeUtil;
import com.sharebicycle.utils.WWToast;
import com.sharebicycle.www.R;
import com.sharebicycle.xutils.WWXCallBack;


import org.xutils.x;

import java.util.Set;


/**
 * 登录activity
 *
 * @author licheng
 */
public class LoginActivity extends FatherActivity implements OnClickListener {

    public static final String DATA_PSW = "psw";
    public static final String DATA_PHONE = "phone";
    public static final String ISFRAOMMAIN = "isFromMain";

    private EditText mEdUserName;
    private EditText mEdPassWord;
    private String phone;
    private String psw1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.user_login, false);

        phone = SharedPreferenceUtils.getInstance().getPhoneNum();
        psw1 = SharedPreferenceUtils.getInstance().getUserPsw();
        if (!TextUtils.isEmpty(getIntent().getStringExtra(DATA_PHONE))) {
            phone = getIntent().getStringExtra(DATA_PHONE);
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra(DATA_PSW))) {

            psw1 = getIntent().getStringExtra(DATA_PSW);
        }

    }

    @Override
    protected void initView() {
        mEdUserName = (EditText) findViewById(R.id.ed_username);
        mEdPassWord = (EditText) findViewById(R.id.ed_password);
        if (!TextUtils.isEmpty(phone)) {
            mEdUserName.setText(phone);

        }
        if (!TextUtils.isEmpty(psw1)) {
            mEdPassWord.setText(psw1);
        }
        findViewById(R.id.tv_login).setOnClickListener(this);
        findViewById(R.id.tv_fatser_register).setOnClickListener(this);
        findViewById(R.id.tv_forget_password).setOnClickListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        phone = SharedPreferenceUtils.getInstance().getPhoneNum();
        psw1 = SharedPreferenceUtils.getInstance().getUserPsw();
        if (!TextUtils.isEmpty(intent.getStringExtra(DATA_PHONE))) {
            phone = intent.getStringExtra(DATA_PHONE);
        }
        if (!TextUtils.isEmpty(intent.getStringExtra(DATA_PSW))) {

            psw1 = intent.getStringExtra(DATA_PSW);
        }

        if (!TextUtils.isEmpty(phone)) {
            mEdUserName.setText(phone);

        }
        if (!TextUtils.isEmpty(psw1)) {
            mEdPassWord.setText(psw1);
        }
        super.onNewIntent(intent);
    }

    @Override
    protected void doOperate() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_login:// 登录
                if (!TimeUtil.isFastClick()) {
                    login();
                } else {
                    WWToast.showShort(R.string.click_too_fast);
                }
                break;
            case R.id.tv_fatser_register:// 快速注册
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.tv_forget_password:// 忘记密码
                PublicWay.startVerifyPhoneNumberActivity(this,
                        VerifyPhoneNumberActivity.LOGIN, null, 0);
                break;

            default:
                break;
        }
    }

    /**
     * 登录操作
     */
    private void login() {
        final String username = mEdUserName.getText().toString();
        final String psw = mEdPassWord.getText().toString();
        if (TextUtils.isEmpty(username)) {
            WWToast.showShort(R.string.please_input_username);
            return;
        }
        if (psw.length() == 0) {
            WWToast.showShort(R.string.please_input_password);
            return;
        }
        if (psw.length() < 6) {
            WWToast.showShort(R.string.psw_input_error);
            return;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", username);
        jsonObject.put("logPsd", psw);
        showWaitDialog();
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject, ApiUser.login()),
                new WWXCallBack("Login") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        MyApplication.getInstance().setSessionId(
                                data.getString("Data"));
                        SharedPreferenceUtils.getInstance().savePhoneNum(
                                username);
                        SharedPreferenceUtils.getInstance().saveUserPsw(psw);
                        finish();

                    }

                    @Override
                    public void onAfterSuccessError(JSONObject data) {
                        super.onAfterSuccessError(data);
                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }
                });

    }




    @Override
    public void onBackPressed() {// 返回键跳主界面
        super.onBackPressed();
    }
}
