package com.lovegod.newbuy.view.myinfo.changeinfo;

import android.app.ProgressDialog;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.LoginMessage;
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.utils.regex.RegexUtil;
import com.lovegod.newbuy.utils.system.SpUtils;

public class ChangeUserNameActivity extends AppCompatActivity {
    private User user;
    private TextInputEditText changeUserNameEdit;
    private Button commitButton;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_name);
        user= (User) SpUtils.getObject(this,"userinfo");
        toolbar=(Toolbar)findViewById(R.id.chang_username_toolbar);
        changeUserNameEdit=(TextInputEditText)findViewById(R.id.change_username_edit);
        commitButton=(Button)findViewById(R.id.change_username_commit_button);
        setSupportActionBar(toolbar);

        changeUserNameEdit.setText(user.getUsername());
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newName=changeUserNameEdit.getText().toString();
                if(checkNewName(newName)){
                    NetWorks.changeUserName(user.getUid(), newName, new BaseObserver<LoginMessage>(ChangeUserNameActivity.this,new ProgressDialog(ChangeUserNameActivity.this)){
                        @Override
                        public void onHandleSuccess(LoginMessage loginMessage) {
                            user.setUsername(newName);
                            SpUtils.removeKey(ChangeUserNameActivity.this,"userinfo");
                            SpUtils.putObject(ChangeUserNameActivity.this,"userinfo",user);
                            finish();
                        }

                        @Override
                        public void onHandleError(LoginMessage loginMessage) {
                            changeUserNameEdit.setError("用户名已存在");
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
     * 检查新用户名是否正确
     * @param newName
     * @return
     */
    private boolean checkNewName(String newName) {
        if(!RegexUtil.isUserName(newName)){
            Toast.makeText(this,"用户名不合法",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
