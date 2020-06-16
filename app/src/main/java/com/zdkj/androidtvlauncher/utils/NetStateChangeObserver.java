package com.zdkj.androidtvlauncher.utils;

public interface NetStateChangeObserver {
    void onNetDisconnected();

    void onNetConnected(NetworkType networkType);
}
