package com.lcsd.dongzhi.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.alibaba.fastjson.JSON;
import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.activity.FuwuListActivity;
import com.lcsd.dongzhi.activity.JokesActivity;
import com.lcsd.dongzhi.activity.LsActivity;
import com.lcsd.dongzhi.activity.NewsTopActivity;
import com.lcsd.dongzhi.activity.WXJXActivity;
import com.lcsd.dongzhi.activity.WebActivity;
import com.lcsd.dongzhi.activity.WepiaoActivity;
import com.lcsd.dongzhi.adapter.Fuwu_Adapter;
import com.lcsd.dongzhi.entity.Fuwu;
import com.lcsd.dongzhi.http.AppConfig;
import com.lcsd.dongzhi.http.AppContext;
import com.lcsd.dongzhi.util.L;
import com.lcsd.dongzhi.view.MultipleStatusView;
import com.lcsd.dongzhi.view.MyGridView;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Administrator on 2017/12/15.
 */
public class Fragment3 extends Fragment {
    private Context mContext;
    private List<Fuwu> list1;
    private List<Fuwu> list2;
    private List<Fuwu> list3;
    private List<Fuwu> list4;
    private Fuwu_Adapter adapter1;
    private Fuwu_Adapter adapter2;
    private Fuwu_Adapter adapter3;
    private Fuwu_Adapter adapter4;
    @BindView(R.id.f3_ptr)
    PtrClassicFrameLayout ptr;
    @BindView(R.id.f3_sc)
    ScrollView scrollView;
    @BindView(R.id.f3_ll1)
    LinearLayout ll_1;
    @BindView(R.id.f3_ll2)
    LinearLayout ll_2;
    @BindView(R.id.f3_ll3)
    LinearLayout ll_3;
    @BindView(R.id.f3_ll4)
    LinearLayout ll_4;
    @BindView(R.id.f3_gv1)
    MyGridView gv1;
    @BindView(R.id.f3_gv2)
    MyGridView gv2;
    @BindView(R.id.f3_gv3)
    MyGridView gv3;
    @BindView(R.id.f3_gv4)
    MyGridView gv4;
    @BindView(R.id.f3_v2)
    View view2;
    @BindView(R.id.f3_v3)
    View view3;
    @BindView(R.id.f3_v4)
    View view4;
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        View view = inflater.inflate(R.layout.fragment3, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @SuppressLint("NewApi")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getContext();
        //显示加载中视图
        multipleStatusView.showLoading();
        initData();
        request_zjdz(false);
        request_bszn(false);
    }


    private void initData() {
        //设置重试视图点击事件
        multipleStatusView.setOnRetryClickListener(mRetryClickListener);
        ptr.setLastUpdateTimeRelateObject(this);
        ptr.disableWhenHorizontalMove(true);
        ptr.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                // 下拉刷新操作
                request_zjdz(true);
                request_bszn(true);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, scrollView, header);
            }
        });
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();
        list4 = new ArrayList<>();
        adapter1 = new Fuwu_Adapter(mContext, list1);
        adapter2 = new Fuwu_Adapter(mContext, list2);
        adapter3 = new Fuwu_Adapter(mContext, list3);
        adapter4 = new Fuwu_Adapter(mContext, list4);
        gv1.setAdapter(adapter1);
        gv2.setAdapter(adapter2);
        gv3.setAdapter(adapter3);
        gv4.setAdapter(adapter4);
        Fuwu fuwu = new Fuwu();
        fuwu.setIdentifier("gongz");
        fuwu.setTitle("工作动态");
        Fuwu fuwu1 = new Fuwu();
        fuwu1.setTitle("通知公告");
        fuwu1.setIdentifier("tzgg");
        list1.add(fuwu);
        list1.add(fuwu1);
        adapter1.notifyDataSetChanged();
        Fuwu fuwu4 = new Fuwu();
        fuwu4.setTitle("新闻头条");
        Fuwu fuwu5 = new Fuwu();
        fuwu5.setTitle("微信精选");
        Fuwu fuwu6 = new Fuwu();
        fuwu6.setTitle("历史的今天");
        Fuwu fuwu7 = new Fuwu();
        fuwu7.setTitle("实时影讯");
        Fuwu fuwu8 = new Fuwu();
        fuwu8.setTitle("轻松一刻");
        list4.add(fuwu4);
        list4.add(fuwu5);
        list4.add(fuwu6);
        list4.add(fuwu7);
        list4.add(fuwu8);
        adapter4.notifyDataSetChanged();
        gv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(mContext, FuwuListActivity.class).putExtra("id", "news").putExtra("title", list1.get(i).getTitle()).putExtra("identifier", list1.get(i).getIdentifier()));
            }
        });
        gv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(mContext, FuwuListActivity.class).putExtra("id", "zoujindongzhi").putExtra("title", list2.get(i).getTitle()).putExtra("identifier", list2.get(i).getIdentifier()));
            }
        });
        gv3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(mContext, WebActivity.class).putExtra("title", list3.get(i).getTitle()).putExtra("url", list3.get(i).getLinkurl()));
            }
        });
        gv4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (list4.get(i).getTitle()) {
                    case "新闻头条":
                    startActivity(new Intent(mContext, NewsTopActivity.class));
                        break;
                    case "微信精选":
                    startActivity(new Intent(mContext, WXJXActivity.class));
                        break;
                    case "历史的今天":
                    startActivity(new Intent(mContext, LsActivity.class));
                        break;
                    case "实时影讯":
                    startActivity(new Intent(mContext, WepiaoActivity.class));
                        break;
                    case "轻松一刻":
                    startActivity(new Intent(mContext, JokesActivity.class));
                        break;
                }
            }
        });
    }

    private void request_bszn(final boolean b) {
        Map<String, String> map = new HashMap<>();
        map.put("c", "data");
        map.put("data", "appbszn");
        AppContext.getInstance().getmMyOkHttp().post(mContext, AppConfig.request_Data, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                try {
                    L.d("办事指南：", response);
                    JSONObject object = new JSONObject(response);
                    if (object.getString("status").equals("ok")) {
                        List<Fuwu> fuwuList = JSON.parseArray(object.getString("content"), Fuwu.class);
                        if (fuwuList != null && fuwuList.size() > 0) {
                            ll_3.setVisibility(View.VISIBLE);
                            view3.setVisibility(View.VISIBLE);
                            if (b) {
                                list3.clear();
                            }
                            list3.addAll(fuwuList);
                            adapter3.notifyDataSetChanged();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (b) {
                    ptr.refreshComplete();
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                if (b) {
                    ptr.refreshComplete();
                }
            }
        });
    }

    private void request_zjdz(final boolean isreflush) {
        Map<String, String> map = new HashMap<>();
        map.put("c", "data");
        map.put("data", "appzjdz");
        AppContext.getInstance().getmMyOkHttp().post(mContext, AppConfig.request_Data, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    try {
                        L.d("走进东至：", response);
                        JSONObject object = new JSONObject(response);
                        if (object.getString("status").equals("ok")) {
                            List<Fuwu> fuwuList = JSON.parseArray(object.getString("content"), Fuwu.class);
                            if (fuwuList != null && fuwuList.size() > 0) {
                                ll_2.setVisibility(View.VISIBLE);
                                view2.setVisibility(View.VISIBLE);
                                if (isreflush) {
                                    list2.clear();
                                }
                                list2.addAll(fuwuList);
                                multipleStatusView.showContent();
                            } else {
                                multipleStatusView.showEmpty();
                            }
                        } else {
                            multipleStatusView.showError();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        multipleStatusView.showError();
                    }
                }
                if (isreflush) {
                    ptr.refreshComplete();
                }
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                if (isreflush) {
                    ptr.refreshComplete();
                }
                try {
                    multipleStatusView.showNoNetwork();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    final View.OnClickListener mRetryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            multipleStatusView.showLoading();
            request_zjdz(true);
            request_bszn(true);
        }
    };
}
