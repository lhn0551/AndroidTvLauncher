package com.zdkj.androidtvlauncher.ui;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.danikula.videocache.HttpProxyCacheServer;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Player.EventListener;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.zdkj.androidtvlauncher.R;
import com.zdkj.androidtvlauncher.base.MyApp;
import com.zdkj.androidtvlauncher.models.VideoBean;
import com.zdkj.androidtvlauncher.msgs.AfterPlay;
import com.zdkj.androidtvlauncher.msgs.LiveChannel;
import com.zdkj.androidtvlauncher.utils.HttpProxyCacheUtil;
import com.zdkj.androidtvlauncher.utils.LogUtils;
import com.zdkj.androidtvlauncher.utils.NetStateChangeObserver;
import com.zdkj.androidtvlauncher.utils.NetStateChangeReceiver;
import com.zdkj.androidtvlauncher.utils.NetworkType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivityExo extends Fragment implements NetStateChangeObserver, EventListener {

    private ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
    @BindView(R.id.videoView)
    PlayerView videoView;
    private List<VideoBean.DataBean> videoList = new ArrayList<>();
    private MainViewModel mainViewModel;
    private SimpleExoPlayer player;
    private DataSource.Factory dataSourceFactory;
    private ConcatenatingMediaSource concatenatedSource;
    private LiveRunnable liveRunnable;

    public static MainActivityExo newInstance() {
        return new MainActivityExo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        ButterKnife.bind(this, view);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initPlayer();
        initData();
        return view;
    }

    private void initData() {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        if (NetUtils.isNetworkConnected(getActivity())) {
            mainViewModel.getPlayList().observe(getViewLifecycleOwner(), videoBean -> {
                videoList = videoBean.getData();
                playVideo();
            });
            mainViewModel.getAfterPlay(player.getCurrentTag() + "")
                    .observe(getViewLifecycleOwner(), afterPlayingBean -> {
                        if (afterPlayingBean.getData().getDelete().equals("1")) {
                            mainViewModel.updatePlayList();
                        }
                    });
        }
    }

    private void initPlayer() {
        liveRunnable = new LiveRunnable();
        DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter.Builder(getActivity()).build();
        dataSourceFactory = new DefaultDataSourceFactory(MyApp.getInstance(), BANDWIDTH_METER,
                new DefaultHttpDataSourceFactory(Util.getUserAgent(MyApp.getInstance(), "电视推广"), BANDWIDTH_METER));
        player = ExoPlayerFactory.newSimpleInstance(MyApp.getInstance());
        videoView.setUseController(false);
        videoView.setPlayer(player);
        player.addListener(new EventListener() {
            @Override
            public void onPlayerError(ExoPlaybackException error) {
                if (error.type == ExoPlaybackException.TYPE_SOURCE) {
                    playVideo();
                }
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            }

            @Override
            public void onPositionDiscontinuity(int reason) {
                if (reason == Player.DISCONTINUITY_REASON_PERIOD_TRANSITION) {
                    EventBus.getDefault().post(new AfterPlay());
                    LogUtils.e("正在播放=" + player.getCurrentTag());
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeChannel(LiveChannel channel) {
        LogUtils.e("hubkauke3");
        if (player != null) {
            HlsMediaSource source = new HlsMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(channel.getUrl()));
            player.prepare(source);
            player.setPlayWhenReady(true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        player.release();
        EventBus.getDefault().unregister(this);
        stopThreadPool();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void LiveAfterPlay(AfterPlay afterPlay) {
        mainViewModel.LiveAfterPlay(player.getCurrentTag() + "");
    }

    private void playVideo() {
        if (videoList.size() == 0) {
            return;
        }
        String videoName = videoList.get(0).getVideo_url();
        if (videoName.endsWith(".m3u8")) {
            HlsMediaSource mediaSource =
                    new HlsMediaSource.Factory(dataSourceFactory).setTag(videoList.get(0).getVideo_id())
                            .createMediaSource(Uri.parse(videoName));
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
            startThreadPool();
            DrawerActivity.isShowMu=true;
        } else {
            DrawerActivity.isShowMu=false;
            clearConcatenatedSource();
            stopThreadPool();
            HttpProxyCacheServer proxy = HttpProxyCacheUtil.getVideoProxy();
            for (int i = 0; i < videoList.size(); i++) {
                String url = proxy.getProxyUrl(videoList.get(i).getVideo_url());
                MediaSource mediaSource =
                        new ProgressiveMediaSource.Factory(dataSourceFactory).setTag(videoList.get(i).getVideo_id())
                                .createMediaSource(Uri.parse(url));
                concatenatedSource.addMediaSource(mediaSource);
            }
            LoopingMediaSource loopingSource = new LoopingMediaSource(concatenatedSource);
            player.prepare(loopingSource, true, true);
            player.setPlayWhenReady(true);
        }

    }

    private void clearConcatenatedSource() {
        if (concatenatedSource != null) {
            concatenatedSource.clear();
        } else {
            concatenatedSource = new ConcatenatingMediaSource();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        NetStateChangeReceiver.registerObserver(this);
    }

    /**
     * 更新列表
     */
    private void CheckNewListFromNet() {
        mainViewModel.updatePlayList();
    }


    @Override
    public void onNetDisconnected() {
        Log.e("WIFI", "NOWIFI");
//        NoInternetVideo();
    }

    @Override
    public void onNetConnected(NetworkType networkType) {
        Log.e("WIFI", "WIFIConnected");
        CheckNewListFromNet();
    }

    @Override
    public void onStop() {
        super.onStop();
        NetStateChangeReceiver.unregisterObserver(this);
    }

    private void startThreadPool() {
        if (exec == null) {
            exec = new ScheduledThreadPoolExecutor(1);
        }
        exec.scheduleAtFixedRate(liveRunnable, 1000, 15000, TimeUnit.MILLISECONDS);
    }

    private void stopThreadPool() {
        if (exec != null) {
            exec.shutdownNow();
            exec = null;
        }
    }

    class LiveRunnable implements Runnable {
        @Override
        public void run() {
            EventBus.getDefault().post(new AfterPlay());
        }
    }
}
