package com.sharebicycle.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sharebicycle.utils.Constants;
import com.sharebicycle.utils.Consts;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信支付回调界面
 *
 * @author xl
 * @version 创建时间：2015-12-12 上午11:53:32
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";
    public static final String WXPAYRESULT = "WXPayResult";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Intent intent = new Intent(WXPAYRESULT);
            intent.putExtra(Consts.KEY_DATA, resp.errCode);
            sendBroadcast(intent);
            finish();
        }
    }

}