package com.lcsd.dongzhi.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;
import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.dl7.player.media.IjkPlayerView;
import com.dl7.player.widgets.ShareDialog;
import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.adapter.ZhiboAdapter;
import com.lcsd.dongzhi.entity.Zhibo;
import com.lcsd.dongzhi.http.AppConfig;
import com.lcsd.dongzhi.http.AppContext;
import com.lcsd.dongzhi.util.L;
import com.lcsd.dongzhi.view.MultipleStatusView;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.utils.Log;
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
public class Fragment2 extends Fragment implements View.OnClickListener {
    @BindView(R.id.gv_zhibo)
    GridView gv;
    @BindView(R.id.f2_iv_gg)
    ImageView iv;
    @BindView(R.id.bt_zhibo)
    Button button1;
    @BindView(R.id.bt_gg)
    Button button2;
    @BindView(R.id.view_zhibo)
    View view1;
    @BindView(R.id.view_gg)
    View view2;
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;
    @BindView(R.id.fl_gb)
    FrameLayout fl_gb;
    @BindView(R.id.gb_video)
    VideoView videoView;
    public static IjkPlayerView mPlayerView;
    private ImageView iv_share, iv_share_window;
    private ZhiboAdapter adapter;
    private List<Zhibo> list;
    private Context mContext;
    private String videotitle, videopath;
    MediaController mctrl = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        View view = inflater.inflate(R.layout.fragment2, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @SuppressLint("NewApi")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getContext();
        //显示加载中视图
        multipleStatusView.showLoading();
        initView();
        initData();
        request_zhibolist();
    }

    private void initView() {
        mPlayerView = (IjkPlayerView) getActivity().findViewById(R.id.f2_player_view);
        iv_share = (ImageView) mPlayerView.findViewById(R.id.iv_share);
        iv_share_window = (ImageView) mPlayerView.findViewById(R.id.iv_share_window);
    }

    private void initData() {
        //设置重试视图点击事件
        multipleStatusView.setOnRetryClickListener(mRetryClickListener);

        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UMVideo video = new UMVideo(videopath);
                video.setTitle(videotitle);//视频的标题
                video.setThumb(new UMImage(mContext, R.drawable.img_thume_defult));//视频的缩略图
                video.setDescription(videotitle);//视频的描述
                new ShareAction((Activity) mContext).withMedia(video)
                        .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                        .setCallback(umShareListener).open();
            }
        });
        iv_share_window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UMVideo video = new UMVideo(videopath);
                video.setTitle(videotitle);//视频的标题
                video.setThumb(new UMImage(mContext, R.drawable.img_thume_defult));//视频的缩略图
                video.setDescription(videotitle);//视频的描述
                new ShareAction((Activity) mContext).withMedia(video)
                        .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                        .setCallback(umShareListener).open();
            }
        });
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        list = new ArrayList<>();
        Glide.with(mContext).load(R.drawable.img_video_thume).fitCenter().into(mPlayerView.mPlayerThumb);
        adapter = new ZhiboAdapter(mContext, list);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(list.get(i).getIdentifier().equals("guangbo")){
                    mPlayerView.setVisibility(View.GONE);
                    if(mPlayerView!=null){
                        mPlayerView.onPause();
                        mPlayerView.onDestroy();
                    }
                    fl_gb.setVisibility(View.VISIBLE);
                    initgbPlay(list.get(i).getFluent());
                }else {
                    if (videoView != null && videoView.isPlaying()) {
                        videoView.pause();
                        videoView.stopPlayback();
                    }
                    fl_gb.setVisibility(View.GONE);
                    mPlayerView.setVisibility(View.VISIBLE);
                    mPlayerView.setTitle(list.get(i).getTitle())
                            .switchVideoSource(list.get(i).getFluent() != null ? list.get(i).getFluent() : null, null, list.get(i).getHigh() != null ? list.get(i).getHigh() : null, list.get(i).getUltra() != null ? list.get(i).getUltra() : null, null)
                            .setMediaQuality(IjkPlayerView.MEDIA_QUALITY_SMOOTH)
                            .start();
                    if (list.get(i).getThumb() != null) {
                        Glide.with(mContext).load(list.get(i).getThumb()).into(iv);
                    }
                }
            }
        });
    }
    //播放广播
    private void initgbPlay(String url) {
        Uri path = Uri.parse(url);
        if(path!=null&&videoView!=null){
            videoView.setVideoURI(path);
            mctrl = new MediaController(getContext());
            videoView.setMediaController(mctrl);
            videoView.start();
        }
    }
    private void request_zhibolist() {
        Map<String, String> map = new HashMap<>();
        map.put("c", "data");
        map.put("data", "zbfldy");
        AppContext.getInstance().getmMyOkHttp().post(mContext, AppConfig.request_Data, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getString("status").equals("ok")) {
                            List<Zhibo> zhibos = JSON.parseArray(object.getString("content"), Zhibo.class);
                            if (zhibos != null && zhibos.size() > 0) {
                                if (list != null && list.size() > 0) {
                                    list.clear();
                                }
                                list.addAll(zhibos);
                                adapter.notifyDataSetChanged();
                                mPlayerView.init(true, true)
                                        .setTitle(list.get(0).getTitle())
                                        .setDialogClickListener(dialogClickListener)
                                        .setVideoSource(list.get(0).getFluent() != null ? list.get(0).getFluent() : null, null, list.get(0).getHigh() != null ? list.get(0).getHigh() : null, list.get(0).getUltra() != null ? list.get(0).getUltra() : null, null)
                                        .setMediaQuality(IjkPlayerView.MEDIA_QUALITY_SMOOTH);
                                videotitle = list.get(0).getTitle();
                                videopath = list.get(0).getFluent();
                                if (list.get(0).getThumb() != null) {
                                    Glide.with(mContext).load(list.get(0).getThumb()).into(iv);
                                }
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
                L.d("请求失败:", error_msg);
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
        button1.setTextColor(getResources().getColor(R.color.huise2));
        button2.setTextColor(getResources().getColor(R.color.huise2));
        view1.setBackgroundColor(getResources().getColor(R.color.transparent));
        view2.setBackgroundColor(getResources().getColor(R.color.transparent));
        switch (view.getId()) {
            case R.id.bt_zhibo:
                button1.setTextColor(Color.rgb(0xf4, 0x4b, 0x50));
                view1.setBackgroundColor(Color.rgb(0xf4, 0x4b, 0x50));
                gv.setVisibility(View.VISIBLE);
                iv.setVisibility(View.GONE);
                break;
            case R.id.bt_gg:
                button2.setTextColor(Color.rgb(0xf4, 0x4b, 0x50));
                view2.setBackgroundColor(Color.rgb(0xf4, 0x4b, 0x50));
                gv.setVisibility(View.GONE);
                iv.setVisibility(View.VISIBLE);
                break;
        }
    }

    //分享视频截图
    private ShareDialog.OnDialogClickListener dialogClickListener = new ShareDialog.OnDialogClickListener() {
        @Override
        public void onShare(Bitmap bitmap, Uri uri) {
            UMImage image = new UMImage(mContext, bitmap);
            UMImage thumb = new UMImage(mContext, R.drawable.img_thume_defult);
            image.setThumb(thumb);
            image.compressStyle = UMImage.CompressStyle.QUALITY;
            image.setTitle(videotitle != null ? videotitle : "");
            new ShareAction((Activity) mContext).withMedia(image)
                    .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                    .setCallback(umShareListener).open();
        }
    };
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        if(mPlayerView!=null){
            mPlayerView.onPause();
        }
        if (videoView != null && videoView.isPlaying()) {
            videoView.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mPlayerView!=null){
            mPlayerView.onResume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPlayerView!=null){
            mPlayerView.onDestroy();
        }
        if (videoView != null) {
            videoView.stopPlayback();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getActivity().findViewById(R.id.view_main_bottom).setVisibility(View.GONE);
            getActivity().findViewById(R.id.ll_main_bottom).setVisibility(View.GONE);
            getActivity().findViewById(R.id.rl_main_topbar).setVisibility(View.GONE);
            getActivity().findViewById(R.id.view_main_top).setVisibility(View.GONE);
            getActivity().findViewById(R.id.top_view).setVisibility(View.GONE);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            getActivity().findViewById(R.id.view_main_bottom).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.ll_main_bottom).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.rl_main_topbar).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.view_main_top).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.top_view).setVisibility(View.VISIBLE);
        }
        mPlayerView.configurationChanged(newConfig);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            //相当于Fragment的onPause
            if (mPlayerView != null) {
                try {
                    mPlayerView.onPause();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (videoView != null) {
                    videoView.pause();
                }
            }
        }
    }

    final View.OnClickListener mRetryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            multipleStatusView.showLoading();
            request_zhibolist();
        }
    };
}
