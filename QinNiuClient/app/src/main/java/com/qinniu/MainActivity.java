package com.qinniu;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TabHost;

public class MainActivity extends TabActivity {
    /**
     * Called when the activity is first created.
     */
    private static TabHost tabHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        tabHost = this.getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

//        下面几行酌情增加或修改，修改就改xxxxActivity为所需页面
        intent = new Intent().setClass(this, LoginActivity.class);
        spec = tabHost.newTabSpec("首页").setIndicator("首页")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, PriceActivity.class);
        spec = tabHost.newTabSpec("行情").setIndicator("行情")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, RegisterActivity.class);
        spec = tabHost.newTabSpec("擒牛").setIndicator("擒牛")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, SimulationActivity.class);
        spec = tabHost.newTabSpec("交易").setIndicator("交易").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, RegisterTurnActivity.class);
        spec = tabHost.newTabSpec("资讯").setIndicator("资讯").setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTabByTag("首页");

//        这个ID是radioGroup的ID，对于不同的group设置不同值，否则会崩溃
        RadioGroup radioGroup = (RadioGroup) this
                .findViewById(R.id.main_tab_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (checkedId) {
                    case R.id.main_tab_home:// 首页
                        tabHost.setCurrentTabByTag("首页");
                        break;
                    case R.id.main_tab_hangqing:// 行情
                        tabHost.setCurrentTabByTag("行情");
                        break;
                    case R.id.main_tab_qinniu:// 擒牛
                        tabHost.setCurrentTabByTag("擒牛");
                        break;
                    case R.id.main_tab_jiaoyi:// 交易
                        tabHost.setCurrentTabByTag("交易");
                        break;
                    case R.id.main_tab_zixun:// 资讯
                        tabHost.setCurrentTabByTag("资讯");
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
