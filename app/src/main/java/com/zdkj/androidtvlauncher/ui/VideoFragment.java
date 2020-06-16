package com.zdkj.androidtvlauncher.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.zdkj.androidtvlauncher.R;
import com.zdkj.androidtvlauncher.base.MyApp;
import com.zdkj.androidtvlauncher.msgs.LiveChannel;
import com.zdkj.androidtvlauncher.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends Fragment implements Player.EventListener {

    PlayerView videoView;
    private DataSource.Factory dataSourceFactory;
    private SimpleExoPlayer player;
    private MainViewModel mainViewModel;

    public VideoFragment() {
    }

    public static VideoFragment newInstance() {
        return new VideoFragment();
    }

    private static boolean isBehindLiveWindow(ExoPlaybackException e) {
        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
            return false;
        }
        Throwable cause = e.getSourceException();
        while (cause != null) {
            if (cause instanceof BehindLiveWindowException) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        EventBus.getDefault().register(this);
        videoView = view.findViewById(R.id.videoView);
        initPlayer();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
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

    private void initPlayer() {
        DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter.Builder(getActivity()).build();
        dataSourceFactory = new DefaultDataSourceFactory(MyApp.getInstance(), BANDWIDTH_METER,
                new DefaultHttpDataSourceFactory(Util.getUserAgent(MyApp.getInstance(), "电视推广"), BANDWIDTH_METER));
        player = ExoPlayerFactory.newSimpleInstance(MyApp.getInstance());
        videoView.setUseController(false);
        videoView.setPlayer(player);
        HlsMediaSource mediaSource =
                new HlsMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse("http://liveplay-kk.rtxapp.com/live/program/live/cctv1hd/4000000/mnf.m3u8"));
        player.prepare(mediaSource, true, true);
        player.setPlayWhenReady(true);
    }

    @Override
    public void onPlayerError(ExoPlaybackException e) {
        if (isBehindLiveWindow(e)) {
            // Re-initialize player at the live edge.
            initPlayer();
        } else {
            // Handle other errors
        }
    }
}