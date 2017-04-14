package com.wongxd.jueduimeizi.utils;

/**
 * Created by wxd1 on 2017/2/22.
 */

import android.content.Context;
import android.view.WindowManager;

public class DensityUtil {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 获取屏幕的宽和高
     *
     * @param c 上下文
     * @return 包含宽和高的数组 0 为宽  1 为高
     */
    public static int[] getScreenWidthAndHeight(Context c) {
        WindowManager wm = (WindowManager) c
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        int[] screen = new int[2];
        screen[0] = width;
        screen[1] = height;
        return screen;
    }
}