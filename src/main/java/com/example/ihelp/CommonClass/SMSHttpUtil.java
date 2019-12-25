package com.example.ihelp.CommonClass;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 朱继涛 2019/11/28
 * 向服务器发送短信的请求，并且传递参数
 */

public class SMSHttpUtil {

    private String phone = "";
    private String code = "";
    private String mode = "";

    public SMSHttpUtil(String phone, String code,String mode){
        this.phone = phone;
        this.code = code;
        this.mode = mode;
    }

    //使用okHttp
    public void sendRequestWithOkHttp(final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            // 指定访问的地址
                            .url("http://47.103.214.166:8080/iHelpWeb/sendSms/?phone="+phone+"&code="+code+"&mode="+mode)
                            .build();
                    //接受返回的信息并进行处理
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();

                    if (listener != null) {
                        // 回调onFinish()方法
                        listener.onFinish(responseData);
                    }

                    //parseJSONWithGSON(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //将传递过来的字符串处理一下,然后判断一下要返回的类型
    public SMSInfo parseJSONWithGSON(String jsonData) {
        SMSInfo smsInfo = null;
        Gson gson = new Gson();
        //在传换成类地时候要看一看传递过来的是一个类，还是一个列表
        smsInfo = gson.fromJson(jsonData, new TypeToken<SMSInfo>(){}.getType());
        return smsInfo;
    }


}
