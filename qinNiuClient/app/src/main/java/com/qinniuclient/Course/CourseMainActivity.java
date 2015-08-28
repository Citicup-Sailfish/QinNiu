package com.qinniuclient.Course;

/*这个类使用了网络上的一个图片轮播demo，所以代码有点长，但是关于listview部分的大多参看行情自选的网路数据传输方式*/

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.qinniuclient.R;
import com.qinniuclient.util.HttpUtil;


public class CourseMainActivity extends Activity {

    public static String IMAGE_CACHE_PATH = "imageloader/Cache"; // 图片缓存路径

    private ViewPager adViewPager;
    private List<ImageView> imageViews;// 滑动的图片集合

    private List<View> dots; // 图片标题正文的那些点
    private List<View> dotList;

    /*private TextView tv_date;
    private TextView tv_title;
    private TextView tv_topic_from;
    private TextView tv_topic;*/
    private int currentItem = 0; // 当前图片的索引号
    // 定义的五个指示点
    private View dot0;
    private View dot1;
    private View dot2;
    private View dot3;
    private View dot4;

    private ScheduledExecutorService scheduledExecutorService;

    // 异步加载图片
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;

    // 轮播banner的数据
    private List<AdDomain> adList;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            adViewPager.setCurrentItem(currentItem);
        };
    };



    //listview部分
    private ListView mycourselist;
    private ArrayList<HashMap<String, Object>> mylist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        // 使用ImageLoader之前初始化
        initImageLoader();
        // 获取图片加载实例
        mImageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.top_banner_android)
                .showImageForEmptyUri(R.drawable.top_banner_android)
                .showImageOnFail(R.drawable.top_banner_android)
                .cacheInMemory(true).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
        initAdData();
        startAd();

        //listview部分
        mycourselist = (ListView) findViewById(R.id.jingping_course_listview);
        new MyAsyncTask().execute();
    }

    //网上搜的listview与scrollview一起滚动冲突解决方法，设定listview高度解决
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    /*重写SimpleAdapter，实现课程的点击跳转，及浏览次数*/
    private class MySimpleAdapter extends SimpleAdapter {

        private TextView text2;
        private TextView text0;
        private TextView text1;
        private TextView textnum0;
        private TextView textnum1;
        private TextView textnum2;
        private List<? extends Map<String, ?>> mData;
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View v = super.getView(position, convertView, parent);


            final int myposition;
            myposition = position;

            textnum0 = (TextView) v.findViewById(R.id.jingping_course_1_number0);
            textnum1 = (TextView) v.findViewById(R.id.jingping_course_1_number1);
            textnum2 = (TextView) v.findViewById(R.id.jingping_course_1_number2);
            textnum0.setTag(position);
            textnum1.setTag(position);
            textnum2.setTag(position);

            text0 = (TextView) v.findViewById(R.id.jingping_course_1_introduce_text0);

            text1 = (TextView) v.findViewById(R.id.jingping_course_1_introduce_text1);

            text0 = (TextView) v.findViewById(R.id.jingping_course_1_introduce_text0);
            text0.setTag(position);
            text0.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*点击时浏览次数加一，需要服务器支持*/
                    textnum0.setText(Integer.toString(Integer.valueOf(mData.get(myposition).get("jingping_course_1_introduce_number0").toString()) + 1));
                    Intent intent = new Intent(CourseMainActivity.this, InformationCourseContentActivity.class);
                    /*获取课程名称，传给下一个界面，在下一个界面以课程名称来确定由哪一个界面跳转过来*/
                    String flag = mData.get(myposition).get("jingping_course_1_introduce_text0").toString();
                    intent.putExtra("text", flag);
                    /*Log.e("text0", text0.getText().toString());
                    Log.e("position", Integer.valueOf(myposition).toString());*/
                    startActivity(intent);
                }
            });

            text1 = (TextView) v.findViewById(R.id.jingping_course_1_introduce_text1);
            text1.setTag(position);
            text1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*点击时浏览次数加一*/
                    textnum1.setText(Integer.toString(Integer.valueOf(mData.get(myposition).get("jingping_course_1_introduce_number1").toString()) + 1));
                    /*获取课程名称，传给下一个界面，在下一个界面以课程名称来确定由哪一个界面跳转过来*/
                    Intent intent = new Intent(CourseMainActivity.this, InformationCourseContentActivity.class);
                    String flag = mData.get(myposition).get("jingping_course_1_introduce_text1").toString();
                    intent.putExtra("text", flag);
                    startActivity(intent);
                }
            });

            text2 = (TextView) v.findViewById(R.id.jingping_course_1_introduce_text2);
            text2.setTag(position);
            text2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*点击时浏览次数加一*/
                    textnum2.setText(Integer.toString(Integer.valueOf(mData.get(myposition).get("jingping_course_1_introduce_number2").toString()) + 1));
                    /*获取课程名称，传给下一个界面，在下一个界面以课程名称来确定由哪一个界面跳转过来*/
                    Intent intent = new Intent(CourseMainActivity.this, InformationCourseContentActivity.class);
                    String flag = mData.get(myposition).get("jingping_course_1_introduce_text2").toString();
                    intent.putExtra("text", flag);
                    startActivity(intent);
                }
            });

            return v;
        }

        public MySimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            this.mData = data;
            // TODO Auto-generated constructor stub
        }
    }



    //listitem的数据来源 服务器
    public class MyAsyncTask extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... arg0) {
            //-------to be improved------------------
            return query("tx");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null && !result.equals("network anomaly") && !result.equals("")) {
                //课堂类型 课堂名称
                String[] keySet = {"jingping_course_1_courseType", "jingping_course_1_image0", "jingping_course_1_introduce_text0",
                        "jingping_course_1_introduce_number0", "jingping_course_1_image1", "jingping_course_1_introduce_text1",
                        "jingping_course_1_introduce_number1", "jingping_course_1_image2", "jingping_course_1_introduce_text2",
                        "jingping_course_1_introduce_number2"};
                int[] toIds = {R.id.jingping_course_1_courseType, R.id.jingping_course_1_image0, R.id.jingping_course_1_introduce_text0
                        , R.id.jingping_course_1_number0, R.id.jingping_course_1_image1, R.id.jingping_course_1_introduce_text1
                        , R.id.jingping_course_1_number1, R.id.jingping_course_1_image2, R.id.jingping_course_1_introduce_text2
                        , R.id.jingping_course_1_number2};
                MySimpleAdapter simpleAdapter = new MySimpleAdapter(CourseMainActivity.this, getHoldPosInfo(result), R.layout.activity_course_listview_item, keySet, toIds);
                mycourselist.setAdapter(simpleAdapter);
                setListViewHeightBasedOnChildren(mycourselist);
            } else if (result.equals("")) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "暂无数据", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                /*无网络时的测试用数据*/
                String[] keySet = {"jingping_course_1_courseType", "jingping_course_1_image0", "jingping_course_1_introduce_text0",
                        "jingping_course_1_introduce_number0", "jingping_course_1_image1", "jingping_course_1_introduce_text1",
                        "jingping_course_1_introduce_number1", "jingping_course_1_image2", "jingping_course_1_introduce_text2",
                        "jingping_course_1_introduce_number2"};
                int[] toIds = {R.id.jingping_course_1_courseType, R.id.jingping_course_1_image0, R.id.jingping_course_1_introduce_text0
                        , R.id.jingping_course_1_number0, R.id.jingping_course_1_image1, R.id.jingping_course_1_introduce_text1
                        , R.id.jingping_course_1_number1, R.id.jingping_course_1_image2, R.id.jingping_course_1_introduce_text2
                        , R.id.jingping_course_1_number2};
                MySimpleAdapter simpleAdapter = new MySimpleAdapter(CourseMainActivity.this, mytest("te"), R.layout.activity_course_listview_item, keySet, toIds);
                mycourselist.setAdapter(simpleAdapter);
                setListViewHeightBasedOnChildren(mycourselist);
                Toast toast = Toast.makeText(getApplicationContext(),
                        "网络异常", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

    /*无网络时的测试用数据*/
    private List<HashMap<String, Object>> mytest(String result) {
        mylist = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map;
        for (int i = 0; i < 9; i++) {
            /*String image = "精品课堂" + Integer.toString(i);*/
            String name = "罗双平：现代人力资源管理之岗位工资设计技术" + Integer.toString(i);
            String number = "1";
            map = new HashMap<String, Object>();
            map.put("jingping_course_1_image0", R.drawable.simulation_infobar_user_icon);
            map.put("jingping_course_1_introduce_text0", name);
            map.put("jingping_course_1_introduce_number0", number);
            map.put("jingping_course_1_image1", R.drawable.simulation_infobar_user_icon);
            map.put("jingping_course_1_introduce_text1", name);
            map.put("jingping_course_1_introduce_number1", number);
            map.put("jingping_course_1_image2", R.drawable.simulation_infobar_user_icon);
            map.put("jingping_course_1_introduce_text2", name);
            map.put("jingping_course_1_introduce_number2", number);
            switch (i) {
                case 0:
                    map.put("jingping_course_1_courseType", "炒股基础课程");
                    break;
                case 1:
                    map.put("jingping_course_1_courseType", "道氏理论");
                    break;
                case 2:
                    map.put("jingping_course_1_courseType", "文本挖掘");
                    break;
                case 3:
                    map.put("jingping_course_1_courseType", "行为金融学");
                    break;
                case 4:
                    map.put("jingping_course_1_courseType", "投资风险评估与管理");
                    break;
                case 5:
                    map.put("jingping_course_1_courseType", "人力资源管理");
                    break;
                case 6:
                    map.put("jingping_course_1_courseType", "市场营销");
                    break;
                case 7:
                    map.put("jingping_course_1_courseType", "国学讲堂");
                    break;
                case 8:
                    map.put("jingping_course_1_courseType", "生活点滴");
                    break;
                case 9:
                    map.put("jingping_course_1_courseType", "宏观经济");
                    break;
            }
            mylist.add(map);
        }
        return mylist;
    }

    /*send request to server and
    *get the response(may be null)
    *    or string:"network anomaly"
    *response format:
    *"username;stockname stockcode;currentprice;
	* buyInPrice;profit;marketPrice;holdPositionNum;buyableNum|..."
    */

    //服务器部分需要改
    private String query(String username) {
        String queryString = "username=" + username;
        String url = HttpUtil.BASE_URL + "GetUserSelfSelectServlet?" + queryString;
        return HttpUtil.queryStringForGet(url);
    }

    /*服务器传数据过来了，参看mytest的数据格式，结合网络传输*/
    private List<HashMap<String, Object>> getHoldPosInfo(String result) {
        //for test
        System.out.println(result);

        mylist = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map;
        String[] tar = result.split("\\|");
        int itemNum = tar.length;
        for (int i = 0; i < itemNum; i++) {
            String[] infoOfStock = tar[i].split(";");
            map = new HashMap<String, Object>();
            map.put("course_type", infoOfStock[0]);
            map.put("course_name", infoOfStock[1]);
            mylist.add(map);
        }
        return mylist;
    }
























    private void initImageLoader() {
        File cacheDir = com.nostra13.universalimageloader.utils.StorageUtils
                .getOwnCacheDirectory(getApplicationContext(),
                        IMAGE_CACHE_PATH);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new LruMemoryCache(12 * 1024 * 1024))
                .memoryCacheSize(12 * 1024 * 1024)
                .discCacheSize(32 * 1024 * 1024).discCacheFileCount(100)
                .discCache(new UnlimitedDiscCache(cacheDir))
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();

        ImageLoader.getInstance().init(config);
    }

    private void initAdData() {
        // 广告数据
        adList = getBannerAd();

        imageViews = new ArrayList<ImageView>();

        // 点
        dots = new ArrayList<View>();
        dotList = new ArrayList<View>();
        dot0 = findViewById(R.id.v_dot0);
        dot1 = findViewById(R.id.v_dot1);
        dot2 = findViewById(R.id.v_dot2);
        dot3 = findViewById(R.id.v_dot3);
        dot4 = findViewById(R.id.v_dot4);
        dots.add(dot0);
        dots.add(dot1);
        dots.add(dot2);
        dots.add(dot3);
        dots.add(dot4);
		
		/*tv_date = (TextView) findViewById(R.id.tv_date);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_topic_from = (TextView) findViewById(R.id.tv_topic_from);
		tv_topic = (TextView) findViewById(R.id.tv_topic);*/

        adViewPager = (ViewPager) findViewById(R.id.vp);
        adViewPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
        // 设置一个监听器，当ViewPager中的页面改变时调用
        adViewPager.setOnPageChangeListener(new MyPageChangeListener());
        addDynamicView();
    }

    private void addDynamicView() {
        // 动态添加图片和下面指示的圆点
        // 初始化图片资源
        for (int i = 0; i < adList.size(); i++) {
            ImageView imageView = new ImageView(this);
            // 异步加载图片
            mImageLoader.displayImage(adList.get(i).getImgUrl(), imageView,
                    options);
            imageView.setScaleType(ScaleType.CENTER_CROP);
            imageViews.add(imageView);
            dots.get(i).setVisibility(View.VISIBLE);
            dotList.add(dots.get(i));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void startAd() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 当Activity显示出来后，每两秒切换一次图片显示
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 2,
                TimeUnit.SECONDS);
    }

    private class ScrollTask implements Runnable {

        @Override
        public void run() {
            synchronized (adViewPager) {
                currentItem = (currentItem + 1) % imageViews.size();
                handler.obtainMessage().sendToTarget();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 当Activity不可见的时候停止切换
        scheduledExecutorService.shutdown();
    }

    private class MyPageChangeListener implements OnPageChangeListener {

        private int oldPosition = 0;

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            currentItem = position;
            AdDomain adDomain = adList.get(position);
			/*tv_title.setText(adDomain.getTitle()); // 设置标题
			tv_date.setText(adDomain.getDate());
			tv_topic_from.setText(adDomain.getTopicFrom());
			tv_topic.setText(adDomain.getTopic());*/
            dots.get(oldPosition).setBackgroundResource(R.drawable.activity_course_advertisement_dot_normal);
            dots.get(position).setBackgroundResource(R.drawable.activity_course_advertisement_dot_focused);
            oldPosition = position;
        }
    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return adList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = imageViews.get(position);
            ((ViewPager) container).addView(iv);
            final AdDomain adDomain = adList.get(position);
            // 在这个方法里面设置图片的点击事件
            iv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 处理跳转逻辑
                }
            });
            return iv;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

        @Override
        public void finishUpdate(View arg0) {

        }

    }

    /**
     * 轮播广播模拟数据
     *
     * @return
     */
    public static List<AdDomain> getBannerAd() {
        List<AdDomain> adList = new ArrayList<AdDomain>();

        AdDomain adDomain = new AdDomain();
        adDomain.setId("108078");
		/*adDomain.setDate("3月4日");
		adDomain.setTitle("我和令计划只是同姓");*/
		/*adDomain.setTopicFrom("阿宅");
		adDomain.setTopic("我想知道令狐安和令计划有什么关系？");*/
        adDomain.setImgUrl("http://g.hiphotos.baidu.com/image/w%3D310/sign=bb99d6add2c8a786be2a4c0f5708c9c7/d50735fae6cd7b8900d74cd40c2442a7d9330e29.jpg");
        adDomain.setAd(false);
        adList.add(adDomain);

        AdDomain adDomain2 = new AdDomain();
        adDomain2.setId("108078");
		/*adDomain2.setDate("3月5日");
		adDomain2.setTitle("我和令计划只是同姓");*/
		/*adDomain2.setTopicFrom("小巫");
		adDomain2.setTopic("“我想知道令狐安和令计划有什么关系？”");*/
        adDomain2
                .setImgUrl("http://g.hiphotos.baidu.com/image/w%3D310/sign=7cbcd7da78f40ad115e4c1e2672e1151/eaf81a4c510fd9f9a1edb58b262dd42a2934a45e.jpg");
        adDomain2.setAd(false);
        adList.add(adDomain2);

        AdDomain adDomain3 = new AdDomain();
        adDomain3.setId("108078");
		/*adDomain3.setDate("3月6日");
		adDomain3.setTitle("我和令计划只是同姓");*/
		/*adDomain3.setTopicFrom("旭东");
		adDomain3.setTopic("“我想知道令狐安和令计划有什么关系？”");*/
        adDomain3
                .setImgUrl("http://e.hiphotos.baidu.com/image/w%3D310/sign=392ce7f779899e51788e3c1572a6d990/8718367adab44aed22a58aeeb11c8701a08bfbd4.jpg");
        adDomain3.setAd(false);
        adList.add(adDomain3);

        AdDomain adDomain4 = new AdDomain();
        adDomain4.setId("108078");
		/*adDomain4.setDate("3月7日");
		adDomain4.setTitle("我和令计划只是同姓");*/
		/*adDomain4.setTopicFrom("小软");
		adDomain4.setTopic("“我想知道令狐安和令计划有什么关系？”");*/
        adDomain4
                .setImgUrl("http://d.hiphotos.baidu.com/image/w%3D310/sign=54884c82b78f8c54e3d3c32e0a282dee/a686c9177f3e670932e4cf9338c79f3df9dc55f2.jpg");
        adDomain4.setAd(false);
        adList.add(adDomain4);

        AdDomain adDomain5 = new AdDomain();
        adDomain5.setId("108078");
		/*adDomain5.setDate("3月8日");
		adDomain5.setTitle("我和令计划只是同姓");*/
		/*adDomain5.setTopicFrom("大熊");
		adDomain5.setTopic("“我想知道令狐安和令计划有什么关系？”");*/
        adDomain5
                .setImgUrl("http://e.hiphotos.baidu.com/image/w%3D310/sign=66270b4fe8c4b7453494b117fffd1e78/0bd162d9f2d3572c7dad11ba8913632762d0c30d.jpg");
        adDomain5.setAd(true); // 代表是广告
        adList.add(adDomain5);

        return adList;
    }

}
