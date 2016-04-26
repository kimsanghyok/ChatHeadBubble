package com.yj.chatheadbubblewidget.widget;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import com.yj.chatheadbubblewidget.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    /**
     * "아직 구현되지 않은 기능입니다."라는 Toast현시.
     *
     * @param p_context
     */
    public static void showNIToast(Context p_context) {
        Toast.makeText(p_context, R.string.msg_not_implemented_yet, Toast.LENGTH_LONG).show();
    }

    /**
     * 시간 문자열값 얻기.
     *
     * @param p_time 시간값.
     * @return 문자열표현.
     */
    public static String getTimeExpression(Date p_time) {
        SimpleDateFormat w_sdf = new SimpleDateFormat("aa hh:mm");
        return w_sdf.format(p_time);
    }

    /**
     * 날짜 문자열표현 얻기.
     *
     * @param p_time 시간값.
     * @return 문자열표현.
     */
    public static String getDateExpression(Date p_time) {
        Calendar w_cal = Calendar.getInstance();
        w_cal.setTime(p_time);
        int w_nDayOfWeek = w_cal.get(Calendar.DAY_OF_WEEK);
        String w_strDayOfWeek = "";
        switch (w_nDayOfWeek) {
            case 1:
                w_strDayOfWeek = "Sun";
                break;
            case 2:
                w_strDayOfWeek = "Mon";
                break;
            case 3:
                w_strDayOfWeek = "Tue";
                break;
            case 4:
                w_strDayOfWeek = "Thu";
                break;
            case 5:
                w_strDayOfWeek = "Wed";
                break;
            case 6:
                w_strDayOfWeek = "Fri";
                break;
            case 7:
                w_strDayOfWeek = "Sat";
                break;
        }

        SimpleDateFormat w_sdf = new SimpleDateFormat("yyyy / M / d ");
        return w_sdf.format(p_time) + w_strDayOfWeek;
    }

}
