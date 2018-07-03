package com.lcsd.dongzhi.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.gyf.barlibrary.ImmersionBar;
import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.adapter.WxjxAdapter;
import com.lcsd.dongzhi.entity.Wxjx;
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
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class WXJXActivity extends BaseBindActivity implements View.OnClickListener {
    @BindView(R.id.titlebar_title)
    TextView tv_title;
    @BindView(R.id.ll_titlebar_left)
    LinearLayout ll_back;
    @BindView(R.id.top_view)
    View mView;
    private Context mContext;
    @BindView(R.id.lv_wxjx)
    ListView mlv;
    @BindView(R.id.ptr_wxjx)
    PtrClassicFrameLayout ptr;
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;
    private WxjxAdapter mAdapter;
    private List<Wxjx.TList> mlist;
    private int pno = 1, ps = 30, totalPage;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_wxjx;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.statusBarView(mView).init();
    }

    @Override
    protected void initData() {
        mContext = this;
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.3f)
                .init();
        tv_title.setText("微信精选");
        ll_back.setOnClickListener(this);
        //设置重试视图点击事件
        multipleStatusView.setOnRetryClickListener(mRetryClickListener);
        mlist = new ArrayList<>();
        mAdapter = new WxjxAdapter(mContext, mlist);
        mlv.setAdapter(mAdapter);
        mlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(mContext, WebViewActivity.class).putExtra("title", "微信精选").putExtra("url", mlist.get(i).getUrl()));
            }
        });
        ptr.setLastUpdateTimeRelateObject(this);
        ptr.disableWhenHorizontalMove(true);
        ptr.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                if (pno < totalPage) {
                    pno++;
                    request_data(2);
                }
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                // 下拉刷新操作
                request_data(1);
            }

            @Override
            public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
                if (pno < totalPage) {
                    return super.checkCanDoLoadMore(frame, mlv, footer);
                } else {
                    return false;
                }
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mlv, header);
            }
        });
        request_data(0);
    }

    private void request_data(final int i) {
        Map<String, String> map = new HashMap<>();
        if (i == 1 || i == 0) {
            map.put("pno", "1");
            pno = 1;
        } else {
            map.put("pno", pno + "");
        }
        map.put("ps", ps + "");
        map.put("key", AppConfig.WxjxKey);
        map.put("dtype", "json");
        AppContext.getInstance().getmMyOkHttp().post(mContext, AppConfig.Wxinjx, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    L.d("微信精选：", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getInt("error_code") == 0) {
                            Wxjx wxjx = JSON.parseObject(object.getString("result"), Wxjx.class);
                            if (wxjx != null) {
                                totalPage = wxjx.getTotalPage();
                                if (wxjx.getList() != null) {
                                    if (i == 1) {
                                        mlist.clear();
                                    }
                                    mlist.addAll(wxjx.getList());
                                    mAdapter.notifyDataSetChanged();
                                    multipleStatusView.showContent();
                                }
                            }
                        } else {
                            Toast.makeText(mContext, object.getString("reason"), Toast.LENGTH_SHORT).show();
                            multipleStatusView.showEmpty();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        multipleStatusView.showError();
                    }
                    if (i == 1 || i == 2) {
                        ptr.refreshComplete();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                L.d("TAG", "获取失败:" + error_msg);
                if (i == 1 || i == 2) {
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_titlebar_left:
                finish();
                break;
        }
    }

    final View.OnClickListener mRetryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            multipleStatusView.showLoading();
            request_data(1);
        }
    };
}
