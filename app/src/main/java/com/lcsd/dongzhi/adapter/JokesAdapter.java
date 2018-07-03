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
import com.lcsd.dongzhi.entity.Jokes;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/12.
 */
public class JokesAdapter extends BaseAdapter {
    private Context context;
    private List<Jokes> list;
    private LayoutInflater layoutInflater;

    public JokesAdapter(Context context, List<Jokes> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
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
        ViewHolder holder = null;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_jokes1, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_content.setText(list.get(i).getContent());
       // holder.tv_time.setText(StringUtils.timeStamp2Date(list.get(i).getUnixtime() + ""));
        if (list.get(i).getUrl() != null && list.get(i).getUrl().length() > 0) {
            Glide.with(context).load(list.get(i).getUrl()).fitCenter().dontAnimate().into(holder.iv);
            holder.iv.setVisibility(View.VISIBLE);
        } else {
            holder.iv.setVisibility(View.GONE);
        }

        return view;
    }

    class ViewHolder {
        /*@Bind(R.id.item_jokes_time)
        TextView tv_time;*/
        @BindView(R.id.item_jokes_content)
        TextView tv_content;
        @BindView(R.id.item_jokes_img)
        ImageView iv;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
        }
    }
}
