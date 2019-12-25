package com.example.ihelp.CommonClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 朱继涛 2019/11/28
 * 工具类，使用正则表达式验证输入的手机号，身份证号的合法性
 */

public class ValidInput {

    //判断字符串是不是合法的手机号的函数，如果是返回true，否则返回false
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean isMatch = false;
        // 制定验证条件，目前有些新卡会出现类似166开头的，需要适当调整
        String regex1 = "^[1][3,4,5,7,8][0-9]{9}$";
        //正式的合法的手机号比上面的那个更好
        String regex2 = "^((13[0-9])|(14[579])|(15([0-3,5-9]))|(16[6])|(17[0135678])|(18[0-9]|19[89]))\\d{8}$";

        p = Pattern.compile(regex2);
        m = p.matcher(str);
        isMatch = m.matches();
        return isMatch;
    }

    //验证输入的身份证形式是否正确
    public static boolean isIDNumber(String IDNumber) {
        if (IDNumber == null || "".equals(IDNumber)) {
            return false;
        }
        // 定义判别用户身份证号的正则表达式（18位，最后一位可以为字母X）
        String regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|";

        //假设18位身份证号码:41000119910101123X  410001 19910101 123X
        // 开头
        // [1-9] 第一位1-9中的一个      4
        // \\d{5} 五位数字           10001（前六位省市县地区）
        // (18|19|20)                19（现阶段可能取值范围18xx-20xx年）
        // \\d{2}                    91（年份）
        // ((0[1-9])|(10|11|12))     01（月份）
        // (([0-2][1-9])|10|20|30|31)01（日期）
        // \\d{3} 三位数字123（第十七位奇数代表男，偶数代表女）
        // [0-9Xx] 0123456789Xx其中的一个 X（第十八位为校验值）
        // $结尾

        boolean matches = IDNumber.matches(regularExpression);
        //判断第18位校验值
        if (matches) {
            if (IDNumber.length() == 18) {
                try {
                    char[] charArray = IDNumber.toCharArray();
                    //前十七位加权因子
                    int[] idCardWi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
                    //这是除以11后，可能产生的11位余数对应的验证码
                    String[] idCardY = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
                    int sum = 0;
                    for (int i = 0; i < idCardWi.length; i++) {
                        int current = Integer.parseInt(String.valueOf(charArray[i]));
                        int count = current * idCardWi[i];
                        sum += count;
                    }
                    char idCardLast = charArray[17];
                    int idCardMod = sum % 11;
                    if (idCardY[idCardMod].toUpperCase().equals(String.valueOf(idCardLast).toUpperCase())) {
                        return true;
                    } else {
                        //System.out.println("身份证最后一位:" + String.valueOf(idCardLast).toUpperCase() +
                         //       "错误,正确的应该是:" + idCardY[idCardMod].toUpperCase());
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //System.out.println("异常:" + IDNumber);
                    return false;
                }
            }
        }
        return matches;
    }

    public static void main(String[] args) {
//        String phone = "15216815759";
//        String phone1 = "12345";
//        String phone2 = "12345678910";
//
//        System.out.println(isMobile(phone));
//        System.out.println(isMobile(phone1));
//        System.out.println(isMobile(phone2));

//        String idCard = "41282919990322241X";
//        System.out.println(isIDNumber(idCard));
//
//        String idCard1 = "14010919981001053X";
//        System.out.println(isIDNumber(idCard1));
//
//        String idCard2 = "412829199903222411";
//        System.out.println(isIDNumber(idCard2));

    }

}

