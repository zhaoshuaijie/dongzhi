package com.lcsd.dongzhi.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.gyf.barlibrary.ImmersionBar;
import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.entity.version;
import com.lcsd.dongzhi.http.AppConfig;
import com.lcsd.dongzhi.http.AppContext;
import com.lcsd.dongzhi.manage.UpdateManager;
import com.lcsd.dongzhi.util.DataCleanManager;
import com.lcsd.dongzhi.util.L;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;

public class SetActivity extends BaseBindActivity implements View.OnClickListener {
    private Context mContent;
    private int Version;
    @BindView(R.id.tv_jc)
    TextView tv_jc;
    @BindView(R.id.titlebar_title)
    TextView tv_title;
    @BindView(R.id.tv_garbage)
    TextView tv_garbage;
    @BindView(R.id.top_view)
    View mView;
    @Override
    protected int setLayoutId() {
        return R.layout.activity_set;
    }
    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.statusBarView(mView).init();
    }
    @Override
    protected void initView() {
        mContent = this;
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.3f)
                .init();
        tv_title.setText("设置");
        findViewById(R.id.ll_bbjc).setOnClickListener(this);
        findViewById(R.id.ll_titlebar_left).setOnClickListener(this);
        findViewById(R.id.ll_clear).setOnClickListener(this);
        try {
            tv_garbage.setText(DataCleanManager.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取包管理者对象
        PackageManager pm = getPackageManager();
        try {
            //获取包的详细信息
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            //获取版本号和版本名称
            tv_jc.setText(info.versionName);
            Version = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_titlebar_left:
                finish();
                break;
            case R.id.ll_clear:
                DataCleanManager.clearAllCache(this);
                try {
                    tv_garbage.setText(DataCleanManager.getTotalCacheSize(this));
                } catch (Exception e) {
                    e.printStackTrace();
                    L.d("TAG", "清除缓存异常:" + e.toString());
                }
                break;
            case R.id.ll_bbjc:
                request();
                break;
        }
    }

    private void request() {
        Map<String, String> map = new HashMap<>();
        map.put("f", "version");
        AppContext.getInstance().getmMyOkHttp().post(mContent, AppConfig.request_Data, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getString("status").equals("ok")) {
                            List<version> list = JSON.parseArray(object.getString("content"), version.class);
                            if (list != null && list.size() > 0) {
                                if (Version < Integer.valueOf(list.get(0).getVersion_no())) {
                                    new UpdateManager(SetActivity.this);
                                } else {
                                    Toast.makeText(SetActivity.this, "当前已是最新版本", Toast.LENGTH_SHORT).show();
                                }
                                L.d("TAG", "version:" + list.get(0).getVersion_no());
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Toast.makeText(mContent, error_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
