package com.lovegod.newbuy.api;

import com.lovegod.newbuy.bean.BaseBean;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.FavouriteShop;
import com.lovegod.newbuy.bean.Goods;
import com.lovegod.newbuy.bean.Shop;

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

import static android.R.attr.id;


/**
 * Created by 123 on 2017/4/14.
 */

public interface ShopApi {

    //设缓存有效期为1天
    long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
    //查询缓存的Cache-Control设置，使用缓存
    String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置。不使用缓存
    String CACHE_CONTROL_NETWORK = "max-age=0";

//    @Headers("Cache-Control: public," + CACHE_CONTROL_CACHE)
    @GET("shops/top")
    Observable<BaseBean<List<Shop>>> getAllShop();

    @GET("shops/{sid}")
    Observable<BaseBean<Shop>>getIDshop(@Path("sid") Integer sid);

    @POST("api/goodBymac")
    Observable<BaseBean<Commodity>> findGoodByMac(@Query("mac")String mac);


    //根据店铺名查询店铺详情
    @GET("shops/name")
    Observable<BaseBean<List<Shop>>>findShopByName(@Query("name")String name,@Query("page")Integer page);

    //添加店铺关注
    @FormUrlEncoded
    @POST("shoplook/add")
    Observable<BaseBean<FavouriteShop>>addFocusShop(@FieldMap Map<String,String>map);

    //查询某店是否被某用户关注
    @GET("shoplook/user/one/{uid}/{sid}")
    Observable<BaseBean<FavouriteShop>>isShopFocus(@Path("uid")int uid,@Path("sid")int sid);

    //取消关注店铺
    @DELETE("shoplook/delete/{said}")
    Observable<BaseBean<FavouriteShop>>cancelShopFocus(@Path("said")int said);

    //分页查找该用户所有关注的店铺
    @GET("shoplook/user/{uid}/{page}")
    Observable<BaseBean<List<FavouriteShop>>>getFocusShop(@Path("uid")int uid,@Path("page")int page);

    //获取附近店铺
    @GET("shops/near/{longgitude}/{latitude}/{dis}")
    Observable<BaseBean<List<Shop>>>getNearbyShop(@Path("longgitude")double longgitude,@Path("latitude")double latitude,@Path("dis")int dis);
}
