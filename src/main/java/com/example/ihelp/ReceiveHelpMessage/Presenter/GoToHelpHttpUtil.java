package com.example.ihelp.ReceiveHelpMessage.Presenter;

import com.example.ihelp.CommonClass.HttpCallbackListener;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GoToHelpHttpUtil {
    private int id;
    private int state;

    public GoToHelpHttpUtil(int id, int state){
        this.id = id;
        this.state = state;
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
                            .url("http://47.103.214.166:8080/iHelpWeb/help/updateState/?id="+id+"&state="+state)
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
