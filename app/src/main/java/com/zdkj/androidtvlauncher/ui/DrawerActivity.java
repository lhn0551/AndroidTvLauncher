package com.zdkj.androidtvlauncher.ui;

import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.FocusHighlightHelper;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.OnChildViewHolderSelectedListener;
import androidx.leanback.widget.VerticalGridView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.zdkj.androidtvlauncher.R;
import com.zdkj.androidtvlauncher.adapter.HPresenter;
import com.zdkj.androidtvlauncher.base.BaseActivity;
import com.zdkj.androidtvlauncher.models.LiveSourceBean;
import com.zdkj.androidtvlauncher.msgs.LiveChannel;
import com.zdkj.androidtvlauncher.utils.LogUtils;
import com.zdkj.androidtvlauncher.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DrawerActivity extends BaseActivity {

    public static boolean isShowMu;
    private static int pos = 0;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.hgv)
    VerticalGridView mHgv;
    private MainViewModel mainViewModel;
    private List<LiveSourceBean.DataBean.LiveBean> liveSourceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, PlayVideoFragment.newInstance())
                    .commitNow();
        }


        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getLiveList().observe(this, liveSourceBean -> {
            liveSourceList = liveSourceBean.getData().getLive();
            initViews();

        });

    }

    private void initViews() {
        pos = 0;
        mHgv.setNumColumns(1);
        mHgv.setItemSpacing(20);
        mHgv.setGravity(Gravity.CENTER_VERTICAL);
        mHgv.setOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener() {
            @Override
            public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
                super.onChildViewHolderSelected(parent, child, position, subposition);
                LogUtils.e("onChildViewHolderSelected() returned: " + position);
                pos = position;
            }

            @Override
            public void onChildViewHolderSelectedAndPositioned(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
                super.onChildViewHolderSelectedAndPositioned(parent, child, position, subposition);
            }
        });
        HPresenter presenter = new HPresenter();
        ArrayObjectAdapter objectAdapter = new ArrayObjectAdapter(presenter);
        objectAdapter.addAll(0, liveSourceList);
        ItemBridgeAdapter bridgeAdapter = new ItemBridgeAdapter(objectAdapter);
        mHgv.setAdapter(bridgeAdapter);
        mHgv.requestFocus();
        FocusHighlightHelper.setupHeaderItemFocusHighlight(bridgeAdapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_A:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_MENU:
                if (isShowMu) {
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                    } else {
                        drawerLayout.openDrawer(GravityCompat.START);
                        mHgv.requestFocus();
                    }
                } else {
                    ToastUtil.showLong("广告时间,暂时无法切换频道");
                }
                break;
            case KeyEvent.KEYCODE_S:
            case KeyEvent.KEYCODE_DPAD_CENTER:

                if (isShowMu) {
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        EventBus.getDefault().post(new LiveChannel(liveSourceList.get(pos).getUrl()));
                    }
                } else {
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                    ToastUtil.showLong("广告时间,暂时无法切换频道");
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

}