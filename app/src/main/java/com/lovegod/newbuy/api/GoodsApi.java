package com.lovegod.newbuy.api;

import com.lovegod.newbuy.MainActivity;
import com.lovegod.newbuy.bean.Assess;
import com.lovegod.newbuy.bean.BaseBean;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.FavouriteGoods;
import com.lovegod.newbuy.bean.Goods;
import com.lovegod.newbuy.view.myinfo.FavouriteGoodsAdapter;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
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

    //提交评价信息
    @FormUrlEncoded
    @POST("assess")
    Observable<BaseBean<Assess>>commitAssess(@FieldMap Map<String,String> map);

    //添加关注商品
    @FormUrlEncoded
    @POST("goodslook/add")
    Observable<BaseBean<FavouriteGoods>>addFoucusGoods(@FieldMap Map<String,String>map);

    //取消关注商品
    @DELETE("goodslook/cancle/{gaid}")
    Observable<BaseBean<FavouriteGoods>>cancelFoucusGoods(@Path("gaid")int gaid);

    //根据用户Id分页查找关注商品
    @GET("goodslook/user/{uid}/{page}")
    Observable<BaseBean<List<FavouriteGoods>>>getFoucusGoods(@Path("uid")int uid,@Path("page")int page);

    //查询某件商品是否被某用户关注
    @GET("goodslook/user/one/{uid}/{cid}")
    Observable<BaseBean<FavouriteGoods>>isGoodsFoucus(@Path("uid")int uid,@Path("cid")int cid);
}
