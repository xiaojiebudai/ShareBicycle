package com.sharebicycle.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sharebicycle.api.ApiUser;
import com.sharebicycle.been.User;
import com.sharebicycle.utils.Constants;
import com.sharebicycle.utils.PermissionUtil;
import com.sharebicycle.utils.ShareUtils;
import com.sharebicycle.utils.SharedPreferenceUtils;
import com.sharebicycle.utils.WWToast;
import com.sharebicycle.www.R;
import com.sharebicycle.xutils.WWXCallBack;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.WeiXinShareContent;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wuri on 2016/11/1.
 */

public class ShareActivity extends FatherActivity implements PermissionUtil.PermissionCallbacks {


    @BindView(R.id.tv_invetercode)
    TextView tvInvetercode;
    @BindView(R.id.ll_qq_zone)
    LinearLayout llQqZone;
    @BindView(R.id.ll_qq)
    LinearLayout llQq;
    @BindView(R.id.ll_wx)
    LinearLayout llWx;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.ll_wx_circle)
    LinearLayout llWxCircle;
    @BindView(R.id.ll_sina)
    LinearLayout llSina;
    private ShareAction doShare;

    @Override
    protected int getLayoutId() {
        return R.layout.act_share;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.share, true);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void doOperate() {
        doRequest();
    }

    User user;

    private void doRequest() {
        RequestParams params = new RequestParams(ApiUser.getSharErweima());
        params.addBodyParameter("sessionId", SharedPreferenceUtils
                .getInstance().getSessionId());
        showWaitDialog();
        x.http().get(params, new WWXCallBack("InviterGet") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                JSONObject jsonObject = data.getJSONObject("Data");
                user = JSON.parseObject(jsonObject.toJSONString(),
                        User.class);
                if (user != null) {
                    tvInvetercode.setText("您的邀请码为："+user.InviterNo);
                }
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        doShare.share();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        WWToast.showShort("权限被拒,无法执行分享操作");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ll_qq_zone, R.id.ll_qq, R.id.ll_wx, R.id.ll_wx_circle, R.id.ll_sina})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_qq_zone:
                doShare = ShareUtils.shareSignle(this, SHARE_MEDIA.QZONE, new CustomShareListener(ShareActivity.this), "http://img.szhysy.cn/Image/t1/1abfc1dfefae413a8acdcee2e074f366__.png", "标题", "测试内容", Constants.SHARE_URL + user.InviterNo);
                break;
            case R.id.ll_qq:
                doShare = ShareUtils.shareSignle(this, SHARE_MEDIA.QQ, new CustomShareListener(ShareActivity.this), "http://img.szhysy.cn/Image/t1/1abfc1dfefae413a8acdcee2e074f366__.png", "标题", "测试内容", Constants.SHARE_URL + user.InviterNo);
                break;
            case R.id.ll_wx:
                doShare = ShareUtils.shareSignle(this, SHARE_MEDIA.WEIXIN, new CustomShareListener(ShareActivity.this), "http://img.szhysy.cn/Image/t1/1abfc1dfefae413a8acdcee2e074f366__.png", "标题", "测试内容", Constants.SHARE_URL + user.InviterNo);

                break;
            case R.id.ll_wx_circle:
                doShare = ShareUtils.shareSignle(this, SHARE_MEDIA.WEIXIN_CIRCLE, new CustomShareListener(ShareActivity.this), "http://img.szhysy.cn/Image/t1/1abfc1dfefae413a8acdcee2e074f366__.png", "标题", "测试内容", Constants.SHARE_URL + user.InviterNo);
                break;
            case R.id.ll_sina:
                doShare = ShareUtils.shareSignle(this, SHARE_MEDIA.SINA, new CustomShareListener(ShareActivity.this), "http://img.szhysy.cn/Image/t1/1abfc1dfefae413a8acdcee2e074f366__.png", "标题", "测试内容", Constants.SHARE_URL + user.InviterNo);
                break;
        }
    }


    private static class CustomShareListener implements UMShareListener {

        private WeakReference<ShareActivity> mActivity;

        private CustomShareListener(ShareActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            WWToast.showShort(R.string.share_success);
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            WWToast.showShort(R.string.share_fail);

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            WWToast.showShort(R.string.share_cancel);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 屏幕横竖屏切换时避免出现window leak的问题
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        doShare.close();
    }
}
