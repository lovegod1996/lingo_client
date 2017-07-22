package com.lovegod.newbuy.view.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lovegod.newbuy.R;

import org.w3c.dom.Text;

/**
 * 封装的搜索控件
 * Created by ywx on 2017/7/13.
 */

public class SearchLayout extends LinearLayout {
    private String searchHint;
    private TextView hintText;
    public SearchLayout(Context context) {
        this(context,null);
    }

    public SearchLayout(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public SearchLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.search_layout,this);
        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.SearchLayout);
        searchHint=ta.getString(R.styleable.SearchLayout_search_hint);
        ta.recycle();
        hintText=(TextView)findViewById(R.id.search_layout_text);
        hintText.setText(searchHint);
    }

    public String getSearchHint() {
        return searchHint;
    }

    public void setSearchHint(String searchHint) {
        this.searchHint = searchHint;
        hintText.setText(searchHint);
    }
}
