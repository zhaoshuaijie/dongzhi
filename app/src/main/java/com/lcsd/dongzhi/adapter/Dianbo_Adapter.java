package com.lcsd.dongzhi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.entity.Dianbo;
import com.lcsd.dongzhi.view.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2017/12/18.
 */
public class Dianbo_Adapter extends BaseAdapter {
    private Context context;
    private List<Dianbo> list;

    public Dianbo_Adapter(Context context, List<Dianbo> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_dianbo_adapter, null);
            holder.circle_image = (CircleImageView) view.findViewById(R.id.civ_db);
            holder.tv_dianbo = (TextView) view.findViewById(R.id.tv_dianbo);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (list.get(i).getTitle() != null) {
            holder.tv_dianbo.setText(list.get(i).getTitle());
        }
        if (list.get(i).getIco() != null) {
            Glide.with(context).load(list.get(i).getIco()).placeholder(R.drawable.img_logo).into(holder.circle_image);
        }
        return view;
    }

    class ViewHolder {
        CircleImageView circle_image;
        TextView tv_dianbo;
    }
}
