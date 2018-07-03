package com.lcsd.dongzhi.activity;

import android.content.Context;
import android.content.SharedPreferences;
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

public class ModifyPwdActivity extends BaseBindActivity implements View.OnClickListener {
    private Context mContext;
    @BindView(R.id.ed_pwd_f)
    EditText ed_1;
    @BindView(R.id.ed_pwd_nf)
    EditText ed_2;
    @BindView(R.id.ed_pwd_ncf)
    EditText ed_3;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.top_view)
    View mView;
    @Override
    protected int setLayoutId() {
        return R.layout.activity_modify_pwd;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.statusBarView(mView).init();
    }
    @Override
    protected void initData() {
        mContext = this;
        tv_title.setText("修改密码");
        findViewById(R.id.ll_back).setOnClickListener(this);
        findViewById(R.id.tv_confim_f).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_confim_f:
                if (StringUtil.isEmpty(ed_1.getText().toString())) {
                    Toast.makeText(mContext, "请输入原始密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (ed_2.getText().length() < 6 || ed_2.getText().length() > 12) {
                    Toast.makeText(mContext, "请输入6—12位新密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!ed_2.getText().toString().equals(ed_3.getText().toString())) {
                    Toast.makeText(mContext, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                requestPwd();
                break;
        }
    }

    private void requestPwd() {
        Map<String, String> map = new HashMap<>();
        map.put("c", "usercp");
        map.put("f", "passwd");
        map.put("oldpass", ed_1.getText().toString());
        map.put("newpass", ed_2.getText().toString());
        map.put("chkpass", ed_3.getText().toString());
        AppContext.getInstance().getmMyOkHttp().post(mContext, AppConfig.request_Data, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    try {
                        L.d("修改密码：",response);
                        JSONObject object = new JSONObject(response);
                        if (object.getString("status").equals("ok")) {
                            SharedPreferences sharedPreferences = getSharedPreferences("dongzhiUserInfo", MODE_PRIVATE);
                            SharedPreferences.Editor usereditor = sharedPreferences.edit();
                            usereditor.putString("pwd", ed_2.getText().toString());
                            usereditor.commit();
                            ModifyPwdActivity.this.finish();
                        }
                        Toast.makeText(mContext, object.getString("content"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                L.d("修改密码：",error_msg);
            }
        });
    }
}
