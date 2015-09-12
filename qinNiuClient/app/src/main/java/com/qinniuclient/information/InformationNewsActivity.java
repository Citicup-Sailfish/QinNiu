package com.qinniuclient.information;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;
import com.qinniuclient.R;
import com.qinniuclient.util.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InformationNewsActivity extends ActionBarActivity {
    /* 日期+(图片URL+标题+时间)*5 */
    private String[] keySet = {"ItemDate",
            "ItemImage1", "ItemTitle1", "ItemTime1", "ItemImage2", "ItemTitle2", "ItemTime2",
            "ItemImage3", "ItemTitle3", "ItemTime3", "ItemImage4", "ItemTitle4", "ItemTime4",
            "ItemImage5", "ItemTitle5", "ItemTime5"};
    private ListView newsList;
    private String[] MyURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_news);
        new MyAsyncTask().execute();
        /*newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(InformationNewsActivity.this, InformationWebView.class);
                startActivity(intent);
            }
        });*/
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
            return query("news");
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
                newsList  = (ListView) findViewById(R.id.InformationNewsList);
                /* 设置adapter */
                MySimpleAdapter adapter = new MySimpleAdapter(InformationNewsActivity.this,
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
        int urlnum = 0;
        int listItemLength = 6;
        MyURL = new String[(tar.length / listItemLength) * 5];
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
                /*MyURL[urlnum] = infoOfNews[3];
                urlnum++;*/
            }
            list.add(map);
        }
        return list;
    }

    /*重写SimpleAdapter，实现课程的点击跳转，及浏览次数*/
    private class MySimpleAdapter extends SimpleAdapter {

        private TextView text0;
        private TextView text1;
        private TextView text2;
        private TextView text3;
        private TextView text4;
        private TextView text5;
        private List<? extends Map<String, ?>> mData;

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View v = super.getView(position, convertView, parent);

            text0 = (TextView) v.findViewById(R.id.information_news_text0);
            text0.setTag(position);
            text0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("url", MyURL[position * 5]);
                    intent.setClass(InformationNewsActivity.this, InformationWebView.class);
                    startActivity(intent);
                }
            });

            text1 = (TextView) v.findViewById(R.id.information_news_text1);
            text1.setTag(position);
            text1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("url", MyURL[position * 5] + 1);
                    intent.setClass(InformationNewsActivity.this, InformationWebView.class);
                    startActivity(intent);
                }
            });

            text2 = (TextView) v.findViewById(R.id.information_news_text2);
            text2.setTag(position);
            text2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("url", MyURL[position * 5] + 2);
                    intent.setClass(InformationNewsActivity.this, InformationWebView.class);
                    startActivity(intent);
                }
            });

            text3 = (TextView) v.findViewById(R.id.information_news_text3);
            text3.setTag(position);
            text3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("url", MyURL[position * 5] + 3);
                    intent.setClass(InformationNewsActivity.this, InformationWebView.class);
                    startActivity(intent);
                }
            });

            text4 = (TextView) v.findViewById(R.id.information_news_text4);
            text4.setTag(position);
            text4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("url", MyURL[position * 5] + 4);
                    intent.setClass(InformationNewsActivity.this, InformationWebView.class);
                    startActivity(intent);
                }
            });

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

}
