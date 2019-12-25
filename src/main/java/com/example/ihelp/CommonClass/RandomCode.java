package com.example.ihelp.CommonClass;

/**
 * Created by 朱继涛 2019/10/24
 * 生成随机5位验证码
 */

public class RandomCode {

    public static String getRandomCode() {
        String randomCode = "";
        for(int i=0; i<4; i++){
            randomCode = randomCode + String.valueOf((int)(1+Math.random()*(9-1+1)));
        }
        return randomCode;
    }

}
