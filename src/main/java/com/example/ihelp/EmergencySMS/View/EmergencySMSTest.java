package com.example.ihelp.EmergencySMS.View;

import android.content.Intent;
import android.os.Bundle;
import com.example.ihelp.EmergencySMS.Presenter.PowerButtonWacther;
import com.example.ihelp.R;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by 朱继涛 2019/11/8
 * 设置发送紧急信息的测试
 */

public class EmergencySMSTest extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_main_activity);

        //startService(new Intent(this, PowerButtonWacther.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //startService(new Intent(this, PowerButtonWacther.class));
    }
}
