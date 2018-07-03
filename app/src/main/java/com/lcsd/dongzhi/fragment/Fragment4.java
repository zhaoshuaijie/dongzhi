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
import android.widget.GridView;

import com.alibaba.fastjson.JSON;
import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.activity.DianboListActivity;
import com.lcsd.dongzhi.adapter.Dianbo_Adapter;
import com.lcsd.dongzhi.entity.Dianbo;
import com.lcsd.dongzhi.http.AppConfig;
import com.lcsd.dongzhi.http.AppContext;
import com.lcsd.dongzhi.util.L;
import com.lcsd.dongzhi.view.MultipleStatusView;
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
public class Fragment4 extends Fragment {
    @BindView(R.id.gv_dianbo)
    GridView gv;
    @BindView(R.id.f4_ptr)
    PtrClassicFrameLayout ptr;
    private Context mContext;
    private List<Dianbo> list;
    private Dianbo_Adapter adapter;
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        View view = inflater.inflate(R.layout.fragment4, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getContext();
        //显示加载中视图
        multipleStatusView.showLoading();
        initData();
        requestDate(false);
    }

    private void initData() {
        //设置重试视图点击事件
        multipleStatusView.setOnRetryClickListener(mRetryClickListener);
        list = new ArrayList<>();
        adapter = new Dianbo_Adapter(mContext, list);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getContext(), DianboListActivity.class).putExtra("title", list.get(i).getTitle()).putExtra("identifier", list.get(i).getIdentifier()));
            }
        });
        ptr.setLastUpdateTimeRelateObject(this);
        ptr.disableWhenHorizontalMove(true);
        ptr.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                // 下拉刷新操作
                requestDate(true);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, gv, header);
            }
        });
    }

    private void requestDate(final boolean isrefresh) {
        Map<String, String> map = new HashMap<>();
        map.put("c", "data");
        map.put("data", "appdbfl");
        AppContext.getInstance().getmMyOkHttp().post(getActivity(), AppConfig.request_Data, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    L.d("点播列表：", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getString("status").equals("ok")) {
                            if (isrefresh) {
                                list.clear();
                            }
                            List<Dianbo> centerEntities = JSON.parseArray(object.getString("content"), Dianbo.class);
                            list.addAll(centerEntities);
                            multipleStatusView.showContent();
                        } else {
                            multipleStatusView.showEmpty();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        multipleStatusView.showError();
                    }
                    if (isrefresh) {
                        ptr.refreshComplete();
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                L.d("TAG", "中心菜单:" + error_msg);
                ptr.refreshComplete();
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
            requestDate(true);
        }
    };
}
