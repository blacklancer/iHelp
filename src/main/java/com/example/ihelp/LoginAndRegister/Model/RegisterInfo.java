package com.example.ihelp.LoginAndRegister.Model;

import com.example.ihelp.CommonClass.User;

/**
 * Created by 朱继涛 2019/10/24
 * 注册界面返回的信息
 */

public class RegisterInfo {

    private String status;         //一共四种情况
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
