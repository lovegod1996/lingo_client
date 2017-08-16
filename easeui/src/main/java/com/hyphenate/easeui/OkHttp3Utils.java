package com.hyphenate.easeui;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * 类名称：OkHttp3Utils
 * 创建人：wangliteng
 * 创建时间：2016-5-18 14:57:11
 * 类描述：封装一个OkHttp3的获取类
 *
 */
public class OkHttp3Utils {

    private static OkHttpClient mOkHttpClient;



    /**
     * 获取OkHttpClient对象
     *
     * @return
     */
    public static OkHttpClient getOkHttpClient() {

        if (null == mOkHttpClient) {
            //同样okhttp3后也使用build设计模式
            mOkHttpClient = new OkHttpClient.Builder()
                    //设置请求读写的超时时间
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
        }
        return mOkHttpClient;
    }
}
