package com.sharebicycle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.RelativeLayout;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.sharebicycle.MyApplication;
import com.sharebicycle.been.TabEntity;
import com.sharebicycle.fragment.BaiduMapFragment;
import com.sharebicycle.fragment.RealEstateFragment;
import com.sharebicycle.fragment.SmartLockFragment;
import com.sharebicycle.utils.Consts;
import com.sharebicycle.www.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends FatherActivity{
    @BindView(R.id.rl_container)
    RelativeLayout rl_container;
    @BindView(R.id.tabLayout)
    CommonTabLayout tabLayout;
    @BindView(R.id.rl_head_left)
    RelativeLayout rlHeadLeft;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles = {"共享单车", "优速门圣", "房屋租售"};
    private int[] mIconUnselectIds = {
            R.mipmap.logo, R.mipmap.logo,
            R.mipmap.logo};
    private int[] mIconSelectIds = {
            R.mipmap.logo, R.mipmap.logo,
            R.mipmap.logo};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initValues() {
    }

    @Override
    protected void initView() {
        mFragments.add(new BaiduMapFragment());
        mFragments.add(new SmartLockFragment());
        mFragments.add(new RealEstateFragment());

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        tabLayout.setTabData(mTabEntities, this, R.id.rl_container, mFragments);
    }

    @Override
    protected void doOperate() {

    }

    // 不去区分业务模式了，之后再改
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean sessionError = intent.getBooleanExtra(Consts.KEY_SESSION_ERROR,
                false);
        if (sessionError) {// session相关错误
            startActivity(new Intent(this, LoginActivity.class));
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.rl_head_left)
    public void onViewClicked() {
        if(MyApplication.isLogin()){
            startActivity(new Intent(this,PersonCenterActivity.class));
        }else{
            startActivity(new Intent(this, LoginActivity.class));
        }

    }

}
