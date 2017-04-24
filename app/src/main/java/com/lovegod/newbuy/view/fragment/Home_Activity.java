package com.lovegod.newbuy.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.lovegod.newbuy.MyApplication;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Shop;
import com.lovegod.newbuy.view.ShopActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * *****************************************
 * Created by thinking on 2017/4/3.
 * 创建时间：
 * <p>
 * 描述：主页
 * <p/>
 * <p/>
 * *******************************************
 */

public class Home_Activity extends Fragment {

    // 按类别搜索显示图标
    final int[] images = new int[]{R.mipmap.gree01, R.mipmap.midea01,
            R.mipmap.haier01, R.mipmap.hisense01, R.mipmap.suning01,
            R.mipmap.taobao01, R.mipmap.tianmao01, R.mipmap.more};
    final String[] titles = new String[]{"格力", "美的", "海尔", "海信", "苏宁", "淘宝",
            "天猫", "更多"};

    // 声明一个Map类型的List以及一个包含店铺图片和信息的数组

    final int[] imageId = new int[]{R.mipmap.entity_shop01,
            R.mipmap.entity_shop02, R.mipmap.entity_shop03,
            R.mipmap.entity_shop03, R.mipmap.entity_shop04,
            R.mipmap.entity_shop05};


//    final String[] shop_name = new String[]{"格兰仕厨房电器专卖店 ",
//            "格力空调专卖店中国店", "苏宁电器专卖店中国店", "美的冰箱电器专卖店", "海信电器专卖店",
//            "格兰仕厨房电器专卖店中国店"};
//    final String[] shop_instruction = new String[]{"地址：新郑市龙湖镇中原工学院北500米",
//            "地址：新郑市龙湖镇中原工学院北500米", "地址：新郑市龙湖镇中原工学院北500米",
//            "地址：新郑市龙湖镇中原工学院北500米", "地址：新郑市龙湖镇中原工学院北500米", "地址：新郑市龙湖镇中原工学院北500米"};

    //  Shop allshop=new Shop();

    GridView gridView;
    ListView listView;
    ;
    private HomeImageListAdapter homeImageListAdapter;
    private List<Shop> shopList = new ArrayList<>();
    int time = 0;
    boolean run = true;
    Thread thread;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.home, container, false);
        listView = (ListView) view.findViewById(R.id.suggest_shop);
        gridView = (GridView) view.findViewById(R.id.gridView_separate);
//        thread = new Thread(new MyThread());
//        thread.start();
        NetWorks.getAllshop(new BaseObserver<List<Shop>>() {
            @Override
            public void onHandleSuccess(List<Shop> shops) {
                shopList = shops;
//                run=false;
//                thread.interrupt();
            }
        });
        Log.e("网络访问时长", time+"");
        homeImageListAdapter = new HomeImageListAdapter();
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute(time);

        AdapterView.OnItemClickListener listclickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Shop shopdd = shopList.get(position);
                Intent intent = new Intent(getActivity(), ShopActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("shop", shopdd);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
        listView.setOnItemClickListener(listclickListener);
        initGridView();
        return view;
    }

    class MyThread implements Runnable {

        @Override
        public void run() {
            while (run){
                while (!Thread.currentThread().isInterrupted()){
                    try {
                        Thread.sleep(1);
                        time++;
                        if(time%10==0)
                        Log.v("计时器：",time+"");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    private Bitmap getBitmap(String url) {
        final Bitmap[] bitmap = new Bitmap[1];
        Glide.with(Home_Activity.this)
                .load(url)
                .asBitmap()
                .error(R.mipmap.ic_launcher)
                .fitCenter()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        bitmap[0] = resource;
                    }
                });
        return bitmap[0];
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void initGridView() {
        List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < images.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", images[i]);
            map.put("title", titles[i]);
            listItem.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), listItem,
                R.layout.items, new String[]{"title", "image"}, new int[]{
                R.id.text_separate, R.id.imageView_separate});
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(clickListener);
    }

    private AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            switch (position) {
//			case 0:
//				intent = new Intent(FootActivity.this, OffLineActivity.class);
//				startActivity(intent);
//				break;

                default:
                    break;
            }
        }

    };

    //实现ViewBinder接口
    class MyViewBinder implements SimpleAdapter.ViewBinder {
        /**
         * view：要板顶数据的视图
         * data：要绑定到视图的数据
         * textRepresentation：一个表示所支持数据的安全的字符串，结果是data.toString()或空字符串，但不能是Null
         * 返回值：如果数据绑定到视图返回真，否则返回假
         */
        @Override
        public boolean setViewValue(View view, Object data,
                                    String textRepresentation) {
            if ((view instanceof ImageView) & (data instanceof Bitmap)) {
                ImageView iv = (ImageView) view;
                Bitmap bmp = (Bitmap) data;
                iv.setImageBitmap(bmp);
                return true;
            } else {
                return false;
            }
        }
    }

    class MyAsyncTask extends AsyncTask<Integer, Void, List<Shop>> {

        @Override
        protected List<Shop> doInBackground(Integer... integers) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return shopList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Shop> shops) {
            super.onPostExecute(shops);
            homeImageListAdapter.bindData(MyApplication.getInstance().getApplicationContext(), shops);
            listView.setAdapter(homeImageListAdapter);
            homeImageListAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
