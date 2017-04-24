package com.lovegod.newbuy.view;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lovegod.newbuy.MyApplication;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.bean.Goods;
import com.lovegod.newbuy.bean.Shop;
import com.lovegod.newbuy.bean.Shopcate;
import com.lovegod.newbuy.view.goods.GoodActivity;
import com.lovegod.newbuy.view.utils.GradationScrollView;
import com.lovegod.newbuy.view.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




/**
 * *****************************************
 * Created by thinking on 2017/3/19.
 * 创建时间：
 * <p>
 * 描述：
 * <p/>
 * <p/>
 * *******************************************
 */

public class ShopActivity extends ExpandableListActivity implements GradationScrollView.ScrollViewListener {
    private GradationScrollView scrollView;

    ExpandableListView listview;

    private TextView textView;
    private int height;
    private ImageView ivBanner;
    // ExpandableListView listview;
    TextView shop_name;
    TextView shop_dizhi;
    RatingBar valuation;
    ImageView shop_phone;
    LinearLayout linearlayout_location;
    /**
     * 创建一级条目容器
     */
    List<Map<String, String>> gruops = new ArrayList<Map<String, String>>();
    /**
     * 存放内容, 以便显示在列表中
     */
    List<List<Map<String, String>>> childs = new ArrayList<List<Map<String, String>>>();

    Shop shop = null;
    Shopcate shopcatekk = new Shopcate();
    Goods goodss = new Goods();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setImgTransparent(this);
        setContentView(R.layout.shop_information);
        MyApplication.getInstance().addActivity(this);
        shop = (Shop) this.getIntent().getSerializableExtra("shop");

        //透明状态栏
        StatusBarUtil.setTranslucent(this, 110);

        scrollView = (GradationScrollView) findViewById(R.id.scrollview);
        listview = (ExpandableListView) findViewById(android.R.id.list);
        listview.setGroupIndicator(null);
        textView = (TextView) findViewById(R.id.textview);
        textView.setText("店铺详情");
        ivBanner = (ImageView) findViewById(R.id.iv_banner);

        shop_name = (TextView) findViewById(R.id.shop_name);
        shop_name.setText(shop.getSname());
        shop_dizhi = (TextView) findViewById(R.id.shop_dizhi);
        shop_dizhi.setText(shop.getSaddress());
        valuation = (RatingBar) findViewById(R.id.valuation);
        valuation.setRating((float) shop.getSlevel());
        shop_phone = (ImageView) findViewById(R.id.shop_phone);

        linearlayout_location = (LinearLayout) findViewById(R.id.linearlayout_location);
        ivBanner.setFocusable(true);
        ivBanner.setFocusableInTouchMode(true);
        ivBanner.requestFocus();

        initListeners();
        // initData();
        setListData();
        linearlayout_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //导航
                Toast.makeText(ShopActivity.this, "稍后添加导航", Toast.LENGTH_SHORT).show();
            }
        });

        shop_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.DIAL");
                intent.setData(Uri.parse("tel:" + shop.getStel()));
                startActivity(intent);
            }
        });
    }

    /**
     * 获取顶部图片高度后，设置滚动监听
     */
    private void initListeners() {

        ViewTreeObserver vto = ivBanner.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                textView.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                height = ivBanner.getHeight();


                scrollView.setScrollViewListener(ShopActivity.this);
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
            textView.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
        } else if (y > 0 && y <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) y / height;
            float alpha = (255 * scale);
            textView.setTextColor(Color.argb((int) alpha, 0, 0, 0));
            textView.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
        } else {    //滑动到banner下面设置普通颜色
            textView.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
        }
    }


    /**
     * 设置列表内容
     */
    public void setListData() {
        // 创建二个一级条目标题
        Map<String, String> title_1 = new HashMap<String, String>();
        Map<String, String> title_2 = new HashMap<String, String>();
        title_1.put("goods", "电视");
        title_2.put("goods", "空调");
        gruops.add(title_1);
        gruops.add(title_2);
//        NetWorks.getshopcate(shop.getSid(), new Observer<Shopcate>() {
//            @Override
//            public void onCompleted() {
//                if (shopcatekk.getCategory().size() != 0) {
//                    for (int i = 0; i < shopcatekk.getCategory().size(); i++) {
//                        Map<String, String> title = new HashMap<String, String>();
//                        title.put("goods", shopcatekk.getCategory().get(i));
//                        gruops.add(title);
//
//                        NetWorks.getgoodes(shop.getSid(), shopcatekk.getCategory().get(i), new Observer<Goods>() {
//                            @Override
//                            public void onCompleted() {
//                            if(goodss.getCommodity().size()!=0){
//                                for(int j=0;j<goodss.getCommodity().size();j++){
//                                    Map<String, String> title_1 = new HashMap<String, String>();
//                                    title_1.put("good",goodss.getCommodity().get(j).getProductname() );
//                                    List<Map<String, String>> childs_1 = new ArrayList<Map<String, String>>();
//                                    childs_1.add(title_1);
//                                    childs.add(childs_1);
//                                }
//                            }
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//
//                            }
//
//                            @Override
//                            public void onNext(Goods goods) {
//                                goodss = goods;
//                            }
//                        });
//
//                    }
//                }
//
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Shopcate shopcate) {
//                shopcatekk = shopcate;
//            }
//        });

        // 创建二级条目内容
        // 内容一
        Map<String, String> title_1_content_1 = new HashMap<String, String>();
        Map<String, String> title_1_content_2 = new HashMap<String, String>();
        Map<String, String> title_1_content_3 = new HashMap<String, String>();
        title_1_content_1.put("good", "长虹 55英寸 智能语音液晶电视");
        title_1_content_2.put("good", "索尼55英寸 4K网络LED液晶电视");
        title_1_content_3.put("good", "创维55英寸 LED液晶平板电视");
       /* title_1_content_1.put("tu", "长虹（CHANGHONG）55D3S 55英寸4K HDR超高清 轻薄金属机身智能语音液晶电视");
        title_1_content_2.put("tu", "索尼（SONY）KD-55X6000D 55英寸 4K网络LED液晶电视");
        title_1_content_3.put("tu", "创维(Skyworth) 55V6 55英寸 4K超高清智能网络LED液晶平板电视");*/
        List<Map<String, String>> childs_1 = new ArrayList<Map<String, String>>();
        childs_1.add(title_1_content_1);
        childs_1.add(title_1_content_2);
        childs_1.add(title_1_content_3);

        // 内容二
        Map<String, String> title_2_content_1 = new HashMap<String, String>();
        Map<String, String> title_2_content_2 = new HashMap<String, String>();
        Map<String, String> title_2_content_3 = new HashMap<String, String>();
        title_2_content_1.put("good", "长虹 55英寸 智能语音液晶电视");
        title_2_content_2.put("good", "索尼55英寸 4K网络LED液晶电视");
        title_2_content_3.put("good", "创维55英寸 LED液晶平板电视");
        List<Map<String, String>> childs_2 = new ArrayList<Map<String, String>>();
        childs_2.add(title_2_content_1);
        childs_2.add(title_2_content_2);
        childs_2.add(title_2_content_3);

        childs.add(childs_1);
        childs.add(childs_2);

        /**
         * 创建ExpandableList的Adapter容器 参数: 1.上下文 2.一级集合 3.一级样式文件 4. 一级条目键值
         * 5.一级显示控件名 6. 二级集合 7. 二级样式 8.二级条目键值 9.二级显示控件名
         */
        SimpleExpandableListAdapter sela = new SimpleExpandableListAdapter(
                this, gruops, R.layout.item_group, new String[]{"goods"},
                new int[]{R.id.goods_name}, childs, R.layout.item_child,
                new String[]{"good"}, new int[]{R.id.good_name});
        // 加入列表
        listview.setAdapter(sela);


        setListViewHeightBasedOnChildren(listview);
    }

    /**
     * 列表子item点击
     */
    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

        String[] group_childs = {gruops.get(groupPosition).toString(), childs.get(groupPosition).get(childPosition).toString()};
        Intent intent=new Intent(ShopActivity.this,GoodActivity.class);
        startActivity(intent);
        return super.onChildClick(parent, v, groupPosition, childPosition, id);
    }

    /**
     * 二级标题按下
     */
    @Override
    public boolean setSelectedChild(int groupPosition, int childPosition,
                                    boolean shouldExpandGroup) {
        return super.setSelectedChild(groupPosition, childPosition,
                shouldExpandGroup);

    }

    /**
     * 一级标题按下
     */
    @Override
    public void setSelectedGroup(int groupPosition) {
        super.setSelectedGroup(groupPosition);
        setListViewHeightBasedOnChildren(listview);
    }


    public static void setListViewHeightBasedOnChildren(ExpandableListView listView) {
        // 获取ListView对应的Adapter
        ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
        if (listAdapter == null) {
            // pre -condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getGroupCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listgroupItem = listAdapter.getGroupView(i, true, null, listView);
            listgroupItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listgroupItem.getMeasuredHeight(); // 统计所有子项的总高度
            System.out.println("height : group" + i + "次" + totalHeight);
            for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                View listchildItem = listAdapter.getChildView(i, j, false, null, listView);
                listchildItem.measure(0, 0); // 计算子项View 的宽高
                totalHeight += listchildItem.getMeasuredHeight(); // 统计所有子项的总高度
                System.out.println("height :" + "group:" + i + " child:" + j + "次" + totalHeight);
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

/*    public static void setlistViewHeightBased(ExpandableListView listView) {
        ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
        if (listAdapter == null) {
            // pre -condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getGroupCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listgroupItem = listAdapter.getGroupView(i, true, null, listView);
            listgroupItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listgroupItem.getMeasuredHeight(); // 统计所有子项的总高度
            System.out.println("height : group" + i + "次" + totalHeight);
        }
        ViewGroup.LayoutParams params = listView .getLayoutParams();
        params.height = totalHeight + ( listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params );
    }*/
}
