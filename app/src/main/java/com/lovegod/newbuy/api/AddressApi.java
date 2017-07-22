package com.lovegod.newbuy.api;

import com.lovegod.newbuy.bean.Address;
import com.lovegod.newbuy.bean.BaseBean;
import com.lovegod.newbuy.bean.City;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.Community;
import com.lovegod.newbuy.bean.District;
import com.lovegod.newbuy.bean.LoginMessage;
import com.lovegod.newbuy.bean.Province;
import com.lovegod.newbuy.bean.Town;

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

/**
 * Created by ywx on 2017/7/18.
 */

public interface AddressApi {
    //设缓存有效期为1天
    long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
    //查询缓存的Cache-Control设置，使用缓存
    String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置。不使用缓存
    String CACHE_CONTROL_NETWORK = "max-age=0";

    //获取所有省份信息
    @GET("allprovince")
    Observable<BaseBean<List<Province>>>getAllProvince();

    //获取某省份城市信息
    @GET("allCity/{pid}")
    Observable<BaseBean<List<City>>>getCity(@Path("pid")int pid);

    //获取某城市县区信息
    @GET("allDistrict/{cid}")
    Observable<BaseBean<List<District>>>getDistrict(@Path("cid")int cid);

    //获取某县区街道信息
    @GET("allTowns/{did}")
    Observable<BaseBean<List<Town>>>getTown(@Path("did")int did);

    //获取某街道居委会信息
    @GET("allCommunity/{tid}")
    Observable<BaseBean<List<Community>>>getCommunity(@Path("tid")int tid);

    //提交地址信息
    @FormUrlEncoded
    @POST("addShip")
    Observable<BaseBean<Address>>addAddress(@FieldMap Map<String,String>map);

    //获取某个用户的所有收货地址
    @GET("Ship/user/{uid}")
    Observable<BaseBean<List<Address>>>getAddress(@Path("uid")int uid);

    //删除某条收货地址
    @DELETE("deleteShip/{said}")
    Observable<BaseBean<LoginMessage>>deleteAddress(@Path("said")int said);

    //编辑某个收货地址
    @FormUrlEncoded
    @PUT("editShip")
    Observable<BaseBean<Address>>editAddress(@FieldMap Map<String,String>map);
}
