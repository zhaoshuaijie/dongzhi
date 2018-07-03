package com.lcsd.dongzhi.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bolex.pressscan.ScanTools;
import com.gyf.barlibrary.ImmersionBar;
import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.util.StringUtil;

import butterknife.BindView;

public class AboutActivity extends BaseBindActivity implements View.OnClickListener {
    @BindView(R.id.top_view)
    View mView;
    @BindView(R.id.titlebar_title)
    TextView tv_title;
    @BindView(R.id.about_iv)
    ImageView iv;
    private static final String PATH = "/storage/emulated/0";

    @Override
    protected int setLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.statusBarView(mView).init();
    }

    @Override
    protected void initData() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.3f)
                .init();
        findViewById(R.id.ll_titlebar_left).setOnClickListener(this);
        tv_title.setText("关于我们");
        iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDialog();
                return true;
            }
        });
    }

    private void showDialog() {
        BitmapDrawable bmpDrawable = (BitmapDrawable) iv.getDrawable();
        final Bitmap bitmap = bmpDrawable.getBitmap();
        new AlertView("图片操作", null, "取消", null,
                new String[]{"保存图片", "识别图片中二维码"},
                this, AlertView.Style.ActionSheet, new OnItemClickListener() {
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    StringUtil.SavaImage(bitmap, PATH, AboutActivity.this);
                } else if (position == 1) {
                    ScanTools.scanCode(iv, new ScanTools.ScanCall() {
                        @Override
                        public void getCode(String code) {
                            startActivity(new Intent(AboutActivity.this, WebActivity.class).putExtra("url", code).putExtra("title", "App下载"));
                        }
                    });
                }
            }
        }).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_titlebar_left:
                this.finish();
                break;
        }
    }
}
