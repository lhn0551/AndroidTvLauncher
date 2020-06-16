package com.zdkj.androidtvlauncher.adapter;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.zdkj.androidtvlauncher.R;
import com.zdkj.androidtvlauncher.models.LiveSourceBean;
import com.zdkj.androidtvlauncher.utils.LogUtils;

import java.util.List;


/**
 * @author 吴灏腾
 * @date 2018/12/17
 * @describe TODO
 */

public class RvAdapter extends BaseQuickAdapter<LiveSourceBean.DataBean.LiveBean, BaseViewHolder> {
    public RvAdapter(int layoutResId, @Nullable List<LiveSourceBean.DataBean.LiveBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, LiveSourceBean.DataBean.LiveBean item) {
        LogUtils.e("TAG111");
        helper.setText(R.id.txt_num, item.getName());
    }
}


