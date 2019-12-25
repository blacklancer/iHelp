package com.example.ihelp.SafetyKnowledge.Presenter;


import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by 朱继涛 on 2019/10/31.
 * 读取本地文件信息，主要是安全知识
 * 现在已经弃用，2019/11/3
 */

public class FileUtil {
    //读取文件内容的方法
    public static String read(Context ctx, String filename) {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = ctx.openFileInput(filename+".txt");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

    //写入文件的方法，也可能用不到，因为原有的安全知识需要提前存入
    public static void write(Context ctx,String msg,String filename){
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = ctx.openFileOutput(filename+".txt", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(msg);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    //删除文件的方法，可能不会用到
    public static void deleteFile(Context ctx,String filename){
        try{
            Log.v("deleteFile", "初始化本地"+filename+"成功");
            ctx.deleteFile(filename+".txt");
        }catch(Exception e){
            Log.v("deleteFile", "初始化本地"+filename+"失败");
        }
    }

}

