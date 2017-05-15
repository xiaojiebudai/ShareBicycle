package com.sharebicycle.activity;

import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sharebicycle.utils.Consts;
import com.sharebicycle.www.R;

/**
 * Created by ZXJ on 2017/5/10.
 */

public class WebViewActivity extends FatherActivity {
    private WebView webview;
    private String data = "";
    public static final int REQUEST_CODE = 10086;
    private int model;

    /**
     * url
     */
    public static final int URL = 0;
    /**
     * data
     */
    public static final int DATA = 1;
    @Override
    protected int getLayoutId() {
        return R.layout.act_webview;
    }

    @Override
    protected void initValues() {
        model = getIntent().getIntExtra(Consts.KEY_MODULE, URL);
        data = getIntent().getStringExtra(Consts.KEY_DATA);

        initDefautHead(getIntent().getStringExtra(Consts.TITLE),true);


    }

    @Override
    protected void initView() {
        webview = (WebView) findViewById(R.id.webview);
        webview.setInitialScale(25);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);
        webview.setWebViewClient(new WebViewClient() { // 通过webView打开链接，不调用系统浏览器
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // 通过内部类定义的方法获取html页面加载的内容，这个需要添加在webview加载完成后的回调中
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
            }
        });
        switch (model) {
            case DATA:
                webview.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
                break;
            case URL:
                webview.loadUrl(data);
                break;
            default:
                break;
        }

    }

    /*
     * 初始化有关点击返回键的功能，如果有上一个网页就返回上一个网页，如果没有则结束activity；
     *
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
         if (webview.canGoBack()) {
                webview.goBack();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }
    @Override
    protected void doOperate() {

    }
}
