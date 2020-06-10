package com.zdkj.androidtvlauncher.base;

import android.app.Application;
import android.provider.Settings;


import com.zdkj.androidtvlauncher.utils.NetStateChangeReceiver;
import com.zdkj.androidtvlauncher.utils.PreferencesUtil;

import rxhttp.wrapper.param.RxHttp;


public class MyApp extends Application {
    private static MyApp instance;
    private static String AndroidId;

    public static MyApp getInstance() {

        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        MyApp.instance = this;
        NetStateChangeReceiver.registerReceiver(this);
        AndroidId=Settings.System.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        PreferencesUtil.getInstance().init(this);
        RxHttp.setDebug(true);
    }

    public static String getAndroidId() {

        return AndroidId;
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        // 取消BroadcastReceiver注册
        NetStateChangeReceiver.unregisterReceiver(this);
    }

}
