package com.hyphenate.easeui;


import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


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
    Observable<BaseBean<List<Commodity>>>getIDshopgoods(@Path("sid") Integer sid);

    @GET("shops/{sid}")
    Observable<BaseBean<Shop>>getIDshop(@Path("sid") Integer sid);
}
