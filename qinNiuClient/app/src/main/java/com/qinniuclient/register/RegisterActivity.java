package com.qinniuclient.register;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qinniuclient.R;
import com.qinniuclient.util.HttpUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;


public class RegisterActivity extends ActionBarActivity {
    private EditText usrEditTest, pwdEditTest;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button regBtn = (Button) findViewById(R.id.RegisterButtonBarLogin);
        Button cancelBtn = (Button) findViewById(R.id.ActionBarBackButton);
        usrEditTest = (EditText) findViewById(R.id.RegisterUsernameBarInput);
        pwdEditTest = (EditText) findViewById(R.id.RegisterPasswordBarInput);

        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setTitle("提示信息");
        progressDialog.setMessage("正在处理...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        //为登录按钮添加事件
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    new MyAsyncTask().execute();
                }
            }
        });

        //为取消按钮添加事件
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            progressDialog.show();
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
            progressDialog.dismiss();
            if (result) {
                showDialog("注册成功！");

//                启动activity
                Intent intent = new Intent(RegisterActivity.this,
                                           RegisterTurnActivity.class);
                startActivities(new Intent[]{intent});
                RegisterActivity.this.finish();
            } else {
                showDialog("用户名已存在，请更换");
            }
        }
    }

    //对用户名密码进行非空验证
    private boolean validate() {
        String usrname = usrEditTest.getText().toString();
        if (usrname.equals("")) {
            showDialog("用户名必须填");
            return false;
        }
        String pwd = pwdEditTest.getText().toString();
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
               .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                   }
               });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //通过用户名与密码进行查询，发送post请求，得到响应
    private String query(String username, String password) {

        String queryString = "username=" + username + "&password=" + password;
        String url = HttpUtil.BASE_URL + "LoginServlet?" + queryString;
        //return HttpUtil.queryStringForGet(url);

        url = HttpUtil.BASE_URL + "RegisterServlet";
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
        String usrname = usrEditTest.getText().toString();
        String pwd = pwdEditTest.getText().toString();
        String result = query(usrname, pwd);
        return result != null && result.equals("1");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
