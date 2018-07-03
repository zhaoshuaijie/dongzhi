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
import com.lcsd.dongzhi.entity.DianBoInfo;
import com.lcsd.dongzhi.util.StringUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/12/18.
 */
public class Dianbo_List_Adapter extends BaseAdapter {
    private Context mContext;
    private List<DianBoInfo.TRs_lists> infos;

    public Dianbo_List_Adapter(Context mContext, List<DianBoInfo.TRs_lists> infos) {
        this.mContext = mContext;
        this.infos = infos;
    }

    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public Object getItem(int i) {
        return infos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        DianBoHoder hoder = null;
        if (view == null) {
            hoder = new DianBoHoder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_dianbo_list, null);
            hoder.tv_read = (TextView) view.findViewById(R.id.tv_read_gd);
            hoder.tv_title = (TextView) view.findViewById(R.id.tv_title_gd);
            hoder.tv_time = (TextView) view.findViewById(R.id.tv_time_gd);
            hoder.iv_yulan = (ImageView) view.findViewById(R.id.iv_pic_gd);
            // hoder.tv_zuoze= (TextView) view.findViewById(R.id.tv_zuoze_gd);
            view.setTag(hoder);
        } else {
            hoder = (DianBoHoder) view.getTag();
        }
        /**视频标题*/
        if (infos.get(position).getTitle() != null && infos.get(position).getTitle().length() > 0)
            hoder.tv_title.setText(infos.get(position).getTitle());
       /* *
        if(infos.get(position).getWriter()!=null&&infos.get(position).getWriter().length()>0)
            hoder.tv_zuoze.setText(infos.get(position).getWriter());*/
        /**时间*/
        if (infos.get(position).getDateline() != null && infos.get(position).getDateline().length() > 0)
            hoder.tv_time.setText(StringUtil.timeStamp2Date(infos.get(position).getDateline()));
        /**热度次数*/
        if (infos.get(position).getHits() != null && infos.get(position).getHits().length() > 0)
            hoder.tv_read.setText("热度：" + infos.get(position).getHits());
        if (infos.get(position).getThumb() != null && infos.get(position).getThumb().length() > 0) {
            Glide.with(mContext).load(infos.get(position).getThumb()).placeholder(R.drawable.img_thume_defult).crossFade().into(hoder.iv_yulan);
        } else {
            hoder.iv_yulan.setImageResource(R.drawable.img_thume_defult);
        }

        return view;
    }

    class DianBoHoder {
        TextView tv_title, tv_time, tv_read/*,tv_zuoze*/;
        ImageView iv_yulan;
    }

}
