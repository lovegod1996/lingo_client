package com.lovegod.newbuy.api;

import com.lovegod.newbuy.bean.BaseBean;
import com.lovegod.newbuy.bean.FavouriteQuest;
import com.lovegod.newbuy.bean.Quest;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.DELETE;
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

    //添加问题回复
    @FormUrlEncoded
    @POST("addReply")
    Observable<BaseBean<Quest.Reply>>commitQuestReply(@FieldMap Map<String,String>map);

    //添加问题关注
    @FormUrlEncoded
    @POST("questlook/add")
    Observable<BaseBean<FavouriteQuest>>addQuestFocus(@FieldMap Map<String,String>map);

    //取消问题关注
    @DELETE("questlook/cancel/{qaid}")
    Observable<BaseBean<FavouriteQuest>>cancelQuestFocus(@Path("qaid")int qaid);

    //根据用户id和问题id查询该问题是否被关注
    @GET("questlook/user/one/{uid}/{qid}")
    Observable<BaseBean<FavouriteQuest>>isQuestFocus(@Path("uid")int uid,@Path("qid")int qid);

    //分页查看某一用户的问题关注列表
    @GET("questlook/uid/{uid}/{page}")
    Observable<BaseBean<List<FavouriteQuest>>>getQuestFocus(@Path("uid")int uid,@Path("page")int page);

    //根据问题id查询问题
    @GET("quests/qid/{qid}")
    Observable<BaseBean<Quest>>getQuesByID(@Path("qid")int qid);
}
