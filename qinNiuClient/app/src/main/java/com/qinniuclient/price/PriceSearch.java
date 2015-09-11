package com.qinniuclient.price;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.qinniuclient.R;
import com.qinniuclient.util.HttpUtil;


public class PriceSearch extends Activity {
    private AutoCompleteTextView act;
    private TextView temp;
    private Button butn1;
    String[] province;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_search);
        province = getResources().getStringArray(R.array.autocomplete_hint_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.activity_price_search_autoitem_style, R.id.zixuan_search_contentTextView, province);
        act = (AutoCompleteTextView) findViewById(R.id.search_autocomplete_zixuangu);
        act.setAdapter(adapter);

        temp = (TextView) findViewById(R.id.hangqing_search_result_text);
        butn1 = (Button) findViewById(R.id.hangqing_zixaun_search_queren);
        act.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            /*判断键盘的键盘的搜索按钮是否被按下，并且潘盾输入是否合法，然后将搜索结果显示在搜索框下方*/
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                    String restule = act.getText().toString();
                    /*从数组里面把文字部分加入到要显示的textview*/
                    if (restule.length() == 6) {
                        /*Log.e("string", restule);
                        Log.e("len", String.valueOf(province.length));
                        Log.e("string2", province[3]);*/
                        int i = 0;
                        for (i = 0; i < province.length; i++) {
                            String stringcom = province[i].substring(0, 6);
                            int com1 = Integer.parseInt(stringcom);
                            int com2 = Integer.parseInt(restule);
                            if (com1 == com2) {
                                /*Log.e("string3", String.valueOf(stringcom.length()));*/
                                restule = province[i];
                                /*Log.e("string1", province[i]);*/
                                break;
                            }
                        }
                        if (i < province.length) {
                            temp.setVisibility(View.VISIBLE);
                            butn1.setVisibility(View.VISIBLE);
                            temp.setText(restule);
                        } else {
                            Toast.makeText(PriceSearch.this, "股票代号不存在",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(PriceSearch.this, "输入错误，请输入6位股票代号",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                return false;

            }
        });

/*点击button，将 搜索结果添加到用户个人的记录中 股票代号在string restule*/
        butn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyAsyncTask().execute();
            }
        });
    }

    /*send request to server and
    *get the response(may be null)
    *    or string:"network anomaly"
    *response format:
    *"username;stockname stockcode;currentprice;
	* buyInPrice;profit;marketPrice;holdPositionNum;buyableNum|..."
    */
    private String query(String username, String stockcode) {
        if (stockcode.charAt(0) == '0' || stockcode.charAt(0) == '3') {
            stockcode = "sz" + stockcode;
        } else {
            stockcode = "sh" + stockcode;
        }
        String queryString = "username=" + username + "&&stockcode=" + stockcode;
        String url = HttpUtil.BASE_URL + "AddUserSelfSelectServlet?" + queryString;
        return HttpUtil.queryStringForGet(url);
    }

    public class MyAsyncTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
        }

        ;

        @Override
        protected Boolean doInBackground(Void... arg0) {
            return addSelfSelect();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (result) {
                Toast.makeText(PriceSearch.this, "添加成功",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PriceSearch.this, "添加失败，请确认并重试",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    boolean addSelfSelect() {
        //-------to be improve------------------
        String username = "tx";
        String stockcode = act.getText().toString();
        String res = query(username, stockcode);
        if (res != null && res.equals("1")) {
            return true;
        } else {
            return false;
        }
    }

}
