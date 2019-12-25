package com.example.ihelp.CommonClass;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 朱继涛 2019/11/28
 * MD5加密的类，工具类
 */

public class MD5Util {

    public static void main(String[] args) {
//        System.out.println(getMD5("123456"));
//        System.out.println(getMD5("123456").length());
    }

    //将字符串使用MD5进行加密,得到加密之后的字符串
    public static String getMD5(String content) {
        try {
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] result = digest.digest(content.getBytes());
            StringBuffer buffer = new StringBuffer();
            // 把每一个byte 做一个与运算 0xff;
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }
            // 标准的md5加密后的结果
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }






}
