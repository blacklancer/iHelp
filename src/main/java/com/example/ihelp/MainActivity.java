package com.example.ihelp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.ihelp.EmergencySMS.Presenter.PowerButtonWacther;
import com.example.ihelp.ReceiveHelpMessage.View.MessageList;
import com.example.ihelp.SafetyKnowledge.View.Safeknowledge;
import com.example.ihelp.SendHelpMessage.View.MapActivity;
import com.example.ihelp.Setting.View.MySetting;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by 朱继涛 2019/10/23
 */

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{

    //定义组件
    private RadioGroup rg_tab_bar;
    private RadioButton rb_channel;

    //接受到的求助信息模块
    public MessageList messageList;
    //地图显示模块
    public MapActivity mapActivity;
    //安全知识模块
    public Safeknowledge safeknowledge;
    //个人设置模块
    private MySetting mySetting;

    private FragmentManager fManager;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //开始监听电源键的service
        startService(new Intent(this, PowerButtonWacther.class));
        //Log.d("XXXX", "添加成功");

        //创建fragment对象
        fManager = getSupportFragmentManager();

        //绑定RadioGroup
        rg_tab_bar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        rg_tab_bar.setOnCheckedChangeListener(this);

        //获取第一个单选按钮，并设置其为选中状态
        rb_channel = (RadioButton) findViewById(R.id.rb_channel);
        rb_channel.setChecked(true);

        //请求手机位置信息权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Request();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (checkedId){
            case R.id.rb_channel:
                if(mapActivity == null){
                    mapActivity = new MapActivity();
                    fTransaction.add(R.id.ly_content, mapActivity );
                }else{
                    fTransaction.show(mapActivity);
                }
                break;
            case R.id.rb_message:
                if(messageList == null){
                    messageList = new MessageList();
                    fTransaction.add(R.id.ly_content,messageList);
                }else{
                    fTransaction.show(messageList);
                }
                break;
            case R.id.rb_better:
                if(safeknowledge == null){
                    safeknowledge = new Safeknowledge();
                    fTransaction.add(R.id.ly_content,safeknowledge);
                }else{
                    fTransaction.show(safeknowledge);
                }
                break;
            case R.id.rb_setting:
                if(mySetting == null){
                    mySetting = new MySetting();
                    fTransaction.add(R.id.ly_content,mySetting);
                }else{
                    fTransaction.show(mySetting);
                }
                break;
        }
        fTransaction.commit();
    }

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(mapActivity != null) fragmentTransaction.hide(mapActivity);
        if(messageList != null) fragmentTransaction.hide(messageList);
        if(safeknowledge != null) fragmentTransaction.hide(safeknowledge);
        if(mySetting != null) fragmentTransaction.hide(mySetting);
    }

    /**
     * 获取地理位置权限
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    void Request(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case 1:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO request success
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Log.d("XXXX", "destory");
        startService(new Intent(this, PowerButtonWacther.class));
    }

}
