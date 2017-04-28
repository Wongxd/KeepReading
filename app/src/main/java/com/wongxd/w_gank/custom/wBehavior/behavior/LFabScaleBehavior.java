package com.wongxd.w_gank.custom.wBehavior.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.wongxd.w_gank.custom.wBehavior.anim.LFabScaleBehaviorAnim;


/**
 * Created by Lauzy on 2017/3/15.
 */

public class LFabScaleBehavior extends CommonBehavior {

    public LFabScaleBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //判断垂直滑动
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        if (isInit) {
            mCommonAnim = new LFabScaleBehaviorAnim(child);
            isInit = false;
        }
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }
}
