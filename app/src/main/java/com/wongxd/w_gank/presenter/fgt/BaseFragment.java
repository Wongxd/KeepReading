package com.wongxd.w_gank.presenter.fgt;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;

/**
 * Created by wxd1 on 2017/2/12.
 */

public abstract class BaseFragment extends Fragment {

    protected View mContainView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Logger.d("RxFragment onCreate : " + getClass().getName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mContainView == null) {
            mContainView = inflater.inflate(returnLayoutID(), container, false);
        }
        return mContainView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        afterCreate(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("RxFragment onDestroy : " + getClass().getName());
    }

    protected abstract int returnLayoutID();

    protected abstract void afterCreate(Bundle savedInstanceState);

    public void showSnackbar(int stringRsId) {
        Snackbar.make(mContainView, stringRsId, Snackbar.LENGTH_SHORT).show();
    }

    public void showSnackbar(String string) {
        Snackbar.make(mContainView, string, Snackbar.LENGTH_SHORT).show();
    }
}
