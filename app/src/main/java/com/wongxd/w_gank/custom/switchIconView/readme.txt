Usage

SwitchIconView extends from AppCompatImageView so you can set icon with app:srcCompat

Set any icon (vector or image) to SwitchIconView and enjoy switchable icon in your app :)

Use app:si_tint_color to set color to icon. Default color is black;

Use app:si_disabled_color to set color when icon disabled. Default color is equals with app:si_tint_color;

Use app:si_disabled_alpha to set alpha when icon disabled. Default alpha is .5;

Use app:si_no_dash if you don't want to draw dash, when icon disabled;

Use app:si_animation_duration if you want to change switching state animation duration;

Use app:si_enabled to set initial icon state;

Fully customized implementation:

    <com.github.zagum.switchicon.SwitchIconView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:padding="8dp"
        app:si_animation_duration="500"
        app:si_disabled_alpha=".3"
        app:si_disabled_color="#b7b7b7"
        app:si_tint_color="#ff3c00"
        app:si_enabled="false"
        app:si_no_dash="true"
        app:srcCompat="@drawable/ic_cloud"/>
Public methods:

  public void setIconEnabled(boolean enabled);

  public void setIconEnabled(boolean enabled, boolean animate);

  public boolean isIconEnabled();

  public void switchState();

  public void switchState(boolean animate);


  添加角标功能
  setNotificationNumber()

  circleBgColor 角标背景色
  textColor     角标字体颜色
  circleSize    角标大小