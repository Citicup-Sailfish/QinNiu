package com.qinniuclient.information;

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
import com.qinniuclient.util.UserButtonOnClickListener;

public class InformationActivity extends TabActivity {
    /**
     * Called when the activity is first created.
     */
    private static TabHost tabHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //        这里改页面
        setContentView(R.layout.activity_information);

        tabHost = this.getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        //        下面几行酌情增加或修改，修改就改xxxxActivity为所需页面
        intent = new Intent().setClass(this, InformationNewsActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        spec = tabHost.newTabSpec("要闻").setIndicator("要闻")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, InformationScrollActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        spec = tabHost.newTabSpec("滚动").setIndicator("滚动")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, InformationOptionalActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        spec = tabHost.newTabSpec("其他").setIndicator("其他").setContent(intent);
        tabHost.addTab(spec);


        tabHost.setCurrentTabByTag("要闻");

        //        这个ID是radioGroup的ID，对于不同的group设置不同值，否则会崩溃
        RadioGroup radioGroup = (RadioGroup) this
                .findViewById(R.id.information_ExchangeTabBar);
        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group,
                                                 int checkedId) {
                        // TODO Auto-generated method stub
                        switch (checkedId) {
                            case R.id.information_ExchangeTabBar_yaowen:// 要闻
                                tabHost.setCurrentTabByTag("要闻");
                                break;
                            case R.id.information_ExchangeTabBar_hundong:// 滚动
                                tabHost.setCurrentTabByTag("滚动");
                                break;
                            case R.id.information_ExchangeTabBar_zixuangu:// 其他
                                tabHost.setCurrentTabByTag("其他");
                                break;
                            default:
                                break;
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

}
