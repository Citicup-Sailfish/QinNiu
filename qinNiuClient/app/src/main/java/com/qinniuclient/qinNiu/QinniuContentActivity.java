package com.qinniuclient.qinNiu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.qinniuclient.R;

public class QinniuContentActivity extends AppCompatActivity {
    private String stockCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qinniu_content);

        /* 时间轮 */
        DatePicker dp = (DatePicker) findViewById(R.id.QinniuContentDatePicker);
        if (dp != null) {
            // 最后一个getChildAt中，0表示年，1表示月，2表示日
            ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2)
                    .setVisibility(View.GONE);
        }

                /* 上级页面获取股票代码 */
        Intent parentIntent = this.getIntent();
        stockCode = parentIntent.getStringExtra("stockCode");
    }
}
