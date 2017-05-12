package com.lovegod.newbuy.service;


import android.annotation.TargetApi;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.receiver.BlutoothReceiver;
import com.lovegod.newbuy.utils.system.ComputeUtil;
import com.lovegod.newbuy.utils.system.Point;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.lovegod.newbuy.MyApplication.blutoothCusList;

/**
 * Created by 123 on 2017/4/2.
 * 自定义蓝牙服务
 * 距离单位：米
 */

public class BluetoothService extends Service {

    private BluetoothManager bluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;

    private boolean b = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onCreate() {
        super.onCreate();
        blutoothCusList.clear();
        // 初始化 Bluetooth adapter, 通过蓝牙管理器得到一个参考蓝牙适配器(API必须在以上android4.3或以上和版本)
        if (bluetoothManager == null)
            bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager == null) {
            Log.e(TAG, "Unable to initialize BluetoothManager.");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
        }

//        mBluetoothAdapter.stopLeScan(mLeScanCallback);
        mBluetoothAdapter.startLeScan(mLeScanCallback);
    }

    private BlutoothCus getHightDevice(List<BlutoothCus> blutoothCusList) {
        BlutoothCus blutoothCus = new BlutoothCus();
        if (blutoothCusList == null || blutoothCusList.size() == 0) {
            return null;
        }
        for (int i = 0; i < blutoothCusList.size(); i++) {
            if (i == 0) {
                blutoothCus = blutoothCusList.get(i);
            } else {
                int longtime = calLastedTime(blutoothCusList.get(i).getDate(), new Date());
                if (longtime < 2) {
                    if (blutoothCus.getRssi() < blutoothCusList.get(i).getRssi()) {
                        blutoothCus = blutoothCusList.get(i);
                    }
                }
            }
        }
        return blutoothCus;
    }

    private BlutoothCus getLowDevice(BlutoothCus blutoothCus, List<BlutoothCus> blutoothCusList) {
        BlutoothCus blutoothCus1 = new BlutoothCus();
        blutoothCus1 = blutoothCus;
        if (blutoothCusList == null || blutoothCusList.size() == 0) {
            return null;
        }
        for (int i = 0; i < blutoothCusList.size(); i++) {
            if (blutoothCusList.get(i).getAddress().equals(blutoothCus1.getAddress())) {
                if (blutoothCus1.getDate().getTime() > blutoothCusList.get(i).getDate().getTime()) {
                    blutoothCus1 = blutoothCusList.get(i);
                }
            }
        }
        return blutoothCus1;
    }

    //获取两个时间的时间差 秒
    public static int calLastedTime(Date startDate, Date endDate) {
        long a = endDate.getTime();
        long b = startDate.getTime();
        int c = (int) ((a - b) / 1000);
        return c;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (blutoothCusList.size() > 0) {

            final Iterator<BlutoothCus> blutoothCusIterator = blutoothCusList.iterator();

            //查询最新的位置信息
            NetWorks.findShopAllBle(blutoothCusList.get(blutoothCusList.size() - 1).getAddress(), new BaseObserver<List<Ble>>() {
                @Override
                public void onHandleSuccess(List<Ble> bles) {
                    if (bles.size() > 0) {
                        //修改list元素数据
                        while (blutoothCusIterator.hasNext()) {
                            BlutoothCus blutoothCus = blutoothCusIterator.next();
                            boolean rem = true;
                            for (int i = 0; i < bles.size(); i++) {
                                if (blutoothCus.getAddress().equals(bles.get(i).getMacaddress())) {
                                    rem = false;
                                }
                            }
                            if (rem) {
                                blutoothCusIterator.remove();
                            } else {
                                int time = calLastedTime(blutoothCus.getDate(), new Date());
                                if (time > 100) {    //秒
                                    blutoothCusIterator.remove();
                                }
                            }
                        }
                        //修改返回坐标值
                        for (BlutoothCus blutoothCus : blutoothCusList) {
                            for (Ble ble : bles) {
                                if (blutoothCus.getAddress().equals(ble.getMacaddress())) {
                                    blutoothCus.setX(ble.getX());
                                    blutoothCus.setY(ble.getY());
                                }
                            }
                        }
                    }
                }
            });

            Collections.reverse(blutoothCusList);//倒序


            BlutoothCus blutoothDevi = getHightDevice(blutoothCusList);
            BlutoothCus lowDevice = getLowDevice(blutoothDevi, blutoothCusList);
            int longtime = 0;
            try {
                longtime = calLastedTime(lowDevice.getDate(), blutoothDevi.getDate());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (longtime > 20) {  //秒
                Point a=new Point();
                Point b=new Point();
                Point c=new Point();

                a.setX(blutoothCusList.get(0).getX());
                a.setY(blutoothCusList.get(0).getY());
                b.setX(blutoothCusList.get(1).getX());
                b.setY(blutoothCusList.get(1).getY());
                c.setX(blutoothCusList.get(2).getX());
                c.setY(blutoothCusList.get(2).getY());

                Point tPoint = ComputeUtil.getTPoint(a, b, c, blutoothCusList.get(0).getDistance(), blutoothCusList.get(1).getDistance(), blutoothCusList.get(2).getDistance());

                Intent intentRec = new Intent(this, BlutoothReceiver.class);
                intentRec.putExtra("device", blutoothDevi);
                intentRec.putExtra("point", tPoint);
                sendBroadcast(intentRec);
//                AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
//                //读者可以修改此处的Minutes从而改变提醒间隔时间
//                //此处是设置每隔55分钟启动一次
//                //这是55分钟的毫秒数
//                int Minutes = 10 * 1000;
//                //SystemClock.elapsedRealtime()表示1970年1月1日0点至今所经历的时间
//                long triggerAtTime = SystemClock.elapsedRealtime() + Minutes;
//                //此处设置开启AlarmReceiver这个Service
////            Intent i = new Intent(this, AlarmReceiver.class);
//                PendingIntent pi = PendingIntent.getBroadcast(this, 0, intentRec, 0);
//
//                //ELAPSED_REALTIME_WAKEUP表示让定时任务的出发时间从系统开机算起，并且会唤醒CPU。
//                manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
            }
        } else {
            Log.v("重新开启扫描", "蓝牙扫描");
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        }
//        try {
//            Thread.sleep(5000);
//        Log.v("休眠：","5秒");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        mBluetoothAdapter.stopLeScan(mLeScanCallback);


        return super.onStartCommand(intent, flags, startId);
    }


    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {


        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {

            BlutoothCus blutoothcus = new BlutoothCus();
            blutoothcus.setAddress(device.getAddress());
            blutoothcus.setBondState(device.getBondState());
            blutoothcus.setName(device.getName());
            blutoothcus.setType(device.getType());
            try {
                blutoothcus.setDistance(getDistence(rssi));
                Log.v("扫描设备：", device.toString() + " " + rssi+"距离： "+blutoothcus.getDistance());
            } catch (Exception e) {
                e.printStackTrace();
                Log.v("扫描设备：", device.toString() + " " + rssi);
            }
            blutoothcus.setRssi(rssi);
            blutoothcus.setDate(new Date());

            blutoothCusList.add(blutoothcus);

        }
    };

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onDestroy() {
        super.onDestroy();
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
    }


    /**
     * 根据阈值计算距离
     *
     * @param rssi
     * @return 米距离
     * @throws Exception
     */
    public Double getDistence(int rssi) throws Exception {
//    uint8 A = 59;
//    float n = 3.0;
//
//    int iRssi = abs(rssi);
//    float power = (iRssi-A)/(10*n);
//    return pow(10, power);
        int A = 59;  //距离一米时的阈值
        float n = 3;  //n：传播因子，与温度、湿度等环境相关。

        int abs = Math.abs(rssi);
        float v = (abs - A) / (10 * n);
        return Math.pow(10, v);

    }

}
