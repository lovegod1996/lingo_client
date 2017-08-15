package com.lovegod.newbuy.utils.userPreferences;

import android.content.Context;
import android.widget.Toast;

import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.Track;
import com.lovegod.newbuy.view.goods.GoodActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by ywx on 2017/8/12.
 * 进行用户信息采集相关操作的工具类
 */

public class UserPreferencesUtil {
    private static final int INFO_TRACK=0;
    private static final int ASSESS_TRACK=1;
    private static final int QUESTION_TRACK=2;
    private static final int ATTENTION_TRACK=3;
    private static final int CART_TRACK=4;
    private static final int USERASSESS_TRACK=5;
    private static final int BUY_TRACK=6;

    /**
     * 修改详情足迹
     * @param context
     * @param detail
     * @param tid
     * @param total
     */
    public static void changeInfoTrack(Context context,int detail,int tid,int total){
        NetWorks.changeInfoTrack(detail,tid,total, new BaseObserver<Track>() {
            @Override
            public void onHandleSuccess(Track track) {

            }

            @Override
            public void onHandleError(Track track) {

            }
        });
    }

    /**
     * 修改查看评价足迹
     * @param context
     * @param assess
     * @param tid
     * @param total
     */
    public static void changeAssessTrack(Context context,int assess,int tid,int total){
        NetWorks.changeAssessTrack(assess, tid, total, new BaseObserver<Track>() {
            @Override
            public void onHandleSuccess(Track track) {

            }

            @Override
            public void onHandleError(Track track) {

            }
        });
    }

    /**
     * 修改问题足迹
     * @param context
     * @param question
     * @param tid
     * @param total
     */
    public static void changeQuestionTrack(Context context,int question,int tid,int total){
        NetWorks.changeQuestionTrack(question, tid, total, new BaseObserver<Track>() {
            @Override
            public void onHandleSuccess(Track track) {

            }

            @Override
            public void onHandleError(Track track) {

            }
        });
    }

    /**
     * 修改关注足迹
     * @param context
     * @param attention
     * @param tid
     * @param total
     */
    public static void changeAttentionTrack(Context context,int attention,int tid,int total){
        NetWorks.changeAttentionTrack(attention, tid, total, new BaseObserver<Track>() {
            @Override
            public void onHandleSuccess(Track track) {

            }

            @Override
            public void onHandleError(Track track) {

            }
        });
    }

    /**
     * 修改购物车足迹
     * @param context
     * @param cart
     * @param tid
     * @param total
     */
    public static void changeCartTrack(Context context,int cart,int tid,int total){
        NetWorks.changeCartTrack(cart, tid, total, new BaseObserver<Track>() {
            @Override
            public void onHandleSuccess(Track track) {

            }

            @Override
            public void onHandleError(Track track) {

            }
        });
    }

    /**
     * 修改提交评价足迹
     * @param context
     * @param userAssess
     * @param tid
     * @param total
     */
    public static void changeUserAssessTrack(Context context,int userAssess,int tid,int total){
        NetWorks.changeUserAssessTrack(userAssess, tid, total, new BaseObserver<Track>() {
            @Override
            public void onHandleSuccess(Track track) {

            }

            @Override
            public void onHandleError(Track track) {

            }
        });
    }

    /**
     * 修改购买足迹
     * @param context
     * @param buy
     * @param tid
     * @param total
     */
    public static void changeBuyTrack(Context context,int buy,int tid,int total){
        NetWorks.changeBuyTrack(buy, tid, total, new BaseObserver<Track>() {
            @Override
            public void onHandleSuccess(Track track) {

            }

            @Override
            public void onHandleError(Track track) {

            }
        });
    }

    /**
     * 用于获取最新的足迹数据进行修改,如果为空就新增数据
     * @param context
     * @param uid  用户id
     * @param cid  商品id
     * @param type 修改的类型
     * @param newData
     */
    public static void changeTrackInfo(final Context context, final int uid, final int cid, final int type, final int newData){
            NetWorks.getOneTrack(uid, cid, new BaseObserver<Track>() {
                @Override
                public void onHandleSuccess(Track track) {
                    if(track==null){
                        addTrack(uid,cid);
                    }else {
                        switch (type) {
                            case INFO_TRACK:
                                track.setDetail(track.getDetail() + 1);
                                changeInfoTrack(context, track.getDetail(), track.getTid(), computeTrackValue(track));
                                break;
                            case ASSESS_TRACK:
                                track.setAssess(track.getAssess() + 1);
                                changeAssessTrack(context, track.getAssess(), track.getTid(), computeTrackValue(track));
                                break;
                            case QUESTION_TRACK:
                                track.setQuestion(track.getQuestion() + 1);
                                changeQuestionTrack(context, track.getQuestion(), track.getTid(), computeTrackValue(track));
                                break;
                            case ATTENTION_TRACK:
                                track.setAttention(newData);
                                changeAttentionTrack(context, track.getAttention(), track.getTid(), computeTrackValue(track));
                                break;
                            case CART_TRACK:
                                track.setCart(track.getCart() + 1);
                                changeCartTrack(context, track.getCart(), track.getTid(), computeTrackValue(track));
                                break;
                            case USERASSESS_TRACK:
                                track.setUserassess(newData);
                                changeUserAssessTrack(context, track.getUserassess(), track.getTid(), computeTrackValue(track));
                                break;
                            case BUY_TRACK:
                                track.setBuy(track.getBuy() + 1);
                                changeBuyTrack(context, track.getBuy(), track.getTid(), computeTrackValue(track));
                                break;
                        }
                    }
                }

                @Override
                public void onHandleError(Track track) {
                }
            });
        }

    /**
     * 根据足迹的各个部分计算用户对某商品的兴趣值
     * @param track
     */
    public static int computeTrackValue(Track track) {
        float total;
        //计算公式
        if (track.getUserassess()!=0) {
            total = track.getDetail() * 0.5f + track.getAssess() * 0.5f + track.getQuestion() * 0.5f + track.getAttention() * 50 + track.getCart() * 2 + track.getBuy() * 10 + (track.getUserassess() - 3) * 10;
        }else {
            total = track.getDetail() * 0.5f + track.getAssess() * 0.5f + track.getQuestion() * 0.5f + track.getAttention() * 50 + track.getCart() * 2 + track.getBuy() * 10;
        }
        return (int)total;
    }

    public static void addTrack(int uid,int cid){
        Map<String,String>map=new HashMap<>();
        map.put("uid",uid+"");
        map.put("cid",cid+"");
        map.put("detail",1+"");
        map.put("assess",0+"");
        map.put("question",0+"");
        map.put("attention",0+"");
        map.put("cart",0+"");
        map.put("buy",0+"");
        map.put("userassess",0+"");
        map.put("total",0+"");
        SimpleDateFormat formatter    =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
        map.put("cordtime",formatter.format(curDate));
        NetWorks.commitTrackInfo(map, new BaseObserver<Track>() {
            @Override
            public void onHandleSuccess(Track track) {
            }

            @Override
            public void onHandleError(Track track) {
            }
        });
    }
}
