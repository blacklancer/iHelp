package com.example.ihelp.Setting.View;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.ihelp.R;
import com.example.ihelp.Setting.Model.EmergencyContent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by 朱继涛 2019/11/4
 * 设置紧急情况向外界发送的求助信息
 */

public class SetEmergencyMsg extends AppCompatActivity {

    //定义初始控件
    private EditText name;
    private EditText phoneNum;
    private EditText msg;

    //判断此时是编辑模式还是浏览模式
    public boolean edit = false;

    EmergencyContent emergencyContent;

    private Button save;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_emergency_msg);

        initView();
        initData();
    }

    //初始化界面
    public void initView() {
        name = (EditText) findViewById(R.id.et_setPhoneNum_name);
        phoneNum = (EditText) findViewById(R.id.et_setPhoneNum_phoneNumber);
        msg = (EditText) findViewById(R.id.et_setPhoneNum_content);
        save = (Button) findViewById(R.id.bt_setPhoneNum_save);

        //初始化时将输入框设置为不可编辑，点击编辑按钮才可以进行保存
        setEditOrNot(false);
        edit = false;

        //modify = (Button)findViewById(R.id.bt_setPhoneNum_modify);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edit) {
                    setEditOrNot(true);
                    save.setText("保存");
                    edit = true;
                }else{
                    savaContent();
                    setEditOrNot(false);
                    save.setText("编辑");
                }
            }
        });

    }

    //初始化数据
    public void initData() {
        emergencyContent = getEmergencyContent();
        if (emergencyContent != null) {
            name.setText(emergencyContent.getEmergencyName());
            phoneNum.setText(emergencyContent.getEmergencyPhone());
            msg.setText(emergencyContent.getEmergencyMsg());
        }
    }

    //保存各项信息的函数，保存在emergency_content。xml里面
    public void savaContent() {
        emergencyContent = new EmergencyContent();
        if(!name.getText().toString().isEmpty() && !phoneNum.getText().toString().isEmpty() && !msg.getText().toString().isEmpty()) {
            emergencyContent.setEmergencyName(name.getText().toString());
            emergencyContent.setEmergencyPhone(phoneNum.getText().toString());
            emergencyContent.setEmergencyMsg(msg.getText().toString());

            SharedPreferences.Editor editor = getSharedPreferences("emergency_content", MODE_PRIVATE).edit();
            editor.putString("name", emergencyContent.getEmergencyName());
            editor.putString("phone", emergencyContent.getEmergencyPhone());
            editor.putString("msg", emergencyContent.getEmergencyMsg());
            editor.apply();
        }else{
            Toast.makeText(getApplicationContext(), "必要信息不能为空！", Toast.LENGTH_LONG).show();
        }
    }

    //从本地文件中读取先前设置的求助信息
    public EmergencyContent getEmergencyContent() {
        EmergencyContent emergencyContent = new EmergencyContent();
        SharedPreferences pref = getSharedPreferences("emergency_content", MODE_PRIVATE);
        emergencyContent.setEmergencyName(pref.getString("name", ""));
        emergencyContent.setEmergencyPhone(pref.getString("phone", ""));
        emergencyContent.setEmergencyMsg(pref.getString("msg", ""));
        return emergencyContent;
    }

    //封装起来的函数
    public void setEditOrNot(boolean type) {
        name.setEnabled(type);
        phoneNum.setEnabled(type);
        msg.setEnabled(type);
    }


}
