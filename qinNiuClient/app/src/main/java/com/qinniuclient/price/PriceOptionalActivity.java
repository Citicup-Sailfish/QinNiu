package com.qinniuclient.price;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.qinniuclient.R;
import com.qinniuclient.login.LoginActivity;
import com.qinniuclient.util.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PriceOptionalActivity extends ActionBarActivity {
    private SharedPreferences sp;
    private ListView PriceOptionalContentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_optional);

        sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // my code
        if (sp == null) {
            return;
        }
        if (!sp.getBoolean("loginState", false)) {
            Intent i = new Intent(PriceOptionalActivity.this,
                    LoginActivity.class);
            // 启动
            startActivity(i);
        }
        PriceOptionalContentList = (ListView) findViewById(R.id.hangqing_zixuan_Listview);
        new MyAsyncTask().execute();
    }

    /**
     * 定义一个类，让其继承AsyncTask这个类,实现异步
     * Params: String类型，表示传递给异步任务的参数类型是String，通常指定的是URL路径,这里用void
     * Progress: Integer类型，进度条的单位通常都是Integer类型
     * Result：boolean，是否登陆成功
     */
    public class MyAsyncTask extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... arg0) {
            //-------to be improved------------------
            if (sp != null) {
                return query(sp.getString("USERNAME", "Error"));
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null && !result.equals("network anomaly") && !result.equals("")) {
                //股票名称 + 代码 + 现价 + 跌涨率
                String[] keySet = {"hangqing_zixuan_socket_name", "hangqing_zixuan_socket_code",
                        "hangqing_zixuan_zuixinjia", "hangqing_zixuan_deizhangfu"};
                int[] toIds = {R.id.hangqing_zixuan_socket_name, R.id.hangqing_zixuan_socket_code,
                        R.id.hangqing_zixuan_zuixinjia, R.id.hangqing_zixuan_deizhangfu};
                MySimpleAdapter simpleAdapter =
                        new MySimpleAdapter(PriceOptionalActivity.this, getHoldPosInfo(result),
                                R.layout.activity_price_optional_list_item, keySet, toIds);
                PriceOptionalContentList.setAdapter(simpleAdapter);
            } else if ("".equals(result)) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "暂无数据", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "网络异常", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

    private class MySimpleAdapter extends SimpleAdapter {

        private TextView text1;
        private TextView text0;
        private List<? extends Map<String, ?>> mData;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View v = super.getView(position, convertView, parent);

            if (position % 2 == 0 ) {
                v.setBackgroundColor(Color.parseColor("#40496b"));
            } else {
                v.setBackgroundColor(Color.parseColor("#3c4567"));
            }

            final int myposition;
            myposition = position;

            text0 = (TextView) v
                    .findViewById(R.id.hangqing_zixuan_zuixinjia);
            text0.setTag(position);

            text1 = (TextView) v
                    .findViewById(R.id.hangqing_zixuan_deizhangfu);
            text1.setTag(position);

            if (text1.getText().toString().charAt(0) == '-') {
                text0.setTextColor(Color.parseColor("#10ab95"));
                text1.setTextColor(Color.parseColor("#10ab95"));
            } else {
                text0.setTextColor(Color.parseColor("#e74e64"));
                text1.setTextColor(Color.parseColor("#e74e64"));
            }

            return v;
        }

        public MySimpleAdapter(Context context,
                               List<? extends Map<String, ?>> data,
                               int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            this.mData = data;
            // TODO Auto-generated constructor stub
        }
    }


    /*send request to server and
    *get the response(may be null)
    *    or string:"network anomaly"
    *response format:
    *"username;stockname stockcode;currentprice;
	* buyInPrice;profit;marketPrice;holdPositionNum;buyableNum|..."
    */
    private String query(String username) {
        String queryString = "username=" + username;
        String url = HttpUtil.BASE_URL + "GetUserSelfSelectServlet?" + queryString;
        return HttpUtil.queryStringForGet(url);
    }

    private List<HashMap<String, Object>> getHoldPosInfo(String result) {
        //for test
        System.out.println(result);

        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map;
        String[] tar = result.split("\\|");
        int itemNum = tar.length;
        for (int i = 0; i < itemNum; i++) {
            String[] infoOfStock = tar[i].split(";");
            map = new HashMap<String, Object>();
            map.put("hangqing_zixuan_socket_name", infoOfStock[0]);
            map.put("hangqing_zixuan_socket_code", infoOfStock[1]);
            map.put("hangqing_zixuan_zuixinjia", infoOfStock[2]);
            map.put("hangqing_zixuan_deizhangfu", infoOfStock[3]);
            list.add(map);
        }
        return list;
    }
}
