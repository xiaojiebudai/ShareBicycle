package com.sharebicycle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sharebicycle.utils.WWToast;
import com.sharebicycle.www.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZXJ on 2017/5/14.
 */

public class SmartLockOpenActivity extends FatherActivity {
    @BindView(R.id.tv_buy_house)
    TextView tvBuyHouse;
    @BindView(R.id.tv_sell_house)
    TextView tvSellHouse;
    @BindView(R.id.tv_rent_house)
    TextView tvRentHouse;
    @BindView(R.id.tv_rentout_house)
    TextView tvRentoutHouse;
    @BindView(R.id.iv_open)
    ImageView ivOpen;
    @BindView(R.id.ll_lock_list)
    LinearLayout llLockList;
    @BindView(R.id.ll_wtf)
    LinearLayout llWtf;
    @BindView(R.id.ll_lock_manage)
    LinearLayout llLockManage;

    @Override
    protected int getLayoutId() {
        return R.layout.act_smart_lock_open;
    }

    @Override
    protected void initValues() {
        initDefautHead("智能门圣", true);
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

    @OnClick({R.id.tv_buy_house, R.id.tv_sell_house, R.id.tv_rent_house, R.id.tv_rentout_house, R.id.iv_open, R.id.ll_lock_list, R.id.ll_wtf, R.id.ll_lock_manage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_buy_house:
                startActivity(new Intent(this,HouseListActivity.class));
                break;
            case R.id.tv_sell_house:
                startActivity(new Intent(this,HouseSellActivity.class));
                break;
            case R.id.tv_rent_house:
                startActivity(new Intent(this,HouseListActivity.class));
                break;
            case R.id.tv_rentout_house:
                startActivity(new Intent(this,HouseRentActivity.class));
                break;
            case R.id.iv_open:
                WWToast.showShort("演示例子，敬请期待");
                break;
            case R.id.ll_lock_list:
                finish();
                break;
            case R.id.ll_wtf:
                WWToast.showShort("演示例子，敬请期待");
                break;
            case R.id.ll_lock_manage:
                WWToast.showShort("演示例子，敬请期待");
                break;
        }
    }
}
