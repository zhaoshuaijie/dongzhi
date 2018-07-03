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
import com.lcsd.dongzhi.entity.NewsTop;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/7.
 */
public class NewsTopAdapter extends BaseAdapter {
    private Context context;
    private List<NewsTop.TData> list;
    private LayoutInflater listContainer;
    final int TYPE_1 = 0;
    final int TYPE_2 = 1;

    public NewsTopAdapter(Context context, List<NewsTop.TData> list) {
        this.list = list;
        this.context = context;
        listContainer = LayoutInflater.from(context);
    }

    public void setList(List<NewsTop.TData> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).getThumbnail_pic_s() != null && list.get(position).getThumbnail_pic_s02() != null && list.get(position).getThumbnail_pic_s03() != null){
            return TYPE_1;
        }else {
            return TYPE_2;
        }
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
        ViewHolder1 holder1 = null;
        ViewHolder2 holder2 = null;
        if (view == null) {
            switch (getItemViewType(i)){
                case TYPE_1:
                    view = listContainer.inflate(R.layout.item_newstop1, viewGroup, false);
                    holder1 = new ViewHolder1(view);
                    view.setTag(holder1);
                    break;
                case TYPE_2:
                    view = listContainer.inflate(R.layout.item_newstop2, viewGroup, false);
                    holder2 = new ViewHolder2(view);
                    view.setTag(holder2);
                    break;
            }
        } else {
            switch (getItemViewType(i)){
                case TYPE_1:
                    holder1 = (ViewHolder1) view.getTag();
                    break;
                case TYPE_2:
                    holder2 = (ViewHolder2) view.getTag();
                    break;
            }
        }

        switch (getItemViewType(i)){
            case TYPE_1:
                holder1.tv_title.setText(list.get(i).getTitle());
                holder1.tv_ly.setText(list.get(i).getAuthor_name());
                holder1.tv_sj.setText(list.get(i).getDate());
                Glide.with(context).load(list.get(i).getThumbnail_pic_s()).fitCenter().placeholder(R.drawable.img_thume_defult).into(holder1.iv1);
                Glide.with(context).load(list.get(i).getThumbnail_pic_s02()).fitCenter().placeholder(R.drawable.img_thume_defult).into(holder1.iv2);
                Glide.with(context).load(list.get(i).getThumbnail_pic_s03()).fitCenter().placeholder(R.drawable.img_thume_defult).into(holder1.iv3);
                break;
            case TYPE_2:
                holder2.tv_title.setText(list.get(i).getTitle());
                holder2.tv_ly.setText(list.get(i).getAuthor_name());
                holder2.tv_sj.setText(list.get(i).getDate());
                Glide.with(context).load(list.get(i).getThumbnail_pic_s()).fitCenter().placeholder(R.drawable.img_thume_defult).into(holder2.iv);
                break;
        }

        return view;
    }

    class ViewHolder1 {
        @BindView(R.id.item_nt_title)
        TextView tv_title;
        @BindView(R.id.item_nt_tv_ly)
        TextView tv_ly;
        @BindView(R.id.item_nt_tv_sj)
        TextView tv_sj;
        @BindView(R.id.item_nt_iv1)
        ImageView iv1;
        @BindView(R.id.item_nt_iv2)
        ImageView iv2;
        @BindView(R.id.item_nt_iv3)
        ImageView iv3;

        public ViewHolder1(View convertView) {
            ButterKnife.bind(this, convertView);
        }
    }

    class ViewHolder2 {
        @BindView(R.id.item_nt_title)
        TextView tv_title;
        @BindView(R.id.item_nt_tv_ly)
        TextView tv_ly;
        @BindView(R.id.item_nt_tv_sj)
        TextView tv_sj;
        @BindView(R.id.item_nt_iv)
        ImageView iv;

        public ViewHolder2(View convertView) {
            ButterKnife.bind(this, convertView);
        }
    }
}
