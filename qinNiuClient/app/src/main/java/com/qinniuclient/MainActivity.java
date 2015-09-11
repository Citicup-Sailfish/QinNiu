package com.qinniuclient;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.qinniuclient.Course.CourseMainActivity;
import com.qinniuclient.information.InformationActivity;
import com.qinniuclient.price.PriceActivity;
import com.qinniuclient.qinNiu.QinniuActivity;
import com.qinniuclient.trade.TradeActivity;

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

        SharedPreferences sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        // 当打开APP时，除非有记住选项，否则不应为登录状态
        if (sp.getBoolean("loginState", false) && !sp.getBoolean("remember", false)) {
            editor.putBoolean("loginState", false);
            editor.apply();
        }

        tabHost = this.getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

//        下面几行酌情增加或修改，修改就改xxxxActivity为所需页面
        intent = new Intent().setClass(this, PriceActivity.class);
        spec = tabHost.newTabSpec("行情").setIndicator("行情").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, CourseMainActivity.class);
        spec = tabHost.newTabSpec("私塾").setIndicator("私塾").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, QinniuActivity.class);
        spec = tabHost.newTabSpec("擒牛").setIndicator("擒牛").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, TradeActivity.class);
        spec = tabHost.newTabSpec("交易").setIndicator("交易").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, InformationActivity.class);
        spec = tabHost.newTabSpec("资讯").setIndicator("资讯").setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTabByTag("行情");

//        这个ID是radioGroup的ID，对于不同的group设置不同值，否则会崩溃
        RadioGroup radioGroup = (RadioGroup) this
                .findViewById(R.id.main_tab_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (checkedId) {
                    case R.id.main_tab_hangqing:// 行情
                        tabHost.setCurrentTabByTag("行情");
                        break;
                    case R.id.main_tab_course:// 私塾
                        tabHost.setCurrentTabByTag("私塾");
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
}
