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

public class MessageActivity extends BaseBindActivity implements View.OnClickListener {
    private Context mContext;
    static Activity messageActivity;
    private String AppKey = "1e212cb27aaa0";
    private String APPSECRET = "43669bd9923d02cbedb160a03f9af8fe";
    private CountDownTimer timer;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.getCode)
    Button getCode;
    @BindView(R.id.Indentity)
    TextView Identity;
    @BindView(R.id.PhoneEd)
    EditText PhoneEd;
    @BindView(R.id.Code)
    EditText CodeEd;
    @BindView(R.id.top_view)
    View mView;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.statusBarView(mView).init();
    }

    @Override
    protected void initData() {
        mContext = this;
        MobSDK.init(this, AppKey, APPSECRET);
        messageActivity = this;
        tv_title.setText("忘记密码");
        findViewById(R.id.ll_back).setOnClickListener(this);
        getCode.setOnClickListener(this);
        Identity.setOnClickListener(this);
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
                                startActivity(new Intent(mContext, Message2Activity.class).putExtra("mobile", PhoneEd.getText().toString()));
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
                getCode.setOnClickListener(null);
                getCode.setText(millisUntilFinished / 1000 + "s后重新获取");
                getCode.setTextColor(getResources().getColor(R.color.huise2));
                getCode.setBackgroundResource(R.drawable.bubian);
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
        getCode.setTextColor(Color.WHITE);
        getCode.setOnClickListener(this);
        getCode.setBackgroundResource(R.drawable.border_btn_login);
        getCode.setText("获取验证码");
        getCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                this.finish();
                break;
            case R.id.getCode: // 获取验证码的过程.
                if (!TextUtils.isEmpty(PhoneEd.getText().toString())) {
                    countDown();
                    //获取验证码
                    SMSSDK.getVerificationCode("86", PhoneEd.getText().toString());
                } else {
                    Toast.makeText(mContext, "手机号码不能为空", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.Indentity://提交
                if (StringUtil.isEmpty(PhoneEd.getText().toString())) {
                    Toast.makeText(mContext, "手机号码不能为空", Toast.LENGTH_SHORT)
                            .show();
                    return;
                } else if (StringUtil.isEmpty(CodeEd.getText().toString())) {
                    Toast.makeText(mContext, "请输入验证码验证码", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                //提交验证码验证
                String number = CodeEd.getText().toString();
                SMSSDK.submitVerificationCode("86", PhoneEd.getText().toString(), number);
                break;
        }
    }
}
