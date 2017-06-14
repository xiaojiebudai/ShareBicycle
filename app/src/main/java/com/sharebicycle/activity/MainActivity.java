package com.sharebicycle.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // 如果本地蓝牙没有开启，则开启
        if (!mBluetoothAdapter.isEnabled()) {
            // 我们通过startActivityForResult()方法发起的Intent将会在onActivityResult()回调方法中获取用户的选择，比如用户单击了Yes开启，
            // 那么将会收到RESULT_OK的结果，
            // 如果RESULT_CANCELED则代表用户不愿意开启蓝牙
            Intent mIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(mIntent, 1);
            // 用enable()方法来开启，无需询问用户(实惠无声息的开启蓝牙设备),这时就需要用到android.permission.BLUETOOTH_ADMIN权限。
            // mBluetoothAdapter.enable();
            // mBluetoothAdapter.disable();//关闭蓝牙
        }
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
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                //  System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
