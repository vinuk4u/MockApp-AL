package com.al.mockapp.utils.views;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by vineeth on 01/04/16
 */
public class MASwipeRefreshLayout extends SwipeRefreshLayout {
    public MASwipeRefreshLayout(Context context) {
        super(context);
    }

    public MASwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return !isRefreshing() && super.onStartNestedScroll(child, target, nestedScrollAxes);
    }
}
