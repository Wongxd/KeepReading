package com.wongxd.w_gank.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wongxd.w_gank.base.rx.RxBus;

import io.reactivex.disposables.CompositeDisposable;

/**
 * mvp-viwe
 */
public interface Vu {
    void init(LayoutInflater inflater, ViewGroup container, RxBus rxBus, CompositeDisposable compositeDisposable);
    View getView();
}
