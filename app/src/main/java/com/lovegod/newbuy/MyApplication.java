package com.lovegod.newbuy;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.lovegod.newbuy.service.BlutoothCus;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 *
 */
public class MyApplication extends Application {
    public static List<BlutoothCus> blutoothCusList = new ArrayList<>();
    public static MyApplication instance;
    // 存储应用锁打开的Activity，方便退出应用的时候回收
    public static List<Activity> mList = new LinkedList<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();
        if (instance == null) {
            instance = this;
        }
    }

    // 启动一个Activity的时候记录当前Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }
    // 程序退出的方法，退出所有Activity
    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
    //当资源（内存）不够用时，启动垃圾回收机制，
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();//启动系统VM垃圾回收线程
    }

    public static MyApplication getInstance() {
        return (MyApplication) instance.getApplicationContext();
    }

    //添加蓝牙
    public void addBlutooth(BlutoothCus blutoothCus) {
        blutoothCusList.add(blutoothCus);
    }


    public static Context getContext() {
        return instance.getApplicationContext();
    }
    /**
     * 获取应用的版本号
     *
     * @return 应用版本号，默认返回1
     */
    public static int getAppVersionCode() {
        Context context = getContext();
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }
}

