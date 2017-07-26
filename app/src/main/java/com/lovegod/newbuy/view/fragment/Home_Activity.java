package com.lovegod.newbuy.view.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.google.zxing.activity.CaptureActivity;
import com.lovegod.newbuy.MyApplication;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.Location;
import com.lovegod.newbuy.bean.Shop;
import com.lovegod.newbuy.utils.system.SystemUtils;
import com.lovegod.newbuy.view.Shop2Activity;
import com.lovegod.newbuy.view.goods.GoodActivity;
import com.lovegod.newbuy.view.myview.SearchLayout;
import com.lovegod.newbuy.view.search.SearchActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static com.lovegod.newbuy.MainActivity.REQUEST_CODE;


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

public class Home_Activity extends android.support.v4.app.Fragment {

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
    Button scan_code_btn;
    static Button city_name;
    public SearchLayout searchLayout;
    private SliderLayout mSliderLayout;
    private PagerIndicator indicator;

    private HomeImageListAdapter homeImageListAdapter;
    private List<Shop> shopList = new ArrayList<>();
    int time = 0;
    boolean run = true;
    Thread thread;

    private static Geocoder geocoder;//此对象能通过经纬度来获取相应的城市等信息

    private final OkHttpClient client = new OkHttpClient();

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Location location = (Location) msg.getData().getSerializable("location");
                    city_name.setText(location.getAddress().substring(3, 6));
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.home, container, false);
        listView = (ListView) view.findViewById(R.id.suggest_shop);
        gridView = (GridView) view.findViewById(R.id.gridView_separate);
        scan_code_btn = (Button) view.findViewById(R.id.scan_code_btn);
        city_name = (Button) view.findViewById(R.id.city_name);


        searchLayout = (SearchLayout) view.findViewById(R.id.main_search_layout);
        //容器
        mSliderLayout = (SliderLayout) view.findViewById(R.id.slider);
        //指示器，那些小点
        indicator = (PagerIndicator) view.findViewById(R.id.custom_indicator);
        initSlider();
//        thread = new Thread(new MyThread());
//        thread.start();
        homeImageListAdapter = new HomeImageListAdapter();
        NetWorks.getAllshop(new BaseObserver<List<Shop>>() {
            @Override
            public void onHandleSuccess(List<Shop> shops) {
                shopList = shops;
                homeImageListAdapter.bindData(MyApplication.getInstance().getApplicationContext(), shops);
                listView.setAdapter(homeImageListAdapter);
                homeImageListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onHandleError(List<Shop> shops) {

            }
        });
        Log.e("网络访问时长", time + "");
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION};
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || SystemUtils.checkPermissionGranted(getActivity(), permissions)) {
            try{
                init();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            requestPermissions(permissions, 1);
        }


//        MyAsyncTask myAsyncTask = new MyAsyncTask();
//        myAsyncTask.execute(time);

        AdapterView.OnItemClickListener listclickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NetWorks.getIDshop(shopList.get(position).getSid(), new BaseObserver<Shop>() {
                    @Override
                    public void onHandleSuccess(Shop shops) {
                        if (shops != null) {
                            Intent intent = new Intent(getActivity(), Shop2Activity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("shop", shops);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onHandleError(Shop shop) {

                    }

                });


            }
        };
        listView.setOnItemClickListener(listclickListener);
        initGridView();
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),SearchActivity.class));
            }
        });
        return view;
    }

    /**
     * 添加基站定位
     */
    private void init() {

        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        String operator = telephonyManager.getNetworkOperator();
        String mcc = operator.substring(0, 3);
        String mnc = operator.substring(3);
        int cid = 0;
        int lac = 0;
        if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
            CdmaCellLocation cdmaCellLocation = (CdmaCellLocation)
                    telephonyManager.getCellLocation();
            cid = cdmaCellLocation.getBaseStationId(); //获取cdma基站识别标号 BID
            lac = cdmaCellLocation.getNetworkId(); //获取cdma网络编号NID
            int sid = cdmaCellLocation.getSystemId(); //用谷歌API的话cdma网络的mnc要用这个getSystemId()取得→SID
        } else {
            GsmCellLocation gsmCellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
            cid = gsmCellLocation.getCid(); //获取gsm基站识别标号
            lac = gsmCellLocation.getLac(); //获取gsm网络编号
        }
//        RequestBody formBody = new FormBody.Builder()
//                .add("coord", "gcj02")
//                .add("output", "json")
//                .add("mcc", mcc)
//                .add("mnc",Integer.parseInt(mnc)+"")
//                .add("lac", lac + "")
//                .add("ci", cid + "")
//                .build();
        String params="?coord=gcj02&output=json&mcc="+mcc+"&mnc="+Integer.parseInt(mnc)+"&lac="+lac+"&ci="+cid;
        final Request request = new Request.Builder()
                .url("http://api.cellocation.com/cell/"+params)
                .get()
                .build();
        System.out.println("定位："+mcc+" "+mnc+" "+lac+" "+cid+" ");
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();
                    System.out.println("定位："+result);
                    Log.v("定位",result);
                    Gson gson = new Gson();
                    Location location = gson.fromJson(result, Location.class);

                    Message message = new Message();
                    message.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("location", location);
                    message.setData(bundle);
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    class MyThread implements Runnable {

        @Override
        public void run() {
            while (run) {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Thread.sleep(1);
                        time++;
                        if (time % 10 == 0)
                            Log.v("计时器：", time + "");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    /*广告栏轮播*/
    private void initSlider() {
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("http://m.360buyimg.com/mobilecms/s300x98_jfs/t2416/102/20949846/13425/a3027ebc/55e6d1b9Ne6fd6d8f.jpg");
        imageUrls.add("http://m.360buyimg.com/mobilecms/s300x98_jfs/t1507/64/486775407/55927/d72d78cb/558d2fbaNb3c2f349.jpg");
        imageUrls.add("http://m.360buyimg.com/mobilecms/s300x98_jfs/t1363/77/1381395719/60705/ce91ad5c/55dd271aN49efd216.jpg");
        for (int i = 0; i < imageUrls.size(); i++) {
            //新建三个展示View，并且添加到SliderLayout
            TextSliderView tsv = new TextSliderView(getActivity());
            tsv.image(imageUrls.get(i));
            final int finalI = i;
            tsv.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {
                    Toast.makeText(getActivity(), "图", Toast.LENGTH_SHORT).show();
                }
            });
            mSliderLayout.addSlider(tsv);
        }
        //对SliderLayout进行一些自定义的配置
        // mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mSliderLayout.setDuration(3000);
//     sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSliderLayout.setCustomIndicator(indicator);
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


        scan_code_btn.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                String[] permission = new String[]{Manifest.permission.CAMERA};
                Integer v = Build.VERSION.SDK_INT;
                if (Build.VERSION.SDK_INT < 23) {
                    startCaptureActivityForResult();
                } else {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        //申请CAMERA权限
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, 0);
                    } else {
                        startCaptureActivityForResult();
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean b = verifyPermissions(grantResults);
        switch (requestCode){
            case 0:
                if (b) {
                    startCaptureActivityForResult();
                } else {
                    showMissingPermissionDialog();
                }
                break;
            case 1:
                if (b) {
                    init();
                } else {
                    showMissingPermissionDialog();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    public boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }
        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { //RESULT_OK = -1
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            Integer cid = Integer.parseInt(scanResult);

            NetWorks.findCommodity(cid, new BaseObserver<Commodity>() {
                @Override
                public void onHandleSuccess(Commodity commodity) {
                    if (commodity != null) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("commodity", commodity);
                        Intent intent = new Intent(getActivity(), GoodActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }

                @Override
                public void onHandleError(Commodity commodity) {

                }
            });

            Toast.makeText(getActivity(), scanResult, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 跳转至扫描页面
     */
    private void startCaptureActivityForResult() {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, REQUEST_CODE);
    }

    /**
     * 显示提示信息
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage("当前应用缺少必要权限。请点击\"设置\"-\"权限\"-打开所需权限。");

        // 拒绝, 退出应用
        builder.setNegativeButton("取消", null);

        builder.setPositiveButton("设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转至当前应用的权限设置页面
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     * 启动应用的设置
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
        startActivity(intent);
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

            NetWorks.getAllshop(new BaseObserver<List<Shop>>() {
                @Override
                public void onHandleSuccess(List<Shop> shops) {
                    shopList = shops;
                }

                @Override
                public void onHandleError(List<Shop> shops) {

                }
            });
            while (shopList == null || shopList.size() == 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
