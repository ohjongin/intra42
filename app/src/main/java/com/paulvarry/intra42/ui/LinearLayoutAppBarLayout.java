package com.paulvarry.intra42.ui;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.LinearLayout;

public class LinearLayoutAppBarLayout extends LinearLayout {

    public LinearLayoutAppBarLayout(Context context) {
        super(context);
    }

    public LinearLayoutAppBarLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearLayoutAppBarLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override

    public WindowInsets onApplyWindowInsets(WindowInsets insets) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            insets = insets.replaceSystemWindowInsets(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(), insets.getSystemWindowInsetRight(), 0);
        }
        return super.onApplyWindowInsets(insets);
    }
}
