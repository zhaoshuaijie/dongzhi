package com.lcsd.dongzhi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.entity.CommentInfoList;
import com.lcsd.dongzhi.view.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */
public class CommentListAdapter extends BaseAdapter {
    private Context mContext;
    private List<CommentInfoList.TRslist> beanList;

    public CommentListAdapter(Context mContext, List<CommentInfoList.TRslist> beanList) {
        this.mContext = mContext;
        this.beanList = beanList;
    }

    @Override
    public int getCount() {
        return beanList.size();
    }

    @Override
    public Object getItem(int i) {

        return beanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        CommentHoder hoder = null;
        if (convertView == null) {
            hoder = new CommentHoder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_comment, null);
            hoder.tv_name = (TextView) convertView.findViewById(R.id.tv_tourist_name);
            hoder.tv_time = (TextView) convertView.findViewById(R.id.tv_tourist_time);
            hoder.tv_comment = (TextView) convertView.findViewById(R.id.tv_tourist_com);
            hoder.civ_head = (CircleImageView) convertView.findViewById(R.id.iv_tourist_photo);
            convertView.setTag(hoder);
        } else {
            hoder = (CommentHoder) convertView.getTag();
        }
        if (beanList.get(position).getAlias() != null && beanList.get(position).getAlias().length() > 0)
            hoder.tv_name.setText(beanList.get(position).getAlias());
        if (beanList.get(position).getContent() != null && beanList.get(position).getContent().length() > 0)
            hoder.tv_comment.setText(beanList.get(position).getContent());
        if (beanList.get(position).getAddtime() != null && beanList.get(position).getAddtime().length() > 0) {
            hoder.tv_time.setText(timeStamp2Date(beanList.get(position).getAddtime()));
        }
        if (beanList.get(position).getAvatar() != null && beanList.get(position).getAvatar().length() > 0)
            Glide.with(mContext).load(beanList.get(position).getAvatar()).crossFade().into(hoder.civ_head);
        else
            hoder.civ_head.setImageResource(R.mipmap.img_defult_head);
        return convertView;
    }

    class CommentHoder {
        TextView tv_name, tv_time, tv_comment;
        CircleImageView civ_head;

    }


    public static String timeStamp2Date(String timestampString) {
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(new java.util.Date(timestamp));
        return date;//unix时间戳转换成java时间,不然会只显示1970.。。。。
    }
}
