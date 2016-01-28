package com.example.robin.papers.demo.util;

/**
 * Created by Robin on 2016/1/28.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @Description:解决在scrollview中只显示第一行数据的问题
 * @author http://blog.csdn.net/finddreams
 */
public class MyGridView extends GridView {
    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}
