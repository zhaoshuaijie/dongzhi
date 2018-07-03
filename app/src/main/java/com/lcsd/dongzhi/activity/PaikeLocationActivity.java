package com.lcsd.dongzhi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.adapter.PaikeLocationAdapter;
import com.lcsd.dongzhi.view.ScrollViewWithListView;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.map.geolocation.TencentPoi;

import java.util.ArrayList;
import java.util.List;


public class PaikeLocationActivity extends BaseActivity implements TencentLocationListener, View.OnClickListener {
    private Context mContext;
    private TencentLocationManager mLocationManager;
    private List<TencentPoi> list;
    private PaikeLocationAdapter adapter;
    private ScrollViewWithListView lv;
    private TextView tv_qx, tv_null, tv_cs;
    private Intent intent;
    private static final int LoCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paike_location);
        mContext = this;
        mLocationManager = TencentLocationManager.getInstance(this);
        intent = getIntent();
        initView();
        initData();
        //开启定位,还缺少权限检测
        satr();
    }

    private void initView() {
        lv = (ScrollViewWithListView) findViewById(R.id.lv);
        tv_qx = (TextView) findViewById(R.id.tv_qx);
        tv_qx.setOnClickListener(this);
        tv_cs = (TextView) findViewById(R.id.tv_cs);
        tv_cs.setOnClickListener(this);
        tv_null = (TextView) findViewById(R.id.tv_null);
        tv_null.setOnClickListener(this);
    }

    private void initData() {
        list = new ArrayList<>();
        adapter = new PaikeLocationAdapter(list, mContext);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent.putExtra("adree", tv_cs.getText().toString() + "·" + list.get(position).getName());
                setResult(LoCATION, intent);
                finish();
            }
        });
    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
        if (i == TencentLocation.ERROR_OK) {
            // 定位成功
            if (tencentLocation.getPoiList() != null && tencentLocation.getPoiList().size() > 0) {
                if (list.size() > 0) {
                    list.clear();
                }
                list.addAll(tencentLocation.getPoiList());
                adapter.notifyDataSetChanged();
            }
            if (tencentLocation.getCity() != null) {
                tv_cs.setText(tencentLocation.getCity());
            }
        } else {
            // 定位失败
            Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {
        if (i == STATUS_DENIED) {
            /* 检测到定位权限被内置或第三方的权限管理或安全软件禁用, 导致当前应用**很可能无法定位**
			 * 必要时可对这种情况进行特殊处理, 比如弹出提示或引导
			 */
            Toast.makeText(this, "定位权限被禁用!", Toast.LENGTH_SHORT).show();
        }
    }

    // 响应点击"停止"
    public void stopLocation() {
        mLocationManager.removeUpdates(this);
    }

    // 响应点击"开始"
    public void satr() {
        // 创建定位请求
        TencentLocationRequest request = TencentLocationRequest.create();
        // 修改定位请求参数, 周期为 30000 ms
        request.setInterval(30000);
        //设置成最后一种模式，可返回周边列表
        request.setRequestLevel(request.REQUEST_LEVEL_POI);
        // 开始定位
        mLocationManager.requestLocationUpdates(request, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出 activity 停止定位!
        stopLocation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_qx:
                finish();
                break;
            case R.id.tv_cs:
                intent.putExtra("adree", tv_cs.getText().toString());
                setResult(LoCATION, intent);
                finish();
                break;
            case R.id.tv_null:
                intent.putExtra("adree", "");
                setResult(LoCATION, intent);
                finish();
                break;
        }
    }
}
