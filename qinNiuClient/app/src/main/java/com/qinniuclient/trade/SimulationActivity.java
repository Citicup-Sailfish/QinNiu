package com.qinniuclient.trade;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.view.LayoutInflater;

import com.qinniuclient.R;

public class SimulationActivity extends TabActivity {
    /**
     * Called when the activity is first created.
     */
    private static TabHost tabHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        这里改页面
        setContentView(R.layout.activity_simulation);

        Button button = (Button) this.findViewById(R.id.SimulationActionBarExchange);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_exchange); // wrong method, need to change
                // 点击button切换到实盘交易页面失败，这里需要补充
            }
        });

        tabHost = this.getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

//        下面几行酌情增加或修改，修改就改xxxxActivity为所需页面
        intent = new Intent().setClass(this, SimulationTabPositionActivity.class);
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
        RadioGroup radioGroup = (RadioGroup) this
                .findViewById(R.id.SimulationTabBar);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_simulation, menu);
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
