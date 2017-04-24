package com.wongxd.w_gank.presenter.aty.webview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.wongxd.w_gank.R;
import com.wongxd.w_gank.base.BasePresenterActivity;
import com.wongxd.w_gank.base.aCache.AcacheUtil;
import com.wongxd.w_gank.net.NetClient;
import com.wongxd.w_gank.net.ZhiHuNetService;
import com.wongxd.w_gank.utils.NetworkAvailableUtils;
import com.wongxd.w_gank.utils.SystemBarHelper;
import com.wongxd.w_gank.utils.ToastUtil;

import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ZhiHuWebviewActivity extends BasePresenterActivity<ZhiHuWebVu> {

    private AppCompatActivity thisActivity;
    private Context mContext;
    private String share_url;

    @Override
    protected void onBindVu() {
        super.onBindVu();
        thisActivity = this;
        mContext = this.getApplicationContext();
        SystemBarHelper.tintStatusBar(this, getResources().getColor(R.color.colorPrimary));

        int id = getIntent().getIntExtra("id", 0);
        String title = getIntent().getStringExtra("title");
        if (null == title || TextUtils.isEmpty(title)) {
            title = "知乎新闻";
        }

        vu.setTitle(title);


        if (!NetworkAvailableUtils.isNetworkAvailable(mContext)) {
            String s = AcacheUtil.getDefault(mContext, AcacheUtil.StringCache).getAsString(id + "");
            if (s == null || TextUtils.isEmpty(s)) {
                ToastUtil.Toast(mContext, "没有相关缓存");
            } else {
                ToastUtil.Toast(mContext, "无网络，已读取缓存");
            }

            vu.loadDataWithBaseUrl(parseString(s));
            Glide.with(mContext)
                    .load(headerImage)
                    .centerCrop()
                    .into(vu.getImagView());
            vu.setImgSource(image_source);


        } else {

            ZhiHuNetService zhiHuNetService = NetClient.getZhihuService();

            zhiHuNetService.getZhiHuDetail(id)
                    .subscribeOn(Schedulers.io())
                    .map(responseBody -> {
                        String info = responseBody.string();
                        //缓存
                        AcacheUtil.getDefault(mContext, AcacheUtil.StringCache)
                                .put(id + "", info);
                        return parseString(info);
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposableList.add(d);
                        }

                        @Override
                        public void onNext(String s) {
                            Glide.with(mContext)
                                    .load(headerImage)
                                    .centerCrop()
                                    .into(vu.getImagView());
                            vu.setImgSource(image_source);
                            vu.loadDataWithBaseUrl(s);

                            vu.getIbShare().setOnClickListener(v -> {
                                Intent intent1 = new Intent(Intent.ACTION_SEND);
                                intent1.putExtra(Intent.EXTRA_TEXT, share_url);
                                intent1.setType("text/plain");
                                startActivity(Intent.createChooser(intent1, "share from " + getString(R.string.app_name)));
                            });

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }

    }

    private String headerImage;
    private String image_source;


    private String parseString(String json) {
        try {
            JSONObject newsContent = new JSONObject(json);
            long id = newsContent.optLong("id");
            String body = newsContent.optString("body");
            String title = newsContent.optString("title");
            image_source = newsContent.optString("image_source");
            share_url = newsContent.optString("share_url");
            String image = "";
            if (newsContent.has("images")) {
                image = newsContent.getString("image");
            }


            if (image.equals("")) {
                headerImage = "file:///android_asset/news_detail_header_image.jpg";
            } else {
                headerImage = image;
            }

            String head = "<head>\n" +
                    "\t<link rel=\"stylesheet\" href=\"" + "news_content_style.css" + "\"/>\n" +
                    "</head>";
            String img = "<div class=\"headline\">";
            String html = head + body.replace(img, " ");
            return html;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    @Override
    protected Class<ZhiHuWebVu> getVuClass() {
        return ZhiHuWebVu.class;
    }
}
