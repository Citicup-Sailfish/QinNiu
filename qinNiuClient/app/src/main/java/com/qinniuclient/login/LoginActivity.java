package com.qinniuclient.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.qinniuclient.R;
import com.qinniuclient.register.RegisterActivity;
import com.qinniuclient.util.HttpUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends ActionBarActivity {
    private AutoCompleteTextView userName;
    private EditText userPassword;
    private CheckBox rememberMe;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化用户名 密码 记住密码 登录
        userName = (AutoCompleteTextView) findViewById(
                R.id.LoginUsernameBarInput);
        userPassword = (EditText) findViewById(R.id.LoginPasswordBarInput);
        rememberMe = (CheckBox) findViewById(R.id.LoginButtonBarCheck);
        Button loginButton = (Button) findViewById(R.id.LoginButtonBarLogin);
        Button backButton = (Button) findViewById(R.id.ActionBarBackButton);
        Button registerButton = (Button) findViewById(R.id.LoginButtonBarReg);

        sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        String name = sp.getString("USERNAME", "");
        String pass = sp.getString("PASSWORD", "");

        boolean choseRemember = sp.getBoolean("remember", false);

        //如果上次选了记住，那进入登录页面也自动勾选记住，并填上用户名和密码
        if (choseRemember) {
            userName.setText(name);
            userPassword.setText(pass);
            rememberMe.setChecked(true);
        }

        // 登录按钮监听
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (validate()) {
                    new MyAsyncTask().execute();
                }
            }
        });

        //为取消按钮添加事件
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动activity
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * 定义一个类，让其继承AsyncTask这个类
     * Params: String类型，表示传递给异步任务的参数类型是String，通常指定的是URL路径,这里用void
     * Progress: Integer类型，进度条的单位通常都是Integer类型
     * Result：boolean，是否登陆成功
     */
    public class MyAsyncTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {
            return login();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                Toast.makeText(LoginActivity.this, "登录成功",
                               Toast.LENGTH_SHORT).show();

                String userNameValue = userName.getText().toString();
                String passwordValue = userPassword.getText().toString();
                SharedPreferences.Editor editor = sp.edit();

                // 保存用户名和密码
                editor.putString("USERNAME", userNameValue);
                editor.putString("PASSWORD", passwordValue);

                // 是否自动登录
                editor.putBoolean("remember", rememberMe.isChecked());

                // 设置登录状态
                editor.putBoolean("loginState", true);

                editor.apply();

                // 跳转
                LoginActivity.this.finish();
            } else {
                Toast.makeText(LoginActivity.this, "用户名或密码错误，请重新登录!",
                               Toast.LENGTH_SHORT).show();
            }
        }
    }

    //对用户名密码进行非空验证
    private boolean validate() {
        String usrname = userName.getText().toString().trim();
        if (usrname.equals("")) {
            showDialog("用户名必须填");
            return false;
        }
        String pwd = userPassword.getText().toString().trim();
        if (pwd.equals("")) {
            showDialog("密码必须填");
            return false;
        }
        return true;
    }

    //显示提示信息的对话框
    private void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg)
               .setCancelable(false)
               .setPositiveButton("确定",
                                  new DialogInterface.OnClickListener() {
                                      public void onClick(
                                              DialogInterface dialog,
                                              int id) {
                                      }
                                  });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //通过用户名与密码进行查询，发送post请求，得到响应
    private String query(String username, String password) {

        String queryString = "username=" + username + "&password=" +
                             password;
        String url;
        //return HttpUtil.queryStringForGet(url);

        url = HttpUtil.BASE_URL + "LoginServlet";
        NameValuePair paraUsername = new BasicNameValuePair("username",
                                                            username);
        NameValuePair paraPassword = new BasicNameValuePair("password",
                                                            password);
        List<NameValuePair> para = new ArrayList<NameValuePair>();
        para.add(paraPassword);
        para.add(paraUsername);
        return HttpUtil.queryStringForPost(url, para);
    }

    //定义login 方法
    private boolean login() {
        String usrname = userName.getText().toString().trim();
        String pwd = userPassword.getText().toString().trim();
        String result = query(usrname, pwd);
        if (result != null && result.equals("1")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
