package com.qinniuclient.price;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.qinniuclient.R;

public class PriceActivity extends TabActivity {
    /**
     * Called when the activity is first created.
     */
    private static TabHost tabHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_price);

        tabHost = this.getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

//        下面几行酌情增加或修改，修改就改xxxxActivity为所需页面

        intent = new Intent().setClass(this, PricePriceActivity.class);
        spec = tabHost.newTabSpec("行情").setIndicator("行情")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, PriceOptionalActivity.class);
        spec = tabHost.newTabSpec("自选").setIndicator("自选")
                .setContent(intent);
        tabHost.addTab(spec);

//        像数组下标一样用
        tabHost.setCurrentTabByTag("行情");

//        这个ID是radioGroup的ID，对于不同的group设置不同值，否则会崩溃
        RadioGroup radioGroup = (RadioGroup) this
                .findViewById(R.id.price_title_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (checkedId) {
                    case R.id.hangqing_ExchangeTabBar_hangqing:// 行情
                        tabHost.setCurrentTabByTag("行情");
                        break;
                    case R.id.hangqing_ExchangeTabBar_zixuan:// 自选
                        tabHost.setCurrentTabByTag("自选");
                        break;
                    default:
                        break;
                }
            }
        });

        /*搜查button的跳转 子博*/
        Button btn1 = (Button) findViewById(R.id.hangqing_ButtonSearch);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent是一种运行时绑定（run-time binding）机制，它能在程序运行过程中连接两个不同的组件。

                //page1为先前已添加的类，并已在AndroidManifest.xml内添加活动事件(<activity android:name="page1"></activity>),在存放资源代码的文件夹下下，
                Intent i = new Intent(PriceActivity.this, PriceSearch.class);

                ////启动
                startActivity(i);
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
