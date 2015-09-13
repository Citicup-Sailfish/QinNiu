package com.qinniuclient.information;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;
import com.qinniuclient.R;
import com.qinniuclient.util.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InformationOptionalActivity extends ActionBarActivity {
    /* 日期+(图片URL+标题+时间)*5 */
    private String[] keySet = {"ItemTitle1", "ItemTime1", "Itemname"};
    private String[] MyURL;
    private ListView newsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_news);
        new MyAsyncTask().execute();
        newsList = (ListView) findViewById(R.id.InformationNewsList);
        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(MyURL[position] + "  " + position);
                Intent intent = new Intent();
                intent.putExtra("url", MyURL[position]);
                intent.setClass(InformationOptionalActivity.this, InformationWebView.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 定义一个类，让其继承AsyncTask这个类,实现异步 Params: String类型，表示传递给异步任务的参数类型是String，通常指定的是URL路径,这里用void
     * Progress: Integer类型，进度条的单位通常都是Integer类型 Result：boolean，是否登陆成功
     */
    public class MyAsyncTask extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... arg0) {
            //-------to be improved-----------------
            return query("optional");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null && !result.equals("network anomaly") &&
                    !"".equals(result)) {
                //----
                int[] toIds = {
                        R.id.information_optional_text0, R.id.information_optional_Newsname,R.id.information_optional_Newstime0};
                //----the firt para "MainActivity.this" should be repair!------
                /* 设置adapter */
                SimpleAdapter adapter = new SimpleAdapter(InformationOptionalActivity.this,
                        getHoldPosInfo(result),
                        R.layout.activity_information_optional_listitem,
                        keySet,
                        toIds);
                newsList.setAdapter(adapter);
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

    /**
     * @param type:"news", "scroll", "optional"
     * @return return format: date|imageUrl;title;time|imageUrl;title;time|...
     */
    private String query(String type) {
        String queryString = "type=" + type;
        String url = HttpUtil.BASE_URL + "InformationServlet?" + queryString;
        return HttpUtil.queryStringForGet(url);
    }

    private List<HashMap<String, Object>> getHoldPosInfo(String result) {
        //for test
        System.out.println(result);

        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        HashMap<String, Object> map;

        /* 避免空指针 */
        if (result == null) {
            return list;
        }
        /* |字符需要转义 */
        String[] tar = result.split("\\|");
        MyURL = new String[tar.length];
        for (int i = 0; i < tar.length; i++) {
            String[] infoOfStock = tar[i].split(";");
            map = new HashMap<String, Object>();
            map.put("ItemTitle1", infoOfStock[0]);
            map.put("Itemname", infoOfStock[2]);
            map.put("ItemTime1", infoOfStock[1]);
            MyURL[i] = infoOfStock[3];
            list.add(map);
        }
        return list;
    }
}
