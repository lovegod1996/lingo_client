package com.lovegod.newbuy.view.goods;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.Order;
import com.lovegod.newbuy.bean.Quest;
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.utils.system.SpUtils;
import com.lovegod.newbuy.view.search.SearchResultActivity;

import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowOneAskActivity extends AppCompatActivity {
    private boolean isFocus;
    private Button focusButton;
    private RelativeLayout goodsInfoLayout;
    private Toolbar toolbar;
    private EditText replyEdit;
    private RecyclerView recycler;
    private ImageView goodsImage;
    private TextView goodsName,askText,countText,sendText;
    private User user;
    private Quest quest;
    private Commodity commodity;
    private List<Quest.Reply>replieList=new ArrayList<>();
    private ReplyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_one_ask);
        Bundle bundle=getIntent().getBundleExtra("bundle");
        quest= (Quest) bundle.getSerializable("quest");
        commodity=(Commodity)bundle.getSerializable("commodity");
        user= (User) SpUtils.getObject(this,"userinfo");
        replieList=quest.getReplies();
        goodsInfoLayout=(RelativeLayout)findViewById(R.id.show_one_ask_goodsinfo_layout);
        toolbar=(Toolbar)findViewById(R.id.show_one_ask_toolbar);
        replyEdit=(EditText)findViewById(R.id.show_one_ask_reply_edit);
        sendText=(TextView)findViewById(R.id.show_one_ask_send);
        askText=(TextView)findViewById(R.id.show_one_ask_ask);
        recycler=(RecyclerView)findViewById(R.id.show_one_ask_recyclerview);
        goodsImage=(ImageView)findViewById(R.id.show_one_ask_image);
        goodsName=(TextView)findViewById(R.id.show_one_ask_goodsname);
        countText=(TextView)findViewById(R.id.show_one_ask_count);
        focusButton=(Button)findViewById(R.id.show_one_ask_focus);

        setSupportActionBar(toolbar);

        LinearLayoutManager manager=new LinearLayoutManager(this);
        adapter=new ReplyAdapter(this,replieList);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(manager);


        //初始化各个值
        initInfo();

        /**
         * 关注按钮监听
         */
        focusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        /**
         * 返回按钮监听
         */
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * 商品点击监听,跳转到商品详情页面
         */
        goodsInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowOneAskActivity.this, GoodActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("commodity",commodity);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        /**
         * 返回键监听
         */
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * 提交问题回复的点击监听
         */
        sendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content=replyEdit.getText().toString().trim();
                if(!TextUtils.isEmpty(content)){
                    sendReplyPre(content);
                }
            }
        });
    }

    /**
     * 提交回复信息之前的信息组装
     */
    private void sendReplyPre(final String content) {
        final Map<String,String>map=new HashMap<>();
        map.put("qid",quest.getQid()+"");
        map.put("uid",user.getUid()+"");
        map.put("up",0+"");
        map.put("content",content);

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
                            map.put("retime",format.format(currentDate));
                            NetWorks.isBuy(user.getUid(), commodity.getCid(), new BaseObserver<Order>(ShowOneAskActivity.this) {
                                @Override
                                public void onHandleSuccess(Order order) {
                                    map.put("buystatue",0+"");
                                    sendReply(map);
                                }

                                @Override
                                public void onHandleError(Order order) {
                                    map.put("buystatue",1+"");
                                    sendReply(map);
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

    /**
     * 初始化信息
     */
    private void initInfo() {
        if(user==null){
            replyEdit.setHint("只有登录用户才能回复");
            replyEdit.setKeyListener(null);
        }
        goodsName.setText(commodity.getProductname());
        Glide.with(this).load(commodity.getLogo()).into(goodsImage);
        askText.setText(quest.getTitle());
        countText.setText("共"+quest.getReplies().size()+"条回答");
    }


    /**
     * 真正提交回复信息
     */
    private void sendReply(Map<String,String>map){
        NetWorks.commitQuestReply(map, new BaseObserver<Quest.Reply>(this) {
            @Override
            public void onHandleSuccess(Quest.Reply reply) {
                replyEdit.setText("");
                replieList.add(reply);
                Collections.sort(replieList,new orderComparator());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onHandleError(Quest.Reply reply) {

            }
        });
    }


    /**
     * 自定义排序，按照订单创建的时间由晚到早进行排序
     */
    class orderComparator implements Comparator<Quest.Reply> {
        @Override
        public int compare(Quest.Reply o1, Quest.Reply o2) {
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date o1Date = null,o2Date=null;
            try {
                o1Date=format.parse(o1.getRetime());
                o2Date=format.parse(o2.getRetime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 1;
        }
    }
}
