package com.lcsd.dongzhi.activity;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.gyf.barlibrary.ImmersionBar;
import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.adapter.LsImgAdapter;
import com.lcsd.dongzhi.entity.Ls2;
import com.lcsd.dongzhi.http.AppConfig;
import com.lcsd.dongzhi.http.AppContext;
import com.lcsd.dongzhi.view.MultipleStatusView;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class LsContnetActivity extends BaseBindActivity implements View.OnClickListener {
    @BindView(R.id.titlebar_title)
    TextView tv_title;
    @BindView(R.id.ll_titlebar_left)
    LinearLayout ll_back;
    @BindView(R.id.top_view)
    View mView;
    private Context mContext;
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;
    @BindView(R.id.lv_ls2)
    ListView lv;
    private View view_head;
    private TextView tv_contnt_title, tv_contnet;
    private String e_id;
    private List<Ls2.TResult.TPicUrl> list;
    private LsImgAdapter adapter;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_ls_contnet;
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
        tv_title.setText("新闻头条");
        ll_back.setOnClickListener(this);
        if (getIntent() != null) {
            e_id = getIntent().getStringExtra("e_id");
        }
        //设置重试视图点击事件
        multipleStatusView.setOnRetryClickListener(mRetryClickListener);
        //显示加载中视图
        multipleStatusView.showLoading();
        list = new ArrayList<>();
        adapter = new LsImgAdapter(mContext, list);
        lv.setAdapter(adapter);
        view_head = View.inflate(mContext, R.layout.item_lslv_head, null);
        tv_contnt_title = (TextView) view_head.findViewById(R.id.ls2_title);
        tv_contnet = (TextView) view_head.findViewById(R.id.ls2_context);
        lv.addHeaderView(view_head);

        request_data();
    }

    private void request_data() {
        final Map<String, String> map = new HashMap<>();
        map.put("key", AppConfig.Lskey);
        map.put("e_id", e_id);
        AppContext.getInstance().getmMyOkHttp().post(mContext, AppConfig.Lssdjt2, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    Ls2 ls2 = JSON.parseObject(response, Ls2.class);
                    if (ls2.getError_code() == 0) {
                        if (ls2.getResult() != null && ls2.getResult().size() > 0) {
                            tv_contnt_title.setText(ls2.getResult().get(0).getTitle());
                            tv_contnet.setText(ls2.getResult().get(0).getContent());
                            list.clear();
                            list.addAll(ls2.getResult().get(0).getPicUrl());
                            adapter.notifyDataSetChanged();
                            multipleStatusView.showContent();
                        } else {
                            multipleStatusView.showEmpty();
                        }
                    } else {
                        Toast.makeText(mContext, ls2.getReason(), Toast.LENGTH_SHORT).show();
                        multipleStatusView.showError();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
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
            request_data();
        }
    };
}
