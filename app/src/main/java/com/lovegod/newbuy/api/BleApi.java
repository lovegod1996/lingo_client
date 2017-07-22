package com.lovegod.newbuy.api;

import com.lovegod.newbuy.bean.BaseBean;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.service.Ble;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by 123 on 2017/5/1.
 */

public interface BleApi {
    //设缓存有效期为1天
    long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
    //查询缓存的Cache-Control设置，使用缓存
    String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置。不使用缓存
    String CACHE_CONTROL_NETWORK = "max-age=0";


    @GET("/bles/mac/{mac}")
    Observable<BaseBean<List<Ble>>> getShopAllBle(@Path("mac") String mac);

    @GET("/positionpush")
    Observable<BaseBean<Commodity>> getPushCommodity(@Query("mac") String mac,@Query("x") Double x,@Query("y") Double y,@Query("uid") Integer uid);

}
