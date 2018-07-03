package com.lcsd.dongzhi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.lcsd.dongzhi.R;


/**
 * Created by Administrator on 2016/9/10.
 * 提示如(正在努力加载。。。)
 */
public class MyProgressDialog extends Dialog {


    public MyProgressDialog(Context context) {
        super(context, R.style.Theme_Light_FullScreenDialogAct);
        setContentView(R.layout.dialog_progress);
        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.TOP);
        window.setAttributes(lp);
        initView();
    }

    private TextView tv_tishi;

    private void initView() {
        tv_tishi = (TextView) findViewById(R.id.tv_tishi);
    }

    public void setMyText(String s) {
        if (s != null && s.length() > 0)
            tv_tishi.setText(s);
    }
}
