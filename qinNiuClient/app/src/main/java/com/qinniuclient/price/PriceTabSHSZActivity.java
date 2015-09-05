package com.qinniuclient.price;
/*这份代码来自网上模板，所以命名方式有些奇怪，我也不敢随意去改名字，怕造成bug*/

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qinniuclient.R;
import com.qinniuclient.price.expandable.PriceItemGroup;
import com.qinniuclient.price.expandable.PriceItemPeople;
import com.qinniuclient.price.expandableui.PinnedHeaderExpandableListView;
import com.qinniuclient.price.expandableui.PinnedHeaderExpandableListView.OnHeaderUpdateListener;
import com.qinniuclient.price.expandableui.StickyLayout;
import com.qinniuclient.price.expandableui.StickyLayout.OnGiveUpTouchEventListener;
import com.qinniuclient.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

public class PriceTabSHSZActivity extends Activity implements
        ExpandableListView.OnChildClickListener,
        ExpandableListView.OnGroupClickListener,
        OnHeaderUpdateListener, OnGiveUpTouchEventListener {
    private PinnedHeaderExpandableListView expandableListView;
    private StickyLayout stickyLayout;
    private ArrayList<PriceItemGroup> groupList;
    private ArrayList<List<PriceItemPeople>> childList;

    private MyexpandableListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_hushen_main);
        expandableListView = (PinnedHeaderExpandableListView) findViewById(R.id.expandablelist);
        stickyLayout = (StickyLayout)findViewById(R.id.sticky_layout);

        groupList = new ArrayList<PriceItemGroup>();
        PriceItemGroup group;
        /*这个地方可以设定榜的名称，涨幅榜，跌幅榜*/
        String[] titleName = {"沪A涨幅榜", "沪A跌幅榜", "深A涨幅榜", "深A跌幅榜"};
        for (int i = 0; i < 4; i++) {
            group = new PriceItemGroup();
            group.setTitle(titleName[i]);
            groupList.add(group);
        }

        new MyAsyncTask().execute();
    }

    /***
     * InitData
     */
    /*初始化股票的数据，*/
    void initData(String result) {
        childList = new ArrayList< List<PriceItemPeople>>();
        String[] tarString = result.split("\\+");
        for (int i = 0; i < groupList.size(); i++) {
            ArrayList<PriceItemPeople> childTemp = null;
            childTemp = new ArrayList<PriceItemPeople>();
            String[] tarlist = tarString[i].split("\\|");
            for (int j = 0; j < 5; j++) {
                String[] tar = tarlist[j].split(";");
                PriceItemPeople people = new PriceItemPeople();
                people.setName(tar[0] + "\n" + tar[1]);
                people.setincrease(tar[2]);
                people.setpercentage(tar[3]);
                childTemp.add(people);
            }
            childList.add(childTemp);
        }
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
            return query();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null && !result.equals("network anomaly") && !result.equals("")) {
                initData(result);

                adapter = new MyexpandableListAdapter(PriceTabSHSZActivity.this);
                expandableListView.setAdapter(adapter);

                // 展开所有group
                for (int i = 0, count = expandableListView.getCount(); i < count; i++) {
                    expandableListView.expandGroup(i);
                }

                expandableListView.setOnHeaderUpdateListener(PriceTabSHSZActivity.this);
                expandableListView.setOnChildClickListener(PriceTabSHSZActivity.this);
                expandableListView.setOnGroupClickListener(PriceTabSHSZActivity.this);
                stickyLayout.setOnGiveUpTouchEventListener(PriceTabSHSZActivity.this);
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

    private String query() {
        String url1 = HttpUtil.BASE_URL + "ShanghaiAInfoServlet";
        String url2 = HttpUtil.BASE_URL + "ShenzhenAInfoServlet";
        return HttpUtil.queryStringForGet(url1) + "+" + HttpUtil.queryStringForGet(url2);
    }


    /***
     * 数据源
     *
     * @author Administrator
     *
     */
    class MyexpandableListAdapter extends BaseExpandableListAdapter {
        private Context context;
        private LayoutInflater inflater;

        public MyexpandableListAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        // 返回父列表个数
        @Override
        public int getGroupCount() {
            return groupList.size();
        }

        // 返回子列表个数
        @Override
        public int getChildrenCount(int groupPosition) {
            return childList.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {

            return groupList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childList.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {

            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            GroupHolder groupHolder = null;
            /*原先的代码，试着改成下面的，看能不能添加给第一个ground添加下拉按钮*/
            /*if (convertView == null) {
                groupHolder = new GroupHolder();
                convertView = inflater.inflate(R.layout.activity_price_hushen_item_group, null);
                groupHolder.textView = (TextView) convertView
                        .findViewById(R.id.group);
                groupHolder.imageView = (ImageView) convertView
                        .findViewById(R.id.image);
                convertView.setTag(groupHolder);
            } else {
                groupHolder = (GroupHolder) convertView.getTag();
            }*/

            groupHolder = new GroupHolder();
            convertView = inflater.inflate(R.layout.activity_price_hushen_item_group, null);
            groupHolder.textView = (TextView) convertView
                    .findViewById(R.id.price_shsz_group);
            groupHolder.imageView = (ImageView) convertView
                    .findViewById(R.id.price_shsz_image);
            convertView.setTag(groupHolder);

            groupHolder.textView.setText(((PriceItemGroup) getGroup(groupPosition))
                    .getTitle());
            if (isExpanded)// ture is Expanded or false is not isExpanded
                groupHolder.imageView.setImageResource(R.drawable.expanded);
            else
                groupHolder.imageView.setImageResource(R.drawable.collapse);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder childHolder = null;
            if (convertView == null) {
                childHolder = new ChildHolder();
                convertView = inflater.inflate(R.layout.activity_price_hushen_item_child, null);

                childHolder.textName = (TextView) convertView
                        .findViewById(R.id.price_shsz_name);
                childHolder.textAge = (TextView) convertView
                        .findViewById(R.id.price_shsz_fall_or_rise);
                childHolder.textAddress = (TextView) convertView
                        .findViewById(R.id.price_shsz_fall_or_rise_prcent);
                /*childHolder.imageView = (ImageView) convertView
                        .findViewById(R.id.image);*/
               /* Button button = (Button) convertView
                        .findViewById(R.id.button1);
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "clicked pos=", Toast.LENGTH_SHORT).show();
                    }
                });*/

                convertView.setTag(childHolder);
            } else {
                childHolder = (ChildHolder) convertView.getTag();
            }

            childHolder.textName.setText(((PriceItemPeople) getChild(groupPosition,
                    childPosition)).getName());

            String shzhstring = String.valueOf(((PriceItemPeople) getChild(
                    groupPosition, childPosition)).getincrease());

            if (groupPosition == 0 || groupPosition == 2) {
                childHolder.textAge.setTextColor(Color.parseColor("#e74e64"));
                childHolder.textAddress.setTextColor(Color.parseColor("#e74e64"));
                childHolder.textAge.setText(String.valueOf(((PriceItemPeople) getChild(
                        groupPosition, childPosition)).getincrease()));
                childHolder.textAddress.setText(((PriceItemPeople) getChild(groupPosition,
                        childPosition)).getpercentage());
            } else {
                childHolder.textAge.setTextColor(Color.parseColor("#10ab95"));
                childHolder.textAddress.setTextColor(Color.parseColor("#10ab95"));
                childHolder.textAge.setText(String.valueOf(((PriceItemPeople) getChild(
                        groupPosition, childPosition)).getincrease()));
                childHolder.textAddress.setText(((PriceItemPeople) getChild(groupPosition,
                        childPosition)).getpercentage());
            }


            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    @Override
    public boolean onGroupClick(final ExpandableListView parent, final View v,
                                int groupPosition, final long id) {

        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
                                int groupPosition, int childPosition, long id) {
        /*Toast.makeText(MainActivity.this,
                childList.get(groupPosition).get(childPosition).getName(), 1)
                .show();*/

        return false;
    }

    class GroupHolder {
        TextView textView;
        ImageView imageView;
    }

    class ChildHolder {
        TextView textName;
        TextView textAge;
        TextView textAddress;
        ImageView imageView;
    }

    @Override
    public View getPinnedHeader() {
        View headerView = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_price_hushen_item_group, null);
        headerView.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        return headerView;
    }

    @Override
    public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {
        PriceItemGroup firstVisibleGroup = (PriceItemGroup) adapter.getGroup(firstVisibleGroupPos);
        TextView textView = (TextView) headerView.findViewById(R.id.price_shsz_group);
        textView.setText(firstVisibleGroup.getTitle());
    }

    @Override
    public boolean giveUpTouchEvent(MotionEvent event) {
        if (expandableListView.getFirstVisiblePosition() == 0) {
            View view = expandableListView.getChildAt(0);
            if (view != null && view.getTop() >= 0) {
                return true;
            }
        }
        return false;
    }

}
