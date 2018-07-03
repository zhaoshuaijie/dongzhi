package com.lcsd.dongzhi.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.entity.UserInfo;
import com.lcsd.dongzhi.http.AppConfig;
import com.lcsd.dongzhi.http.AppContext;
import com.lcsd.dongzhi.util.L;
import com.lcsd.dongzhi.util.StringUtil;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class LoginActivity extends BaseBindActivity implements View.OnClickListener {
    private Context mContext;
    static Activity LoginActivity;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.ed_phone_l)
    EditText ed_phone;
    @BindView(R.id.ed_pwd_l)
    EditText ed_pwd;
    @BindView(R.id.top_view)
    View mView;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.statusBarView(mView).init();
    }

    @Override
    protected void initData() {
        LoginActivity = this;
        mContext = this;
        findViewById(R.id.ll_back).setOnClickListener(this);
        tv_title.setText("会员登录");
        findViewById(R.id.tv_login).setOnClickListener(this);
        findViewById(R.id.tv_register_l).setOnClickListener(this);
        findViewById(R.id.tv_forpwd_l).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_login:
                if (StringUtil.isEmpty(ed_phone.getText().toString())) {
                    Toast.makeText(mContext, "请输入账号", Toast.LENGTH_SHORT).show();
                    return;
                } else if (StringUtil.isEmpty(ed_pwd.getText().toString())) {
                    Toast.makeText(mContext, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                requestLogin();
                break;
            case R.id.tv_register_l:
                startActivity(new Intent(this, ZhuCe1Activity.class));
                break;
            case R.id.tv_forpwd_l:
                startActivity(new Intent(this, MessageActivity.class));
                break;
        }
    }

    private void requestLogin() {
        Map<String, String> map = new HashMap<>();
        map.put("c", "login");
        map.put("mobile", ed_phone.getText().toString());
        map.put("pass", ed_pwd.getText().toString());
        AppContext.getInstance().getmMyOkHttp().post(mContext, AppConfig.request_Data, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    L.d("TAG", "登陆成功:" + response);
                    try {
                        JSONObject object = new JSONObject(response);
                        Toast.makeText(mContext, object.getString("content"), Toast.LENGTH_SHORT).show();
                        if (object.getString("status").equals("ok")) {
                            SharedPreferences sharedPreferences = getSharedPreferences("dongzhiUserInfo", MODE_PRIVATE);
                            SharedPreferences.Editor usereditor = sharedPreferences.edit();
                            usereditor.putString("userid", ed_phone.getText().toString());
                            usereditor.putString("pwd", ed_pwd.getText().toString());
                            usereditor.commit();
                            requestLUserInfo();
                        }
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                L.d("TAG", "登陆失败:" + error_msg);
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
                                ((Activity) mContext).finish();
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

}
