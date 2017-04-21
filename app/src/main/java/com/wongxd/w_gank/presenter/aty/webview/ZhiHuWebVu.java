package com.wongxd.w_gank.presenter.aty.webview;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wongxd.w_gank.R;
import com.wongxd.w_gank.base.Vu;
import com.wongxd.w_gank.base.rx.RxBus;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by wxd1 on 2017/4/7.
 */
public class ZhiHuWebVu implements Vu {

    private View view;
    private WebView webview;
    private Toolbar toolbar;
    private ProgressBar pbLoadurl;
    private ImageView ivTitle;
    private TextView tvTitle;
    private TextView tvImgSource;
    private ImageButton ibShare;

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setTitle(String s) {
        tvTitle.setText(s);
    }

    public void setImgSource(String s) {
        tvImgSource.setText(s);
    }

    public void loadUrl(String url) {
        webview.loadUrl(url);
    }

    public void loadDataWithBaseUrl(String data) {
        webview.loadDataWithBaseURL("file:///android_asset/", data, "text/html", "UTF-8", null);
    }

    public ImageView getImagView() {
        return ivTitle;
    }


    public ImageButton getIbShare() {
        return ibShare;
    }

    @Override
    public void init(LayoutInflater inflater, ViewGroup container, RxBus rxBus, CompositeDisposable compositeDisposable) {
        view = inflater.inflate(R.layout.aty_zhihu_webview, container);
        webview = (WebView) view.findViewById(R.id.webview);

        ibShare = (ImageButton) view.findViewById(R.id.ib_share);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        pbLoadurl = (ProgressBar) view.findViewById(R.id.pb_loadurl);
        ivTitle = (ImageView) view.findViewById(R.id.iv_aty_webview);
        tvTitle = (TextView) view.findViewById(R.id.tv_img_title);
        tvImgSource = (TextView) view.findViewById(R.id.tv_img_source);
        toolbar.setTitleTextColor(Color.WHITE);
        pbLoadurl.setVisibility(View.VISIBLE);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.setVerticalScrollBarEnabled(true);
        webview.setHorizontalScrollBarEnabled(false);
        webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    pbLoadurl.setVisibility(View.GONE);
                } else {

                    if (newProgress < 20) newProgress = 20;
                    pbLoadurl.setProgress(newProgress);
                }
            }
        });
    }

    @Override
    public View getView() {
        return view;
    }
}
