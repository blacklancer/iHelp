package com.example.ihelp.SafetyKnowledge.Presenter;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by 朱继涛 2019/11/2
 * 从本地获取json字符的类，json的目录为src/main/assets
 */

public class GetJsonDataUtil {

    public String getJson(Context context, String fileName){
        StringBuilder stringBuilder = new StringBuilder();
        try{
            AssetManager assetManager = context.getAssets();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bufferedReader.readLine())!=null){
                stringBuilder.append(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
