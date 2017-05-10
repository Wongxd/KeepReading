package com.wongxd.w_gank.seePhoto;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by wxd1 on 2017/5/5.
 */

public class photoDialog extends Dialog {
    public photoDialog(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.setContentView(mView);
        Window window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = 0;
        wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wl.width = WindowManager.LayoutParams.MATCH_PARENT; //屏幕宽高的属性由DisplayMetrics类获得
        wl.gravity = Gravity.CENTER;
        window.setAttributes(wl);
    }


}
