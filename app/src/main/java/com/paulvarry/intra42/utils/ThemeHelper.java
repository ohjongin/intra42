package com.paulvarry.intra42.utils;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.appbar.AppBarLayout;
import com.paulvarry.intra42.AppClass;
import com.paulvarry.intra42.R;
import com.paulvarry.intra42.api.model.Users;

public class ThemeHelper {

    public static void setTheme(AppClass app) {
        app.themeSettings = AppSettings.Theme.getEnumTheme(app, app.me);
        app.themeRes = ThemeHelper.getThemeResource(app.themeSettings);
        applyThemeBlitheness(app);
        app.setTheme(app.themeRes);
    }

    public static void setTheme(Activity activity, AppClass app) {
        applyThemeBlitheness(app);
        activity.setTheme(app.themeRes);
    }

    public static void setTheme(Activity activity, Users user) {
        if (activity == null)
            return;

        activity.setTheme(getThemeResource(AppSettings.Theme.getEnumTheme(activity, user)));
    }

    private static void applyThemeBlitheness(Context context) {
        AppSettings.Theme.EnumBrightness brightness = AppSettings.Theme.getBrightness(context);

        if (brightness == AppSettings.Theme.EnumBrightness.DARK)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else if (brightness == AppSettings.Theme.EnumBrightness.LIGHT)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

    public static void setActionBar(AppBarLayout appBarLayout, AppClass app) {
        setActionBar(appBarLayout, app.themeSettings);
    }

    public static void setActionBar(AppBarLayout appBarLayout, Users user) {
        setActionBar(appBarLayout, AppSettings.Theme.getEnumTheme(appBarLayout.getContext(), user));
    }

    public static void setActionBar(AppBarLayout appBarLayout, AppSettings.Theme.EnumTheme enumTheme) {
        if (appBarLayout == null)
            return;

        AppSettings.Theme.EnumTheme themeSettings = AppSettings.Theme.getEnumTheme(appBarLayout.getContext());
        if (themeSettings == AppSettings.Theme.EnumTheme.DEFAULT) {
            themeSettings = AppSettings.Theme.getEnumTheme(appBarLayout.getContext());
            if (themeSettings != AppSettings.Theme.EnumTheme.DEFAULT &&
                    themeSettings != AppSettings.Theme.EnumTheme.BLUE &&
                    themeSettings != AppSettings.Theme.EnumTheme.PURPLE &&
                    themeSettings != AppSettings.Theme.EnumTheme.GREEN &&
                    themeSettings != AppSettings.Theme.EnumTheme.RED)
                return;
        }

        ImageView imageView = appBarLayout.findViewById(R.id.imageViewActionBar);
        if (imageView == null)
            return;

        boolean enable = AppSettings.Theme.getActionBarBackgroundEnable(appBarLayout.getContext());

        if (enumTheme == null || !enable) {
            imageView.setVisibility(View.GONE);
            return;
        }

        imageView.setVisibility(View.VISIBLE);
        switch (enumTheme) {
            case RED:
                imageView.setImageResource(R.drawable.order_background);
                break;
            case PURPLE:
                imageView.setImageResource(R.drawable.assembly_background);
                break;
            case GREEN:
                imageView.setImageResource(R.drawable.alliance_background);
                break;
            case BLUE:
                imageView.setImageResource(R.drawable.federation_background);
                break;
            default:
                imageView.setVisibility(View.GONE);
        }
    }

    @StyleRes
    public static int getThemeResource(AppSettings.Theme.EnumTheme theme) {
        int themeRes;

        switch (theme) {
            case RED:
                themeRes = R.style.ThemeIntraOrder;
                break;
            case PURPLE:
                themeRes = R.style.ThemeIntraAssembly;
                break;
            case BLUE:
                themeRes = R.style.ThemeIntraFederation;
                break;
            case GREEN:
                themeRes = R.style.ThemeIntraAlliance;
                break;
            default:
                themeRes = R.style.ThemeIntra;
                break;
        }

        return themeRes;
    }

    public static int getColorPrimary(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    public static int getColorAccent(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        return typedValue.data;
    }
}

