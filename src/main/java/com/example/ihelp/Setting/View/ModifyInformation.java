package com.example.ihelp.Setting.View;

import android.content.SharedPreferences;
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
import com.example.ihelp.LoginAndRegister.View.RegisterActivity;
import com.example.ihelp.R;
import com.example.ihelp.Setting.Model.ModifyInfo;
import com.example.ihelp.Setting.Presenter.ModifyHttpUtil;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by 朱继涛 2019/11/4
 * 修改个人信息的界面
 */

public class ModifyInformation extends AppCompatActivity implements View.OnClickListener {

    private static final int MODIFY_SUCCESS=1;
    private static final int PHONE_EXITS=2;
    private static final int SERVICE_ERROR=3;
    private static final int SEND_SMS = 4;
    private static final int SMS_FAIL = 5;
    private static final int UPDATE_TEXT =6;

    //各种控件
    private EditText name;
    private EditText idcard;
    private EditText phonenum;
    private EditText newpassword;
    private EditText cnewpassword;
    private EditText phonecode;
    private Button modify;
    private Button getSMS;

    private String realCode = "";
    private int waitTime = 0;
    private ModifyHttpUtil modifyHttpUtil;

    private String pass = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_information);

        initView();
        initData();
        initTime();
    }

    public void initView(){
        name = (EditText)findViewById(R.id.et_modify_userName);
        idcard = (EditText)findViewById(R.id.et_modify_idcard);
        phonenum = (EditText)findViewById(R.id.et_modify_phoneNumber);
        newpassword = (EditText)findViewById(R.id.et_modify_new_password);
        cnewpassword = (EditText)findViewById(R.id.et_modify_connew_password);
        phonecode = (EditText)findViewById(R.id.et_modify_phoneCodes);

        getSMS = (Button)findViewById(R.id.modify_msg);
        modify = (Button)findViewById(R.id.bt_modify_confirm);
    }

    //初始化数据
    public void initData(){
        //从本地文件中读取先前设置的求助信息
            SharedPreferences pref = getSharedPreferences("userInfo", MODE_PRIVATE);
            pass = pref.getString("password", "");
            phonenum.setText(pref.getString("phone", ""));
            name.setText(pref.getString("name", ""));
            idcard.setText(pref.getString("idcard",""));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.modify_msg:
                //TODO 发送短信验证码
                waitTime = 61;
                String phone = phonenum.getText().toString().trim();
                realCode = "";
                realCode = RandomCode.getRandomCode();
                Log.d("XXXXX", realCode);
                //使用拼接字符串的方式发送短信
                final SMSHttpUtil smsHttpUtil = new SMSHttpUtil(phone, realCode,"SMS_179290554");
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
            case R.id.bt_modify_confirm:   //忘记密码
                //TODO 确认修改信息
                //将数据保存入数据库，然后提示修改成功
                String userName = name.getText().toString().trim();
                String idCard = idcard.getText().toString().trim();
                String phoneNumber = phonenum.getText().toString().trim();
                String newPassWord = newpassword.getText().toString().trim();
                String cnewPassWord = cnewpassword.getText().toString().trim();
                String phoneCode = phonecode.getText().toString().toLowerCase();

                //注册验证，先判断是否为空
                if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(phoneNumber)
                        && !TextUtils.isEmpty(idCard)
                        && !TextUtils.isEmpty(newPassWord)&& !TextUtils.isEmpty(cnewPassWord)
                        && !TextUtils.isEmpty(phoneCode)) {
                    //判断手机号是否合法
                    if(ValidInput.isMobile(phoneNumber)) {
                        //判断新旧密码是否一致
                        if(!pass.equals(MD5Util.getMD5(newPassWord))) {
                            //输入的两次新密码是否一致
                            if (newPassWord.equals(cnewPassWord)) {
                                Log.d("XXXXX", realCode + phoneCode);
                                //在判断验证码是否正确
                                if (phoneCode.equals(realCode)) {
                                    //将用户注册信息加入到数据库中
                                    final User user = new User(userName, idCard, MD5Util.getMD5(newPassWord), phoneNumber);
                                    //Log.d("XXX", user.toJson(user));

                                    modifyHttpUtil = new ModifyHttpUtil(user);
                                    modifyHttpUtil.sendRequestWithOkHttp(new HttpCallbackListener() {
                                        //重写HttpCallbackListener接口的方法，对传递过来的数据进行操作
                                        @Override
                                        public void onFinish(String response) {
                                            //新建一个Message对象，存储需要发送的消息
                                            Message message = new Message();

                                            ModifyInfo modifyInfo = modifyHttpUtil.parseJSONWithGSON(response);
                                            Log.d("XXXXX", String.valueOf(modifyInfo.getErrorCode()));
                                            if (modifyInfo.getStatus().equals("ok")) {
                                                //输出提示信息
                                                message.what = MODIFY_SUCCESS;
                                                handler.sendMessage(message);
                                                //将修改后的信息存到本地
                                                saveUserInfo(user);
                                            } else if (modifyInfo.getErrorCode() == 400) {
                                                message.what = SERVICE_ERROR;
                                            } else if (modifyInfo.getErrorCode() == 401) {
                                                message.what = PHONE_EXITS;
                                            }
                                            handler.sendMessage(message);
                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            e.printStackTrace();
                                        }
                                    });
                                } else {
                                    Toast.makeText(this, "验证码错误,修改失败！", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(this, "输入的两次密码不匹配！", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(this, "新旧密码不能一样！", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(this, "手机号格式不正确！", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "未完善信息，修改失败！", Toast.LENGTH_SHORT).show();
                }
                break;
                default:
                    break;
        }
    }


    //输出提示信息
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MODIFY_SUCCESS:
                    //在这里可以进行UI操作
                    Toast.makeText(ModifyInformation.this, "修改成功！", Toast.LENGTH_SHORT).show();
                    break;
                case PHONE_EXITS:
                    //在这里可以进行UI操作
                    Toast.makeText(ModifyInformation.this, "该手机号已经存在！", Toast.LENGTH_SHORT).show();
                    break;
                case SERVICE_ERROR:
                    //在这里可以进行UI操作
                    Toast.makeText(ModifyInformation.this, "服务器错误！", Toast.LENGTH_SHORT).show();
                    break;
                case SEND_SMS:
                    Toast.makeText(ModifyInformation.this, "验证码已发送！", Toast.LENGTH_SHORT).show();
                    break;
                case SMS_FAIL:
                    Toast.makeText(ModifyInformation.this, "验证码发送失败，请重新发送！", Toast.LENGTH_SHORT).show();
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


    //将修改之后的用户信息存入本地userInfo.xml中
    public void saveUserInfo(User user){
        SharedPreferences.Editor editor = getSharedPreferences("userInfo", MODE_PRIVATE).edit();
        editor.putString("name", user.getName());
        editor.putString("idcard", user.getIdCard());
        editor.putString("password", user.getPassword());
        editor.putString("phone", user.getPhone());
        editor.apply();
    }

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
