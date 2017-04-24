package com.wongxd.w_gank.presenter.fgt;

import android.support.v7.app.AppCompatActivity;

import com.wongxd.w_gank.base.BasePresenterFragment;
import com.wongxd.w_gank.base.aCache.AcacheUtil;
import com.wongxd.w_gank.base.rx.RxEventCodeType;
import com.wongxd.w_gank.base.rx.Subscribe;
import com.wongxd.w_gank.model.MeiZiBean;
import com.wongxd.w_gank.net.GankService;
import com.wongxd.w_gank.net.NetClient;
import com.wongxd.w_gank.presenter.aty.PhotoActivity;
import com.wongxd.w_gank.utils.NetworkAvailableUtils;
import com.wongxd.w_gank.utils.ToastUtil;
import com.wongxd.w_gank.vu.GankMeiZiVu;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wxd1 on 2017/4/19.
 */

public class GankMeiZiFragment extends BasePresenterFragment<GankMeiZiVu> {
    private GankService gankService;

    @Override
    protected Class<GankMeiZiVu> getVuClass() {
        return GankMeiZiVu.class;
    }

    public GankMeiZiFragment() {
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onBindVu() {
        super.onBindVu();
        bus.register(this);

        gankService = NetClient.getGankService();

        doGetList(false, 1);
    }

    @Override
    protected void onDestroyVu() {
        bus.unRegister(this);
        super.onDestroyVu();
    }

    @Subscribe(code = RxEventCodeType.GANK_MEIZI_REQUEST_REFRESH)
    void refresh(Integer page) {
        doGetList(false, page);
    }

    @Subscribe(code = RxEventCodeType.GANK_MEIZI_REQUEST_LOADMORE)
    void loadMore(Integer page) {
        doGetList(true, page);
    }

    @Subscribe(code = RxEventCodeType.GANK_MEIZI_VIEW_BIGIMG)
    void viedBigImg(GankMeiZiVu.ImageBean s) {
        PhotoActivity.startActivity((AppCompatActivity) getActivity(), s.getImgUrl(), s.getIv());
    }

    private void doGetList(boolean isLoadMore, int page) {

        Observable<MeiZiBean> observable;
        if (!NetworkAvailableUtils.isNetworkAvailable(getContext())) {
            observable = Observable.create(e -> {
                MeiZiBean info = (MeiZiBean) AcacheUtil.getDefault(getContext(), AcacheUtil.GankMeiZiCache).getAsObject(page + "");
                if (null == info) {
                    getActivity().runOnUiThread(() -> ToastUtil.Toast(getContext(), page == 1 ? "无网络且无缓存数据" : "无网络，不可获取网络数据"));
                    e.onError(new Throwable("nodata"));
                } else {
                    getActivity().runOnUiThread(() -> ToastUtil.Toast(getContext(), "无网络，已读取缓存"));
                    e.onNext(info);
                }
            });

        } else
            observable = gankService.getMeiZi(page);
        observable.subscribeOn(Schedulers.io())
                .map(gankBean -> {
                    if (gankBean.isError()) {
                        throw new Exception("iserror=true");
                    }
                    AcacheUtil.getDefault(getContext(), AcacheUtil.GankMeiZiCache)
                            .put(page + "", gankBean);
                    return gankBean.getResults();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<MeiZiBean.ResultsBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<MeiZiBean.ResultsBean> list) {
                        if (isLoadMore) {
                            vu.processLoadMore(list, false);
                        } else {
                            vu.processRefresh(list, false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isLoadMore) {
                            vu.processLoadMore(null, true);
                        } else {
                            vu.processRefresh(null, true);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
