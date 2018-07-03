package com.lcsd.dongzhi.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.util.StringUtil;
import com.mob.MobSDK;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ZhuCe1Activity extends BaseBindActivity implements View.OnClickListener{
    static Activity Activity_zhuce_1;
    private Context mContext;
    private String AppKey = "1e212cb27aaa0";
    private String APPSECRET = "43669bd9923d02cbedb160a03f9af8fe";
    private CountDownTimer timer;
    @BindView(R.id.tv_title)TextView tv_title;
    @BindView(R.id.Phone_num)EditText Phone_num;
    @BindView(R.id.Code_num)EditText Code_num;
    @BindView(R.id.btn_getCode)Button btn_getCode;
    @BindView(R.id.phone_code_submit)Button phone_code_submit;
    @BindView(R.id.top_view)
    View mView;
    @Override
    protected int setLayoutId() {
        return R.layout.activity_zhu_ce1;
    }
    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.statusBarView(mView).init();
    }
    @Override
    protected void initView() {
        MobSDK.init(this, AppKey, APPSECRET);
        Activity_zhuce_1 = this;
        mContext = this;
    }

    @Override
    protected void initData() {
        tv_title.setText("手机注册");
        findViewById(R.id.ll_back).setOnClickListener(this);
        btn_getCode.setOnClickListener(this);
        phone_code_submit.setOnClickListener(this);
        EventHandler handler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "验证成功", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(mContext, ZhuCe2Activity.class).putExtra("Phone_num", Phone_num.getText().toString()));
                            }
                        });

                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "验证码已发送", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                    Throwable throwable = (Throwable) data;
                    try {
                        JSONObject obj = new JSONObject(throwable.getMessage());
                        final String des = obj.optString("detail");
                        if (!TextUtils.isEmpty(des)) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext, des, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        // 注册监听器
        SMSSDK.registerEventHandler(handler);
    }
    private void countDown() {
        timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btn_getCode.setOnClickListener(null);
                btn_getCode.setText(millisUntilFinished / 1000 + "s后重新获取");
                btn_getCode.setTextColor(getResources().getColor(R.color.huise2));
                btn_getCode.setBackgroundResource(R.drawable.bubian);
            }

            @Override
            public void onFinish() {
                clickCode();
            }
        };
        // 调用start方法开始倒计时
        timer.start();
    }
    private void clickCode() {
        btn_getCode.setTextColor(Color.WHITE);
        btn_getCode.setOnClickListener(this);
        btn_getCode.setBackgroundResource(R.drawable.border_btn_login);
        btn_getCode.setText("获取验证码");
        btn_getCode.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_back:
                    this.finish();
                    break;
                case R.id.btn_getCode: // 获取验证码的过程.
                    if (!TextUtils.isEmpty(Phone_num.getText().toString())) {
                        countDown();
                        //获取验证码
                        SMSSDK.getVerificationCode("86", Phone_num.getText().toString());
                    } else {
                        Toast.makeText(mContext, "手机号码不能为空", Toast.LENGTH_SHORT)
                                .show();
                    }
                    break;
                case R.id.phone_code_submit:
                    if (StringUtil.isEmpty(Phone_num.getText().toString())) {
                        Toast.makeText(mContext, "手机号码不能为空", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    } else  if (StringUtil.isEmpty(Code_num.getText().toString())) {
                        Toast.makeText(mContext, "请输入验证码验证码", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    //提交验证码验证
                    String number = Code_num.getText().toString();
                    SMSSDK.submitVerificationCode("86", Phone_num.getText().toString(), number);
                    break;
            }
    }
}
