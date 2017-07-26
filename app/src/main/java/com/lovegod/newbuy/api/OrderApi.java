package com.lovegod.newbuy.api;

import com.lovegod.newbuy.bean.BaseBean;
import com.lovegod.newbuy.bean.Order;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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

    //添加某个订单的支付信息
    @FormUrlEncoded
    @PUT("order/pay/{oid}")
    Observable<BaseBean<Order>>commitPayOrder(@Path("oid")long oid, @Field("paytype")String paytype);

    //获取所有订单信息
    @GET("orders/uid/{uid}")
    Observable<BaseBean<List<Order>>>getAllOrder(@Path("uid")int uid);

    //根据订单id查找该订单的商品
    @GET("orgood/oid/{oid}")
    Observable<BaseBean<List<Order.OrderGoods>>>getOrderGoods(@Path("oid")long oid);

    //查找未付款订单
    @GET("orders/nopay/{uid}")
    Observable<BaseBean<List<Order>>>getForThePaymentOrder(@Path("uid")int uid);

    //查看未发货订单
    @GET("orders/noship/{uid}")
    Observable<BaseBean<List<Order>>>getToSendGoodsOrder(@Path("uid")int uid);

    //查询未收货订单
    @GET("orders/noreceipt/{uid}")
    Observable<BaseBean<List<Order>>>getForTheGoodsOrder(@Path("uid")int uid);

    @GET("orders/receipt/{uid}")
    Observable<BaseBean<List<Order>>>getFinishOrder(@Path("uid")int uid);

    //分页查找所有订单
    @GET("orders/page/{uid}/{page}")
    Observable<BaseBean<List<Order>>>getAllOrderByPage(@Path("uid")int uid,@Path("page")int page);

    //根据订单状态分页查找订单
    @GET("orders/page/{uid}/{statue}/{page}")
    Observable<BaseBean<List<Order>>>getOrderByStatue(@Path("uid")int uid,@Path("statue")int statue,@Path("page")int page);

    //根据订单id取消订单
    @PUT("order/cancel/{oid}")
    Observable<BaseBean<Order>>cancelOrder(@Path("oid")long oid);

    @PUT("order/deal/{oid}")
    Observable<BaseBean<Order>>confirmTheGoods(@Path("oid")long oid);
}
