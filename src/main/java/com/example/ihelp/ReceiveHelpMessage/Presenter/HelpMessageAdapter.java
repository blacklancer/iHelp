package com.example.ihelp.ReceiveHelpMessage.Presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ihelp.CommonClass.HelpMessage;
import com.example.ihelp.R;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by 朱继涛 2019/11/14
 * 四个模块之一的收到的求助信息的实体类适配器
 */

public class HelpMessageAdapter extends RecyclerView.Adapter<HelpMessageAdapter.ViewHolder>{

    private Context context;
    //创建list集合，泛型为之前定义的实体类
    private List<HelpMessage> messageList;

    private View inflater;

    //声明点击事件回调接口
    private OnItemClickListener onItemClickListener;

    //添加构造方法
    public HelpMessageAdapter(Context context,List<HelpMessage> messageList){
        this.context = context;
        this.messageList = messageList;
    }

    //在onCreateViewHolder（）中完成布局的绑定，同时创建ViewHolder对象，返回ViewHolder对象
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //创建ViewHolder，返回每一项的布局
        inflater = LayoutInflater.from(context).inflate(R.layout.help_message_item,parent,false);
        final ViewHolder holder = new ViewHolder(inflater);

        // 点击事件一般都写在绑定数据这里，当然写到上边的创建布局时候也是可以的
        if (onItemClickListener != null){
            holder.messageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    // 这里利用回调来给RecyclerView设置点击事件
                    onItemClickListener.onItemClick(position);
                }
            });
        }
        return holder;
    }

    //在内部类中完成对控件的绑定
    static class ViewHolder extends RecyclerView.ViewHolder {
        //创建View变量playerView，用来对整个子项进行监听
        private View messageView;
        private TextView titleView;
        private TextView contentView;
        private TextView addressView;
        private TextView levelView;
        private TextView nameView;
        private TextView phoneView;
        private TextView stateView;

        public ViewHolder(View itemView) {
            super(itemView);
            //将子项对外层的布局itemView赋给playerView，这样就能表示整个子项
            messageView = itemView;
            titleView = itemView.findViewById(R.id.message_title);
            contentView = itemView.findViewById(R.id.message_content);
            addressView = itemView.findViewById(R.id.message_address);
            levelView = itemView.findViewById(R.id.message_level);
            nameView = itemView.findViewById(R.id.message_name);
            phoneView = itemView.findViewById(R.id.message_phone);
            stateView = itemView.findViewById(R.id.message_state);
        }
    }

    //在onBindViewHolder（）中完成对数据的填充
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.titleView.setText(messageList.get(position).getTitle());
        holder.contentView.setText(messageList.get(position).getContent());
        holder.addressView.setText(messageList.get(position).getAddress());
        holder.levelView.setText(String.valueOf(messageList.get(position).getLevel()));
        holder.phoneView.setText(messageList.get(position).getPhone());
        holder.nameView.setText(messageList.get(position).getName());
        if(messageList.get(position).getState()==0){
            holder.stateView.setText("当前无人前往救助");
        }
        else{
            holder.stateView.setText("当前已有人前往救助");
        }
    }


    //  添加数据
    public void addData(int position, HelpMessage helpMessage) {
        //在list中添加数据，并通知条目加入一条
        messageList.add(position, helpMessage);
        //添加动画
        notifyItemInserted(position);
        notifyItemRangeChanged(position, messageList.size());
    }

    //  删除数据
    public void removeData(int position) {
        messageList.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
        notifyItemRangeChanged(position, messageList.size());
    }

    //这个方法很简单了，返回playerList中的子项的个数
    @Override
    public int getItemCount() {
        return messageList.size();
    }


    //定义recyclerview的点击回调接口
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    //提供setter方法
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

}


