package com.wongxd.w_gank.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wongxd.w_gank.base.rx.RxBus;

import io.reactivex.disposables.CompositeDisposable;


/**
 * mvp-presenter
 *
 * @param <V>
 */
public abstract class BasePresenterFragment<V extends Vu> extends Fragment {

    protected V vu;
    protected RxBus bus;
    protected CompositeDisposable disposableList = new CompositeDisposable();//所有观察者序列


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus = RxBus.getDefault();
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        try {
            vu = getVuClass().newInstance();
            vu.init(inflater, container,bus,disposableList);
            onBindVu();
            view = vu.getView();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public final void onDestroyView() {
        disposableList.clear();//取消
        onDestroyVu();
        vu = null;
        super.onDestroyView();
    }

    protected void onDestroyVu() {
    }


    @Override
    public final void onPause() {
        beforePause();
        super.onPause();
    }

    protected void beforePause() {
    }

    @Override
    public final void onResume() {
        super.onResume();
        afterResume();
    }

    protected void afterResume() {
    }

    protected void onBindVu() {
    }


    protected abstract Class<V> getVuClass();


    private boolean isPrepare = false;
    private boolean isFirst = true;
    private boolean isVisible = false;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isPrepare = true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) isVisible = true;
        if (isVisible && isFirst && isPrepare) {
            isFirst = false;
            lazyLoad();
        }
    }


    /**
     * 只有在可见时才加载
     */
    protected abstract void lazyLoad();
}