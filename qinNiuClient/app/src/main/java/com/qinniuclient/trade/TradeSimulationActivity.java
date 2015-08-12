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
//        �����ҳ��
        setContentView(R.layout.activity_trade_simulation);

        /*Button button = (Button) this.findViewById(R.id.SimulationActionBarExchange);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_trade_exchange); // wrong method, need to change
                // ���button�л���ʵ�̽���ҳ��ʧ�ܣ�������Ҫ����
            }
        });*/

        tabHost = this.getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

//        ���漸���������ӻ��޸ģ��޸ľ͸�xxxxActivityΪ����ҳ��
        intent = new Intent().setClass(this, SimulationTabPositionActivity.class);
        spec = tabHost.newTabSpec("�ֲ�").setIndicator("�ֲ�")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, SimulationTabBuyActivity.class);
        spec = tabHost.newTabSpec("����").setIndicator("����")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, SimulationTabQueryActivity.class);
        spec = tabHost.newTabSpec("��ѯ").setIndicator("��ѯ")
                .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTabByTag("�ֲ�");

//        ���ID��radioGroup��ID�����ڲ�ͬ��group���ò�ֵͬ����������
        RadioGroup radioGroup = (RadioGroup) this
                .findViewById(R.id.SimulationTabBar);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (checkedId) {
                    case R.id.SimulationTabBarPosition:// �ֲ�
                        tabHost.setCurrentTabByTag("�ֲ�");
                        break;
                    case R.id.SimulationTabBarBuy:// ����
                        tabHost.setCurrentTabByTag("����");
                        break;
                    case R.id.SimulationTabBarQuery:// ��ѯ
                        tabHost.setCurrentTabByTag("��ѯ");
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
