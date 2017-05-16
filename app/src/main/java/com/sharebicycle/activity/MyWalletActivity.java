package com.sharebicycle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sharebicycle.dialog.CommonDialog;
import com.sharebicycle.utils.DialogUtils;
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
                startActivity(new Intent(this,TicketListActivity.class));
                break;
            case R.id.ll_yajin:
                final CommonDialog commonDialog = DialogUtils.getCommonDialog(this, "退押金后，您的账户李的优惠券将会被清空，您确定要退吗？");
                commonDialog.getButtonLeft("退押金").setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        commonDialog.dismiss();
                    }
                });commonDialog.getButtonRight("不退了").setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        commonDialog.dismiss();
                    }
                });
                commonDialog.show();
                break;
            case R.id.ll_detail:
                startActivity(new Intent(this,BussnessDetailActivity.class));
                break;
            case R.id.tv_recharge:
                startActivity(new Intent(this,RechagerActivity.class));
                break;
        }
    }
}
