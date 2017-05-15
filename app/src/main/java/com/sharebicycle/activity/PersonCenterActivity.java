package com.sharebicycle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sharebicycle.MyApplication;
import com.sharebicycle.api.ApiUser;
import com.sharebicycle.been.User;
import com.sharebicycle.utils.ImageUtils;
import com.sharebicycle.utils.SharedPreferenceUtils;
import com.sharebicycle.utils.WWViewUtil;
import com.sharebicycle.www.R;
import com.sharebicycle.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZXJ on 2017/5/12.
 */

public class PersonCenterActivity extends FatherActivity {
    @BindView(R.id.img_header)
    ImageView imgHeader;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_xinyong)
    TextView tvXinyong;
    @BindView(R.id.ll_person)
    LinearLayout llPerson;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.my_wallet)
    LinearLayout myWallet;
    @BindView(R.id.my_riding_history)
    LinearLayout myRidingHistory;
    @BindView(R.id.my_foucs_house)
    LinearLayout myFoucsHouse;
    @BindView(R.id.my_foucs_olds)
    LinearLayout myFoucsOlds;
    @BindView(R.id.my_house_look_history)
    LinearLayout myHouseLookHistory;
    @BindView(R.id.my_entrust)
    LinearLayout myEntrust;
    @BindView(R.id.my_house_idcard)
    LinearLayout myHouseIdcard;
    @BindView(R.id.my_message)
    LinearLayout myMessage;
    @BindView(R.id.my_inveter)
    LinearLayout myInveter;
    @BindView(R.id.setting)
    LinearLayout setting;

    @Override
    protected int getLayoutId() {
        return R.layout.act_persondata;
    }

    @Override
    protected void initValues() {
        initDefautHead("个人中心", true);

    }

    @Override
    protected void initView() {

    }
    @Override
    public void onResume() {

            getUserInfo();
        super.onResume();
    }
    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiUser.getUserInfo());
        params.addBodyParameter("sessionId", MyApplication.getInstance()
                .getSessionId());
        x.http().get(params, new WWXCallBack("InfoGet") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {

                User    user = JSON.parseObject(data.getString("Data"), User.class);
                SharedPreferenceUtils.getInstance().savePhoneNum(user.Mobile);
                ImageUtils.setCircleHeaderImage(PersonCenterActivity.this, user.HeadUrl,
                        imgHeader);
                tvName.setText(user.UserName);
                tvMoney.setText(WWViewUtil.numberFormatPrice(user.Consume));
            }
            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });

    }

    @Override
    protected void doOperate() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ll_person, R.id.my_wallet, R.id.my_riding_history, R.id.my_foucs_house, R.id.my_foucs_olds, R.id.my_house_look_history, R.id.my_entrust, R.id.my_house_idcard, R.id.my_message, R.id.my_inveter, R.id.setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_person:
                startActivity(new Intent(this,PersonalDataActivity.class));
                break;
            case R.id.my_wallet:
                startActivity(new Intent(this,MyWalletActivity.class));
                break;
            case R.id.my_riding_history:

                break;
            case R.id.my_foucs_house:
                startActivity(new Intent(this,MyFoucsHouseListActivity.class));
                break;
            case R.id.my_foucs_olds:
                startActivity(new Intent(this,MyFoucsHouseListActivity.class));
                break;
            case R.id.my_house_look_history:
                startActivity(new Intent(this,MyFoucsHouseListActivity.class));
                break;
            case R.id.my_entrust:
       
                break;
            case R.id.my_house_idcard:
                break;
            case R.id.my_message:
                startActivity(new Intent(this,MessageListActivity.class));
                break;
            case R.id.my_inveter:
                startActivity(new Intent(this,ShareActivity.class));
                break;
            case R.id.setting:
                startActivity(new Intent(this,SettingActivity.class));
                break;
        }
    }
}
