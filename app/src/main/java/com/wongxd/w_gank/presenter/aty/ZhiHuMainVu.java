package com.wongxd.w_gank.presenter.aty;

import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.wongxd.w_gank.R;
import com.wongxd.w_gank.base.Vu;
import com.wongxd.w_gank.base.rx.RxBus;

import io.reactivex.disposables.CompositeDisposable;


/**
 * Created by wxd1 on 2017/4/6.
 */

public class ZhiHuMainVu implements Vu {


    private View view;
    private EasyRecyclerView rvAtyMain;
    private Toolbar toolbar;
    private TextView tvTitle;
    private SliderLayout sliderAtyMain;



    public void setTitle(String s) {
        tvTitle.setText(s);

    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }


    public EasyRecyclerView getRecycleView() {
        return rvAtyMain;
    }


    public SliderLayout getSliderAtyMain() {
        return sliderAtyMain;
    }

    @Override
    public void init(LayoutInflater inflater, ViewGroup container, RxBus rxBus, CompositeDisposable compositeDisposable) {
        view = inflater.inflate(R.layout.aty_zhihu, container, false);
        rvAtyMain = (EasyRecyclerView) view.findViewById(R.id.rv_aty_main);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        tvTitle = (TextView) view.findViewById(R.id.tv_title_vu_main);
        sliderAtyMain = (SliderLayout) view.findViewById(R.id.slider_aty_main);
    }

    @Override
    public View getView() {
        return view;
    }
}
