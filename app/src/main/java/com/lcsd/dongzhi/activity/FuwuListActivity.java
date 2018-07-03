package com.lcsd.dongzhi.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.gyf.barlibrary.ImmersionBar;
import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.adapter.FwListAdapter;
import com.lcsd.dongzhi.entity.FuwuList;
import com.lcsd.dongzhi.http.AppConfig;
import com.lcsd.dongzhi.http.AppContext;
import com.lcsd.dongzhi.util.L;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class FuwuListActivity extends BaseBindActivity implements View.OnClickListener {
    @BindView(R.id.titlebar_title)
    TextView tv_title;
    @BindView(R.id.ll_titlebar_left)
    LinearLayout ll_back;
    @BindView(R.id.lv_fw_list)
    ListView lv;
    @BindView(R.id.ptr_fuwulist)
    PtrClassicFrameLayout ptr;
    @BindView(R.id.top_view)
    View mView;
    private TextView tv_empty;
    private View emptyView;
    private Context mContext;
    private int pageid = 1;
    private int total;
    private String title, identifier, id;
    private List<FuwuList.TRs_lists> list;
    private FwListAdapter adapter;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_fuwu;
    }
    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.statusBarView(mView).init();
    }
    @Override
    protected void initView() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.3f)
                .init();
        mContext = this;
        if (getIntent().getExtras() != null) {
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            identifier = getIntent().getStringExtra("identifier");
        }
    }

    @Override
    protected void initData() {
        if (title != null) {
            tv_title.setText(title);
        }
        emptyView = View.inflate(this, R.layout.item_empty, null);
        tv_empty = (TextView) emptyView.findViewById(R.id.empty_tv);
        ll_back.setOnClickListener(this);
        ptr.setLastUpdateTimeRelateObject(this);
        ptr.disableWhenHorizontalMove(true);
        ptr.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                if (pageid < total) {
                    pageid++;
                    requestDianBoInfo(2);
                }
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                // 下拉刷新操作
                requestDianBoInfo(1);
            }

            @Override
            public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
                if (pageid < total) {
                    return super.checkCanDoLoadMore(frame, lv, footer);
                } else {
                    return false;
                }
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, lv, header);
            }
        });
        addContentView(emptyView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        lv.setEmptyView(emptyView);
        list = new ArrayList<>();
        adapter = new FwListAdapter(mContext, list, title);
        lv.setAdapter(adapter);
        if (identifier != null) {
            requestDianBoInfo(0);
        }
    }

    private void requestDianBoInfo(final int i) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("cate", identifier);
        if (i == 1 || i == 0) {
            map.put("pageid", "1");
            pageid = 1;
        } else {
            map.put("pageid", pageid + "");
        }
        AppContext.getInstance().getmMyOkHttp().post(mContext, AppConfig.request_Data, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    L.d("TAG", "服务列表:" + response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getString("status").equals("ok")) {
                            FuwuList info = JSON.parseObject(object.getString("content"), FuwuList.class);
                            if (info != null && info.getRs_lists() != null && info.getRs_lists().size() > 0) {
                                if (i == 1) {
                                    list.clear();
                                }
                                total = info.getTotal();
                                list.addAll(info.getRs_lists());
                            }
                        } else {
                            Toast.makeText(mContext, object.getString("content"), Toast.LENGTH_SHORT).show();
                        }
                        if (list.size() == 0) {
                            tv_empty.setText("暂无数据");
                            emptyView.findViewById(R.id.empty_progress).setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
                if (i == 1 || i == 2) {
                    ptr.refreshComplete();
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                L.d("TAG", "失败获取点播的数据:" + error_msg);
                tv_empty.setText("网络出错");
                emptyView.findViewById(R.id.empty_progress).setVisibility(View.GONE);
                ptr.refreshComplete();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_titlebar_left:
                finish();
                break;
        }
    }

}
