package com.qinniuclient.trade;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.qinniuclient.R;

public class TradeSimulationActivity extends TabActivity {
    /**
     * Called when the activity is first created.
     */
    private static TabHost tabHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        这里改页面
        setContentView(R.layout.activity_trade_simulation);

        /*Button button = (Button) this.findViewById(R.id.SimulationActionBarExchange);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_trade_exchange); // wrong method, need to change
            }
        });*/

        tabHost = this.getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

//        下面几行酌情增加或修改，修改就改xxxxActivity为所需页面
        intent = new Intent().setClass(this, SimulationTabPositionActivity.class).addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP);
        spec = tabHost.newTabSpec("持仓").setIndicator("持仓")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, SimulationTabBuyActivity.class);
        spec = tabHost.newTabSpec("买入").setIndicator("买入")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, SimulationTabQueryActivity.class);
        spec = tabHost.newTabSpec("查询").setIndicator("查询")
                .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTabByTag("持仓");

//        这个ID是radioGroup的ID，对于不同的group设置不同值，否则会崩溃
        RadioGroup radioGroup = (RadioGroup) this.findViewById(R.id.SimulationTabBar);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (checkedId) {
                    case R.id.SimulationTabBarPosition:// 持仓
                        tabHost.setCurrentTabByTag("持仓");
                        break;
                    case R.id.SimulationTabBarBuy:// 买入
                        tabHost.setCurrentTabByTag("买入");
                        break;
                    case R.id.SimulationTabBarQuery:// 查询
                        tabHost.setCurrentTabByTag("查询");
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
