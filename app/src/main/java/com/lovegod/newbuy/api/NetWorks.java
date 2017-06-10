package com.lovegod.newbuy.api;

import com.lovegod.newbuy.bean.BaseBean;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.Goods;
import com.lovegod.newbuy.bean.Shop;
import com.lovegod.newbuy.bean.ShopCartBean;
import com.lovegod.newbuy.bean.SortFrist;
import com.lovegod.newbuy.service.Ble;
import com.lovegod.newbuy.utils.retrofitRxjava.RetrofitUtils;
import com.lovegod.newbuy.view.BaseActivity;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by 123 on 2017/4/14.
 */

public class NetWorks extends RetrofitUtils {

    protected static final ShopApi shopApi = getRetrofit().create(ShopApi.class);

    protected static final CartApi cartApi = getRetrofit().create(CartApi.class);

    protected static final SortApi sortApi = getRetrofit().create(SortApi.class);

    protected static final BleApi bleApi = getRetrofit().create(BleApi.class);

    protected static final GoodsApi goodsApi = getRetrofit().create(GoodsApi.class);

    public static void getAllshop(BaseObserver<List<Shop>> shopObservable) {
        setSubscribe(shopApi.getAllShop(), shopObservable);
        System.out.println();
    }

    public static void getIDshop(int sid, BaseObserver<Shop> shopObservable) {
        setSubscribe(shopApi.getIDshop(sid), shopObservable);
    }

    public static void getIDshopgoods(int sid, BaseObserver<List<Commodity>> goodsBaseObserver) {
        setSubscribe(goodsApi.getIDshopgoods(sid), goodsBaseObserver);
    }


    public static void postAddcart(Map<String, Object> addmap, BaseObserver<ShopCartBean> shopCartBeanBaseObserver) {
        setSubscribe(cartApi.postAddcart(addmap), shopCartBeanBaseObserver);
    }

    public static void getCarts(int uid, BaseObserver<List<ShopCartBean>> cartObserver) {
        setSubscribe(cartApi.getCarts(uid), cartObserver);
    }

    public static void putAddNumCart(int cbid, int num, BaseObserver<ShopCartBean> baseObserver) {
        setSubscribe(cartApi.putAddNumCart(cbid, num), baseObserver);
    }

    public static void DeleteCart(int cbid, BaseObserver<ShopCartBean> deletebaseObserver) {
        setSubscribe(cartApi.DeleteCart(cbid), deletebaseObserver);
    }



    public static void getSortFirst(BaseObserver<List<SortFrist>> sortObservable) {
        setSubscribe(sortApi.getSortFirst(), sortObservable);
    }

    public static void getSecondGoods(int cgid, BaseObserver<List<Commodity>> sortgoodObservable) {
        setSubscribe(sortApi.getSecondGoods(cgid), sortgoodObservable);
    }


    public static void findGoodByMac(String mac, BaseObserver<Commodity> shopObservable) {
        setSubscribe(shopApi.findGoodByMac(mac), shopObservable);
    }

    public static void findShopAllBle(String mac, BaseObserver<List<Ble>> bleObervable) {
        setSubscribe(bleApi.getShopAllBle(mac), bleObervable);
    }

    public static void getPushCommodity(String mac, Double x, Double y, Integer uid, BaseObserver<Commodity> comObervable) {
        setSubscribe(bleApi.getPushCommodity(mac, x, y, uid), comObervable);
    }

    public static void findCommodity(Integer id, BaseObserver<Commodity> commodityBaseObserver) {
        setSubscribe(goodsApi.findCommotity(id), commodityBaseObserver);
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
