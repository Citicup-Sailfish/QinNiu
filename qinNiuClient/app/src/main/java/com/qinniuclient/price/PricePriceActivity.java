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
//        �����ҳ��
        setContentView(R.layout.activity_price_price);

        tabHost = this.getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

//        ���漸���������ӻ��޸ģ��޸ľ͸�xxxxActivityΪ����ҳ��
        intent = new Intent().setClass(this, PriceTabIndexActivity.class);
        spec = tabHost.newTabSpec("��ָ").setIndicator("��ָ")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, PriceTabSHSZActivity.class);
        spec = tabHost.newTabSpec("����").setIndicator("����")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, PriceTabSelectorActivity.class);
        spec = tabHost.newTabSpec("���").setIndicator("���")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, PriceTabHKUSActivity.class);
        spec = tabHost.newTabSpec("������").setIndicator("������").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, PriceTabOtherActivity.class);
        spec = tabHost.newTabSpec("����").setIndicator("����").setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTabByTag("��ָ");

//        ���ID��radioGroup��ID�����ڲ�ͬ��group���ò�ֵͬ����������
        RadioGroup radioGroup = (RadioGroup) this
                .findViewById(R.id.hangqing_ExchangeTabBar);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (checkedId) {
                    case R.id.hangqing_ExchangeTabBar_yaowen:// ��ָ
                        tabHost.setCurrentTabByTag("��ָ");
                        break;
                    case R.id.hangqing_ExchangeTabBar_hundong:// ����
                        tabHost.setCurrentTabByTag("����");
                        break;
                    case R.id.hangqing_ExchangeTabBar_jihui:// ���
                        tabHost.setCurrentTabByTag("���");
                        break;
                    case R.id.hangqing_ExchangeTabBar_zixuangu:// ������
                        tabHost.setCurrentTabByTag("������");
                        break;
                    case R.id.hangqing_ExchangeTabBar_more:// ����
                        tabHost.setCurrentTabByTag("����");
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
