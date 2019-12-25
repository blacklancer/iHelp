package com.example.ihelp.EmergencySMS.Presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by 朱继涛 2019/11/8
 * 设置接受手机开机广播的设置
 */

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent bootActivityIntent = new Intent(context,
                    PowerButtonWacther.class);
            //在非activity中startActivity需要加上下面的Flag
            bootActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startService(bootActivityIntent);
        }

    }

}
