package com.example.ihelp.Setting.View;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.example.ihelp.R;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by 朱继涛 2019/11/4
 * 设置消息提醒等级的界面
 */

public class SetMessagePrompt extends AppCompatActivity {

    private RadioGroup radgroup;
    private Button savePrompt;

    //设置消息提醒的级别
    private int level;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_message_prompt);

        initView();
        initData();
    }

    public void initView(){
        radgroup = (RadioGroup) findViewById(R.id.radioGroup);
        savePrompt = (Button)findViewById(R.id.bt_setmessageprompt_save);
        //第一种获得单选按钮值的方法
        //为radioGroup设置一个监听器:setOnCheckedChanged()
        radgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //将获取的点击按钮减去2131165333再加1得到等级
                level = checkedId - 2131165330 + 1;
                //通过获取的数据来进行设置
                //Toast.makeText(getApplicationContext(), "按钮组值发生改变,你选了" + level, Toast.LENGTH_LONG).show();
            }
        });

        savePrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savaLevel();
            }
        });

    }

    public void initData(){
        level = getLevel();
        //Toast.makeText(getApplicationContext(), level, Toast.LENGTH_LONG).show();

        //从prompt。xml中读取到级别，然后显示到当前的RadioGroup中
        radgroup.check(level + 2131165330 - 1);
    }


    //保存当前选择的信息提示级别，保存在prompt。xml里面
    public void savaLevel() {
        SharedPreferences.Editor editor = getSharedPreferences("prompt_level", MODE_PRIVATE).edit();
        editor.putInt("level", level);
        editor.apply();
    }

    //获取当前保存的信息提示级别
    public int getLevel(){
        SharedPreferences pref = getSharedPreferences("prompt_level", MODE_PRIVATE);
        int temp =pref.getInt("level", 0);
        return temp;
    }


}
