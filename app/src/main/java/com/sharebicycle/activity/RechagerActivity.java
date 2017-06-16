package com.sharebicycle.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sharebicycle.MyApplication;
import com.sharebicycle.api.Api;
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
import com.sharebicycle.wxapi.WXPayEntryActivity;
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
 * Created by ZXJ on 2017/5/16.
 */

public class RechagerActivity extends FatherActivity {
    @BindView(R.id.tv_0)
    TextView tv0;
    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.tv_2)
    TextView tv2;
    @BindView(R.id.tv_3)
    TextView tv3;
    @BindView(R.id.ll_payway_container)
    LinearLayout llPaywayContainer;
    @BindView(R.id.tv_recharge)
    TextView tvRecharge;

    @Override
    protected int getLayoutId() {
        return R.layout.act_rechager;
    }

    @Override
    protected void initValues() {
        IntentFilter intent = new IntentFilter(WXPayEntryActivity.WXPAYRESULT);
        registerReceiver(receiver, intent);
        initDefautHead("钱包充值", true);
    }

    @Override
    protected void initView() {
        selectView = tv0;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private int moneyNum = 100;
    private View selectView;

    @OnClick({R.id.tv_0, R.id.tv_1, R.id.tv_2, R.id.tv_3, R.id.tv_recharge})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_0:
                setView(view);
                moneyNum = 100;
                break;
            case R.id.tv_1:
                setView(view);
                moneyNum = 50;
                break;
            case R.id.tv_2:
                setView(view);
                moneyNum = 20;
                break;
            case R.id.tv_3:
                moneyNum = 10;
                setView(view);
                break;
            case R.id.tv_recharge:
                break;
        }
    }

    private void setView(View view) {
        if (selectView.getId() != view.getId()) {
            ((TextView) selectView).setTextColor(getResources().getColor(R.color.text_f7));
            selectView.setBackgroundResource(R.color.text_c1);
            ((TextView) view).setTextColor(getResources().getColor(R.color.white));
            view.setBackgroundResource(R.color.color_main);
            selectView = view;
        }
    }
    @Override
    protected void doOperate() {
        wxapi = WXAPIFactory.createWXAPI(RechagerActivity.this,
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
        WWToast.showShort(R.string.order_pay_success);
        setResult(RESULT_OK);
        finish();
    }

    /**
     * 支付列表改为动态生产
     */
    private void getPayList() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiPay.PaymentValid());
        params.addBodyParameter(Consts.KEY_SESSIONID, MyApplication
                .getInstance().getSessionId());
        params.addBodyParameter("busType", "2");
        x.http().get(params, new WWXCallBack("PaymentValid") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                JSONArray array = data.getJSONArray("Data");
                list = JSON.parseArray(array.toJSONString(), PayWay.class);
                try {
                    llPaywayContainer.removeAllViews();
                    for (int i = 0; i < list.size(); i++) {
                        final PayWay payWay = list.get(i);
                        final View view = View.inflate(RechagerActivity.this,
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
                            ImageUtils.setCommonImage(RechagerActivity.this,
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
        jsonObject.put("savAmt", moneyNum);
        x.http().post(ParamsUtils.getPostJsonParams(jsonObject, ApiUser.Saving()), new WWXCallBack("Saving") {
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
        jsonObject.put("busType", PayWay.RECHARGE);
        jsonObject.put("orderId", orderId);
        x.http().post(ParamsUtils.getPostJsonParams(jsonObject, ApiPay.OrderPay()), new WWXCallBack("OrderPay") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                if (payCode.equals(Consts.GHTNET_PAY)) {
                    PublicWay.startWebViewActivityForResult(
                            RechagerActivity.this, "网关支付",
                            data.getString("Data"), WebViewActivity.Pay, 888);

                } else if (payCode.equals(Consts.FuYou)) {
                    PublicWay.startWebViewActivityForResult(
                            RechagerActivity.this, getString(R.string.fuyou_pay),
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
                    PayUtils.pay(RechagerActivity.this,
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
