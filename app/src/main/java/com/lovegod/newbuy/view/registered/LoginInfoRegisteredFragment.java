package com.lovegod.newbuy.view.registered;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.utils.Md5Util.MD5Util;
import com.lovegod.newbuy.utils.regex.RegexUtil;
import com.lovegod.newbuy.view.LoginActivity;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by ywx on 2017/7/10.
 */

public class LoginInfoRegisteredFragment extends Fragment implements View.OnClickListener{
    private RegisteredActivity activity;

    private static final int TAKE_PHOTO=1;
    private static final String TAG="LoginInfoRegisteredFragment";
    private TextInputEditText userNameEdit,passwdEdit,passwdConfirmEdit;
    private Button nextStepButton;
    private ImageView genderImage;
    private CircleImageView portraitImage;
    private boolean isMale=true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_login_info_registered,container,false);
        userNameEdit=(TextInputEditText)view.findViewById(R.id.registered_username);
        passwdEdit=(TextInputEditText)view.findViewById(R.id.registered_passwd);
        passwdConfirmEdit=(TextInputEditText)view.findViewById(R.id.registered_confirm_passwd);
        nextStepButton=(Button)view.findViewById(R.id.login_info_nextstep_button);
        genderImage=(ImageView)view.findViewById(R.id.gender_image);
        portraitImage=(CircleImageView)view.findViewById(R.id.portrait_image);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity= (RegisteredActivity) getActivity();
        nextStepButton.setOnClickListener(this);
        genderImage.setOnClickListener(this);
        portraitImage.setOnClickListener(this);
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new PicassoImageLoader());
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);//允许裁剪（单选才有效）
        imagePicker.setMultiMode(false);//设置单选
        imagePicker.setSaveRectangle(false); //是否按矩形区域保存
        imagePicker.setStyle(CropImageView.Style.CIRCLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }

    /**
     * 检测用户名的合法性
     * @param userName 用户名
     */
    private void checkUserName(final String userName) {
        if(TextUtils.isEmpty(userName)){
            setError(userNameEdit,"用户名为空");
        }else if(!RegexUtil.isUserName(userName)){
            setError(userNameEdit,"不合法的用户名");
        }else {
            //判断用户名是否存在
            NetWorks.getUserNameInfo(userName, new BaseObserver<User>() {
                @Override
                public void onHandleSuccess(User user) {
                    //这里通过就是都合格了
                    if (user == null) {
                        if(isMale){
                            activity.getUser().setGender("男");
                        }else{
                            activity.getUser().setGender("女");
                        }
                        activity.getUser().setUsername(userName);
                        commitLogin(activity.getUser());
                        Log.d(TAG,activity.getUser().toString());
                    } else {
                        setError(userNameEdit, "用户名已存在");
                    }
                }

                @Override
                public void onHandleError(User user) {

                }
            });
        }
    }

    /**
     * 检测密码和确认密码的合法性
     * @param passwd 密码
     * @param passwdConfrim 确认的密码
     * @return
     */
    private boolean checkPasswd(String passwd,String passwdConfrim) {
        if(TextUtils.isEmpty(passwd)){
            setError(passwdEdit,"密码为空");
            return false;
        }else if(!RegexUtil.isValidPasswd(passwd)){
            setError(passwdEdit,"不合法的密码");
            return false;
        }else if(TextUtils.isEmpty(passwdConfrim)){
            setError(passwdConfirmEdit,"请再次输入密码");
            return false;
        }else if(!passwd.equals(passwdConfrim)){
            setError(passwdConfirmEdit,"两次输入密码不一致");
            return false;
        }
        activity.getUser().setPassword(MD5Util.encode(passwd));
        return true;
    }

    private void setError(TextInputEditText editText,String error){
        editText.setError(error);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_info_nextstep_button:
                String userName=userNameEdit.getText().toString();
                String passwd=passwdEdit.getText().toString();
                String passwdConfirm=passwdConfirmEdit.getText().toString();
                if(checkPasswd(passwd,passwdConfirm)){
                    checkUserName(userName);
                }
                break;
            case R.id.gender_image:
                if(isMale){
                    genderImage.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.gender_female));
                    isMale=false;
                }else {
                    genderImage.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.gender_male));
                    isMale=true;
                }
                break;
            case R.id.portrait_image:
                openAlbum();
                break;
        }
    }

    private void openAlbum() {
        Intent intent=new Intent(getActivity(), ImageGridActivity.class);
        startActivityForResult(intent,TAKE_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case TAKE_PHOTO:
                if(resultCode==ImagePicker.RESULT_CODE_ITEMS)
                    if (data != null && requestCode == TAKE_PHOTO) {
                        ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        Log.d(TAG,"image path is: "+images.get(0).path);
                        activity.getUser().setHeaderpic(images.get(0).path);
                        portraitImage.setImageURI(Uri.parse(images.get(0).path));
                    }
                break;
        }
    }
    private void commitLogin(User user){
        Map<String,String> map=new HashMap<>();
        map.put("username",user.getUsername());
        map.put("password",user.getPassword());
        map.put("gender",user.getGender());
        map.put("phone",user.getPhone());
        if(!TextUtils.isEmpty(user.getRealname())) {
            map.put("name", user.getRealname());
        }
        if(!TextUtils.isEmpty(user.getIdnumber())){
            map.put("id", user.getIdnumber());
        }
        NetWorks.commitLoginInfo(map, new BaseObserver<User>(getActivity(),new ProgressDialog(getActivity())) {
            @Override
            public void onHandleSuccess(final User user) {
                if(user!=null) {

                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            try {   //注册环信账号
                                EMClient.getInstance().createAccount(user.getPhone(), user.getPassword());
                            } catch (HyphenateException e) {
                                e.printStackTrace();

                            }
                        }
                    }.start();

                    Toast.makeText(getActivity(),"注册成功，快登陆吧",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                }else {
                    Toast.makeText(getActivity(),"注册失败",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onHandleError(User user) {

            }
        });
    }
}
