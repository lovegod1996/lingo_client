package com.lovegod.newbuy.api;

import com.lovegod.newbuy.bean.BaseBean;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.ShopCartBean;
import com.lovegod.newbuy.bean.SortFrist;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * *****************************************
 * Created by thinking on 2017/5/24.
 * 创建时间：
 * <p>
 * 描述：
 * <p/>
 * <p/>
 * *******************************************
 */

public interface SortApi {
        //设缓存有效期为1天
        long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
        //查询缓存的Cache-Control设置，使用缓存
        String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
        //查询网络的Cache-Control设置。不使用缓存
        String CACHE_CONTROL_NETWORK = "max-age=0";


        @GET("categorys")
        Observable<BaseBean<List<SortFrist>>> getSortFirst();

        @GET("goods/category/{cgid}")
        Observable<BaseBean<List<Commodity>>> getSecondGoods(@Path("cgid")Integer cgid);

    }
