package com.lovegod.newbuy.utils.Md5Util;
import java.security.MessageDigest;

/**
 * 说明：MD5加密工具
 * Author: lovegod
 * Date:  2017/5/10.
 */
public class MD5Util {

    private static String key="newbuy";
    /**
     * 加密工具
     * @param password 密码

     * @return  加密后的字符串
     */
    public static String encode(String password) {
        password = password + key;
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        char[] charArray = password.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}
