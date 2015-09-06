package com.qinniuclient.qinNiu;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_qinniu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
