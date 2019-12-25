package com.example.ihelp.SafetyKnowledge.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.ihelp.R;
import com.example.ihelp.SafetyKnowledge.Presenter.TitleAdapter;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by 朱继涛 2019/11/2
 * 四个模块之一的安全知识模块
 */

public class Safeknowledge extends Fragment{

    private RecyclerView recyclerView; //声明RecyclerView
    private TitleAdapter titleAdapter; //声明适配器
    private List<String> titleList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //一般在这里进行init操作
        init();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.safe_knowledge, container,false);

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
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);

    }

    //在这里初始化数据就是，将本地存储的安全知识取出，然后初始化
    public void initData(){
        //填充RecyclerView的内容
        titleList = new ArrayList<>();
        titleList.add("消防安全常识");
        titleList.add("交通安全常识");
        titleList.add("生活安全常识");

        //向适配器中填充数据
        titleAdapter = new TitleAdapter(getContext(),titleList);

        //设置RecyclerView的布局为线性垂直
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        //填充layout
        recyclerView.setAdapter(titleAdapter);
    }

    //初始化监听器的函数
    public void initListener(){

        // 设置数据后就要给RecyclerView设置点击事件,传递参数同时打开ShowKnowledge活动
        titleAdapter.setOnItemClickListener(new TitleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), ShowKnowledge.class);
                intent.putExtra("key", position);
                startActivity(intent);
            }
        });
    }

}
