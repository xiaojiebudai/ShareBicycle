package com.sharebicycle.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.sharebicycle.MyApplication;
import com.sharebicycle.activity.LoginActivity;
import com.sharebicycle.activity.PersonCenterActivity;
import com.sharebicycle.activity.ScanActivity;
import com.sharebicycle.utils.WWToast;
import com.sharebicycle.www.R;

/**
 * Created by ZXJ on 2017/5/10.
 * 地图换高德
 */

public class BaiduMapFragment extends FatherFragment {
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private TextureMapView mapView;
    private AMap aMap;
    //以前的定位点
    private LatLng oldLatLng;
    //是否是第一次定位
    private boolean isFirstLatLng;
    @Override
    protected int getLayoutId() {
        return R.layout.frag_baidu;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        if (mGroup == null) {
            mGroup = inflater.inflate(getLayoutId(), container, false);
            mapView = (TextureMapView) mGroup.findViewById(R.id.map);
            mapView.onCreate(savedInstanceState);
            isFirstLatLng=true;
            aMap = mapView.getMap();
            UiSettings mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象

            mUiSettings.setZoomControlsEnabled(false);
            mUiSettings.setCompassEnabled(true);
//            mUiSettings.setMyLocationButtonEnabled(true); //显示默认的定位按钮

            aMap.setTrafficEnabled(true);// 显示实时交通状况
            //地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT
            aMap.setMapType(AMap.MAP_TYPE_NORMAL);// 地图模式
            mGroup.findViewById(R.id.iv_saoma).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(MyApplication.isLogin()){
                        Intent intent=new Intent();
                        intent.setClass(getActivity(), ScanActivity.class);
                        BaiduMapFragment.this.startActivityForResult(intent,888);
                    }else{
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    }


                }
            });     mGroup.findViewById(R.id.iv_local).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startLocation();

                }
            });
            aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {

                }

                @Override
                public void onCameraChangeFinish(CameraPosition cameraPosition) {
                    WWToast.showShort(cameraPosition.target.toString());

                }
            });

        }

        initLocation();
        startLocation();
        return mGroup;
    }
    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     */
    private void changeCamera(CameraUpdate update) {

        aMap.moveCamera(update);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            if(requestCode==888){
                //扫描结果
                String s=data.getStringExtra("codedContent");
                WWToast.showShort(s);
            }
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        destroyLocation();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 初始化定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(getActivity().getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(10000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }
    /**绘制两个坐标点之间的线段,从以前位置到现在位置*/
    private void setUpMap(LatLng oldData, LatLng newData ) {
        // 绘制一个大地曲线
        aMap.addPolyline((new PolylineOptions())
                .add(oldData, newData)
                .geodesic(true).color(Color.GREEN));

    }
    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {

                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (location.getErrorCode() == 0) {


//                //定位成功
                    LatLng newLatLng =new LatLng(location.getLatitude(),location.getLongitude());
                    changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                            newLatLng, 18, 20, 0)));
                    stopLocation();
//                    if(isFirstLatLng){
//                        //记录第一次的定位信息
//                        oldLatLng = newLatLng;
//                        isFirstLatLng = false;
//                    }
//                    //位置有变化
//                    if(oldLatLng != newLatLng){
//                        setUpMap( oldLatLng , newLatLng );
//                        oldLatLng = newLatLng;
//                    }

                } else {
                    //定位失败

                }

                //解析定位结果，


            }
        }
    };

    /**
     * 开始定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void startLocation() {
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 停止定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void stopLocation() {
        // 停止定位
        locationClient.stopLocation();
    }

    /**
     * 销毁定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }
}
