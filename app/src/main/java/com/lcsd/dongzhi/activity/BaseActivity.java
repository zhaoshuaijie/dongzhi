package com.lcsd.dongzhi.activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by jie on 2018/5/16.
 */
public class BaseActivity extends AppCompatActivity {

    protected void onResume() {
        super.onResume();
        //友盟统计
        MobclickAgent.onResume(this);
    }
    protected void onPause() {
        super.onPause();
        //友盟统计
        MobclickAgent.onPause(this);
    }

    //保证app字体不受系统字体影响
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }
}
