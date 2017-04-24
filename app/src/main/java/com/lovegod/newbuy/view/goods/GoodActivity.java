package com.lovegod.newbuy.view.goods;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.lovegod.newbuy.R;
import com.lovegod.newbuy.view.ShopActivity;
import com.lovegod.newbuy.view.utils.GradationScrollView;
import com.lovegod.newbuy.view.utils.MaterialIndicator;
import com.lovegod.newbuy.view.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



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


    FrameLayout flg;
    Button addcart;//加入购物车
    Button compare;//比较
    Button shop;//店铺
    ImageView turnback;
    ListView image_details_listview;
    RelativeLayout li_title;



    TextView tv_good_detail_cate;//产品参数
    /*对话框*/
    Dialog parameterDialog;
ListView dialog_listview;
    final String[] parameter_name = new String[] { "包装尺寸 ",
            "品牌", "屏幕尺寸", "分辨率", "背光灯类型",
            "类型","商品名称","整机功率","对比度","互联网电视","重量","操作系统","USB支持视频格式" };
    final String[] parameter = new String[] { "1335*829*196MM",
            "Sony/索尼", "55INC",
            "3840*2160", "LED", "LED液晶电视机","KD-55X6000D","195W","500:1","是","16.8KG(不含底座)","Linux","MPEG1/MPEG2PS/MPEG2TS/AVCHD/MP4Part10/MP4Part2/AVI/MOV/WMV/MKV/RMVB/WEBM/3GPP" };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setImgTransparent(this);
        setContentView(R.layout.good_information);


        scrollView = (GradationScrollView) findViewById(R.id.scrollview_good);
        textView = (TextView) findViewById(R.id.textview_good);
//        text_bg = (LinearLayout) findViewById(R.id.text_bg);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        viewPager.setFocusable(true);
        viewPager.setFocusableInTouchMode(true);
        viewPager.requestFocus();



        //透明状态栏
        StatusBarUtil.setTranslucent(this,110);

        addcart = (Button) findViewById(R.id.addcart);
        compare = (Button) findViewById(R.id.compare);
        shop = (Button) findViewById(R.id.shop);
        turnback = (ImageView) findViewById(R.id.turnback);
        li_title=(RelativeLayout)findViewById(R.id.li_title);


        tv_good_detail_cate=(TextView)findViewById(R.id.tv_good_detail_cate);
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
                R.layout.dialog_listview_item, new String[] { "ParameterName","Parameter" }, new int[] { R.id.parameeter_name,R.id.parameeter});
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
/**
 * 产品参数
 */
        tv_good_detail_cate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parameterDialog.show();
            }
        });





        image_details_listview = (ListView) findViewById(R.id.image_details_listview);
        int[] resImags = {R.mipmap.tu1,  R.mipmap.tu2, R.mipmap.tu3, R.mipmap.tu4, R.mipmap.tu5, R.mipmap.tu6,};
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < resImags.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", resImags[i]);
            listItem.add(map);
        }
        SimpleAdapter mSimpleAdapter = new SimpleAdapter(this, listItem, R.layout.good_listview_item, new String[] {"ItemImage"}, new int[]{R.id.item_image});
        image_details_listview.setAdapter(mSimpleAdapter);





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
    private View.OnClickListener dialoglistener=new View.OnClickListener(){
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
        turnback = (ImageView) findViewById(R.id.turnback);

        turnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(GoodActivity.this,CompareActivity.class);
                startActivity(intent2);
            }
        });

        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoodActivity.this, ShopActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
//    public Action getIndexApiAction() {
//        Thing object = new Thing.Builder()
//                .setName("Good Page") // TODO: Define a title for the content shown.
//                // TODO: Make sure this auto-generated URL is correct.
//                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
//                .build();
//        return new Action.Builder(Action.TYPE_VIEW)
//                .setObject(object)
//                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
//                .build();
//    }



    /*//**
     * viewpager适配器
     */
    private class MyPagerAdapter extends PagerAdapter {
        public int[] drawables = {R.mipmap.tutu2,R.mipmap.tutu1, R.mipmap.tutu3, R.mipmap.tutu4};

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setImageResource(drawables[position]);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
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
     * 滑动监听
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
            textView.setTextColor(Color.argb((int) 255, 144, 151, 166));
        }

    }
}
