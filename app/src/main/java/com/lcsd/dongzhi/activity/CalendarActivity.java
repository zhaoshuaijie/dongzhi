package com.lcsd.dongzhi.activity;

import android.view.View;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.lcsd.dongzhi.R;
import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;


public class CalendarActivity extends BaseBindActivity implements View.OnClickListener {
    @BindView(R.id.titlebar_title)
    TextView tv_title;
    @BindView(R.id.top_view)
    View mView;
    @Override
    protected int setLayoutId() {
        return R.layout.activity_calendar;
    }
    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.statusBarView(mView).init();
    }
    @Override
    protected void initData() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.3f)
                .init();
        tv_title.setText("日历");
        findViewById(R.id.ll_titlebar_left).setOnClickListener(this);
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 3);
        CalendarPickerView calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        Date today = new Date();
        calendar.init(today, nextYear.getTime())
                .withSelectedDate(today);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_titlebar_left:
                this.finish();
                break;
        }
    }

}
