package com.lovegod.newbuy.api;

import com.lovegod.newbuy.bean.Assess;
import com.lovegod.newbuy.bean.BaseBean;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.FavouriteGoods;
import com.lovegod.newbuy.bean.Track;
import com.lovegod.newbuy.bean.Trial;

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
    Observable<BaseBean<List<Commodity>>>getIDshopgoods(@Path("sid")Integer sid);

    @GET("assess/{cid}")
    Observable<BaseBean<List<Assess>>> getAllAssess(@Path("cid")Integer cid);

    @GET("assess/count/{cid}")
    Observable<BaseBean<Integer>>getAssessCount(@Path("cid")int cid);

    @GET("assess/new/{cid}")
    Observable<BaseBean<Assess>>getNewAssess(@Path("cid")int cid);

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

    //添加试用商品请求
    @FormUrlEncoded
    @POST("apply/experience/add")
    Observable<BaseBean<Trial>>addTrialGoods(@FieldMap Map<String,String>map);

    //获取申请试用的列表
    @GET("apply/experience/uid/{uid}")
    Observable<BaseBean<List<Trial>>>getTrialGoods(@Path("uid")int uid);

    //查看某用户是否申请了我商品
    @GET("apply/experience/uid/{uid}/cid/{cid}")
    Observable<BaseBean<Trial>>isTrial(@Path("uid")int uid,@Path("cid")int cid);

    //提交足迹数据
    @FormUrlEncoded
    @POST("track/add")
    Observable<BaseBean<Track>>commitTrackInfo(@FieldMap Map<String,String>map);

    //根据用户名和商品id查询足迹
    @GET("track/uid/{uid}/cid/{cid}")
    Observable<BaseBean<Track>>getOneTrack(@Path("uid")int uid,@Path("cid")int cid);

    //根据用户id分页查询足迹
    @GET("track/uid/{uid}/page/{page}")
    Observable<BaseBean<List<Track>>>getAllTrack(@Path("uid")int uid,@Path("page")int page);

    //修改详情足迹数据
    @PUT("track/update/detail/{detail}/{tid}/{total}")
    Observable<BaseBean<Track>>changeInfoTrack(@Path("detail")int detail,@Path("tid")int tid,@Path("total")int total);

    //修改评价足迹数据
    @PUT("track/update/assess/{assess}/{tid}/{total}")
    Observable<BaseBean<Track>>changeAssessTrack(@Path("assess")int assess,@Path("tid")int tid,@Path("total")int total);

    //修改问题足迹数据
    @PUT("track/update/question/{question}/{tid}/{total}")
    Observable<BaseBean<Track>>changeQuestionTrack(@Path("question")int question,@Path("tid")int tid,@Path("total")int total);

    //修改关注足迹数据
    @PUT("track/update/attention/{attention}/{tid}/{total}")
    Observable<BaseBean<Track>>changeAttentionTrack(@Path("attention")int attention,@Path("tid")int tid,@Path("total")int total);

    //修改购物车足迹数据
    @PUT("track/update/cart/{cart}/{tid}/{total}")
    Observable<BaseBean<Track>>changeCartTrack(@Path("cart")int cart,@Path("tid")int tid,@Path("total")int total);

    //修改提交评价足迹数据
    @PUT("track/update/userassess/{userassess}/{tid}/{total}")
    Observable<BaseBean<Track>>changeUserAssessTrack(@Path("userassess")int userassess,@Path("tid")int tid,@Path("total")int total);

    //修改购买足迹数据
    @PUT("track/update/buy/{buy}/{tid}/{total}")
    Observable<BaseBean<Track>>changeBuyTrack(@Path("buy")int buy,@Path("tid")int tid,@Path("total")int total);

    //获取推荐商品
    @GET("track/index/user/{uid}")
    Observable<BaseBean<List<Commodity>>>getRecommendGoods(@Path("uid")int uid);
}
