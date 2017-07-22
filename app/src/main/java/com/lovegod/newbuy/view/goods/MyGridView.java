package com.lovegod.newbuy.view.goods;

import android.content.Context;
import android.widget.GridView;

/**
 * *****************************************
 * Created by thinking on 2017/6/14.
 * 创建时间：
 * <p>
 * 描述：
 * <p/>
 * <p/>
 * *******************************************
 */

public class MyGridView extends GridView {

    public MyGridView(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }
    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
