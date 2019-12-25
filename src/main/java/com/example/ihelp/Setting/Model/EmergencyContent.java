package com.example.ihelp.Setting.Model;

/**
 * Created by 朱继涛 2019/11/4
 * 对应紧急求助消息的实体类
 */

public class EmergencyContent {

    private String emergencyName;
    private String emergencyPhone;
    private String emergencyMsg;

    public void setEmergencyMsg(String emergencyMsg) {
        this.emergencyMsg = emergencyMsg;
    }

    public void setEmergencyName(String emergencyName) {
        this.emergencyName = emergencyName;
    }

    public void setEmergencyPhone(String emergencyPhone) {
        this.emergencyPhone = emergencyPhone;
    }

    public String getEmergencyMsg() {
        return emergencyMsg;
    }

    public String getEmergencyName() {
        return emergencyName;
    }

    public String getEmergencyPhone() {
        return emergencyPhone;
    }
}
