package com.wongxd.w_gank.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by wxd1 on 2017/2/18.
 */

public class SPUtil {
    private static volatile SPUtil spUtilInstance;
    private SharedPreferences sp;

    public SPUtil(Context c, String name) {
      this.sp=c.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static SPUtil getInstance(Context c) {

        if (spUtilInstance == null) {
            synchronized (SPUtil.class) {
                if (spUtilInstance == null) {
                    spUtilInstance = new SPUtil(c,"CarInsurance");
                }
            }
        }
        return spUtilInstance;
    }


    public void saveInfo(String key, String info) {
        sp.edit().putString(key, info).apply();
    }

    public String getInfo(String key, String defValue) {
       return   sp.getString(key, defValue);
    }
}
