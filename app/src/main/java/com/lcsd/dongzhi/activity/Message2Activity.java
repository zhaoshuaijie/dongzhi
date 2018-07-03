package com.lcsd.dongzhi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.http.AppConfig;
import com.lcsd.dongzhi.http.AppContext;
import com.lcsd.dongzhi.util.L;
import com.lcsd.dongzhi.util.StringUtil;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class Message2Activity extends BaseBindActivity implements View.OnClickListener {
    private Context mContext;
    private String mobile;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.ed_pwd_p2)
    EditText ed_pwd;
    @BindView(R.id.ed_cpwd)
    EditText ed_cpwd;
    @BindView(R.id.top_view)
    View mView;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_message2;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.statusBarView(mView).init();
    }

    @Override
    protected void initData() {
        mContext = this;
        if (getIntent() != null) {
            mobile = getIntent().getStringExtra("mobile");
        }
        tv_title.setText("忘记密码");
        findViewById(R.id.ll_back).setOnClickListener(this);
        findViewById(R.id.bt_cpwd2).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                this.finish();
                break;
            case R.id.bt_cpwd2:
                if (StringUtil.isEmpty(ed_pwd.getText().toString())) {
                    Toast.makeText(mContext, "请输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (StringUtil.isEmpty(ed_cpwd.getText().toString())) {
                    Toast.makeText(mContext, "请再次输入密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!ed_pwd.getText().toString().equals(ed_cpwd.getText().toString())) {
                    Toast.makeText(mContext, "两次密码不一致,请重新输入!", Toast.LENGTH_SHORT).show();
                    return;
                }
                requestNtxeStep();
                break;
        }
    }

    private void requestNtxeStep() {
        Map<String, String> map = new HashMap<>();
        map.put("c", "login");
        map.put("f", "getpass");
        map.put("mobile", mobile);
        map.put("newpass", ed_pwd.getText().toString());
        map.put("chkpass", ed_cpwd.getText().toString());
        AppContext.getInstance().getmMyOkHttp().post(this, AppConfig.request_Data, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    L.d("手机号：", mobile);
                    L.d("TAG", "忘记密码:" + response);
                    try {
                        JSONObject object = new JSONObject(response);
                        Toast.makeText(mContext, object.getString("content"), Toast.LENGTH_SHORT).show();
                        if (object.getString("status").equals("ok")) {
                            LoginActivity.LoginActivity.finish();
                            MessageActivity.messageActivity.finish();
                            startActivity(new Intent(mContext, LoginActivity.class));
                            Message2Activity.this.finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                L.d("TAG", "忘记密码:" + error_msg);
            }
        });
    }
}
