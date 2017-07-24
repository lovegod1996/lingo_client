package com.lovegod.newbuy.view.myinfo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Address;
import com.lovegod.newbuy.bean.City;
import com.lovegod.newbuy.bean.District;
import com.lovegod.newbuy.bean.Province;
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.utils.regex.RegexUtil;
import com.lovegod.newbuy.utils.system.SpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeAddressActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int REQUEST_PROVINCE=1;
    private EditText nameEdit,phoneEdit,detailEdit,mailEdit;
    private CheckBox defaultCheckBox;
    private TextView sexEdit,provinceEdit,areaEdit;
    private Button commitButton;
    private OptionsPickerView sexOptions;
    private Toolbar toolbar;
    private User user;
    private Address address;
    //以下全是需要提交的信息
    private String name,sex,phone,province,area,detail,mail;
    private int status=0;
    private ArrayList<String>sexList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address);
        user= (User) SpUtils.getObject(this,"userinfo");
        address= (Address) getIntent().getSerializableExtra("edit_address");
        toolbar=(Toolbar)findViewById(R.id.change_address_toolbar);
        nameEdit = (EditText) findViewById(R.id.change_address_name_edit);
        phoneEdit = (EditText) findViewById(R.id.change_address_phone_edit);
        sexEdit = (TextView) findViewById(R.id.change_address_sex_edit);
        provinceEdit = (TextView) findViewById(R.id.change_address_province_edit);
        areaEdit = (TextView) findViewById(R.id.change_address_area_edit);
        detailEdit = (EditText) findViewById(R.id.change_address_detail_edit);
        mailEdit = (EditText) findViewById(R.id.change_address_mail_edit);
        commitButton = (Button) findViewById(R.id.change_address_button);
        defaultCheckBox=(CheckBox)findViewById(R.id.change_address_default);
        setSupportActionBar(toolbar);

        commitButton.setOnClickListener(this);
        sexEdit.setOnClickListener(this);
        provinceEdit.setOnClickListener(this);
        areaEdit.setOnClickListener(this);

        //初始化性别的选择控件
        initSexPicker();

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
         * 监听复选框的改变事件
         */
        defaultCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    status=1;
                }else {
                    status=0;
                }
            }
        });


        //获取编辑传过来的信息
        if(address!=null){
            nameEdit.setText(address.getName());
            phoneEdit.setText(address.getPhone());
            if(address.getSex()==0){
                sexEdit.setText("男");
            }else {
                sexEdit.setText("女");
            }
            String[] loc=address.getAddress().split(";");
            provinceEdit.setText(loc[0]);
            areaEdit.setText(loc[1]);
            detailEdit.setText(loc[2]);
            mailEdit.setText(address.getZip());
            if(address.getStatue()==0) {
                defaultCheckBox.setChecked(false);
                status=0;
            } else {
                defaultCheckBox.setChecked(true);
                defaultCheckBox.setClickable(false);
                defaultCheckBox.setText("已设置成默认地址");
                status=1;
            }
        }
    }

    private void initSexPicker() {
        sexList.clear();
        sexOptions=new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                sexEdit.setText(sexList.get(options1));
            }
        }).setContentTextSize(16)//设置滚轮文字大小
                .setDividerColor(Color.BLACK)//设置分割线的颜色
                .setBgColor(Color.parseColor("#EFEEEC"))
                .setTitleBgColor(Color.WHITE)
                .setCancelColor(Color.parseColor("#2196F3"))
                .setSubmitColor(Color.parseColor("#2196F3"))
                .setTextColorCenter(Color.BLACK)
                .setBackgroundId(Color.TRANSPARENT) //设置外部遮罩颜色
                .build();
        sexList.add("男");
        sexList.add("女");
        sexOptions.setPicker(sexList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change_address_sex_edit:
                //弹出性别选择
                sexOptions.show();
                break;
            case R.id.change_address_province_edit:
                //启动地区选择的活动
                startActivityForResult(new Intent(ChangeAddressActivity.this,ShowAreaActivity.class),REQUEST_PROVINCE);
                break;
            case R.id.change_address_area_edit:
                //启动地区选择的活动
                startActivityForResult(new Intent(ChangeAddressActivity.this,ShowAreaActivity.class),REQUEST_PROVINCE);
                break;
            case R.id.change_address_button:
                name=nameEdit.getText().toString();
                sex=sexEdit.getText().toString();
                phone=phoneEdit.getText().toString();
                province=provinceEdit.getText().toString();
                area=areaEdit.getText().toString();
                detail=detailEdit.getText().toString();
                mail=mailEdit.getText().toString();
                if(isInfoEmpty(name,sex,phone,province,area,detail,mail)){
                    return;
                }
                if(!judgeName(name)){
                    return;
                }
                if(!judgePhone(phone)){
                    return;
                }
                if(!judgeDetail(detail)){
                    return;
                }
                if(!judgeMail(mail)){
                    return ;
                }
                //如果是添加就提交，如果是编辑就更新
                if(address==null) {
                    doCommit();
                }else {
                    doUpdate();
                }
                break;
        }
    }

    /**
     * 执行更新操作
     */
    private void doUpdate() {
        Map<String,String>map=new HashMap<>();
        map.put("said",address.getSaid()+"");
        map.put("uid",user.getUid().toString());
        map.put("name",name);
        if(sex.equals("男")) {
            map.put("sex", "0");
        }else {
            map.put("sex","1");
        }
        map.put("phone",phone);
        map.put("address",province+";"+area+";"+detail);
        map.put("statue",status+"");
        map.put("zip",mail);
        NetWorks.editAddress(map, new BaseObserver<Address>(this,new ProgressDialog(this)) {
            @Override
            public void onHandleSuccess(Address address) {
                finish();
            }

            @Override
            public void onHandleError(Address address) {

            }
        });
    }

    /**
     * 执行提交操作
     */
    private void doCommit() {
        final Map<String,String>map=new HashMap<>();
        map.put("uid",user.getUid().toString());
        map.put("name",name);
        if(sex.equals("男")) {
            map.put("sex", "0");
        }else {
            map.put("sex","1");
        }
        map.put("phone",phone);
        map.put("address",province+";"+area+";"+detail);
        map.put("statue",status+"");
        map.put("zip",mail);
        NetWorks.getAddress(user.getUid(), new BaseObserver<List<Address>>(this) {
            @Override
            public void onHandleSuccess(List<Address> list) {
                //如果没有收货地址，控制此次提交要设置成默认
                if(list.size()==0&&status==0){
                    Toast.makeText(ChangeAddressActivity.this,"请设置默认地址",Toast.LENGTH_SHORT).show();
                }else {
                    NetWorks.addAddress(map, new BaseObserver<Address>(ChangeAddressActivity.this,new ProgressDialog(ChangeAddressActivity.this)) {
                        @Override
                        public void onHandleSuccess(Address address) {
                            finish();
                        }

                        @Override
                        public void onHandleError(Address address) {

                        }
                    });
                }
            }

            @Override
            public void onHandleError(List<Address> list) {

            }
        });
    }

    /**
     * 判断邮政编码是否正确
     * @param mail
     * @return
     */
    private boolean judgeMail(String mail) {
        if(RegexUtil.isMailNumber(mail)){
            return true;
        }
        Toast.makeText(this,"邮政编码不正确",Toast.LENGTH_SHORT).show();
        return  false;
    }

    /**
     * 判断地址是否正确
     * @param detail
     * @return
     */
    private boolean judgeDetail(String detail) {
        if(detail.length()>=5&&detail.length()<=60){
            return true;
        }
        Toast.makeText(this,"地址不正确",Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 判断手机号是否正确
     * @param phone
     * @return
     */
    private boolean judgePhone(String phone) {
        if(RegexUtil.isElevenNumber(phone)){
            return true;
        }
        Toast.makeText(this,"手机号不正确",Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 判断填写信息是否有空的
     * @param name
     * @param sex
     * @param phone
     * @param province
     * @param area
     * @param detail
     * @param mail
     * @return
     */
    private boolean isInfoEmpty(String name, String sex, String phone, String province, String area, String detail, String mail) {
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"请填写姓名",Toast.LENGTH_SHORT).show();
            return true;
        }else if(TextUtils.isEmpty(sex)){
            Toast.makeText(this,"请选择性别",Toast.LENGTH_SHORT).show();
            return true;
        }else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this,"请填写手机号",Toast.LENGTH_SHORT).show();
            return true;
        }else if(TextUtils.isEmpty(province)||TextUtils.isEmpty(area)){
            Toast.makeText(this,"请选择地区",Toast.LENGTH_SHORT).show();
            return true;
        }else if(TextUtils.isEmpty(detail)) {
            Toast.makeText(this, "请填写详细地址", Toast.LENGTH_SHORT).show();
            return true;
        }else if(TextUtils.isEmpty(mail)) {
            Toast.makeText(this, "请填写邮政编码", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    /**
     * 判断姓名是否正确
     * @param name 姓名
     * @return
     */
    private boolean judgeName(String name) {
        if(RegexUtil.isName(name)) {
            return true;
        }
        Toast.makeText(this,"姓名不正确",Toast.LENGTH_SHORT).show();
        return  false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_PROVINCE:
                if(resultCode==RESULT_OK){
                    String content=data.getStringExtra("area_data_return");
                    String []return_data=content.split(";");
                    provinceEdit.setText(return_data[0]);
                    areaEdit.setText(return_data[1]);
                }
                break;
        }
    }
}
