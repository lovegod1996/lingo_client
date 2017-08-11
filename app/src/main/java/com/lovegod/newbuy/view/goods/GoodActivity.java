package com.lovegod.newbuy.view.goods;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.Printer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.ywx.lib.StarRating;
import com.example.ywx.viewlibrary.LoadingButton;
import com.github.chrisbanes.photoview.PhotoView;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Assess;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.FavouriteGoods;
import com.lovegod.newbuy.bean.Order;
import com.lovegod.newbuy.bean.Quest;
import com.lovegod.newbuy.bean.Shop;
import com.lovegod.newbuy.bean.ShopCartBean;
import com.lovegod.newbuy.bean.Shopcate;
import com.lovegod.newbuy.bean.Trial;
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.utils.system.SpUtils;
import com.lovegod.newbuy.view.Shop2Activity;
import com.lovegod.newbuy.view.ShopActivity;
import com.lovegod.newbuy.view.carts.CartActivity;
import com.lovegod.newbuy.view.fragment.Cart_Activity;
import com.lovegod.newbuy.view.myinfo.PublishAssessActivity;
import com.lovegod.newbuy.view.utils.GradationScrollView;
import com.lovegod.newbuy.view.utils.MaterialIndicator;
import com.lovegod.newbuy.view.utils.MyRecyclerViewAdapter;
import com.lovegod.newbuy.view.utils.NoScrollListView;
import com.lovegod.newbuy.view.utils.StatusBarUtil;

import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.os.Build.VERSION_CODES.M;
import static android.os.Build.VERSION_CODES.N;


/**
 * *****************************************
 * Created by thinking on 2017/3/20.
 * 创建时间：
 * <p>
 * 描述：
 * <p/>
 * <p/>
 * *******************************************
 */

public class GoodActivity extends Activity implements GradationScrollView.ScrollViewListener {
    private FavouriteGoods foucusGoods;
    //是否收藏该商品
    private boolean isFoucus=false;
    private GradationScrollView scrollView;
    private TextView textView;
    private int imageHeight;
    private ViewPager viewPager;
    private User user;

    //问答动画是否执行
    private boolean isAnmi=true;

    NoScrollListView imagelist;

    FrameLayout flg;
    Button addcart;//加入购物车
    Button compare;//比较
    Button shop;//店铺
    ImageView turnback;
    ImageView keep;
    ListView image_details_listview;
    RelativeLayout li_title;

    @BindView(R.id.assess_li)
    LinearLayout assessLi;
    @BindView(R.id.user_ratingbar)
    StarRating userRatingbar;
    @BindView(R.id.shop_cart)
    ImageView shopCartIcon;
    @BindView(R.id.good_info_ask_button)
    FloatingActionButton askButton;
    @BindView(R.id.assess_btn)
    Button assess_btn;
    @BindView(R.id.goodsname)
    TextView goodsName;
    @BindView(R.id.goodsprice)
    TextView goodsPrice;
    @BindView(R.id.goodsalvo)
    TextView goodsalevo;
    @BindView(R.id.if_online_store)
    TextView if_online_store;
    @BindView(R.id.store_name)
    TextView store_name;
    @BindView(R.id.store_location)
    TextView store_location;
    @BindView(R.id.store_month_number)
    TextView store_month_number;
    @BindView(R.id.goods_trial)
    TextView trialText;
    @BindView(R.id.user_name)
    TextView user_name;
    @BindView(R.id.user_time)
    TextView user_time;
    @BindView(R.id.user_assess)
    TextView user_assess;
    @BindView(R.id.assess_num)
    TextView assess_num;
    TextView tv_good_detail_cate;//产品参数
    /*对话框*/
    Dialog parameterDialog;
    ListView dialog_listview;
    final String[] parameter_name = new String[]{"包装尺寸 ",
            "品牌", "屏幕尺寸", "分辨率", "背光灯类型",
            "类型", "商品名称", "整机功率", "对比度", "互联网电视", "重量", "操作系统", "USB支持视频格式"};
    final String[] parameter = new String[]{"1335*829*196MM",
            "Sony/索尼", "55INC",
            "3840*2160", "LED", "LED液晶电视机", "KD-55X6000D", "195W", "500:1", "是", "16.8KG(不含底座)", "Linux", "MPEG1/MPEG2PS/MPEG2TS/AVCHD/MP4Part10/MP4Part2/AVI/MOV/WMV/MKV/RMVB/WEBM/3GPP"};

    //  private  ShopCartBean a1;
    private List<Commodity> goodslist = new ArrayList<>();
    private List<ShopCartBean> shopCartBeen = new ArrayList<>();
    private String[] imgUrlArr;
    private List<ImageView> imgList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setImgTransparent(this);
        setContentView(R.layout.good_information);
        user= (User) SpUtils.getObject(this,"userinfo");

        ButterKnife.bind(this);

        scrollView = (GradationScrollView) findViewById(R.id.scrollview_good);
        textView = (TextView) findViewById(R.id.textview_good);
//        text_bg = (LinearLayout) findViewById(R.id.text_bg);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        viewPager.setFocusable(true);
        viewPager.setFocusableInTouchMode(true);
        viewPager.requestFocus();

        final Commodity commodity = (Commodity) getIntent().getSerializableExtra("commodity");

        goodsName.setText(commodity.getProductname());
        goodsPrice.setText(commodity.getPrice() + "");
        goodsalevo.setText(commodity.getSalesvolu() + "人购买");
        textView.setText(commodity.getProductname());
        //透明状态栏
        StatusBarUtil.setTranslucent(this, 110);

        addcart = (Button) findViewById(R.id.addcart);
        compare = (Button) findViewById(R.id.compare);
        shop = (Button) findViewById(R.id.shop);
        turnback = (ImageView) findViewById(R.id.turnback);
        li_title = (RelativeLayout) findViewById(R.id.li_title);
        keep = (ImageView) findViewById(R.id.keep);


        tv_good_detail_cate = (TextView) findViewById(R.id.tv_good_detail_cate);

        //查询是否已经关注
        queryWhetherFoucus();

        //查询是否已经申请体验
        queryWhetherApply();

        //获取最新的评论信息以及评论个数
        getAssess();

        NetWorks.getIDshop(commodity.getSid(), new BaseObserver<Shop>() {
            @Override
            public void onHandleSuccess(Shop shop) {
                store_name.setText(shop.getShopname());
                store_location.setText(shop.getSaddress());
                store_name.setText(shop.getShopname());

            }

            @Override
            public void onHandleError(Shop shop) {

            }
        });

        /**
         * 购物车按钮监听
         */
        shopCartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GoodActivity.this, CartActivity.class));
            }
        });

        /**
         * 问答的按钮监听
         */
        askButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GoodActivity.this,AskActivity.class);
                intent.putExtra("commodity",commodity);
                startActivity(intent);
            }
        });

        assess_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoodActivity.this, AssessActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Cid", String.valueOf(commodity.getCid()));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        //对话框
        parameterDialog = new Dialog(this, R.style.param_dialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_parameter_dialog, null);

        root.findViewById(R.id.dialog_button).setOnClickListener(dialoglistener);
        dialog_listview = (ListView) root.findViewById(R.id.dialog_listview);
        List<Map<String, Object>> listItem1 = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < parameter_name.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("ParameterName", parameter_name[i]);
            map.put("Parameter", parameter[i]);
            listItem1.add(map);
        }
        // 声明适配器，并将其绑定到ListView控件
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItem1,
                R.layout.dialog_listview_item, new String[]{"ParameterName", "Parameter"}, new int[]{R.id.parameeter_name, R.id.parameeter});
        dialog_listview.setAdapter(simpleAdapter);

        parameterDialog.setCanceledOnTouchOutside(true);//点击外部使对话框消失
        parameterDialog.setContentView(root);
        final Window dialogWindow = parameterDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);

        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值

        lp.width = getResources().getDisplayMetrics().widthPixels; // 宽度
        dialogWindow.setAttributes(lp);

        /**
         * 产品参数
         */
        tv_good_detail_cate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parameterDialog.show();
            }
        });
        final String[] a = commodity.getDetailshow().split(";");

        /**
         * 商品详情listview
         */
        image_details_listview = (ListView) findViewById(R.id.image_details_listview);
        image_details_listview.setAdapter(new ImageListAdapter(GoodActivity.this, a));
        MaterialIndicator indicator = (MaterialIndicator) findViewById(R.id.indicator);
        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.addOnPageChangeListener(indicator);
        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        indicator.setAdapter(viewPager.getAdapter());
        initlistener();
        initListeners();

        /**
         * 申请体验按钮监听
         */
        trialText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    final Map<String, String> map = new HashMap<>();
                    map.put("uid",user.getUid()+"");
                    map.put("username",user.getUsername());
                    map.put("cid",commodity.getCid()+"");
                    map.put("sid",commodity.getSid()+"");
                    map.put("areatype",3+"");
                    //获取商店信息
                    NetWorks.getIDshop(commodity.getSid(), new BaseObserver<Shop>(GoodActivity.this) {
                        @Override
                        public void onHandleSuccess(Shop shop) {
                            map.put("area",shop.getCity()+"");
                        }

                        @Override
                        public void onHandleError(Shop shop) {

                        }
                    });
                    //获取时间
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            URL url= null;//取得资源对象
                            try {
                                url = new URL("http://www.taobao.com");
                                URLConnection uc=url.openConnection();//生成连接对象
                                uc.connect(); //发出连接
                                final long ld=uc.getDate(); //取得网站日期时间
                                final Date currentDate=new Date(ld); //转换为标准时间对象
                                final SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                //获取到了后在主线程进行操作
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        map.put("applytime",format.format(currentDate));
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    //弹出对话框
                    final Dialog dialog = new Dialog(GoodActivity.this, R.style.transparent_dialog);
                    LinearLayout root = (LinearLayout) LayoutInflater.from(GoodActivity.this).inflate(R.layout.apply_trial_dialog, null);
                    final StarRating starRating= (StarRating) root.findViewById(R.id.apply_trial_star);
                    LoadingButton commitButton=(LoadingButton)root.findViewById(R.id.apply_trial_button);
                    dialog.setContentView(root);
                    Window window = dialog.getWindow();
                    WindowManager.LayoutParams params = window.getAttributes();
                    params.width = getResources().getDisplayMetrics().widthPixels;
                    window.setAttributes(params);
                    dialog.show();
                    /**
                     * 提交按钮监听
                     */
                    commitButton.setOnButtonClickListener(new LoadingButton.OnButtonClickListener() {
                        @Override
                        public void onButtonClick() {
                            map.put("star",starRating.getCurrentCount()+"");
                            NetWorks.addTrialGoods(map, new BaseObserver<Trial>(GoodActivity.this) {
                                @Override
                                public void onHandleSuccess(Trial trial) {
                                    dialog.dismiss();
                                    Toast.makeText(GoodActivity.this,"申请成功，会尽快帮您处理",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onHandleError(Trial trial) {

                                }
                            });
                        }
                    });
                }else {
                    Toast.makeText(GoodActivity.this,"快去登录，享受体验资格~",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 查询该商品是否申请体验
     */
    private void queryWhetherApply() {
        if(user!=null){
            Commodity commodity = (Commodity) getIntent().getSerializableExtra("commodity");
            NetWorks.isTrial(user.getUid(), commodity.getCid(), new BaseObserver<Trial>(this) {
                @Override
                public void onHandleSuccess(Trial trial) {
                    if(trial!=null){
                        trialText.setText("已申请");
                    }else {
                        trialText.setText("申请体验");
                    }
                }

                @Override
                public void onHandleError(Trial trial) {

                }
            });
        }
    }

    /**
     * 判断该商品该用户是否关注(如果用户登录了的话)
     */
    private void queryWhetherFoucus() {
        if(user!=null){
            Commodity commodity = (Commodity) getIntent().getSerializableExtra("commodity");
            NetWorks.isGoodsFoucus(user.getUid(), commodity.getCid(), new BaseObserver<FavouriteGoods>(this,new ProgressDialog(this)) {
                @Override
                public void onHandleSuccess(FavouriteGoods favouriteGoods) {
                    if(favouriteGoods!=null){
                        keep.setImageResource(R.mipmap.keeped);
                        foucusGoods=favouriteGoods;
                        isFoucus=true;
                    }else {
                        keep.setImageResource(R.mipmap.keep_gray);
                        isFoucus=false;
                    }
                }

                @Override
                public void onHandleError(FavouriteGoods favouriteGoods) {

                }
            });
        }
    }

    //对话框的点击事件
    private View.OnClickListener dialoglistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_button:
                    parameterDialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };


    private void initlistener() {

        turnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * 收藏按钮监听
         */
        keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user!=null) {
                    if (!isFoucus) {
                        //提交关注请求
                        addFoucus();
                    } else {
                        deleteFoucus();
                    }
                }else {
                    Toast.makeText(GoodActivity.this, "登陆后才能收藏喜欢的宝贝哦~", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    final Commodity commodity = (Commodity) getIntent().getSerializableExtra("commodity");
                    NetWorks.getIDshop(commodity.getSid(), new BaseObserver<Shop>() {
                        @Override
                        public void onHandleSuccess(Shop shop) {
                            Map<String, Object> addcartMap = new HashMap<String, Object>();
                            addcartMap.put("uid", 1);
                            addcartMap.put("sid", commodity.getSid());
                            addcartMap.put("cid", commodity.getCid());
                            addcartMap.put("commodity_pic", commodity.getLogo());
                            addcartMap.put("commodity_name", commodity.getProductname());
                            addcartMap.put("price", commodity.getPrice());
                            addcartMap.put("commodity_select", "55寸");
                            addcartMap.put("amount", 1);
                            addcartMap.put("shopname", shop.getShopname());
                            NetWorks.postAddcart(addcartMap, new BaseObserver<ShopCartBean>() {
                                @Override
                                public void onHandleSuccess(ShopCartBean shopCartBean) {
                                    Toast toast = Toast.makeText(getApplicationContext(), "宝贝已添加到购物车", Toast.LENGTH_LONG);
                                    toast.show();
                                }

                                @Override
                                public void onHandleError(ShopCartBean shopCartBean) {

                                }
                            });
                        }

                        @Override
                        public void onHandleError(Shop shop) {

                        }
                    });
                }else {
                    Toast.makeText(GoodActivity.this, "登陆后才能购买喜欢的宝贝哦~", Toast.LENGTH_SHORT).show();
                }
            }
        });

        compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(GoodActivity.this, CompareActivity.class);
                startActivity(intent2);
            }
        });


        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Commodity commodity = (Commodity) getIntent().getSerializableExtra("commodity");
                NetWorks.getIDshop(commodity.getSid(), new BaseObserver<Shop>() {
                    @Override
                    public void onHandleSuccess(Shop shop) {
                        Intent intent = new Intent(GoodActivity.this, Shop2Activity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("shop", shop);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onHandleError(Shop shop) {

                    }
                });

            }
        });

        /**
         * 图片展示列表的每一项点击监听，用于展示大图
         */
        image_details_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView imageView= (ImageView) view;
                final Dialog dialog=new Dialog(GoodActivity.this,R.style.map_dialog);
                RelativeLayout root= (RelativeLayout) LayoutInflater.from(GoodActivity.this).inflate(R.layout.assess_dialog,null);
                PhotoView dialog_image = (PhotoView) root.findViewById(R.id.dialog_image);
                dialog_image.setImageDrawable(imageView.getDrawable());
                dialog.setContentView(root);
                dialog.show();
                //取消按钮监听
                root.findViewById(R.id.re_diaog).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    /**
     * 取消收藏宝贝请求
     */
    private void deleteFoucus() {
        NetWorks.cancelFoucusGoods(foucusGoods.getGaid(), new BaseObserver<FavouriteGoods>(this) {
            @Override
            public void onHandleSuccess(FavouriteGoods favouriteGoods) {
                keep.setImageResource(R.mipmap.keep_gray);
                Toast.makeText(GoodActivity.this, "取消收藏成功~", Toast.LENGTH_SHORT).show();
                isFoucus = false;
            }

            @Override
            public void onHandleError(FavouriteGoods favouriteGoods) {

            }
        });
    }

    /**
     * 添加收藏宝贝请求
     */
    private void addFoucus() {
        Commodity commodity = (Commodity) getIntent().getSerializableExtra("commodity");
        final Map<String,String>map=new HashMap<>();
        map.put("sid",commodity.getSid()+"");
        map.put("cid",commodity.getCid()+"");
        map.put("uid",user.getUid()+"");
        //联网获取时间
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url= null;//取得资源对象
                try {
                    url = new URL("http://www.taobao.com");
                    URLConnection uc = url.openConnection();//生成连接对象
                    uc.connect(); //发出连接
                    final long ld = uc.getDate(); //取得网站日期时间
                    final Date currentDate = new Date(ld); //转换为标准时间对象
                    final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            map.put("looktime",format.format(currentDate));
                            NetWorks.addFoucusGoods(map, new BaseObserver<FavouriteGoods>(GoodActivity.this) {
                                @Override
                                public void onHandleSuccess(FavouriteGoods favouriteGoods) {
                                    foucusGoods=favouriteGoods;
                                    keep.setImageResource(R.mipmap.keeped);
                                    Toast.makeText(GoodActivity.this, "成功收藏该宝贝~", Toast.LENGTH_SHORT).show();
                                    isFoucus = true;
                                }

                                @Override
                                public void onHandleError(FavouriteGoods favouriteGoods) {

                                }
                            });
                        }
                    });
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /*
     * viewpager适配器
     */
    private class MyPagerAdapter extends PagerAdapter {
        Commodity commodity = (Commodity) getIntent().getSerializableExtra("commodity");
        String[] a = commodity.getHeadershow().split(";");
        String[] only4 = Arrays.copyOfRange(a, 0, 4);

        @Override
        public int getCount() {
            return only4.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final ImageView view = new ImageView(container.getContext());
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(GoodActivity.this).load(only4[position]).asBitmap().into(view);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(((View) object));
        }
    }

    /**
     * 获取顶部图片高度后，设置滚动监听
     */
    private void initListeners() {

        ViewTreeObserver vto = viewPager.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                li_title.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                imageHeight = viewPager.getHeight();

                scrollView.setScrollViewListener(GoodActivity.this);
            }
        });
    }


    /**
     * 滑动监听,设置标题栏
     *
     * @param scrollView
     * @param x
     * @param y
     * @param oldx
     * @param oldy
     */
    @Override
    public void onScrollChanged(GradationScrollView scrollView, int x, int y,
                                int oldx, int oldy) {
        // TODO Auto-generated method stub
        if (y <= 0) {   //设置标题的背景颜色
            li_title.setBackgroundColor(Color.argb( 0, 144, 151, 166));
        } else if (y > 0 && y <= imageHeight) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            askButton.setVisibility(View.GONE);
            isAnmi=true;
            float scale = (float) y / imageHeight;
            float alpha = (255 * scale);
            textView.setTextColor(Color.argb((int) alpha, 255, 255, 255));
            li_title.setBackgroundColor(Color.argb((int) alpha, 144, 151, 166));
        } else {    //滑动到banner下面设置普通颜色
            askButton.setVisibility(View.VISIBLE);
            //如果刚拉入下面执行动画
            if(isAnmi){
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(askButton, "scaleX",0.7f,1f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(askButton, "scaleY", 0.7f,1f);
                scaleX.setRepeatMode(ValueAnimator.REVERSE);
                scaleX.setRepeatCount(0);
                scaleY.setRepeatMode(ValueAnimator.REVERSE);
                scaleY.setRepeatCount(0);
                scaleX.setInterpolator(new OvershootInterpolator());
                scaleX.setDuration(700);
                scaleY.setDuration(500);
                //set把两个动画加进来一起执行
                AnimatorSet set=new AnimatorSet();
                set.playTogether(scaleX,scaleY);
                set.start();
                isAnmi=false;
            }

            li_title.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));

            textView.setTextColor(Color.argb((int) 255, 0, 0, 0));
        }

    }

    /**
     * listview 图片ImageListAdapter
     */
    class ImageListAdapter extends ArrayAdapter {
        private Context context;
        private LayoutInflater inflater;

        private String[] imageUrls;

        public ImageListAdapter(Context context, String[] imageUrls) {
            super(context, R.layout.good_listview_item, imageUrls);

            this.context = context;
            this.imageUrls = imageUrls;

            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.good_listview_item, parent, false);
            }
            final ImageView imageView= (ImageView) convertView;
            //先使用 Glide 把图片的原图请求加载过来，然后再按原图来显示图片
            Glide.with(context)
                    .load(imageUrls[position])
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            imageView.setImageBitmap(resource);
                        }
                    });

            return convertView;
        }

    }

    /**
     * 获取一条最新的评价以及获取评价总个数
     */
    private void getAssess(){
        final Commodity commodity = (Commodity) getIntent().getSerializableExtra("commodity");
         /*获取评价*/
        NetWorks.getNewAssess(commodity.getCid(), new BaseObserver<Assess>(this) {

            @Override
            public void onHandleSuccess(Assess assess) {
                if (assess!=null) {
                    Log.v("评价：",assess.getUsername());
                    user_name.setText(newName(assess.getUsername()));
                    user_time.setText(assess.getDate().substring(0, 10));
                    user_assess.setText(assess.getDetail());
                    userRatingbar.setCurrentStarCount((int) assess.getGrade());

                    //获取评价的总条数
                    NetWorks.getAssessCount(commodity.getCid(), new BaseObserver<Integer>() {
                        @Override
                        public void onHandleSuccess(Integer integer) {
                            assess_num.setText("全部评价(" + integer + ")");
                        }

                        @Override
                        public void onHandleError(Integer integer) {

                        }
                    });

                }
            }

            @Override
            public void onHandleError(Assess assess) {
                assessLi.setVisibility(View.GONE);
                assess_num.setText("全部评价(" + 0 + ")");
            }
        });
    }

    /**
     * 隐藏名字，中间用*隐藏
     * @param username 原始用户名
     * @return 隐藏后的用户名
     */
    private String newName(String username) {
        String newname;
        int num = username.length();
        newname = username.charAt(0) + "**" + username.charAt(num - 2) + username.charAt(num - 1);
        return newname;
    }

}

