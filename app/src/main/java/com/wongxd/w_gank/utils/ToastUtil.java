package com.wongxd.w_gank.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by wxd1 on 2017/2/17.
 */

public class ToastUtil {
    private static volatile Toast toast;

    public static void Toast(Context c, String msg) {
        if (toast == null) {
            synchronized (ToastUtil.class) {
                if (toast == null) {
                    toast = Toast.makeText(c, msg, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        } else {
            toast.setText(msg);
            toast.show();
        }
    }

    public static void Toast(Context c, int msgRes) {
        Toast(c, c.getString(msgRes));
    }
}
