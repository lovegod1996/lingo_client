package com.lovegod.newbuy.api;

import com.lovegod.newbuy.bean.BaseBean;

import com.lovegod.newbuy.bean.ShopCartBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservableBlockingSubscribe;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * *****************************************
 * Created by thinking on 2017/5/21.
 * 创建时间：
 * <p>
 * 描述：
 * <p/>
 * <p/>
 * *******************************************
 */

public interface CartApi {
    //设缓存有效期为1天
    long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
    //查询缓存的Cache-Control设置，使用缓存
    String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置。不使用缓存
    String CACHE_CONTROL_NETWORK = "max-age=0";

    @FormUrlEncoded
    @POST("addCart")
    Observable<BaseBean<ShopCartBean>> postAddcart(@FieldMap Map<String, Object> addmap);

    @GET("carts/{uid}")
    Observable<BaseBean<List<ShopCartBean>>> getCarts(@Path("uid") Integer uid);

    @FormUrlEncoded
    @PUT("carts/put/{id}")
    Observable<BaseBean<ShopCartBean>> putAddNumCart(@Path("id") Integer cbid, @Field("num") Integer num);

    @DELETE("carts/delete/{id}")
    Observable<BaseBean<ShopCartBean>> DeleteCart(@Path("id") Integer cbid);
}
