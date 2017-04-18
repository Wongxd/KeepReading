package com.wongxd.w_gank.base;


import android.support.v7.app.AppCompatActivity;

/**
 * basepresenteractivity的父类，解耦预留
 */
public class BaseActivity extends AppCompatActivity {

//    private LinearLayout rootLayout;
//    private LinearLayout ll_fillToStatuBar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // 这句很关键，注意是调用父类的方法
//        super.setContentView(R.layout.activity_base);
//        // 经测试在代码里直接声明透明状态栏更有效
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
//            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
//        }
//    }
//
//    @Override
//    public void setContentView(int layoutId) {
//        setContentView(View.inflate(this, layoutId, null));
//    }
//
//    @Override
//    public void setContentView(View view) {
//        rootLayout = (LinearLayout) findViewById(R.id.root_layout);
//        ll_fillToStatuBar = (LinearLayout) rootLayout.findViewById(R.id.ll_fillToStatuBar);
//        if (rootLayout == null) return;
//        rootLayout.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//    }
//
//    protected void setStatuBarColor(@ColorRes int res) {
//        ll_fillToStatuBar.setBackground(new ColorDrawable(getResources().getColor(res)));
//        }
}