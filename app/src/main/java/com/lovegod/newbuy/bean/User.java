package com.lovegod.newbuy.bean;

import java.io.Serializable;

/**
 * 说明：
 * Author: lovegod
 * Date:  2017/5/9.
*/
public class User implements Serializable{

    private Integer uid;
    private String username;
    private String password;
    private String gender;
    private String phone;
    private String realname;
    private String idnumber;
    private String headerpic;

    public String getHeaderpic() {
        return headerpic;
    }

    public void setHeaderpic(String headerpic) {
        this.headerpic = headerpic;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +'\n'+
                ", username=" + username + '\n' +
                ", password=" + password + '\n' +
                ", gender=" + gender + '\n' +
                ", phone=" + phone + '\n' +
                ", realname=" + realname + '\n' +
                ", portraitpath=" + headerpic + '\n' +
                ", idnumber=" + idnumber + '}';
    }
}
