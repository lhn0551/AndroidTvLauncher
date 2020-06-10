package com.zdkj.androidtvlauncher.ui;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.zdkj.androidtvlauncher.api.AppNetWork;
import com.zdkj.androidtvlauncher.models.AfterPlayingBean;
import com.zdkj.androidtvlauncher.models.VideoBean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import rxhttp.wrapper.param.RxHttp;

import static com.zdkj.androidtvlauncher.base.MyApp.getAndroidId;


public class MainViewModel extends ViewModel {
    private MutableLiveData<VideoBean> videoLiveData = new MutableLiveData<>();
    private MutableLiveData<AfterPlayingBean> afterPlayLiveData = new MutableLiveData<>();

    LiveData<VideoBean> getPlayList() {
        //获取数据
        loadVideoList();
        return videoLiveData;
    }

    LiveData<AfterPlayingBean> getAfterPlay(String video_id) {
        afterPlay(video_id);
        return afterPlayLiveData;
    }


    @SuppressLint("CheckResult")
    private void loadVideoList() {

        RxHttp.get(AppNetWork.DOWNLOADVIDEO + getAndroidId())
                .asClass(VideoBean.class)
                .observeOn(AndroidSchedulers.mainThread())   //控制下游在主线程执行
                .doOnSubscribe(disposable -> {
                })
                .doFinally(() -> {
                })
                .subscribe(s -> {
                    if (s.getCode() == 200) {
                        videoLiveData.postValue(s);
                    }
                }, throwable -> {
                });

    }
    @SuppressLint("CheckResult")
    private void afterPlay(String video_id) {
        RxHttp.get(AppNetWork.AFTERFINISHPLAYING + getAndroidId() + "&video_id=" + video_id)
                .asClass(AfterPlayingBean.class)
                .observeOn(AndroidSchedulers.mainThread())   //控制下游在主线程执行
                .doOnSubscribe(disposable -> {
                })
                .doFinally(() -> {
                })
                .subscribe(s -> {
                    if (s.getCode() == 200) {
                        afterPlayLiveData.postValue(s);
                    }
                }, throwable -> {
                });

    }
    public void updatePlayList(){
        loadVideoList();
    }
}