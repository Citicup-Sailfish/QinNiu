package com.qinniuclient.register;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.qinniuclient.R;

import java.util.Timer;
import java.util.TimerTask;


public class RegisterTurnActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_turn);

        // 定时返回
        Toast.makeText(RegisterTurnActivity.this, "3秒后返回...",
                       Toast.LENGTH_SHORT).show();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                RegisterTurnActivity.this.finish();
            }
        }, 3 * 1000);

        Button backButton = (Button) findViewById(R.id.ActionBarBackButton);

        //为取消按钮添加事件
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
