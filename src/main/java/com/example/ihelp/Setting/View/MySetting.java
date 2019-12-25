package com.example.ihelp.Setting.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.ihelp.R;
import com.example.ihelp.Setting.Presenter.OptionAdapter;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by 朱继涛 on 2015/10/31.
 * 个人设置界面
 */

public class MySetting extends Fragment {

    private RecyclerView recyclerView; //声明RecyclerView
    private OptionAdapter optionAdapter; //声明适配器
    private List<String> optionList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //一般在这里进行init操作
        init();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_setting, container,false);

        //进行initView操作
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //进行initData以及initListener操作
        initData();
        initListener();
        super.onActivityCreated(savedInstanceState);
    }


    public void init(){
    }

    //初始化界面的函数
    public void initView(final View view){
        //绑定RecyclerView
        recyclerView = (RecyclerView)view.findViewById(R.id.option_recycler_view);
    }

    //在这里初始化数据就是，将本地存储的安全知识取出，然后初始化
    public void initData(){
        //填充RecyclerView的内容
        optionList = new ArrayList<>();
        optionList.add("修改个人信息");
        optionList.add("设置紧急求助信息");
        optionList.add("设置信息提醒等级");
        optionList.add("更多设置");

        //向适配器中填充数据
        optionAdapter = new OptionAdapter(getContext(),optionList);

        //设置RecyclerView的布局为线性垂直
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        //填充layout
        recyclerView.setAdapter(optionAdapter);
    }

    //初始化监听器的函数
    public void initListener(){

        // 设置数据后就要给RecyclerView设置点击事件,传递参数同时打开ShowKnowledge活动
        optionAdapter.setOnItemClickListener(new OptionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //通过获取不同的数值来跳转到不同的功能界面
                Intent intent = null;
                if(position==0){
                    intent = new Intent(getActivity(), ModifyInformation.class);
                }else if(position==1){
                    intent = new Intent(getActivity(), SetEmergencyMsg.class);
                }else if(position==2){
                    intent = new Intent(getActivity(), SetMessagePrompt.class);
                }else if(position==3){
                    intent = new Intent(getActivity(), MoreSettings.class);
                }
                startActivity(intent);
            }
        });
    }

}
