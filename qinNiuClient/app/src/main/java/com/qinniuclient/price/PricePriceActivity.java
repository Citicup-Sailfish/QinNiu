package com.qinniuclient.price;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.qinniuclient.R;

public class PricePriceActivity extends TabActivity {
    /**
     * Called when the activity is first created.
     */
    private static TabHost tabHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        这里改页面
        setContentView(R.layout.activity_price_price);

        tabHost = this.getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

//        下面几行酌情增加或修改，修改就改xxxxActivity为所需页面
        intent = new Intent().setClass(this, PriceTabIndexActivity.class);
        spec = tabHost.newTabSpec("股指").setIndicator("股指")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, PriceTabSHSZActivity.class);
        spec = tabHost.newTabSpec("沪深").setIndicator("沪深")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, PriceTabSelectorActivity.class);
        spec = tabHost.newTabSpec("板块").setIndicator("板块")
                .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTabByTag("股指");

//        这个ID是radioGroup的ID，对于不同的group设置不同值，否则会崩溃
        RadioGroup radioGroup = (RadioGroup) this
                .findViewById(R.id.hangqing_ExchangeTabBar);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (checkedId) {
                    case R.id.hangqing_ExchangeTabBar_yaowen:// 股指
                        tabHost.setCurrentTabByTag("股指");
                        break;
                    case R.id.hangqing_ExchangeTabBar_hundong:// 沪深
                        tabHost.setCurrentTabByTag("沪深");
                        break;
                    case R.id.hangqing_ExchangeTabBar_jihui:// 板块
                        tabHost.setCurrentTabByTag("板块");
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
