package com.zdkj.androidtvlauncher.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zdkj.androidtvlauncher.utils.LogUtils;

import java.util.ArrayList;

/**
 * @Author：wuhaoteng
 * @Date:2018/12/23
 * @Desc：支持 1.选中项居中 2.焦点记忆 3.可以控制是否横竖向移除
 */
public class TvRecyclerView extends RecyclerView {
    //焦点是否居中
    private boolean mSelectedItemCentered = true;
    private int mSelectedItemOffsetStart = 0;
    private int mSelectedItemOffsetEnd = 0;

    //是否可以纵向移出
    private boolean mCanFocusOutVertical = true;
    //是否可以横向移出
    private boolean mCanFocusOutHorizontal = false;
    //焦点移出recyclerview的事件监听
    private FocusLostListener mFocusLostListener;
    //焦点移入recyclerview的事件监听
    private FocusGainListener mFocusGainListener;
    //最后一次聚焦的位置
    private int mLastFocusPosition = 0;
    private View mLastFocusView = null;

    public TvRecyclerView(Context context) {
        super(context);
        init();
    }

    public TvRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TvRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public int getLastFocusPosition() {
        return mLastFocusPosition;
    }

    public void init() {
        setItemAnimator(null);
        /**
         * 该属性是当一个为view获取焦点时，定义viewGroup和其子控件两者之间的关系。
         *
         * 属性的值有三种：
         * beforeDescendants：viewgroup会优先其子类控件而获取到焦点
         * afterDescendants：viewgroup只有当其子类控件不需要获取焦点时才获取焦点
         * blocksDescendants：viewgroup会覆盖子类控件而直接获得焦点
         * */
        setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        setChildrenDrawingOrderEnabled(true);
        this.setFocusable(true);
    }


    /**
     * 实现焦点记忆的关键代码
     * <p>
     * root.addFocusables会遍历root的所有子view和孙view,然后调用addFocusable把isFocusable的view添加到focusables
     */
    @Override
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        LogUtils.e("views = " + views);
        LogUtils.e("lastFocusView = " + mLastFocusView + " mLastFocusPosition = " + mLastFocusPosition);
        if (this.hasFocus() || mLastFocusView == null) {
            //在recyclerview内部焦点切换
            super.addFocusables(views, direction, focusableMode);
        } else {
            //将当前的view放到Focusable views列表中，再次移入焦点时会取到该view,实现焦点记忆功能
            views.add(getLayoutManager().findViewByPosition(mLastFocusPosition));
            LogUtils.e("views.add(lastFocusView)");
        }
    }

    @Override
    public View focusSearch(View focused, int direction) {
        View realNextFocus = super.focusSearch(focused, direction);
        View nextFocus = FocusFinder.getInstance().findNextFocus(this, focused, direction);
        LogUtils.e("focused = " + focused);
        LogUtils.e("nextFocus = " + nextFocus);
        LogUtils.e("realNextFocus = " + realNextFocus);
        //canScrollVertically(1)  true表示能滚动，false表示已经滚动到底部
        //canScrollVertically(-1) true表示能滚动，false表示已经滚动到顶部
        LogUtils.e("canScrollVertically(-1) = " + canScrollVertically(-1));
        LogUtils.e("canScrollVertically(1) = " + canScrollVertically(1));
        LogUtils.e("canScrollHorizontally(-1) = " + canScrollHorizontally(-1));
        LogUtils.e("canScrollHorizontally(1) = " + canScrollHorizontally(1));
        switch (direction) {
            case FOCUS_RIGHT:
                //调用移出的监听
                if (nextFocus == null) {
                    if (mCanFocusOutHorizontal) {
                        if (mFocusLostListener != null) {
                            mFocusLostListener.onFocusLost(focused, direction);
                        }
                        return realNextFocus;
                    } else {
                        return null;
                    }
                }
                break;
            case FOCUS_LEFT:
                //调用移出的监听
                if (nextFocus == null) {
                    if (mCanFocusOutHorizontal) {
                        if (mFocusLostListener != null) {
                            mFocusLostListener.onFocusLost(focused, direction);
                        }
                        return realNextFocus;
                    } else {
                        return null;
                    }
                }
                break;
            case FOCUS_UP:
                if (nextFocus == null && !canScrollVertically(-1)) {
                    //滑动到顶部
                    if (mCanFocusOutVertical) {
                        return realNextFocus;
                    } else {
                        return null;
                    }
                }
                break;
            case FOCUS_DOWN:
                if (nextFocus == null && !canScrollVertically(1)) {
                    //滑动到底部
                    if (mCanFocusOutVertical) {
                        return realNextFocus;
                    } else {
                        return null;
                    }
                }
                break;
        }
        return realNextFocus;
    }

    /**
     * 通过ViewParent#requestChildFocus通知父控件即将获取焦点
     *
     * @param child   下一个要获得焦点的recyclerview item
     * @param focused 当前聚焦的view
     */
    @Override
    public void requestChildFocus(View child, View focused) {
        if (null != child) {
            LogUtils.e("nextchild = " + child + ",focused = " + focused);
            if (!hasFocus()) {
                //recyclerview 子view 重新获取焦点，调用移入焦点的事件监听
                if (mFocusGainListener != null) {
                    mFocusGainListener.onFocusGain(child, focused);
                }
            }

            //执行过super.requestChildFocus之后hasFocus会变成true
            super.requestChildFocus(child, focused);
            //取得获得焦点的item的position
            mLastFocusView = focused;
            mLastFocusPosition = getChildViewHolder(child).getAdapterPosition();
            LogUtils.e("focusPos = " + mLastFocusPosition);

            //计算控制recyclerview 选中item的居中从参数
            if (mSelectedItemCentered) {
                mSelectedItemOffsetStart = !isVertical() ? (getFreeWidth() - child.getWidth()) : (getFreeHeight() - child.getHeight());
                mSelectedItemOffsetStart /= 2;
                mSelectedItemOffsetEnd = mSelectedItemOffsetStart;
            }
        }
        LogUtils.e("mSelectedItemOffsetStart = " + mSelectedItemOffsetStart);
        LogUtils.e("mSelectedItemOffsetEnd = " + mSelectedItemOffsetEnd);
    }

    /**
     * 通过该方法设置选中的item居中
     * <p>
     * 该方法能够确定在布局中滚动或者滑动时候，子item和parent之间的位置
     * dy，dx的实际意义就是在滚动中下滑和左右滑动的距离
     * 而这个值的确定会严重影响滑动的流畅程度
     */
    @Override
    public boolean requestChildRectangleOnScreen(View child, Rect rect, boolean immediate) {
        final int parentLeft = getPaddingLeft();
        final int parentRight = getWidth() - getPaddingRight();

        final int parentTop = getPaddingTop();
        final int parentBottom = getHeight() - getPaddingBottom();

        final int childLeft = child.getLeft() + rect.left;
        final int childTop = child.getTop() + rect.top;

        final int childRight = childLeft + rect.width();
        final int childBottom = childTop + rect.height();

        final int offScreenLeft = Math.min(0, childLeft - parentLeft - mSelectedItemOffsetStart);
        final int offScreenRight = Math.max(0, childRight - parentRight + mSelectedItemOffsetEnd);

        final int offScreenTop = Math.min(0, childTop - parentTop - mSelectedItemOffsetStart);
        final int offScreenBottom = Math.max(0, childBottom - parentBottom + mSelectedItemOffsetEnd);


        final boolean canScrollHorizontal = getLayoutManager().canScrollHorizontally();
        final boolean canScrollVertical = getLayoutManager().canScrollVertically();

        // Favor the "start" layout direction over the end when bringing one side or the other
        // of a large rect into view. If we decide to bring in end because start is already
        // visible, limit the scroll such that start won't go out of bounds.
        final int dx;
        if (canScrollHorizontal) {
            if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                dx = offScreenRight != 0 ? offScreenRight
                        : Math.max(offScreenLeft, childRight - parentRight);
            } else {
                dx = offScreenLeft != 0 ? offScreenLeft
                        : Math.min(childLeft - parentLeft, offScreenRight);
            }
        } else {
            dx = 0;
        }

        // Favor bringing the top into view over the bottom. If top is already visible and
        // we should scroll to make bottom visible, make sure top does not go out of bounds.
        final int dy;
        if (canScrollVertical) {
            dy = offScreenTop != 0 ? offScreenTop : Math.min(childTop - parentTop, offScreenBottom);
        } else {
            dy = 0;
        }


        if (dx != 0 || dy != 0) {
            LogUtils.e("dx = " + dx);
            LogUtils.e("dy = " + dy);
            if (immediate) {
                scrollBy(0, dy);
            } else {
                smoothScrollBy(0, dy);
            }
            // 重绘是为了选中item置顶，具体请参考getChildDrawingOrder方法
            postInvalidate();
            return true;
        }


        return false;
    }

    /**
     * 判断是垂直，还是横向.
     */
    private boolean isVertical() {
        LayoutManager manager = getLayoutManager();
        if (manager != null) {
            LinearLayoutManager layout = (LinearLayoutManager) getLayoutManager();
            return layout.getOrientation() == LinearLayoutManager.VERTICAL;

        }
        return false;
    }

    private int getFreeWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int getFreeHeight() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }


    /**
     * 是否可以垂直滚动
     **/
    public boolean isCanFocusOutVertical() {
        return mCanFocusOutVertical;
    }

    /**
     * 设置可以垂直滚动
     **/
    public void setCanFocusOutVertical(boolean canFocusOutVertical) {
        mCanFocusOutVertical = canFocusOutVertical;
    }

    /**
     * 是否可以水平滚动
     **/
    public boolean isCanFocusOutHorizontal() {
        return mCanFocusOutHorizontal;
    }

    /**
     * 设置是否可以水平滚动
     **/
    public void setCanFocusOutHorizontal(boolean canFocusOutHorizontal) {
        mCanFocusOutHorizontal = canFocusOutHorizontal;
    }


    /**
     * 设置焦点丢失监听
     */
    public void setFocusLostListener(FocusLostListener focusLostListener) {
        this.mFocusLostListener = focusLostListener;
    }

    /**
     * 设置焦点获取监听
     */
    public void setGainFocusListener(FocusGainListener focusListener) {
        this.mFocusGainListener = focusListener;
    }

    public interface FocusLostListener {
        void onFocusLost(View lastFocusChild, int direction);
    }


    public interface FocusGainListener {
        void onFocusGain(View child, View focued);
    }


}
