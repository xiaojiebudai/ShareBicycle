package com.sharebicycle.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.alibaba.fastjson.JSONObject;
import com.sharebicycle.MyApplication;
import com.sharebicycle.api.ApiUser;
import com.sharebicycle.api.ApiVariable;
import com.sharebicycle.dialog.CommonDialog;
import com.sharebicycle.utils.DialogUtils;
import com.sharebicycle.utils.FileUtils;
import com.sharebicycle.utils.PublicWay;
import com.sharebicycle.utils.SharedPreferenceUtils;
import com.sharebicycle.www.R;
import com.sharebicycle.xutils.WWXCallBack;


import org.xutils.http.RequestParams;
import org.xutils.x;


/***
 * Description:设置 Company: wangwanglife Version：1.0
 *
 * @author ZXJ
 * @date @2016-7-27
 ***/
public class SettingActivity extends FatherActivity implements OnClickListener {
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_exit:
                final CommonDialog commonDialog = DialogUtils
                        .getCommonDialogTwiceConfirm(SettingActivity.this,
                                "退出当前账号？", true);
                commonDialog.getButtonRight().setOnClickListener(
                        new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                commonDialog.dismiss();
                                exit();

                            }
                        });
                commonDialog.show();
                break;
            case R.id.ll_clear:
                showWaitDialog();
                try{
                    FileUtils.clearAllCache(this);
                }catch (Exception e){
                }
                dismissWaitDialog();
                break;
            case R.id.ll_money_des:

                break;
            case R.id.ll_about_us:
                getInfo();
                break;


            default:
                break;
        }
    }


    private void exit() {
        RequestParams params = new RequestParams(ApiUser.getLogout());
        params.addBodyParameter("sessionId", MyApplication.getInstance()
                .getSessionId());
        showWaitDialog();
        x.http().get(params, new WWXCallBack("Logout") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                // 信息置空
                MyApplication.getInstance().updataSessionId("");
                SharedPreferenceUtils.getInstance().clearLoginData();
                SharedPreferenceUtils.getInstance().saveUserPsw("");
                startActivity(new Intent(SettingActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onAfterSuccessError(JSONObject data) {
            }

            @Override
            public void onAfterFinished() {
                // TODO Auto-generated method stub
                dismissWaitDialog();
            }
        });
    }

    private void getInfo() {
        RequestParams params = new RequestParams(ApiVariable.AboutUs());
        params.addBodyParameter("sessionId", MyApplication.getInstance()
                .getSessionId());
        showWaitDialog();
        x.http().get(params, new WWXCallBack("AboutUs") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                PublicWay.startWebViewActivity(SettingActivity.this, "关于我们",
                        data.getString("Data"), WebViewActivity.DATA);
            }

            @Override
            public void onAfterSuccessError(JSONObject data) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAfterFinished() {
                // TODO Auto-generated method stub
                dismissWaitDialog();
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_setting;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.setting, true);

    }

    @Override
    protected void initView() {
        findViewById(R.id.tv_exit).setOnClickListener(this);
        findViewById(R.id.ll_money_des).setOnClickListener(this);
        findViewById(R.id.ll_clear).setOnClickListener(this);
        findViewById(R.id.ll_about_us).setOnClickListener(this);


    }

    @Override
    protected void doOperate() {

    }
}
