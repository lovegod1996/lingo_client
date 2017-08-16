package com.hyphenate.easeui;


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


    public static void getPhoneInfo(String number, BaseObserver<User> loginObserver) {
        setSubscribe(loginApi.getPhoneInfo(number), loginObserver);
    }

    public static void getUserNameInfo(String username, BaseObserver<User> loginObserver) {
        setSubscribe(loginApi.getUserNameInfo(username), loginObserver);
    }

    public static void commitLoginInfo(Map<String, String> partMap, BaseObserver<User> commitObserver) {
        setSubscribe(loginApi.commitLoginInfo(partMap), commitObserver);
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
