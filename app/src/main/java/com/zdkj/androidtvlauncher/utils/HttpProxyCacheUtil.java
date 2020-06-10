package com.zdkj.androidtvlauncher.utils;

import com.danikula.videocache.HttpProxyCacheServer;
import com.zdkj.androidtvlauncher.base.MyApp;

public class HttpProxyCacheUtil {
    private static HttpProxyCacheServer videoProxy;

    public static HttpProxyCacheServer getVideoProxy() {
        if (videoProxy == null) {
            videoProxy = new HttpProxyCacheServer.Builder(MyApp.getInstance().getApplicationContext())
                    .cacheDirectory(CachesUtil.getMediaCacheFile(CachesUtil.VIDEO))
                    .maxCacheFilesCount(20)
                    .fileNameGenerator(new CacheFileNameGenerator())
                    .build();
        }
        return videoProxy;
    }
}
