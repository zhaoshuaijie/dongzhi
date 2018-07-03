package com.lcsd.dongzhi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.activity.NewsDetialActivity;
import com.lcsd.dongzhi.entity.NewsList;
import com.lcsd.dongzhi.util.StringUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/9/8.
 */
public class NewsListAdapter extends BaseAdapter {
    private Context context;
    private List<NewsList> list;
    private LayoutInflater listContainer;
    private String name;

    public NewsListAdapter(Context context, List<NewsList> list, String name) {
        this.context = context;
        this.list = list;
        listContainer = LayoutInflater.from(context);
        this.name = name;
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
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        MyNewsDataHoder hoder = null;
        if (convertView == null) {
            hoder = new MyNewsDataHoder();
            convertView = listContainer.inflate(R.layout.adaptet_newsinfo, null);
            hoder.iv_news = (ImageView) convertView.findViewById(R.id.iv_news_photo);
            hoder.tv_title = (TextView) convertView.findViewById(R.id.tv_news_info);
            hoder.tv_scanner = (TextView) convertView.findViewById(R.id.tv_news_scanner);
            hoder.tv_time = (TextView) convertView.findViewById(R.id.tv_news_time);
            hoder.rl_news = (RelativeLayout) convertView.findViewById(R.id.rl_news);
          //  hoder.tv_zuoze= (TextView) convertView.findViewById(R.id.tv_news_zuoze);
            convertView.setTag(hoder);
        } else {
            hoder = (MyNewsDataHoder) convertView.getTag();
        }
        if (list.get(i).getThumb().equals("")) {
            hoder.iv_news.setImageResource(R.drawable.img_thume_defult);
        } else {
            Glide.with(context).load(list.get(i).getThumb()).centerCrop().placeholder(R.drawable.img_thume_defult).crossFade().into(hoder.iv_news);
        }
        hoder.tv_title.setText(list.get(i).getTitle());
        hoder.tv_scanner.setText( list.get(i).getHits() + "阅");
        hoder.tv_time.setText(StringUtil.timeStamp2Date(list.get(i).getDateline()));
        hoder.rl_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, NewsDetialActivity.class).putExtra("newsId", list.get(i).getId()).putExtra("title", name).putExtra("img",list.get(i).getThumb()!=null?list.get(i).getThumb():"").putExtra("note",list.get(i).getNote()!=null?list.get(i).getNote():""));
            }
        });
       // hoder.tv_zuoze.setText(list.get(i).getWriter());
        return convertView;
    }

    class MyNewsDataHoder {
        ImageView iv_news;
        TextView tv_title, tv_scanner, tv_time/*,tv_zuoze*/;
        RelativeLayout rl_news;
    }
}
