package com.lcsd.dongzhi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.lcsd.dongzhi.R;

/**
 * Created by Administrator on 2017/12/25.
 */
public class Select_dialog extends Dialog {
    public Select_dialog(Context context) {
        super(context, R.style.Theme_Light_FullScreenDialogAct);
        setContentView(R.layout.select_dialog);
        Window window = this.getWindow();//实例
        WindowManager.LayoutParams lp = window.getAttributes();//获取window对象
        window.setGravity(Gravity.CENTER);//对话框弹出的位置
        window.setAttributes(lp);
    }
}
