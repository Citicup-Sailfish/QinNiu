package com.qinniuclient.Course;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.media.MediaPlayer.OnCompletionListener;

import com.qinniuclient.R;

import java.io.File;


public class InformationCourseContentActivity extends ActionBarActivity {

    private VideoView video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_content);
        String flag = "   ";

        /*根据课堂名称判断上一级界面是点击哪一个课堂*/
        Intent intent1 = this.getIntent();
        flag = intent1.getStringExtra("text");
        Log.e("mytest1", flag);

        /*设置标题的跑马灯*/
        TextView coursetittle = (TextView) findViewById(R.id.jingping_course_2_tittle);
        coursetittle.setText(flag);

        /*视频代码*/
        Uri uri = Uri.parse("http://forum.ea3w.com/coll_ea3w/attach/2008_10/12237832415.3gp");
        uri = Uri.parse("http://g3.letv.cn/27/9/3/letv-uts/2522159-AVC-1610564-AAC-123276-2196960-489820604-1b165c6eec261a12e925f48f7ca35892-1366904195636.flv");
        VideoView videoView = (VideoView)this.findViewById(R.id.video);
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(uri);
        //videoView.start();
        videoView.requestFocus();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_information_course_content, menu);
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
