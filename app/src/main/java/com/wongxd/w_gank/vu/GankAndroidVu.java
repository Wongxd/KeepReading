package com.wongxd.w_gank.vu;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.wongxd.w_gank.R;
import com.wongxd.w_gank.base.Vu;
import com.wongxd.w_gank.base.rx.RxBus;
import com.wongxd.w_gank.base.rx.RxEventCodeType;
import com.wongxd.w_gank.model.GankBean;
import com.wongxd.w_gank.utils.glide.GlideRoundTransform;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by wxd1 on 2017/4/18.
 */

public class GankAndroidVu implements Vu, RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_fgt_gank_android)
    EasyRecyclerView rvFgtGankAndroid;
    private View view;
    List<GankBean.ResultsBean> gankBeanList = new ArrayList<>();
    RecyclerArrayAdapter<GankBean.ResultsBean> adapter;
    private RxBus bus;
    private CompositeDisposable disposableList;
    private int page = 2; //第一次loadmore的page初始值

    @Override
    public void init(LayoutInflater inflater, ViewGroup container, RxBus rxBus, CompositeDisposable compositeDisposable) {
        bus = rxBus;
        disposableList = compositeDisposable;
        view = inflater.inflate(R.layout.fgt_gank_android, container, false);
        ButterKnife.bind(this, view);
        initRecycleView(inflater.getContext());
    }

    @Override
    public View getView() {
        return view;
    }


    private void initRecycleView(Context c) {
        rvFgtGankAndroid.setAdapterWithProgress(adapter = new RecyclerArrayAdapter<GankBean.ResultsBean>(c) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new GankAndroidViewHolder(parent);
            }

        });
        rvFgtGankAndroid.setLayoutManager(new LinearLayoutManager(c));
        rvFgtGankAndroid.setItemAnimator(new DefaultItemAnimator());

        rvFgtGankAndroid.setProgressView(R.layout.rv_view_progress);
        rvFgtGankAndroid.setEmptyView(R.layout.rv_view_empty);
        rvFgtGankAndroid.setErrorView(R.layout.view_error);

        adapter.setMore(R.layout.rv_view_more, this);
        adapter.setNoMore(R.layout.rv_view_nomore, new RecyclerArrayAdapter.OnNoMoreListener() {
            @Override
            public void onNoMoreShow() {
                adapter.resumeMore();
            }

            @Override
            public void onNoMoreClick() {
                adapter.resumeMore();
            }
        });
        adapter.setError(R.layout.rv_view_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
                adapter.resumeMore();
            }

            @Override
            public void onErrorClick() {
                adapter.resumeMore();
            }
        });

        rvFgtGankAndroid.setRefreshListener(this);

    }


    @Override
    public void onRefresh() {
        page = 1;
        RxBus.getDefault().post(RxEventCodeType.GANK_ANDROID_REQUEST_REFRESH, page);
    }

    @Override
    public void onLoadMore() {
        RxBus.getDefault().post(RxEventCodeType.GANK_ANDROID_REQUEST_LOADMORE, page);
    }

    public void processRefresh(List<GankBean.ResultsBean> list, boolean isError) {
        adapter.clear();
        if (isError) {
            adapter.pauseMore();
            return;
        }

        adapter.addAll(list);
    }

    public void processLoadMore(List<GankBean.ResultsBean> list, boolean isError) {
        if (isError) {
            adapter.pauseMore();
            return;
        }
        adapter.addAll(list);
        page++;
    }


    public class GankAndroidViewHolder extends BaseViewHolder<GankBean.ResultsBean> {
        private TextView title;
        private TextView time;
        private TextView who;
        private ImageView icon;

        public GankAndroidViewHolder(ViewGroup parent) {
            super(parent, R.layout.rv_item_fgt_gank_android);
            title = $(R.id.title);
            time = $(R.id.time);
            who = $(R.id.who);
            icon = $(R.id.icon);

        }


        @Override
        public void setData(GankBean.ResultsBean data) {
            super.setData(data);
            title.setText(data.getDesc());
            time.setText(data.getPublishedAt().substring(0, 10));
            who.setText(data.getWho());
            String imgUrl = "";
            if (null != data.getImages()) {
                imgUrl = data.getImages().get(0);
            }

            String[] types = imgUrl.split("/.");
            String type = types[types.length - 1];
            if (type.equals("gif")) {

                Glide.with(getContext().getApplicationContext()).load(imgUrl)
                        .asGif()
                        .into(icon);
            }

            Glide.with(getContext().getApplicationContext()).load(imgUrl)
                    .asBitmap().transform(new GlideRoundTransform(getContext()))
                    .into(icon);

            //点击事件，rxbus
            itemView.setOnClickListener(v -> bus.post(RxEventCodeType.GANK_ANDROID_VIEW_DETAIL, data));
        }
    }

}
