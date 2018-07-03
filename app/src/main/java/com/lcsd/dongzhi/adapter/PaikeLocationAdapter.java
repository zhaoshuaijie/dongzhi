package com.lcsd.dongzhi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lcsd.dongzhi.R;
import com.tencent.map.geolocation.TencentPoi;

import java.util.List;

/**
 * Created by Administrator on 2017/11/9.
 */
public class PaikeLocationAdapter extends BaseAdapter {
    private List<TencentPoi> list;
    private Context context;

    public PaikeLocationAdapter(List<TencentPoi> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodle hodle = null;
        if (convertView == null) {
            hodle = new ViewHodle();
            convertView = View.inflate(context, R.layout.item_paike_location, null);
            hodle.name = (TextView) convertView.findViewById(R.id.tv_name);
            hodle.adree = (TextView) convertView.findViewById(R.id.tv_adree);
            convertView.setTag(hodle);
        } else {
            hodle = (ViewHodle) convertView.getTag();
        }
        hodle.name.setText(list.get(position).getName());
        hodle.adree.setText(list.get(position).getAddress());
        return convertView;
    }

    class ViewHodle {
        TextView name, adree;
    }
}
