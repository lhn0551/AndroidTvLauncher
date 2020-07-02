package com.zdkj.androidtvlauncher.ui;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
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
import com.zdkj.androidtvlauncher.models.ImageBean;
import com.zdkj.androidtvlauncher.models.VideoBean;
import com.zdkj.androidtvlauncher.msgs.AfterPlay;
import com.zdkj.androidtvlauncher.msgs.LiveChannel;
import com.zdkj.androidtvlauncher.msgs.StopText;
import com.zdkj.androidtvlauncher.utils.HttpProxyCacheUtil;
import com.zdkj.androidtvlauncher.utils.LogUtils;
import com.zdkj.androidtvlauncher.utils.MarqueeScrollerView;
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


public class PlayVideoFragment extends Fragment implements NetStateChangeObserver, EventListener {

    private static String playerId;
    @BindView(R.id.iv_ad)
    ImageView ivAd;
    @BindView(R.id.tv_Timer)
    TextView tvTimer;
    @BindView(R.id.tv_ad)
    MarqueeScrollerView tvAd;
    @BindView(R.id.videoView)
    PlayerView videoView;
    private ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
    private List<VideoBean.DataBean> videoList = new ArrayList<>();
    private MainViewModel mainViewModel;
    private SimpleExoPlayer player;
    private DataSource.Factory dataSourceFactory;
    private ConcatenatingMediaSource concatenatedSource;
    private boolean isLive = true;
    private boolean isTextShow = true;
    private CountDownTimer timer;
    private HttpProxyCacheServer proxy;

    public static PlayVideoFragment newInstance() {
        return new PlayVideoFragment();
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

    /**
     * 请求数据
     */
    private void initData() {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        if (NetUtils.isNetworkConnected(getActivity())) {
            mainViewModel.getPlayList().observe(getViewLifecycleOwner(), videoBean -> {
                videoList = videoBean.getData();
                playVideo();
            });

            mainViewModel.getAfterPlay(playerId + "")
                    .observe(getViewLifecycleOwner(), afterPlayingBean -> {
                        switch (afterPlayingBean.getData().getDelete()) {
                            case "1":
                                mainViewModel.updatePlayList();
                                break;
                            case "2":
                                mainViewModel.updateImageList();
                                break;
                            case "3":
                                mainViewModel.updateTextList();
                                break;
                        }
                    });

            mainViewModel.getImageList().observe(getViewLifecycleOwner(), imageBean -> {
                if (playerId == null) {
                    return;
                }
                List<ImageBean.DataBean> imageList = imageBean.getData();
                if (isLive && imageList.size() > 0) {
                    isLive = false;
                    showLivePic(imageList.get(0).getImage_url());
                    if (timer == null) {
                        tvTimer.setVisibility(View.VISIBLE);
                        CountDown(imageList.get(0).getTimestr());
                        timer = null;
                        isLive = true;
                    }

                }
            });
        }

        mainViewModel.getTextList().observe(getViewLifecycleOwner(), textBean -> {
            if (playerId == null) {
                return;
            }
            if (isTextShow && textBean.getData().size() > 0) {
                isTextShow = false;
                tvAd.setVisibility(View.VISIBLE);
                tvAd.setText(textBean.getData().get(0).getTxt());
                tvAd.setSelected(true);

                tvAd.setRndDuration(textBean.getData().get(0).getTxt().length() * 400);
                tvAd.startScroll();
//                tvAd.setMarqueeListener(repeatLimit -> {
//                    LogUtils.e("剩余滚动次数:" + repeatLimit);
//                    isTextShow = true;
//                    tvAd.setVisibility(View.GONE);
//                    tvAd.setText("");
//                });

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void stopTextScroll(StopText msg) {
        tvAd.setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    private void CountDown(int time) {
        timer = new CountDownTimer(time * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText(getResources().getString(R.string.adtimer) + "\t" + millisUntilFinished / 1000 + "\t");
            }

            @Override
            public void onFinish() {
                showLivePic(null);
                tvTimer.setVisibility(View.GONE);
                isLive = true;
            }
        }.start();

    }

    /**
     * 初始化播放器
     */
    private void initPlayer() {

        DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter.Builder(getActivity()).build();
        dataSourceFactory = new DefaultDataSourceFactory(MyApp.getInstance(), BANDWIDTH_METER,
                new DefaultHttpDataSourceFactory(Util.getUserAgent(MyApp.getInstance(), "电视推广"), BANDWIDTH_METER));
//        if (player==null)
        player = ExoPlayerFactory.newSimpleInstance(MyApp.getInstance());
        videoView.setUseController(false);
        videoView.setPlayer(player);
        player.addListener(new EventListener() {
            @Override
            public void onPlayerError(ExoPlaybackException error) {
                LogUtils.e(error.getMessage());
                if (error.type == ExoPlaybackException.TYPE_SOURCE) {
                    playVideo();
                }
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playWhenReady) {
                    playerId = player.getCurrentTag() + "";
                }
            }

            @Override
            public void onPositionDiscontinuity(int reason) {
                if (reason == Player.DISCONTINUITY_REASON_PERIOD_TRANSITION) {
                    playerId = player.getCurrentTag() + "";
                    EventBus.getDefault().post(new AfterPlay(playerId + ""));
                    LogUtils.e("切换视频=" + playerId);
                }
            }
        });

    }

    /**
     * @param channel 直播流换台
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeChannel(LiveChannel channel) {
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
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

    }

    /**
     * Live ad show (gif&pic)
     *
     * @param imgUrl gir/pic url
     */
    private void showLivePic(String imgUrl) {
        if (imgUrl != null) {
            Glide.with(this).load(imgUrl).into(ivAd);
        } else
            ivAd.setImageDrawable(null);
    }

    /**
     * @param afterPlay 分成接口
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void LiveAfterPlay(AfterPlay afterPlay) {
        if (afterPlay.getId().equals("")) {
            return;
        }
        mainViewModel.LiveAfterPlay(afterPlay.getId());
    }

    /**
     * 播放视频
     */
    private void playVideo() {
        if (videoList.size() == 0) {
            return;
        }
        String videoName = videoList.get(0).getVideo_url();
        if (videoName.endsWith(".m3u8")) {
            Log.e("TAG", videoName);
            HlsMediaSource mediaSource =
                    new HlsMediaSource.Factory(dataSourceFactory).setTag(videoList.get(0).getVideo_id())
                            .createMediaSource(Uri.parse(videoName));
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
            startThreadPool();
            DrawerActivity.isShowMu = true;
        } else {
            proxy = HttpProxyCacheUtil.getVideoProxy();
            DrawerActivity.isShowMu = false;
            if (concatenatedSource != null) {
                concatenatedSource.clear();
            } else {
                concatenatedSource = new ConcatenatingMediaSource();
            }
            stopThreadPool();


            for (int i = 0; i < videoList.size(); i++) {
//                proxy.registerCacheListener(this, videoList.get(i).getVideo_url());
                String url = proxy.getProxyUrl(videoList.get(i).getVideo_url());
                LogUtils.e("proxyUrl=" + url + "\noriginal Url=" + videoList.get(i).getVideo_url());
//                String url = videoList.get(i).getVideo_url();
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

    /**
     * 直播轮询进程池
     */
    private void startThreadPool() {
        if (exec == null) {
            exec = new ScheduledThreadPoolExecutor(1);
        }
        if (exec.getTaskCount() == 0)
            exec.scheduleAtFixedRate(new LiveRunnable(), 15000, 30000, TimeUnit.MILLISECONDS);
    }

    private void stopThreadPool() {
        if (exec.getTaskCount() > 0) {
            exec.shutdownNow();
            exec = null;
        }
    }


    static class LiveRunnable implements Runnable {
        @Override
        public void run() {
            synchronized (PlayVideoFragment.class) {
                EventBus.getDefault().post(new AfterPlay(playerId));
            }

        }
    }
}
