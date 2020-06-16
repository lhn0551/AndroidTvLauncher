package com.zdkj.androidtvlauncher.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.leanback.widget.Presenter;

import com.zdkj.androidtvlauncher.R;
import com.zdkj.androidtvlauncher.models.LiveSourceBean;

public class HPresenter extends Presenter {
    /**
     * 创建ViewHolder，作用同RecyclerView$Adapter的onCreateViewHolder
     *
     * @param viewGroup
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_h, viewGroup, false);
        return new ViewHolder(inflate);
    }

    /**
     * 同RecyclerView$Adapter的onBindViewHolder，但是解耦了position
     *
     * @param viewHolder
     * @param o
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object o) {
        if (o instanceof LiveSourceBean.DataBean.LiveBean) {
            ((TextView) viewHolder.view.findViewById(R.id.tv_index)).setText(((LiveSourceBean.DataBean.LiveBean) o).getName());

        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        //解绑时释放资源
    }
}