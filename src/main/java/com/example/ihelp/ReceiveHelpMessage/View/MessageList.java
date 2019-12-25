package com.example.ihelp.ReceiveHelpMessage.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.example.ihelp.CommonClass.HelpMessage;
import com.example.ihelp.CommonClass.HttpCallbackListener;
import com.example.ihelp.R;
import com.example.ihelp.ReceiveHelpMessage.Presenter.Filter;
import com.example.ihelp.ReceiveHelpMessage.Presenter.GetMessageHttpUtil;
import com.example.ihelp.ReceiveHelpMessage.Presenter.HelpMessageAdapter;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 朱继涛 2019/11/14
 * 四个模块之一的收到的求助信息列表
 */

public class MessageList extends Fragment{

    private static final int REFRESH_SUCCESS = 1;

    private Spinner spinner;

    private Context mcontext;

    //实现下拉刷新的组件
    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerView; //声明RecyclerView
    private HelpMessageAdapter messageAdapter; //声明适配器
    private List<HelpMessage> messageList;

    private GetMessageHttpUtil getMessageHttpUtil;

    private Filter filter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //一般在这里进行init操作
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_list, container,false);

        spinner = (Spinner) view.findViewById(R.id.spinner);
        //进行initView操作
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //进行initData以及initListener操作
        initData();
        initListener();
    }


    //初始化界面的函数
    public void initView(final View view){
        //绑定RecyclerView
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);

        //绑定下拉刷新的组件
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        //添加监听器
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        //填充RecyclerView的内容
        messageList = new ArrayList<>();

        //向适配器中填充数据
        messageAdapter = new HelpMessageAdapter(getContext(),messageList);

        //设置RecyclerView的布局为线性垂直
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        //填充layout
        recyclerView.setAdapter(messageAdapter);

    }

    //在这里初始化数据
    public void initData(){
    }

    //初始化监听器的函数
    public void initListener(){

        // 设置数据后就要给RecyclerView设置点击事件,传递参数同时打开ShowKnowledge活动
        messageAdapter.setOnItemClickListener(new HelpMessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), GetPath.class);
                //messageList
                intent.putExtra("lat", messageList.get(position).getLat());
                intent.putExtra("lng", messageList.get(position).getLng());
                intent.putExtra("id",messageList.get(position).getId());
                startActivity(intent);
            }
        });
    }

    //设置刷新数据的函数
    public void refreshData(){
        //线程沉睡是为了让用户看见刷新的过程
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        getMessageHttpUtil = new GetMessageHttpUtil();
        getMessageHttpUtil.sendRequestWithOkHttp(new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                List<HelpMessage> helpMessages = getMessageHttpUtil.parseJSONWithGSON(response);

                //筛选
                SharedPreferences pref = getActivity().getSharedPreferences("locationInfo", MODE_PRIVATE);
                filter = new Filter(pref,helpMessages,Integer.valueOf((String) spinner.getSelectedItem()).intValue());
                helpMessages = filter.getHelpMessages();

                //向handler传递参数
                Message message = new Message();
                message.what = REFRESH_SUCCESS;
                Bundle bundle = new Bundle();
                ArrayList list = new ArrayList();
                list.add(helpMessages);
                bundle.putStringArrayList("data",list);
                message.setData(bundle);
                handler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {

            }
        });

    }

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REFRESH_SUCCESS:
                    //在这里可以进行UI操作

                    ArrayList data = msg.getData().getParcelableArrayList("data");
                    List<HelpMessage> helpMessages = (List<HelpMessage>) data.get(0);

                    //首先清空数据
                    messageList.clear();
                    //替换
                    messageList.addAll(helpMessages);

                    messageAdapter.notifyDataSetChanged();

                    //停止刷新
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), "刷新成功！", Toast.LENGTH_SHORT).show();

                    break;
                default:
                    break;
            }
        }
    };
}