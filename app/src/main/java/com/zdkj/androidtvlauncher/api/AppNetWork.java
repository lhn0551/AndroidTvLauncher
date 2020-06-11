package com.zdkj.androidtvlauncher.api;

/**
 * 提供当前应用访问服务器的请求地址
 */
public class AppNetWork {

    //提供web应用的地址
    public static final String BASE_URL = "http://ad.zadtek.com/";
    public static final String ISBIND = BASE_URL + "bind/is_bind?key=";//是否绑定接口
    public static final String DOWNLOADVIDEO = BASE_URL + "video/video_list?key=";//视频下载列表接口
    public static final String DOWNLOADFINISHED = BASE_URL + "video/download_finish?key=";//下载完成通知后台接口
    public static final String AFTERFINISHPLAYING = BASE_URL + "video/play_finish?key=";//播放完视频分成接口
    public static final String LIVELIST = BASE_URL + "video/live_broadcast";//播放完视频分成接口


}
