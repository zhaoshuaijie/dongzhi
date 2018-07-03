package com.lcsd.dongzhi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import com.alibaba.fastjson.JSON;
import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.activity.NewsDetialActivity;
import com.lcsd.dongzhi.adapter.NewsListAdapter;
import com.lcsd.dongzhi.entity.NewsList;
import com.lcsd.dongzhi.http.AppConfig;
import com.lcsd.dongzhi.http.AppContext;
import com.lcsd.dongzhi.util.L;
import com.lcsd.dongzhi.view.Autoviewpage;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Administrator on 2017/12/15.
 */
public class FragmentContent extends Fragment {
    @BindView(R.id.sv_news)
    ListView listView;
    @BindView(R.id.auto_viewpage)
    Autoviewpage autoviewpage;
    @BindView(R.id.sc_zixun)
    ScrollView scrollView;
    @BindView(R.id.fc_ptrframelayout)
    PtrClassicFrameLayout ptr;
    private int pageid = 1;
    private int total;
    private String identifier, title;
    private List<NewsList> list_news;
    private List<NewsList> list_hd;
    private NewsListAdapter newsListAdapter;

    public static Fragment getInstance(Bundle bundle) {
        FragmentContent fragment = new FragmentContent();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void initData() {
        identifier = getArguments().getString("identifier");
        title = getArguments().getString("title");
        ptr.setLastUpdateTimeRelateObject(this);
        ptr.disableWhenHorizontalMove(true);
        list_news = new ArrayList<>();
        list_hd = new ArrayList<>();
        newsListAdapter = new NewsListAdapter(getActivity(), list_news, title);
        listView.setAdapter(newsListAdapter);

        autoviewpage.setDotSpace(10);
        autoviewpage.setDotSize(10);
        autoviewpage.setDelay(3000);
        autoviewpage.setOnItemClickListener(new Autoviewpage.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getActivity().startActivity(new Intent(getActivity(), NewsDetialActivity.class).putExtra("newsId", list_hd.get(position).getId()).putExtra("title", title).putExtra("img", list_hd.get(position).getThumb() != null ? list_hd.get(position).getThumb() : "").putExtra("note", list_hd.get(position).getNote() != null ? list_hd.get(position).getNote() : ""));
            }
        });

        ptr.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                if (pageid < total) {
                    pageid++;
                    requestNewsList(2);
                }
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                // 下拉刷新操作
                requestNewsList(1);
            }

            @Override
            public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
                if (pageid < total) {
                    return super.checkCanDoLoadMore(frame, scrollView, footer);
                } else {
                    return false;
                }
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, scrollView, header);
            }
        });
        requestNewsList(0);
    }

    public void requestNewsList(final int i) {
        Map<String, String> map = new HashMap<>();
        map.put("id", "news");
        map.put("cate", identifier);
        if (i == 1) {
            map.put("pageid", 1 + "");
            pageid = 1;
        } else {
            map.put("pageid", pageid + "");
        }
        AppContext.getInstance().getmMyOkHttp().post(getContext(), AppConfig.request_Data, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    L.d("TAG", "新闻列表:" + response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getString("status").equals("ok")) {
                            JSONObject content = new JSONObject(object.getString("content"));
                            List<NewsList> Hd = JSON.parseArray(content.getString("hd_list"), NewsList.class);
                            List<NewsList> Rs = JSON.parseArray(content.getString("rs_lists"), NewsList.class);
                            total = Integer.parseInt(content.getString("total"));
                            if (Rs != null && Rs.size() > 0) {
                                if (i == 1) {
                                    list_news.clear();
                                }
                                list_news.addAll(Rs);
                                newsListAdapter.notifyDataSetChanged();
                            }
                            if (Hd != null && Hd.size() > 0) {
                                if (i == 0 || i == 1) {
                                    if (list_hd.size() == 0) {
                                        list_hd.addAll(Hd);
                                        initVp();
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (i == 1 || i == 2) {
                        ptr.refreshComplete();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                L.d("TAG", "信息错误:" + error_msg);
                if (i == 1 || i == 2) {
                    ptr.refreshComplete();
                }
            }
        });
    }


    private void initVp() {
        autoviewpage.setImageTitleBeanList(list_hd);
        autoviewpage.commit();
    }

    @Override
    public void onDestroy() {
        // 释放资源
        try {
            autoviewpage.releaseResource();
        } catch (Exception e) {

        }
        super.onDestroy();
    }

}
