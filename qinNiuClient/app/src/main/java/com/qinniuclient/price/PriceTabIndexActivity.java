package com.qinniuclient.price;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qinniuclient.R;
import com.qinniuclient.util.HttpUtil;


public class PriceTabIndexActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_tab_index);

        new MyAsyncTask().execute("0");
        new MyAsyncTask().execute("1");
        new MyAsyncTask().execute("2");
    }

    public class MyAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... arg0) {
            return query(arg0[0]);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null && !result.equals("network anomaly") && !result.equals("")) {
                String[] tar = result.split("\\|");

                if (tar[0].equals("0")) {
                    initGuoNeiTbl(tar);
                } else {
                    if (tar[0].equals("1")) {
                        initQiTaTbl(tar);
                    } else {
                        initQiHuoTbl(tar);
                    }
                }
            } else {
                if (result.equals("")) {
                    //showDialog("暂无数据");
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "暂无数据", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    //showDialog("网络异常");
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "网络异常", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
            //------for test, should be delete finally-----
            //System.out.println(result);
        }
    }

    private void initTblCell(String[] tar, int[] ids, int index) {
        String[] info = new String[4];
        if (tar[index + 1].split(";").length == 0) {
            System.out.println("haha");
            for (int i = 0; i < 4; i++) {
                info[i] = " ";
            }
        } else {
            info = tar[index + 1].split(";");
        }
        TextView tv;
        ImageView iv;
        tv = (TextView)findViewById(ids[0]);
        iv = (ImageView)findViewById(ids[1]);
        tv.setText(info[0]);
        if (info[1].equals("INCREASE")) {
            tv.setTextColor(Color.parseColor("#e74e64"));
            iv.setBackgroundResource(R.drawable.hangqing_zhang_red_image);
        } else {
            tv.setTextColor(Color.parseColor("#10ab95"));
            iv.setBackgroundResource(R.drawable.hangqing_green_die_image);
        }
        iv.setVisibility(View.VISIBLE);
        tv = (TextView)findViewById(ids[2]);
        tv.setText(info[2]);
        tv = (TextView)findViewById(ids[3]);
        tv.setText(info[3]);
    }

    private void initGuoNeiTbl(String[] tar) {
        int[][] idMatrix = {
                {R.id.hangqing_guonei_kaipanjiao0, R.id.hangqing_guonei_image0, R.id.hangqing_guonei_diezhangzhi0, R.id.hangqing_guonei_diezhanglv0},
                {R.id.hangqing_guonei_kaipanjiao1, R.id.hangqing_guonei_image1, R.id.hangqing_guonei_diezhangzhi1, R.id.hangqing_guonei_diezhanglv1},
                {R.id.hangqing_guonei_kaipanjiao2, R.id.hangqing_guonei_image2, R.id.hangqing_guonei_diezhangzhi2, R.id.hangqing_guonei_diezhanglv2},
                {R.id.hangqing_guonei_kaipanjiao3, R.id.hangqing_guonei_image3, R.id.hangqing_guonei_diezhangzhi3, R.id.hangqing_guonei_diezhanglv3},
                {R.id.hangqing_guonei_kaipanjiao4, R.id.hangqing_guonei_image4, R.id.hangqing_guonei_diezhangzhi4, R.id.hangqing_guonei_diezhanglv4},
        };
        for (int i = 0; i < 5; i++) {
            initTblCell(tar, idMatrix[i], i);
        }
    }

    private void initQiTaTbl(String[] tar) {
        int[][] idMatrix = {
                {R.id.hangqing_qita_kaipanjiao0, R.id.hangqing_qita_image0, R.id.hangqing_qita_diezhangzhi0, R.id.hangqing_qita_diezhanglv0},
                {R.id.hangqing_qita_kaipanjiao1, R.id.hangqing_qita_image1, R.id.hangqing_qita_diezhangzhi1, R.id.hangqing_qita_diezhanglv1},
                {R.id.hangqing_qita_kaipanjiao2, R.id.hangqing_qita_image2, R.id.hangqing_qita_diezhangzhi2, R.id.hangqing_qita_diezhanglv2},
                {R.id.hangqing_qita_kaipanjiao3, R.id.hangqing_qita_image3, R.id.hangqing_qita_diezhangzhi3, R.id.hangqing_qita_diezhanglv3},
                {R.id.hangqing_qita_kaipanjiao4, R.id.hangqing_qita_image4, R.id.hangqing_qita_diezhangzhi4, R.id.hangqing_qita_diezhanglv4},
                {R.id.hangqing_qita_kaipanjiao5, R.id.hangqing_qita_image5, R.id.hangqing_qita_diezhangzhi5, R.id.hangqing_qita_diezhanglv5},
                {R.id.hangqing_qita_kaipanjiao6, R.id.hangqing_qita_image6, R.id.hangqing_qita_diezhangzhi6, R.id.hangqing_qita_diezhanglv6},
                {R.id.hangqing_qita_kaipanjiao7, R.id.hangqing_qita_image7, R.id.hangqing_qita_diezhangzhi7, R.id.hangqing_qita_diezhanglv7}
        };
        for (int i = 0; i < 8; i++) {
            initTblCell(tar, idMatrix[i], i);
        }
    }

    private void initQiHuoTbl(String[] tar) {
        int[][] idMatrix = {
                {R.id.hangqing_qihuo_kaipanjiao0, R.id.hangqing_qihuo_image0, R.id.hangqing_qihuo_diezhangzhi0, R.id.hangqing_qihuo_diezhanglv0},
                {R.id.hangqing_qihuo_kaipanjiao1, R.id.hangqing_qihuo_image1, R.id.hangqing_qihuo_diezhangzhi1, R.id.hangqing_qihuo_diezhanglv1},
                {R.id.hangqing_qihuo_kaipanjiao2, R.id.hangqing_qihuo_image2, R.id.hangqing_qihuo_diezhangzhi2, R.id.hangqing_qihuo_diezhanglv2},
                {R.id.hangqing_qihuo_kaipanjiao3, R.id.hangqing_qihuo_image3, R.id.hangqing_qihuo_diezhangzhi3, R.id.hangqing_qihuo_diezhanglv3},
                {R.id.hangqing_qihuo_kaipanjiao4, R.id.hangqing_qihuo_image4, R.id.hangqing_qihuo_diezhangzhi4, R.id.hangqing_qihuo_diezhanglv4},
                {R.id.hangqing_qihuo_kaipanjiao5, R.id.hangqing_qihuo_image5, R.id.hangqing_qihuo_diezhangzhi5, R.id.hangqing_qihuo_diezhanglv5}
        };
        for (int i = 0; i < 6; i++) {
            initTblCell(tar, idMatrix[i], i);
        }
    }



    /*send request to server and
    *get the response(may be null)
    *    or string:"network anomaly"
    *response format:
    *"username;stockname stockcode;currentprice;
	* buyInPrice;profit;marketPrice;holdPositionNum;buyableNum|..."
    */

    private String query(String stockType) {
        String queryString = "stockType=" + stockType;
        String url = HttpUtil.BASE_URL + "StockInfoServlet?" + queryString;
        return HttpUtil.queryStringForGet(url);
    }


/*
    //显示提示信息的对话框
    private void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PriceTabIndexActivity.this);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_price_tab_index, menu);
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
