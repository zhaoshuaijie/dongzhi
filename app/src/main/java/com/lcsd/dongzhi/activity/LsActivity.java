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
import com.lcsd.dongzhi.adapter.LsAdapter;
import com.lcsd.dongzhi.entity.Ls1;
import com.lcsd.dongzhi.http.AppConfig;
import com.lcsd.dongzhi.http.AppContext;
import com.lcsd.dongzhi.util.L;
import com.lcsd.dongzhi.view.MultipleStatusView;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class LsActivity extends BaseBindActivity implements View.OnClickListener{
    @BindView(R.id.titlebar_title)
    TextView tv_title;
    @BindView(R.id.ll_titlebar_left)
    LinearLayout ll_back;
    @BindView(R.id.top_view)
    View mView;
    private Context mContext;
    @BindView(R.id.lv_ls)
    ListView mlv;
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;
    private List<Ls1.TResult> list;
    private LsAdapter adapter;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_ls;
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
        tv_title.setText("历史上的今天");
        ll_back.setOnClickListener(this);
        //设置重试视图点击事件
        multipleStatusView.setOnRetryClickListener(mRetryClickListener);
        //显示加载中视图
        multipleStatusView.showLoading();
        list=new ArrayList<>();
        adapter=new LsAdapter(mContext,list);
        mlv.setAdapter(adapter);
        mlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(mContext,LsContnetActivity.class).putExtra("e_id",list.get(i).getE_id()));
            }
        });
        request_data();
    }

    private void request_data() {
        Calendar c = Calendar.getInstance();
        int month=c.get(Calendar.MONTH)+1;
        int day=c.get(Calendar.DAY_OF_MONTH);
        final Map<String,String> map=new HashMap<>();
        map.put("key", AppConfig.Lskey);
        map.put("date",month+"/"+day);
        AppContext.getInstance().getmMyOkHttp().post(mContext, AppConfig.Lssdjt1, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if(response!=null){
                    L.d("历史上的今天：",response);
                    Ls1 ls= JSON.parseObject(response,Ls1.class);
                    if(ls.getError_code()==0){
                        if(ls.getResult()!=null&&ls.getResult().size()>0){
                            if(list.size()>0){
                                list.clear();
                            }
                            list.addAll(ls.getResult());
                            adapter.notifyDataSetChanged();
                            multipleStatusView.showContent();
                        }else {
                            multipleStatusView.showEmpty();
                        }
                    }else {
                        Toast.makeText(mContext,ls.getReason(),Toast.LENGTH_SHORT).show();
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
    final View.OnClickListener mRetryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            multipleStatusView.showLoading();
            request_data();
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_titlebar_left:
                finish();
                break;
        }
    }
}
