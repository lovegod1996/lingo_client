package com.lovegod.newbuy.api;

import com.lovegod.newbuy.bean.BaseBean;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.Shop;
import com.lovegod.newbuy.service.Ble;
import com.lovegod.newbuy.utils.retrofitRxjava.RetrofitUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by 123 on 2017/4/14.
 */

public class NetWorks extends RetrofitUtils {

    protected static final ShopApi shopApi = getRetrofit().create(ShopApi.class);

    protected static final BleApi bleApi=getRetrofit().create(BleApi.class);

    protected  static final GoodsApi goodsApi=getRetrofit().create(GoodsApi.class);

    public static void getAllshop(BaseObserver<List<Shop>> shopObservable) {
        setSubscribe(shopApi.getAllShop(), shopObservable);
        System.out.println();
    }

    public static void findGoodByMac(String mac,BaseObserver<Commodity> shopObservable) {
        setSubscribe(shopApi.findGoodByMac(mac), shopObservable);
    }

    public static void findShopAllBle(String mac, BaseObserver<List<Ble>> bleObervable){
        setSubscribe(bleApi.getShopAllBle(mac),bleObervable);
    }

    public static void getPushCommodity(String mac,Double x,Double y,Integer uid, BaseObserver<Commodity> comObervable){
        setSubscribe(bleApi.getPushCommodity(mac,x,y,uid),comObervable);
    }

    public static void findOntCommodity(Integer id,BaseObserver<Commodity> commodityBaseObserver){
        setSubscribe(goodsApi.findCommotity(id),commodityBaseObserver);
    }

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
