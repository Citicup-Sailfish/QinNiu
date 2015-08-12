package com.qinniuclient.trade;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.qinniuclient.R;
import com.qinniuclient.util.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SimulationTabPositionActivity extends ActionBarActivity {
    private ListView SimulationContentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation_tab_position);

        SimulationContentList = (ListView)findViewById(R.id.SimulationContentList);
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
            return query("obj");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null && !result.equals("network anomaly") && !result.equals("")) {
                //股票名称&代码 + 现价 + 成本价 + 盈亏 + 最新市值 + 持仓数量 + 可卖数量
                String[] keySet = {"ItemTitle","ItemPriceValue", "ItemCostValue","ItemRateValue", "ItemLatestValue", "ItemPositionValue", "ItemAvaliableValue"};
                int[] toIds = {R.id.ItemTitle, R.id.ItemPriceValue, R.id.ItemCostValue, R.id.ItemRateValue, R.id.ItemLatestValue, R.id.ItemPositionValue, R.id.ItemAvaliableValue};
                //----the firt para "MainActivity.this" should be repair!------------------
                SimpleAdapter simpleAdapter = new SimpleAdapter(SimulationTabPositionActivity.this, getHoldPosInfo(result), R.layout.activity_simulation_tab_position_item, keySet, toIds);
                SimulationContentList.setAdapter(simpleAdapter);
                ///---
                setListViewHeightBasedOnChildren(SimulationContentList);
            } else if (result.equals("")) {
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

    /*send request to server and
    *get the response(may be null)
    *    or string:"network anomaly"
    *response format:
    *"username;stockname stockcode;currentprice;
	* buyInPrice;profit;marketPrice;holdPositionNum;buyableNum|..."
    */
    private String query(String username) {
        String queryString = "username=" + username;
        String url = HttpUtil.BASE_URL + "holdPosition?" + queryString;
        return HttpUtil.queryStringForGet(url);
    }

    private List<HashMap<String, Object>> getHoldPosInfo(String result) {
        //for test
        System.out.println(result);

        ArrayList<HashMap<String, Object> > list = new ArrayList<HashMap<String, Object> >();
        HashMap<String, Object> map;

        String[] tar = result.split("\\|");
        int itemNum = tar.length;
        for (int i = 0; i < itemNum; i++) {
            String[] infoOfStock = tar[i].split(";");
            map = new HashMap<String, Object>();
            map.put("ItemTitle", infoOfStock[1]);
            map.put("ItemPriceValue", infoOfStock[2]);
            map.put("ItemCostValue", infoOfStock[3]);
            map.put("ItemRateValue", infoOfStock[4]);
            map.put("ItemLatestValue", infoOfStock[5]);
            map.put("ItemPositionValue", infoOfStock[6]);
            map.put("ItemAvaliableValue", infoOfStock[7]);
            list.add(map);
        }
        return list;
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = SimulationContentList.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, SimulationContentList);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = SimulationContentList.getLayoutParams();
        params.height = totalHeight+ (SimulationContentList.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        SimulationContentList.setLayoutParams(params);
    }
/*
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
*/





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_simulation_tab_position, menu);
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
