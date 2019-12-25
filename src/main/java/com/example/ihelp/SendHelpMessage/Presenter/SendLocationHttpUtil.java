package com.example.ihelp.SendHelpMessage.Presenter;

import android.util.Log;

import com.example.ihelp.CommonClass.HelpMessage;
import com.example.ihelp.CommonClass.HttpCallbackListener;
import com.example.ihelp.SendHelpMessage.Model.LocationInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendLocationHttpUtil {
    LocationInfo locationInfo;

    public SendLocationHttpUtil(LocationInfo locationInfo){
        this.locationInfo = locationInfo;
    }

    //使用okHttp
    public void sendRequestWithOkHttp(final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    MediaType mediaType = MediaType.parse("application/json; charset=UTF-8");
                    RequestBody requestBody = RequestBody.create(mediaType, locationInfo.toJson(locationInfo));

                    Request request = new Request.Builder()
                            // 指定访问的地址
                            .url("http://47.103.214.166:8080/iHelpWeb/location/update")
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
    public LocationInfo parseJSONWithGSON(String jsonData) {
        LocationInfo locationInfo = null;
        Gson gson = new Gson();
        //在传换成类地时候要看一看传递过来的是一个类，还是一个列表
        locationInfo = gson.fromJson(jsonData, new TypeToken<LocationInfo>(){}.getType());
        return locationInfo;
    }

}
