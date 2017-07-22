package com.lovegod.newbuy.view.goods;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lovegod.newbuy.R;
import com.lovegod.newbuy.view.utils.MySimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




/**
 * *****************************************
 * Created by thinking on 2017/4/5.
 * 创建时间：
 * <p>
 * 描述：比较列表
 * <p/>
 * <p/>
 * *******************************************
 */

public class CompareActivity extends Activity {
    ImageView compare;
    ImageView compare_back;
    TextView compare_goodname;
    ListView compare_listview;
    RatingBar ratingbar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.good_compare);
       init();
        initlistener();
    }



    private void init() {
        compare=(ImageView)findViewById(R.id.compare);
        compare_back=(ImageView)findViewById(R.id.compare_back);
        compare_goodname=(TextView)findViewById(R.id.compare_goodname);
        compare_listview=(ListView)findViewById(R.id.compare_listview);
        ratingbar=(RatingBar)findViewById(R.id.ratingbar);
        compare_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        int[] shop_images = {R.mipmap.tu1, R.mipmap.tu2, R.mipmap.tu3, R.mipmap.tu4, R.mipmap.tu5, R.mipmap.tu6};
        String[] shop_name={"百业家电专营店","思迪电器专营店","中佳电器专营店","华强官方旗舰店","粤城电器","杭越电器专营店"};
        Double[] ratingbars={5.0,4.5,5.0,4.5,4.5,4.5};
        Double[] moneys={3900.0,3990.0,4099.0,4199.0,4199.00,4399.0};

        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < shop_images.length; i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("ShopImage",shop_images[i]);
            listItem.put("ShopName",shop_name[i]);
         listItem.put("Ratingbar",ratingbars[i]);//////////////
            listItem.put("Moneys",moneys[i]);
            listItems.add(listItem);
        }
        MySimpleAdapter simpleAdapter = new MySimpleAdapter(this, listItems, R.layout.compare_listview_item,
                new String[]{"ShopImage", "ShopName","RatingBars", "Moneys"},
                new int[]{R.id.compare_shop_image, R.id.compare_shopname,R.id.ratingbar, R.id.compare_money,});

        compare_listview.setAdapter(simpleAdapter);

    }
    private void initlistener() {
compare_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
});

    }

}
