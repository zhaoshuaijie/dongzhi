package com.lcsd.dongzhi.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.fragment.Fragment2;
import com.lcsd.dongzhi.http.AppConfig;
import com.lcsd.dongzhi.http.AppContext;
import com.lcsd.dongzhi.manage.UpdateManager;
import com.lcsd.dongzhi.util.L;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class MainActivity extends BaseBindActivity implements View.OnClickListener {
    @BindView(R.id.top_view)
    View mView;
    @BindView(R.id.iv_01)
    ImageView iv_01;
    @BindView(R.id.iv_02)
    ImageView iv_02;
    @BindView(R.id.iv_03)
    ImageView iv_03;
    @BindView(R.id.iv_04)
    ImageView iv_04;
    @BindView(R.id.iv_05)
    ImageView iv_05;
    @BindView(R.id.tv_01)
    TextView tv_01;
    @BindView(R.id.tv_02)
    TextView tv_02;
    @BindView(R.id.tv_03)
    TextView tv_03;
    @BindView(R.id.tv_04)
    TextView tv_04;
    @BindView(R.id.tv_05)
    TextView tv_05;
    @BindView(R.id.iv_hy)
    ImageView iv_hy;
    @BindView(R.id.tv_title_main)
    TextView tv_title;
    @BindView(R.id.ll_01)
    LinearLayout ll_1;
    @BindView(R.id.ll_02)
    LinearLayout ll_2;
    @BindView(R.id.ll_03)
    LinearLayout ll_3;
    @BindView(R.id.ll_04)
    LinearLayout ll_4;
    @BindView(R.id.ll_05)
    LinearLayout ll_5;
    private Context mContext;
    private Fragment[] mFragment;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.statusBarView(mView).init();
    }

    @Override
    protected void initView() {
        mContext = this;
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.3f)
                .init();
        this.mFragment = new Fragment[5];
        this.mFragmentManager = getSupportFragmentManager();
        mFragment[0] = this.mFragmentManager
                .findFragmentById(R.id.frg_01);
        mFragment[1] = this.mFragmentManager
                .findFragmentById(R.id.frg_02);
        mFragment[2] = this.mFragmentManager
                .findFragmentById(R.id.frg_03);
        mFragment[3] = this.mFragmentManager
                .findFragmentById(R.id.frg_04);
        mFragment[4] = this.mFragmentManager
                .findFragmentById(R.id.frg_05);
        mFragmentTransaction = mFragmentManager.beginTransaction()
                .hide(mFragment[0]).hide(mFragment[1]).hide(mFragment[2]).hide(mFragment[3]).hide(mFragment[4]);
        mFragmentTransaction.show(mFragment[1]).commit();
    }

    @Override
    protected void initData() {
        new UpdateManager(mContext);
        iv_hy.setOnClickListener(this);
        ll_1.setOnClickListener(onClickListener);
        ll_2.setOnClickListener(onClickListener);
        ll_3.setOnClickListener(onClickListener);
        ll_4.setOnClickListener(onClickListener);
        ll_5.setOnClickListener(onClickListener);
        tv_title.setText("直  播");
        iv_01.setBackgroundResource(R.drawable.img_news);
        iv_02.setBackgroundResource(R.drawable.img_zhibois);
        iv_03.setBackgroundResource(R.drawable.img_service);
        iv_04.setBackgroundResource(R.drawable.img_db);
        iv_05.setBackgroundResource(R.drawable.img_paike);
        request_appgt();
    }

    private void request_appgt() {
        Map<String, String> map = new HashMap<>();
        map.put("f", "status");
        AppContext.getInstance().getmMyOkHttp().post(mContext, AppConfig.request_Data, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    try {
                        JSONObject object = new JSONObject(response);
                        L.d("app关停功能：", response);
                        String status = object.getString("status");
                        if (status.equals("ok")) {
                            String content = object.getString("content");
                            Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
                            handler.sendEmptyMessageDelayed(1, 2000);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                L.d("app关停功能：", error_msg);
            }
        });
    }

    /**
     * 返回键两次退出程序
     */
    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Fragment2.mPlayerView.handleVolumeKey(keyCode)) {
            return true;
        } else if (Fragment2.mPlayerView != null && Fragment2.mPlayerView.IsFullscreen()) {
            Fragment2.mPlayerView.onBackPressed();
            return false;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_hy:
                startActivity(new Intent(mContext, HYActivity.class));
                break;
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            iv_01.setBackgroundResource(R.drawable.img_news);
            iv_02.setBackgroundResource(R.drawable.img_zhibo);
            iv_03.setBackgroundResource(R.drawable.img_service);
            iv_04.setBackgroundResource(R.drawable.img_db);
            iv_05.setBackgroundResource(R.drawable.img_paike);
            tv_01.setTextColor(Color.rgb(0x7a, 0x7e, 0x83));
            tv_02.setTextColor(Color.rgb(0x7a, 0x7e, 0x83));
            tv_03.setTextColor(Color.rgb(0x7a, 0x7e, 0x83));
            tv_04.setTextColor(Color.rgb(0x7a, 0x7e, 0x83));
            tv_05.setTextColor(Color.rgb(0x7a, 0x7e, 0x83));
            mFragmentTransaction = mFragmentManager.beginTransaction()
                    .hide(mFragment[0]).hide(mFragment[1]).hide(mFragment[2]).hide(mFragment[3]).hide(mFragment[4]);
            switch (view.getId()) {
                case R.id.ll_01:
                    mFragmentTransaction.show(mFragment[0]).commit();
                    iv_hy.setVisibility(View.INVISIBLE);
                    tv_title.setText("新  闻");
                    iv_01.setBackgroundResource(R.drawable.img_newsis);
                    tv_01.setTextColor(Color.rgb(0xf4, 0x4b, 0x50));
                    break;
                case R.id.ll_02:
                    mFragmentTransaction.show(mFragment[1]).commit();
                    iv_hy.setVisibility(View.VISIBLE);
                    iv_02.setBackgroundResource(R.drawable.img_zhibois);
                    tv_title.setText("直  播");
                    tv_02.setTextColor(Color.rgb(0xf4, 0x4b, 0x50));
                    break;
                case R.id.ll_03:
                    mFragmentTransaction.show(mFragment[2]).commit();
                    iv_hy.setVisibility(View.INVISIBLE);
                    iv_03.setBackgroundResource(R.drawable.img_serviceis);
                    tv_title.setText("服  务");
                    tv_03.setTextColor(Color.rgb(0xf4, 0x4b, 0x50));
                    break;
                case R.id.ll_04:
                    mFragmentTransaction.show(mFragment[3]).commit();
                    iv_hy.setVisibility(View.INVISIBLE);
                    tv_title.setText("点  播");
                    iv_04.setBackgroundResource(R.drawable.img_dbis);
                    tv_04.setTextColor(Color.rgb(0xf4, 0x4b, 0x50));
                    break;
                case R.id.ll_05:
                    mFragmentTransaction.show(mFragment[4]).commit();
                    iv_hy.setVisibility(View.INVISIBLE);
                    tv_title.setText("拍  客");
                    iv_05.setBackgroundResource(R.drawable.img_paikeis);
                    tv_05.setTextColor(Color.rgb(0xf4, 0x4b, 0x50));
                    break;
            }
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                MainActivity.this.finish();
            }
        }
    };

}
