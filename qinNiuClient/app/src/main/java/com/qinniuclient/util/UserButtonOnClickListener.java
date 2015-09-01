package com.qinniuclient.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import com.qinniuclient.login.LoginActivity;

public class UserButtonOnClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        Context currentActivity = v.getContext();
        SharedPreferences sp = currentActivity.getSharedPreferences("userInfo",
                                                                    Activity.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        if (!sp.getBoolean("loginState", false)) {
            Intent i = new Intent(currentActivity,
                                  LoginActivity.class);
            // 启动
            currentActivity.startActivity(i);
        } else {
            // 本应留空

            // 测试用，点击注销
            // editor.putBoolean("remember", false);
            editor.putBoolean("loginState", false);
            editor.apply();
            Toast.makeText(currentActivity, "注销成功",
                           Toast.LENGTH_SHORT).show();
        }
    }
}
