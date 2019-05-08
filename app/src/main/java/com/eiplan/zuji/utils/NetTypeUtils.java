package com.eiplan.zuji.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * 判断网络类型的工具类
 */

public class NetTypeUtils {
    public static int getNetType(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return 0;  //没有网
        }
        if(activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) { //当前网络状态是wifi
            return 1;
        }else if(activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE){//移动网络
            return 2;
        }
        return 0;
    }
}
