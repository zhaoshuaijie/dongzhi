package com.lcsd.dongzhi.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.activity.ImagePageActivity;
import com.lcsd.dongzhi.adapter.JokesAdapter;
import com.lcsd.dongzhi.entity.Jokes;
import com.lcsd.dongzhi.http.AppConfig;
import com.lcsd.dongzhi.http.AppContext;
import com.lcsd.dongzhi.util.L;
import com.lcsd.dongzhi.view.MultipleStatusView;
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
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Administrator on 2018/3/14.
 */
public class Fragment_jokes extends Fragment {
    @BindView(R.id.lv_jokes)
    ListView lv;
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;
    @BindView(R.id.ptr_jokes)
    PtrClassicFrameLayout ptr;
    private Context mContext;
    private List<Jokes> list;
    private JokesAdapter adapter;
    private String type;

    public static Fragment getInstance(Bundle bundle) {
        Fragment_jokes fragment = new Fragment_jokes();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jokes, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        initData();
        request_data(false);
    }

    private void initData() {
        //显示加载中视图
        multipleStatusView.showLoading();
        //设置重试视图点击事件
        multipleStatusView.setOnRetryClickListener(mRetryClickListener);
        type = getArguments().getString("type");
        list = new ArrayList<>();
        adapter = new JokesAdapter(mContext, list);
        lv.setAdapter(adapter);
        ptr.setLastUpdateTimeRelateObject(this);
        ptr.disableWhenHorizontalMove(true);
        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                request_data(true);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, lv, header);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (list.get(i).getUrl() != null && list.get(i).getUrl().length() > 0) {
                    startActivity(new Intent(mContext, ImagePageActivity.class).putExtra("imgs", new String[]{list.get(i).getUrl()}).putExtra("index", 0));
                }
            }
        });
    }

    private void request_data(final boolean isrefresh) {
        Map<String, String> map = new HashMap<>();
        map.put("key", AppConfig.JokesKey);
        if (type != null) {
            map.put("type", type);
        }
        AppContext.getInstance().getmMyOkHttp().post(mContext, AppConfig.Jokes, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    L.d("笑话大全：", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getInt("error_code") == 0) {
                            List<Jokes> jokes = JSON.parseArray(object.getString("result"), Jokes.class);
                            if (jokes != null && jokes.size() > 0) {
                                list.addAll(0, jokes);
                                adapter.notifyDataSetChanged();
                                multipleStatusView.showContent();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        multipleStatusView.showError();
                    }
                }
                if (isrefresh) {
                    ptr.refreshComplete();
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                if (isrefresh) {
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
            request_data(false);
        }
    };
}
