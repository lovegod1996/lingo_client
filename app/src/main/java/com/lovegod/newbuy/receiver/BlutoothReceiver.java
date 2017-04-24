package com.lovegod.newbuy.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.Goods;
import com.lovegod.newbuy.service.BlutoothCus;
import com.lovegod.newbuy.utils.system.SpUtils;


/**
 * Created by 123 on 2017/4/4.
 */

public class BlutoothReceiver extends BroadcastReceiver {

    Goods goodss = new Goods();

    @Override
    public void onReceive(final Context context, Intent intent) {

        final BlutoothCus blutoothCus = (BlutoothCus) intent.getSerializableExtra("device");
          BlutoothCus blutoothCus1= (BlutoothCus) SpUtils.getObject(context,"ble");
        if(blutoothCus1!=null){
            if(blutoothCus1.getAddress().equals(blutoothCus.getAddress())){
                long i = (blutoothCus.getDate().getTime() - blutoothCus1.getDate().getTime()) / (24 * 60 * 60 * 100);
                if(i>3*60){
                    if (blutoothCus.getRssi() > -80) {
                        SpUtils.removeKey(context,"ble");
                        SpUtils.putObject(context,"ble",blutoothCus);
                        Log.v("推送蓝牙", blutoothCus.getAddress());
                        NetWorks.findGoodByMac(blutoothCus.getAddress(), new BaseObserver<Commodity>() {
                            @Override
                            public void onHandleSuccess(Commodity commodity) {
                                if(commodity!=null){
                                    //设置点击通知栏的动作为启动另外一个广播
                                    Intent broadcastIntent = new Intent(context, NotificationReceiver.class);
                                    Bundle bundle=new Bundle();
                                    bundle.putSerializable("commodity",commodity);
                                    broadcastIntent.putExtras(bundle);

                                    PendingIntent pendingIntent = PendingIntent.
                                            getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                                    builder.setContentTitle(commodity.getProductname())
                                            .setContentText("进入应用查看详情")
                                            .setTicker("进入应用查看详情")
                                            .setDefaults(Notification.DEFAULT_SOUND)
                                            .setContentIntent(pendingIntent)
                                            .setSmallIcon(android.R.drawable.ic_lock_idle_charging);

                                    Log.i("定位推送", "显示推送消息");
                                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                    manager.notify(2, builder.build());
                                }else{
                                    Log.v("蓝牙定位返回：","返回商品信息为空");
                                }
                            }
                        });
                    } else {
                        Log.v("蓝牙RSSI", "值达不到");
                    }
                }
            }else{
                if (blutoothCus.getRssi() > -80) {
                    SpUtils.removeKey(context,"ble");
                    SpUtils.putObject(context,"ble",blutoothCus);
                    Log.v("推送蓝牙", blutoothCus.getAddress());
                    NetWorks.findGoodByMac(blutoothCus.getAddress(), new BaseObserver<Commodity>() {
                        @Override
                        public void onHandleSuccess(Commodity commodity) {
                            if(commodity!=null){
                                //设置点击通知栏的动作为启动另外一个广播
                                Intent broadcastIntent = new Intent(context, NotificationReceiver.class);
                                Bundle bundle=new Bundle();
                                bundle.putSerializable("commodity",commodity);
                                broadcastIntent.putExtras(bundle);

                                PendingIntent pendingIntent = PendingIntent.
                                        getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                                builder.setContentTitle(commodity.getProductname())
                                        .setContentText("进入应用查看详情")
                                        .setTicker("进入应用查看详情")
                                        .setDefaults(Notification.DEFAULT_SOUND)
                                        .setContentIntent(pendingIntent)
                                        .setSmallIcon(android.R.drawable.ic_lock_idle_charging);

                                Log.i("定位推送", "显示推送消息");
                                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                manager.notify(2, builder.build());
                            }else{
                                Log.v("蓝牙定位返回：","返回商品信息为空");
                            }
                        }
                    });
                } else {
                    Log.v("蓝牙RSSI", "值达不到");
                }
            }
        }else{
            if (blutoothCus.getRssi() > -80) {
                SpUtils.putObject(context,"ble",blutoothCus);
                Log.v("推送蓝牙", blutoothCus.getAddress());
                NetWorks.findGoodByMac(blutoothCus.getAddress(), new BaseObserver<Commodity>() {
                    @Override
                    public void onHandleSuccess(Commodity commodity) {
                        if(commodity!=null){
                            //设置点击通知栏的动作为启动另外一个广播
                            Intent broadcastIntent = new Intent(context, NotificationReceiver.class);
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("commodity",commodity);
                            broadcastIntent.putExtras(bundle);

                            PendingIntent pendingIntent = PendingIntent.
                                    getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                            builder.setContentTitle(commodity.getProductname())
                                    .setContentText("进入应用查看详情")
                                    .setTicker("进入应用查看详情")
                                    .setDefaults(Notification.DEFAULT_SOUND)
                                    .setContentIntent(pendingIntent)
                                    .setSmallIcon(android.R.drawable.ic_lock_idle_charging);

                            Log.i("定位推送", "显示推送消息");
                            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            manager.notify(2, builder.build());
                        }else{
                            Log.v("蓝牙定位返回：","返回商品信息为空");
                        }
                    }
                });
            } else {
                Log.v("蓝牙RSSI", "值达不到");
            }
        }



    }
}
