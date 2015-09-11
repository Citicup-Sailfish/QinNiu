package com.qinniuclient.qinNiu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.qinniuclient.R;

public class QinniuActivity extends ActionBarActivity {

    /* qinniu_rank_icon_1到qinniu_rank_icon_10 */
    final private int[] rankIDs = {
            R.drawable.qinniu_rank_icon_1, R.drawable.qinniu_rank_icon_2,
            R.drawable.qinniu_rank_icon_3
    };

    /* 不变 提高 降低 */
    final private int[] compareIDs = {
            R.drawable.qinniu_compare_icon_none, R.drawable.qinniu_compare_icon_up,
            R.drawable.qinniu_compare_icon_down
    };

    /* 偶数下标item背景色为backgroundColors[0]，奇数为[1] */
    final private int[] backgroundColors = {Color.rgb(57, 66, 100), Color.rgb(60, 69, 102)};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qinniu);
        //GradeProgressBar gpb= (GradeProgressBar) findViewById(R.id.QinniuGrade);

        //ImageView iv = (ImageView) findViewById(R.id.QinniuItemRank);
        //iv.setBackgroundColor(backgroundColors[0]);
/*        DatePicker dp = (DatePicker) findViewById(R.id.datePicker);
        if (dp != null) {
            // 最后一个getChildAt中，0表示年，1表示月，2表示日
            ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2)
                    .setVisibility(View.GONE);
        }*/
        TextView tv = (TextView) findViewById(R.id.QinniuTitle);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context ct = getBaseContext();

                //启动activity
                Intent intent = new Intent();
                intent.setClass(QinniuActivity.this, QinniuContentActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
