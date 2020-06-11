package com.zdkj.androidtvlauncher.ui;

import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.zdkj.androidtvlauncher.R;
import com.zdkj.androidtvlauncher.adapter.RvAdapter;
import com.zdkj.androidtvlauncher.base.BaseActivity;
import com.zdkj.androidtvlauncher.models.LiveSourceBean;
import com.zdkj.androidtvlauncher.msgs.LiveChannel;
import com.zdkj.androidtvlauncher.utils.ToastUtil;
import com.zdkj.androidtvlauncher.widget.TvRecyclerView;
import com.zdkj.androidtvlauncher.widget.V7LinearLayoutManager;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DrawerActivity extends BaseActivity {

    public static boolean isShowMu;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.tvRecycler)
    TvRecyclerView tvRecycler;
    private MainViewModel mainViewModel;
    private List<LiveSourceBean.DataBean.LiveBean> liveSourceList;
    private RvAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainActivityExo.newInstance())
                    .commitNow();
        }
        adapter = new RvAdapter(R.layout.item_left_view, null);
        adapter.addChildClickViewIds(R.id.txt_num);
        tvRecycler.setLayoutManager(new V7LinearLayoutManager(this));
        tvRecycler.setAdapter(adapter);
        tvRecycler.setGainFocusListener(new TvRecyclerView.FocusGainListener() {
            @Override
            public void onFocusGain(View child, View focued) {
            }
        });

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getLiveList().observe(this, liveSourceBean -> {
            liveSourceList = liveSourceBean.getData().getLive();
            adapter.setNewData(liveSourceList);
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_A:
            case KeyEvent.KEYCODE_MENU:
                if (isShowMu) {
                    if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                        drawerLayout.closeDrawer(Gravity.LEFT);
                    } else {
                        drawerLayout.openDrawer(Gravity.LEFT);
                        tvRecycler.requestFocus();
                    }
                } else {
                    ToastUtil.showLong("广告时间,暂时无法切换频道");
                }
                break;
            case KeyEvent.KEYCODE_S:
            case KeyEvent.KEYCODE_DPAD_CENTER:

                if (isShowMu) {
                    if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        EventBus.getDefault().post(new LiveChannel(liveSourceList.get(tvRecycler.getmLastFocusPosition()).getUrl()));
                    }
                } else {
                    if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                        drawerLayout.closeDrawer(Gravity.LEFT);
                    }
                    ToastUtil.showLong("广告时间,暂时无法切换频道");
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

}