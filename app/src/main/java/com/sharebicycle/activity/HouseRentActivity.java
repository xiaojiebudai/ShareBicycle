package com.sharebicycle.activity;

import android.view.View;

import com.sharebicycle.www.R;

/**
 * Created by ZXJ on 2017/5/14.
 */

public class HouseRentActivity extends FatherActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.act_house_rent;
    }

    @Override
    protected void initValues() {
        initDefautHead("发布租赁房源", true);
        initTextHeadRigth("发布", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void doOperate() {

    }
}
