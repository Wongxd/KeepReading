package com.wongxd.w_gank.base;

import android.os.Bundle;

import com.wongxd.w_gank.base.rx.RxBus;

import io.reactivex.disposables.CompositeDisposable;


/**
 * mvp-presenter
 *
 * @param <V>
 */
public abstract class BasePresenterActivity<V extends Vu> extends BaseSwipeActivity {

    protected V vu;
    protected RxBus bus;
    protected CompositeDisposable disposableList = new CompositeDisposable();//所有观察者序列

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus = RxBus.getDefault();
        try {
            vu = getVuClass().newInstance();
            vu.init(getLayoutInflater(), null, bus, disposableList);
            setContentView(vu.getView());
            onBindVu();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected final void onPause() {
        beforePause();
        super.onPause();
    }

    protected void beforePause() {
    }

    @Override
    protected final void onResume() {
        super.onResume();
        afterResume();
    }

    protected void afterResume() {
    }

    @Override
    protected final void onDestroy() {
        disposableList.clear();//取消
        onDestroyVu();
        vu = null;
        bus = null;
        super.onDestroy();
    }

    @Override
    public final void onBackPressed() {
        if (!handleBackPressed()) {
            super.onBackPressed();
        }
    }

    public boolean handleBackPressed() {
        return false;
    }

    protected abstract Class<V> getVuClass();

    protected void onBindVu() {
    }

    protected void onDestroyVu() {
    }


}
