package com.wongxd.w_gank.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * mvp-viwe
 */
public interface Vu {
    void init(LayoutInflater inflater, ViewGroup container);
    View getView();
}
