package com.lovegod.newbuy.view.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.utils.system.SpUtils;
import com.lovegod.newbuy.utils.system.SystemUtils;
import com.lovegod.newbuy.view.Shop2Activity;
import com.lovegod.newbuy.view.goods.GoodActivity;
import com.lovegod.newbuy.view.goods.MyGridView;
import com.lovegod.newbuy.view.myview.OnRefreshListener;
import com.lovegod.newbuy.view.myview.RefreshLayout;
import com.lovegod.newbuy.view.myview.SearchLayout;
import com.lovegod.newbuy.view.search.SearchActivity;
import com.lovegod.newbuy.view.utils.GradationScrollView;

import java.io.IOException;
import java.sql.Ref;
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

public class Home_Activity extends Fragment{

    // 按类别搜索显示图标
    final int[] images = new int[]{R.mipmap.gree01, R.mipmap.midea01,
            R.mipmap.haier01, R.mipmap.hisense01, R.mipmap.suning01,
            R.mipmap.taobao01, R.mipmap.tianmao01, R.mipmap.more};
    final String[] titles = new String[]{"格力", "美的", "海尔", "海信", "苏宁", "淘宝",
            "天猫", "更多"};

    MyGridView gridView;
    Button scan_code_btn;
    static Button city_name;
    public SearchLayout searchLayout;
    private SliderLayout mSliderLayout;
    private PagerIndicator indicator;

    private RelativeLayout titleLayout;
    private RefreshLayout refreshLayout;
    private RecyclerView homeRecycler;
    private HomeAdapter adapter;
    private List<Commodity>commodityList=new ArrayList<>();
    private User user;

    private HomeImageListAdapter homeImageListAdapter;
    private List<Shop> shopList = new ArrayList<>();

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
        user= (User) SpUtils.getObject(getActivity(),"userinfo");
        //listView = (ListView) view.findViewById(R.id.suggest_shop);
        //gridView = (MyGridView) view.findViewById(R.id.gridView_separate);
        scan_code_btn = (Button) view.findViewById(R.id.scan_code_btn);
        city_name = (Button) view.findViewById(R.id.city_name);
        titleLayout=(RelativeLayout)view.findViewById(R.id.rl_first_top);
        refreshLayout=(RefreshLayout)view.findViewById(R.id.home_refresh);
        refreshLayout.setBackground(getResources().getColor(R.color.colorPrimary));
        homeRecycler=(RecyclerView)view.findViewById(R.id.home_list);
        adapter=new HomeAdapter(getActivity(),commodityList,images);
        homeRecycler.setLayoutManager(new GridLayoutManager(getActivity(),6,GridLayoutManager.VERTICAL,false));
        homeRecycler.setAdapter(adapter);
        homeRecycler.addItemDecoration(new HomeDecoration());


        searchLayout = (SearchLayout) view.findViewById(R.id.main_search_layout);
        //容器
        //mSliderLayout = (SliderLayout) view.findViewById(R.id.slider);
        //指示器，那些小点
        //indicator = (PagerIndicator) view.findViewById(R.id.custom_indicator);

        //initSlider();
        homeImageListAdapter = new HomeImageListAdapter();
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


        /**
         * 搜索框点击事件
         */
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),SearchActivity.class));
            }
        });

        /**
         * 刷新中的回调
         */
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRecommendGoods();
            }
        });

        return view;
    }

    /**
     * 获取推荐商品
     */
    private void getRecommendGoods() {
        if(user!=null) {
            commodityList.clear();
            NetWorks.getRecommendGoods(user.getUid(), new BaseObserver<List<Commodity>>() {
                @Override
                public void onHandleSuccess(List<Commodity> commodities) {
                    for(Commodity commodity:commodities){
                        commodityList.add(commodity);
                    }
                    adapter.notifyDataSetChanged();
                    refreshLayout.refreshDone();
                }

                @Override
                public void onHandleError(List<Commodity> commodities) {
                    refreshLayout.refreshDone();
                    Toast.makeText(getActivity(),"刷新失败",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
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
                    //将当前位置存入缓存
                    SpUtils.putObject(getActivity(),"location",location);
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

    /**
     * 活动恢复交互重新请求数据
     */
    @Override
    public void onResume() {
        super.onResume();
        //获取推荐商品
        getRecommendGoods();
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
}
