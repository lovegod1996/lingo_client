package com.lovegod.newbuy;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.Location;
import com.lovegod.newbuy.service.BluetoothService;
import com.lovegod.newbuy.view.BaseActivity;
import com.lovegod.newbuy.view.fragment.Cart_Activity;
import com.lovegod.newbuy.view.fragment.Life_Fragment;
import com.lovegod.newbuy.view.fragment.MyInfo_Activity;
import com.lovegod.newbuy.view.fragment.Sort_Activity;
import com.lovegod.newbuy.view.myinfo.MyInfoActivity;
import com.lovegod.newbuy.view.carts.CartActivity;
import com.lovegod.newbuy.view.fragment.Home_Activity;
import com.lovegod.newbuy.view.goods.GoodActivity;
import com.lovegod.newbuy.view.sorts.SortActivity;
import com.lovegod.newbuy.view.utils.BottomNavigationViewHelper;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends BaseActivity {

    private static Timer timer = new Timer(true);
    public static final int REQUEST_CODE = 0;
    @BindView(R.id.container)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.content)
    FrameLayout content;

    private final OkHttpClient client = new OkHttpClient();

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        MyApplication.getInstance().addActivity(this);
        ButterKnife.bind(this);
        // 检查当前手机是否支持ble 蓝牙,如果不支持退出程序
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "不支持蓝牙", Toast.LENGTH_SHORT).show();
            finish();
        }

        /**
         * 权限请求返回
         */
        PermissionCall permissionCall = new PermissionCall() {
            @Override
            public void requestSuccess() {
                showSnackBar(coordinatorLayout, "已授予权限", Snackbar.LENGTH_SHORT);
            }

            @Override
            public void refused() {
                showSnackBar(coordinatorLayout, "授予失败", Snackbar.LENGTH_SHORT);
            }
        };
        String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION };
        if (!checkPermissionGranted(permissions)) {
            requestRunTimePermissions(permissions, permissionCall);
        }
        //检查蓝牙
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

        BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Toast.makeText(this, "不支持蓝牙", Toast.LENGTH_SHORT).show();
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
            Toast.makeText(this, "蓝牙没打开", Toast.LENGTH_SHORT).show();
        }
        timer.schedule(timerTask, 0, 30 * 1000);

        initView();


        replaceFragment(new Home_Activity());
        BottomNavigationViewHelper.disableShiftMode(navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    /**
     * 添加基站定位
     */
    private void init() {

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String operator = telephonyManager.getNetworkOperator();
        String mcc = operator.substring(0, 3);
        String mnc = operator.substring(3);
        int cid=0;
        int lac=0;
        if(telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA){
            CdmaCellLocation cdmaCellLocation = (CdmaCellLocation)
                    telephonyManager.getCellLocation();
             cid = cdmaCellLocation.getBaseStationId(); //获取cdma基站识别标号 BID
             lac = cdmaCellLocation.getNetworkId(); //获取cdma网络编号NID
           int sid = cdmaCellLocation.getSystemId(); //用谷歌API的话cdma网络的mnc要用这个getSystemId()取得→SID
        }else{
            GsmCellLocation gsmCellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
             cid = gsmCellLocation.getCid(); //获取gsm基站识别标号
             lac = gsmCellLocation.getLac(); //获取gsm网络编号
        }
        RequestBody formBody = new FormBody.Builder()
                .add("coord", "gcj02")
                .add("output","json")
                .add("mcc",mcc)
                .add("mnc",mnc)
                .add("lac",lac+"")
                .add("ci",cid+"")
                .build();
        Request request = new Request.Builder()
                .url("https://en.wikipedia.org/w/index.php")
                .post(formBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String result=response.body().toString();
            Gson gson=new Gson();
            Location location = gson.fromJson(result, Location.class);




        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                     replaceFragment(new Home_Activity());
                    return true;
                case R.id.navigation_sort:
                    replaceFragment(new Sort_Activity());
                    //startActivity(new Intent(MainActivity.this, SortActivity.class));
                    return true;
                case R.id.navigation_life:
                    replaceFragment(new Life_Fragment());
                    return true;
                case R.id.navigation_cart:
                    replaceFragment(new Cart_Activity());
                    //startActivity(new Intent(MainActivity.this, CartActivity.class));

                    return true;
                case R.id.navigation_me:
                    replaceFragment(new MyInfo_Activity());
                    //startActivity(new Intent(MainActivity.this, MyInfoActivity.class));
                    return true;

            }
            return false;
        }
    };


    private void initView() {
        // foot_home.setTextColor(getResources().getColor(R.color.colorPrimary));

        Commodity commodity = (Commodity) getIntent().getSerializableExtra("commodity");
        if (commodity != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("commodity", commodity);
            Intent intent = new Intent(this, GoodActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            //开启蓝牙所有推送服务
            Intent intentBlutooth = new Intent(MainActivity.this, BluetoothService.class);
            startService(intentBlutooth);
        }
    };

    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            MyApplication.getInstance().exit();
            System.exit(0);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            onBackPressed();
            return false;
        }
        return false;
    }

    /**
     * 替换fragment到主页面中
     * @param fragment 新的fragment
     */
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.content,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
