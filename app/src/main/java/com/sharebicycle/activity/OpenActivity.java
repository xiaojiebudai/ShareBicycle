package com.sharebicycle.activity;

import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.sharebicycle.api.ApiLock;
import com.sharebicycle.been.Device;
import com.sharebicycle.service.BluetoothLeService;
import com.sharebicycle.utils.WWToast;
import com.sharebicycle.utils.ZLog;
import com.sharebicycle.www.R;
import com.sharebicycle.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by ZXJ on 2017/5/23.
 * 扫码-->获取mac链接设备以及开锁指令-->开锁-->开锁结果给后台-->开锁结果展示
 */

public class OpenActivity extends FatherActivity {

    private StringBuffer sbValues;
    private String mDeviceAddress;

    private BluetoothLeService mBluetoothLeService;
    private boolean mConnected = false;


    String openStr = "xQ0OQel79+eOmYRP+9hhollQBECHZ1hSZvnHNZ9ksru/9uqQGTVjvCpBdgTPALVMQLQpiGD4sUwrET5mi1HRxw==";


    boolean connect_status_bit = false;
    private boolean isOpen = false;
    private Chronometer chronoeter;
    private SimpleDateFormat sdf;


    @Override
    protected int getLayoutId() {
        return R.layout.act_open;
    }

    @Override
    protected void initView() {
        // Sets up UI references.

        sbValues = new StringBuffer();
        chronoeter = (Chronometer) findViewById(R.id.chronometer);


        //为计时器绑定监听事件
        chronoeter.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer ch) {
                chronoeter.setText(sdf.format(new Date(SystemClock.elapsedRealtime() - ch.getBase())));
            }
        });

    }

    @Override
    protected void initValues() {
        scanData = getIntent().getStringExtra("Data");
        sdf = new SimpleDateFormat("HH:mm:ss");  // 时间样式

    }

    @Override
    public void onBackPressed() {
        if (!isGo) super.onBackPressed();
    }

    @Override
    protected void doOperate() {
        sendOpenQr(scanData);

    }




    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
        dismissWaitDialog();
    }
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
                finish();
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
    private long openTime = 0;
    private void displayData(byte[] data1) //接收FFE1串口透传数据通道数据
    {
        if (data1 != null && data1.length > 0) {
            String res = new String(data1);
            if (sbValues.length() >= 88 || (System.currentTimeMillis() - openTime) < 20000) return;
            sbValues.append(res);
            len_g += data1.length;
            if (sbValues.length() >= 5000) sbValues.delete(0, sbValues.length());

            ZLog.showPost(res.toString());
            if (sbValues.length() == 88) {

                if (isOpen) {
                    sendOpenData(sbValues.toString(), taskId);
                    isOpen = false;
                    openTime = System.currentTimeMillis();
                    sbValues.delete(0, sbValues.length());
                } else {

                    closeDevice(sbValues.toString(), mDeviceAddress);
                }


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

                mBluetoothLeService.Delay_ms(100);
                mBluetoothLeService.enable_JDY_ble(0);
                mBluetoothLeService.Delay_ms(100);
                mBluetoothLeService.enable_JDY_ble(1);
                mBluetoothLeService.Delay_ms(100);

                byte[] WriteBytes = new byte[2];
                WriteBytes[0] = (byte) 0xE7;
                WriteBytes[1] = (byte) 0xf6;
                mBluetoothLeService.function_data(WriteBytes);// 发送读取所有IO状态

                if (sbValues != null && sbValues.length() > 0) {
                    sbValues.delete(0, sbValues.length());
                }
                mBluetoothLeService.txxx(openStr, true);//发送字符串数据
                isOpen = true;
            } else {
                //Toast.makeText(this, "Deleted Successfully!", Toast.LENGTH_LONG).show();
                Toast toast = Toast.makeText(OpenActivity.this, "设备没有连接！", Toast.LENGTH_SHORT);
                toast.show();
            }
        } else if (gattServices.size() > 0 && mBluetoothLeService.get_connected_status(gattServices) == 1)//表示为JDY-09、JDY-10系列蓝牙模块
        {
            if (connect_status_bit) {
                mConnected = true;


                mBluetoothLeService.Delay_ms(100);
                mBluetoothLeService.enable_JDY_ble(0);
                if (sbValues != null && sbValues.length() > 0) {
                    sbValues.delete(0, sbValues.length());
                }
                mBluetoothLeService.txxx(openStr, true);//发送字符串数据
                isOpen = true;


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


    public static RequestParams getPostJsonParams(JSONObject jsonObject,
                                                  String url) {
        RequestParams params = new RequestParams(url);
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        return params;
    }

    /**
     * 发送开锁扫码数据
     *
     * @param scanData
     */
    private String taskId;
    private String scanData;

    private void sendOpenQr(String scanData) {

        showWaitDialog();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("scanData", scanData);
        x.http().post(getPostJsonParams(jsonObject, ApiLock.OpenSend()), new WWXCallBack("OpenSend") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                Device device = JSONObject.parseObject(data.getString("Data"), Device.class);
                taskId = data.getString("TaskId");
                openStr = device.CommandText;
                mDeviceAddress = device.Bluetooth;
                registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
                if (mBluetoothLeService != null) {
                   mBluetoothLeService.connect(mDeviceAddress);
                }

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
     * 发送开锁数据
     *
     * @param scanData
     * @param taskId
     */
    private void sendOpenData(String scanData, String taskId) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("receiveData", scanData);
        jsonObject.put("taskId", taskId);
        x.http().post(getPostJsonParams(jsonObject, ApiLock.OpenReveice()), new WWXCallBack("OpenReveice") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                Device device = JSONObject.parseObject(data.getString("Data"), Device.class);

                mBluetoothLeService.txxx(device.CommandText, true);//发送字符串数据
                initDefautHead("骑行中", false);
                WWToast.showShort("开始用车");
                //设置开始计时时间
                chronoeter.setBase(SystemClock.elapsedRealtime());
                //启动计时器
                chronoeter.start();
                isGo = true;
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });

    }

    /**
     * 关锁指令
     */
    private void closeDevice(String receiveData, String address) {
        showWaitDialog();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("receiveData", receiveData);
        jsonObject.put("bluetooth", address);
        x.http().post(getPostJsonParams(jsonObject, ApiLock.LockReveice()), new WWXCallBack("LockReveice") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                Device device = JSONObject.parseObject(data.getString("Data"), Device.class);
                mBluetoothLeService.txxx(device.CommandText, true);//发送字符串数据
                WWToast.showShort("关锁成功");
                startActivity(new Intent(OpenActivity.this, RidingFinishActivity.class));
                finish();
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }
}
