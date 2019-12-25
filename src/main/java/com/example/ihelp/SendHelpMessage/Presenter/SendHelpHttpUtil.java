package com.example.ihelp.SendHelpMessage.Presenter;

import android.util.Log;

import com.example.ihelp.CommonClass.HelpMessage;
import com.example.ihelp.CommonClass.HttpCallbackListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendHelpHttpUtil {
    private HelpMessage helpMessage;

    public SendHelpHttpUtil(HelpMessage helpMessage){
        this.helpMessage = helpMessage;
    }

    //使用okHttp
    public void sendRequestWithOkHttp(final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    MediaType mediaType = MediaType.parse("application/json; charset=UTF-8");
                    RequestBody requestBody = RequestBody.create(mediaType, helpMessage.toJson(helpMessage));

                    Request request = new Request.Builder()
                            // 指定访问的地址
                            .url("http://47.103.214.166:8080/iHelpWeb/help/add")
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

}
