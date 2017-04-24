package com.wongxd.w_gank.presenter.aty;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.wongxd.w_gank.R;
import com.wongxd.w_gank.base.BaseSwipeActivity;
import com.wongxd.w_gank.custom.pinchImageview.PinchImageView;
import com.wongxd.w_gank.utils.ImageSave;
import com.wongxd.w_gank.utils.SystemBarHelper;
import com.wongxd.w_gank.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoActivity extends BaseSwipeActivity implements View.OnClickListener {

    private static final String TAG = "PhotoActivity";
    public static final String URL = "URL";


    @BindView(R.id.meizi_image)
    PinchImageView meiziImage;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.btn_save)
    ImageView btnSave;
    @BindView(R.id.pb_loadimg_aty_photo)
    ProgressBar pbLoadimgAtyPhoto;

    private String mUrl;
    private Bitmap mBitmap;
    private Context mContext;
    private AppCompatActivity thisActivity;

    public static void startActivity(AppCompatActivity activity, String url, View transitionView) {
        Intent intent = new Intent(activity, PhotoActivity.class);
        intent.putExtra(URL, url);

        // 这里指定了共享的视图元素
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, transitionView,
                        activity.getResources().getString(R.string.gank_meizi_transitions_name));

        ActivityCompat.startActivity(activity, intent, options.toBundle());

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_photo);
        ButterKnife.bind(this);
        thisActivity = this;
        mContext = this.getApplicationContext();

        SystemBarHelper.immersiveStatusBar(this);

        btnBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        Intent intent = getIntent();
        mUrl = intent.getStringExtra(URL);
        Glide.with(this)
                .load(mUrl)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        pbLoadimgAtyPhoto.setVisibility(View.GONE);
                        meiziImage.setImageBitmap(resource);
                        mBitmap = resource;
                    }
                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_save:
                ImageSave.with(getApplicationContext())
                        .save(mBitmap)
                        .setImageSaveListener(new ImageSave.ImageSaveListener() {
                            @Override
                            public void onSuccess() {
                                ToastUtil.Toast(mContext, "保存成功");
                            }

                            @Override
                            public void onError() {
                                ToastUtil.Toast(mContext, "保存失败");
                            }
                        });
                break;
        }

    }

}
