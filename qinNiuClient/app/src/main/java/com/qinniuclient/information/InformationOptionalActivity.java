package com.qinniuclient.information;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    private String[] keySet = {"ItemDate",
            "ItemImage1", "ItemTitle1", "ItemTime1", "ItemImage2", "ItemTitle2", "ItemTime2",
            "ItemImage3", "ItemTitle3", "ItemTime3", "ItemImage4", "ItemTitle4", "ItemTime4",
            "ItemImage5", "ItemTitle5", "ItemTime5"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_optional);

        new MyAsyncTask().execute();
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
                int[] toIds = {R.id.information_news_day_times,
                        R.id.information_news_imageButton0, R.id.information_news_text0,
                        R.id.information_news_Newstime0,
                        R.id.information_news_imageButton1, R.id.information_news_text1,
                        R.id.information_news_Newstime1,
                        R.id.information_news_imageButton2, R.id.information_news_text2,
                        R.id.information_news_Newstime2,
                        R.id.information_news_imageButton3, R.id.information_news_text3,
                        R.id.information_news_Newstime3,
                        R.id.information_news_imageButton4, R.id.information_news_text4,
                        R.id.information_news_Newstime4};
                //----the firt para "MainActivity.this" should be repair!------------------
                ListView newsList = (ListView) findViewById(R.id.InformationNewsList);
                /* 设置adapter */
                SimpleAdapter adapter = new SimpleAdapter(InformationOptionalActivity.this,
                        getHoldPosInfo(result),
                        R.layout.activity_information_news_listitem,
                        keySet,
                        toIds);
                /* 设置binder */
                adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                    public boolean setViewValue(View view, Object data, String textRepresentation) {
                        // 判断是否为我们要处理的对象
                        if (view instanceof ImageView && data instanceof String) {
                            ImageView iv = (ImageView) view;
                            // 使用外部库载入图像
                            Ion.with(iv).load((String) data);
                            return true;
                        }
                        return false;
                    }
                });
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
        int listItemLength = 6;
        /* 外层循环生成单个map, 内层循环处理5条新闻 */
        for (int i = 0; i < tar.length / listItemLength; i++) {
            map = new HashMap<>();
            map.put(keySet[0], tar[i * listItemLength]);
            for (int j = 1; j < listItemLength; j++) {
                String[] infoOfNews = tar[i * listItemLength + j].split(";");
                int baseNum = j * 3;
                /* 图片 */
                map.put(keySet[baseNum - 2], infoOfNews[0]);
                /* 标题 */
                map.put(keySet[baseNum - 1], infoOfNews[1]);
                /* 时间 */
                map.put(keySet[baseNum], infoOfNews[2]);
            }
            list.add(map);
        }
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_information_optional, menu);
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
