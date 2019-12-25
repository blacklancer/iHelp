package com.example.ihelp.EmergencySMS.Presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import com.example.ihelp.EmergencySMS.Model.LocationService;
import com.example.ihelp.EmergencySMS.Model.PressCounter;
import com.example.ihelp.Setting.Model.EmergencyContent;
import java.util.ArrayList;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 朱继涛 2019/11/4
 * 发送求助信息的核心处理类
 */

public class UrgencyProcessor implements PowerButtonWacther.OnPowerButtonPressedListener
//        ，LocationedListener
   {
       //下面是各个必要的组件
    private PressCounter mCounter;
    private Context mContext;
    private static final long[] pattern = { 1000, 500 };
    private Vibrator mVibrator;

    EmergencyContent emergencyContent = null;

   // private LocationService locationService;

       //构造函数
    public UrgencyProcessor(Context context) {
        //locationService = HelpApplication.getInstance().getLocationService();
        mCounter = PressCounter.getInstance();
        mContext = context;
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }


    //监听电源键按钮的消息，如果按键信息符合要求就发送信息
    @Override
    public void powerButtonPressed() {
        mCounter.add(System.currentTimeMillis());
        if (mCounter.check()) {
            mVibrator.vibrate(pattern, -1);// 不重复震动

            //先读取提前设置好的紧急求助信息
            emergencyContent = getEmergencyContent();
            Log.d("XXXX", emergencyContent.getEmergencyMsg());
            //发送信息
             sendMsg(emergencyContent.getEmergencyPhone(),emergencyContent.getEmergencyName(), emergencyContent.getEmergencyMsg());

//            locationService.setListener(this);
//            locationService.startLocate();
        }
    }


       /***
        * 发送短信msg到phoneNumber
        * @param phone
        * @param content
        */
       private void sendMsg(String phone, String name, String content) {
           //向指定的手机发送信息
           if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(phone)) {
               SmsManager manager = SmsManager.getDefault();
               ArrayList<String> strings = manager.divideMessage(content);
               for (int i = 0; i < strings.size(); i++) {
                   manager.sendTextMessage(phone, null, name+","+content, null, null);
               }
               Log.d("XXXX", "发送成功！");
           } else {
               Log.d("XXXX", "发送失败！");
           }
       }

       //从文件中读取设置的电话号码和信息
       public EmergencyContent getEmergencyContent() {
           EmergencyContent emergencyContent = new EmergencyContent();
           SharedPreferences pref = mContext.getSharedPreferences("emergency_content", MODE_PRIVATE);
           emergencyContent.setEmergencyName(pref.getString("name", ""));
           emergencyContent.setEmergencyPhone(pref.getString("phone", ""));
           emergencyContent.setEmergencyMsg(pref.getString("msg", ""));
           return emergencyContent;
       }

    @Override
    public void sendMsgOk() {
        long[] pattern = { 3000, 500 };
        mVibrator.vibrate(pattern, -1);
    }

    /***
     * 封装游标的奇葩方法
     * @param cursor
     * @param cloumn
     * @return
     */
    protected String getCursorString(Cursor cursor, String cloumn) {
        return cursor.getString(cursor.getColumnIndex(cloumn));
    }
}
