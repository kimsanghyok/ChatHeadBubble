package com.yj.chatheadbubblewidgetdemo;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

/**
 * Created by toltori on 4/25/16.
 */
public class Utils {
    public static final String LogTag = "ChatHeadDemo";

    public static boolean canDrawOverlays(Context context){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }else{
            return Settings.canDrawOverlays(context);
        }
    }
}
