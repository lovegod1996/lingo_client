package com.lovegod.newbuy.view.goods;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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

import com.lovegod.newbuy.R;

/**
 * 问答模块的主活动
 */
public class AskActivity extends AppCompatActivity {
    private RelativeLayout askLayout;
    private String saveQuestion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
        askLayout=(RelativeLayout)findViewById(R.id.ask_commit_ask_layout);


        /**
         * 提问按钮监听
         */
        askLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(AskActivity.this,R.style.no_title_dialog);
                LinearLayout root= (LinearLayout) LayoutInflater.from(AskActivity.this).inflate(R.layout.ask_dialog,null);
                final EditText editText=(EditText)root.findViewById(R.id.ask_dialog_edit);
                TextView sureButton= (TextView) root.findViewById(R.id.ask_dialog_sure);
                TextView cancelButton=(TextView)root.findViewById(R.id.ask_dialog_cancel);
                //如果有上次未提交的内容，设置进来
                if(!TextUtils.isEmpty(saveQuestion)){
                    editText.setText(saveQuestion);
                }
                dialog.setTitle("提问");
                dialog.setContentView(root);
                dialog.setCanceledOnTouchOutside(false);
                Window window=dialog.getWindow();
                window.setWindowAnimations(R.style.popWindowStyle);
                WindowManager.LayoutParams params=window.getAttributes();
                root.measure(0,0);
                params.width=getResources().getDisplayMetrics().widthPixels;
                window.setAttributes(params);
                dialog.show();

                /**
                 * 取消按钮监听
                 */
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveQuestion=editText.getText().toString().trim();
                        dialog.dismiss();
                    }
                });

                /**
                 * 确定按钮监听
                 */
                sureButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        });
    }
}
