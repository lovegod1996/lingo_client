package com.lovegod.newbuy.view;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lovegod.newbuy.MainActivity;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.LoginMessage;
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.utils.Md5Util.MD5Util;
import com.lovegod.newbuy.utils.net.NetWorkUtil;
import com.lovegod.newbuy.utils.regex.RegexUtil;
import com.lovegod.newbuy.utils.system.ActivityCollector;
import com.lovegod.newbuy.utils.system.SpUtils;
import com.lovegod.newbuy.utils.system.UserInfoUtil;
import com.lovegod.newbuy.view.registered.RegisteredActivity;

import java.io.IOException;
import top.wefor.circularanim.CircularAnim;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG="LoginActivity";
    private ImageView loginImage;
    private TextView registeredText,forgetPasswdText;
    private TextInputEditText userNameEdit,passwdEdit;
    private Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginImage=(ImageView)findViewById(R.id.login_logo);
        registeredText = (TextView) findViewById(R.id.registered_text);
        forgetPasswdText = (TextView) findViewById(R.id.forget_passwd_text);
        userNameEdit = (TextInputEditText) findViewById(R.id.login_username);
        passwdEdit = (TextInputEditText) findViewById(R.id.login_passwd);
        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
        registeredText.setOnClickListener(this);
    }

    /**
     * 判断用户名/手机号和密码的正确性
     * @param userName 用户名/手机号
     * @param passwd  密码
     * @throws IOException
     */
    private void judgeLoginInfo(final String userName, final String passwd) throws IOException {
        //先判断是否有未填写的空
        if(TextUtils.isEmpty(userName)||TextUtils.isEmpty(passwd)){
            Toast.makeText(LoginActivity.this,"用户名/手机或密码未填写",Toast.LENGTH_SHORT).show();
        }else {
            //判断是否是11位数字
            if(RegexUtil.isElevenNumber(userName)){
                //通过手机号查询
                queryByPhone(userName,passwd);
            }else {
                //通过用户名查询
                queryByUserName(userName,passwd);
            }
        }
    }

    /**
     * 执行登录失败
     */
    private void executeLoginFailed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this,"用户名/手机号或密码错误",Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 延迟1s开启新活动
     */
    private void startNewActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CircularAnim.fullActivity(LoginActivity.this,loginButton).colorOrImageRes(R.color.colorPrimary).go(new CircularAnim.OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }
                });
            }
        },1000);
    }

    /**
     * 运行logo动画
     */
    private void runLogoAnim() {
        Drawable drawable=loginImage.getDrawable();
        if(drawable instanceof Animatable)
        {
            ((Animatable) drawable).start();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.login_button:
                String userName=userNameEdit.getText().toString();
                String passwd=passwdEdit.getText().toString();
                try {
                    judgeLoginInfo(userName,passwd);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.registered_text:
                startActivity(new Intent(LoginActivity.this, RegisteredActivity.class));
                break;
        }
    }

    /**
     * 执行登陆操作
     */
    private void executeLogin(){
        runLogoAnim();
        startNewActivity();
    }

    /**
     * 通过手机号访问服务器比对数据
     * @param userName
     * @param passwd
     */
    public void queryByPhone(final String userName, final String passwd){
        NetWorks.getPhoneInfo(userName, new BaseObserver<User>(LoginActivity.this) {
            @Override
            public void onHandleSuccess(User user) {
                if (user != null) {
                    if (MD5Util.encode(passwd).equals(user.getPassword())) {
                        SpUtils.putObject(LoginActivity.this,"userinfo",user);
                        executeLogin();
                    } else {
                        executeLoginFailed();
                    }
                }else{
                    queryByUserName(userName,passwd);
                }
            }

            @Override
            public void onHandleError(User user) {

            }
        });
    }

    /**
     * 通过用户名访问服务器比对数据
     * @param userName
     * @param passwd
     */
    public void queryByUserName(String userName, final String passwd){
        NetWorks.getUserNameInfo(userName, new BaseObserver<User>(LoginActivity.this) {
            @Override
            public void onHandleSuccess(User user) {
                if(user!=null){
                    if(MD5Util.encode(passwd).equals(user.getPassword())){
                        SpUtils.putObject(LoginActivity.this,"userinfo",user);
                        executeLogin();
                    }else{
                        executeLoginFailed();
                    }
                }else {
                    executeLoginFailed();
                }
            }

            @Override
            public void onHandleError(User user) {

            }
        });
    }
}
