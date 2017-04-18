package com.wongxd.w_gank.utils.glide;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wongxd.w_gank.R;


/**
 * Created by wxd1 on 2017/3/13.
 */

public class GlideLoader {

    public static void LoadAsCircleImage(Context c, Object path, ImageView iv, int width, int height) {

        Glide.with(c)
                .load(path)
                .override(width,height)
                .transform(new GlideCircleTransform(c))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(iv);
    }

    public static void LoadAsCircleImage(Context c, Object path, ImageView iv) {

        Glide.with(c)
                .load(path)
                .transform(new GlideCircleTransform(c))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(iv);
    }



    public static void LoadAsRoundImage(Context c, Object path, ImageView iv, int width, int height) {

        Glide.with(c)
                .load(path)
                .override(width,height)
                .transform(new GlideRoundTransform(c))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(iv);
    }

    public static void LoadAsRoundImage(Context c, Object path, ImageView iv) {

        Glide.with(c)
                .load(path)
                .transform(new GlideRoundTransform(c))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(iv);
    }

}
