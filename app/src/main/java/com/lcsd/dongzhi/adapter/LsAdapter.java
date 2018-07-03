package com.lcsd.dongzhi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.entity.Ls1;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/6.
 */
public class LsAdapter extends BaseAdapter {
    private Context context;
    private List<Ls1.TResult> list;
    private LayoutInflater layoutInflater;

    public LsAdapter(Context context, List<Ls1.TResult> list) {
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
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_ls, viewGroup, false);
            view.setTag(new ViewHolder(view));
        }
        ViewHolder holder = (ViewHolder) view.getTag();

        holder.tv_title.setText(list.get(i).getTitle());
        holder.tv_sj.setText(list.get(i).getDate());

        return view;
    }

    class ViewHolder {
        @BindView(R.id.item_ls_title)
        TextView tv_title;
        @BindView(R.id.item_ls_sj)
        TextView tv_sj;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
        }
    }
}
