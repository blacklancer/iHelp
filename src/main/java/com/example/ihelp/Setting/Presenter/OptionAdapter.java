package com.example.ihelp.Setting.Presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.ihelp.R;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by 朱继涛 2019/11/4
 * 个人设置里选项的适配器
 */

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.MyViewHolder> {

    private Context context;
    private List<String> list;
    private View inflater;

    //声明点击事件回调接口
    private OptionAdapter.OnItemClickListener onItemClickListener;

    //构造方法，传入数据
    public OptionAdapter(Context context, List<String> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public OptionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建ViewHolder，返回每一项的布局
        inflater = LayoutInflater.from(context).inflate(R.layout.setting_option_item,parent,false);
        final OptionAdapter.MyViewHolder myViewHolder = new OptionAdapter.MyViewHolder(inflater);

        //设置点击事件，返回不同的点击位置，传递到ShowKnowledge界面
        // 点击事件一般都写在绑定数据这里，当然写到上边的创建布局时候也是可以的
        if (onItemClickListener != null){
            myViewHolder.titleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = myViewHolder.getAdapterPosition();
                    // 这里利用回调来给RecyclerView设置点击事件
                    onItemClickListener.onItemClick(position);
                }
            });
        }
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(OptionAdapter.MyViewHolder holder, int position) {
        //将数据和控件绑定
        holder.textView.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        //返回Item总条数
        return list.size();
    }

    //内部类，绑定控件，并且设置点击事件
    class MyViewHolder extends RecyclerView.ViewHolder{
        //最外层的view，便于设置点击事件
        View titleView;
        TextView textView;
        public MyViewHolder(View itemView) {
            super(itemView);
            titleView = itemView;
            textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }

    //定义recyclerview的点击回调接口
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    //提供setter方法
    public void setOnItemClickListener(OptionAdapter.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public OptionAdapter.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

}
