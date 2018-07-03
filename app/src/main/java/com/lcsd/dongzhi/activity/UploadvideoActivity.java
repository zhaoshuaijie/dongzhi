package com.lcsd.dongzhi.activity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.VideoView;

import com.gyf.barlibrary.ImmersionBar;
import com.lcsd.dongzhi.R;

import butterknife.BindView;

public class UploadvideoActivity extends BaseBindActivity implements View.OnClickListener{
    @BindView(R.id.localvideo_video)
    VideoView videoView;
    @BindView(R.id.localvideo_left_back)
    ImageView iv_back;
    @BindView(R.id.localvideo_right_delete)
    ImageView iv_delete;
    private Intent intent;
    private Uri path;
    private static final int WATCHVIDEO=5;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_upolad_video;
    }
    @Override
    protected void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // LOLLIPOP解决方案
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // KITKAT解决方案
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    protected void initData() {
        if(getIntent()!=null){
            intent=getIntent();
            path= Uri.parse(intent.getStringExtra("videouri"));
        }
        iv_back.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
        videoView.setVideoURI(path);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.setVideoURI(path);
                videoView.start();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.localvideo_left_back:
                finish();
                break;
            case R.id.localvideo_right_delete:
                intent.putExtra("delete",1);
                setResult(WATCHVIDEO,intent);
                finish();
                break;
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (videoView != null) {
            videoView.stopPlayback();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.stopPlayback();
        }
    }
}
