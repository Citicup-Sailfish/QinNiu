package com.qinniuclient.trade;

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
import android.view.View.OnClickListener;

import com.qinniuclient.R;

public class ExchangeActivity extends TabActivity {
    /**
     * Called when the activity is first created.
     */
    private static TabHost tabHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        �����ҳ��
        setContentView(R.layout.activity_exchange);

        tabHost = this.getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

//        ���漸���������ӻ��޸ģ��޸ľ͸�xxxxActivityΪ����ҳ��
        intent = new Intent().setClass(this, ExchangeTabPositionActivity.class);
        spec = tabHost.newTabSpec("�ֲ�").setIndicator("�ֲ�")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, ExchangeTabBuyActivity.class);
        spec = tabHost.newTabSpec("����").setIndicator("����")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, ExchangeTabQueryActivity.class);
        spec = tabHost.newTabSpec("��ѯ").setIndicator("��ѯ")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, ExchangeTabTransferActivity.class);
        spec = tabHost.newTabSpec("ת��").setIndicator("ת��")
                .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTabByTag("�ֲ�");

//        ���ID��radioGroup��ID�����ڲ�ͬ��group���ò�ֵͬ����������
        RadioGroup radioGroup = (RadioGroup) this
                .findViewById(R.id.ExchangeTabBar);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (checkedId) {
                    case R.id.ExchangeTabBarPosition:// �ֲ�
                        tabHost.setCurrentTabByTag("�ֲ�");
                        break;
                    case R.id.ExchangeTabBarBuy:// ����
                        tabHost.setCurrentTabByTag("����");
                        break;
                    case R.id.ExchangeTabBarQuery:// ��ѯ
                        tabHost.setCurrentTabByTag("��ѯ");
                        break;
                    case R.id.ExchangeTabBarTransfer:// ת��
                        tabHost.setCurrentTabByTag("ת��");
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
        getMenuInflater().inflate(R.menu.menu_exchange, menu);
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
