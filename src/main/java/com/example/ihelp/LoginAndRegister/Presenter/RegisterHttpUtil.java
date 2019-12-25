package com.example.ihelp.LoginAndRegister.Presenter;

import com.example.ihelp.CommonClass.HttpCallbackListener;
import com.example.ihelp.CommonClass.User;
import com.example.ihelp.LoginAndRegister.Model.RegisterInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.net.URLEncoder;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 朱继涛 2019/11/16
 * 注册行为http类
 */

public class RegisterHttpUtil {

    private User user;

    public RegisterHttpUtil(){}

    public RegisterHttpUtil(User user){
       this.user = user;
    }

    //使用okHttp
    public void sendRequestWithOkHttp(final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();

                    MediaType mediaType = MediaType.parse("application/json; charset=UTF-8");
                    RequestBody requestBody = RequestBody.create(mediaType, user.toJson(user));
                    //Log.d("XXXXX", user.toJson(user));

                    Request request = new Request.Builder()
                            // 指定访问的地址
                            .url("http://47.103.214.166:8080/iHelpWeb/user/register")
                            .post(requestBody)
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
    public RegisterInfo parseJSONWithGSON(String jsonData) {
        RegisterInfo registerInfo = null;
        Gson gson = new Gson();
        //在传换成类地时候要看一看传递过来的是一个类，还是一个列表
        registerInfo = gson.fromJson(jsonData, new TypeToken<RegisterInfo>(){}.getType());
        return registerInfo;
    }


}
