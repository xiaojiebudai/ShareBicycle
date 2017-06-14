package com.sharebicycle.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.sharebicycle.www.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZXJ on 2017/6/12.
 */

public class RidingFinishActivity extends FatherActivity {
    @BindView(R.id.tv_back)
    TextView tvBack;

    @Override
    protected int getLayoutId() {
        return R.layout.act_ridingfinish;
    }

    @Override
    protected void initValues() {
        initDefautHead("骑行报告", false);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void doOperate() {

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_back)
    public void onViewClicked() {
        finish();
    }
}
