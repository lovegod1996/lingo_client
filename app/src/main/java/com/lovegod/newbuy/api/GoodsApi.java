package com.lovegod.newbuy.api;

import com.lovegod.newbuy.bean.Assess;
import com.lovegod.newbuy.bean.BaseBean;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.Goods;

import java.util.List;

import io.reactivex.Observable;

import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


/**
 * Created by lovegod on 2017/5/11.
 */

public interface GoodsApi {

    //设缓存有效期为1天
    long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
    //查询缓存的Cache-Control设置，使用缓存
    String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置不使用缓存
    String CACHE_CONTROL_NETWORK = "max-age=0";

    @GET("goods/{id}")
    Observable<BaseBean<Commodity>> findCommotity(@Path("id") Integer id);

    @GET("goods/shop/{sid}")
    Observable<BaseBean<List<Commodity>>>getIDshopgoods(@Path("sid")Integer sid);

   @GET("assess/{cid}")
    Observable<BaseBean<List<Assess>>> getAllAssess(@Path("cid")Integer cid);


    //根据名字查询商品
    @GET("goods/name")
    Observable<BaseBean<List<Commodity>>>getNameGoods(@Query("name")String name,@Query("page")Integer page);
}
