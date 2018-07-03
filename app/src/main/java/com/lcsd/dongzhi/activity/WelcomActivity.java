package com.lcsd.dongzhi.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gyf.barlibrary.ImmersionBar;
import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.entity.UserInfo;
import com.lcsd.dongzhi.entity.kjgg;
import com.lcsd.dongzhi.http.AppConfig;
import com.lcsd.dongzhi.http.AppContext;
import com.lcsd.dongzhi.util.L;
import com.lcsd.dongzhi.view.MyVideoView;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomActivity extends BaseActivity {
    @BindView(R.id.iv_wel)
    ImageView iv;
    @BindView(R.id.wel_video)
    MyVideoView videoView;
    @BindView(R.id.wel_tv)
    TextView tv;
    private Context mContext;
    private String img, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = View.inflate(this, R.layout.activity_welcom, null);
        //渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
        aa.setDuration(2000);
        view.startAnimation(aa);
        ImmersionBar.with(this)
                .transparentBar()
                .init();
        setContentView(view);
        mContext = this;
        ButterKnife.bind(this);
        initView();
        request_gg();
        if (AppContext.getInstance().checkUser()) {
            requestLog();
        }
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                if (img != null && img.length() > 1) {
                    Glide.with(mContext).load(img).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            tv.setText("4 跳过");
                            tv.setVisibility(View.VISIBLE);
                            handler.sendEmptyMessageDelayed(1, 1000);
                            handler.sendEmptyMessageDelayed(2, 2000);
                            handler.sendEmptyMessageDelayed(3, 3000);
                            handler.sendEmptyMessageDelayed(4, 4000);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            tv.setText("4 跳过");
                            tv.setVisibility(View.VISIBLE);
                            handler.sendEmptyMessageDelayed(1, 1000);
                            handler.sendEmptyMessageDelayed(2, 2000);
                            handler.sendEmptyMessageDelayed(3, 3000);
                            handler.sendEmptyMessageDelayed(4, 4000);
                            return false;
                        }
                    }).into(iv);
                } else if (url != null && url.length() > 1) {
                    videoView.setBackgroundResource(R.drawable.img_wel);
                    videoView.setVisibility(View.VISIBLE);
                    videoView.setFitsSystemWindows(true);
                    videoView.setVideoPath(url);
                    videoView.start();
                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                            mp.setLooping(false);//不循环播放
                            mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                                @Override
                                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                                    if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START)
                                        tv.setText("4 跳过");
                                    tv.setVisibility(View.VISIBLE);
                                    handler.sendEmptyMessageDelayed(1, 1000);
                                    handler.sendEmptyMessageDelayed(2, 2000);
                                    handler.sendEmptyMessageDelayed(3, 3000);
                                    handler.sendEmptyMessageDelayed(4, 4000);
                                    videoView.setBackgroundColor(Color.TRANSPARENT);
                                    return true;
                                }
                            });
                        }
                    });
                } else {
                    redirectTo();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }

        });

    }

    private void requestLog() {
        SharedPreferences sharedPreferences = getSharedPreferences("dongzhiUserInfo", Context.MODE_PRIVATE);
        Map<String, String> map = new HashMap<>();
        map.put("c", "login");
        map.put("mobile", sharedPreferences.getString("userid", ""));
        map.put("pass", sharedPreferences.getString("pwd", ""));
        AppContext.getInstance().getmMyOkHttp().post(this, AppConfig.request_Data, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    try {
                        L.d("TAG", "登陆状态：" + response);
                        JSONObject object = new JSONObject(response);
                        if (object.getString("status").equals("ok")) {
                            requestLUserInfo();
                        }
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {

            }
        });
    }

    private void requestLUserInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("c", "usercp");
        AppContext.getInstance().getmMyOkHttp().post(this, AppConfig.request_Data, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    try {
                        L.d("TAG", "获取用户数据:" + response);
                        JSONObject object = new JSONObject(response);
                        if (object.getString("status").equals("ok")) {
                            List<UserInfo> list = JSON.parseArray(object.getString("content"), UserInfo.class);
                            if (list != null && list.size() > 0) {
                                UserInfo info = list.get(0);
                                AppContext.getInstance().saveUserInfo(info);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                L.d("TAG", "获取用户数据失败:" + error_msg);
            }
        });
    }

    protected void initView() {
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeMessages(4);
                redirectTo();
            }
        });
    }

    /**
     * 跳转到...
     */
    private void redirectTo() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //广告
    private void request_gg() {
        final Map<String, String> map = new HashMap<>();
        map.put("c", "data");
        map.put("data", "appad");
        AppContext.getInstance().getmMyOkHttp().post(AppConfig.request_Data, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    L.d("开机广告：", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getString("status").equals("ok")) {
                            List<kjgg> list = JSON.parseArray(String.valueOf(object.getJSONArray("content")), kjgg.class);
                            if (list != null && list.size() > 0) {
                                img = list.get(0).getPic();
                                url = list.get(0).getVideo();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                L.d("开机广告：", error_msg);
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

        }
        return false;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    tv.setText("3 跳过");
                    break;
                case 2:
                    tv.setText("2 跳过");
                    break;
                case 3:
                    tv.setText("1 跳过");
                    break;
                case 4:
                    redirectTo();
                    break;
            }
        }
    };
}
