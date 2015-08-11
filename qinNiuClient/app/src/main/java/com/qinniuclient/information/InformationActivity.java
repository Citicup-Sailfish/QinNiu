package com.qinniuclient.information;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.qinniuclient.R;

public class InformationActivity extends TabActivity {
    /**
     * Called when the activity is first created.
     */
    private static TabHost tabHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        这里改页面
        setContentView(R.layout.activity_information);

        tabHost = this.getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

//        下面几行酌情增加或修改，修改就改xxxxActivity为所需页面
        intent = new Intent().setClass(this, InformationNewsActivity.class);
        spec = tabHost.newTabSpec("要闻").setIndicator("要闻")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, InformationScrollActivity.class);
        spec = tabHost.newTabSpec("滚动").setIndicator("滚动")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, InformationChanceActivity.class);
        spec = tabHost.newTabSpec("机会").setIndicator("机会")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, InformationOptionalActivity.class);
        spec = tabHost.newTabSpec("自选股").setIndicator("自选股").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, InformationCourseActivity.class);
        spec = tabHost.newTabSpec("精品课堂").setIndicator("精品课堂").setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTabByTag("要闻");

//        这个ID是radioGroup的ID，对于不同的group设置不同值，否则会崩溃
        RadioGroup radioGroup = (RadioGroup) this
                .findViewById(R.id.information_ExchangeTabBar);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (checkedId) {
                    case R.id.information_ExchangeTabBar_yaowen:// 要闻
                        tabHost.setCurrentTabByTag("要闻");
                        break;
                    case R.id.information_ExchangeTabBar_hundong:// 滚动
                        tabHost.setCurrentTabByTag("滚动");
                        break;
                    case R.id.information_ExchangeTabBar_jihui:// 机会
                        tabHost.setCurrentTabByTag("机会");
                        break;
                    case R.id.information_ExchangeTabBar_zixuangu:// 自选股
                        tabHost.setCurrentTabByTag("自选股");
                        break;
                    case R.id.information_ExchangeTabBar_more:// 精品课堂
                        tabHost.setCurrentTabByTag("精品课堂");
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
