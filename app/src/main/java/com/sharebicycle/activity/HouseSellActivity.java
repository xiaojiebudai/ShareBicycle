package com.sharebicycle.activity;

import android.view.View;

import com.sharebicycle.www.R;

/**
 * Created by ZXJ on 2017/5/14.
 */

public class HouseSellActivity extends FatherActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.act_house_sell;
    }

    @Override
    protected void initValues() {
        initDefautHead("发布房源", true);
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
