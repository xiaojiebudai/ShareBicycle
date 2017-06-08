package com.sharebicycle.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.sharebicycle.www.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <!--押金充值;实名认证;完成-->
 * Created by ZXJ on 2017/6/8.
 */

public class CashPledgeStep extends FatherActivity {
    @BindView(R.id.step_view)
    HorizontalStepView stepView;
    @BindView(R.id.tv_pay_money)
    TextView tvPayMoney;
    @BindView(R.id.ll_wx_pay)
    LinearLayout llWxPay;
    @BindView(R.id.ll_alipay)
    LinearLayout llAlipay;
    @BindView(R.id.ll_pay)
    LinearLayout llPay;
    @BindView(R.id.ed_name)
    EditText edName;
    @BindView(R.id.ed_idcard)
    EditText edIdcard;
    @BindView(R.id.ll_idcard_info)
    LinearLayout llIdcardInfo;
    @BindView(R.id.ll_success)
    LinearLayout llSuccess;
    @BindView(R.id.button_ok)
    Button buttonOk;
    ArrayList<StepBean> stepsBeanList = new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.act_cashpledgestep;
    }

    @Override
    protected void initValues() {

    }

    @Override
    protected void initView() {

        StepBean stepBean0 = new StepBean("押金充值",0);
        StepBean stepBean1 = new StepBean("实名认证",-1);
        StepBean stepBean2 = new StepBean("注册完成",-1);

        stepsBeanList.add(stepBean0);
        stepsBeanList.add(stepBean1);
        stepsBeanList.add(stepBean2);



        stepView
                .setStepViewTexts(stepsBeanList)//总步骤
                .setTextSize(12)//set textSize
                .setStepsViewIndicatorCompletedLineColor(getResources().getColor(R.color.white))//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(getResources().getColor(R.color.white))//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor(getResources().getColor(R.color.white))//设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor(getResources().getColor(R.color.white))//设置StepsView text未完成线的颜色
                .setStepsViewIndicatorCompleteIcon(getResources().getDrawable(R.mipmap.register_finish_icon))//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(getResources().getDrawable(R.mipmap.register_present_icon))//设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(getResources().getDrawable(R.mipmap.register_present_icon));//设置StepsViewIndicator AttentionIcon
        llPay.setVisibility(View.VISIBLE);
        initDefautHead("押金充值",true);
        buttonOk.setVisibility(View.GONE);
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

    @OnClick({R.id.ll_wx_pay, R.id.ll_alipay, R.id.button_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_wx_pay:
                if(  stepsBeanList.get(0).getState()==0){
                    stepsBeanList.get(0).setState(1);
                    stepsBeanList.get(1).setState(0);
                    llPay.setVisibility(View.GONE);
                    llIdcardInfo.setVisibility(View.VISIBLE);
                    stepView
                            .setStepViewTexts(stepsBeanList);
                    initDefautHead("实名认证",true);
                    buttonOk.setVisibility(View.VISIBLE);
                    buttonOk.setText("提交");
                }
                break;
            case R.id.ll_alipay:
                if(  stepsBeanList.get(0).getState()==0){
                    stepsBeanList.get(0).setState(1);
                    stepsBeanList.get(1).setState(0);
                    llPay.setVisibility(View.GONE);
                    llIdcardInfo.setVisibility(View.VISIBLE);
                    stepView
                            .setStepViewTexts(stepsBeanList);
                    initDefautHead("实名认证",true);
                    buttonOk.setVisibility(View.VISIBLE);
                    buttonOk.setText("提交");
                }
                break;
            case R.id.button_ok:

                if(  stepsBeanList.get(1).getState()==0){
                    stepsBeanList.get(1).setState(1);
                    stepsBeanList.get(2).setState(1);
                    llIdcardInfo.setVisibility(View.GONE);
                    llSuccess.setVisibility(View.VISIBLE);
                    stepView
                            .setStepViewTexts(stepsBeanList);
                    initDefautHead("注册完成",true);
                    buttonOk.setText("去用车");

                }else
                if(  stepsBeanList.get(2).getState()==1){
                    finish();
                }

                break;
        }
    }
}
