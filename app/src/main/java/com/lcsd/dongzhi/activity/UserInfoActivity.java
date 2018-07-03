package com.lcsd.dongzhi.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.dialog.MyProgressDialog;
import com.lcsd.dongzhi.entity.UserInfo;
import com.lcsd.dongzhi.http.AppConfig;
import com.lcsd.dongzhi.http.AppContext;
import com.lcsd.dongzhi.util.L;
import com.lcsd.dongzhi.util.StringUtil;
import com.lcsd.dongzhi.view.CircleImageView;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class UserInfoActivity extends BaseBindActivity implements View.OnClickListener, OnItemClickListener {
    private UserInfo userInfo = null;
    private Context mContext;
    public File headFile;
    //选择图片回调标识
    private static final int PICK_PHOTO = 1001;
    //保存图片回传标识
    public final static int StartCutPicture = 1002;
    private MyProgressDialog dialog;
    private String headpath;
    private AlertView mAlertViewExt;
    private EditText etName;
    private InputMethodManager imm;

    @BindView(R.id.titlebar_title)
    TextView tv_title;
    @BindView(R.id.tv_nickname_u)
    TextView tv_nickname;
    @BindView(R.id.tv_sex_u)
    TextView tv_sex;
    @BindView(R.id.civ_head_u)
    CircleImageView civ_head;
    @BindView(R.id.top_view)
    View mView;
    @Override
    protected int setLayoutId() {
        return R.layout.activity_user_info;
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
        userInfo = AppContext.getInstance().getUserInfo();
        mContext = this;
        findViewById(R.id.ll_titlebar_left).setOnClickListener(this);
        tv_title.setText("个人信息");
        findViewById(R.id.rl_civhead_u).setOnClickListener(this);
        findViewById(R.id.rl_nickname_u).setOnClickListener(this);
        findViewById(R.id.rl_sex_u).setOnClickListener(this);
        dialog = new MyProgressDialog(mContext);
        dialog.setCancelable(false);
        dialog.setMyText("上传中...");
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mAlertViewExt = new AlertView("修改昵称", "请输入你的昵称！", "取消", null, new String[]{"完成"}, this, AlertView.Style.Alert, this);
        ViewGroup extView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.alertext_form,null);
        etName = (EditText) extView.findViewById(R.id.etName);
        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                //输入框出来则往上移动
                boolean isOpen=imm.isActive();
                mAlertViewExt.setMarginBottom(isOpen&&focus ? 120 :0);
            }
        });
        mAlertViewExt.addExtView(extView);
        if (userInfo.getAlias() != null && !StringUtil.isEmpty(userInfo.getAlias()))
            tv_nickname.setText(userInfo.getAlias());
        if (userInfo.getGender() != null)
            if (userInfo.getGender() == 1) {
                tv_sex.setText("男");
            } else {
                tv_sex.setText("女");
            }
        if (userInfo.getAvatar() != null && !StringUtil.isEmpty(userInfo.getAvatar()))
            Glide.with(this).load(AppContext.getInstance().getUserInfo().getAvatar()).centerCrop().crossFade().into(civ_head);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_titlebar_left:
                finish();
                break;
            case R.id.rl_civhead_u:
                selectPhoto();
                break;
            case R.id.rl_nickname_u:
                mAlertViewExt.show();
                break;
            case R.id.rl_sex_u:
                showSexDialog();
                break;
        }
    }

    private void showSexDialog() {
        new AlertView("修改性别", null, "取消", null,
                new String[]{"男", "女"},
                this, AlertView.Style.ActionSheet, new OnItemClickListener() {
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    modifyUserInfo(3, "1");
                } else if (position == 1) {
                    modifyUserInfo(3, "2");
                }
            }
        }).show();
    }

    /**
     * Dopost = uname  ，uname=昵称      修改会员昵称
     * Dopost = name  ，name=姓名        修改会员姓名
     * Dopost = sex  ，sex=性别           修改会员性别
     */
    private void modifyUserInfo(final int i, final String str) {
        Map<String, String> map = new HashMap<>();
        map.put("c", "usercp");
        map.put("f", "info");
        if (i == 1) {
            map.put("alias", str);
        } else if (i == 2) {
            map.put("fullname", str);
        } else if (i == 3) {
            map.put("gender", str);
        }
        AppContext.getInstance().getmMyOkHttp().post(mContext, AppConfig.request_Data, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    try {
                        L.d("修改信息：",response);
                        JSONObject object = new JSONObject(response);
                        Toast.makeText(mContext, object.getString("content"), Toast.LENGTH_SHORT).show();
                        if (object.getString("status").equals("ok")) {
                            if (i == 1) {//昵称
                                userInfo.setAlias(str);
                                AppContext.getInstance().saveUserInfo(userInfo);
                                tv_nickname.setText(str);
                            } else if (i == 3) {//性别
                                userInfo.setGender(Integer.valueOf(str));
                                AppContext.getInstance().saveUserInfo(userInfo);
                                if (Integer.valueOf(str) == 1) {
                                    tv_sex.setText("男");
                                } else if (Integer.valueOf(str) == 2) {
                                    tv_sex.setText("女");
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Toast.makeText(mContext, error_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //选择图片
    private void selectPhoto() {
        Matisse.from((Activity) mContext)
                .choose(MimeType.allOf()) // 选择 mime 的类型
                .countable(true) // 显示选择的数量
                .capture(true)  // 开启相机，和 captureStrategy 一并使用否则报错
                .captureStrategy(new CaptureStrategy(true, "com.lcsd.dongzhi.MyFileProvider")) // 拍照的图片路径
                .theme(R.style.Matisse_Zhihu) // 蓝色背景
                .maxSelectable(1) // 图片选择的最多数量
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size)) // 列表中显示的图片大小
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f) // 缩略图的比例
                .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                .forResult(PICK_PHOTO); // 设置作为标记的请求码，返回图片时使用
    }

    //选择图片后回传图片地址
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_PHOTO && resultCode == RESULT_OK) {
                if (Matisse.obtainResult(data) != null && Matisse.obtainResult(data).size() > 0) {
                    L.d("图片uri：", Matisse.obtainResult(data).get(0).toString());
                    headpath = StringUtil.getRealPathFromUri(mContext, Matisse.obtainResult(data).get(0));
                    if (headpath == null) {
                        String s = Matisse.obtainResult(data).get(0).toString();
                        headpath = Environment.getExternalStorageDirectory().toString() + s.substring(s.indexOf("external_storage_root") + 21, s.length());
                    }
                    if (headpath != null) {
                        Intent intent = new Intent("com.android.camera.action.CROP");
                        intent.setDataAndType(Uri.fromFile(new File(headpath)), "image/*");
                        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
                        intent.putExtra("crop", "true");
                        // aspectX aspectY 是宽高的比例
                        intent.putExtra("aspectX", 1);
                        intent.putExtra("aspectY", 1);
                        // outputX outputY 是裁剪图片宽高
                        intent.putExtra("outputX", 180);
                        intent.putExtra("outputY", 180);
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent, StartCutPicture);
                    }
                }
            } else if (requestCode == StartCutPicture) {
                //压缩图片
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        final Bitmap photo = extras.getParcelable("data");
                        StringUtil.saveBitmapToPath(photo, headpath);
                        headFile = new File(headpath);
                        dialog.show();
                        if (headFile != null) {
                            request_tx();
                        } else {
                            dialog.dismiss();
                        }
                    }
                }
            }
        } catch (Error e) {
            Toast.makeText(mContext,
                    "系统Error：" + e.getLocalizedMessage(), Toast.LENGTH_LONG)
                    .show();
        } catch (Exception e) {
            Toast.makeText(mContext,
                    "系统Exception：" + e.getLocalizedMessage(), Toast.LENGTH_LONG)
                    .show();
        }
    }

    //修改头像
    private void request_tx() {
        Map<String, String> map = new HashMap<>();
        map.put("c", "usercp");
        map.put("f", "avatar");
        Map<String, File> map1 = new HashMap<>();
        map1.put("avatar", headFile);
        AppContext.getInstance().getmMyOkHttp().upload(mContext, AppConfig.request_Data, map, map1, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    L.d("修改头像：", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        Toast.makeText(mContext, object.getString("content"), Toast.LENGTH_SHORT).show();
                        if (object.getString("status").equals("ok")) {
                            requestLUserInfo();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                L.d("TAG", "上传错误：" + error_msg);
            }
        });
    }

    private void requestLUserInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("c", "usercp");
        AppContext.getInstance().getmMyOkHttp().post(mContext, AppConfig.request_Data, map, new RawResponseHandler() {
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
                                if (AppContext.getInstance().getUserInfo().getAvatar() != null) {
                                    Glide.with(mContext).load(AppContext.getInstance().getUserInfo().getAvatar()).centerCrop().crossFade().into(civ_head);
                                }
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

    @Override
    public void onItemClick(Object o, int position) {
        closeKeyboard();
        if(o == mAlertViewExt && position != AlertView.CANCELPOSITION){
            String name = etName.getText().toString();
            if(name.isEmpty()){
                Toast.makeText(this, "修改的昵称不能为空！", Toast.LENGTH_SHORT).show();
            }
            else{
                modifyUserInfo(1, name);
            }

            return;
        }
    }
    private void closeKeyboard() {
        //关闭软键盘
        imm.hideSoftInputFromWindow(etName.getWindowToken(),0);
        //恢复位置
        mAlertViewExt.setMarginBottom(0);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            if(mAlertViewExt!=null && mAlertViewExt.isShowing()){
                mAlertViewExt.dismiss();
                return false;
            }
        }

        return super.onKeyDown(keyCode, event);

    }
}
