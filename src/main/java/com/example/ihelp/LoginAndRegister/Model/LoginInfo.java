package com.example.ihelp.LoginAndRegister.Model;

import com.example.ihelp.CommonClass.User;

import java.io.Serializable;

/**
 * Created by 朱继涛 2019/10/24
 * 登陆界面返回的信息
 */

public class LoginInfo implements Serializable{


    //{"status":"error","errorCode":2,"user":{"id":null,"name":null,,"password":null,"phone":null}}

    private String status;
    private Integer errorCode;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
