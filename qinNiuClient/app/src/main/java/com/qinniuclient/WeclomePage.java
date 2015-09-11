package com.qinniuclient;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class WeclomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*this.requestWindowFeature(Window.FEATURE_NO_TITLE);*/
        setContentView(R.layout.activity_welcome);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WeclomePage.this, MainActivity.class);
                startActivity(intent);
                WeclomePage.this.finish();
            }
        }, 2000);

    }
}
