package com.example.ihelp.LoginAndRegister.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.ihelp.CommonClass.HttpCallbackListener;
import com.example.ihelp.CommonClass.MD5Util;
import com.example.ihelp.CommonClass.RandomCode;
import com.example.ihelp.CommonClass.SMSHttpUtil;
import com.example.ihelp.CommonClass.SMSInfo;
import com.example.ihelp.CommonClass.User;
import com.example.ihelp.CommonClass.ValidInput;
import com.example.ihelp.LoginAndRegister.Model.IdentifyInfo;
import com.example.ihelp.LoginAndRegister.Model.RegisterInfo;
import com.example.ihelp.LoginAndRegister.Presenter.IdHttpUtil;
import com.example.ihelp.LoginAndRegister.Presenter.RegisterHttpUtil;
import com.example.ihelp.R;

import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by 朱继涛 2018/6/24
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REGISTER_SUCCESS=1;
    private static final int IDCARD_EXITS=2;
    private static final int USER_EXITS=3;
    private static final int SERVICE_ERROR=4;
    private static final int SEND_SMS = 5;
    private static final int SMS_FAIL = 6;
    private static final int UPDATE_TEXT =7;
    private static final int ID_ERROR = 8;

    private RegisterHttpUtil registerHttpUtil = null;

    //实际发送的验证码
    private String realCode = "";
    private int waitTime =0;
    //用户输入的验证码
    private EditText phoneCode;

    private EditText userName;
    private EditText idCard;
    private EditText phoneNumber;
    private EditText passWord;
    private EditText confirmPassWord;
    private Button getSMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = (EditText)findViewById(R.id.et_registeractivity_username);
        idCard = (EditText)findViewById(R.id.et_registeractivity_idcard);
        phoneNumber = (EditText)findViewById(R.id.et_registeractivity_phoneNumber);
        passWord = (EditText)findViewById(R.id.et_registeractivity_password);
        confirmPassWord = (EditText)findViewById(R.id.et_registeractivity_comfirmPassword);

        phoneCode = (EditText)findViewById(R.id.et_registeractivity_phoneCodes);
        getSMS = (Button)findViewById(R.id.sendCode);

        initTime();
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_registeractivity_back: //返回登录页面
                Intent intent1 = new Intent(this, loginActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.sendCode:    //验证码的生成
                waitTime =61;
                String phone = phoneNumber.getText().toString().trim();
                realCode = "";
                realCode = RandomCode.getRandomCode();
                //Log.d("XXXXX", realCode);
                //使用拼接字符串的方式发送短信
                final SMSHttpUtil smsHttpUtil = new SMSHttpUtil(phone, realCode, "SMS_177540594");
                smsHttpUtil.sendRequestWithOkHttp(new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        Log.d("XXX", response);
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
            case R.id.bt_registeractivity_register:    //注册按钮
                //获取用户输入的用户名、身份证号，性别、手机号、密码、验证码
                String username =userName.getText().toString().trim();
                final String idcard = idCard.getText().toString().trim();
                String password = passWord.getText().toString().trim();
                String comfirmPass = confirmPassWord.getText().toString().trim();
                String phonenumber = phoneNumber.getText().toString().trim();
                String phonecode = phoneCode.getText().toString().toLowerCase();

                //注册验证，先判断输入的各项信息是否为空
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(phonenumber)
                        && !TextUtils.isEmpty(idcard)&& !TextUtils.isEmpty(comfirmPass)
                        && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(phonecode)) {
                    //判断输入的手机号和身份证号是否合法
                    if(ValidInput.isMobile(phonenumber) && ValidInput.isIDNumber(idcard)) {
                        //先判断两次密码是否一致
                        if (comfirmPass.equals(password)) {
                            //在判断验证码是否正确
                            if (phonecode.equals(realCode)) {
                                //将用户注册信息加入到数据库中
                                final User user = new User(username, idcard, MD5Util.getMD5(password), phonenumber);

                                final IdHttpUtil idHttpUtil = new IdHttpUtil(idcard, username);
                                idHttpUtil.sendRequestWithOkHttp(new HttpCallbackListener() {
                                    @Override
                                    public void onFinish(String response) {
                                        IdentifyInfo identifyInfo = idHttpUtil.parseJSONWithGSON(response);
                                        if(identifyInfo.getStatus().equals("01")){
                                            registerHttpUtil = new RegisterHttpUtil(user);
                                            registerHttpUtil.sendRequestWithOkHttp(new HttpCallbackListener() {
                                                //重写HttpCallbackListener接口的方法，对传递过来的数据进行操作
                                                @Override
                                                public void onFinish(String response) {
                                                    //新建一个Message对象，存储需要发送的消息
                                                    Message message = new Message();

                                                    RegisterInfo registerInfo = registerHttpUtil.parseJSONWithGSON(response);
                                                    //Log.d("XXXXX", String.valueOf(registerInfo.getErrorCode()));
                                                    if (registerInfo.getStatus().equals("ok")) {
                                                        //输出提示信息
                                                        message.what = REGISTER_SUCCESS;
                                                        handler.sendMessage(message);

                                                        Intent intent = new Intent(RegisterActivity.this, loginActivity.class);
                                                        startActivity(intent);
                                                        finish();  //销毁此Activity
                                                    } else if (registerInfo.getErrorCode() == 301) {
                                                        message.what = IDCARD_EXITS;
                                                        handler.sendMessage(message);
                                                    } else if (registerInfo.getErrorCode() == 302) {
                                                        message.what = USER_EXITS;
                                                        handler.sendMessage(message);
                                                    } else if (registerInfo.getErrorCode() == 300) {
                                                        message.what = SERVICE_ERROR;
                                                        handler.sendMessage(message);
                                                    }
                                                }
                                                @Override
                                                public void onError(Exception e) {
                                                    e.printStackTrace();
                                                }
                                            });
                                        }else {
                                            Message message = new Message();
                                            message.what = ID_ERROR;
                                            handler.sendMessage(message);
                                        }
                                    }

                                    @Override
                                    public void onError(Exception e) { }
                                });

//                                registerHttpUtil = new RegisterHttpUtil(user);
//                                registerHttpUtil.sendRequestWithOkHttp(new HttpCallbackListener() {
//                                    //重写HttpCallbackListener接口的方法，对传递过来的数据进行操作
//                                    @Override
//                                    public void onFinish(String response) {
//                                        //新建一个Message对象，存储需要发送的消息
//                                        Message message = new Message();
//
//                                        RegisterInfo registerInfo = registerHttpUtil.parseJSONWithGSON(response);
//                                        //Log.d("XXXXX", String.valueOf(registerInfo.getErrorCode()));
//                                        if (registerInfo.getStatus().equals("ok")) {
//                                            //输出提示信息
//                                            message.what = REGISTER_SUCCESS;
//                                            handler.sendMessage(message);
//
//                                            Intent intent = new Intent(RegisterActivity.this, loginActivity.class);
//                                            startActivity(intent);
//                                            finish();  //销毁此Activity
//                                        } else if (registerInfo.getErrorCode() == 301) {
//                                            message.what = IDCARD_EXITS;
//                                            handler.sendMessage(message);
//                                        } else if (registerInfo.getErrorCode() == 302) {
//                                            message.what = USER_EXITS;
//                                            handler.sendMessage(message);
//                                        } else if (registerInfo.getErrorCode() == 300) {
//                                            message.what = SERVICE_ERROR;
//                                            handler.sendMessage(message);
//                                        }
//                                    }
//                                    @Override
//                                    public void onError(Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                });

                            } else {
                                Toast.makeText(this, "验证码错误,注册失败！", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "两次密码不一致，请重新输入！", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(this, "手机号或者身份证号格式不正确！", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "未完善信息，注册失败", Toast.LENGTH_SHORT).show();
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
                case REGISTER_SUCCESS:
                    //在这里可以进行UI操作
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    break;
                case IDCARD_EXITS:
                    //在这里可以进行UI操作
                    Toast.makeText(RegisterActivity.this, "身份证号码已经存在！", Toast.LENGTH_SHORT).show();
                    break;
                case USER_EXITS:
                    //在这里可以进行UI操作
                    Toast.makeText(RegisterActivity.this, "该账号已经存在！", Toast.LENGTH_SHORT).show();
                    break;
                case SERVICE_ERROR:
                    //在这里可以进行UI操作
                    Toast.makeText(RegisterActivity.this, "服务器错误！", Toast.LENGTH_SHORT).show();
                    break;
                case SEND_SMS:
                    Toast.makeText(RegisterActivity.this, "验证码已发送！", Toast.LENGTH_SHORT).show();
                    break;
                case SMS_FAIL:
                    Toast.makeText(RegisterActivity.this, "验证码发送失败，请重新发送！", Toast.LENGTH_SHORT).show();
                    break;
                case ID_ERROR:
                    Toast.makeText(RegisterActivity.this, "身份证号不正确！", Toast.LENGTH_SHORT).show();
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



}

