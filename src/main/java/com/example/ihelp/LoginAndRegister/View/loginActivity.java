package com.example.ihelp.LoginAndRegister.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.ihelp.CommonClass.HttpCallbackListener;
import com.example.ihelp.CommonClass.MD5Util;
import com.example.ihelp.CommonClass.ValidInput;
import com.example.ihelp.LoginAndRegister.Model.LoginInfo;
import com.example.ihelp.LoginAndRegister.Presenter.LoginHttpUtil;
import com.example.ihelp.MainActivity;
import com.example.ihelp.R;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by 朱继涛 2019/10/24
 * 登录界面
 */

public class loginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int LOGIN_SUCCESS=1;
    private static final int PASSWORD_ERROR=2;
    private static final int USER_NULL=3;

    private EditText userName;           //此时的用户名指的是手机号
    private EditText passWord;           //密码登陆时

    private LoginHttpUtil loginHttpUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = (EditText)findViewById(R.id.et_loginactivity_username);
        passWord = (EditText)findViewById(R.id.et_loginactivity_password);
        userName.setText("18721912858");
        passWord.setText("123456");
    }


    //各个点击时间的实现
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_loginactivity_register:
                //TODO 注册界面跳转
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
                break;
            case R.id.tv_loginactivity_forget:   //忘记密码
            //TODO 忘记密码操作界面跳转
            //    startActivity(new Intent(this, FindPasswordActivity.class));
            //    break;
            case R.id.tv_loginactivity_check:    //短信验证码登录
            // TODO 短信验证码登录界面跳转
                startActivity(new Intent(this, MessageLoginActivity.class));
                finish();
                break;

            /**
             * 登录验证：
             * 从EditText的对象上获取文本编辑框输入的数据，并把左右两边的空格去掉
             *  进行匹配验证,先判断一下用户名密码是否为空，
             *  if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password))
             *  再进而for循环判断是否与数据库中的数据相匹配
             *  一旦匹配，立即将match = true；break；
             *  否则 一直匹配到结束 match = false；
             *  登录成功之后，进行页面跳转：
             */
            case R.id.bt_loginactivity_login:
                String name = userName.getText().toString().trim();
                String password = passWord.getText().toString().trim();
                //先判断账号和密码是否为空
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
                    if(ValidInput.isMobile(name)) {
                        boolean match = false;
                        //下面是从数据库查询的步骤，也就是从判断是否匹配的地方
                        loginHttpUtil = new LoginHttpUtil(name, MD5Util.getMD5(password));
                        loginHttpUtil.sendRequestWithOkHttp(new HttpCallbackListener() {
                            //重写HttpCallbackListener接口的方法，对传递过来的数据进行操作
                            @Override
                            public void onFinish(String response) {
                                //新建一个Message对象，存储需要发送的消息
                                Message message = new Message();

                                LoginInfo loginInfo = loginHttpUtil.parseJSONWithGSON(response);
                                //Log.d("XXXXX", String.valueOf(loginInfo.getErrorCode()));
                                if (loginInfo.getStatus().equals("ok")) {
                                    //输出提示信息
                                    message.what = LOGIN_SUCCESS;
                                    handler.sendMessage(message);
                                    //先将读取到的用户信息存入本地，便于修改
                                    saveUserInfo(loginInfo);

                                    Intent intent = new Intent(loginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();  //销毁此Activity
                                } else if (loginInfo.getErrorCode() == 201) {
                                    message.what = PASSWORD_ERROR;
                                    handler.sendMessage(message);
                                } else if (loginInfo.getErrorCode() == 202) {
                                    message.what = USER_NULL;
                                    handler.sendMessage(message);
                                }
                            }
                            @Override
                            public void onError(Exception e) { e.printStackTrace(); }
                        });

                    }else {
                        Toast.makeText(this, "手机号格式不正确！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请输入你的用户名或密码", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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

    //输出提示信息
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case LOGIN_SUCCESS:
                    //在这里可以进行UI操作
                    Toast.makeText(loginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    break;
                case PASSWORD_ERROR:
                    //在这里可以进行UI操作
                    Toast.makeText(loginActivity.this, "用户名或密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
                    break;
                case USER_NULL:
                    //在这里可以进行UI操作
                    Toast.makeText(loginActivity.this, "用户名不存在，请重新输入", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };


}
