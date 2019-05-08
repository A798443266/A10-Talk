package com.eiplan.zuji;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.support.multidex.MultiDex;

import com.eiplan.zuji.model.Model;
import com.eiplan.zuji.single.CallManager;
import com.eiplan.zuji.single.CallReceiver;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import com.mob.MobSDK;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import cn.smssdk.SMSSDK;
import okhttp3.OkHttpClient;

public class MyApplication extends Application {
    public static Context context;
    private CallReceiver callReceiver;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        init();
    }

    private void init() {
        MultiDex.install(this);


        //初始化短信验证功能
        MobSDK.init(context);



        //初始化EaseUI
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(false);// 设置需要同意才能接受邀请
        options.setAutoAcceptGroupInvitation(false);// 设置需要同意才能接受群邀请
        EaseUI.getInstance().init(context,options);

        //初始化Model
        Model.getInstance().init(context);


        //初始化科大讯飞
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=5c3c4375");

        //okhttpUtils配置
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(5000L, TimeUnit.MILLISECONDS)
                .readTimeout(5000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);

        //初始化友盟sdk
        UMConfigure.init(this,"5c3ee366b465f51d37000d9e"
                ,"umeng",UMConfigure.DEVICE_TYPE_PHONE,"");

        //设置每次都打开调用面板
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(this).setShareConfig(config);

        UMConfigure.setLogEnabled(true);
        //        设置各个平台的appkey：
        PlatformConfig.setWeixin("wx964bf50b4cc71f08", "2730ec0e0aff154064d76666851b6ad2");
        PlatformConfig.setQQZone("101546670","1444e55f13388b461941b96ad0683c70");



      /*  // 设置通话广播监听器
        IntentFilter callFilter = new IntentFilter(EMClient.getInstance()
                .callManager()
                .getIncomingCallBroadcastAction());
        if (callReceiver == null) {
            callReceiver = new CallReceiver();
        }
        //注册通话广播接收者
        context.registerReceiver(callReceiver, callFilter);
        CallManager.getInstance().setExternalInputData(false);
        // 通话管理类的初始化
        CallManager.getInstance().init(context);*/


//        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
//        registerReceiver(new com.hyphenate.easeui.ui.CallReceiver(), callFilter);
    }

    //获取全局上下文对象
    public static Context getGlobalContext(){
        return context;
    }
}
