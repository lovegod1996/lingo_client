package com.lovegod.newbuy.api;


import com.lovegod.newbuy.bean.Address;
import com.lovegod.newbuy.bean.Assess;
import com.lovegod.newbuy.bean.BaseBean;
import com.lovegod.newbuy.bean.City;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.Community;
import com.lovegod.newbuy.bean.District;
import com.lovegod.newbuy.bean.Goods;
import com.lovegod.newbuy.bean.LoginMessage;
import com.lovegod.newbuy.bean.Order;
import com.lovegod.newbuy.bean.Province;
import com.lovegod.newbuy.bean.Shop;
import com.lovegod.newbuy.bean.ShopCartBean;
import com.lovegod.newbuy.bean.SortFrist;
import com.lovegod.newbuy.bean.Town;
import com.lovegod.newbuy.bean.User;

import com.lovegod.newbuy.bean.Assess;
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
import okhttp3.MultipartBody;
import okhttp3.RequestBody;



/**
 * Created by 123 on 2017/4/14.
 */

public class NetWorks extends RetrofitUtils {

    protected static final LoginApi loginApi = getRetrofit().create(LoginApi.class);

    protected static final ShopApi shopApi = getRetrofit().create(ShopApi.class);

    protected static final CartApi cartApi = getRetrofit().create(CartApi.class);

    protected static final SortApi sortApi = getRetrofit().create(SortApi.class);

    protected static final BleApi bleApi = getRetrofit().create(BleApi.class);

    protected static final GoodsApi goodsApi = getRetrofit().create(GoodsApi.class);

    protected static final ChangeApi changeApi=getRetrofit().create(ChangeApi.class);

    protected static final AddressApi addressApi=getRetrofit().create(AddressApi.class);

    protected static final OrderApi orderApi=getRetrofit().create(OrderApi.class);

    public static void getAllshop(BaseObserver<List<Shop>> shopObserver) {
        setSubscribe(shopApi.getAllShop(), shopObserver);
        System.out.println();
    }

    public static void getIDshop(int sid, BaseObserver<Shop> shopObserver) {
        setSubscribe(shopApi.getIDshop(sid), shopObserver);
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

    public static void getSortFirst(BaseObserver<List<SortFrist>> sortObserver) {
        setSubscribe(sortApi.getSortFirst(), sortObserver);
    }

    public static void getSecondGoods(int cgid, BaseObserver<List<Commodity>> sortgoodObserver) {
        setSubscribe(sortApi.getSecondGoods(cgid), sortgoodObserver);
    }


    public static void findGoodByMac(String mac, BaseObserver<Commodity> shopObserver) {
        setSubscribe(shopApi.findGoodByMac(mac), shopObserver);
    }

    public static void findShopAllBle(String mac, BaseObserver<List<Ble>> bleObserver) {
        setSubscribe(bleApi.getShopAllBle(mac), bleObserver);
    }

    public static void getPushCommodity(String mac, Double x, Double y, Integer uid, BaseObserver<Commodity> comObserver) {
        setSubscribe(bleApi.getPushCommodity(mac, x, y, uid), comObserver);
    }

    public static void findCommodity(Integer id, BaseObserver<Commodity> commodityBaseObserver) {
        setSubscribe(goodsApi.findCommotity(id), commodityBaseObserver);
    }


    public static void getAllAssess(int cid, BaseObserver<List<Assess>> assessObserver) {
        setSubscribe(goodsApi.getAllAssess(cid), assessObserver);
    }

    public static void getPhoneInfo(String number, BaseObserver<User> loginObserver) {
        setSubscribe(loginApi.getPhoneInfo(number),loginObserver);
    }
    public static void getUserNameInfo(String username, BaseObserver<User> loginObserver) {
        setSubscribe(loginApi.getUserNameInfo(username),loginObserver);
    }
    public static void commitLoginInfo(Map<String,String>partMap,BaseObserver<User>commitObserver){
        setSubscribe(loginApi.commitLoginInfo(partMap),commitObserver);
    }
    public static void changePassword(Integer id, Integer paramsId, String password, BaseObserver<LoginMessage>changePasswdObserver){
        setSubscribe(changeApi.changePasswd(id,paramsId,password),changePasswdObserver);
    }
    public static void changeUserName(Integer id,String userName, BaseObserver<LoginMessage>changePasswdObserver){
        setSubscribe(changeApi.changeUserName(id,userName),changePasswdObserver);
    }
    public static void getIdInfo(Integer id,BaseObserver<User>idObserver){
        setSubscribe(loginApi.getIdInfo(id),idObserver);
    }
    public static void changePhoneNumber(Integer id,String phone,BaseObserver<User>changePhoneObserver){
        setSubscribe(changeApi.changPhoneNumber(id,phone),changePhoneObserver);
    }
    public static void getNameGoods(String name,Integer page,BaseObserver<List<Commodity>>goodsObserver){
        setSubscribe(goodsApi.getNameGoods(name,page),goodsObserver);
    }
    public static void getNameShop(String name,Integer page,BaseObserver<List<Shop>>shopObserver){
        setSubscribe(shopApi.findShopByName(name,page),shopObserver);
    }
    public static void getAllProvince(BaseObserver<List<Province>>provinceObserver){
        setSubscribe(addressApi.getAllProvince(),provinceObserver);
    }
    public static void getCity(int pid,BaseObserver<List<City>>cityObserver){
        setSubscribe(addressApi.getCity(pid),cityObserver);
    }
    public static void getDistrict(int cid, BaseObserver<List<District>>districtObserver){
        setSubscribe(addressApi.getDistrict(cid),districtObserver);
    }
    public static void getTown(int did, BaseObserver<List<Town>>townObserver){
        setSubscribe(addressApi.getTown(did),townObserver);
    }
    public static void getCommunity(int tid, BaseObserver<List<Community>>communityObserver){
        setSubscribe(addressApi.getCommunity(tid),communityObserver);
    }
    public static void addAddress(Map<String,String> map,BaseObserver<Address>addressObserver){
        setSubscribe(addressApi.addAddress(map),addressObserver);
    }
    public static void getAddress(int uid,BaseObserver<List<Address>>addressObserver){
        setSubscribe(addressApi.getAddress(uid),addressObserver);
    }
    public static void deleteAddress(int said,BaseObserver<LoginMessage>deleteObserver){
        setSubscribe(addressApi.deleteAddress(said),deleteObserver);
    }
    public static void editAddress(Map<String,String> map,BaseObserver<Address>addressObserver){
        setSubscribe(addressApi.editAddress(map),addressObserver);
    }
    public static void commitOrder(Map<String,String>map, BaseObserver<Order>orderObserver){
        setSubscribe(orderApi.commitOrder(map),orderObserver);
    }
    public static void commitOrderGoods(Map<String,String>map, BaseObserver<Order.OrderGoods>orderObserver){
        setSubscribe(orderApi.commitOrderGoods(map),orderObserver);
    }
    public static void commitPayOrder(long oid,String paytype,BaseObserver<Order>orderObserver){
        setSubscribe(orderApi.commitPayOrder(oid,paytype),orderObserver);
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
