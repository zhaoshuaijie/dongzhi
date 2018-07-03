package com.lcsd.dongzhi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.entity.Wxjx;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/6.
 */
public class WxjxAdapter extends BaseAdapter{
    private Context context;
    private List<Wxjx.TList> list;
    private LayoutInflater mInflater;
    public WxjxAdapter(Context context, List<Wxjx.TList> list){
        this.context=context;
        this.list=list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = mInflater.inflate(R.layout.item_wxjx, viewGroup, false);
            view.setTag(new ViewHolder(view));
        }
        ViewHolder holder = (ViewHolder) view.getTag();

        Glide.with(context).load(list.get(i).getFirstImg()).centerCrop().placeholder(R.drawable.img_thume_defult).into(holder.iv);
        holder.tv_title.setText(list.get(i).getTitle());
        holder.tv_ly.setText("来源："+list.get(i).getSource());

        return view;
    }

    class ViewHolder{
        @BindView(R.id.item_wxjx_iv)
        ImageView iv;
        @BindView(R.id.item_wxjx_tv_title)
        TextView tv_title;
        @BindView(R.id.item_wxjx_tv_ly)
        TextView tv_ly;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
        }
    }
}
