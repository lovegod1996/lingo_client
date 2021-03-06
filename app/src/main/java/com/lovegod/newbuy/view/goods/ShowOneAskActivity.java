package com.lovegod.newbuy.view.goods;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import com.lovegod.newbuy.bean.FavouriteQuest;
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
    private int commodityId;
    private FavouriteQuest focusQuest;
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
    private Commodity mCommodity;
    private List<Quest.Reply>replieList=new ArrayList<>();
    private ReplyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_one_ask);
        Bundle bundle=getIntent().getBundleExtra("bundle");
        quest= (Quest) bundle.getSerializable("quest");
        commodityId=bundle.getInt("commodityId");
        user= (User) SpUtils.getObject(this,"userinfo");
        replieList=quest.getReplies();
        //根据传过来的id获取商品信息
        getGoodsInfo();

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

        //查询是否关注过此问题
        isFocusQuest();

        /**
         * 关注按钮监听
         */
        focusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user!=null) {
                    if (!isFocus){
                        addFocus();
                    }else {
                        cancelFocus();
                    }
                }else {
                    Toast.makeText(ShowOneAskActivity.this,"要先登录才能使用该功能哦~",Toast.LENGTH_SHORT).show();
                }
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
                //提交成功前不可点击
                sendText.setEnabled(false);
                sendText.setClickable(false);
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
        map.put("username",user.getUsername());
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
                            NetWorks.isBuy(user.getUid(), mCommodity.getCid(), new BaseObserver<Order>(ShowOneAskActivity.this) {
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
            replyEdit.setHint("您尚未登录，只有登录用户才能回复");
            replyEdit.setKeyListener(null);
        }
        goodsName.setText(mCommodity.getProductname());
        Glide.with(this).load(mCommodity.getLogo()).into(goodsImage);
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
                recycler.scrollToPosition(replieList.size()-1);
                initInfo();
                sendText.setEnabled(true);
                sendText.setClickable(true);
                Toast.makeText(ShowOneAskActivity.this,"回复已提交",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onHandleError(Quest.Reply reply) {
                sendText.setEnabled(true);
                sendText.setClickable(true);
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

    /**
     * 判断该用户是否关注此问题
     */
    private void isFocusQuest(){
        //如果用户登录了
        if(user!=null){
            NetWorks.isQuestFocus(user.getUid(), quest.getQid(), new BaseObserver<FavouriteQuest>(this) {
                @Override
                public void onHandleSuccess(FavouriteQuest favouriteQuest) {
                    if(favouriteQuest!=null){
                        focusQuest=favouriteQuest;
                        setFocus();
                    }else {
                        setUnFocus();
                    }
                }

                @Override
                public void onHandleError(FavouriteQuest favouriteQuest) {

                }
            });
        }
    }

    /**
     * 添加关注请求
     */
    private void addFocus(){
        final Map<String,String>map=new HashMap<>();
        map.put("qid",quest.getQid()+"");
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
                            NetWorks.addQuestFocus(map, new BaseObserver<FavouriteQuest>(ShowOneAskActivity.this) {
                                @Override
                                public void onHandleSuccess(FavouriteQuest favouriteQuest) {
                                    focusQuest=favouriteQuest;
                                    setFocus();
                                    Toast.makeText(ShowOneAskActivity.this,"关注成功~",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onHandleError(FavouriteQuest favouriteQuest) {

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
     * 取消关注请求
     */
    private void cancelFocus(){
        NetWorks.cancelQuestFocus(focusQuest.getQaid(), new BaseObserver<FavouriteQuest>(this) {
            @Override
            public void onHandleSuccess(FavouriteQuest favouriteQuest) {
                setUnFocus();
                Toast.makeText(ShowOneAskActivity.this,"取消关注成功~",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onHandleError(FavouriteQuest favouriteQuest) {

            }
        });
    }

    /**
     * 设置关注按钮为关注状态
     */
    private void setFocus(){
        isFocus=true;
        focusButton.setBackgroundResource(R.drawable.focus_shape);
        focusButton.setText("取消关注");
        focusButton.setTextColor(Color.parseColor("#FD6861"));
    }

    /**
     * 设置关注按钮为非关注状态
     */
    private void setUnFocus(){
        isFocus=false;
        focusButton.setBackgroundResource(R.drawable.unfocus_shape);
        focusButton.setText("关注此问题");
        focusButton.setTextColor(Color.BLACK);
    }

    /**
     * 根据商品id获取到商品信息
     */
    private void getGoodsInfo(){
        NetWorks.findCommodity(commodityId, new BaseObserver<Commodity>(this) {
            @Override
            public void onHandleSuccess(Commodity commodity) {
               mCommodity=commodity;
                //初始化各个值
                initInfo();

                /**
                 * 商品点击监听,跳转到商品详情页面(请求到了再设置监听)
                 */
                goodsInfoLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShowOneAskActivity.this, GoodActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("commodity",mCommodity);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onHandleError(Commodity commodity) {

            }
        });
    }
}
