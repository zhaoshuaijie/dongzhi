package com.lcsd.dongzhi.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.http.AppConfig;
import com.lcsd.dongzhi.photoview.PhotoView;
import com.lcsd.dongzhi.util.StringUtil;

import butterknife.BindView;

public class ImagePageActivity extends BaseBindActivity {
    @BindView(R.id.img_tv)
    TextView tv;
    @BindView(R.id.img_viewpager)
    ViewPager vp;
    private String[] img;
    private int index;
    public static final String PATH1 = "/storage/emulated/0";

    @Override
    protected int setLayoutId() {
        return R.layout.activity_image_page;
    }
    @Override
    protected void initImmersionBar() {
        ImmersionBar.with(this)
                .transparentBar()
                .init();
    }
    @Override
    protected void initView() {
        if (getIntent() != null) {
            img = getIntent().getStringArrayExtra("imgs");
            index = getIntent().getIntExtra("index", 0);
        }
    }

    @Override
    protected void initData() {
        tv.setText((index + 1) + "/" + img.length);
        vp.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
        vp.setAdapter(pagerAdapter);
        vp.setCurrentItem(index);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tv.setText(position + 1 + "/" + img.length);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return img.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final PhotoView photoView = new PhotoView(ImagePageActivity.this);
            photoView.enable();
            photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            String s = img[position].substring(0, 4);
            if (s.equals("http")) {
                Glide.with(ImagePageActivity.this).load(img[position]).into(photoView);
            } else {
                Glide.with(ImagePageActivity.this).load(AppConfig.Mainurl + img[position]).into(photoView);
            }
            container.addView(photoView);
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImagePageActivity.this.finish();
                }
            });
            photoView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    showDialog(img[position], photoView);
                    return true;
                }
            });
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    };


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.animator.sacle_small);
    }

    private void showDialog(final String url, final PhotoView photoView) {
        new AlertView("保存图片", null, "取消", null,
                new String[]{"保存到本地"},
                this, AlertView.Style.ActionSheet, new OnItemClickListener() {
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    Drawable bmpDrawable = photoView.getDrawable();
                    final Bitmap bitmap = StringUtil.drawableToBitmap(bmpDrawable);
                    if(bitmap!=null){
                        StringUtil.SavaImage(bitmap, PATH1, ImagePageActivity.this);
                    }else {
                        Toast.makeText(ImagePageActivity.this,"保存失败",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).show();
    }

}
