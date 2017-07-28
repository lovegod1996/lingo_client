package com.lovegod.newbuy.view.myinfo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Assess;
import com.lovegod.newbuy.bean.Order;
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.utils.system.SpUtils;
import com.lovegod.newbuy.view.carts.ShopCartAdapter;
import com.lovegod.newbuy.view.myview.StarRating;
import com.lovegod.newbuy.view.registered.PicassoImageLoader;
import com.lovegod.newbuy.view.search.ControlScrollRecyclerView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublishAssessActivity extends AppCompatActivity {
    private static final int TAKE_PHOTO=1;

    private User user;
    private ImageView goodsImage;
    private Order.OrderGoods orderGoods;
    private Toolbar toolbar;
    private EditText assessContentText,assessTitleText;
    private StarRating starRating,deliverRating,shopRating;
    private TextView totalText,deliverText,shopText,publishText;
    private ImageView addImage;
    private ControlScrollRecyclerView recyclerView;
    private AssessPicAdapter adapter;
    private List<ImageItem>images=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_assess);
        user= (User) SpUtils.getObject(this,"userinfo");
        orderGoods= (Order.OrderGoods) getIntent().getSerializableExtra("assess_goods");
        toolbar=(Toolbar)findViewById(R.id.publish_assess_toolbar);
        publishText=(TextView)findViewById(R.id.publish_assess_publish);
        goodsImage=(ImageView)findViewById(R.id.publish_assess_goodsimage);
        assessContentText=(EditText)findViewById(R.id.publish_assess_content_edit);
        assessTitleText=(EditText)findViewById(R.id.publish_assess_title_edit);
        recyclerView=(ControlScrollRecyclerView)findViewById(R.id.publish_assess_recyclerview);
        deliverRating=(StarRating)findViewById(R.id.publish_assess_deliverservice_star);
        shopRating=(StarRating)findViewById(R.id.publish_assess_shopservice_star);
        starRating=(StarRating)findViewById(R.id.publish_assess_total_star);
        deliverText=(TextView)findViewById(R.id.publish_assess_deliverservice_text);
        shopText=(TextView)findViewById(R.id.publish_assess_shopservice_text);
        totalText=(TextView)findViewById(R.id.publish_assess_total_text);
        addImage=(ImageView)findViewById(R.id.publish_assess_addimage);
        //设置评分的星星默认都为5颗
        starRating.setCurrentStarCount(5);
        deliverRating.setCurrentStarCount(5);
        shopRating.setCurrentStarCount(5);
        //设置recyclerview的布局以及数据源
        GridLayoutManager manager=new GridLayoutManager(this,4);
        adapter=new AssessPicAdapter(this,images);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        recyclerView.setCanScroll(false);
        //初始化图片选择器
        initImagePicker();

        //加载商品Logo
        Glide.with(this).load(orderGoods.getLogo()).into(goodsImage);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        adapter.setOnItemClickListener(new AssessPicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                images.remove(position);
                adapter.notifyItemRemoved(position);
                //删除过后如果图片数量小于5，就显示添加按钮
                if(images.size()<5){
                    addImage.setVisibility(View.VISIBLE);
                }
            }
        });


        /**
         * 监听总评的星星点击改变后面的描述
         */
        starRating.setOnStarChangeListener(new StarRating.OnStarChangeListener() {
            @Override
            public void onStarChange(int starCount) {
                switch (starCount){
                    case 1:
                        totalText.setText("非常差");
                        break;
                    case 2:
                        totalText.setText("差");
                        break;
                    case 3:
                        totalText.setText("一般");
                        break;
                    case 4:
                        totalText.setText("好");
                        break;
                    case 5:
                        totalText.setText("非常好");
                        break;
                }
            }
        });

        /**
         * 设置添加图片按钮监听
         */
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PublishAssessActivity.this, ImageGridActivity.class);
                startActivityForResult(intent, TAKE_PHOTO);
            }
        });

        /**
         * 监听物流服务星星改变
         */
        deliverRating.setOnStarChangeListener(new StarRating.OnStarChangeListener() {
            @Override
            public void onStarChange(int starCount) {
                switch (starCount){
                    case 1:
                        deliverText.setText("非常差");
                        break;
                    case 2:
                        deliverText.setText("差");
                        break;
                    case 3:
                        deliverText.setText("一般");
                        break;
                    case 4:
                        deliverText.setText("好");
                        break;
                    case 5:
                        deliverText.setText("非常好");
                        break;
                }
            }
        });

        /**
         * 监听商店服务星星改变
         */
        shopRating.setOnStarChangeListener(new StarRating.OnStarChangeListener() {
            @Override
            public void onStarChange(int starCount) {
                switch (starCount){
                    case 1:
                        shopText.setText("非常差");
                        break;
                    case 2:
                        shopText.setText("差");
                        break;
                    case 3:
                        shopText.setText("一般");
                        break;
                    case 4:
                        shopText.setText("好");
                        break;
                    case 5:
                        shopText.setText("非常好");
                        break;
                }
            }
        });

        /**
         * 返回按钮监听
         */
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(PublishAssessActivity.this).setTitle("确定要取消发布评价吗").setNegativeButton("确定取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setPositiveButton("继续发布", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
            }
        });

        /**
         * 发布点击监听
         */
        publishText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=assessTitleText.getText().toString().trim();
                String content=assessContentText.getText().toString().trim();
                //如果总评为空
                if(TextUtils.isEmpty(title)){
                    Toast.makeText(PublishAssessActivity.this,"概述必须填哦",Toast.LENGTH_SHORT).show();
                    return;
                }
                commitAssess(title,content);
            }
        });
    }

    /**
     * 提交评价
     */
    private void commitAssess(String title,String content) {
        final Map<String,String>assessMap=new HashMap<>();
        assessMap.put("cid",orderGoods.getCid()+"");
        assessMap.put("ogid",orderGoods.getOgid()+"");
        assessMap.put("sid",orderGoods.getSid()+"");
        assessMap.put("uid",user.getUid()+"");
        assessMap.put("hollrall",title);
        assessMap.put("detail",content);
        assessMap.put("grade",starRating.getCurrentCount()+"");
        assessMap.put("param",orderGoods.getParam());
        assessMap.put("username",user.getUsername());
        assessMap.put("count",orderGoods.getCount()+"");
        //获取网络时间的线程
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
                            assessMap.put("date",format.format(currentDate));
                            NetWorks.commitAssess(assessMap, new BaseObserver<Assess>(PublishAssessActivity.this,new ProgressDialog(PublishAssessActivity.this)){
                                @Override
                                public void onHandleSuccess(Assess assess) {
                                    //提交评价成功后修改订单商品状态为已评价
                                    NetWorks.changeOrderGoodsStatue(orderGoods.getOgid(), new BaseObserver<Order.OrderGoods>(PublishAssessActivity.this) {
                                        @Override
                                        public void onHandleSuccess(Order.OrderGoods orderGoods) {
                                            finish();
                                        }

                                        @Override
                                        public void onHandleError(Order.OrderGoods orderGoods) {

                                        }
                                    });
                                }

                                @Override
                                public void onHandleError(Assess assess) {

                                }
                            });
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 初始化图片选择器
     */
    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new PicassoImageLoader());
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setMultiMode(true);//设置单选
        imagePicker.setSelectLimit(5);    //选中数量限制
        imagePicker.setSaveRectangle(false); //是否按矩形区域保存
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==ImagePicker.RESULT_CODE_ITEMS){
            if ((data!=null&& requestCode == TAKE_PHOTO)){
                ArrayList<ImageItem> tempImages = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                //如果新添加的照片加上当前照片数量大于5，就不添加
                if (images.size()+tempImages.size()>5){
                    Toast.makeText(this,"最多上传5张图片",Toast.LENGTH_SHORT).show();
                    return;
                }
                for(ImageItem item:tempImages){
                    images.add(item);
                }
                adapter.notifyDataSetChanged();
                //如果一次选择了张，隐藏添加按钮
                if(images.size()==5){
                    addImage.setVisibility(View.GONE);
                }
            }
        }
    }
}
