package com.zdkj.androidtvlauncher.base;

import android.app.Application;

import com.zdkj.androidtvlauncher.utils.NetStateChangeReceiver;
import com.zdkj.androidtvlauncher.utils.PreferencesUtil;

import rxhttp.wrapper.param.RxHttp;


public class MyApp extends Application {
    private static MyApp instance;
    private static String AndroidId;

    public static MyApp getInstance() {

        return instance;
    }

    public static String getAndroidId() {

        return AndroidId;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyApp.instance = this;
        NetStateChangeReceiver.registerReceiver(this);
//        AndroidId=Settings.System.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        AndroidId = "65f61f5df110d25d";
        PreferencesUtil.getInstance().init(this);
        RxHttp.setDebug(true);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // 取消BroadcastReceiver注册
        NetStateChangeReceiver.unregisterReceiver(this);
    }

}
