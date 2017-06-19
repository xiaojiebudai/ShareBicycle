package com.sharebicycle.activity;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.TextView;

import com.sharebicycle.service.BluetoothLeService;
import com.sharebicycle.utils.WWToast;
import com.sharebicycle.utils.ZLog;
import com.sharebicycle.www.R;
import com.vise.baseble.ViseBluetooth;
import com.vise.baseble.callback.IBleCallback;
import com.vise.baseble.callback.IConnectCallback;
import com.vise.baseble.exception.BleException;
import com.vise.baseble.utils.HexUtil;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZXJ on 2017/6/18.
 */

public class BTtestActivity extends FatherActivity {
    @BindView(R.id.mac)
    TextView mac;
    @BindView(R.id.state)
    TextView state;
    @BindView(R.id.open)
    TextView open;    @BindView(R.id.info)
    TextView info;
    private String mDeviceAddress="A4:C1:38:77:29:B4";
    private String commandText="IHkB8Mn+39dDBv6pGsoqUQ1d3Yd9ddw0GCf8o9WjkcePvzDeJ49zvvb4C6rag6F3fzvjt4qNv\\/6Dt9rgsD2U6Q==";

    private boolean mConnected = false;
    private String  Service_uuid = "0000ffe0-0000-1000-8000-00805f9b34fb";
    private String   Characteristic_uuid_TX = "0000ffe1-0000-1000-8000-00805f9b34fb";
    private String   Characteristic_uuid_FUNCTION = "0000ffe2-0000-1000-8000-00805f9b34fb";
    private StringBuilder mOutputInfo = new StringBuilder();
    @Override
    protected int getLayoutId() {
        return R.layout.act_bt_test;
    }

    @Override
    protected void initValues() {

    }

    @Override
    protected void initView() {
        mac.setText(mDeviceAddress);

    }
private BluetoothGattCharacteristic characteristic;
    @Override
    protected void doOperate() {
        ViseBluetooth.getInstance().connectByMac(mDeviceAddress, false, new IConnectCallback() {
            @Override
            public void onConnectSuccess(BluetoothGatt gatt, int status) {
                WWToast.showShort("链接成功");
                state.setText("链接成功");
                if (gatt != null) {
                    ZLog.showPost("gatt");
                    gatt.getService(UUID.fromString(Service_uuid)).getCharacteristics();
                     characteristic = gatt.getService(UUID.fromString(Service_uuid)).getCharacteristic(UUID.fromString(Characteristic_uuid_TX));
                    ViseBluetooth.getInstance().enableCharacteristicNotification(characteristic, new IBleCallback<BluetoothGattCharacteristic>() {
                        @Override
                        public void onSuccess(BluetoothGattCharacteristic bluetoothGattCharacteristic, int type) {
                            WWToast.showShort("监听服务成功");
                            if (characteristic == null || characteristic.getValue() == null) {
                                return;
                            }
//                            ViseLog.i("notify success:" + HexUtil.encodeHexStr(characteristic.getValue()));
                            mOutputInfo.append(HexUtil.encodeHexStr(characteristic.getValue())).append("\n");
                            info.setText(mOutputInfo.toString());
                        }

                        @Override
                        public void onFailure(BleException exception) {

                        }
                    }, true);

//                    txxx(commandText,gatt);

                }

            }

            @Override
            public void onConnectFailure(BleException exception) {

            }

            @Override
            public void onDisconnect() {

            }
        });
    }

    private Queue<byte[]> dataInfoQueue = new LinkedList<>();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            send();
        }
    };

    private void send(byte[] data) {
        if (dataInfoQueue != null) {
            dataInfoQueue.clear();
            dataInfoQueue = splitPacketFor20Byte(data);
            handler.post(runnable);
        }
    }

    private void send() {
        if (dataInfoQueue != null && !dataInfoQueue.isEmpty()) {
            if (dataInfoQueue.peek() != null) {
//                ViseBluetooth.getInstance().writeCharacteristic(characteristic, dataInfoQueue.poll(), new
                ViseBluetooth.getInstance().writeCharacteristic(characteristic, dataInfoQueue.poll(), new IBleCallback<BluetoothGattCharacteristic>() {
                    @Override
                    public void onSuccess(BluetoothGattCharacteristic bluetoothGattCharacteristic, int type) {

                    }

                    @Override
                    public void onFailure(BleException exception) {

                    }
                });
            }
            if (dataInfoQueue.peek() != null) {
                handler.postDelayed(runnable, 100);
            }
        }
    }

    private Queue<byte[]> splitPacketFor20Byte(byte[] data) {
        Queue<byte[]> dataInfoQueue = new LinkedList<>();
        if (data != null) {
            int index = 0;
            do {
                byte[] surplusData = new byte[data.length - index];
                byte[] currentData;
                System.arraycopy(data, index, surplusData, 0, data.length - index);
                if (surplusData.length <= 20) {
                    currentData = new byte[surplusData.length];
                    System.arraycopy(surplusData, 0, currentData, 0, surplusData.length);
                    index += surplusData.length;
                } else {
                    currentData = new byte[20];
                    System.arraycopy(data, index, currentData, 0, 20);
                    index += 20;
                }
                dataInfoQueue.offer(currentData);
            } while (index < data.length);
        }
        return dataInfoQueue;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.open)
    public void onViewClicked() {
        send(commandText.getBytes());
    }
    @Override
    protected void onResume() {
        super.onResume();
        ViseBluetooth.getInstance().connectByMac(mDeviceAddress, false, new IConnectCallback() {
            @Override
            public void onConnectSuccess(BluetoothGatt gatt, int status) {
                WWToast.showShort("链接成功");
                state.setText("链接成功");
            }

            @Override
            public void onConnectFailure(BleException exception) {

            }

            @Override
            public void onDisconnect() {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        ViseBluetooth.getInstance().disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViseBluetooth.getInstance().clear();
    }

}
