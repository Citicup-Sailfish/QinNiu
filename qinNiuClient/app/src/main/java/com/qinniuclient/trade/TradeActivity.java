package com.qinniuclient.trade;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.qinniuclient.R;
import com.qinniuclient.login.LoginActivity;
import com.qinniuclient.util.UserButtonOnClickListener;

public class TradeActivity extends TabActivity {
    /**
     * Called when the activity is first created.
     */
    private static TabHost tabHost;

    private SharedPreferences sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_trade);

        sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);

        tabHost = this.getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        // 下面几行酌情增加或修改，修改就改xxxxActivity为所需页面

        intent = new Intent().setClass(this, TradeSimulationActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        spec = tabHost.newTabSpec("模拟交易").setIndicator("模拟交易")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, TradeExchangeActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        spec = tabHost.newTabSpec("实盘交易").setIndicator("实盘交易")
                .setContent(intent);
        tabHost.addTab(spec);

        //        像数组下标一样用
        tabHost.setCurrentTabByTag("模拟交易");

        //        这个ID是radioGroup的ID，对于不同的group设置不同值，否则会崩溃
        RadioGroup radioGroup = (RadioGroup) this
                .findViewById(R.id.trade_title_group);
        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group,
                                                 int checkedId) {
                        // TODO Auto-generated method stub
                        switch (checkedId) {
                            case R.id.trade_ExchangeTabBar_simulation:// 行情
                                tabHost.setCurrentTabByTag("模拟交易");
                                break;
                            case R.id.trade_ExchangeTabBar_exchange:// 自选
                                tabHost.setCurrentTabByTag("实盘交易");
                                break;
                            default:
                                break;
                        }

                        if (sp == null) {
                            return;
                        }
                        View v = tabHost.getCurrentView();
                        if (!sp.getBoolean("loginState", false)) {
                            v.setVisibility(View.INVISIBLE);
                            Intent i = new Intent(TradeActivity.this,
                                    LoginActivity.class);
                            // 启动
                            startActivity(i);
                        } else {
                            v.setVisibility(View.VISIBLE);
                        }
                    }
                });

        /*登录button的跳转 TX*/
        Button userButton = (Button) findViewById(R.id.UserButton);
        userButton.setOnClickListener(new UserButtonOnClickListener() {
            @Override
            public void onClick(View v) {
                super.onClick(v);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (sp == null) {
            return;
        }
        View v = tabHost.getCurrentView();
        if (!sp.getBoolean("loginState", false)) {
            v.setVisibility(View.INVISIBLE);
        } else {
            v.setVisibility(View.VISIBLE);
        }
    }
}
