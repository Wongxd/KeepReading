package com.wongxd.jueduimeizi.custom.QQSlideMenu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created  on 2017/4/14.
 * 仿QQ的协同侧滑菜单
 */

public class CoordinatorMenu extends FrameLayout {

    private static final String TAG = "DragViewGroup";
    private final int mScreenWidth;
    private final int mScreenHeight;

    private View mMenuView;
    private MainView mMainView;

    private ViewDragHelper mViewDragHelper;

    //状态相关
    private static final int MENU_CLOSED = 100;
    private static final int MENU_OPENED = 200;
    private int mMenuState = MENU_CLOSED;

    //拖拽方向相关
    private int mDragOrientation;
    private static final int LEFT_TO_RIGHT = 3;
    private static final int RIGHT_TO_LEFT = 4;


    //回弹效果 打开或关闭时，滑动距离小于弹性距离 回弹到当前位置
    private static final float SPRING_BACK_VELOCITY = 1500; //弹性速度
    private static final int SPRING_BACK_DISTANCE = 80;  //弹性距离
    private int mSpringBackDistance;

    private static final int MENU_MARGIN_RIGHT = 64;
    private int mMenuWidth;

    //视差相关
    private static final int MENU_OFFSET = 128;
    private int mMenuOffset;

    private static final float TOUCH_SLOP_SENSITIVITY = 1.f;

    //主页阴影相关
    private static final String DEFAULT_SHADOW_OPACITY = "00";
    private String mShadowOpacity = DEFAULT_SHADOW_OPACITY;

    public CoordinatorMenu(Context context) {
        this(context, null);
    }

    public CoordinatorMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CoordinatorMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final float density = getResources().getDisplayMetrics().density;//屏幕密度

        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getResources().getDisplayMetrics().heightPixels;

        mSpringBackDistance = (int) (SPRING_BACK_DISTANCE * density + 0.5f);

        mMenuOffset = (int) (MENU_OFFSET * density + 0.5f);

        mMenuWidth = mScreenWidth - (int) (MENU_MARGIN_RIGHT * density + 0.5f);

        mViewDragHelper = ViewDragHelper.create(this, TOUCH_SLOP_SENSITIVITY, new CoordinatorCallback());

    }

    private class CoordinatorCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return mMainView == child || mMenuView == child;
        }

        //观察被触摸的view
        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            if (capturedChild == mMenuView) {//当触摸的view是menu
                mViewDragHelper.captureChildView(mMainView, activePointerId);//交给main处理
            }
        }


        /**
         * 获取view水平方向的拖拽范围,但是目前不能限制边界,返回的值目前用在手指抬起的时候view缓慢移动的动画世界的计算上面; 最好不要返回0
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return 1;
        }


        /**
         * 控制child在水平方向的移动
         *
         * @param child
         * @param left  表示ViewDragHelper认为你想让当前child的left改变的值,left=child.getLeft()+dx
         * @param dx    本次child水平方向移动的距离
         * @return 表示你真正想让child的left变成的值
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (left < 0) {
                left = 0;
            } else if (left > mMenuWidth) {
                left = mMenuWidth;
            }
            return left;
        }


        /**
         * 手指抬起的执行该方法，
         * releasedChild：当前抬起的view
         * xvel:x方向的移动的速度 正：向右移动， 负：向左移动
         * yvel: y方向移动的速度  正：向上移动， 负：向下移动
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
//            Log.e(TAG, "onViewReleased: xvel: " + xvel);
            if (mDragOrientation == LEFT_TO_RIGHT) {//从左向右滑
                if (mMainView.getLeft() < mSpringBackDistance) {//小于设定的距离
                    closeMenu();//关闭菜单
                } else {
                    openMenu();//否则打开菜单
                }
            } else if (mDragOrientation == RIGHT_TO_LEFT) {//从右向左滑
                if (mMainView.getLeft() < mMenuWidth - mSpringBackDistance) {//小于设定的距离
                    closeMenu();//关闭菜单
                } else {
                    openMenu();//否则打开菜单
                }
            }

        }

        /**
         * 当child的位置改变的时候执行,一般用来做其他子View的伴随移动
         * changedView：位置改变的child
         * left：child当前最新的left
         * top: child当前最新的top
         * dx: 本次水平移动的距离
         * dy: 本次垂直移动的距离
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
//            Log.d(TAG, "onViewPositionChanged: dx:" + dx);
            //dx代表距离上一个滑动时间间隔后的滑动距离
            if (dx > 0) {//正
                mDragOrientation = LEFT_TO_RIGHT;//从左往右
            } else if (dx < 0) {//负
                mDragOrientation = RIGHT_TO_LEFT;//从右往左
            }


            float scale = (float) (mMenuWidth - mMenuOffset) / (float) mMenuWidth;
            int menuLeft = left - ((int) (scale * left) + mMenuOffset);
            mMenuView.layout(menuLeft, mMenuView.getTop(),
                    menuLeft + mMenuWidth, mMenuView.getBottom());


            //main阴影的透明度
            float showing = (float) (mScreenWidth - left) / (float) mScreenWidth;
            int hex = 255 - Math.round(showing * 255);
            if (hex < 16) {
                mShadowOpacity = "0" + Integer.toHexString(hex);
            } else {
                mShadowOpacity = Integer.toHexString(hex);
            }


            //计算滑动百分比
            float fraction = (float) (mMainView.getLeft()) / (float) mMenuWidth;

            //根据百分比来值来确定侧滑菜单是打开还是关闭
            if (fraction == 0 && mMenuState != MENU_CLOSED) {//如果百分比是0，且当前状态不是关闭
                mMenuState = MENU_CLOSED;
                //调用回调方法
                listener.onClose();
            } else if (fraction == 1 && mMenuState != MENU_OPENED) {
                mMenuState = MENU_OPENED;
                //调用回调方法
                listener.onOpen();
            }
            if (listener != null) {
                //只要listener存在，就将百分比暴露出去
                listener.Draging(fraction);
            }
        }
    }


    /**
     * 设置外部监听回调
     */
    private onDragStateChangeListener listener;

    public void setOnDragStateChangeListener(onDragStateChangeListener listener) {
        this.listener = listener;
    }

    /**
     * 外部监听回调
     */
    public interface onDragStateChangeListener {
        /**
         * 侧滑菜单打开
         */
        void onOpen();

        /**
         * 侧滑菜单处于关闭
         */
        void onClose();

        /**
         * 正在拖拽,将此时的百分比随时暴露给调用者
         */
        void Draging(float fraction);
    }


    //加载完布局文件后调用
    @Override
    protected void onFinishInflate() {
        mMenuView = getChildAt(0);
        mMainView = (MainView) getChildAt(1);
        mMainView.setParent(this);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mViewDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //将触摸事件传递给ViewDragHelper，此操作必不可少
        mViewDragHelper.processTouchEvent(event);
        return true;
    }


    /**
     * 重写onlayout  给menu加一个初始的offset （视差）
     *
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        Log.d(TAG, "onLayout: ");
        super.onLayout(changed, left, top, right, bottom);
        MarginLayoutParams menuParams = (MarginLayoutParams) mMenuView.getLayoutParams();
        menuParams.width = mMenuWidth;
        mMenuView.setLayoutParams(menuParams);

        //熄屏后会重掉onlayout   如果打开菜单，熄屏，再亮屏，此时菜单就又恢复到关闭的状态了，因为重新亮屏后，layout方法会重新调用
        if (mMenuState == MENU_OPENED) {//判断菜单的状态为打开的话
            //保持打开的位置
            mMenuView.layout(0, 0, mMenuWidth, bottom);
            mMainView.layout(mMenuWidth, 0, mMenuWidth + mScreenWidth, bottom);
            return;
        }

        mMenuView.layout(-mMenuOffset, top, mMenuWidth - mMenuOffset, bottom);
    }


    /**
     * 重写主要是为了main阴影   防止过度绘制
     *
     * @param canvas
     * @param child
     * @param drawingTime
     * @return
     */
    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {

//      防止过度绘制
        final int restoreCount = canvas.save();//保存画布当前的剪裁信息

        final int height = getHeight();
        final int clipLeft = 0;
        int clipRight = mMainView.getLeft();
        if (child == mMenuView) {
            canvas.clipRect(clipLeft, 0, clipRight, height);//剪裁显示的区域
        }

        boolean result = super.drawChild(canvas, child, drawingTime);//绘制当前view

        //恢复画布之前保存的剪裁信息
        //以正常绘制之后的view
        canvas.restoreToCount(restoreCount);


        //main 阴影相关
        int shadowLeft = mMainView.getLeft();
//        Log.d(TAG, "drawChild: shadowLeft: " + shadowLeft);
        final Paint shadowPaint = new Paint();
//        Log.d(TAG, "drawChild: mShadowOpacity: " + mShadowOpacity);
        shadowPaint.setColor(Color.parseColor("#" + mShadowOpacity + "777777"));
        shadowPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(shadowLeft, 0, mScreenWidth, mScreenHeight, shadowPaint);

        return result;
    }

    //获取菜单状态
    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        if (mMainView.getLeft() == 0) {
            mMenuState = MENU_CLOSED;
        } else if (mMainView.getLeft() == mMenuWidth) {
            mMenuState = MENU_OPENED;
        }
    }

    public void openMenu() {
        mViewDragHelper.smoothSlideViewTo(mMainView, mMenuWidth, 0);
        ViewCompat.postInvalidateOnAnimation(CoordinatorMenu.this);
    }

    public void closeMenu() {
        mViewDragHelper.smoothSlideViewTo(mMainView, 0, 0);
        ViewCompat.postInvalidateOnAnimation(CoordinatorMenu.this);
    }

    public boolean isOpened() {
        return mMenuState == MENU_OPENED;
    }


    //旋转屏幕也会出现类似熄屏的问题，这时就需要调用onSaveInstanceState和onRestoreInstanceState这两个方法分别用来保存和恢复我们菜单的状态。
    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        final SavedState ss = new SavedState(superState);
        ss.menuState = mMenuState;//保存状态
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        final SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        if (ss.menuState == MENU_OPENED) {
            openMenu();
        }
    }

    protected static class SavedState extends AbsSavedState {
        int menuState;

        SavedState(Parcel in, ClassLoader loader) {
            super(in, loader);
            menuState = in.readInt();
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(menuState);
        }

        public static final Creator<SavedState> CREATOR = ParcelableCompat.newCreator(
                new ParcelableCompatCreatorCallbacks<SavedState>() {
                    @Override
                    public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                        return new SavedState(in, loader);
                    }

                    @Override
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                });
    }

}