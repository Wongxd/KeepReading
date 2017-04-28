package com.wongxd.w_gank;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wongxd.w_gank.base.BasePresenterActivity;
import com.wongxd.w_gank.base.rx.Subscribe;
import com.wongxd.w_gank.presenter.aty.ZhiHuActivity;
import com.wongxd.w_gank.presenter.fgt.GankAndroidFragment;
import com.wongxd.w_gank.presenter.fgt.GankMeiZiFragment;
import com.wongxd.w_gank.utils.SystemUtils;
import com.wongxd.w_gank.utils.ToastUtil;
import com.wongxd.w_gank.vu.MainVu;

import static com.wongxd.w_gank.base.rx.RxEventCodeType.DRAWLAYOUT_ITEM_CLICK;

public class MainActivity extends BasePresenterActivity<MainVu> {

    private AppCompatActivity thisActivity;
    private Context mContext;
    private FragmentManager mFragmentManager;
    private Fragment mCurrentFragment;
    private Fragment gankAndroidFragment;
    private GankMeiZiFragment gankMeiZiFragment;

    @Override
    protected void onBindVu() {
        super.onBindVu();
        thisActivity = this;
        mContext = getApplicationContext();

        bus.register(this);
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(false)
                .setSwipeRelateEnable(true);


        initPremission();

        initFragment();


    }

    @Subscribe(code = DRAWLAYOUT_ITEM_CLICK)
    public void navItemClick(Integer id) {
        switch (id) {
            case R.id.menu_nav_zhihu:
                startActivity(new Intent(thisActivity, ZhiHuActivity.class));
                break;
            case R.id.menu_nav_share:
                SystemUtils.share(thisActivity, thisActivity.getString(R.string.app_name), "分享");
                break;
        }
    }

    private void initFragment() {
        mFragmentManager = getSupportFragmentManager();
        gankAndroidFragment = new GankAndroidFragment();
        gankMeiZiFragment = new GankMeiZiFragment();
        mCurrentFragment = gankAndroidFragment;
        mFragmentManager.beginTransaction().add(R.id.fl_content_aty_main, mCurrentFragment, "android").commit();
        vu.setFragmentSwitchListener((containerId, pos) -> {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            Fragment fragment = null;
            switch (pos) {
                case 1://android
                    fragment = gankAndroidFragment;
                    break;
                case 2:
                    fragment = gankMeiZiFragment;
                    break;
            }

            if (mCurrentFragment != null) {
                fragmentTransaction.hide(mCurrentFragment);
            }
            if (fragment.isAdded()) {
                fragmentTransaction.show(fragment);
            } else {
                fragmentTransaction.add(containerId, fragment, "meizi");
            }
            fragmentTransaction.commit();
            mCurrentFragment = fragment;
        });
    }

    private void initPremission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .requestEach(Manifest.permission.CAMERA,
                        Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(permission -> { // will emit 2 Permission objects
                    if (permission.granted) {
                        // `permission.name` is granted !

                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // Denied permission without ask never again

                    } else {
                        // Denied permission with ask never again
                        // Need to go to the settings

                        AlertDialog dialog = new AlertDialog.Builder(thisActivity)
                                .setMessage(permission.name + "\n权限被禁止，请到 设置-权限 中给予")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", thisActivity.getPackageName(), null);
                                        intent.setData(uri);
                                        thisActivity.startActivity(intent);
                                    }
                                })
                                .create();
                        dialog.show();
                    }
                });
    }

    private long lastBackTime = 0;

    @Override
    public boolean handleBackPressed() {
        long thisBackTime = System.currentTimeMillis();
        if (thisBackTime - lastBackTime < 800) {
            return super.handleBackPressed();
        } else {
            ToastUtil.CustomToast(mContext, "再次点击，退出程序。");
        }
        lastBackTime = System.currentTimeMillis();
        return !super.handleBackPressed();
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

    @Override
    protected Class<MainVu> getVuClass() {
        return MainVu.class;
    }


    @Override
    protected void onDestroyVu() {
        mContext=null;
        bus.unRegister(this);
        bus.safeExit();
        super.onDestroyVu();
    }


}
