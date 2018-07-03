package com.lcsd.dongzhi.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.photoview.PhotoView;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaikeImagePageActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.paike_img_tv)
    TextView tv;
    @BindView(R.id.paike_img_viewpager)
    ViewPager vp;
    @BindView(R.id.iv_left_back)
    ImageView iv_back;
    @BindView(R.id.iv_right_delete)
    ImageView iv_delete;
    private static final int DELETE_PHOTO=3;
    private String[] uri;
    //点击上个页面图片的索引
    private int index;
    private Context mContext;
    private ArrayList<View> listViews = null;
    private MyPageAdapter pagerAdapter;
    private Intent intent;

    public static ArrayList<Uri> teamlist=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paike_image_page);
        ButterKnife.bind(this);
        ImmersionBar.with(this)
                .transparentBar()
                .init();
        if(teamlist!=null){
            teamlist.clear();
        }
        mContext = this;
        if (getIntent() != null) {
            intent=getIntent();
            uri = getIntent().getStringArrayExtra("imgs");
            index = getIntent().getIntExtra("index", 0);
        }
        initData();
    }

    private void initData() {
        if (listViews == null) {
            listViews = new ArrayList<>();
            for (int i = 0; i < uri.length; i++) {
                PhotoView pv = new PhotoView(this);
                pv.enable();
                pv.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Glide.with(mContext).load(uri[i]).into(pv);
                listViews.add(pv);
                teamlist.add(Uri.parse(uri[i]));
            }
        }
        tv.setText(listViews.size() + "/" + 9);
        iv_back.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
        vp.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
        pagerAdapter = new MyPageAdapter(listViews);
        vp.setAdapter(pagerAdapter);
        vp.setCurrentItem(index);
    }


    class MyPageAdapter extends PagerAdapter {
        private ArrayList<View> listViews;
        private int size;

        public MyPageAdapter(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        @Override
        public int getCount() {
            return size;
        }

        public void setListViews(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            try {
                container.addView(listViews.get(position % size), 0);
            } catch (Exception e) {
            }
            return listViews.get(position % size);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(listViews.get(position % size));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left_back:
                intent.putExtra("type","1");
                setResult(DELETE_PHOTO,intent);
                finish();
                break;
            case R.id.iv_right_delete:
                if (listViews.size() == 1) {
                    intent.putExtra("type","0");
                    setResult(DELETE_PHOTO,intent);
                    finish();
                } else {
                    teamlist.remove(vp.getCurrentItem());
                    vp.removeAllViews();
                    if (listViews != null) {
                        listViews .clear();
                        for (int i = 0; i < teamlist.size(); i++) {
                            PhotoView pv = new PhotoView(this);
                            pv.enable();
                            pv.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            Glide.with(mContext).load(teamlist.get(i)).into(pv);
                            listViews.add(pv);
                        }
                    }
                    pagerAdapter.setListViews(listViews);
                    tv.setText(listViews.size() + "/" + 9);
                    pagerAdapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
