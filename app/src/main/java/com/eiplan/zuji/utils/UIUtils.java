package com.eiplan.zuji.utils;

import android.content.Context;

import com.eiplan.zuji.MyApplication;

public class UIUtils {

    public static Context getContext(){
        return MyApplication.context;
    }

    //返回指定colorId对应的颜色值
    public static int getColor(int colorId){
        return getContext().getResources().getColor(colorId);
    }

    //dp转px
    public static int dp2px(int dp){
        //获取手机密度
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (density * dp +0.5);
    }

    //px转dp
    public static int px2dp(int px){
        //获取手机密度
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5);

    }
}
