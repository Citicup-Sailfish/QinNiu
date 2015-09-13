package com.qinniuclient.qinNiu;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;
import com.qinniuclient.R;
import com.qinniuclient.util.HttpUtil;

import java.util.Date;

public class QinniuContentActivity extends AppCompatActivity {
    /* 股票代码 */
    private String stockCode;
    /* 排名 */
    private String stockRank;
    /* 当前日期 */
    private String curDate;
    /* 查询日期 */
    private String queryDate;
    /* 时间轮 */
    private DatePicker dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qinniu_content);

        /* 上级页面获取股票代码 */
        Intent parentIntent = this.getIntent();
        stockCode = parentIntent.getStringExtra("stockCode");
        stockRank = parentIntent.getStringExtra("stockRank");
        curDate = parentIntent.getStringExtra("date");

        new SyncGetContent().execute();
        new SyncImage().execute();
        /* 时间轮 */
        dp = (DatePicker) findViewById(R.id.QinniuContentDatePicker);
        dp.setMaxDate(new Date().getTime());
        if (dp != null) {
            // 最后一个getChildAt中，0表示年，1表示月，2表示日
            ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0))
                    .getChildAt(2).setVisibility(View.GONE);
        }

        /* 查询按钮监听 */
        Button queryButton = (Button) findViewById(R.id.QinniuContentQuery);
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryDate = String.valueOf(dp.getYear()) + "-" +
                        String.valueOf(dp.getMonth() + 1);
                new SyncQuery().execute();
            }
        });

       /* 全景网点击 */
        TextView P5W = (TextView) findViewById(R.id.QinniuContentP5WText);
        P5W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View hint = findViewById(R.id.QinniuContentP5WHint);
                if (hint.getVisibility() == View.VISIBLE) {
                    hint.setVisibility(View.INVISIBLE);
                }

                // 留给炳坚加提示框
            }
        });

       /* 雪球网点击 */
        TextView xueqiu = (TextView) findViewById(R.id.QinniuContentXueqiuText);
        xueqiu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View hint = findViewById(R.id.QinniuContentXueqiuHint);
                if (hint.getVisibility() == View.VISIBLE) {
                    hint.setVisibility(View.INVISIBLE);
                }

                // 留给炳坚加提示框
            }
        });
    }

    /**
     * 定义一个类，让其继承AsyncTask这个类,实现异步 Params: String类型，表示传递给异步任务的参数类型是String，通常指定的是URL路径,这里用void
     * Progress: Integer类型，进度条的单位通常都是Integer类型 Result：boolean，是否登陆成功
     */
    public class SyncGetContent extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... arg0) {
            //-------to be improved------------------
            return query(curDate + '-' + stockCode);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null && !result.equals("network anomaly") &&
                    !result.equals("")) {
                /* 获取View */
                TextView stockNameTV =
                        (TextView) findViewById(R.id.QinniuContentStockName);
                TextView stockCodeTV =
                        (TextView) findViewById(R.id.QinniuContentStockCode);
                TextView stockRankTV =
                        (TextView) findViewById(R.id.QinniuContentStockRank);
                TextView SynthesisScoreTV =
                        (TextView) findViewById(
                                R.id.QinniuContentSynthesisScore);
                TextView P5WScoreTV =
                        (TextView) findViewById(R.id.QinniuContentP5WScore);
                TextView XueqiuScoreTV =
                        (TextView) findViewById(R.id.QinniuContentXueqiuScore);

                /* 拆分 */
                String stockInfo[] = result.split(";");
                for (int i = 3; i < stockInfo.length; i++) {
                    if (stockInfo[i].length() > 13) {
                        stockInfo[i] = stockInfo[i].substring(0, 13);
                    }
                }

                /* 赋值 */
                stockNameTV.setText(stockInfo[0]);
                stockCodeTV.setText(stockInfo[1]);
                stockRankTV.setText("本月排名: " + stockInfo[2]);
                SynthesisScoreTV.setText(stockInfo[3]);
                XueqiuScoreTV.setText(stockInfo[4]);
                P5WScoreTV.setText(stockInfo[5]);

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
     * 定义一个类，让其继承AsyncTask这个类,实现异步 Params: String类型，表示传递给异步任务的参数类型是String，通常指定的是URL路径,这里用void
     * Progress: Integer类型，进度条的单位通常都是Integer类型 Result：boolean，是否登陆成功
     */
    public class SyncQuery extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... arg0) {
            //-------to be improved------------------
            return query(queryDate + '-' + stockCode);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null && !result.equals("network anomaly") &&
                    !result.equals("")) {
                /* 获取View */
                TextView SynthesisScoreTV =
                        (TextView) findViewById(
                                R.id.QinniuContentSynthesisScoreHistory);
                TextView P5WScoreTV = (TextView) findViewById(
                        R.id.QinniuContentP5WScoreHistory);
                TextView XueqiuScoreTV =
                        (TextView) findViewById(
                                R.id.QinniuContentXueqiuScoreHistory);

                /* 拆分 */
                String stockInfo[] = result.split(";");
                for (int i = 3; i < stockInfo.length; i++) {
                    if (stockInfo[i].length() > 13) {
                        stockInfo[i] = stockInfo[i].substring(0, 13);
                    }
                }

                /* 赋值 */
                SynthesisScoreTV.setText(stockInfo[3]);
                XueqiuScoreTV.setText(stockInfo[4]);
                P5WScoreTV.setText(stockInfo[5]);

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
     * 定义一个类，让其继承AsyncTask这个类,实现异步 Params: String类型，表示传递给异步任务的参数类型是String，通常指定的是URL路径,这里用void
     * Progress: Integer类型，进度条的单位通常都是Integer类型 Result：boolean，是否登陆成功
     */
    public class SyncImage extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... arg0) {
            //-------to be improved-----------------
            return HttpUtil.BASE_URL + "scoreImage/" + stockRank + ".png";
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
                ImageView iv =
                        (ImageView) findViewById(R.id.QinniuContentDiagram);
                Ion.with(iv).load(result);
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
     * @param queryDate: target date
     * @return return format: date|imageUrl;title;time|imageUrl;title;time|...
     */
    private String query(String queryDate) {
        String dateSplit[] = queryDate.split("\\-");
        String queryString =
                "year=" + dateSplit[0] + "&month=" +
                        Integer.valueOf(dateSplit[1]).toString() +
                        "&stockcode=" + stockCode;
        String url = HttpUtil.BASE_URL + "StockScoreServlet?" + queryString;
        return HttpUtil.queryStringForGet(url);
    }
}
