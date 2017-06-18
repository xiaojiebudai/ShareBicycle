package com.sharebicycle.activity;

import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.MyLocationStyle;
import com.sharebicycle.MyApplication;
import com.sharebicycle.api.ApiLock;
import com.sharebicycle.been.Device;
import com.sharebicycle.been.RidingOrder;
import com.sharebicycle.service.BluetoothLeService;
import com.sharebicycle.utils.Consts;
import com.sharebicycle.utils.ParamsUtils;
import com.sharebicycle.utils.TimeUtil;
import com.sharebicycle.utils.WWToast;
import com.sharebicycle.utils.WWViewUtil;
import com.sharebicycle.utils.ZLog;
import com.sharebicycle.www.R;
import com.sharebicycle.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZXJ on 2017/5/23.
 * 扫码-->获取mac链接设备以及开锁指令-->开锁-->开锁结果给后台-->开锁结果展示
 */

public class OpenActivity extends FatherActivity {

    @BindView(R.id.ll_opening)
    LinearLayout llOpening;
    @BindView(R.id.map)
    MapView map;
    @BindView(R.id.chronometer)
    Chronometer chronometer;
    @BindView(R.id.tv_riding_price)
    TextView tvRidingPrice;
    @BindView(R.id.ll_riding)
    FrameLayout llRiding;
    @BindView(R.id.tv_finish_price)
    TextView tvFinishPrice;
    @BindView(R.id.tv_finish_daijingquan)
    TextView tvFinishDaijingquan;
    @BindView(R.id.tv_ok)
    TextView tvOk;
    @BindView(R.id.ll_pay)
    FrameLayout llPay;
    @BindView(R.id.ll_opened)
    FrameLayout llOpened;
    @BindView(R.id.infoOperating)
    ImageView infoOperatingIV;
    private StringBuffer sbValues;
    private String mDeviceAddress;

    private BluetoothLeService mBluetoothLeService;
    private boolean mConnected = false;


    String openStr = "xQ0OQel79+eOmYRP+9hhollQBECHZ1hSZvnHNZ9ksru/9uqQGTVjvCpBdgTPALVMQLQpiGD4sUwrET5mi1HRxw==";


    boolean connect_status_bit = false;
    private AMap aMap;

    public static final int OPEN = 0;
    public static final int RIDING = 1;
    private int model;
    private double consumeNow;

    @Override
    protected int getLayoutId() {
        return R.layout.act_open;
    }

    @Override
    protected void initValues() {
        initDefautHead("开锁中。。。", false);
        model = getIntent().getIntExtra(Consts.KEY_MODULE, OPEN);
        if (model == OPEN) {
            scanData = getIntent().getStringExtra("Data");
        } else {
            order = JSONObject.parseObject(getIntent().getStringExtra(Consts.KEY_DATA), RidingOrder.class);
        }
    }

    @Override
    protected void initView() {
        // Sets up UI references.

        sbValues = new StringBuffer();

        Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        infoOperatingIV.startAnimation(operatingAnim);


    }


    @Override
    public void onBackPressed() {
        if (!isGo) super.onBackPressed();
    }
private   Timer timer;
    @Override
    protected void doOperate() {
        if (model == OPEN) {
            sendOpenQr(scanData);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isGo) {
                        //20s未开锁直接失败
                        WWToast.showShort("开锁失败，请重试");
                        finish();
                    }
                }
            }, 40000);


        } else {
            if (order != null && order.Status == 0) {
                //骑行中
                initDefautHead("骑行中", false);
                mDeviceAddress = order.Bluetooth;
                llOpening.setVisibility(View.GONE);
                llOpened.setVisibility(View.VISIBLE);
                llRiding.setVisibility(View.VISIBLE);
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
                registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
                timer=new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (mBluetoothLeService != null) {
                            if(!connect_status_bit){
                                mBluetoothLeService.connect(mDeviceAddress);
                            }
                        }
                    }
                },500,1000);

                Intent gattServiceIntent = new Intent(OpenActivity.this, BluetoothLeService.class);
                bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
                //设置开始计时时间
                chronometer.setBase(order.BeginTime * 1000);
                //启动计时器
                chronometer.start();
                isGo = true;
            } else {
                //骑行结算
                initDefautHead("骑行结束", false);
                llOpening.setVisibility(View.GONE);
                llOpened.setVisibility(View.VISIBLE);
                llRiding.setVisibility(View.GONE);
                llPay.setVisibility(View.VISIBLE);
                tvFinishPrice.setText(WWViewUtil.numberFormatPrice(order.PayAmt));
                tvOk.setText("去充值");
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
//        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        map.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
        dismissWaitDialog();
        unregisterReceiver(mGattUpdateReceiver);
        map.onDestroy();
        timer.cancel();
    }
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

                if (mBluetoothLeService != null) {
                    if (mConnected == false) {
                        final boolean result = mBluetoothLeService.connect(mDeviceAddress);
                    }
                }
            }
            if (msg.what == 2) {
                try {
                    Thread.currentThread();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mBluetoothLeService.enable_JDY_ble(0);
                try {
                    Thread.currentThread();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mBluetoothLeService.enable_JDY_ble(0);
                try {
                    Thread.currentThread();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mBluetoothLeService.enable_JDY_ble(1);
                try {
                    Thread.currentThread();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                byte[] WriteBytes = new byte[2];
                WriteBytes[0] = (byte) 0xE7;
                WriteBytes[1] = (byte) 0xf6;
                mBluetoothLeService.function_data(WriteBytes);// 发送读取所有IO状态
            }
            super.handleMessage(msg);
        }
    };
    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                connect_status_bit = true;
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                connect_status_bit = false;
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) //接收FFE1串口透传数据通道数据
            {
                //displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
                //byte data1;
                //intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);//  .getByteExtra(BluetoothLeService.EXTRA_DATA, data1);
                displayData(intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA));


            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE1.equals(action)) //接收FFE2功能配置返回的数据
            {
                displayData(intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };
    int len_g = 0;

    private void displayData(byte[] data1) //接收FFE1串口透传数据通道数据
    {
        if (data1 != null && data1.length > 0) {
            String res = new String(data1);
            ZLog.showPost(res);
            if (sbValues.length() >= 88) return;
            sbValues.append(res);
            len_g += data1.length;
            if (sbValues.length() >= 5000) sbValues.delete(0, sbValues.length());

            if (sbValues.length() == 88) {
                WWToast.showShort("收到设备反馈");
                infoReceive(sbValues.toString(), mDeviceAddress);
            }
        }

    }

    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {


        if (gattServices == null) return;

        if (gattServices.size() > 0 && mBluetoothLeService.get_connected_status(gattServices) == 2)//表示为JDY-06、JDY-08系列蓝牙模块
        {
            if (connect_status_bit) {
                mConnected = true;
                WWToast.showShort("蓝牙链接");
                mBluetoothLeService.Delay_ms(100);
                mBluetoothLeService.enable_JDY_ble(0);
                mBluetoothLeService.Delay_ms(100);
                mBluetoothLeService.enable_JDY_ble(1);
                mBluetoothLeService.Delay_ms(100);

                byte[] WriteBytes = new byte[2];
                WriteBytes[0] = (byte) 0xE7;
                WriteBytes[1] = (byte) 0xf6;
                mBluetoothLeService.function_data(WriteBytes);// 发送读取所有IO状态
                if (model == OPEN) {
                    if (sbValues != null && sbValues.length() > 0) {
                        sbValues.delete(0, sbValues.length());
                    }
                    mBluetoothLeService.txxx(openStr, true);//发送字符串数据
                }


            } else {
                //Toast.makeText(this, "Deleted Successfully!", Toast.LENGTH_LONG).show();
                Toast toast = Toast.makeText(OpenActivity.this, "设备没有连接！", Toast.LENGTH_SHORT);
                toast.show();
            }
        } else if (gattServices.size() > 0 && mBluetoothLeService.get_connected_status(gattServices) == 1)//表示为JDY-09、JDY-10系列蓝牙模块
        {
            if (connect_status_bit) {
                mConnected = true;
                WWToast.showShort("蓝牙链接");

                mBluetoothLeService.Delay_ms(100);
                mBluetoothLeService.enable_JDY_ble(0);

                if (model == OPEN) {
                    if (sbValues != null && sbValues.length() > 0) {
                        sbValues.delete(0, sbValues.length());
                    }
                    mBluetoothLeService.txxx(openStr, true);//发送字符串数据
                }

            } else {
                //Toast.makeText(this, "Deleted Successfully!", Toast.LENGTH_LONG).show();
                Toast toast = Toast.makeText(OpenActivity.this, "设备没有连接！", Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(OpenActivity.this, "提示！此设备不为JDY系列BLE模块", Toast.LENGTH_SHORT);
            toast.show();
        }


    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE1);
        return intentFilter;
    }


    /**
     * 发送开锁扫码数据
     *
     * @param scanData
     */
    private String taskId;
    private String scanData;

    private void sendOpenQr(String scanData) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("scanData", scanData);
        jsonObject.put(Consts.KEY_SESSIONID, MyApplication
                .getInstance().getSessionId());
        x.http().post(ParamsUtils.getPostJsonParams(jsonObject, ApiLock.OpenSend()), new WWXCallBack("OpenSend") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                Device device = JSONObject.parseObject(data.getString("Data"), Device.class);
                taskId = data.getString("TaskId");
                openStr = device.CommandText;
                mDeviceAddress = device.Bluetooth;
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
                registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
                timer=new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (mBluetoothLeService != null) {
                            if(!connect_status_bit){
                                mBluetoothLeService.connect(mDeviceAddress);
                            }
                        }
                    }
                },500,1000);
                Intent gattServiceIntent = new Intent(OpenActivity.this, BluetoothLeService.class);
              bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);


            }

            @Override
            public void onAfterFinished() {

            }
        });
    }

    private boolean isGo = false;

    /**
     * 关锁指令
     */
    private void infoReceive(String receiveData, String address) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("receiveData", receiveData);
        jsonObject.put("bluetooth", address);
        jsonObject.put(Consts.KEY_SESSIONID, MyApplication
                .getInstance().getSessionId());
        ZLog.showPost(receiveData);
        //发送完立马清除
        if (sbValues != null && sbValues.length() > 0) {
            sbValues.delete(0, sbValues.length());
        }
        x.http().post(ParamsUtils.getPostJsonParams(jsonObject, ApiLock.LockReveice()), new WWXCallBack("LockReveice") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                Device device = JSONObject.parseObject(data.getString("Data"), Device.class);
                WWToast.showShort("收到后台验证");
                switch (device.CommandName) {
                    case Device.INIT:
                        break;
                    case Device.OPEN:
                        mBluetoothLeService.txxx(device.CommandText, true);//发送字符串数据
                        initDefautHead("骑行中", false);
                        WWToast.showShort("开始用车");
                        isGo = true;

                        llOpening.setVisibility(View.GONE);
                        llOpened.setVisibility(View.VISIBLE);
                        llRiding.setVisibility(View.VISIBLE);
                        getLastOrder(true);
                        timer.cancel();

                        break;
                    case Device.CLOSE:
                        mBluetoothLeService.txxx(device.CommandText, true);//发送字符串数据
                        WWToast.showShort("关锁成功");
                        timer.cancel();
                        chronometer.stop();
                        initDefautHead("骑行结束", false);
                        llRiding.setVisibility(View.GONE);
                        llPay.setVisibility(View.VISIBLE);
                        getLastOrder(false);
                        break;
                    case Device.UNKNOW:
                        break;
                    default:
                        break;
                }


            }

            @Override
            public void onAfterFinished() {
            }
        });
    }

    /**
     * 获取订单信息
     *
     * @param runing
     */
    RidingOrder order;

    private void getLastOrder(final boolean runing) {
        RequestParams params = ParamsUtils.getSessionParams(ApiLock.LastOrder());
        x.http().get(params, new WWXCallBack("LastOrder") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                order = JSONObject.parseObject(data.getString("Data"), RidingOrder.class);
                if (runing) {
                    //骑行中
                    tvRidingPrice.setText("当前费用:1.00元");
                    //设置开始计时时间
                    chronometer.setBase(System.currentTimeMillis());
                    //为计时器绑定监听事件
                    chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                        @Override
                        public void onChronometerTick(Chronometer ch) {
                            long time = (System.currentTimeMillis() - ch.getBase()) / 1000;
                            chronometer.setText(TimeUtil.getTimeDifference(time));
                            long hour = time % (24 * 3600) / 3600+1;
                            if (hour > 0) {
                                if (order.PriceUnit == 30) {
                                    hour = hour * 2;
                                }
                                if(consumeNow!=hour){
                                    consumeNow=hour;
                                    tvRidingPrice.setText("当前费用:" + WWViewUtil.numberFormatPrice(consumeNow) + "元");
                                }

                            }
                        }
                    });
                    //启动计时器
                    chronometer.start();
                } else {
                    //骑行结束
                    tvFinishPrice.setText(WWViewUtil.numberFormatPrice(order.PayAmt));
                    if (order.Status == 1) {
                        tvOk.setText("去充值");
                    } else if (order.Status == 2) {
                        tvOk.setText("确认支付");
                    }

                }
            }

            @Override
            public void onAfterFinished() {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        map.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        aMap = map.getMap();
        UiSettings mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象

        mUiSettings.setZoomControlsEnabled(false);
        mUiSettings.setCompassEnabled(true);
//            mUiSettings.setMyLocationButtonEnabled(true); //显示默认的定位按钮
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.mipmap.index_location_icon)));
        myLocationStyle.strokeWidth(5);
        myLocationStyle.strokeColor(getResources().getColor(R.color.transparent));//设置定位蓝点精度圆圈的边框颜色的方法。
        myLocationStyle.radiusFillColor(getResources().getColor(R.color.transparent));//设置定位蓝点精度圆圈的填充颜色的方法。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        aMap.setTrafficEnabled(true);// 显示实时交通状况
        //地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);// 地图模式
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        cameraPosition.target, 17, 30, 0)));

            }
        });
    }

    @OnClick(R.id.tv_ok)
    public void onViewClicked() {
        //如果Status==1就表示余额不够扣
//        如果Status==2就是已经扣完费了
//        Status==0就表示还没收到关锁信息
        if (order.Status == 1) {
            startActivityForResult(new Intent(this, RechagerActivity.class), 999);
        } else if (order.Status == 2) {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 999) {
                finish();
            }
        }
    }
}
