package com.lcsd.dongzhi.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.gyf.barlibrary.ImmersionBar;
import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.adapter.CommentListAdapter;
import com.lcsd.dongzhi.entity.CommentInfoList;
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
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class CommentListActivity extends BaseBindActivity implements View.OnClickListener {
    @BindView(R.id.titlebar_title)
    TextView tv_title;
    @BindView(R.id.ll_titlebar_left)
    LinearLayout ll_back;
    @BindView(R.id.iv_fx)
    ImageView iv_fx;
    @BindView(R.id.iv_pl)
    ImageView iv_pl;
    @BindView(R.id.tv_plorzw)
    TextView tv_pl;
    @BindView(R.id.ed_comment)
    EditText ed_comment;
    @BindView(R.id.tv_newstitle)
    TextView tv_newstitle;
    @BindView(R.id.tv_loadmore)
    TextView tv_loadmore;
    @BindView(R.id.lv_comment)
    ListView lv_comment;
    @BindView(R.id.tv_plnum)
    TextView tv_plnum;
    @BindView(R.id.top_view)
    View mView;
    private EditText editText;
    private String title, id, type, url, note, img;
    private Context mContext;
    private List<CommentInfoList.TRslist> beanList;
    private int totalpage;
    private CommentListAdapter adapter;
    private int pageid = 1;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_comment_list;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.statusBarView(mView).init();
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.3f)
                .init();
        mContext = this;
        if (getIntent() != null) {
            type = getIntent().getStringExtra("type");
            title = getIntent().getStringExtra("title");
            id = getIntent().getStringExtra("id");
            note = getIntent().getStringExtra("note");
            img = getIntent().getStringExtra("img");
            if (type != null && type.equals("2")) {
                url = getIntent().getStringExtra("url");
            }
        }

    }

    @Override
    protected void initData() {
        tv_pl.setText("正文");
        ed_comment.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        tv_loadmore.setVisibility(View.INVISIBLE);
        tv_loadmore.setOnClickListener(this);
        iv_fx.setOnClickListener(this);
        iv_pl.setOnClickListener(this);
        tv_pl.setOnClickListener(this);
        tv_title.setText("评论");
        tv_newstitle.setText(title);
        beanList = new ArrayList<>();
        adapter = new CommentListAdapter(mContext, beanList);
        lv_comment.setAdapter(adapter);
        setListViewHeightBasedOnChildren(lv_comment);
        requestComList();
    }

    private void requestComList() {
        Map<String, String> map = new HashMap<>();
        map.put("c", "comment");
        map.put("f", "index");
        map.put("id", id);
        map.put("pageid", pageid + "");
        AppContext.getInstance().getmMyOkHttp().post(mContext, AppConfig.request_Data, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getString("status").equals("ok")) {
                            L.d("TAG", "评论详情:" + response);
                            CommentInfoList commentInfo = JSON.parseObject(object.getString("content"), CommentInfoList.class);
                            tv_plnum.setText("全部评论（" + commentInfo.getTotal() + "）");
                            if (commentInfo.getTotal().equals("0")) {
                                tv_loadmore.setVisibility(View.VISIBLE);
                                tv_loadmore.setText("暂无评论");
                                tv_loadmore.setClickable(false);
                            } else if (commentInfo.getRslist() != null && commentInfo.getRslist().size() > 0) {
                                tv_loadmore.setVisibility(View.VISIBLE);
                                totalpage = commentInfo.getTotalpage();
                                beanList.addAll(commentInfo.getRslist());
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(lv_comment);
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                L.d("TAG", "信息错误:" + error_msg);
                adapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(lv_comment);
            }
        });

    }

    private void commentNews() {
        Map<String, String> map = new HashMap<>();
        map.put("c", "comment");
        map.put("f", "save");
        map.put("id", id);
        map.put("comment", editText.getText().toString());
        AppContext.getInstance().getmMyOkHttp().post(mContext, AppConfig.request_Data, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    try {
                        JSONObject object = new JSONObject(response);
                        Toast.makeText(mContext, object.getString("content"), Toast.LENGTH_SHORT).show();
                        if (object.getString("status").equals("ok")) {
                            editText.setText("");
                        }
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
                if (type.equals("1")) {
                    UMWeb web = new UMWeb(AppConfig.request_Data + "?id=" + id);
                    web.setTitle(title);//标题
                    if (img != null && img.length() > 0) {
                        web.setThumb(new UMImage(mContext, img));  //缩略图
                    } else {
                        web.setThumb(new UMImage(mContext, R.drawable.img_logo));  //缩略图
                    }
                    if (note != null && note.length() > 0) {
                        web.setDescription(note);//描述
                    } else {
                        web.setDescription(title);//描述
                    }
                    new ShareAction(CommentListActivity.this).withMedia(web)
                            .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                            .setCallback(umShareListener).open();
                } else {
                    UMVideo video = new UMVideo(url);
                    video.setTitle(title);//视频的标题
                    video.setThumb(new UMImage(mContext, R.drawable.img_logo));//视频的缩略图
                    video.setDescription(title);//视频的描述
                    new ShareAction((Activity) mContext).withMedia(video)
                            .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                            .setCallback(umShareListener).open();
                }
                break;
            case R.id.iv_pl:
                this.finish();
                break;
            case R.id.tv_plorzw:
                this.finish();
                break;
            case R.id.tv_loadmore:
                if (totalpage == pageid) {
                    tv_loadmore.setText("已显示全部内容");//新闻列表
                    tv_loadmore.setTextColor(getResources().getColor(R.color.color_text));
                    tv_loadmore.setClickable(false);
                } else {
                    pageid++;
                    requestComList();
                }
                break;
            case R.id.ed_comment:
                showDialog2();
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

    /**
     * 重新计算ListView的高度
     *
     * @param 、、listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight() + 15f;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + 30;
        listView.setLayoutParams(params);
    }

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
                                    Toast.makeText(mContext, "请输入评论内容!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .setShowBottom(true)
                .show(getSupportFragmentManager());
    }

}
