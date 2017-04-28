package com.wongxd.w_gank.base;

import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.jude.swipbackhelper.SwipeListener;
import com.wongxd.w_gank.base.broadcastReceiver.NetCheckReceive;
import com.wongxd.w_gank.base.rx.RxBus;
import com.wongxd.w_gank.base.rx.RxEventCodeType;
import com.wongxd.w_gank.utils.ToastUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * Created by wxd1 on 2017/4/5.
 */

public class BaseSwipeActivity extends BaseActivity {
    private NetCheckReceive netCheckReceive;//检查网络状态的广播
    private Observable<String> netObservable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.onCreate(this);


//        如果需要可在SwipeBackHelper.onCreate()之后进行如下参数设置：
        SwipeBackHelper.getCurrentPage(this)//获取当前页面
                .setSwipeBackEnable(true)//设置是否可滑动
//                .setSwipeEdge(200)//可滑动的范围。px。200表示为左边200px的屏幕
                .setSwipeEdgePercent(0.12f)//可滑动的范围。百分比。0.2表示为左边20%的屏幕
                .setSwipeSensitivity(0.5f)//对横向滑动手势的敏感程度。0为迟钝 1为敏感
                .setScrimColor(Color.parseColor("#90000000"))//底层阴影颜色
                .setClosePercent(0.8f)//触发关闭Activity百分比
                .setSwipeRelateEnable(true)//是否与下一级activity联动(微信效果)。默认关
                .setSwipeRelateOffset(500)//activity联动时的偏移量。默认500px。
                .setDisallowInterceptTouchEvent(false)//不抢占事件，默认关（事件将先由子View处理再由滑动关闭处理）
                .addListener(new SwipeListener() {//滑动监听

                    @Override
                    public void onScroll(float percent, int px) {//滑动的百分比与距离
                    }

                    @Override
                    public void onEdgeTouch() {//当开始滑动
                    }

                    @Override
                    public void onScrollToClose() {//当滑动关闭

                    }
                });


        //注册检查网络的广播
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        netCheckReceive = new NetCheckReceive();
        this.registerReceiver(netCheckReceive, filter);

        netObservable = RxBus.getDefault().toObservable(RxEventCodeType.NETEVENT, String.class);
        netObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (!s.equals("WIFI连接")) {
                        ToastUtil.CustomToast(getApplicationContext(), s);
                    }
                });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        netObservable.unsubscribeOn(AndroidSchedulers.mainThread());
        this.unregisterReceiver(netCheckReceive);
        netObservable=null;
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }

}
