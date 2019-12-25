package com.example.ihelp.LoginAndRegister.Presenter;

import com.example.ihelp.CommonClass.HttpCallbackListener;
import com.example.ihelp.LoginAndRegister.Model.IdentifyInfo;
import com.example.ihelp.LoginAndRegister.Model.LoginInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 朱继涛 2019/11/16
 * 登陆行为http类
 */

public class IdHttpUtil {

    public static String idcard;
    public static String name;

    public IdHttpUtil(){}

    public IdHttpUtil(String idcard, String name){
        this.idcard = idcard;
        this.name = name;
    }

    //使用okHttp
    public static void sendRequestWithOkHttp(final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            // 指定访问的地址
                            .url("http://47.103.214.166:8080/iHelpWeb/user/idIdentify/?idCard="+idcard+"&name="+name)
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
    public IdentifyInfo parseJSONWithGSON(String jsonData) {
        IdentifyInfo identifyInfo = null;
        Gson gson = new Gson();
        //在传换成类地时候要看一看传递过来的是一个类，还是一个列表
        identifyInfo = gson.fromJson(jsonData, new TypeToken<IdentifyInfo>(){}.getType());
        return identifyInfo;
    }


}
