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
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.service.BluetoothService;
import com.lovegod.newbuy.view.BaseActivity;
import com.lovegod.newbuy.view.fragment.Home_Activity;
import com.lovegod.newbuy.view.goods.GoodActivity;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    private static Timer timer = new Timer(true);
    public static final int REQUEST_CODE = 0;
    @BindView(R.id.container)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.ly_foot_home)
    LinearLayout ly_foot_home;
    @BindView(R.id.ly_foot_history)
    LinearLayout ly_foot_history;
    @BindView(R.id.ly_foot_cart)
    LinearLayout ly_foot_cart;
    @BindView(R.id.ly_foot_me)
    LinearLayout ly_foot_me;

    @BindView(R.id.foot_home)
    TextView foot_home;
    @BindView(R.id.foot_history)
    TextView foot_history;
    @BindView(R.id.foot_cart)
    TextView foot_cart;
    @BindView(R.id.foot_me)
    TextView foot_me;

    @BindView(R.id.home_image)
    ImageView home_image;
    @BindView(R.id.history_image)
    ImageView history_image;
    @BindView(R.id.cart_image)
    ImageView cart_image;
    @BindView(R.id.me_image)
    ImageView me_image;



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
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
        ly_foot_home.performClick();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Home_Activity fg1 = new Home_Activity();
        ft.add(R.id.fl, fg1);
        ft.commit();
    }




    private void initView() {
        foot_home.setTextColor(getResources().getColor(R.color.colorPrimary));

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
//            finish();
//
//            System.exit(0);
//            android.os.Process.killProcess(android.os.Process.myPid());
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
}
