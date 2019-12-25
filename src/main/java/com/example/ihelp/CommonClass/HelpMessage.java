package com.example.ihelp.CommonClass;

import com.amap.api.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.Date;

/**
 * Created by 朱继涛 2019/11/14
 * 四个模块之一的收到的求助信息的实体类
 */

public class HelpMessage {
    private String name;
    private String phone;
    private String title;
    private String content;
    private float latitude;
    private float longitude;
    private int level;
    private int state;
    private long time;
    private String address;
    private int id;


    public HelpMessage(String name, String phone, String title, String content, float latitude, float longitude, int level, String address){
        this.name = name;
        this.phone = phone;
        this.title = title;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.level = level;
        this.state = 0;
        this.time = new Date().getTime();
        this.address = address;
    }

    public String getName(){return name;}

    public String getPhone(){return phone;}

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public float getLat() { return latitude; }

    public float getLng() { return longitude; }

    public int getLevel() {
        return level;
    }

    public int getState(){ return state; }

    public long getTime(){return  time;}

    public String getAddress(){return address;}

    public int getId(){return id;}

    public void setName(String name){
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLat(float lat) {
        this.latitude = latitude;
    }

    public void setLng(float lng) {
        this.longitude = longitude;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setState(int state) {this.state = state; }

    public void setTime(long time) {this.time = time;}

    public void setAddress(String address){this.address = address;}

    public void setId(int id){this.id = id;}

    public String toJson(HelpMessage helpMessage){

        Gson gson = new Gson();
        String str = gson.toJson(helpMessage);
        return str;
    }
}
