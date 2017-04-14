package com.wongxd.jueduimeizi.base;

import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wxd1 on 2017/4/7.
 */

public abstract class BaseVuImp implements Vu {
    @Override
    public void init(LayoutInflater inflater, ViewGroup container) {
        initView(inflater,container);
    }

    @Override
    public View getView() {
        return getThisView();
    }

    public abstract View getThisView();

    public abstract void initView(LayoutInflater inflater, ViewGroup container);


    public <T extends View> T $(@IdRes int resId){
        return (T) getThisView().findViewById(resId);
    }

}
