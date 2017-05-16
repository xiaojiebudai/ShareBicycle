package com.sharebicycle.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sharebicycle.MyApplication;
import com.sharebicycle.api.ApiPay;
import com.sharebicycle.been.PayWay;
import com.sharebicycle.utils.Consts;
import com.sharebicycle.utils.ImageUtils;
import com.sharebicycle.utils.WWToast;
import com.sharebicycle.www.R;
import com.sharebicycle.xutils.WWXCallBack;

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
        initDefautHead("钱包充值", true);
    }

    @Override
    protected void initView() {
        selectView = tv0;
    }

    @Override
    protected void doOperate() {
        getPayList();
    }

    private List<PayWay> list = new ArrayList<PayWay>();
    private final List<View> mList = new ArrayList<View>();
    private final HashMap<String, TextView> map = new HashMap<String, TextView>();

    /**
     * 支付列表改为动态生产
     */
    private void getPayList() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiPay.PaymentValid());
        params.addBodyParameter(Consts.KEY_SESSIONID, MyApplication
                .getInstance().getSessionId());

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
                                gotoPay(list.get((Integer) view.getTag()));
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

    private void gotoPay(PayWay payWay) {
        WWToast.showShort(payWay.PayName);
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
            ((TextView)selectView).setTextColor(getResources().getColor(R.color.text_f7));
            selectView.setBackgroundResource(R.color.text_c1);
            ((TextView) view).setTextColor(getResources().getColor(R.color.white));
            view.setBackgroundResource(R.color.color_main);
            selectView=view;
        }
    }
}
