package com.example.ihelp.ReceiveHelpMessage.Presenter;

import android.content.SharedPreferences;
import android.util.Log;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.example.ihelp.CommonClass.HelpMessage;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Filter {

    private SharedPreferences pref;
    private List<HelpMessage> helpMessages;
    private int level;

    public Filter(SharedPreferences pref, List<HelpMessage> helpMessages, int level){
        this.pref = pref;
        this.helpMessages = helpMessages;
        this.level = level;
    }

    public float getdistance(HelpMessage helpMessage){
        float distance = 0;
        float lat = pref.getFloat("lat",0);
        float lng = pref.getFloat("lng",0);
        LatLng latLng1 = new LatLng((double)lat,(double)lng);
        LatLng latLng2 = new LatLng((double)helpMessage.getLat(),(double)helpMessage.getLng());
        distance = AMapUtils.calculateLineDistance(latLng1,latLng2);

        return distance;
    }

    public List<HelpMessage> getHelpMessages(){
        int length = helpMessages.size();
        int iter = 0;
        for(int i=0; i<length; i++){
            if(helpMessages.get(iter).getState()==2 || helpMessages.get(iter).getLevel()<level || getdistance(helpMessages.get(iter))>1500 || helpMessages.get(iter).getPhone().equals(pref.getString("phone",""))){
                helpMessages.remove(iter);
                iter--;
            }
            iter++;
        }
        return helpMessages;
    }
}
