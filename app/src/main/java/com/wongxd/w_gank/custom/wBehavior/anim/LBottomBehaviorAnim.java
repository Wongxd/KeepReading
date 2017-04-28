package com.wongxd.w_gank.custom.wBehavior.anim;

import android.animation.ValueAnimator;
import android.view.View;

/**
 * Created by Lauzy on 2017/3/14.
 */

public class LBottomBehaviorAnim extends CommonAnim{

    private View mBottomView;
    private float mOriginalY;

    public LBottomBehaviorAnim(View bottomView) {
        mBottomView = bottomView;
        mOriginalY = mBottomView.getY();
    }

    public void hideBottom() {
//        ValueAnimator animator = ValueAnimator.ofFloat(mBottomView.getY(), mBottomView.getY() + mBottomView.getHeight());

    }

    public void showBottom() {
//        ValueAnimator animator = ValueAnimator.ofFloat(mBottomView.getY(), mBottomView.getY() - mBottomView.getHeight());//Y值会发生变化，采用全局OriginalY

    }

    @Override
    public void show() {
        ValueAnimator animator = ValueAnimator.ofFloat(mBottomView.getY(), mOriginalY);
        animator.setDuration(getDuration());
        animator.setInterpolator(getInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mBottomView.setY((Float) valueAnimator.getAnimatedValue());
            }
        });
        animator.start();
    }

    @Override
    public void hide() {
        ValueAnimator animator = ValueAnimator.ofFloat(mBottomView.getY(), mOriginalY + mBottomView.getHeight());
        animator.setDuration(getDuration());
        animator.setInterpolator(getInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mBottomView.setY((Float) valueAnimator.getAnimatedValue());
            }
        });
        animator.start();
    }
}
