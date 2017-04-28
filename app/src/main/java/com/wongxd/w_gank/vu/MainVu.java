package com.wongxd.w_gank.vu;


import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wongxd.w_gank.R;
import com.wongxd.w_gank.base.Vu;
import com.wongxd.w_gank.base.rx.RxBus;
import com.wongxd.w_gank.base.rx.RxEventCodeType;
import com.wongxd.w_gank.custom.arcNavigationView.ArcNavigationView;
import com.wongxd.w_gank.custom.fadeinTextView.FadingTextView;
import com.wongxd.w_gank.custom.switchIconView.SwitchIconView;
import com.wongxd.w_gank.utils.SystemBarHelper;
import com.wongxd.w_gank.utils.glide.GlideLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wxd1 on 2017/4/18.
 */

public class MainVu implements Vu, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar_aty_main)
    Toolbar toolbarAtyMain;
    @BindView(R.id.si_bottom_android)
    SwitchIconView siBottomAndroid;
    @BindView(R.id.ll_bottom_android)
    LinearLayout llBottomAndroid;
    @BindView(R.id.si_bottom_meizi)
    SwitchIconView siBottomMeizi;
    @BindView(R.id.ll_bottom_meizi)
    LinearLayout llBottomMeizi;
    @BindView(R.id.tv_title_aty_main)
    FadingTextView tvTitleAtyMain;
    @BindView(R.id.fl_content_aty_main)
    FrameLayout flContentAtyMain;
    @BindView(R.id.nav_aty_main)
    ArcNavigationView navAtyMain;
    @BindView(R.id.drawer_aty_main)
    DrawerLayout drawerAtyMain;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    private View view;
    private int lastSeletePos = R.id.si_bottom_android;//上次选中的选项
    RxBus rxBus;
    CompositeDisposable disposableList;

    String[] androids = {"Android", "Gank.io"};
    String[] meizi = {"MeiZi", "Gank.io"};
    private ImageView ivHeader;


    @Override
    public void init(LayoutInflater inflater, ViewGroup container, RxBus rxBus, CompositeDisposable compositeDisposable) {
        view = inflater.inflate(R.layout.aty_main, container, false);
        ButterKnife.bind(this, view);
        this.rxBus = rxBus;
        this.disposableList = compositeDisposable;


        navAtyMain.setNavigationItemSelectedListener(this);
        View headerView = navAtyMain.getHeaderView(0);
        ivHeader = (ImageView) headerView.findViewById(R.id.iv_header);


        SystemBarHelper.tintStatusBar((Activity) inflater.getContext(), inflater.getContext().getResources().getColor(R.color.colorPrimary));
        if (Build.VERSION.SDK_INT < 20) {
            SystemBarHelper.setHeightAndPadding(inflater.getContext(), toolbarAtyMain);
        }

        GlideLoader.LoadAsCircleImage(inflater.getContext(), R.drawable.ic_portrait, ivMenu);
        GlideLoader.LoadAsCircleImage(inflater.getContext(), R.drawable.ic_portrait, ivHeader);

        tvTitleAtyMain.setTimeout(3, FadingTextView.SECONDS);
        tvTitleAtyMain.setTexts(androids);
        tvTitleAtyMain.forceRefresh();
        siBottomMeizi.setIconEnabled(false);
//        siBottomAndroid.setNotificationNumber(1000);
//        siBottomMeizi.setNotificationNumber(2000);

        //toggledrawer 的bus监听
        Disposable toggleDrawerDisposble = rxBus.toObservable(RxEventCodeType.TOGGLE_DRAWLAYOUT, Integer.class)
                .subscribe(integer -> {
                    toggleDrawer();
                });
        compositeDisposable.add(toggleDrawerDisposble);

    }

    private void toggleDrawer() {
        if (drawerAtyMain.isDrawerOpen(GravityCompat.START)) {
            drawerAtyMain.closeDrawer(GravityCompat.START);
        } else drawerAtyMain.openDrawer(GravityCompat.START);
    }

    @Override
    public View getView() {
        return view;
    }

    @OnClick({R.id.ll_bottom_android, R.id.ll_bottom_meizi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_bottom_android:
                if (siBottomAndroid.getNotificationNumber() != 0) {
                    //有角标 刷新

                }
                if (lastSeletePos == R.id.si_bottom_android) return;
                lastSeletePos = R.id.si_bottom_android;
                siBottomAndroid.setIconEnabled(true);
                siBottomMeizi.setIconEnabled(false);

                break;
            case R.id.ll_bottom_meizi:
                if (siBottomMeizi.getNotificationNumber() != 0) {

                }
                if (lastSeletePos == R.id.si_bottom_meizi) return;
                lastSeletePos = R.id.si_bottom_meizi;
                siBottomAndroid.setIconEnabled(false);
                siBottomMeizi.setIconEnabled(true);

                break;
        }

        if (view instanceof LinearLayout) {
            int pos;
            if (siBottomAndroid.isIconEnabled()) {//显示info
                tvTitleAtyMain.setTexts(androids);
                pos = 1;
            } else {
                tvTitleAtyMain.setTexts(meizi);
                pos = 2;
            }
            tvTitleAtyMain.forceRefresh();
            if (null != fragmentSwitchListener) {
                fragmentSwitchListener.switchFragment(R.id.fl_content_aty_main, pos);
            }
        }
    }

    private FragmentSwitchListener fragmentSwitchListener;


    public void setFragmentSwitchListener(FragmentSwitchListener fragmentSwitchListener) {
        this.fragmentSwitchListener = fragmentSwitchListener;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        rxBus.post(RxEventCodeType.DRAWLAYOUT_ITEM_CLICK, item.getItemId());
        drawerAtyMain.postDelayed(() -> drawerAtyMain.closeDrawer(GravityCompat.START), 100);
        return true;
    }

    @OnClick(R.id.iv_menu)
    public void onViewClicked() {
        toggleDrawer();
    }

    public interface FragmentSwitchListener {
        /**
         * @param containerId
         * @param pos         1--tofragment should be android, 2 for meizi
         */
        void switchFragment(int containerId, int pos);
    }

}
