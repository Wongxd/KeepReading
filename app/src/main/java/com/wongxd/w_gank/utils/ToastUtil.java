package com.wongxd.w_gank.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wongxd.w_gank.R;

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
        CustomToast(c, c.getString(msgRes));
    }


    //  #######################################自定义布局的Toast#############################

    private static String oldMsg;
    private static long firstShowTime;
    private static long lastShowTime;
    private static TextView tv;

    public static void CustomToast(Context context, String s) {

        if (toast == null) {

            toast = new Toast(context.getApplicationContext());

            View v = View.inflate(context.getApplicationContext(), R.layout.toast_layout, null);
            tv = (TextView) v.findViewById(R.id.tv_toast);
            tv.setText(s);
            tv.setTextColor(Color.WHITE);

            toast.setView(v);

            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            firstShowTime = System.currentTimeMillis();
            oldMsg = s;
        } else {
            lastShowTime = System.currentTimeMillis();
            if (oldMsg.equals(s) && lastShowTime - firstShowTime < Toast.LENGTH_SHORT) {
                toast.show();
            } else {
                tv.setText(s);
                tv.setTextColor(Color.WHITE);

                toast.show();
                oldMsg = s;
            }
            firstShowTime = lastShowTime;
        }


    }

    public static void CustomToast(Context context, int res) {
        CustomToast(context, context.getString(res));
    }


}
