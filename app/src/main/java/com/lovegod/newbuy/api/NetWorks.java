package com.lovegod.newbuy.api;


import com.lovegod.newbuy.bean.Address;
import com.lovegod.newbuy.bean.Assess;
import com.lovegod.newbuy.bean.BaseBean;
import com.lovegod.newbuy.bean.City;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.Community;
import com.lovegod.newbuy.bean.District;
import com.lovegod.newbuy.bean.FavouriteGoods;
import com.lovegod.newbuy.bean.FavouriteQuest;
import com.lovegod.newbuy.bean.FavouriteShop;
import com.lovegod.newbuy.bean.LoginMessage;
import com.lovegod.newbuy.bean.Order;
import com.lovegod.newbuy.bean.Province;
import com.lovegod.newbuy.bean.Quest;
import com.lovegod.newbuy.bean.Shop;
import com.lovegod.newbuy.bean.ShopCartBean;
import com.lovegod.newbuy.bean.SortFrist;
import com.lovegod.newbuy.bean.Town;
import com.lovegod.newbuy.bean.Track;
import com.lovegod.newbuy.bean.Trial;
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.bean.boss.Boss;
import com.lovegod.newbuy.service.Ble;
import com.lovegod.newbuy.utils.retrofitRxjava.RetrofitUtils;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by 123 on 2017/4/14.
 */

public class NetWorks extends RetrofitUtils {

    protected static final LoginApi loginApi = getRetrofit().create(LoginApi.class);

    protected static final ShopApi shopApi = getRetrofit().create(ShopApi.class);

    protected static final BossApi bossApi = getRetrofit().create(BossApi.class);

    protected static final CartApi cartApi = getRetrofit().create(CartApi.class);

    protected static final SortApi sortApi = getRetrofit().create(SortApi.class);

    protected static final BleApi bleApi = getRetrofit().create(BleApi.class);

    protected static final GoodsApi goodsApi = getRetrofit().create(GoodsApi.class);

    protected static final ChangeApi changeApi = getRetrofit().create(ChangeApi.class);

    protected static final AddressApi addressApi = getRetrofit().create(AddressApi.class);

    protected static final OrderApi orderApi = getRetrofit().create(OrderApi.class);

    protected static final QuestApi questApi = getRetrofit().create(QuestApi.class);


    public static void getBossBySid(Integer sid, BaseObserver<Boss> bossObserver) {
        setSubscribe(bossApi.getBoossidByshopid(sid), bossObserver);
    }

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
        setSubscribe(loginApi.getPhoneInfo(number), loginObserver);
    }

    public static void getUserNameInfo(String username, BaseObserver<User> loginObserver) {
        setSubscribe(loginApi.getUserNameInfo(username), loginObserver);
    }

    public static void commitLoginInfo(Map<String, String> partMap, BaseObserver<User> commitObserver) {
        setSubscribe(loginApi.commitLoginInfo(partMap), commitObserver);
    }

    public static void changePassword(Integer id, Integer paramsId, String password, BaseObserver<LoginMessage> changePasswdObserver) {
        setSubscribe(changeApi.changePasswd(id, paramsId, password), changePasswdObserver);
    }

    public static void changeUserName(Integer id, String userName, BaseObserver<LoginMessage> changePasswdObserver) {
        setSubscribe(changeApi.changeUserName(id, userName), changePasswdObserver);
    }

    public static void getIdInfo(Integer id, BaseObserver<User> idObserver) {
        setSubscribe(loginApi.getIdInfo(id), idObserver);
    }

    public static void changePhoneNumber(Integer id, String phone, BaseObserver<User> changePhoneObserver) {
        setSubscribe(changeApi.changPhoneNumber(id, phone), changePhoneObserver);
    }

    public static void getNameGoods(String name, Integer page, BaseObserver<List<Commodity>> goodsObserver) {
        setSubscribe(goodsApi.getNameGoods(name, page), goodsObserver);
    }

    public static void getNameShop(String name, Integer page, BaseObserver<List<Shop>> shopObserver) {
        setSubscribe(shopApi.findShopByName(name, page), shopObserver);
    }

    public static void getAllProvince(BaseObserver<List<Province>> provinceObserver) {
        setSubscribe(addressApi.getAllProvince(), provinceObserver);
    }

    public static void getCity(int pid, BaseObserver<List<City>> cityObserver) {
        setSubscribe(addressApi.getCity(pid), cityObserver);
    }

    public static void getDistrict(int cid, BaseObserver<List<District>> districtObserver) {
        setSubscribe(addressApi.getDistrict(cid), districtObserver);
    }

    public static void getTown(int did, BaseObserver<List<Town>> townObserver) {
        setSubscribe(addressApi.getTown(did), townObserver);
    }

    public static void getCommunity(int tid, BaseObserver<List<Community>> communityObserver) {
        setSubscribe(addressApi.getCommunity(tid), communityObserver);
    }

    public static void addAddress(Map<String, String> map, BaseObserver<Address> addressObserver) {
        setSubscribe(addressApi.addAddress(map), addressObserver);
    }

    public static void getAddress(int uid, BaseObserver<List<Address>> addressObserver) {
        setSubscribe(addressApi.getAddress(uid), addressObserver);
    }

    public static void deleteAddress(int said, BaseObserver<LoginMessage> deleteObserver) {
        setSubscribe(addressApi.deleteAddress(said), deleteObserver);
    }

    public static void editAddress(Map<String, String> map, BaseObserver<Address> addressObserver) {
        setSubscribe(addressApi.editAddress(map), addressObserver);
    }

    public static void commitOrder(Map<String, String> map, BaseObserver<Order> orderObserver) {
        setSubscribe(orderApi.commitOrder(map), orderObserver);
    }

    public static void commitOrderGoods(Map<String, String> map, BaseObserver<Order.OrderGoods> orderObserver) {
        setSubscribe(orderApi.commitOrderGoods(map), orderObserver);
    }

    public static void commitPayOrder(long oid, String paytype, BaseObserver<Order> orderObserver) {
        setSubscribe(orderApi.commitPayOrder(oid, paytype), orderObserver);
    }

    public static void getAllOrder(int uid, BaseObserver<List<Order>> orderObserver) {
        setSubscribe(orderApi.getAllOrder(uid), orderObserver);
    }

    public static void getOrderGoods(long oid, BaseObserver<List<Order.OrderGoods>> orderObserver) {
        setSubscribe(orderApi.getOrderGoods(oid), orderObserver);
    }

    public static void getForThePaymentOrder(int uid, BaseObserver<List<Order>> orderObserver) {
        setSubscribe(orderApi.getForThePaymentOrder(uid), orderObserver);
    }

    public static void getToSendGoodsOrder(int uid, BaseObserver<List<Order>> orderObserver) {
        setSubscribe(orderApi.getToSendGoodsOrder(uid), orderObserver);
    }

    public static void getForTheGoodsOrder(int uid, BaseObserver<List<Order>> orderObserver) {
        setSubscribe(orderApi.getForTheGoodsOrder(uid), orderObserver);
    }

    public static void getFinishOrder(int uid, BaseObserver<List<Order>> orderObserver) {
        setSubscribe(orderApi.getFinishOrder(uid), orderObserver);
    }

    public static void getAllOrderByPage(int uid, int page, BaseObserver<List<Order>> orderObserver) {
        setSubscribe(orderApi.getAllOrderByPage(uid, page), orderObserver);
    }

    public static void getOrderByStatue(int uid, int statue, int openstatue, int page, BaseObserver<List<Order>> orderObserver) {
        setSubscribe(orderApi.getOrderByStatue(uid, statue, openstatue, page), orderObserver);
    }

    public static void getAddressById(int said, BaseObserver<Address> addressObserver) {
        setSubscribe(addressApi.getAddressById(said), addressObserver);
    }

    public static void cancelOrder(long oid, BaseObserver<Order> orderObserver) {
        setSubscribe(orderApi.cancelOrder(oid), orderObserver);
    }

    public static void confirmTheGoods(long oid, BaseObserver<Order> orderObserver) {
        setSubscribe(orderApi.confirmTheGoods(oid), orderObserver);
    }

    public static void getOrderById(long oid, BaseObserver<Order> orderObserver) {
        setSubscribe(orderApi.getOrderById(oid), orderObserver);
    }

    public static void getNoAssessGoods(int uid, int page, BaseObserver<List<Order.OrderGoods>> orderObserver) {
        setSubscribe(orderApi.getNoAssessGoods(uid, page), orderObserver);
    }

    public static void commitAssess(Map<String, String> map, BaseObserver<Assess> assessObserver) {
        setSubscribe(goodsApi.commitAssess(map), assessObserver);
    }

    public static void changeOrderGoodsStatue(int ogid, BaseObserver<Order.OrderGoods> orderObserver) {
        setSubscribe(orderApi.changeOrderGoodsStatue(ogid), orderObserver);
    }

    public static void commitQuest(Map<String, String> map, BaseObserver<Quest> questObserver) {
        setSubscribe(questApi.commitQuest(map), questObserver);
    }

    public static void queryQuest(int cid, BaseObserver<List<Quest>> questObserver) {
        setSubscribe(questApi.queryQuest(cid), questObserver);
    }

    public static void commitQuestReply(Map<String, String> map, BaseObserver<Quest.Reply> questObserver) {
        setSubscribe(questApi.commitQuestReply(map), questObserver);
    }

    public static void isBuy(int uid, int cid, BaseObserver<Order> orderObserver) {
        setSubscribe(orderApi.isBuy(uid, cid), orderObserver);
    }

    public static void addFoucusGoods(Map<String, String> map, BaseObserver<FavouriteGoods> goodsObserver) {
        setSubscribe(goodsApi.addFoucusGoods(map), goodsObserver);
    }

    public static void cancelFoucusGoods(int gaid, BaseObserver<FavouriteGoods> goodsObserver) {
        setSubscribe(goodsApi.cancelFoucusGoods(gaid), goodsObserver);
    }

    public static void getFoucusGoods(int uid, int page, BaseObserver<List<FavouriteGoods>> goodsObserver) {
        setSubscribe(goodsApi.getFoucusGoods(uid, page), goodsObserver);
    }

    public static void isGoodsFoucus(int uid, int cid, BaseObserver<FavouriteGoods> goodsObserver) {
        setSubscribe(goodsApi.isGoodsFoucus(uid, cid), goodsObserver);
    }

    public static void addFocusShop(Map<String, String> map, BaseObserver<FavouriteShop> shopObserver) {
        setSubscribe(shopApi.addFocusShop(map), shopObserver);
    }

    public static void isShopFocus(int uid, int sid, BaseObserver<FavouriteShop> shopObserver) {
        setSubscribe(shopApi.isShopFocus(uid, sid), shopObserver);
    }

    public static void cancelShopFocus(int said, BaseObserver<FavouriteShop> shopObserver) {
        setSubscribe(shopApi.cancelShopFocus(said), shopObserver);
    }

    public static void getFocusShop(int uid, int page, BaseObserver<List<FavouriteShop>> shopObserver) {
        setSubscribe(shopApi.getFocusShop(uid, page), shopObserver);
    }

    public static void addQuestFocus(Map<String, String> map, BaseObserver<FavouriteQuest> questObserver) {
        setSubscribe(questApi.addQuestFocus(map), questObserver);
    }

    public static void cancelQuestFocus(int qaid, BaseObserver<FavouriteQuest> questObserver) {
        setSubscribe(questApi.cancelQuestFocus(qaid), questObserver);
    }

    public static void isQuestFocus(int uid, int qid, BaseObserver<FavouriteQuest> questObserver) {
        setSubscribe(questApi.isQuestFocus(uid, qid), questObserver);
    }

    public static void getQuestFocus(int uid, int page, BaseObserver<List<FavouriteQuest>> questObserver) {
        setSubscribe(questApi.getQuestFocus(uid, page), questObserver);
    }

    public static void getQuesByID(int qid, BaseObserver<Quest> questObserver) {
        setSubscribe(questApi.getQuesByID(qid), questObserver);
    }

    public static void getAssessCount(int cid, BaseObserver<Integer> assesscountObserver) {
        setSubscribe(goodsApi.getAssessCount(cid), assesscountObserver);
    }

    public static void getNewAssess(int cid, BaseObserver<Assess> assessObserver) {
        setSubscribe(goodsApi.getNewAssess(cid), assessObserver);
    }

    public static void getNearbyShop(double longgitude, double latitude, int dis, BaseObserver<List<Shop>> shopObserver) {
        setSubscribe(shopApi.getNearbyShop(longgitude, latitude, dis), shopObserver);
    }
    public static void addTrialGoods(Map<String,String>map, BaseObserver<Trial>trialObserver){
        setSubscribe(goodsApi.addTrialGoods(map),trialObserver);
    }
    public static void getTrialGoods(int uid,BaseObserver<List<Trial>>trialObserver){
        setSubscribe(goodsApi.getTrialGoods(uid),trialObserver);
    }
    public static void isTrial(int uid,int cid,BaseObserver<Trial>trialObserver){
        setSubscribe(goodsApi.isTrial(uid, cid),trialObserver);
    }
    public static void commitTrackInfo(Map<String,String>map, BaseObserver<Track>goodsObserver){
        setSubscribe(goodsApi.commitTrackInfo(map),goodsObserver);
    }
    public static void getOneTrack(int uid,int cid,BaseObserver<Track>goodsObserver){
        setSubscribe(goodsApi.getOneTrack(uid, cid),goodsObserver);
    }
    public static void getAllTrack(int uid,int page,BaseObserver<List<Track>>goodsObserver){
        setSubscribe(goodsApi.getAllTrack(uid,page),goodsObserver);
    }
    public static void changeInfoTrack(int detail,int tid,int total,BaseObserver<Track>goodsObserver){
        setSubscribe(goodsApi.changeInfoTrack(detail,tid,total),goodsObserver);
    }
    public static void changeAssessTrack(int assess,int tid,int total,BaseObserver<Track>goodsObserver){
        setSubscribe(goodsApi.changeAssessTrack(assess, tid, total),goodsObserver);
    }
    public static void changeQuestionTrack(int question,int tid,int total,BaseObserver<Track>goodsObserver){
        setSubscribe(goodsApi.changeQuestionTrack(question, tid, total),goodsObserver);
    }
    public static void changeAttentionTrack(int attention,int tid,int total,BaseObserver<Track>goodsObserver){
        setSubscribe(goodsApi.changeAttentionTrack(attention, tid, total),goodsObserver);
    }
    public static void changeCartTrack(int cart,int tid,int total,BaseObserver<Track>goodsObserver){
        setSubscribe(goodsApi.changeCartTrack(cart, tid, total),goodsObserver);
    }
    public static void changeUserAssessTrack(int userassess,int tid,int total,BaseObserver<Track>goodsObserver){
        setSubscribe(goodsApi.changeUserAssessTrack(userassess, tid, total),goodsObserver);
    }
    public static void changeBuyTrack(int buy,int tid,int total,BaseObserver<Track>goodsObserver){
        setSubscribe(goodsApi.changeBuyTrack(buy, tid, total),goodsObserver);
    }
    public static void getRecommendGoods(int uid,BaseObserver<List<Commodity>>goodsObserver){
        setSubscribe(goodsApi.getRecommendGoods(uid),goodsObserver);
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
