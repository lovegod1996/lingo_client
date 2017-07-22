package com.lovegod.newbuy.commen;


import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.bean.BaseBean;
import com.lovegod.newbuy.bean.Goods;
import com.lovegod.newbuy.bean.Shop;
import com.lovegod.newbuy.bean.Shopcate;
import com.lovegod.newbuy.utils.retrofitRxjava.RetrofitUtils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * 类名称：NetWorks
 * 创建人：wangliteng
 * 创建时间：2016-5-18 14:57:11
 * 类描述：网络请求的操作类
 */
public class NetWorks extends RetrofitUtils {

    protected static final NetService service = getRetrofit().create(NetService.class);

    //设缓存有效期为1天
    protected static final long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
    //查询缓存的Cache-Control设置，使用缓存
    protected static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置。不使用缓存
    protected static final String CACHE_CONTROL_NETWORK = "max-age=0";

    private interface NetService {

        @GET("api/allShop")
        Observable<BaseBean<Shop>> getAllShop();

        @POST("api/goodes")
        Observable<Goods> goodes(@Query("sid") Integer sid, @Query("cate") String cate);

        @POST("api/shopcate")
        Observable<Shopcate> shopcate(@Query("sid") Integer sid);

        @POST("api/goodBymac")
        Observable<Goods> goodmac(@Query("mac") String mac);

        @POST("api/goodebyname")
        Observable<Goods> goodebyname(@Query("sid") Integer sid, @Query("name") String name);
//        //POST请求
//        @FormUrlEncoded
//        @POST("bjws/app.user/login")
//        Observable<Verification> getVerfcationCodePost(@Field("tel") String tel, @Field("password") String pass);
//
//        //POST请求
//        @FormUrlEncoded
//        @POST("bjws/app.user/login")
//        Observable<Verification> getVerfcationCodePostMap(@FieldMap Map<String, String> map);
//
//        //GET请求
//        @GET("bjws/app.user/login")
//        Observable<Verification> getVerfcationGet(@Query("tel") String tel, @Query("password") String pass);
//
//
//        //GET请求，设置缓存
//        @Headers("Cache-Control: public," + CACHE_CONTROL_CACHE)
//        @GET("bjws/app.user/login")
//        Observable<Verification> getVerfcationGetCache(@Query("tel") String tel, @Query("password") String pass);
//
//
//        @Headers("Cache-Control: public," + CACHE_CONTROL_NETWORK)
//        @GET("bjws/app.menu/getMenu")
//        Observable<MenuBean> getMainMenu();
    }

    public static void getAllshop(BaseObserver<Shop> shopObservable) {
        setSubscribe(service.getAllShop(), shopObservable);
    }

//    public static void getgoodes(Integer sid, String cate, BaseObserver<Goods> observer)
//    {
//        setSubscribe(service.goodes(sid,cate), observer);
//    }
//
//    public static void getshopcate(Integer sid,BaseObserver<Shopcate> observer){
//        setSubscribe(service.shopcate(sid), observer);
//    }
//
//    public static void getgoodebyname(Integer sid, String name, BaseObserver<Goods> observer){
//        setSubscribe(service.goodebyname(sid,name), observer);
//    }
//
//    public static void getgoodmac(String mC,BaseObserver<Goods> observer){
//        setSubscribe(service.goodmac(mC), observer);
//    }

//    //POST请求
//    public static void verfacationCodePost(String tel, String pass, Observer<Verification> observer) {
//        setSubscribe(service.getVerfcationCodePost(tel, pass), observer);
//    }
//
//
//    //POST请求参数以map传入
//    public static void verfacationCodePostMap(Map<String, String> map, Observer<Verification> observer) {
//        setSubscribe(service.getVerfcationCodePostMap(map), observer);
//    }
//
//    //Get请求设置缓存
//    public static void verfacationCodeGetCache(String tel, String pass, Observer<Verification> observer) {
//        setSubscribe(service.getVerfcationGetCache(tel, pass), observer);
//    }
//
//    //Get请求
//    public static void verfacationCodeGet(String tel, String pass, Observer<Verification> observer) {
//        setSubscribe(service.getVerfcationGet(tel, pass), observer);
//    }
//
//    //Get请求
//    public static void verfacationCodeGetsub(String tel, String pass, Observer<Verification> observer) {
//        setSubscribe(service.getVerfcationGet(tel, pass), observer);
//    }
//
//    //Get请求
//    public static void Getcache(Observer<MenuBean> observer) {
//        setSubscribe(service.getMainMenu(), observer);
//    }

    /**
     * 插入观察者
     *
     * @param observable
     * @param observer
     * @param <T>
     */
    public static <T> void setSubscribe(Observable<BaseBean<T>> observable, BaseObserver<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())//子线程访问网络
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(observer);
    }


}
