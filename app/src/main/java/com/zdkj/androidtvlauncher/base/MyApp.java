package com.zdkj.androidtvlauncher.base;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;

import com.danikula.videocache.HttpProxyCacheServer;
import com.tencent.bugly.Bugly;
import com.zdkj.androidtvlauncher.utils.NetStateChangeReceiver;
import com.zdkj.androidtvlauncher.utils.PreferencesUtil;

import rxhttp.wrapper.param.RxHttp;


public class MyApp extends Application {
    private static MyApp instance;
    private static String AndroidId;
    private HttpProxyCacheServer proxy;
    public static MyApp getInstance() {

        return instance;
    }

    public static String getAndroidId() {

        return AndroidId;
    }
    public static HttpProxyCacheServer getProxy(Context context) {
        MyApp app = (MyApp) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(this)
                .maxCacheSize(1024 * 1024 * 1024)
                .maxCacheFilesCount(20)// 1 Gb for cache
                .build();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        MyApp.instance = this;
        NetStateChangeReceiver.registerReceiver(this);
        AndroidId= Settings.System.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//        AndroidId = "65f61f5df110d25d";
        PreferencesUtil.getInstance().init(this);
        RxHttp.setDebug(true);
        Bugly.init(getApplicationContext(), "618e80e891", false);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // 取消BroadcastReceiver注册
        NetStateChangeReceiver.unregisterReceiver(this);
    }

}
