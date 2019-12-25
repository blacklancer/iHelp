package com.example.ihelp.CommonClass;

/**
 * Created by 朱继涛 2019/11/16
 * 服务器回调接口
 */

//处理从服务器中查询而来的数据的接口，在主函数中来调用
public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);
}
