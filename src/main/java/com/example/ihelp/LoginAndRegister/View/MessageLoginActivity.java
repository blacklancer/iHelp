package com.example.ihelp.LoginAndRegister.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ihelp.CommonClass.HttpCallbackListener;
import com.example.ihelp.CommonClass.RandomCode;
import com.example.ihelp.CommonClass.SMSHttpUtil;
import com.example.ihelp.CommonClass.SMSInfo;
import com.example.ihelp.CommonClass.ValidInput;
import com.example.ihelp.LoginAndRegister.Model.LoginInfo;
import com.example.ihelp.LoginAndRegister.Presenter.MessageLoginHttpUtil;
import com.example.ihelp.MainActivity;
import com.example.ihelp.R;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by 朱继涛 2019/10/24
 * 短信验证码登录界面
 */

public class MessageLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int LOGIN_SUCCESS=1;
    private static final int USER_NULL=2;
    private static final int SEND_SMS=3;
    private static final int SMS_FAIL=4;
    private static final int UPDATE_TEXT =5;

    private EditText userName;           //此时的用户名指的是手机号
    private EditText passWord;           //使用短信验证码登陆密码为密码
    private Button login;
    private Button getSMS;


    private String realCode;             //实际的验证码
    private int waitTime =0;

    private MessageLoginHttpUtil messageLoginHttpUtil;

    //测试使用的函数
    public static void main(String[] args) {
        //SendSms.sendsms();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_login_activity);

        userName = (EditText)findViewById(R.id.et_messageloginactivity_username);
        passWord = (EditText)findViewById(R.id.et_messageloginactivity_password);
        login = (Button)findViewById(R.id.bt_messageloginactivity_login);
        getSMS = (Button)findViewById(R.id.sendMsg);

        initTime();

        userName.setText("15216815759");
    }

    //各个点击时间的实现
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_messageloginactivity_back:
                //TODO 返回箭头功能
                finish();  //销毁此Activity
                break;
            case R.id.sendMsg:
                //TODO 返回箭头功能
                waitTime = 61;

                String phone = userName.getText().toString().trim();
                realCode = RandomCode.getRandomCode();
                //Log.d("XXXXX", realCode);
                //使用拼接字符串的方式发送短信
                final SMSHttpUtil smsHttpUtil = new SMSHttpUtil(phone, realCode,"SMS_177540595");
                smsHttpUtil.sendRequestWithOkHttp(new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                       //Log.d("XXX", response);
                        //新建一个Message对象，存储需要发送的消息
                        SMSInfo smsInfo = smsHttpUtil.parseJSONWithGSON(response);
                        Message message = new Message();
                        if(smsInfo.getCode().equals("OK")){
                            message.what = SEND_SMS;
                        }else {
                            message.what = SMS_FAIL;
                        }
                        handler.sendMessage(message);
                    }
                    @Override
                    public void onError(Exception e) { }
                });

                break;
            case R.id.bt_messageloginactivity_login:
                //获取输入的值
                String name = userName.getText().toString().trim();
                String password = passWord.getText().toString().trim();

                //先判断是否为空
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
                    if(ValidInput.isMobile(name)) {
                        //先判断验证码是否正确
                        if (realCode.equals(password)) {
                            messageLoginHttpUtil = new MessageLoginHttpUtil(name);
                            messageLoginHttpUtil.sendRequestWithOkHttp(new HttpCallbackListener() {
                                //重写HttpCallbackListener接口的方法，对传递过来的数据进行操作
                                @Override
                                public void onFinish(String response) {
                                    //新建一个Message对象，存储需要发送的消息
                                    Message message = new Message();

                                    LoginInfo loginInfo = messageLoginHttpUtil.parseJSONWithGSON(response);
                                    // Log.d("XXXXX", String.valueOf(loginInfo.getErrorCode()));
                                    if (loginInfo.getStatus().equals("ok")) {
                                        //输出提示信息
                                        message.what = LOGIN_SUCCESS;
                                        handler.sendMessage(message);
                                        //先将读取到的用户信息存入本地，便于修改
                                        saveUserInfo(loginInfo);

                                        Intent intent = new Intent(MessageLoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();  //销毁此Activity
                                    } else {
                                        message.what = USER_NULL;
                                        handler.sendMessage(message);
                                    }
                                }

                                @Override
                                public void onError(Exception e) { e.printStackTrace(); }
                            });

                        } else {
                            Toast.makeText(this, "验证码不正确，请重新输入", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(this, "手机格式不正确！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请输入你的用户名或验证码", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //输出提示信息
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case LOGIN_SUCCESS:
                    //在这里可以进行UI操作
                    Toast.makeText(MessageLoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    break;
                case USER_NULL:
                    //在这里可以进行UI操作
                    Toast.makeText(MessageLoginActivity.this, "用户名不存在，请重新输入", Toast.LENGTH_SHORT).show();
                    break;
                case SEND_SMS:
                    Toast.makeText(MessageLoginActivity.this, "验证码已发送！", Toast.LENGTH_SHORT).show();
                    break;
                case SMS_FAIL:
                    Toast.makeText(MessageLoginActivity.this, "验证码发送失败，请重新发送！", Toast.LENGTH_SHORT).show();
                    break;
                case UPDATE_TEXT:
                    if(waitTime>0) {
                        getSMS.setText(waitTime+"S后重新获取");
                        getSMS.setEnabled(false);
                    } else {
                        getSMS.setText("发送验证码");
                        getSMS.setEnabled(true);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    //定时器，用来限制验证码的重新发送
    public void initTime() {
        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                /** TODO Auto-generated method stub*/
                handler.sendEmptyMessage(UPDATE_TEXT);
                if (waitTime == 0)
                    return;
                waitTime--;
            }
        };
        Timer mTimer = new Timer();
        mTimer.schedule(mTimerTask, 1000, 1000);
    }


    //将读取到的用户信息存入本地userInfo.xml中
    public void saveUserInfo(LoginInfo loginInfo){
        SharedPreferences.Editor editor = getSharedPreferences("userInfo", MODE_PRIVATE).edit();
        editor.putString("name", loginInfo.getUser().getName());
        editor.putString("idcard", loginInfo.getUser().getIdCard());
        editor.putString("password", loginInfo.getUser().getPassword());
        editor.putString("phone", loginInfo.getUser().getPhone());
        editor.apply();
    }

}
