package com.example.ihelp.CommonClass;


import com.google.gson.Gson;
import java.io.Serializable;

/**
 * Created by 朱继涛 2019/10/24
 * 用户的基本注册信息
 */

public class User implements Serializable {

    //{"status":"error","errorCode":2,"user":{"id":null,"name":null,"idcard":null,"password":null,"phone":null}}

    private int id;
    private String name;            //用户名称
    private String idCard;          //身份证号
    private String phone;           //手机号，也就是账号
    private String password;        //密码，也可以是手机验证码

    public User(String name,  String idCard, String password, String phone) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.idCard = idCard;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }


    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String toJson(User user){

        Gson gson = new Gson();
        String str = gson.toJson(user);
//        JSONObject json = JSONObject.fromObject(user);//将java对象转换为json对象
//        String str = json.toString();//将json对象转换为字符串
        return str;
    }
}
