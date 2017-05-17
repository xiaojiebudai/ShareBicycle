package com.sharebicycle.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.sharebicycle.adapter.BannerPagerAdapter;
import com.sharebicycle.view.CirclePageIndicator;
import com.sharebicycle.view.HorizontalInnerViewPager;
import com.sharebicycle.www.R;
import com.sharebicycle.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZXJ on 2017/5/14.
 */

public class HouseDetailActivity extends FatherActivity {
    @BindView(R.id.vp_goods_details_imgs)
    HorizontalInnerViewPager vpGoodsDetailsImgs;
    @BindView(R.id.indicator)
    CirclePageIndicator indicator;
    @BindView(R.id.ll_goods_details_banner_container)
    RelativeLayout llGoodsDetailsBannerContainer;
    @BindView(R.id.tv_goods_name)
    TextView tvGoodsName;
    @BindView(R.id.iv_collect)
    ImageView ivCollect;
    @BindView(R.id.ll_collect)
    LinearLayout llCollect;
    @BindView(R.id.tv_zixun)
    TextView tvZixun;
    @BindView(R.id.tv_yuyue)
    TextView tvYuyue;
    private BannerPagerAdapter mAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.act_house_detail;
    }

    @Override
    protected void initValues() {
        int height = getResources().getDisplayMetrics().widthPixels;
        ViewGroup.LayoutParams layoutParams = vpGoodsDetailsImgs
                .getLayoutParams();
        layoutParams.height = height;
        vpGoodsDetailsImgs.setLayoutParams(layoutParams);
        mAdapter = new BannerPagerAdapter(this);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setStatusBar() {
       toTop(this);
    }

    @Override
    protected void doOperate() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getProductInfo();
    }

    private void getProductInfo() {
        showWaitDialog();
        RequestParams params = new RequestParams("XXXX");
        params.addBodyParameter("productId", "XXXX");
        x.http().get(params, new WWXCallBack("ProductGet") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {//参数
//                good = JSONObject.parseObject(data.getString("Data"), DistributionGood.class);
//
//                if (good != null) {
//                    if (good.Images != null) {
//                        ArrayList<String> mImages = new ArrayList<String>();
//                        for (int i = 0; i < good.Images.size(); i++) {
//                            mImages.add(good.Images.get(i).ImageUrl);
//                        }
//                        mAdapter.setData(mImages);
//                        vpGoodsDetailsImgs.setAdapter(mAdapter);
//                        indicator.setFillColor(getResources().getColor(
//                                R.color.color_main));
//                        indicator.setPageColor(0xAAFFFFFF);
//                        indicator.setStrokeWidth(0);
//                        indicator.setRadius(DensityUtil.dip2px(
//                                HouseDetailActivity.this, 3));
//                        indicator.setViewPager(vpGoodsDetailsImgs);
//                    }
//                }
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ll_collect, R.id.tv_zixun, R.id.tv_yuyue})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_collect:
                break;
            case R.id.tv_zixun:
                break;
            case R.id.tv_yuyue:
                break;
        }
    }
}
