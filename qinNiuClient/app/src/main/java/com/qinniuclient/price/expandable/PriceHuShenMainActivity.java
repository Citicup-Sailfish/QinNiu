package com.qinniuclient.price.expandable;
/*这份代码来自网上模板，所以命名方式有些奇怪，我也不敢随意去改名字，怕造成bug*/
import java.util.ArrayList;
import java.util.List;

import com.qinniuclient.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import com.qinniuclient.price.expandableui.PinnedHeaderExpandableListView;
import com.qinniuclient.price.expandableui.StickyLayout;
import com.qinniuclient.price.expandableui.PinnedHeaderExpandableListView.OnHeaderUpdateListener;
import com.qinniuclient.price.expandableui.StickyLayout.OnGiveUpTouchEventListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PriceHuShenMainActivity extends Activity implements
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
        initData();

        adapter = new MyexpandableListAdapter(this);
        expandableListView.setAdapter(adapter);

        // 展开所有group
        for (int i = 0, count = expandableListView.getCount(); i < count; i++) {
            expandableListView.expandGroup(i);
        }

        expandableListView.setOnHeaderUpdateListener(this);
        expandableListView.setOnChildClickListener(this);
        expandableListView.setOnGroupClickListener(this);
        stickyLayout.setOnGiveUpTouchEventListener(this);

    }

    /***
     * InitData
     */
    /*初始化股票的数据，*/
    void initData() {
        groupList = new ArrayList<PriceItemGroup>();
        PriceItemGroup group = null;
        /*这个地方可以设定榜的名称，涨幅榜，跌幅榜*/
        for (int i = 0; i < 4; i++) {
            group = new PriceItemGroup();
            group.setTitle("涨幅榜-" + i);
            groupList.add(group);
        }

        childList = new ArrayList<List<PriceItemPeople>>();
        for (int i = 0; i < groupList.size(); i++) {
            ArrayList<PriceItemPeople> childTemp;
            if (i == 0) {
                childTemp = new ArrayList<PriceItemPeople>();
                for (int j = 0; j < 5; j++) {
                    PriceItemPeople people = new PriceItemPeople();
                    people.setName("恒生银行-" + j);
                    people.setAge(3939.123);
                    people.setAddress("23.23%" + j);

                    childTemp.add(people);
                }
            } else if (i == 1) {
                childTemp = new ArrayList<PriceItemPeople>();
                for (int j = 0; j < 8; j++) {
                    PriceItemPeople people = new PriceItemPeople();
                    people.setName("ff-" + j);
                    people.setAge(40);
                    people.setAddress("sh-" + j);

                    childTemp.add(people);
                }
            } else {
                childTemp = new ArrayList<PriceItemPeople>();
                for (int j = 0; j < 23; j++) {
                    PriceItemPeople people = new PriceItemPeople();
                    people.setName("hh-" + j);
                    people.setAge(20);
                    people.setAddress("sh-" + j);

                    childTemp.add(people);
                }
            }
            childList.add(childTemp);
        }

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
                    groupPosition, childPosition)).getAge());

            childHolder.textAge.setText(String.valueOf(((PriceItemPeople) getChild(
                    groupPosition, childPosition)).getAge()));
            childHolder.textAddress.setText(((PriceItemPeople) getChild(groupPosition,
                    childPosition)).getAddress());

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
