package com.qinniuclient.qinNiu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.qinniuclient.R;
import com.qinniuclient.util.HttpUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QinniuHistoryActivity extends ActionBarActivity {
    // 股票排名 + 股票信息(名称+代码) + 股票评分
    final private String[] keySet = {"QinniuItemRank", "QinniuItemStock", "QinniuItemGrade"};
    String curDateStr;

    private ListView QinniuList;
    private DatePicker QinniuHistoryDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qinniu_history);

        Date currentDate = new Date();
        curDateStr = new SimpleDateFormat("yyyy-MM").format(currentDate);
        new MyAsyncTask().execute();

        QinniuList = (ListView) findViewById(R.id.QinniuHistoryRecommendList);

        QinniuHistoryDatePicker = (DatePicker) findViewById(R.id.QinniuHistoryDatePicker);
                /* 时间轮 */
        QinniuHistoryDatePicker.setMaxDate(new Date().getTime());
        if (QinniuHistoryDatePicker != null) {
            // 最后一个getChildAt中，0表示年，1表示月，2表示日
            ((ViewGroup) ((ViewGroup) QinniuHistoryDatePicker.getChildAt(0)).getChildAt(0))
                    .getChildAt(2).setVisibility(View.GONE);
        }
        Button QinniuHistoryQuery = (Button) findViewById(R.id.QinniuHistoryQuery);
        QinniuHistoryQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QinniuList.setAdapter(null);
                curDateStr = String.valueOf(QinniuHistoryDatePicker.getYear()) + "-" + String.valueOf(QinniuHistoryDatePicker.getMonth() + 1);
                new MyAsyncTask().execute();
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
            //-------to be improved------------------
            return query(curDateStr);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null && !result.equals("network anomaly") && !result.equals("")) {
                int[] toIds = {R.id.QinniuHisttoryItemRank, R.id.QinniuHisttoryItemStock, R.id.QinniuHisttoryItemGrade};
                MySimpleAdapter simpleAdapter =
                        new MySimpleAdapter(QinniuHistoryActivity.this, getHoldPosInfo(result),
                                R.layout.activity_qinniu_history_list_item, keySet, toIds);
                QinniuList.setAdapter(simpleAdapter);
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
            return v;
        }

        public MySimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource,
                               String[] from, int[] to) {
            super(context, data, resource, from, to);
            this.mData = data;
        }
    }

    /**
     * @param queryDate: target date
     * @return return format: date|imageUrl;title;time|imageUrl;title;time|...
     */
    private String query(String queryDate) {
        String dateSplit[] = queryDate.split("\\-");
        String queryString =
                "year=" + dateSplit[0] + "&month=" + Integer.valueOf(dateSplit[1]).toString();
        String url = HttpUtil.BASE_URL + "StockRankInfoServlet?" + queryString;
        return HttpUtil.queryStringForGet(url);
    }

    private List<HashMap<String, Object>> getHoldPosInfo(String result) {
        //for test
        System.out.println(result);

        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map;
        String[] tar = result.split("\\|");
        for (int i = 0; i < tar.length; i++) {
            String[] infoOfStock = tar[i].split(";");
            map = new HashMap<String, Object>();
            map.put(keySet[0], infoOfStock[0]);
            map.put(keySet[1], infoOfStock[2] + '\n' + infoOfStock[1]);
            map.put(keySet[2], infoOfStock[3]);
            list.add(map);
        }
        return list;
    }
}
