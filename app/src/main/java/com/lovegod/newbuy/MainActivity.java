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
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.service.BluetoothService;
import com.lovegod.newbuy.view.BaseActivity;
import com.lovegod.newbuy.view.carts.CartActivity;
import com.lovegod.newbuy.view.fragment.Home_Activity;
import com.lovegod.newbuy.view.goods.GoodActivity;
import com.lovegod.newbuy.view.sorts.SortActivity;
import com.lovegod.newbuy.view.utils.BottomNavigationViewHelper;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lovegod.newbuy.R.id.foot_home;
import static com.lovegod.newbuy.R.id.ly_foot_cart;
import static com.lovegod.newbuy.R.id.ly_foot_history;
import static com.lovegod.newbuy.R.id.ly_foot_home;
import static com.lovegod.newbuy.R.id.ly_foot_me;

public class MainActivity extends BaseActivity {

    private static Timer timer = new Timer(true);
    public static final int REQUEST_CODE = 0;
    @BindView(R.id.container)
    CoordinatorLayout coordinatorLayout;


    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.content)
    FrameLayout content;




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


        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Home_Activity fg1 = new Home_Activity();
        ft.add(R.id.content, fg1);
        ft.commit();
        BottomNavigationViewHelper.disableShiftMode(navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                //    replaceFragment(mHome_Activity);
                    return true;
                case R.id.navigation_sort:
                  /*  if (null == mMinTaoFragment) {
                        mMinTaoFragment = new Mintaofragment();
                    }
                    replaceFragment(mMinTaoFragment);*/
                    Intent intent1 = new Intent(MainActivity.this,SortActivity.class);
                    startActivity(intent1);
                    return true;
                case R.id.navigation_history:

                    return true;
                case R.id.navigation_cart:
                    Intent intent3 = new Intent(MainActivity.this, CartActivity.class);
                    startActivity(intent3);

                    return true;
                case R.id.navigation_me:

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
}
