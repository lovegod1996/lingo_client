package com.lovegod.newbuy.view.sorts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lovegod.newbuy.R;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.view.goods.GoodActivity;
import com.lovegod.newbuy.view.utils.MyRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.media.CamcorderProfile.get;


/**
 * *****************************************
 * Created by thinking on 2017/5/29.
 * 创建时间：
 * <p>
 * 描述：
 * <p/>
 * <p/>
 * *******************************************
 */

public class CategoryGoodsActivity extends AppCompatActivity {

    @BindView(R.id.category_toolbar)
    Toolbar category_toolbar;

    @BindView(R.id.title_name)
    TextView title_name;

    @BindView(R.id.category_goods_recyclerview)
    RecyclerView category_goods_recyclerview;

    @BindView(R.id.category_li3_change)
    LinearLayout category_li3_change;

    @BindView(R.id.change)
    ImageView change;

    private CateGoodsAdapter mAdapter;
private boolean change10=true;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_goods);
        ButterKnife.bind(this);
        final List<Commodity> goodscate=(List<Commodity>) getIntent().getSerializableExtra("categoods");



        if(goodscate!=null) {
            title_name.setText(goodscate.get(0).getProductname());

            category_goods_recyclerview.setLayoutManager(new LinearLayoutManager(CategoryGoodsActivity.this));
            mAdapter = new CateGoodsAdapter(CategoryGoodsActivity.this, goodscate);
            category_goods_recyclerview.setAdapter(mAdapter);
            category_goods_recyclerview.setLayoutManager(new LinearLayoutManager(this));
            mAdapter.setItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(CategoryGoodsActivity.this, GoodActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("commodity",goodscate.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);

                }

                @Override
                public void onItemLongClick(View view, int position) {

                }
            });
        }
        else
            Toast.makeText(this, "是空的。。。。", Toast.LENGTH_SHORT).show();
    }
}
