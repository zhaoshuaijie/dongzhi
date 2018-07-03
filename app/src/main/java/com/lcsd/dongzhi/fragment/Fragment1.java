package com.lcsd.dongzhi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.alibaba.fastjson.JSON;
import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.entity.NewsTitle;
import com.lcsd.dongzhi.http.AppConfig;
import com.lcsd.dongzhi.http.AppContext;
import com.lcsd.dongzhi.util.L;
import com.lcsd.dongzhi.view.MultipleStatusView;
import com.lcsd.dongzhi.view.PagerSlidingTabStrip;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/15.
 */
public class Fragment1 extends Fragment {
    @BindView(R.id.psts_tab)
    PagerSlidingTabStrip tabs;
    @BindView(R.id.view_pager)
    ViewPager pager;
    private MyPagerAdapter adapter;
    private List<NewsTitle> entities = new ArrayList<>();
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        View view = inflater.inflate(R.layout.fragment1, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pager.setOffscreenPageLimit(4);
        //显示加载中视图
        multipleStatusView.showLoading();
        initData();
        request_toptitle();
    }

    private void initData() {
        //设置重试视图点击事件
        multipleStatusView.setOnRetryClickListener(mRetryClickListener);
    }

    final View.OnClickListener mRetryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            multipleStatusView.showLoading();
            request_toptitle();
        }
    };

    private void request_toptitle() {
        Map<String, String> map = new HashMap<>();
        map.put("c", "data");
        map.put("data", "appclass");
        AppContext.getInstance().getmMyOkHttp().post(getActivity(), AppConfig.request_Data, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    L.d("新闻分类栏：", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getString("status").equals("ok")) {
                            List<NewsTitle> centerEntities = JSON.parseArray(object.getString("content"), NewsTitle.class);
                            if (entities != null) {
                                entities.clear();
                            }
                            entities.addAll(centerEntities);
                            if (entities != null) {
                                adapter = new MyPagerAdapter(getActivity().getSupportFragmentManager(), entities);
                                pager.setAdapter(adapter);
                                tabs.setViewPager(pager);
                                pager.setCurrentItem(0);
                                setTabsValue();
                                adapter.notifyDataSetChanged();
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

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private List<NewsTitle> list;

        public MyPagerAdapter(FragmentManager fm, List<NewsTitle> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return list.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putString("identifier", list.get(position).getIdentifier());
            bundle.putString("title", list.get(position).getTitle());
            return FragmentContent.getInstance(bundle);
        }

    }

    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(true);

        // 设置Tab的分割线的颜色
        tabs.setDividerColor(getResources().getColor(R.color.transparent));
        // 设置分割线的上下的间距,传入的是dp
        tabs.setDividerPaddingTopBottom(12);

        // 设置Tab底部线的高度,传入的是dp
        tabs.setUnderlineHeight(1);
        //设置Tab底部线的颜色
        tabs.setUnderlineColor(getResources().getColor(R.color.transparent));

        // 设置Tab 指示器Indicator的高度,传入的是dp
        tabs.setIndicatorHeight(2);//滑动线的厚度
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColor(getResources().getColor(R.color.red));

        // 设置Tab标题文字的大小,传入的是dp
        tabs.setTextSize(15);
        // 设置选中Tab文字的颜色
        tabs.setSelectedTextColor(getResources().getColor(R.color.red));
        //设置正常Tab文字的颜色
        tabs.setTextColor(getResources().getColor(R.color.huise2));

        //  设置点击Tab时的背景色
        tabs.setTabBackground(R.drawable.background_tab);

        //是否支持动画渐变(颜色渐变和文字大小渐变)
        tabs.setFadeEnabled(true);
        // 设置最大缩放,是正常状态的0.3倍
        tabs.setZoomMax(0.05F);//增大倍数
        //设置Tab文字的左右间距,传入的是dp
        tabs.setTabPaddingLeftRight(10);
    }
}
