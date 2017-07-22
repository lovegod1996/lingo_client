package com.lovegod.newbuy.view.myinfo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lovegod.newbuy.MainActivity;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.LoginMessage;
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.utils.Md5Util.MD5Util;
import com.lovegod.newbuy.utils.regex.RegexUtil;
import com.lovegod.newbuy.utils.system.ActivityCollector;
import com.lovegod.newbuy.utils.system.SpUtils;
import com.lovegod.newbuy.utils.system.UserInfoUtil;

public class ChangePasswdActivity extends AppCompatActivity {
    private User user;
    private TextInputEditText rawPasswdText,newPasswdText,newPasswdConfimText;
    private Toolbar toolbar;
    private Button commitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_passwd);
        user= (User) SpUtils.getObject(this,"userinfo");
        toolbar=(Toolbar)findViewById(R.id.change_passwd_toolbar);
        rawPasswdText=(TextInputEditText)findViewById(R.id.raw_passwd_edit_text);
        newPasswdText=(TextInputEditText)findViewById(R.id.new_passwd_edit_text);
        newPasswdConfimText=(TextInputEditText)findViewById(R.id.new_passwdconfirm_edit_text);
        commitButton=(Button)findViewById(R.id.commit_change_passwd_button);
        setSupportActionBar(toolbar);

        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rawPasswd=rawPasswdText.getText().toString();
                final String newPasswd=newPasswdText.getText().toString();
                String newPasswdConfirm=newPasswdConfimText.getText().toString();
                //检查老密码正确
                if(!judgeRawPasswd(rawPasswd)){
                    return;
                }
                //检查新密码是否合法
                else if(judgeNewPasswd(newPasswd,newPasswdConfirm)){
                    //合法后提交修改信息
                    NetWorks.changePassword(user.getUid(), user.getUid(), newPasswd, new BaseObserver<LoginMessage>(ChangePasswdActivity.this,new ProgressDialog(ChangePasswdActivity.this)) {
                        @Override
                        public void onHandleSuccess(LoginMessage loginMessage) {
                                Toast.makeText(ChangePasswdActivity.this,"修改成功,需要重新登录",Toast.LENGTH_SHORT).show();
                                SpUtils.removeKey(ChangePasswdActivity.this,"userinfo");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(new Intent(ChangePasswdActivity.this, MainActivity.class));
                                        finish();
                                    }
                                },1500);
                        }

                        @Override
                        public void onHandleError(LoginMessage loginMessage) {

                        }
                    });
                }
            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 判断新密码是否合法以及是否相同
     * @param newPasswd
     * @param newPasswdConfirm
     * @return
     */
    private boolean judgeNewPasswd(String newPasswd, String newPasswdConfirm) {
        if(TextUtils.isEmpty(newPasswd)){
            Toast.makeText(ChangePasswdActivity.this,"新密码不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }else if (MD5Util.encode(newPasswd).equals(user.getPassword())){
            Toast.makeText(ChangePasswdActivity.this,"新老密码不能相同",Toast.LENGTH_SHORT).show();
            return false;
        }else if(!RegexUtil.isValidPasswd(newPasswd)){
            Toast.makeText(ChangePasswdActivity.this,"新密码不合法",Toast.LENGTH_SHORT).show();
            return false;
        }else if(TextUtils.isEmpty(newPasswdConfirm)){
            Toast.makeText(ChangePasswdActivity.this,"请输入再次输入新密码",Toast.LENGTH_SHORT).show();
            return false;
        }else if(!newPasswd.equals(newPasswdConfirm)){
            Toast.makeText(ChangePasswdActivity.this,"新密码输入不一致",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 判断旧密码是否正确
     * @param rawPasswd
     */
    private boolean judgeRawPasswd(String rawPasswd) {
        if(TextUtils.isEmpty(rawPasswd)){
            Toast.makeText(ChangePasswdActivity.this,"原密码不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }else if(!MD5Util.encode(rawPasswd).equals(user.getPassword())){
            Toast.makeText(ChangePasswdActivity.this,"原密码不正确",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
