package com.lcsd.dongzhi.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.entity.UserInfo;
import com.lcsd.dongzhi.http.AppConfig;
import com.lcsd.dongzhi.http.AppContext;
import com.lcsd.dongzhi.util.L;
import com.lcsd.dongzhi.util.StringUtil;
import com.lcsd.dongzhi.view.CircleImageView;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class HYActivity extends BaseBindActivity implements View.OnClickListener, OnItemClickListener {
    private Context mContext;
    @BindView(R.id.tuichu)
    TextView tv_tc;
    @BindView(R.id.tv_name_m)
    TextView tv_name;
    @BindView(R.id.civ_head_m)
    CircleImageView civ_head;
    private AlertView mAlertView;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_hy;
    }

    private void setListener() {
        civ_head.setOnClickListener(this);
        tv_tc.setOnClickListener(this);
        findViewById(R.id.ll_calendar).setOnClickListener(this);
        findViewById(R.id.ll_weather).setOnClickListener(this);
        findViewById(R.id.ll_pwd).setOnClickListener(this);
        findViewById(R.id.ll_set).setOnClickListener(this);
        findViewById(R.id.ll_about).setOnClickListener(this);
        findViewById(R.id.ll_back).setOnClickListener(this);
        mAlertView = new AlertView("提示", "确定退出登录？", "取消", new String[]{"确定"}, null, this, AlertView.Style.Alert, this).setCancelable(false);
    }

    @Override
    protected void initData() {
        if (AppContext.getInstance().checkUser()) {
            tv_tc.setVisibility(View.VISIBLE);
            UserInfo userInfo = AppContext.getInstance().getUserInfo();
            if (userInfo.getAvatar() != null && !StringUtil.isEmpty(userInfo.getAvatar())) {
                Glide.with(this).load(AppContext.getInstance().getUserInfo().getAvatar()).centerCrop().crossFade().into(civ_head);
            }
            if (userInfo.getAlias() != null && !StringUtil.isEmpty(userInfo.getAlias())) {
                tv_name.setText(AppContext.getInstance().getUserInfo().getAlias());
            } else {
                tv_name.setText(" ");
            }
        } else {
            tv_tc.setVisibility(View.GONE);
            tv_name.setText("");
            civ_head.setImageResource(R.mipmap.img_defult_head);
        }
        mContext = this;
        setListener();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_calendar:
                startActivity(new Intent(mContext, CalendarActivity.class));
                break;
            case R.id.ll_weather:
                startActivity(new Intent(mContext, WebActivity.class).putExtra("url", AppConfig.request_tianqi).putExtra("title", "天气"));
                break;
            case R.id.ll_pwd:
                if (AppContext.getInstance().checkUser()) {
                    startActivity(new Intent(mContext, ModifyPwdActivity.class));
                } else {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
                break;
            case R.id.ll_set:
                startActivity(new Intent(mContext, SetActivity.class));
                break;
            case R.id.tuichu:
                mAlertView.show();
                break;
            case R.id.ll_about:
                startActivity(new Intent(mContext, AboutActivity.class));
                break;
            case R.id.civ_head_m:
                if (AppContext.getInstance().checkUser()) {
                    startActivity(new Intent(mContext, UserInfoActivity.class));
                } else {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
                break;
            case R.id.ll_back:
                this.finish();
                break;
        }
    }

    private void logout() {
        Map<String, String> map = new HashMap<>();
        map.put("c", "logout");
        AppContext.getInstance().getmMyOkHttp().post(mContext, AppConfig.request_Data, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    L.d("TAG", "退出登录成功:" + response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getString("status").equals("ok")) {
                            AppContext.getInstance().cleanUserInfo();
                            tv_tc.setVisibility(View.GONE);
                            tv_name.setText("");
                            civ_head.setImageResource(R.mipmap.img_defult_head);
                            SharedPreferences sharedPreferences = mContext.getSharedPreferences("dongzhiUserInfo", MODE_PRIVATE);
                            SharedPreferences.Editor usereditor = sharedPreferences.edit();
                            usereditor.putString("userid", " ");
                            usereditor.putString("pwd", " ");
                            usereditor.commit();
                        } else {
                            Toast.makeText(mContext, object.getString("content"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                L.d("TAG", "退出登录成功:" + error_msg);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onItemClick(Object o, int position) {
        if (position != AlertView.CANCELPOSITION) {
            logout();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (mAlertView != null && mAlertView.isShowing()) {
                mAlertView.dismiss();
                return false;
            }
        }

        return super.onKeyDown(keyCode, event);

    }
}
