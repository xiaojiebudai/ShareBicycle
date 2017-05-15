package com.sharebicycle.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sharebicycle.MyApplication;
import com.sharebicycle.api.ApiUser;
import com.sharebicycle.been.User;
import com.sharebicycle.been.UserReal;
import com.sharebicycle.utils.Consts;
import com.sharebicycle.utils.FileUtils;
import com.sharebicycle.utils.ImageUtils;
import com.sharebicycle.utils.ParamsUtils;
import com.sharebicycle.utils.PublicWay;
import com.sharebicycle.utils.WWToast;
import com.sharebicycle.view.ChoosePhotoPop;
import com.sharebicycle.www.R;
import com.sharebicycle.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/***
 * Description:用户信息 Company: wangwanglife Version：1.0
 *
 * @author ZXJ
 * @date @2016-7-26
 ***/

public class PersonalDataActivity extends FatherActivity implements
        OnClickListener {
    private TextView  phone, username, authentication_status;
    private ImageView header_img;
    private LinearLayout   ll_name, ll_img, ll_Authentication;
    private static final int SELECTPHOTPO = 999;
    private boolean isRefresh = true;

    @Override
    protected int getLayoutId() {
        return R.layout.act_personaldata;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.person_data, true);
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub
        phone = (TextView) findViewById(R.id.phone);

        username = (TextView) findViewById(R.id.username);
        authentication_status = (TextView) findViewById(R.id.authentication_status);
        header_img = (ImageView) findViewById(R.id.header_img);

        ll_name = (LinearLayout) findViewById(R.id.ll_name);
        ll_img = (LinearLayout) findViewById(R.id.ll_img);
        ll_Authentication = (LinearLayout) findViewById(R.id.ll_Authentication);

        ll_name.setOnClickListener(this);
        ll_img.setOnClickListener(this);
        ll_Authentication.setOnClickListener(this);

    }

    @Override
    protected void doOperate() {
    }

    @Override
    protected void onResume() {
        if (isRefresh) {
            getDataInfo();
            realGet();
            isRefresh = false;
        }

        super.onResume();
    }

    private UserReal userReal;

    private void realGet() {
        RequestParams params = new RequestParams(ApiUser.RealGet());
        params.addBodyParameter(Consts.KEY_SESSIONID, MyApplication
                .getInstance().getSessionId());
        showWaitDialog();
        x.http().get(params, new WWXCallBack("RealGet") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                // TODO Auto-generated method stub
                // 0 待提交，1待审核，2审核通过，3审核不通过
                userReal = JSON.parseObject(data.getString("Data"), UserReal.class);
                if (userReal == null) {
                    authentication_status.setText(R.string.no_auth);
                } else {
                    switch (userReal.Status) {
                        case 0:
                            authentication_status.setText(R.string.no_auth);
                            break;
                        case 1:
                            authentication_status.setText(R.string.wait_check);
                            break;
                        case 2:
                            authentication_status.setText(userReal.CredName);
                            findViewById(R.id.iv_arrow_gray).setVisibility(View.INVISIBLE);
                            break;
                        case 3:
                            authentication_status.setText(R.string.check_no_pass);
                            break;
                        default:
                            break;
                    }
                }

            }

            @Override
            public void onAfterFinished() {
                // TODO Auto-generated method stub
                dismissWaitDialog();
            }
        });
    }

    private User user;

    /**
     * 获取信息
     */
    private void getDataInfo() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiUser.getUserInfo());
        params.addBodyParameter(Consts.KEY_SESSIONID, MyApplication
                .getInstance().getSessionId());
        x.http().get(params, new WWXCallBack("InfoGet") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                user = JSON.parseObject(data.getString("Data"), User.class);
                if (user == null) {
                    return;
                }
                ImageUtils.setCircleHeaderImage(PersonalDataActivity.this,
                        user.HeadUrl, header_img);
                username.setText(user.UserName);
                phone.setText(user.Mobile);

            }

            @Override
            public void onAfterSuccessError(JSONObject data) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAfterFinished() {
                // TODO Auto-generated method stub
                dismissWaitDialog();
            }
        });
    }


    private ChoosePhotoPop choosePhotoPop;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_img:
                if (choosePhotoPop == null) {
                    choosePhotoPop = new ChoosePhotoPop(this,
                            R.layout.act_personaldata, SELECTPHOTPO);
                }
                choosePhotoPop.showChooseWindow();
                break;
            case R.id.ll_name:
                if (user == null) {
                    WWToast.showShort(R.string.date_no_get);
                    return;
                }
                Intent intent = new Intent(this, EditUserNameActivity.class);
                intent.putExtra(EditUserNameActivity.NAME, user.UserName);
                startActivityForResult(intent, 888);
                break;
            case R.id.ll_Authentication:
                isRefresh = true;
                // 0 待提交，1待审核，2审核通过，3审核不通过
                if (userReal != null && userReal.Status != 0) {
                    if (userReal.Status == 1) {
                        PublicWay.startIdentityAuthenticationResultActivity(
                                PersonalDataActivity.this,
                                IdentityAuthenticationResultActivity.IDENTITYING, "");
                    } else if (userReal.Status == 3) {
                        PublicWay.startIdentityAuthenticationResultActivity(
                                PersonalDataActivity.this,
                                IdentityAuthenticationResultActivity.IDENTITYDEFAULT, userReal.Explain);
                    }
                } else {
                    startActivity(new Intent(PersonalDataActivity.this,
                            IdentityAuthenticationActivity.class));
                }
                break;

            default:
                break;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK || data != null) {
            switch (requestCode) {
                case 888:
                    String name = data.getStringExtra(EditUserNameActivity.NAME);
                    updateName(name);
                    break;
                case SELECTPHOTPO:
                    upImg(data.getStringExtra(Consts.KEY_DATA));
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 上传用户名
     */
    private void updateName(final String name) {
        showWaitDialog();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.KEY_SESSIONID, MyApplication.getInstance()
                .getSessionId());
        jsonObject.put("userName", name);
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject,
                        ApiUser.getUpdateName()),
                new WWXCallBack("InfoNameUpdate") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {

                        if (user != null){
                            user.UserName = name;
                        }
                        username.setText(name);
                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }
                });
    }

    /**
     * 上传图片到服务器
     */
    private void upImg(String localUrl) {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiUser.getUpImg());
        final String newUrl = FileUtils.getCompressedImageFileUrl(localUrl);

        params.addBodyParameter("file", new File(newUrl));
        params.setMultipart(true);
        x.http().post(params, new WWXCallBack("UpImage") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                updateImg(data.getString("Data"));
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    /**
     * 上传图片链接到服务器
     */
    private void updateImg(final String imgUrl) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.KEY_SESSIONID, MyApplication.getInstance()
                .getSessionId());
        jsonObject.put("imgUrl", imgUrl);
        showWaitDialog();
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject,
                        ApiUser.getUpdateHeader()),
                new WWXCallBack("InfoHeadUpdate") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        ImageUtils.setCircleImage(PersonalDataActivity.this,
                                imgUrl, header_img);
                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }
                });
    }

}
