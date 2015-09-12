package com.qinniuclient.qinNiu;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.qinniuclient.R;
import com.qinniuclient.util.HttpUtil;
import com.qinniuclient.util.RoundProgressBar;
import com.qinniuclient.util.UserButtonOnClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QinniuActivity extends ActionBarActivity {
    // 股票排名 + 股票信息(名称+代码) + 股票评分
    final private String[] keySet =
            {"QinniuItemRankNum", "QinniuItemStock", "QinniuItemGrade"};

    /* qinniu_rank_icon_1到qinniu_rank_icon_10 */
    final private int[] rankIDs = {
            R.drawable.qinniu_rank_icon_1, R.drawable.qinniu_rank_icon_2,
            R.drawable.qinniu_rank_icon_3
    };

    /* 偶数下标item背景色为backgroundColors[0]，奇数为[1] */
    final private int[] backgroundColors = {Color.rgb(57, 66, 100), Color.rgb(60, 69, 102)};

    String curDateStr;

    private ListView QinniuList;

    private Date queryDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qinniu);

        Date currentDate = new Date();
        curDateStr = new SimpleDateFormat("yyyy-MM").format(currentDate);

        queryDate = new Date(115, 5, 1);
        TextView Title = (TextView) findViewById(R.id.QinniuTitle);
        Title.setText(new SimpleDateFormat("yyyy年MM月推荐股票").format(queryDate));

        QinniuList = (ListView) findViewById(R.id.QinniuRecommendList);

        new MyAsyncTask().execute();

        /*登录button的跳转 TX*/
        Button userButton = (Button) findViewById(R.id.UserButton);
        userButton.setOnClickListener(new UserButtonOnClickListener() {
            @Override
            public void onClick(View v) {
                super.onClick(v);
            }
        });

        QinniuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                HashMap<String, Object> item =
                        (HashMap<String, Object>) parent.getItemAtPosition(position);

                String stock = (String) item.get("QinniuItemStock");
                /*传递item的课堂名称*/
                Intent intent = new Intent();
                intent.putExtra("stockCode", stock.split("\\n")[1]);
                intent.putExtra("stockRank", Integer.toString(
                        (Integer) item.get("QinniuItemRankNum")));
                intent.putExtra("date", new SimpleDateFormat("yyyy-MM").format(queryDate));
                intent.setClass(v.getContext(), QinniuContentActivity.class);
                //                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        Button QinniuHistoryRankButton = (Button) findViewById(R.id.QinniuHistoryRankButton);
        QinniuHistoryRankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent();
                intent1.setClass(QinniuActivity.this, QinniuHistoryActivity.class);
                startActivity(intent1);
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
            return query("2015-06");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null && !result.equals("network anomaly") && !result.equals("")) {
                int[] toIds = {R.id.QinniuItemRankNum, R.id.QinniuItemStock,
                        R.id.QinniuItemGrade};
                MySimpleAdapter simpleAdapter =
                        new MySimpleAdapter(QinniuActivity.this, getHoldPosInfo(result),
                                R.layout.activity_qinniu_list_item, keySet, toIds);

                /* 设置binder */
                simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                    public boolean setViewValue(View view, Object data, String textRepresentation) {
                        // 判断是否为我们要处理的对象
                        if (view instanceof RoundProgressBar && data instanceof String) {
                            RoundProgressBar rpb = (RoundProgressBar) view;
                            /* attrs={"rank", "float"} */
                            String attrs[] = ((String) data).split(";");
                            rpb.setText(attrs[1]);

                            if (Integer.valueOf(attrs[0]) <= 3) {
                                rpb.setProgress((int) Float.parseFloat(attrs[1]));
                            } else {
                                rpb.setProgress(0);
                            }
                            return true;
                        }
                        return false;
                    }
                });

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

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View v = super.getView(position, convertView, parent);
            if ((position & 1) == 0) {
                v.setBackgroundColor(Color.parseColor("#40496b"));
            } else {
                v.setBackgroundColor(Color.parseColor("#3c4567"));
            }

            TextView tv = (TextView) v.findViewById(R.id.QinniuItemRankNum);
            ImageView iv = (ImageView) v.findViewById(R.id.QinniuItemRankImage);
            tv.setText(Integer.toString(position + 1));
            // 判断是否为我们要处理的对象
            if (position < 3) {
                tv.setVisibility(View.INVISIBLE);
                iv.setVisibility(View.VISIBLE);

                iv.setImageResource(rankIDs[position]);
            } else {
                tv.setVisibility(View.VISIBLE);
                iv.setVisibility(View.INVISIBLE);
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
            map.put(keySet[0], i + 1);
            map.put(keySet[1], infoOfStock[2] + '\n' + infoOfStock[1]);
            map.put(keySet[2], Integer.toString(i + 1) + ";" + infoOfStock[3].substring(0, 5));
            // map.put(keySet[3], compareIDs[Integer.parseInt(infoOfStock[0]) - 1]);
            list.add(map);
        }
        return list;
    }
}
