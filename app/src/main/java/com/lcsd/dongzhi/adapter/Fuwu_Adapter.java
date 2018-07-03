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
import com.lcsd.dongzhi.entity.Fuwu;

import java.util.List;

/**
 * Created by Administrator on 2017/12/26.
 */
public class Fuwu_Adapter extends BaseAdapter{
    private Context context;
    private List<Fuwu> list;

    public Fuwu_Adapter(Context context, List<Fuwu> list) {
        this.context = context;
        this.list = list;

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
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_gridview_f3, null);
            holder.image = (ImageView) view.findViewById(R.id.item_f3_img);
            holder.tv = (TextView) view.findViewById(R.id.item_f3_tv);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (list.get(i).getTitle() != null) {
            holder.tv.setText(list.get(i).getTitle());
        }
        if(list.get(i).getTitle().equals("工作动态")){
            holder.image.setImageResource(R.drawable.img_gzdt);
        }else if(list.get(i).getTitle().equals("通知公告")){
            holder.image.setImageResource(R.drawable.img_tzgg);
        }else if(list.get(i).getTitle().equals("新闻头条")){
            holder.image.setImageResource(R.drawable.img_xwtt);
        } else if(list.get(i).getTitle().equals("微信精选")){
            holder.image.setImageResource(R.drawable.img_wxjx);
        }else if(list.get(i).getTitle().equals("历史的今天")){
            holder.image.setImageResource(R.drawable.img_lssdjt);
        }else if(list.get(i).getTitle().equals("实时影讯")){
            holder.image.setImageResource(R.drawable.img_ssyx);
        }else if(list.get(i).getTitle().equals("轻松一刻")){
            holder.image.setImageResource(R.drawable.img_qsyk);
        }else if(list.get(i).getIco() != null){
            Glide.with(context).load(list.get(i).getIco()).placeholder(R.drawable.img_logo).into(holder.image);
        }
        return view;
    }

    class ViewHolder {
        ImageView image;
        TextView tv;
    }
}
