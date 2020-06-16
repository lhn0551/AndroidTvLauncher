package com.zdkj.androidtvlauncher.ui;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zdkj.androidtvlauncher.api.AppNetWork;
import com.zdkj.androidtvlauncher.models.AfterPlayingBean;
import com.zdkj.androidtvlauncher.models.ImageBean;
import com.zdkj.androidtvlauncher.models.LiveSourceBean;
import com.zdkj.androidtvlauncher.models.TextBean;
import com.zdkj.androidtvlauncher.models.VideoBean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import rxhttp.wrapper.param.RxHttp;

import static com.zdkj.androidtvlauncher.base.MyApp.getAndroidId;


public class MainViewModel extends ViewModel {
    private MutableLiveData<VideoBean> videoLiveData = new MutableLiveData<>();
    private MutableLiveData<AfterPlayingBean> afterPlayLiveData = new MutableLiveData<>();
    private MutableLiveData<LiveSourceBean> liveData = new MutableLiveData<>();
    private MutableLiveData<String> liveSource = new MutableLiveData<>();
    private MutableLiveData<ImageBean> imageListData = new MutableLiveData<>();
    private MutableLiveData<TextBean> TextListData = new MutableLiveData<>();

    LiveData<TextBean> getTextList() {
        //获取数据
        loadTextList();
        return TextListData;
    }

    LiveData<VideoBean> getPlayList() {
        //获取数据
        loadVideoList();
        return videoLiveData;
    }

    LiveData<ImageBean> getImageList() {
        //获取数据
        loadImageList();
        return imageListData;
    }

    public void updateImageList() {
        loadImageList();
    }

    public void updateTextList() {
        loadTextList();
    }

    @SuppressLint("CheckResult")
    private void loadTextList() {
        RxHttp.get(AppNetWork.TEXTLIST + getAndroidId())
                .asClass(TextBean.class)
                .observeOn(AndroidSchedulers.mainThread())   //控制下游在主线程执行
                .doOnSubscribe(disposable -> {
                })
                .doFinally(() -> {
                })
                .subscribe(s -> {
                    if (s.getCode() == 200) {
                        TextListData.postValue(s);
                    }
                }, throwable -> {
                });
    }

    @SuppressLint("CheckResult")
    private void loadImageList() {
        RxHttp.get(AppNetWork.IMAGELIST + getAndroidId())
                .asClass(ImageBean.class)
                .observeOn(AndroidSchedulers.mainThread())   //控制下游在主线程执行
                .doOnSubscribe(disposable -> {
                })
                .doFinally(() -> {
                })
                .subscribe(s -> {
                    if (s.getCode() == 200) {
                        imageListData.postValue(s);
                    }
                }, throwable -> {
                });
    }

    LiveData<LiveSourceBean> getLiveList() {
        //获取数据
        getLiveSource();
        return liveData;
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

    @SuppressLint("CheckResult")
    private void getLiveSource() {
        RxHttp.postForm(AppNetWork.LIVELIST)
                .add("key", "zhengdiankeji")
                .asClass(LiveSourceBean.class)
                .observeOn(AndroidSchedulers.mainThread())   //控制下游在主线程执行
                .doOnSubscribe(disposable -> {
                })
                .doFinally(() -> {
                })
                .subscribe(s -> {
                    if (s.getCode() == 200) {
                        liveData.postValue(s);
                    }
                }, throwable -> {
                });

    }

    public void updatePlayList() {
        loadVideoList();
    }

    public void LiveAfterPlay(String video_id) {
        afterPlay(video_id);
    }
}
