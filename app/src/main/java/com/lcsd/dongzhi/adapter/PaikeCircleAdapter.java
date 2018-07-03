package com.lcsd.dongzhi.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.activity.CirclevideoActivity;
import com.lcsd.dongzhi.activity.LoginActivity;
import com.lcsd.dongzhi.entity.Circle;
import com.lcsd.dongzhi.http.AppConfig;
import com.lcsd.dongzhi.http.AppContext;
import com.lcsd.dongzhi.util.L;
import com.lcsd.dongzhi.util.StringUtil;
import com.lcsd.dongzhi.view.CircleImageView;
import com.lcsd.dongzhi.view.ExpandListView;
import com.lcsd.dongzhi.view.ExpandableTextView;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ================================================
 * 描    述：拍客圈的Adapter
 * 修订历史：
 * ================================================
 */
public class PaikeCircleAdapter extends BaseAdapter {

    private Context context;
    private List<Circle.TRslist> data;
    private LayoutInflater mInflater;

    private EditText editText;
    private NiceDialog nicedialog;
    private AlertView mAlertView;

    public PaikeCircleAdapter(Context context, List<Circle.TRslist> data) {
        this.context = context;
        this.data = data;
        mInflater = LayoutInflater.from(context);
        nicedialog = new NiceDialog();
        mAlertView = new AlertView("提示", "评论需要先登录，去登录？", "取消", new String[]{"确定"}, null, context, AlertView.Style.Alert, mOnItemClickListener).setCancelable(true);

    }

    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(Object o, int position) {
            if (position != AlertView.CANCELPOSITION) {
                context.startActivity(new Intent(context, LoginActivity.class));
            }
        }
    };

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Circle.TRslist getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_paike_circle, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();

        //设置描述，会员头像，名称等
        holder.content.setText(data.get(position).getTitle(), position);
        if (StringUtil.isEmpty(data.get(position).getAlias())) {
            holder.username.setText("这家伙很懒");
        } else {
            holder.username.setText(data.get(position).getAlias());
        }
        holder.createTime.setText(StringUtil.getInterval1(Long.parseLong(data.get(position).getDateline()) * 1000));
        Glide.with(context).load(data.get(position).getAvatar()).fitCenter().dontAnimate().into(holder.avatar);
        //设置定位
        if (data.get(position).getAddress() != null && data.get(position).getAddress().length() > 0) {
            holder.ll_location.setVisibility(View.VISIBLE);
            holder.tv_location.setText(data.get(position).getAddress());
        } else {
            holder.ll_location.setVisibility(View.GONE);
        }
        //判断视频还是图集
        if (data.get(position).getVideo() != null && data.get(position).getVideo().length() > 0) {
            if (data.get(position).getThumb() != null && !data.get(position).getThumb().equals("")) {
                setImage(context, holder.mVideoimg, data.get(position).getThumb());
            } else {
                setImage(context, holder.mVideoimg, data.get(position).getVideo_cover() == null ? null : data.get(position).getVideo_cover());
            }
            holder.nineGrid.setVisibility(View.GONE);
            holder.rl.setVisibility(View.VISIBLE);
            holder.rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, CirclevideoActivity.class).putExtra("videopath", data.get(position).getVideo()));
                }
            });
        } else {
            ArrayList<ImageInfo> imageInfo = new ArrayList<>();
            List<String> imageDetails = data.get(position).getPictures();
            if (imageDetails != null && imageDetails.size() > 0) {
                for (String imageDetail : imageDetails) {
                    ImageInfo info = new ImageInfo();
                    info.setBigImageUrl(imageDetail);
                    imageInfo.add(info);
                }
                holder.nineGrid.setAdapter(new NineGridViewClickAdapter(context, imageInfo));
                holder.nineGrid.setVisibility(View.VISIBLE);
                holder.rl.setVisibility(View.GONE);
            } else {
                holder.nineGrid.setVisibility(View.GONE);
                holder.rl.setVisibility(View.GONE);
            }
        }
        //评论列表
        if (data.get(position).getComment() != null && data.get(position).getComment() != null && data.get(position).getComment().size() > 0) {
            holder.lv_comments.setVisibility(View.VISIBLE);
            holder.lv_comments.setAdapter(new CommentsAdapter(context, data.get(position).getComment()));
           /* if (data.get(position).getZan_list() != null && data.get(position).getZan_list().size() > 0) {
                holder.view.setVisibility(View.VISIBLE);
            } else {
                holder.view.setVisibility(View.GONE);
            }*/
        } else {
            holder.lv_comments.setVisibility(View.GONE);
            // holder.view.setVisibility(View.GONE);
        }
        //点赞列表
        /*if (data.get(position).getZan_list() != null && data.get(position).getZan_list().size() > 0) {
            holder.approveListLayout.setVisibility(View.VISIBLE);
            holder.approveListLayout.setList(data.get(position).getZan_list());
            holder.approveListLayout.notifyDataSetChanged();
        } else {
            holder.approveListLayout.setVisibility(View.GONE);
        }
        //是否已点赞
        if (data.get(position).getIs_zan() == 1) {
            holder.tv_favour.setTextColor(context.getResources().getColor(R.color.color_blue));
            holder.iv_favour.setImageResource(R.mipmap.img_preview_like);
        } else {
            holder.tv_favour.setTextColor(context.getResources().getColor(R.color.color_text));
            holder.iv_favour.setImageResource(R.mipmap.img_preview_no);
        }
        //点赞或取消赞
        holder.ll_favour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (System.currentTimeMillis() - mLasttime < 1000)//防止快速点击操作
                    return;
                mLasttime = System.currentTimeMillis();
                if (AppContext.getInstance().checkUser()) {
                    if (data.get(position).getIs_zan() == 0) {
                        Map<String, String> map = new HashMap<>();
                        map.put("c", "plugin");
                        map.put("f", "exec");
                        map.put("id", "zhan");
                        map.put("exec", "plus");
                        map.put("tid", data.get(position).getId());
                        AppContext.getInstance().getmMyOkHttp().post(context, AppConfig.request_Data, map, new RawResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, String response) {
                                if (response != null) {
                                    L.d("赞：", response);
                                    try {
                                        JSONObject object = new JSONObject(response);
                                        if (object.getString("status").equals("ok")) {
                                            data.get(position).setIs_zan(1);
                                            data.get(position).setZan(data.get(position).getZan() + 1);

                                            Circle.TRslist.TZan_list zan = new Circle.TRslist.TZan_list();
                                            zan.setAvatar(AppConfig.Mainurl + AppContext.getInstance().getUserInfo().getAvatar());
                                            zan.setId(AppContext.getInstance().getUserInfo().getUser_id());
                                            zan.setUser(AppContext.getInstance().getUserInfo().getAlias());
                                            if (data.get(position).getZan_list() != null) {
                                                data.get(position).getZan_list().add(0, zan);
                                            }
                                            notifyDataSetChanged();
                                        } else {
                                            Toast.makeText(context, object.getString("content"), Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, String error_msg) {
                                Toast.makeText(context, "网络异常！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Map<String, String> map = new HashMap<>();
                        map.put("c", "plugin");
                        map.put("f", "exec");
                        map.put("id", "zhan");
                        map.put("exec", "minus");
                        map.put("tid", data.get(position).getId());
                        AppContext.getInstance().getmMyOkHttp().post(context, AppConfig.request_Data, map, new RawResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, String response) {
                                if (response != null) {
                                    L.d("取消赞：", response);
                                    try {
                                        JSONObject object = new JSONObject(response);
                                        if (object.getString("status").equals("ok")) {
                                            data.get(position).setIs_zan(0);
                                            data.get(position).setZan(data.get(position).getZan() - 1);
                                            if (data.get(position).getZan_list() != null) {
                                                for (int i = 0; i < data.get(position).getZan_list().size(); i++) {
                                                    if (data.get(position).getZan_list().get(i).getId().equals(AppContext.getInstance().getUserInfo().getUser_id())) {
                                                        data.get(position).getZan_list().remove(i);
                                                        break;
                                                    }
                                                }
                                            }
                                            notifyDataSetChanged();
                                        } else {
                                            Toast.makeText(context, object.getString("content"), Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, String error_msg) {
                                Toast.makeText(context, "网络异常！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    showLoginDialog();
                }
            }
        });*/
        //分享
        holder.ll_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.get(position).getShareurl() != null) {
                    UMWeb web = new UMWeb(data.get(position).getShareurl());
                    web.setTitle(data.get(position).getTitle());//标题
                    if (data.get(position).getThumb() != null && data.get(position).getThumb().length() > 0) {
                        web.setThumb(new UMImage(context, data.get(position).getThumb()));  //缩略图
                    } else if(data.get(position).getPictures()!=null&&data.get(position).getPictures().size()>0){
                        web.setThumb(new UMImage(context, data.get(position).getPictures().get(0)));  //缩略图
                    } else {
                        web.setThumb(new UMImage(context, R.drawable.img_thume_defult));  //缩略图
                    }
                    if (data.get(position).getTitle() != null && data.get(position).getTitle().length() > 0) {
                        web.setDescription(data.get(position).getTitle());//描述
                    }
                    new ShareAction((Activity) context).withMedia(web)
                            .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                            .setCallback(umShareListener).open();
                } else {
                    Toast.makeText(context, "无分享链接", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //评论
        holder.ll_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppContext.getInstance().checkUser()) {
                    showDialog(data.get(position).getId(), null, "评论");
                } else {
                    showLoginDialog();
                }
            }
        });
        //回复评论：
        holder.lv_comments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (AppContext.getInstance().checkUser()) {
                    if (!AppContext.getInstance().getUserInfo().getUser_id().equals(data.get(position).getComment().get(i).getUid())) {
                        showDialog(data.get(position).getId(), data.get(position).getComment().get(i).getId(), "回复" + data.get(position).getComment().get(i).getAlias());
                    }
                } else {
                    showLoginDialog();
                }
            }
        });
        return convertView;
    }

    //评论请求
    private void request_pl(String id, String pid, String comment) {
        Map<String, String> map = new HashMap<>();
        map.put("c", "comment");
        map.put("f", "save");
        map.put("id", id);
        if (pid != null) {
            map.put("pid", pid);
        }
        map.put("comment", comment);
        AppContext.getInstance().getmMyOkHttp().post(context, AppConfig.request_Data, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    L.d("评论成功：", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getString("status").equals("ok")) {
                            Toast.makeText(context, "评论成功，等待管理员审核。", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, object.getString("content"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //评论弹框
    public void showDialog(final String id, final String pid, final String hit) {
        nicedialog.setLayoutId(R.layout.commit_layout)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(com.othershe.nicedialog.ViewHolder holder, final BaseNiceDialog dialog) {
                        editText = holder.getView(R.id.edit_input);
                        final TextView tv = holder.getView(R.id.tv_fs);
                        SpannableString ss = new SpannableString(hit);
                        editText.setHint(new SpannableString(ss));
                        editText.post(new Runnable() {
                            @Override
                            public void run() {
                                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(editText, 0);
                            }
                        });
                        tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (editText.getText() != null && !StringUtil.isEmpty(editText.getText().toString())) {
                                    request_pl(id, pid, StringUtil.replaceBlank(editText.getText().toString()));
                                    editText.setText("");
                                    nicedialog.dismiss();
                                } else {
                                    Toast.makeText(context, "请输入评论内容", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .setShowBottom(true)
                .show(((AppCompatActivity) context).getSupportFragmentManager());
    }

    //登录提示dilog
    private void showLoginDialog() {
        mAlertView.show();
    }

    class ViewHolder {
        @BindView(R.id.tv_content)
        ExpandableTextView content;
        //九宫格图片展示控件
        @BindView(R.id.nineGrid)
        NineGridView nineGrid;
        @BindView(R.id.tv_username)
        TextView username;
        @BindView(R.id.tv_createTime)
        TextView createTime;
        //头像
        @BindView(R.id.avatar)
        CircleImageView avatar;
        //评论的listview
        @BindView(R.id.lv_comments)
        ExpandListView lv_comments;
        //评论图标
        @BindView(R.id.ll_comment)
        LinearLayout ll_comment;
        //分享
        @BindView(R.id.ll_share)
        LinearLayout ll_share;
        //点赞布局
        /*@Bind(R.id.ll_favour)
        LinearLayout ll_favour;
        @Bind(R.id.iv_favour)
        ImageView iv_favour;
        @Bind(R.id.tv_favour)
        TextView tv_favour;*/
        //点赞列表
        /*@Bind(R.id.Approve_favours)
        LikesView approveListLayout;*/
        //定位
        @BindView(R.id.ll_paike_location)
        LinearLayout ll_location;
        @BindView(R.id.tv_paike_location)
        TextView tv_location;
        //点赞与评论分割线
       /* @Bind(R.id.plview)
        View view;*/
        //视频缩略图
        @BindView(R.id.video_img)
        ImageView mVideoimg;
        @BindView(R.id.video_rl)
        RelativeLayout rl;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
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

    //使用Glide加载图片
    private void setImage(Context context, ImageView imageView, String url) {
        Glide.with(context).load(url)
                .placeholder(R.drawable.img_circe_nothume)
                .centerCrop()
                .dontAnimate()
                .into(imageView);
    }
}