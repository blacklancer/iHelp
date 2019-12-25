package com.example.ihelp.SendHelpMessage.Model;

import com.amap.api.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.Date;

/**
 * 用户地理位置信息类
 */


public class LocationInfo {
    private String phone;
    private float latitude;
    private float longitude;
    private long time;

    public LocationInfo(String phone, float latitude, float longitude){
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = new Date().getTime();
    }

    public String getPhone(){return phone;}

    public float getLat(){return latitude;}

    public float getLng() {return longitude; }

    private long getTime(){return  time;}

    public void setPhone(String phone){this.phone = phone;}

    public void setLat(float latitude){this.latitude = latitude;}

    public void setLng(float longitude) {this.longitude = longitude;}

    public void setTime(long time) {this.time = time;}

    public String toJson(LocationInfo locationInfo){

        Gson gson = new Gson();
        String str = gson.toJson(locationInfo);
        return str;
    }

}
