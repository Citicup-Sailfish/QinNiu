package com.qinniuclient.Course;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.qinniuclient.R;
import com.qinniuclient.login.LoginActivity;
import com.qinniuclient.util.UserButtonOnClickListener;

public class CourseMainActivity extends TabActivity {
    private static TabHost tabHost;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);

        tabHost = this.getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        //        下面几行酌情增加或修改，修改就改xxxxActivity为所需页面
        intent = new Intent().setClass(this, CourseBaseActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        spec = tabHost.newTabSpec("炒股基础教程").setIndicator("炒股基础教程")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, CourseExtendActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        spec = tabHost.newTabSpec("拓展精品课程").setIndicator("拓展精品课程")
                .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTabByTag("炒股基础教程");

        //        这个ID是radioGroup的ID，对于不同的group设置不同值，否则会崩溃
        RadioGroup radioGroup =
                (RadioGroup) this.findViewById(R.id.CourseTabBar);
        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group,
                                                 int checkedId) {
                        // TODO Auto-generated method stub
                        switch (checkedId) {
                            case R.id.CourseTabBarBase:// 炒股基础教程
                                tabHost.setCurrentTabByTag("炒股基础教程");
                                break;
                            case R.id.CourseTabBarExtend:// 拓展精品课程
                                tabHost.setCurrentTabByTag("拓展精品课程");
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
                            Intent i = new Intent(CourseMainActivity.this,
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
