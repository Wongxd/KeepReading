package com.wongxd.w_gank.presenter.fgt;

import com.wongxd.w_gank.base.BasePresenterFragment;
import com.wongxd.w_gank.base.aCache.AcacheUtil;
import com.wongxd.w_gank.base.rx.RxEventCodeType;
import com.wongxd.w_gank.base.rx.Subscribe;
import com.wongxd.w_gank.base.rx.ThreadMode;
import com.wongxd.w_gank.model.GankBean;
import com.wongxd.w_gank.net.GankService;
import com.wongxd.w_gank.net.NetClient;
import com.wongxd.w_gank.presenter.aty.GankWebActivity;
import com.wongxd.w_gank.utils.NetworkAvailableUtils;
import com.wongxd.w_gank.utils.ToastUtil;
import com.wongxd.w_gank.vu.GankAndroidVu;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wxd1 on 2017/4/18.
 */

public class GankAndroidFragment extends BasePresenterFragment<GankAndroidVu> {

    private GankService gankService;

    public GankAndroidFragment() {
    }

    @Override
    protected Class<GankAndroidVu> getVuClass() {
        return GankAndroidVu.class;
    }

    @Override
    protected void onBindVu() {
        super.onBindVu();

        bus.register(this);

        gankService = NetClient.getGankService();

        doGetList(false, 1);
    }


    @Subscribe(code = RxEventCodeType.GANK_ANDROID_REQUEST_REFRESH, threadMode = ThreadMode.CURRENT)
    public void refresh(Integer page) {
        doGetList(false, page);
    }

    @Subscribe(code = RxEventCodeType.GANK_ANDROID_REQUEST_LOADMORE, threadMode = ThreadMode.CURRENT)
    public void loadMore(Integer page) {
        doGetList(true, page);
    }

    @Subscribe(code = RxEventCodeType.GANK_ANDROID_VIEW_DETAIL, threadMode = ThreadMode.CURRENT)
    public void viewDetail(GankBean.ResultsBean resultsBean) {
        GankWebActivity.startWebActivity(getActivity(), resultsBean);
    }

    private void doGetList(boolean isLoadMore, int page) {
        Observable<GankBean> observable;
        if (!NetworkAvailableUtils.isNetworkAvailable(getContext())) {

            observable = Observable.create(e -> {

                GankBean info = (GankBean) AcacheUtil.getDefault(getContext(), AcacheUtil.GankAndroidCache).getAsObject(page + "");
                if (null == info) {
                    getActivity().runOnUiThread(() -> ToastUtil.CustomToast(getContext(), page == 1 ? "无网络且无缓存数据" : "无网络，不可获取网络数据"));
                    e.onError(new Throwable("nodata"));
                } else {
                    getActivity().runOnUiThread(() -> ToastUtil.CustomToast(getContext(), "无网络，已读取缓存"));
                    e.onNext(info);
                }

            });

        } else
            observable = gankService.getAndroid(page);

        observable.subscribeOn(Schedulers.io())
                .map(gankBean -> {
                    if (gankBean.isError()) {
                        throw new Exception("werror");
                    }
                    AcacheUtil.getDefault(getContext(), AcacheUtil.GankAndroidCache)
                            .put(page + "", gankBean);
                    return gankBean.getResults();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<GankBean.ResultsBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableList.add(d);
                    }

                    @Override
                    public void onNext(List<GankBean.ResultsBean> list) {
                        if (null == vu) return;
                        if (isLoadMore) {
                            vu.processLoadMore(list, false);
                        } else {
                            vu.processRefresh(list, false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (null == vu) return;
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


    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onDestroyVu() {
        bus.unRegister(this);
        super.onDestroyVu();
    }
}
