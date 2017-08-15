package com.lovegod.newbuy.view.map;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.bean.Shop;


/**
 * *****************************************
 * Created by thinking on 2017/6/10.
 * 创建时间：
 * <p>
 * 描述：
 * <p/>
 * <p/>
 * *******************************************
 */

public class ShopLocationMap extends AppCompatActivity {

    MapView bmapView = null;
    BaiduMap mbaidmap;
    LocationClient mLocClient;//定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动

    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;


    Toolbar map_toobar;
    ImageView map_bacck;
    TextView map_shop_name;
    FloatingActionButton flg_navigation;


    //对话框
    Dialog shopDialog;
    TextView dialog_shop_name;//对话框商店名称
    TextView dialog_shop_position;//对话框商店位置
    TextView dialog_shop_distance;//对话框商店距离
    TextView dialog_me_position;//对话框我的位置

    ImageView traffic_map;
    ImageView satellite_map;

    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;

    MarkerOptions optionA;
   static MarkerOptions optionB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.shop_map);
        bmapView = (MapView) findViewById(R.id.bmapView);
         Shop shopd = (Shop) getIntent().getSerializableExtra("shop_info");
        map_bacck = (ImageView) findViewById(R.id.map_bacck);
        map_shop_name = (TextView) findViewById(R.id.map_shop_name);
        flg_navigation = (FloatingActionButton) findViewById(R.id.flg_navigation);
        map_toobar = (Toolbar) findViewById(R.id.map_toobar);

        traffic_map = (ImageView) findViewById(R.id.traffic_map);
        satellite_map = (ImageView) findViewById(R.id.satellite_map);

        //对话框
        shopDialog = new Dialog(this, R.style.map_dialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.map_dialog, null);
        // root.findViewById(R.id.park_info_relativelayout).setOnClickListener(dialoglistener);
        dialog_shop_name = (TextView) root.findViewById(R.id.dialog_shop_name);
        dialog_shop_position = (TextView) root.findViewById(R.id.dialog_shop_position);
        dialog_shop_distance = (TextView) root.findViewById(R.id.dialog_shop_distance);
        dialog_me_position = (TextView) root.findViewById(R.id.dialog_me_position);
        //dialog_me_position.setText(address);
        shopDialog.setCanceledOnTouchOutside(true);//点击外部使对话框消失
        shopDialog.setContentView(root);
        Window dialogWindow = shopDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = -20; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        dialogWindow.setAttributes(lp);

        flg_navigation.hide();//浮动按钮隐藏

        mbaidmap = bmapView.getMap();
        mbaidmap.setMapType(BaiduMap.MAP_TYPE_NORMAL);//普通地图
        mbaidmap.setMyLocationEnabled(true);//允许定位图层
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;//普通态： 更新定位数据时不对地图做任何操作
      /*  LatLng ll = new LatLng(113.690422, 34.599417);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(18.0f);
        mbaidmap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));*/

        // 定位初始化
        mLocClient = new LocationClient(this);
//        mbaidmap.setMyLocationConfiguration(new MyLocationConfiguration(mCurrentMode, true, null)); //设置定位数据, 只有先允许定位图层后设置数据才会生效
        /*MyLocationConfiguration(MyLocationConfiguration.LocationMode mode,boolean enableDirection,BitmapDescriptor customMarker,int accuracyCircleFillColor,int accuracyCircleStrokeColor)
           mode - 定位图层显示方式, 默认为 LocationMode.NORMAL 普通态
           enableDirection - 是否允许显示方向信息
           customMarker - 设置用户自定义定位图标，可以为 null
           accuracyCircleFillColor - 设置精度圈填充颜色
           accuracyCircleStrokeColor - 设置精度圈填充颜色
        */
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        option.setNeedDeviceDirect(true);
        option.setOpenAutoNotifyMode();
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        option.setIsNeedLocationDescribe(true);//设置是否需要返回位置语义化信息，可以在BDLocation.getLocationDescribe()中得到数据，ex:"在天安门附近"， 可以用作地址信息的补充
        option.setIsNeedLocationPoiList(true);//设置是否需要返回位置POI信息，可以在BDLocation.getPoiList()中得到数据
        mLocClient.setLocOption(option);//option - client相关的参数设定
        mLocClient.start();//启动定位sdk

        LatLng shop_ll = new LatLng(shopd.getLonggitude(), shopd.getLatitude());
        initmark(shop_ll);
    }

    private void initmark(LatLng ll) {
        final Shop shopd2 = (Shop) getIntent().getSerializableExtra("shop_info");
        BitmapDescriptor bd10 = BitmapDescriptorFactory.fromResource(R.mipmap.end_point);
        optionA = new MarkerOptions().position(ll).icon(bd10).title(shopd2.getShopname());
        //在地图上添加Marker，并显示
        mbaidmap.addOverlay(optionA);
        mbaidmap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                dialog_me_position.setText(optionB.getTitle());
                dialog_shop_distance.setText(String.valueOf(DistanceUtil.getDistance(optionB.getPosition(), marker.getPosition())));
                dialog_shop_name.setText(marker.getTitle());
                dialog_shop_position.setText(shopd2.getSaddress());
                shopDialog.show();
                flg_navigation.show();
                flg_navigation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {




                    }
                });
                return false;
            }
        });


    }

    private void init(LatLng mell,String address) {
        //对话框
        shopDialog = new Dialog(this, R.style.map_dialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.map_dialog, null);
        // root.findViewById(R.id.park_info_relativelayout).setOnClickListener(dialoglistener);
        dialog_shop_name = (TextView) root.findViewById(R.id.dialog_shop_name);
        dialog_shop_position = (TextView) root.findViewById(R.id.dialog_shop_position);
        dialog_shop_distance = (TextView) root.findViewById(R.id.dialog_shop_distance);
        dialog_me_position = (TextView) root.findViewById(R.id.dialog_me_position);
        dialog_me_position.setText(address);
        shopDialog.setCanceledOnTouchOutside(true);//点击外部使对话框消失
        shopDialog.setContentView(root);
        Window dialogWindow = shopDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = -20; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        dialogWindow.setAttributes(lp);
        shopDialog.show();

    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null || bmapView == null) {
                return;
            }
            mCurrentLat = bdLocation.getLatitude();
            mCurrentLon = bdLocation.getLongitude();
            locData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();
            mbaidmap.setMyLocationData(locData);
            if (isFirstLoc == true) {
                isFirstLoc = false;
                LatLng me_ll = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
                String me_address=bdLocation.getAddrStr();
                optionB.position(me_ll).title(me_address);

            }
        }

        public void onConnectHotSpotMessage(String s, int i) {

        }

    }


    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mbaidmap.setMyLocationEnabled(false);
        bmapView.onDestroy();
        bmapView = null;
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        bmapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        bmapView.onPause();
    }
}
