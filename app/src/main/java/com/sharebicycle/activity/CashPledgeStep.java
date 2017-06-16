package com.sharebicycle.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.sharebicycle.MyApplication;
import com.sharebicycle.api.ApiPay;
import com.sharebicycle.api.ApiUser;
import com.sharebicycle.been.PayOrder;
import com.sharebicycle.been.PayWay;
import com.sharebicycle.been.WXPay;
import com.sharebicycle.utils.AlipayPayResult;
import com.sharebicycle.utils.Constants;
import com.sharebicycle.utils.Consts;
import com.sharebicycle.utils.ImageUtils;
import com.sharebicycle.utils.ParamsUtils;
import com.sharebicycle.utils.PayUtils;
import com.sharebicycle.utils.PublicWay;
import com.sharebicycle.utils.WWToast;
import com.sharebicycle.www.R;
import com.sharebicycle.xutils.WWXCallBack;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
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
    @BindView(R.id.ll_payway_container)
    LinearLayout llPaywayContainer;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.button_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {

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
    @Override
    protected void doOperate() {
        wxapi = WXAPIFactory.createWXAPI(CashPledgeStep.this,
                Constants.WX_APP_ID);
        wxapi.registerApp(Constants.WX_APP_ID);
        getPayList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);

        if (arg0 == 888) {
            if (arg1 == RESULT_OK) {
                payOk();
            }
        }
    }

    private IWXAPI wxapi;
    private List<PayWay> list = new ArrayList<PayWay>();
    private final List<View> mList = new ArrayList<View>();
    private final HashMap<String, TextView> map = new HashMap<String, TextView>();
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            int errCode = arg1.getIntExtra(Consts.KEY_DATA, 0);
            switch (errCode) {
                case BaseResp.ErrCode.ERR_OK:// 成功
                    payOk();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:// 用户取消支付
                    WWToast.showShort(R.string.pay_fail);

                    break;
                case BaseResp.ErrCode.ERR_COMM:// 错误
                    WWToast.showShort(R.string.pay_fail);
                    break;

                default:
                    break;
            }
        }
    };

    private void payOk() {
        WWToast.showShort("押金支付成功");
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
    }

    /**
     * 支付列表改为动态生产
     */
    private void getPayList() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiPay.PaymentValid());
        params.addBodyParameter(Consts.KEY_SESSIONID, MyApplication
                .getInstance().getSessionId());
        params.addBodyParameter("busType", "4");
        x.http().get(params, new WWXCallBack("PaymentValid") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                JSONArray array = data.getJSONArray("Data");
                list = JSON.parseArray(array.toJSONString(), PayWay.class);
                try {
                    llPaywayContainer.removeAllViews();
                    for (int i = 0; i < list.size(); i++) {
                        final PayWay payWay = list.get(i);
                        final View view = View.inflate(CashPledgeStep.this,
                                R.layout.item_payway, null);
                        final ImageView select = (ImageView) view
                                .findViewById(R.id.iv_selected);
                        view.setTag(i);
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for (int j = 0; j < list.size(); j++) {
                                    list.get(j).isSelect = false;
                                }
                                list.get((Integer) view.getTag()).isSelect = true;
                                for (int j = 0; j < mList.size(); j++) {
                                    if (j == (Integer) view.getTag()) {
//                                    mList.get(j).setVisibility(View.VISIBLE);
                                        mList.get(j).setVisibility(View.GONE);
                                    } else {
                                        mList.get(j).setVisibility(View.GONE);
                                    }
                                }
                                creatOrder(list.get((Integer) view.getTag()));

                            }
                        });

                        ImageView payIco = (ImageView) view
                                .findViewById(R.id.iv_pay_pic);
                        TextView payname = (TextView) view
                                .findViewById(R.id.tv_payname);

                        try {
                            ImageUtils.setCommonImage(CashPledgeStep.this,
                                    payWay.PayIco, payIco);
                        } catch (Exception e) {

                        }

                        payname.setText(payWay.PayName);

                        mList.add(select);
                        llPaywayContainer.addView(view);
                        if (i == 0) {
                            list.get(0).isSelect = true;
//                        mList.get(0).setVisibility(View.VISIBLE);
                            mList.get(0).setVisibility(View.GONE);
                        }
                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    private void creatOrder(final PayWay payWay) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.KEY_SESSIONID, MyApplication
                .getInstance().getSessionId());
        jsonObject.put("savAmt", 199);
        x.http().post(ParamsUtils.getPostJsonParams(jsonObject, ApiUser.Foregift()), new WWXCallBack("Foregift") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                PayOrder order=JSON.parseObject(data.getString("Data"),PayOrder.class);
                gotoPay(payWay,order.SheetId);
            }

            @Override
            public void onAfterFinished() {

            }
        });
    }

    private void gotoPay(PayWay payWay,long orderId) {

        final String payCode = payWay.PayCode;
        showWaitDialog();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.KEY_SESSIONID, MyApplication
                .getInstance().getSessionId());
        jsonObject.put("payCode", payCode);
        jsonObject.put("busType", PayWay.PLEDGE);
        jsonObject.put("orderId", orderId);
        x.http().post(ParamsUtils.getPostJsonParams(jsonObject, ApiPay.OrderPay()), new WWXCallBack("OrderPay") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                if (payCode.equals(Consts.GHTNET_PAY)) {
                    PublicWay.startWebViewActivityForResult(
                            CashPledgeStep.this, "网关支付",
                            data.getString("Data"), WebViewActivity.Pay, 888);

                } else if (payCode.equals(Consts.FuYou)) {
                    PublicWay.startWebViewActivityForResult(
                            CashPledgeStep.this, getString(R.string.fuyou_pay),
                            data.getString("Data"), WebViewActivity.Pay, 888);
                } else if (payCode.equals(Consts.WX_PAY)) {
                    if (wxapi.isWXAppInstalled()) {
                        String string = data.getString("Data");
                        WXPay wxPay = JSONObject.parseObject(string, WXPay.class);
                        PayReq req = new PayReq();
                        req.appId = wxPay.appid;
                        req.partnerId = wxPay.partnerid;
                        req.prepayId = wxPay.prepayid;
                        req.packageValue = "Sign=WXPay";
                        req.nonceStr = wxPay.noncestr;
                        req.timeStamp = wxPay.timestamp;
                        req.sign = wxPay.sign;
                        wxapi.sendReq(req);
                    } else {
                        WWToast.showShort(R.string.please_download_wx_app);
                    }
                } else if (payCode.equals(Consts.ALI_PAY)) {
                    PayUtils.pay(CashPledgeStep.this,
                            data.getString("Data"), new PayUtils.PayResultLisentner() {
                                @Override
                                public void PayResult(String payResult,
                                                      int payType) {
                                    // TODO Auto-generated method stub
                                    AlipayPayResult payResult2 = new AlipayPayResult(
                                            payResult);
                                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                                    String resultStatus = payResult2.getResultStatus();
                                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                                    if (TextUtils.equals(resultStatus, "9000")) {
                                        // 支付成功
                                        payOk();
                                    } else {
                                        // 判断resultStatus 为非“9000”则代表可能支付失败
                                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                                        if (TextUtils.equals(resultStatus, "8000")) {
                                            WWToast.showShort(R.string.pay_result_is_confirming);
                                            finish();
                                        } else {
                                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                                            WWToast.showShort(R.string.pay_fail);

                                        }
                                    }
                                }
                            });
                }
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });

    }



}
