package com.example.ihelp.LoginAndRegister.Model;

import com.example.ihelp.CommonClass.User;

import java.io.Serializable;

/**
 * Created by 朱继涛 2019/10/24
 * 实名注册的返回的信息
 */

public class IdentifyInfo implements Serializable{

//             "status": "01",                          /*状态码:详见状态码说明 01 通过，02不通过  */
//             "msg": "实名认证通过！",                 /*提示信息*/
//             "idCard": "511126199511111111",          /*身份证号*/
//             "name": "张三",                          /*姓名*/
//             "sex": "男",                             /*性别*/
//             "area": "四川省乐山市夹江县",            /*身份证所在地(参考)*/
//             "province": "四川省",                    /*省(参考)*/
//             "city": "乐山市",                        /*市(参考)*/
//             "prefecture": "夹江县",                  /*区县(参考)*/
//             "birthday": "1995-11-11",                /*出生年月*/
//             "addrCode": "511126",                    /*地区代码*/
//             "lastCode": "1"                          /*身份证校验码*/

    private String status;
    private String msg;
    private String idCard;
    private String name;
    private String sex;
    private String area;
    private String province;
    private String city;
    private String prefecture;
    private String birthday;
    private String addrCode;
    private String lastCode;

    public String getSex() {
        return sex;
    }

    public String getIdCard() {
        return idCard;
    }

    public String getMsg() {
        return msg;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public String getArea() {
        return area;
    }

    public String getAddrCode() {
        return addrCode;
    }

    public String getName() {
        return name;
    }

    public String getPrefecture() {
        return prefecture;
    }

    public String getStatus() {
        return status;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getLastCode() {
        return lastCode;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddrCode(String addrCode) {
        this.addrCode = addrCode;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setPrefecture(String prefecture) {
        this.prefecture = prefecture;
    }

    public void setLastCode(String lastCode) {
        this.lastCode = lastCode;
    }
}
