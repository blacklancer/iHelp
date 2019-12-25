package com.example.ihelp.CommonClass;


/**
 * Created by 朱继涛 2019/12/2
 * 发送验证码返回的信息
 */

public class SMSInfo {

    //{"Message":"OK","RequestId":"724A3FA0-A73F-43BF-B517-1497945B4DCC","BizId":"294024575282207063^0","Code":"OK"}

    private String Message;
    private String RequestId;
    private String BizId;
    private String Code;

    public String getCode() {
        return Code;
    }

    public String getBizId() {
        return BizId;
    }

    public String getMessage() {
        return Message;
    }

    public String getRequestId() {
        return RequestId;
    }

    public void setCode(String code) {
        Code = code;
    }

    public void setBizId(String bizId) {
        BizId = bizId;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public void setRequestId(String requestId) {
        RequestId = requestId;
    }
}
