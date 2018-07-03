package com.lcsd.dongzhi.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.dl7.player.media.IjkPlayerView;
import com.dl7.player.widgets.ShareDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.http.AppConfig;
import com.lcsd.dongzhi.http.AppContext;
import com.lcsd.dongzhi.util.L;
import com.lcsd.dongzhi.util.StringUtil;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.utils.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class PlayVideoActivity extends BaseBindActivity implements View.OnClickListener {
    @BindView(R.id.titlebar_title)
    TextView tv_title;
    @BindView(R.id.ll_titlebar_left)
    LinearLayout ll_back;
    @BindView(R.id.ll_topbar)
    LinearLayout ll_top;
    @BindView(R.id.iv_fx)
    ImageView iv_fx;
    @BindView(R.id.iv_pl)
    ImageView iv_pl;
    @BindView(R.id.tv_plorzw)
    TextView tv_pl;
    @BindView(R.id.ed_comment)
    EditText ed_comment;
    @BindView(R.id.player_view)
    IjkPlayerView mPlayerView;
    @BindView(R.id.top_view)
    View mView;
    private ImageView iv_share, iv_share_window;
    private EditText editText;
    private Context mContext;
    private String newsId, title, note, img, url;
    private UMVideo video;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_play_video;
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
        if (getIntent().getExtras() != null) {
            newsId = getIntent().getStringExtra("newsId");
            title = getIntent().getStringExtra("title");
            note = getIntent().getStringExtra("note");
            img = getIntent().getStringExtra("img");
            url = getIntent().getStringExtra("url");
            if (newsId == null) {
                finish();
            }
        }
    }

    @Override
    protected void initData() {

        video = new UMVideo(url);
        video.setTitle(title);//标题
        if (img != null && img.length() > 0) {
            video.setThumb(new UMImage(mContext, img));  //缩略图
        } else {
            video.setThumb(new UMImage(mContext, R.drawable.img_thume_defult));  //缩略图
        }
        if (note != null && note.length() > 0) {
            video.setDescription(note);//描述
        } else {
            video.setDescription(title);//描述
        }
        iv_share = (ImageView) mPlayerView.findViewById(R.id.iv_share);
        iv_share_window = (ImageView) mPlayerView.findViewById(R.id.iv_share_window);
        iv_share.setOnClickListener(this);
        iv_share_window.setOnClickListener(this);
        tv_title.setText(title);
        ed_comment.setOnClickListener(this);
        tv_pl.setText("评论");
        ll_back.setOnClickListener(this);
        iv_fx.setOnClickListener(this);
        iv_pl.setOnClickListener(this);
        tv_pl.setOnClickListener(this);
        Glide.with(mContext).load(R.drawable.img_thume_defult).centerCrop().into(mPlayerView.mPlayerThumb);
        mPlayerView.init(false, true)
                .setDialogClickListener(dialogClickListener)
                .setTitle(title)
                .switchVideoSource(null, null, url, null, null)
                .setMediaQuality(IjkPlayerView.MEDIA_QUALITY_HIGH);
    }
    private ShareDialog.OnDialogClickListener dialogClickListener = new ShareDialog.OnDialogClickListener() {
        @Override
        public void onShare(Bitmap bitmap, Uri uri) {
            UMImage image = new UMImage(mContext,bitmap);
            UMImage thumb =  new UMImage(mContext, R.drawable.img_thume_defult);
            image.setThumb(thumb);
            image.compressStyle = UMImage.CompressStyle.QUALITY;
            image.setTitle(title!=null?title:"");
            new ShareAction((Activity) mContext).withMedia(image)
                    .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                    .setCallback(umShareListener).open();
        }
    };
    private void commentNews() {
        Map<String, String> map = new HashMap<>();
        map.put("id", newsId);
        map.put("comment", editText.getText().toString());
        map.put("c", "comment");
        map.put("f", "save");
        AppContext.getInstance().getmMyOkHttp().post(mContext, AppConfig.request_Data, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getString("status").equals("ok")) {
                            editText.setText("");
                        }
                        Toast.makeText(mContext, object.getString("content"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                L.d("TAG", "评论失败:" + error_msg);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_titlebar_left:
                this.finish();
                break;
            case R.id.iv_fx:
                new ShareAction(PlayVideoActivity.this).withMedia(video)
                        .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                        .setCallback(umShareListener).open();
                break;
            case R.id.iv_pl:
                startActivity(new Intent(mContext, CommentListActivity.class).putExtra("title", title).putExtra("id", newsId).putExtra("type", "1").putExtra("img", img).putExtra("note", note));
                break;
            case R.id.tv_plorzw:
                startActivity(new Intent(mContext, CommentListActivity.class).putExtra("title", title).putExtra("id", newsId).putExtra("type", "1").putExtra("img", img).putExtra("note", note));
                break;
            case R.id.ed_comment:
                showDialog2();
                break;
            case R.id.iv_share:
                new ShareAction(PlayVideoActivity.this).withMedia(video)
                        .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                        .setCallback(umShareListener).open();
                break;
            case R.id.iv_share_window:
                new ShareAction(PlayVideoActivity.this).withMedia(video)
                        .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                        .setCallback(umShareListener).open();
                break;
        }
    }

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

    public void showDialog2() {
        NiceDialog.init()
                .setLayoutId(R.layout.commit_layout)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        editText = holder.getView(R.id.edit_input);
                        final TextView tv = holder.getView(R.id.tv_fs);
                        editText.post(new Runnable() {
                            @Override
                            public void run() {
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(editText, 0);
                            }
                        });
                        tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (editText.getText() != null && !StringUtil.isEmpty(editText.getText().toString())) {
                                    commentNews();
                                } else {
                                    Toast.makeText(mContext, "请输入评论内容", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .setShowBottom(true)
                .show(getSupportFragmentManager());
    }

    @Override
    public void onResume() {
        super.onResume();
        mPlayerView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPlayerView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayerView.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ll_top.setVisibility(View.GONE);
            mView.setVisibility(View.GONE);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            ll_top.setVisibility(View.VISIBLE);
            mView.setVisibility(View.VISIBLE);
        }
        mPlayerView.configurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mPlayerView.handleVolumeKey(keyCode)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (mPlayerView.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

}
