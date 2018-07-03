package com.lcsd.dongzhi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.entity.Zhibo;
import com.lcsd.dongzhi.view.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2017/9/14.
 */
public class ZhiboAdapter extends BaseAdapter{

    private Context context;
    private List<Zhibo> list;

    public ZhiboAdapter(Context context, List<Zhibo> list) {
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
        ZhiBoHoder hoder=null;
        if(view==null){
            hoder=new ZhiBoHoder();
            view= LayoutInflater.from(context).inflate(R.layout.item_zhibo_adapter,null);
            hoder.imageView= (CircleImageView) view.findViewById(R.id.civ_zb);
            hoder.tv_title= (TextView) view.findViewById(R.id.tv_zhibo);
            view.setTag(hoder);
        }else {
            hoder= (ZhiBoHoder) view.getTag();
        }
        hoder.tv_title.setText(list.get(i).getTitle());
        Glide.with(context).load(list.get(i).getIco()).placeholder(R.drawable.img_logo).into(hoder.imageView);
        return view;
    }
    class ZhiBoHoder {
        TextView tv_title;
        CircleImageView imageView;
    }
}
