package com.qinniuclient.Course;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.qinniuclient.R;
import com.qinniuclient.util.HttpUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class InformationCourseContentActivity extends ActionBarActivity {

    private VideoView video;
    private Button commentBtn;
    private EditText commentContentEditText;
    private ArrayList<HashMap<String, Object>> mycommentlist;
    private ListView mycommentlistview;
    private TextView commentnum;
    private TextView jingping_course_2_introduce;
    private String thisresourceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_content);

        commentBtn = (Button) findViewById(R.id.jingping_course_2_comment_button);
        commentContentEditText = (EditText) findViewById(R.id.jingping_course_2_comment_edit);


        /*根据课堂名称判断上一级界面是点击哪一个课堂*/
        String flag = "   ";
        Intent intent1 = this.getIntent();
        flag = intent1.getStringExtra("video");
        thisresourceName = flag;

        /*设置标题的跑马灯*/
        TextView coursetittle = (TextView) findViewById(R.id.jingping_course_2_tittle);
        coursetittle.setText(intent1.getStringExtra("text"));

        /*视频代码*/

        /*Uri uri = Uri.parse(HttpUtil.BASE_URL + "courseVideo/try.mp4");*/
        Uri uri = Uri.parse(HttpUtil.BASE_URL + "courseVideo/" + flag + ".mp4");
        VideoView videoView = (VideoView)this.findViewById(R.id.video);
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(uri);
        //videoView.start();
        videoView.requestFocus();

        mycommentlistview = (ListView) findViewById(R.id.jingping_course_2_comment_list);
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentContentEditText.getText().toString().trim().equals("")) {
                    Toast.makeText(InformationCourseContentActivity.this, "评论不能为空",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                new MyAsyncTaskComment().execute();
            }
        });

        new MyAsyncTask().execute();
    }

    private String getCurrentUserName() {
        SharedPreferences sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        String queryStr = sp.getString("USERNAME", "Error");
        return queryStr;
    }

    /**
     * 定义一个类，让其继承AsyncTask这个类
     * Params: String类型，表示传递给异步任务的参数类型是String，通常指定的是URL路径,这里用void
     * Progress: Integer类型，进度条的单位通常都是Integer类型
     * Result：boolean，是否登陆成功
     */
    public class MyAsyncTaskComment extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... arg0) {
            return commentUp();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //
            System.out.println(result);
            if (result != null &&
                    !result.equals("network anomaly") && !"".equals(result)) {
                //--------------------------for test------------
                System.out.println("return :" + result);
                //---------------------------------------------
                String[] tar = result.split("\\+");
                int commentNum = tar.length;
                /*jingping_course_2_introduce = (TextView) findViewById(R.id.jingping_course_2_introduce);
                jingping_course_2_introduce.setText(tar[0]);*/
                commentnum = (TextView) findViewById(R.id.jingping_course_2_comment_number);
                commentnum.setText("全部评论 (" + Integer.valueOf(commentNum).toString() + ")");
                System.out.println("comment-length: " + commentNum);
                mycommentlist = new ArrayList<HashMap<String, Object>>();
                for (int i = 0; i < commentNum; i++) {
                    HashMap<String, Object> mycommentmap = new HashMap<String, Object>();
                    mycommentmap.put("name", tar[i].split("@")[0]);
                    mycommentmap.put("time", tar[i].split("@")[1]);
                    mycommentmap.put("comment", tar[i].split("@")[2]);
                    mycommentlist.add(mycommentmap);
                }

                String[] keySet = {"name",
                        "time",
                        "comment"};
                int[] toIds = {R.id.jingping_course_2_commet_listitem_username,
                        R.id.jingping_course_2_commet_listitem_time,
                        R.id.jingping_course_2_commet_listitem_comment_edit};
                SimpleAdapter simpleAdapter = new SimpleAdapter(
                        InformationCourseContentActivity.this, mycommentlist,
                        R.layout.activity_course_content_comment_listitem, keySet, toIds);
                mycommentlistview.setAdapter(simpleAdapter);
                setListViewHeightBasedOnChildren(mycommentlistview);
            } else {
                Toast.makeText(InformationCourseContentActivity.this, "提交失败，请重试",
                        Toast.LENGTH_SHORT).show();
            }
            commentContentEditText.setText("");
        }
    }

    public class MyAsyncTask extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... arg0) {
            String resourceName = thisresourceName;
            return query(resourceName);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null &&
                    !result.equals("network anomaly") && !"".equals(result)) {
                //--------------------------for test------------
                System.out.println(result);
                //---------------------------------------------
                String[] tar = result.split("\\+");
                int commentNum = tar.length;
                jingping_course_2_introduce = (TextView) findViewById(R.id.jingping_course_2_introduce);
                jingping_course_2_introduce.setText(tar[0]);
                commentnum = (TextView) findViewById(R.id.jingping_course_2_comment_number);
                commentnum.setText("全部评论 (" + Integer.valueOf(commentNum - 1).toString() + ")");
                System.out.println("comment-length: " + commentNum);
                mycommentlist = new ArrayList<HashMap<String, Object>>();
                for (int i = 1; i < commentNum; i++) {
                    HashMap<String, Object> mycommentmap = new HashMap<String, Object>();
                    mycommentmap.put("name", tar[i].split("@")[0]);
                    mycommentmap.put("time", tar[i].split("@")[1]);
                    mycommentmap.put("comment", tar[i].split("@")[2]);
                    mycommentlist.add(mycommentmap);
                }

                String[] keySet = {"name",
                        "time",
                        "comment"};
                int[] toIds = {R.id.jingping_course_2_commet_listitem_username,
                        R.id.jingping_course_2_commet_listitem_time,
                        R.id.jingping_course_2_commet_listitem_comment_edit};
                SimpleAdapter simpleAdapter = new SimpleAdapter(
                        InformationCourseContentActivity.this, mycommentlist,
                        R.layout.activity_course_content_comment_listitem, keySet, toIds);
                mycommentlistview.setAdapter(simpleAdapter);
                setListViewHeightBasedOnChildren(mycommentlistview);
            } else if ("".equals(result)) {
                Toast.makeText(InformationCourseContentActivity.this, "暂无数据",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(InformationCourseContentActivity.this, "网络异常",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }

    private String commentUp() {
        String usrname = getCurrentUserName();
        String resourceName = thisresourceName;
        String commentContent = commentContentEditText.getText().toString().trim();
        return queryForUpload(resourceName, usrname, commentContent);
    }

    //use Post to get send and get all comment
    private String queryForUpload(String resourceName, String usrname, String commentContent) {
        String url = HttpUtil.BASE_URL + "GetCommentServlet";
        NameValuePair paraResourceName = new BasicNameValuePair("resourceName",
                resourceName);
        NameValuePair paraUsrname = new BasicNameValuePair("usrname",
                usrname);
        NameValuePair paraCommentContent = new BasicNameValuePair("commentContent",
                commentContent);
        Date date = new Date();
        String dateStr = new SimpleDateFormat("yy-MM-dd HH:mm").format(date);
        List<NameValuePair> para = new ArrayList<NameValuePair>();
        para.add(paraResourceName);
        para.add(paraUsrname);
        para.add(paraCommentContent);
        para.add(new BasicNameValuePair("createTime", dateStr));
        return HttpUtil.queryStringForPost(url, para);
    }

    //use Get to gain the comment
    private String query(String resourceName) {
        String queryString = "resourceName=" + resourceName;
        String url = HttpUtil.BASE_URL + "GetCommentServlet?" + queryString;
        return HttpUtil.queryStringForGet(url);
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = mycommentlistview.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, mycommentlistview);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = mycommentlistview.getLayoutParams();
        params.height = totalHeight +
                (mycommentlistview.getDividerHeight() *
                        (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        mycommentlistview.setLayoutParams(params);
    }

}
