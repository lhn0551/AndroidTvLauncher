package com.zdkj.androidtvlauncher.utils;

import android.os.Environment;


import com.zdkj.androidtvlauncher.base.MyApp;

import java.io.File;

public class CachesUtil {
    public static String VIDEO = "video";

    /**
     * 获取媒体缓存文件
     *
     * @param child
     * @return
     */
    public static File getMediaCacheFile(String child) {
        String directoryPath = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 外部储存可用
            directoryPath = MyApp.getInstance().getExternalFilesDir(child).getAbsolutePath();
        } else {
            directoryPath = MyApp.getInstance().getFilesDir().getAbsolutePath() + File.separator + child;
        }
        File file = new File(directoryPath);
        //判断文件目录是否存在
        if (!file.exists()) {
            file.mkdirs();
        }
        LogUtils.d("getMediaCacheFile ====> " + directoryPath);
        return file;
    }
}
