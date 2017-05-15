package com.sharebicycle.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sharebicycle.www.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZXJ on 2017/5/15.
 */

public class MyWalletActivity extends FatherActivity {
    @BindView(R.id.tv_momey)
    TextView tvMomey;
    @BindView(R.id.tv_ticket)
    TextView tvTicket;
    @BindView(R.id.ll_ticket)
    LinearLayout llTicket;
    @BindView(R.id.ll_yajin)
    LinearLayout llYajin;
    @BindView(R.id.ll_detail)
    LinearLayout llDetail;
    @BindView(R.id.tv_recharge)
    TextView tvRecharge;

    @Override
    protected int getLayoutId() {
        return R.layout.act_mywallet;
    }

    @Override
    protected void initValues() {
        initDefautHead("我的钱包", true);
    }

    @Override
    protected void initView() {

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

    @OnClick({R.id.ll_ticket, R.id.ll_yajin, R.id.ll_detail, R.id.tv_recharge})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_ticket:
                break;
            case R.id.ll_yajin:
                break;
            case R.id.ll_detail:
                break;
            case R.id.tv_recharge:
                break;
        }
    }
}
