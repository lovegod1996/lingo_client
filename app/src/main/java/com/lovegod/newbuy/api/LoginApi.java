package com.lovegod.newbuy.api;


import com.lovegod.newbuy.bean.BaseBean;
import com.lovegod.newbuy.bean.User;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2017/7/10.
 */

public interface LoginApi {
    //设缓存有效期为1天
    long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
    //查询缓存的Cache-Control设置，使用缓存
    String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置不使用缓存
    String CACHE_CONTROL_NETWORK = "max-age=0";
    //通过手机号查询用户的接口
    @GET("user/{phone}")
    Observable<BaseBean<User>>getPhoneInfo(@Path("phone")String phone);

    //通过用户名查询的接口
    @GET("user/username/{username}")
    Observable<BaseBean<User>> getUserNameInfo(@Path("username")String username);

    //通过用户数据库id查询的接口
    @GET("user/id/{id}")
    Observable<BaseBean<User>>getIdInfo(@Path("id")Integer id);

    //提交注册信息的接口
    @FormUrlEncoded
    @POST("user/user_Regist")
    Observable<BaseBean<User>>commitLoginInfo(@FieldMap Map<String,String>map);
}
