package com.qinniuclient.Course;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by 加松 on 2015/8/28.
 */
public class CourseContentMarqueeTextView extends TextView {

    public CourseContentMarqueeTextView(Context context) {
        super(context);
    }

    public CourseContentMarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CourseContentMarqueeTextView(Context context, AttributeSet attrs,
                           int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

}