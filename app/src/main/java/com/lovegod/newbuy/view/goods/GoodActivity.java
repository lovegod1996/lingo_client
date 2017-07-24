package com.lovegod.newbuy.view.goods;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
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
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.chrisbanes.photoview.PhotoView;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Assess;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.Shop;
import com.lovegod.newbuy.bean.ShopCartBean;
import com.lovegod.newbuy.view.Shop2Activity;
import com.lovegod.newbuy.view.ShopActivity;
import com.lovegod.newbuy.view.utils.GradationScrollView;
import com.lovegod.newbuy.view.utils.MaterialIndicator;
import com.lovegod.newbuy.view.utils.MyRecyclerViewAdapter;
import com.lovegod.newbuy.view.utils.NoScrollListView;
import com.lovegod.newbuy.view.utils.StatusBarUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
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
    private GradationScrollView scrollView;
    private TextView textView;
    private int imageHeight;
    private ViewPager viewPager;
//    LinearLayout text_bg;

    NoScrollListView imagelist;

    FrameLayout flg;
    Button addcart;//加入购物车
    Button compare;//比较
    Button shop;//店铺
    ImageView turnback;
    ImageView keep;
    ListView image_details_listview;
    RelativeLayout li_title;


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
        parameterDialog = new Dialog(this, R.style.map_dialog);
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
        Window dialogWindow = parameterDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = -20; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        dialogWindow.setAttributes(lp);

        // int cid=commodity.getCid();
        NetWorks.getAllAssess(commodity.getCid(), new BaseObserver<List<Assess>>() {
            @Override
            public void onHandleSuccess(List<Assess> assesses) {
                if(assesses.size()>0) {
                    assess_num.setText("全部评价(" + assesses.size() + ")");
                    user_name.setText(assesses.get(0).getHollrall());
                    user_time.setText(String.valueOf(assesses.get(0).getUid()));
                    user_assess.setText(assesses.get(0).getDetail());
                }

            }

            @Override
            public void onHandleError(List<Assess> assesses) {

            }
        });
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

        keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keep.setImageResource(R.mipmap.keeped);
            }
        });

        addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            li_title.setBackgroundColor(Color.argb((int) 0, 144, 151, 166));
        } else if (y > 0 && y <= imageHeight) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) y / imageHeight;
            float alpha = (255 * scale);
            textView.setTextColor(Color.argb((int) alpha, 255, 255, 255));
            li_title.setBackgroundColor(Color.argb((int) alpha, 144, 151, 166));
        } else {    //滑动到banner下面设置普通颜色
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

}

