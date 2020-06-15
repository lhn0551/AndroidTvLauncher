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
import com.zdkj.androidtvlauncher.utils.HttpProxyCacheUtil;
import com.zdkj.androidtvlauncher.utils.LogUtils;
import com.zdkj.androidtvlauncher.utils.MyTextView;
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

    @BindView(R.id.iv_ad)
    ImageView ivAd;
    @BindView(R.id.tv_Timer)
    TextView tvTimer;
    @BindView(R.id.tv_ad)
    MyTextView tvAd;
    @BindView(R.id.videoView)
    PlayerView videoView;
    private ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
    private List<VideoBean.DataBean> videoList = new ArrayList<>();
    private MainViewModel mainViewModel;
    private SimpleExoPlayer player;
    private DataSource.Factory dataSourceFactory;
    private ConcatenatingMediaSource concatenatedSource;
    private LiveRunnable liveRunnable;
    private boolean isLive=true;
    private boolean isTextShow = true;
    private CountDownTimer timer;

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

            mainViewModel.getAfterPlay(player.getCurrentTag() + "")
                    .observe(getViewLifecycleOwner(), afterPlayingBean -> {
                        if (afterPlayingBean.getData().getDelete().equals("1")) {
                            mainViewModel.updatePlayList();
                        } else if (afterPlayingBean.getData().getDelete().equals("2")) {
                            mainViewModel.updateImageList();
                        } else if (afterPlayingBean.getData().getDelete().equals("3")) {
                            isLive = false;
//                            getAdText();
                            mainViewModel.updateTextList();
                        }
                    });

            mainViewModel.getImageList().observe(getViewLifecycleOwner(), imageBean -> {
                List<ImageBean.DataBean> imageList = imageBean.getData();
                if (isLive && imageList.size() > 0) {
                    isLive=false;
                    showLivePic(imageList.get(0).getImage_url());
//                    Timer timer=new Ti
                    if (timer == null) {
                        tvTimer.setVisibility(View.VISIBLE);
                        CountDown(imageList.get(0).getTimestr());
                        timer = null;
                    }

                }
            });
        }

        mainViewModel.getTextList().observe(getViewLifecycleOwner(), textBean -> {
            if (isTextShow && textBean.getData().size() > 0) {
                isTextShow = false;
                tvAd.setVisibility(View.VISIBLE);
                tvAd.setText(textBean.getData().get(0).getTxt());
                tvAd.setSelected(true);
                tvAd.setMarqueeListener(new MyTextView.OnMarqueeListener() {
                    @Override
                    public void onMarqueeRepeateChanged(int repeatLimit) {
                        LogUtils.e("reaaaa" + repeatLimit);
                        isTextShow = true;
                        tvAd.setVisibility(View.GONE);
                        tvAd.setText("");
                    }
                });
//                tvAd.startFor0();
//                tvAd.setOnMarqueeCompleteListener(() -> {
//                    LogUtils.e("TAGTAGTAGTAG");
//                    isTextShow=true;
//                    tvAd.startStopMarquee(false);
//                    tvAd.setVisibility(View.GONE);
//                    tvAd.setText("");
//                });


            }
        });
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
                isLive=true;
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
                    EventBus.getDefault().post(new AfterPlay(player.getCurrentTag() + ""));
                    LogUtils.e("正在播放=" + player.getCurrentTag());
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
            HlsMediaSource mediaSource =
                    new HlsMediaSource.Factory(dataSourceFactory).setTag(videoList.get(0).getVideo_id())
                            .createMediaSource(Uri.parse(videoName));
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
            startThreadPool();
            DrawerActivity.isShowMu = true;
        } else {
            DrawerActivity.isShowMu = false;
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

    /**
     * 删除广告数据列表
     */
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

    /**
     * 直播轮询进程池
     */
    private void startThreadPool() {
        liveRunnable = new LiveRunnable();
        if (exec == null) {
            exec = new ScheduledThreadPoolExecutor(1);
        }
        exec.scheduleAtFixedRate(liveRunnable, 15000, 30000, TimeUnit.MILLISECONDS);
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
            EventBus.getDefault().post(new AfterPlay(player.getCurrentTag() + ""));
        }
    }
}
