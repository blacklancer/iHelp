package com.example.ihelp.SendHelpMessage.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ihelp.CommonClass.HelpMessage;
import com.example.ihelp.CommonClass.HttpCallbackListener;
import com.example.ihelp.R;
import com.example.ihelp.SendHelpMessage.Presenter.EndHelpHttpUtil;
import com.example.ihelp.SendHelpMessage.Presenter.MyHelpHttpUtil;

import java.util.ArrayList;
import java.util.List;

import static com.amap.api.maps.model.BitmapDescriptorFactory.getContext;

public class MyHelpMessage extends AppCompatActivity {

    private static final int IS_EXIST = 1;
    private static final int NOT_EXIST = 0;
    private static final int END_SUCCESS = 2;
    private static final int END_FAILD = 3;

    private TextView level;
    private TextView title;
    private TextView demand;
    private TextView state;
    private TextView error;
    private TextView show1;
    private TextView show2;
    private TextView show3;
    private TextView show4;
    private Button button;
    private int id;

    private MyHelpHttpUtil myHelpHttpUtil;
    private EndHelpHttpUtil endHelpHttpUtil;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_help_message);

        error = (TextView)findViewById(R.id.error);
        level = (TextView) findViewById(R.id.level);
        title = (TextView) findViewById(R.id.title);
        demand = (TextView) findViewById(R.id.demand);
        state = (TextView) findViewById(R.id.state);
        button = (Button) findViewById(R.id.button);

        show1 = (TextView) findViewById(R.id.show1);
        show2 = (TextView) findViewById(R.id.show2);
        show3 = (TextView) findViewById(R.id.show3);
        show4 = (TextView) findViewById(R.id.show4);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getInt("id");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endHelpHttpUtil = new EndHelpHttpUtil(id, 2);
                endHelpHttpUtil.sendRequestWithOkHttp(new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        Message message = new Message();
                        if(response.equals("ok")){
                            message.what = END_SUCCESS;
                        }
                        else{
                            message.what = END_FAILD;
                        }
                        handler.sendMessage(message);
                    }
                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
        });
        Init();
    }


    private void Init(){
        if(id==0){
            error.setVisibility(View.VISIBLE);
            level.setVisibility(View.GONE);
            title.setVisibility(View.GONE);
            demand.setVisibility(View.GONE);
            state.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
            show1.setVisibility(View.GONE);
            show2.setVisibility(View.GONE);
            show3.setVisibility(View.GONE);
            show4.setVisibility(View.GONE);
        }
        else{
            myHelpHttpUtil = new MyHelpHttpUtil(id);
            myHelpHttpUtil.sendRequestWithOkHttp(new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {

                    Message message = new Message();
                    HelpMessage helpMessage = myHelpHttpUtil.parseJSONWithGSON(response);
                    if(helpMessage.getState()==2){
                        message.what = NOT_EXIST;
                        handler.sendMessage(message);
                    }
                    else{
                        message.what = IS_EXIST;
                        Bundle bundle = new Bundle();
                        ArrayList helpMessages = new ArrayList();
                        helpMessages.add(helpMessage);
                        bundle.putStringArrayList("data",helpMessages);
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
    }

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case IS_EXIST:
                    //在这里可以进行UI操作
                    ArrayList data = msg.getData().getParcelableArrayList("data");
                    HelpMessage helpMessage = (HelpMessage)data.get(0);
                    if(helpMessage.getState()==0){
                        state.setText("当前无人救助");
                    }
                    else{
                        state.setText("当前已有人前来救助");
                    }
                    level.setText(String.valueOf(helpMessage.getLevel()));
                    title.setText(helpMessage.getTitle());
                    demand.setText(helpMessage.getContent());
                    break;
                case NOT_EXIST:
                    error.setVisibility(View.VISIBLE);
                    level.setVisibility(View.GONE);
                    title.setVisibility(View.GONE);
                    demand.setVisibility(View.GONE);
                    state.setVisibility(View.GONE);
                    button.setVisibility(View.GONE);
                    show1.setVisibility(View.GONE);
                    show2.setVisibility(View.GONE);
                    show3.setVisibility(View.GONE);
                    show4.setVisibility(View.GONE);
                    break;
                case END_SUCCESS:
                    Toast.makeText(getContext(), "结束求助成功！", Toast.LENGTH_SHORT).show();
                    break;
                case END_FAILD:
                    Toast.makeText(getContext(), "结束求助失败！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
}
