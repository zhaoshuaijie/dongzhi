package com.lcsd.dongzhi.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import com.alibaba.fastjson.JSON;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.activity.LoginActivity;
import com.lcsd.dongzhi.activity.PaikeUploadActivity;
import com.lcsd.dongzhi.adapter.PaikeCircleAdapter;
import com.lcsd.dongzhi.entity.Circle;
import com.lcsd.dongzhi.http.AppConfig;
import com.lcsd.dongzhi.http.AppContext;
import com.lcsd.dongzhi.util.L;
import com.lzy.ninegrid.NineGridView;
import com.melnykov.fab.FloatingActionButton;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
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
public class Fragment5 extends Fragment implements OnItemClickListener {
    private Context mContext;
    private int pageid = 1;
    private int total;
    private ArrayList<Circle.TRslist> mData;
    private PaikeCircleAdapter mAdapter;
    private AlertView mAlertView;
    @BindView(R.id.lv_paike)
    ListView lv;
    @BindView(R.id.paike_ptrframelayout)
    PtrClassicFrameLayout ptr;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        View view = inflater.inflate(R.layout.fragment5, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getContext();
        NineGridView.setImageLoader(new GlideImageLoader());
        initData();
        request_paike(0);
    }

    private void initData() {
        mAlertView = new AlertView("提示", "上传拍客需要先登录，去登录？", "取消", new String[]{"确定"}, null, mContext, AlertView.Style.Alert, this).setCancelable(true);
        mData = new ArrayList<>();
        mAdapter = new PaikeCircleAdapter(mContext, mData);
        lv.setAdapter(mAdapter);
        fab.attachToListView(lv);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppContext.getInstance().checkUser()) {
                    startActivity(new Intent(mContext, PaikeUploadActivity.class));
                } else {
                    mAlertView.show();
                }
            }
        });
        ptr = (PtrClassicFrameLayout) getActivity().findViewById(R.id.paike_ptrframelayout);
        ptr.setLastUpdateTimeRelateObject(this);
        ptr.disableWhenHorizontalMove(true);
        ptr.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                if (pageid < total) {
                    pageid++;
                    request_paike(2);
                }
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                // 下拉刷新操作
                request_paike(1);
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
    }

    private void request_paike(final int i) {
        Map<String, String> map = new HashMap<>();
        map.put("id", "paike");
        if (i == 1 || i == 0) {
            map.put("pageid", 1 + "");
            pageid = 1;
        } else {
            map.put("pageid", pageid + "");
        }
        map.put("psize", 10 + "");
        AppContext.getInstance().getmMyOkHttp().post(mContext, AppConfig.request_Data, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    L.d("拍客列表：", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getString("status").equals("ok")) {
                            Circle circle = JSON.parseObject(object.getString("content"), Circle.class);
                            total = circle.getTotal();
                            if (i == 1) {
                                mData.clear();
                            }
                            if (circle != null && circle.getRslist() != null) {
                                mData.addAll(circle.getRslist());
                                mAdapter.notifyDataSetChanged();
                            }
                            if (i == 1 || i == 2) {
                                ptr.refreshComplete();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                L.d("TAG", error_msg);
                if (i == 1 || i == 2) {
                    ptr.refreshComplete();
                }
            }
        });
    }

    @Override
    public void onItemClick(Object o, int position) {
        if (position != AlertView.CANCELPOSITION) {
            startActivity(new Intent(mContext, LoginActivity.class));
        }
    }

    /**
     * 设置Glide 加载
     */
    private class GlideImageLoader implements NineGridView.ImageLoader {
        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            Glide.with(context).load(url)
                    .placeholder(R.drawable.ic_default_color)
                    .error(R.drawable.ic_default_color)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }
}
