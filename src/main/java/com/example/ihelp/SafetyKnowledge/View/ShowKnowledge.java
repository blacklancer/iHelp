package com.example.ihelp.SafetyKnowledge.View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.ihelp.R;
import com.example.ihelp.SafetyKnowledge.Model.Information;
import com.example.ihelp.SafetyKnowledge.Presenter.GetJsonDataUtil;
import com.google.gson.Gson;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by 朱继涛 2019/11/3
 * 展示各种安全知识的界面
 */

public class ShowKnowledge extends AppCompatActivity{

    //从SafeKnowledge传过来的参数，请求的数据类型
    private String type;

    //定义控件
    private TextView knowledgeTitle;
    private ImageView knowledgeImage;
    private TextView knowledgeContent;
    private TextView moreKnowledge;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_safe_knowledge);

        //获取传递过来的参数
        Intent intent = getIntent();
        int type = intent.getIntExtra("key",1);

        //初始化view和填充数据,设置监听器
        initView();
        initData(String.valueOf(type+1));
        initListener(type);
    }

    //初始化view的函数
    public void initView(){
        knowledgeTitle = (TextView)findViewById(R.id.knowledge_title);
        knowledgeImage = (ImageView)findViewById(R.id.knowledge_image);
        knowledgeContent = (TextView)findViewById(R.id.knowledge_content);
        moreKnowledge = (TextView)findViewById(R.id.more_knowledge);
    }

    //数据填充的初始化函数，注意json文件放置的目录是src/main/assets
    public void initData(String type){
        //判断数据是否加载成功之后在进行设置
        if(!type.equals("")) {
            String jsonData = new GetJsonDataUtil().getJson(this, "data_0" + type + ".json");
            Gson gson = new Gson();
            Information information = gson.fromJson(jsonData, Information.class);

            //填充标题和内容
            knowledgeTitle.setText(information.getTitle());
            knowledgeContent.setText(information.getContent());

            //填充中间图片
            if(type.equals("1")){
                knowledgeImage.setImageResource(R.drawable.bg1);
            }else if(type.equals("2")) {
                knowledgeImage.setImageResource(R.drawable.bg2);
            }else if(type.equals("3")){
                knowledgeImage.setImageResource(R.drawable.bg3);
            }

            //下面是使用listview填充数据但是效果不好，所以放弃了
//            String [] data = new String[information.getContent().size()];
//            for(int i=0; i<information.getContent().size(); i++){
//                 data[i] = information.getContent().get(i).getItem();
//            }
//            ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String> (
//                    ShowKnowledge.this, android.R.layout.simple_list_item_1, data);
//            ListView listView = (ListView) findViewById(R.id.contentList);
//            listView.setAdapter(arrayAdapter);
        }else {
            Log.d("XXX", "json数据读取失败！");
        }
    }

    //设置点击事件
    public void initListener(final int type){
        //设置点击事件，点击更多信息可以跳转到相应的网页查看更多信息
        moreKnowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = null;
                Intent intent = null;
                if(type==0) {
                    uri = Uri.parse("https://baijiahao.baidu.com/s?id=1629666573028406797&wfr=spider&for=pc");
                }else if(type==1){
                    uri = Uri.parse("http://www.sohu.com/a/279080018_99951959");
                }else if(type==2){
                    uri = Uri.parse("http://www.sohu.com/a/288075309_170113");
                }
                intent  = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }


    //get and set方法
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
