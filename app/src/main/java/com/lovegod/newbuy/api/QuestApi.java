package com.lovegod.newbuy.api;

import com.lovegod.newbuy.bean.BaseBean;
import com.lovegod.newbuy.bean.Quest;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by ywx on 2017/7/31.
 */

public interface QuestApi {
    //设缓存有效期为1天
    long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
    //查询缓存的Cache-Control设置，使用缓存
    String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置。不使用缓存
    String CACHE_CONTROL_NETWORK = "max-age=0";

    //添加问题
    @FormUrlEncoded
    @POST("addquest")
    Observable<BaseBean<Quest>>commitQuest(@FieldMap Map<String,String> map);

    @GET("quests/{cid}")
    Observable<BaseBean<List<Quest>>>queryQuest(@Path("cid")int cid);

    @FormUrlEncoded
    @POST("addReply")
    Observable<BaseBean<Quest.Reply>>commitQuestReply(@FieldMap Map<String,String>map);
}
