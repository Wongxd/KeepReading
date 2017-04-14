package com.wongxd.jueduimeizi;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wongxd.jueduimeizi.custom.QQSlideMenu.CoordinatorMenu;

public class MainActivity extends AppCompatActivity {

    private AppCompatActivity thisActivity;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thisActivity = this;
        mContext = this.getApplicationContext();

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .requestEach(Manifest.permission.CAMERA,
                        Manifest.permission.READ_PHONE_STATE)
                .subscribe(permission -> { // will emit 2 Permission objects
                    if (permission.granted) {
                        // `permission.name` is granted !

                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // Denied permission without ask never again

                    } else {
                        // Denied permission with ask never again
                        // Need to go to the settings

                    }
                });


        // 经测试在代码里直接声明透明状态栏更有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }


        CoordinatorMenu coordinatorMenu = (CoordinatorMenu) findViewById(R.id.contentPanel);
        coordinatorMenu.setOnDragStateChangeListener(new CoordinatorMenu.onDragStateChangeListener() {
            @Override
            public void onOpen() {

            }

            @Override
            public void onClose() {

            }

            @Override
            public void Draging(float fraction) {

            }
        });


    }


    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);

    }
}
