package com.wongxd.w_gank.presenter.aty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.wongxd.w_gank.R;
import com.wongxd.w_gank.base.BaseSwipeActivity;
import com.wongxd.w_gank.model.GankBean;
import com.wongxd.w_gank.utils.SystemBarHelper;
import com.wongxd.w_gank.utils.SystemUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GankWebActivity extends BaseSwipeActivity {

    private static final String TAG = "GankWebActivity";

    public static final String DATA_ID = "data_id";
    public static final String DATA_TITLE = "data_title";
    public static final String DATA_URL = "data_url";
    public static final String DATA_TYPE = "data_type";
    public static final String DATA_WHO = "data_who";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;

    private String mId;
    private String mTitle;
    private String mURl;
    private String mType;
    private String mWho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);

        SystemBarHelper.tintStatusBar(this, getResources().getColor(R.color.colorPrimary));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getArguments();

        initWebView();
        webView.loadUrl(mURl);
    }

    void getArguments() {
        Intent intent = getIntent();
        mId = intent.getStringExtra(DATA_ID);
        mTitle = intent.getStringExtra(DATA_TITLE);
        mType = intent.getStringExtra(DATA_TYPE);
        mURl = intent.getStringExtra(DATA_URL);
        mWho = intent.getStringExtra(DATA_WHO);

    }

    public static void startWebActivity(Activity a, GankBean.ResultsBean result) {
        Intent intent = new Intent(a, GankWebActivity.class);
        intent.putExtra(DATA_ID, result.get_id());
        intent.putExtra(DATA_TITLE, result.getDesc());
        intent.putExtra(DATA_TYPE, result.getType());
        intent.putExtra(DATA_URL, result.getUrl());
        intent.putExtra(DATA_WHO, result.getWho());
        a.startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_copy_the_url:
                SystemUtils.copyText(this, mTitle + "---" + mURl);
                break;
            case R.id.action_open_in_browser:
                SystemUtils.openUrlByBrowser(this, mURl);
                break;
            case R.id.action_share:
                SystemUtils.share(this, mURl, mTitle);
                break;
            case android.R.id.home:
                if (webView != null && webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void initWebView() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(android.webkit.WebView view, int newProgress) {
                if (newProgress == 100) {
                    // 网页加载完成
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                        progressBar.setProgress(15);
                    }
                } else {
                    // 加载中
                    if (progressBar != null) {
                        progressBar.setVisibility(View.VISIBLE);
                        if (newProgress < 15) newProgress = 15;
                        progressBar.setProgress(newProgress);
                    }
                }
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDisplayZoomControls(true);
    }
}
