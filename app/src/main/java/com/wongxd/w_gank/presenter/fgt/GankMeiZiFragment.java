package com.wongxd.w_gank.presenter.fgt;

import android.support.v7.app.AppCompatActivity;

import com.wongxd.w_gank.base.BasePresenterFragment;
import com.wongxd.w_gank.base.aCache.ACache;
import com.wongxd.w_gank.base.aCache.AcacheUtil;
import com.wongxd.w_gank.base.rx.RxEventCodeType;
import com.wongxd.w_gank.model.MeiZiBean;
import com.wongxd.w_gank.net.GankService;
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
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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
        bus.toObservable(RxEventCodeType.GANK_MEIZI_REQUEST_REFRESH, Integer.class)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableList.add(d);
                    }

                    @Override
                    public void onNext(Integer page) {
                        doGetList(false, page);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        bus.toObservable(RxEventCodeType.GANK_MEIZI_REQUEST_LOADMORE, Integer.class)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableList.add(d);
                    }

                    @Override
                    public void onNext(Integer page) {
                        doGetList(true, page);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


        disposableList.add(bus.toObservable(RxEventCodeType.GANK_MEIZI_VIEW_BIGIMG, GankMeiZiVu.ImageBean.class).subscribe(s -> {
//            Intent i = new Intent(getActivity(), PhotoActivity.class);
//            i.putExtra(PhotoActivity.URL, s.getImgUrl());
//            startActivity(i);
            PhotoActivity.startActivity((AppCompatActivity) getActivity(),s.getImgUrl(),s.getIv());
        }));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gank.io/api/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        gankService = retrofit.create(GankService.class);

        doGetList(false, 1);
    }


    private void doGetList(boolean isLoadMore, int page) {

        Observable<MeiZiBean> observable;
        if (!NetworkAvailableUtils.isNetworkAvailable(getContext())) {
            observable = Observable.create(e -> {
                MeiZiBean info = (MeiZiBean) AcacheUtil.getDefault(getContext(), AcacheUtil.GankMeiZiCache).getAsObject(page + "");
                if (null == info) {
                    getActivity().runOnUiThread(() -> ToastUtil.Toast(getContext(),  page==1?"无网络且无缓存数据":"无网络，不可获取网络数据"));
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
                            .put(page + "", gankBean, 8 * ACache.TIME_HOUR);
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
