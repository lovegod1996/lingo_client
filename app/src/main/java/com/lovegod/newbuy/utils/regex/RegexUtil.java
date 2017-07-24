package com.lovegod.newbuy.utils.regex;

/**
 * Created by Administrator on 2017/7/9.
 * 正则表达式判断的工具类
 */

public class RegexUtil {
    public static boolean isElevenNumber(String number)
    {
        return number.matches("\\d{11}");
    }
    public static boolean isName(String name){return name.matches("[\u4e00-\u9fa5]{2,}");}
    public static boolean isIdentityNumber(String number) {
        if(number.matches("\\d{17}X")||number.matches("\\d{18}")){
            return true;
        }
        return false;
    }
    public static boolean isUserName(String userName){
        if(userName.length()>=4&&userName.length()<=20){
            if(userName.matches("[a-zA-Z\\d\u4e00-\u9fa5]*")){
                return true;
            }
            return false;
        }
        return false;
    }
    public static boolean isValidPasswd(String passwd){
        if(passwd.length()>=6){
            return true;
        }
        return false;
    }
    public static boolean isMailNumber(String number){
        return number.matches("\\d{6}");
    }
}
