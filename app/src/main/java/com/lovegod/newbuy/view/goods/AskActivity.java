package com.lovegod.newbuy.view.goods;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.Quest;
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.utils.system.SpUtils;
import com.lovegod.newbuy.view.LoginActivity;
import com.lovegod.newbuy.view.carts.ShopCartAdapter;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 问答模块的主活动
 */
public class AskActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recycler;
    private RelativeLayout askLayout;
    private String saveQuestion;
    private Commodity commodity;
    private User user;
    private AskAdapter adapter;
    private List<Quest>questList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
        commodity= (Commodity) getIntent().getSerializableExtra("commodity");
        toolbar=(Toolbar)findViewById(R.id.ask_toolbar);
        askLayout=(RelativeLayout)findViewById(R.id.ask_commit_ask_layout);
        recycler=(RecyclerView)findViewById(R.id.ask_recyclerview);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        adapter=new AskAdapter(this,questList);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(adapter);

        setSupportActionBar(toolbar);

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
         * item的查看更多回答点击
         */
        adapter.setOnItemClick(new ShopCartAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle=new Bundle();
                //商品信息
                bundle.putSerializable("commodity",commodity);
                //被点击的问题信息
                bundle.putSerializable("quest",questList.get(position));
                Intent intent=new Intent(AskActivity.this,ShowOneAskActivity.class);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });


        /**
         * 提问按钮监听
         */
        askLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user!=null) {
                    final Dialog dialog = new Dialog(AskActivity.this, R.style.no_title_dialog);
                    LinearLayout root = (LinearLayout) LayoutInflater.from(AskActivity.this).inflate(R.layout.ask_dialog, null);
                    final EditText editText = (EditText) root.findViewById(R.id.ask_dialog_edit);
                    TextView sureButton = (TextView) root.findViewById(R.id.ask_dialog_sure);
                    TextView cancelButton = (TextView) root.findViewById(R.id.ask_dialog_cancel);
                    //如果有上次未提交的内容，设置进来
                    if (!TextUtils.isEmpty(saveQuestion)) {
                        editText.setText(saveQuestion);
                    }
                    dialog.setContentView(root);
                    dialog.setCanceledOnTouchOutside(false);
                    Window window = dialog.getWindow();
                    window.setWindowAnimations(R.style.popWindowStyle);
                    WindowManager.LayoutParams params = window.getAttributes();
                    params.width = getResources().getDisplayMetrics().widthPixels;
                    window.setAttributes(params);
                    dialog.show();

                    /**
                     * 取消按钮监听
                     */
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            saveQuestion = editText.getText().toString().trim();
                            dialog.dismiss();
                        }
                    });

                    /**
                     * 确定按钮监听
                     */
                    sureButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String title = editText.getText().toString().trim();
                            if (TextUtils.isEmpty(title)) {
                                Toast.makeText(AskActivity.this, "你还没输入问题呢", Toast.LENGTH_SHORT).show();
                            } else {
                                //提交问题
                                commitQuest(title,dialog);
                            }
                        }
                    });
                }else {
                    Toast.makeText(AskActivity.this,"请先登陆哦~",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AskActivity.this, LoginActivity.class));
                }
            }
        });
    }

    /**
     * 提交问题的请求
     */
    private void commitQuest(String title, final Dialog dialog) {
        final Map<String, String> map = new HashMap<>();
        map.put("cid", commodity.getCid() + "");
        map.put("uid", user.getUid() + "");
        map.put("username", user.getUsername());
        map.put("title",title);
        map.put("up",0+"");

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
                        map.put("asktime",format.format(currentDate));
                        NetWorks.commitQuest(map, new BaseObserver<Quest>(AskActivity.this) {
                            @Override
                            public void onHandleSuccess(Quest quest) {
                                Toast.makeText(AskActivity.this,"提问成功，别的小伙伴很快就会来回答的",Toast.LENGTH_SHORT).show();
                                onResume();
                                dialog.dismiss();
                            }

                            @Override
                            public void onHandleError(Quest quest) {

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
     * 如果用户前往登录页面登陆回到该页面重新获取用户信息
     */
    @Override
    protected void onResume() {
        super.onResume();
        user= (User) SpUtils.getObject(this,"userinfo");
        questList.clear();
        queryQuest();
    }

    /**
     * 查询所有问题
     */
    private void queryQuest(){
        NetWorks.queryQuest(commodity.getCid(), new BaseObserver<List<Quest>>(this,new ProgressDialog(this)) {
            @Override
            public void onHandleSuccess(List<Quest> quests) {
                for(Quest quest:quests){
                    questList.add(quest);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onHandleError(List<Quest> quests) {

            }
        });
    }
}
