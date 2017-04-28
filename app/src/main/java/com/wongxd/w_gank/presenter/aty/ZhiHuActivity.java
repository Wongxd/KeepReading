package com.wongxd.w_gank.presenter.aty;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.wongxd.w_gank.R;
import com.wongxd.w_gank.adapter.ZhiHuAdapter;
import com.wongxd.w_gank.base.BasePresenterActivity;
import com.wongxd.w_gank.base.aCache.AcacheUtil;
import com.wongxd.w_gank.model.ZhiHuBean;
import com.wongxd.w_gank.net.NetClient;
import com.wongxd.w_gank.net.ZhiHuNetService;
import com.wongxd.w_gank.presenter.aty.webview.ZhiHuWebviewActivity;
import com.wongxd.w_gank.utils.NetworkAvailableUtils;
import com.wongxd.w_gank.utils.SystemBarHelper;
import com.wongxd.w_gank.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class ZhiHuActivity extends BasePresenterActivity<ZhiHuMainVu> {

    private AppCompatActivity thisActivity;
    private Context mContext;
    private ZhiHuAdapter zhiHuAdapter;
    private String lastDate;
    private List<Map<String, String>> popList = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private ZhiHuNetService zhiHuNetService;


    @Override
    protected void onBindVu() {
        super.onBindVu();
        thisActivity = this;
        mContext = this.getApplicationContext();


        SystemBarHelper.immersiveStatusBar(this);
        SystemBarHelper.setHeightAndPadding(this, vu.getToolbar());

        EasyRecyclerView rv = initRecycleView();

        simpleAdapter = new SimpleAdapter(mContext, popList, R.layout.pop_list_item, new String[]{"date"}
                , new int[]{R.id.tv_pop_list_item});
        view = LayoutInflater.from(mContext).inflate(R.layout.pop_list, null, false);
        lv = (ListView) view.findViewById(R.id.lv_pop_list);
        lv.setAdapter(simpleAdapter);

        initTitleClick();

       zhiHuNetService = NetClient.getZhihuService();

        rv.setRefreshing(true, true);

    }

    SimpleAdapter simpleAdapter;
    View view;
    ListView lv;

    @android.support.annotation.NonNull
    private EasyRecyclerView initRecycleView() {
        EasyRecyclerView rv = vu.getRecycleView();
        rv.setAdapterWithProgress(zhiHuAdapter = new ZhiHuAdapter(thisActivity));
        layoutManager = new LinearLayoutManager(mContext);
        rv.setLayoutManager(layoutManager);

        zhiHuAdapter.setOnItemClickListener(position -> {
            if (!zhiHuAdapter.getAllData().get(position).getDate().contains("-")) {

                Intent intent = new Intent(thisActivity, ZhiHuWebviewActivity.class);
                intent.putExtra("id", zhiHuAdapter.getAllData().get(position).getId());
                intent.putExtra("title", zhiHuAdapter.getAllData().get(position).getTitle());
                startActivity(intent);
            }
        });
        zhiHuAdapter.setMore(R.layout.rv_view_more, () -> {
            getZhiHuBeforeList(lastDate);
        });

        zhiHuAdapter.setNoMore(R.layout.rv_view_nomore, new RecyclerArrayAdapter.OnNoMoreListener() {
            @Override
            public void onNoMoreShow() {
                zhiHuAdapter.resumeMore();
            }

            @Override
            public void onNoMoreClick() {
                zhiHuAdapter.resumeMore();
            }
        });

        zhiHuAdapter.setError(R.layout.rv_view_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
            }

            @Override
            public void onErrorClick() {
                zhiHuAdapter.resumeMore();
            }
        });

        rv.setRefreshListener(this::getZhiHuList);


        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取最后一个可见view的位置
                    int lastItemPosition = linearManager.findLastVisibleItemPosition();
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();

                    if (zhiHuAdapter.getAllData().size() != 0 && firstItemPosition != -1
                            && zhiHuAdapter.getCount() > firstItemPosition) {
                        String date = zhiHuAdapter.getAllData().get(firstItemPosition).getDate();
                        if (!date.contains("-")) {
                            String year = date.substring(0, 4);
                            String month = date.substring(4, 6);
                            String day = date.substring(6);
                            vu.setTitle(year + "-" + month + "-" + day);
                        } else vu.setTitle(date);
                    }

                }
            }
        });
        return rv;
    }


    private void initTitleClick() {
        vu.getTvTitle().setOnClickListener(v -> {

            backgroundAlpha(0.5f);

            //选择日期的popwindow

            PopupWindow pop = new PopupWindow(view,
                    (int) (vu.getTvTitle().getWidth() * 1.5), ViewGroup.LayoutParams.WRAP_CONTENT, true);

            pop.setOutsideTouchable(true);
            //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
            pop.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            pop.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            pop.showAsDropDown(vu.getTvTitle(), -(int) (vu.getTvTitle().getWidth() * 0.25), 0);

            //添加pop窗口关闭事件
            pop.setOnDismissListener(new poponDismissListener());


            lv.setOnItemClickListener((parent, view1, position, id) -> {

                String p = popList.get(position).get("position");
                vu.getRecycleView().scrollToPosition(Integer.valueOf(p));
                pop.dismiss();
            });
        });
    }

    /**
     * 添加弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     */
    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }


    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);

    }


    /**
     * 获取知乎信息
     */
    private void getZhiHuList() {

        Observable<ZhiHuBean> zhiHuBeanObservable = null;
        if (!NetworkAvailableUtils.isNetworkAvailable(mContext)) {
            zhiHuBeanObservable = Observable.create(new ObservableOnSubscribe<ZhiHuBean>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<ZhiHuBean> e) throws Exception {
                    runOnUiThread(() -> ToastUtil.CustomToast(mContext, "无网络，已智能读取缓存"));
                    ZhiHuBean zhiHuBean = (ZhiHuBean) AcacheUtil.getDefault(mContext, AcacheUtil.ZhiHuCache).getAsObject("laset");
                    e.onNext(zhiHuBean);
                    e.onComplete();
                }
            });
        } else {
            zhiHuBeanObservable = zhiHuNetService.getZhiHuLatest();
        }
        zhiHuBeanObservable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<ZhiHuBean>() {
                    @Override
                    public void accept(@NonNull ZhiHuBean zhiHuBean) throws Exception {
                        //缓存知乎bean
                        AcacheUtil.getDefault(mContext, AcacheUtil.ZhiHuCache).put("laset", zhiHuBean);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(zhiHuBean -> {
                    vu.getSliderAtyMain().removeAllSliders();
                    for (ZhiHuBean.TopStoriesBean t :
                            zhiHuBean.getTop_stories()) {
                        TextSliderView textSliderView = new TextSliderView(mContext);
                        textSliderView
                                .description(t.getTitle())
                                .image(t.getImage())
                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                .setOnSliderClickListener(slider -> {
                                    Intent i = new Intent(thisActivity, ZhiHuWebviewActivity.class);
                                    i.putExtra("title", slider.getBundle().get("title") + "");
                                    i.putExtra("id", slider.getBundle().getInt("id"));
                                    startActivity(i);
                                });
                        textSliderView.bundle(new Bundle());
                        textSliderView.getBundle()
                                .putString("title", t.getTitle());
                        textSliderView.getBundle()
                                .putInt("id", t.getId());
                        vu.getSliderAtyMain().addSlider(textSliderView);
                    }

//                        vu.getSliderAtyMain().setPresetTransformer(SliderLayout.Transformer.RotateUp);
                    vu.getSliderAtyMain().setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                    vu.getSliderAtyMain().setDuration(5000);
                })
                .map(zhiHuBean -> {
                    //

                    String date = zhiHuBean.getDate();
                    lastDate = date;
                    List<ZhiHuBean.StoriesBean> stories = new ArrayList<>();

                    ZhiHuBean.StoriesBean first = new ZhiHuBean.StoriesBean();
                    String year = date.substring(0, 4);
                    String month = date.substring(4, 6);
                    String day = date.substring(6);
                    first.setDate(year + "-" + month + "-" + day);
                    stories.add(first);

                    for (ZhiHuBean.StoriesBean s : zhiHuBean.getStories()) {
                        s.setDate(date);
                        stories.add(s);
                    }

                    return stories;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ZhiHuBean.StoriesBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableList.add(d);

                    }

                    @Override
                    public void onNext(List<ZhiHuBean.StoriesBean> stories) {
                        popList.clear();
                        Map<String, String> map = new HashMap<>();
                        map.put("date", stories.get(0).getDate());
                        map.put("position", 0 + "");
                        popList.add(map);

                        zhiHuAdapter.clear();
                        zhiHuAdapter.addAll(stories);
                        simpleAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.CustomToast(thisActivity, "msg: " + e.getMessage());
                        vu.getRecycleView().showError();
                    }

                    @Override
                    public void onComplete() {


                    }
                });
    }

    /**
     * getzhihubeforlist
     *
     * @param time
     */
    private void getZhiHuBeforeList(final String time) {

        Observable<ZhiHuBean> zhiHuBeanObservable = null;
        if (!NetworkAvailableUtils.isNetworkAvailable(mContext)) {

            zhiHuBeanObservable = Observable.create(new ObservableOnSubscribe<ZhiHuBean>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<ZhiHuBean> e) throws Exception {

                    ZhiHuBean zhiHuBean = (ZhiHuBean) AcacheUtil.getDefault(mContext, AcacheUtil.ZhiHuCache).getAsObject(time);
                    if (zhiHuBean != null
                            &&
                            !zhiHuBean.getDate().equals(time)) {
                        runOnUiThread(() -> ToastUtil.CustomToast(mContext, "无网络，已智能读取缓存"));
                        e.onNext(zhiHuBean);
                        e.onComplete();
                    } else {
                        e.onError(new Throwable("无网络，不能获取数据"));
                    }

                }
            });

        } else {
            zhiHuBeanObservable = zhiHuNetService.getZhiHuBefore(time);
        }


        zhiHuBeanObservable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(zhiHuBean -> {
                    //缓存知乎bean
                    AcacheUtil.getDefault(mContext, AcacheUtil.ZhiHuCache).put(time, zhiHuBean);
                })
                .map(zhiHuBean -> {
                    String date = zhiHuBean.getDate();
                    lastDate = date;
                    List<ZhiHuBean.StoriesBean> stories = new ArrayList<>();

                    ZhiHuBean.StoriesBean first = new ZhiHuBean.StoriesBean();
                    String year = date.substring(0, 4);
                    String month = date.substring(4, 6);
                    String day = date.substring(6);
                    first.setDate(year + "-" + month + "-" + day);
                    stories.add(first);

                    for (ZhiHuBean.StoriesBean s : zhiHuBean.getStories()) {
                        s.setDate(date);
                        stories.add(s);
                    }
                    return stories;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ZhiHuBean.StoriesBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableList.add(d);
                    }

                    @Override
                    public void onNext(List<ZhiHuBean.StoriesBean> stories) {
                        Map<String, String> map = new HashMap<>();
                        map.put("date", stories.get(0).getDate());
                        map.put("position", zhiHuAdapter.getCount() + "");
                        popList.add(map);

                        zhiHuAdapter.addAll(stories);
                        simpleAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.CustomToast(thisActivity, "msg: " + e.getMessage());
//                        zhiHuAdapter.pauseMore();
                        //添加空，可以显示没有更多
                        zhiHuAdapter.add(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    protected void afterResume() {
        super.afterResume();

    }


    @Override
    protected void onDestroyVu() {
        vu.getSliderAtyMain().stopAutoCycle();
        super.onDestroyVu();
    }


    @Override
    protected Class<ZhiHuMainVu> getVuClass() {
        return ZhiHuMainVu.class;
    }


}
