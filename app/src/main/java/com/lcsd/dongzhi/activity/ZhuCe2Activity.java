package com.lcsd.dongzhi.activity;

import android.content.Context;
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

public class ZhuCe2Activity extends BaseBindActivity implements View.OnClickListener {
    private Context mContext;
    private String Phone_num;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.code_num_sr)
    EditText code_num_sr;
    @BindView(R.id.Code_num_check)
    EditText Code_num_check;
    @BindView(R.id.top_view)
    View mView;
    @Override
    protected int setLayoutId() {
        return R.layout.activity_zhu_ce2;
    }
    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.statusBarView(mView).init();
    }
    @Override
    protected void initView() {
        mContext = this;
        if (getIntent() != null) {
            Phone_num = getIntent().getStringExtra("Phone_num");
            if (Phone_num == null) {
                finish();
            }
        }
    }

    @Override
    protected void initData() {
        tv_title.setText("手机注册");
        findViewById(R.id.code_code_submit).setOnClickListener(this);
        findViewById(R.id.ll_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                this.finish();
                break;
            case R.id.code_code_submit:
                if (StringUtil.isEmpty(code_num_sr.getText().toString()) || code_num_sr.getText().length() < 6 || code_num_sr.getText().length() > 12) {
                    Toast.makeText(mContext, "请输入6~12位密码!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (StringUtil.isEmpty(Code_num_check.getText().toString())) {
                    Toast.makeText(mContext, "请再次输入密码!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!code_num_sr.getText().toString().equals(Code_num_check.getText().toString())) {
                    Toast.makeText(mContext, "两次密码不一致!", Toast.LENGTH_SHORT).show();
                    return;
                }
                request_zhuce();
                break;
        }
    }

    private void request_zhuce() {
        Map<String, String> map = new HashMap<>();
        map.put("c","register");
        map.put("mobile", Phone_num);
        map.put("newpass", code_num_sr.getText().toString());
        map.put("chkpass", Code_num_check.getText().toString());
        map.put("nickname", "未设置");
        map.put("fullname","");
        AppContext.getInstance().getmMyOkHttp().post(mContext, AppConfig.request_Data, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    try {
                        L.d("TAG", "注册返回数据:" + response);
                        JSONObject object=new JSONObject(response);
                        Toast.makeText(mContext,object.getString("content"),Toast.LENGTH_SHORT).show();
                        if(object.getString("status").equals("ok")){
                            ZhuCe1Activity.Activity_zhuce_1.finish();
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                L.d("TAG", "注册失败:" + error_msg);
            }
        });
    }
}
