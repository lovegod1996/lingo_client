package com.lovegod.newbuy.api;

import com.lovegod.newbuy.bean.BaseBean;
import com.lovegod.newbuy.bean.Order;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by ywx on 2017/7/21.
 * 订单查询的接口
 */

public interface OrderApi {
    //设缓存有效期为1天
    long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
    //查询缓存的Cache-Control设置，使用缓存
    String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置。不使用缓存
    String CACHE_CONTROL_NETWORK = "max-age=0";

    //提交订单
    @FormUrlEncoded
    @POST("order")
    Observable<BaseBean<Order>>commitOrder(@FieldMap Map<String,String>map);

    //提交某个订单的商品
    @FormUrlEncoded
    @POST("orgood")
    Observable<BaseBean<Order.OrderGoods>>commitOrderGoods(@FieldMap Map<String,String>map);

    @FormUrlEncoded
    @PUT("order/pay/{oid}")
    Observable<BaseBean<Order>>commitPayOrder(@Path("oid")long oid, @Field("paytype")String paytype);
}
