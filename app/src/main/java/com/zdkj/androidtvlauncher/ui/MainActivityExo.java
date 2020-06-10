package com.zdkj.androidtvlauncher.ui;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.danikula.videocache.HttpProxyCacheServer;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
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
import com.zdkj.androidtvlauncher.base.BaseActivity;
import com.zdkj.androidtvlauncher.models.VideoBean;
import com.zdkj.androidtvlauncher.utils.HttpProxyCacheUtil;
import com.zdkj.androidtvlauncher.utils.LogUtils;
import com.zdkj.androidtvlauncher.utils.NetStateChangeObserver;
import com.zdkj.androidtvlauncher.utils.NetStateChangeReceiver;
import com.zdkj.androidtvlauncher.utils.NetworkType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivityExo extends BaseActivity implements NetStateChangeObserver {

    @BindView(R.id.videoView)
    PlayerView videoView;

    @BindView(R.id.loadingView)
    LinearLayout loadingView;
    @BindView(R.id.tvState)
    TextView tvState;
    private List<VideoBean.DataBean> videoList = new ArrayList<>();
    private MainViewModel mainViewModel;
    private SimpleExoPlayer player;
    private DataSource.Factory dataSourceFactory;
    private ConcatenatingMediaSource concatenatedSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initPlayer();

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        if (NetUtils.isNetworkConnected(this)) {
            mainViewModel.getPlayList().observe(this, videoBean -> {
                videoList = videoBean.getData();
                playVideo();
            });
        } else {
        }


    }

    private void initPlayer() {
        player = ExoPlayerFactory.newSimpleInstance(this);
        videoView.setUseController(false);
        videoView.setPlayer(player);
        DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter.Builder(this).build();
        dataSourceFactory = new DefaultDataSourceFactory(this, BANDWIDTH_METER,
                new DefaultHttpDataSourceFactory(Util.getUserAgent(this, "电视推广"), BANDWIDTH_METER));

//        dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "电视推广"));
        player.addListener(new Player.EventListener() {
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
                LogUtils.e("INDEX" + player.getCurrentWindowIndex());
                LogUtils.e("Period" + player.getCurrentPeriodIndex());
                switch (reason) {
                    case Player.DISCONTINUITY_REASON_PERIOD_TRANSITION:
                        AfterPlay();
                        LogUtils.e("正在播放=" + player.getCurrentTag());
                        break;
                }
            }
        });
    }

    private void AfterPlay() {
        mainViewModel.getAfterPlay(player.getCurrentTag() + "")
                .observe(this, afterPlayingBean -> {
                    if (afterPlayingBean.getData().getDelete().equals("1")) {
                        mainViewModel.updatePlayList();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }

    private void playVideo() {
//        if (videoList.size() == 0) {
//            return;
//        }
        if (concatenatedSource != null) {
            concatenatedSource.clear();
        } else {
            concatenatedSource = new ConcatenatingMediaSource();
        }
        HttpProxyCacheServer proxy = HttpProxyCacheUtil.getVideoProxy();
        String url;
        for (int i = 0; i < videoList.size(); i++) {
            String videoName=videoList.get(i).getVideo_url();
            if (videoName.endsWith(".m3u8")){
                url=videoName;
                HlsMediaSource mediaSource =
                        new HlsMediaSource.Factory(dataSourceFactory).setTag(videoList.get(i).getVideo_id())
                                .createMediaSource(Uri.parse(url));
                concatenatedSource.addMediaSource(mediaSource);
            }else {
                url = proxy.getProxyUrl(videoName);
                MediaSource mediaSource =
                        new ProgressiveMediaSource.Factory(dataSourceFactory).setTag(videoList.get(i).getVideo_id())
                                .createMediaSource(Uri.parse(url));
                concatenatedSource.addMediaSource(mediaSource);
            }


        }
        LoopingMediaSource loopingSource = new LoopingMediaSource(concatenatedSource);
        player.prepare(loopingSource, true, true);
        player.setPlayWhenReady(true);

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
    protected void onResume() {
        super.onResume();
        NetStateChangeReceiver.registerObserver(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        NetStateChangeReceiver.unregisterObserver(this);
    }


}
