package com.lovegod.newbuy.api;

import com.lovegod.newbuy.bean.BaseBean;
import com.lovegod.newbuy.bean.LoginMessage;
import com.lovegod.newbuy.bean.User;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by ywx on 2017/7/11.
 */

public interface ChangeApi {
    //设缓存有效期为1天
    long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
    //查询缓存的Cache-Control设置，使用缓存
    String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置。不使用缓存
    String CACHE_CONTROL_NETWORK = "max-age=0";

    //修改密码的接口
    @FormUrlEncoded
    @PUT("user/updatePass/{id}")
    Observable<BaseBean<LoginMessage>>changePasswd(@Path("id")Integer id, @Field("id")Integer paramsId, @Field("password")String password);

    //修改用户名的接口
    @FormUrlEncoded
    @PUT("user/username/{id}")
    Observable<BaseBean<LoginMessage>>changeUserName(@Path("id")Integer id,@Field("username")String username);

    //修改用户手机号的接口
    @PUT("user/updatePhone/{id}/{phone}")
    Observable<BaseBean<User>>changPhoneNumber(@Path("id")Integer id,@Path("phone")String phone);
}
