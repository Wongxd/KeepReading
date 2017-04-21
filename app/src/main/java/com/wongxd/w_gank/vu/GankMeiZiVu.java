package com.wongxd.w_gank.vu;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.SpaceDecoration;
import com.wongxd.w_gank.R;
import com.wongxd.w_gank.base.Vu;
import com.wongxd.w_gank.base.rx.RxBus;
import com.wongxd.w_gank.base.rx.RxEventCodeType;
import com.wongxd.w_gank.model.MeiZiBean;
import com.wongxd.w_gank.utils.DensityUtil;
import com.wongxd.w_gank.utils.glide.GlideRoundTransform;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;


/**
 * Created by wxd1 on 2017/4/19.
 */

public class GankMeiZiVu implements Vu, RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_fgt_gank_meizi)
    EasyRecyclerView rvFgtGankMeizi;
    private View view;
    List<MeiZiBean.ResultsBean> MeiZiBeanList = new ArrayList<>();
    RecyclerArrayAdapter<MeiZiBean.ResultsBean> adapter;
    private RxBus bus;
    private CompositeDisposable disposableList;
    private int page = 2;//初始值应该为2


    @Override
    public void init(LayoutInflater inflater, ViewGroup container, RxBus rxBus, CompositeDisposable compositeDisposable) {
        bus = rxBus;
        disposableList = compositeDisposable;
        view = inflater.inflate(R.layout.fgt_gank_meizi, container, false);
        ButterKnife.bind(this, view);
        initRecycleView(inflater.getContext());
    }

    @Override
    public View getView() {
        return view;
    }


    private void initRecycleView(Context c) {
        rvFgtGankMeizi.setAdapterWithProgress(adapter = new RecyclerArrayAdapter<MeiZiBean.ResultsBean>(c) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new GankMeiZiViewHolder(parent);
            }

        });


        rvFgtGankMeizi.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        SpaceDecoration itemDecoration = new SpaceDecoration((int) DensityUtil.dip2px(c, 5));
        itemDecoration.setPaddingEdgeSide(true);
        itemDecoration.setPaddingStart(true);
        itemDecoration.setPaddingHeaderFooter(true);
        rvFgtGankMeizi.addItemDecoration(itemDecoration);

        rvFgtGankMeizi.setItemAnimator(new DefaultItemAnimator());


        rvFgtGankMeizi.setProgressView(R.layout.rv_view_progress);
        rvFgtGankMeizi.setEmptyView(R.layout.rv_view_empty);
        rvFgtGankMeizi.setErrorView(R.layout.view_error);

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

        rvFgtGankMeizi.setRefreshListener(this);

    }


    @Override
    public void onRefresh() {
        page = 1;
        RxBus.getDefault().post(RxEventCodeType.GANK_MEIZI_REQUEST_REFRESH, page);
    }

    @Override
    public void onLoadMore() {
        RxBus.getDefault().post(RxEventCodeType.GANK_MEIZI_REQUEST_LOADMORE, page);
    }

    public void processRefresh(List<MeiZiBean.ResultsBean> list, boolean isError) {
        adapter.clear();
        if (isError) {
            adapter.pauseMore();
            return;
        }
        adapter.addAll(list);
    }

    public void processLoadMore(List<MeiZiBean.ResultsBean> list, boolean isError) {
        if (isError) {
            adapter.pauseMore();
            return;
        }
        adapter.addAll(list);
        page++;
    }


    public class GankMeiZiViewHolder extends BaseViewHolder<MeiZiBean.ResultsBean> {
        private TextView title;
        private ImageView icon;

        public GankMeiZiViewHolder(ViewGroup parent) {
            super(parent, R.layout.rv_item_fgt_gank_meizi);
            title = $(R.id.title);
            icon = $(R.id.icon);

        }


        @Override
        public void setData(MeiZiBean.ResultsBean data) {
            super.setData(data);


            title.setText(data.getDesc());
            String imgUrl = data.getUrl();

            Glide.with(getContext().getApplicationContext()).load(imgUrl)
                    .placeholder(new ColorDrawable(getContext().getResources().getColor(R.color.colorPrimary)))
                    .transform(new GlideRoundTransform(getContext()))
                    .into(icon);

            itemView.setOnClickListener(v -> RxBus.getDefault().post(RxEventCodeType.GANK_MEIZI_VIEW_BIGIMG, new ImageBean(imgUrl, icon)));

        }
    }

    public class ImageBean {
        private String imgUrl;
        private ImageView iv;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public ImageView getIv() {
            return iv;
        }

        public void setIv(ImageView iv) {
            this.iv = iv;
        }

        public ImageBean(String imgUrl, ImageView iv) {

            this.imgUrl = imgUrl;
            this.iv = iv;
        }
    }
}
