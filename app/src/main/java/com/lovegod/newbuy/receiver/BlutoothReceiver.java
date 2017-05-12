package com.lovegod.newbuy.receiver;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.WindowManager;

import com.lovegod.newbuy.MyApplication;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.Goods;
import com.lovegod.newbuy.service.BlutoothCus;
import com.lovegod.newbuy.utils.system.Point;
import com.lovegod.newbuy.utils.system.SpUtils;
import com.lovegod.newbuy.view.goods.GoodActivity;


/**
 * Created by 123 on 2017/4/4.
 */

public class BlutoothReceiver extends BroadcastReceiver {

    Goods goodss = new Goods();

    @Override
    public void onReceive(final Context context, final Intent intent) {

        final BlutoothCus blutoothCus = (BlutoothCus) intent.getSerializableExtra("device");
        BlutoothCus blutoothCus1 = (BlutoothCus) SpUtils.getObject(context, "ble");


        Point point = (Point) intent.getSerializableExtra("point");

        if (blutoothCus1 != null) {
            if (blutoothCus1.getAddress().equals(blutoothCus.getAddress())) {
                long i = (blutoothCus.getDate().getTime() - blutoothCus1.getDate().getTime()) / (24 * 60 * 60 * 100);
                if (i > 3 * 60) {   //设置三分钟后才可继续推送
                    if (blutoothCus.getRssi() > -80) {
                        SpUtils.removeKey(context, "ble");
                        SpUtils.putObject(context, "ble", blutoothCus);
                        Log.v("推送蓝牙", blutoothCus.getAddress());
                        Log.v("推送坐标", point.getX() + " " + point.getY());

                        NetWorks.getPushCommodity(blutoothCus.getAddress(), point.getX(), point.getY(),1, new BaseObserver<Commodity>() {
                            @Override
                            public void onHandleSuccess(final Commodity commodity) {
                                //设置点击通知栏的动作为启动另外一个广播
                                if (commodity != null) {
                                    int size = MyApplication.mList.size();
                                    if (size > 0) {
                                        //显示弹窗
                                        // 获得广播发送的数据
                                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                                        dialogBuilder.setTitle("猜您喜欢");
                                        dialogBuilder.setMessage(commodity.getProductname());
                                        dialogBuilder.setCancelable(false);
                                        dialogBuilder.setPositiveButton("查看", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Bundle bundle = new Bundle();
                                                bundle.putSerializable("commodity", commodity);
                                                Intent intent1 = new Intent(context, GoodActivity.class);
                                                intent.putExtras(bundle);
                                                context.startActivity(intent1);
                                            }
                                        });
                                        dialogBuilder.setNegativeButton("残忍拒绝", null);
                                        AlertDialog alertDialog = dialogBuilder.create();
                                        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                                        alertDialog.show();
                                    } else {
                                        //显示通知栏
                                        Intent broadcastIntent = new Intent(context, NotificationReceiver.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("commodity", commodity);
                                        broadcastIntent.putExtras(bundle);
                                        PendingIntent pendingIntent = PendingIntent.
                                                getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                                        builder.setContentTitle(commodity.getProductname())
                                                .setContentText("进入应用查看详情")
                                                .setTicker("进入应用查看详情")
                                                .setAutoCancel(true)
                                                .setDefaults(Notification.DEFAULT_ALL)
                                                .setContentIntent(pendingIntent)
                                                .setSmallIcon(android.R.drawable.ic_lock_idle_charging);
                                        Log.i("定位推送", "显示推送消息");
                                        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                        manager.notify(2, builder.build());
                                    }
                                }
                            }
                        });
                    } else {
                        Log.v("蓝牙RSSI", "值达不到");
                    }
                }
            } else {
                if (blutoothCus.getRssi() > -80) {
                    SpUtils.removeKey(context, "ble");
                    SpUtils.putObject(context, "ble", blutoothCus);
                    Log.v("推送蓝牙", blutoothCus.getAddress());
                    Log.v("推送坐标", point.getX() + " " + point.getY());

                    NetWorks.getPushCommodity(blutoothCus.getAddress(), point.getX(), point.getY(),1, new BaseObserver<Commodity>() {
                        @Override
                        public void onHandleSuccess(final Commodity commodity) {
                            //设置点击通知栏的动作为启动另外一个广播
                            if (commodity != null) {
                                int size = MyApplication.mList.size();
                                if (size > 0) {
                                    //显示弹窗
                                    // 获得广播发送的数据
                                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                                    dialogBuilder.setTitle("猜您喜欢");
                                    dialogBuilder.setMessage(commodity.getProductname());
                                    dialogBuilder.setCancelable(false);
                                    dialogBuilder.setPositiveButton("查看", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("commodity", commodity);
                                            Intent intent1 = new Intent(context, GoodActivity.class);
                                            intent.putExtras(bundle);
                                            context.startActivity(intent1);
                                        }
                                    });
                                    dialogBuilder.setNegativeButton("残忍拒绝", null);
                                    AlertDialog alertDialog = dialogBuilder.create();
                                    alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                                    alertDialog.show();
                                } else {
                                    //显示通知栏
                                    Intent broadcastIntent = new Intent(context, NotificationReceiver.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("commodity", commodity);
                                    broadcastIntent.putExtras(bundle);
                                    PendingIntent pendingIntent = PendingIntent.
                                            getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                                    builder.setContentTitle(commodity.getProductname())
                                            .setContentText("进入应用查看详情")
                                            .setTicker("进入应用查看详情")
                                            .setAutoCancel(true)
                                            .setDefaults(Notification.DEFAULT_ALL)
                                            .setContentIntent(pendingIntent)
                                            .setSmallIcon(android.R.drawable.ic_lock_idle_charging);
                                    Log.i("定位推送", "显示推送消息");
                                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                    manager.notify(2, builder.build());
                                }
                            }
                        }
                    });
                } else {
                    Log.v("蓝牙RSSI", "值达不到");
                }
            }
        } else {
            if (blutoothCus.getRssi() > -80) {
                SpUtils.putObject(context, "ble", blutoothCus);
                Log.v("推送蓝牙", blutoothCus.getAddress());
                Log.v("推送坐标", point.getX() + " " + point.getY());

                NetWorks.getPushCommodity(blutoothCus.getAddress(), point.getX(), point.getY(),1, new BaseObserver<Commodity>() {
                    @Override
                    public void onHandleSuccess(final Commodity commodity) {
                        //设置点击通知栏的动作为启动另外一个广播
                        if (commodity != null) {
                            int size = MyApplication.mList.size();
                            if (size > 0) {
                                //显示弹窗
                                // 获得广播发送的数据
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                                dialogBuilder.setTitle("猜您喜欢");
                                dialogBuilder.setMessage(commodity.getProductname());
                                dialogBuilder.setCancelable(false);
                                dialogBuilder.setPositiveButton("查看", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("commodity", commodity);
                                        Intent intent1 = new Intent(context, GoodActivity.class);
                                        intent.putExtras(bundle);
                                        context.startActivity(intent1);
                                    }
                                });
                                dialogBuilder.setNegativeButton("残忍拒绝", null);
                                AlertDialog alertDialog = dialogBuilder.create();
                                alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                                alertDialog.show();
                            } else {
                                //显示通知栏
                                Intent broadcastIntent = new Intent(context, NotificationReceiver.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("commodity", commodity);
                                broadcastIntent.putExtras(bundle);
                                PendingIntent pendingIntent = PendingIntent.
                                        getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                                builder.setContentTitle(commodity.getProductname())
                                        .setContentText("进入应用查看详情")
                                        .setTicker("进入应用查看详情")
                                        .setAutoCancel(true)
                                        .setDefaults(Notification.DEFAULT_ALL)
                                        .setContentIntent(pendingIntent)
                                        .setSmallIcon(android.R.drawable.ic_lock_idle_charging);
                                Log.i("定位推送", "显示推送消息");
                                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                manager.notify(2, builder.build());
                            }
                        }
                    }
                });
            } else {
                Log.v("蓝牙RSSI", "值达不到");
            }
        }


    }
}
