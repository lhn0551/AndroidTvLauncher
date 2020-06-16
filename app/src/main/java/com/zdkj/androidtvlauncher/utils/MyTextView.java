package com.zdkj.androidtvlauncher.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.Choreographer;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;


@SuppressLint("AppCompatCustomView")
public class MyTextView extends TextView {


    private static final byte MARQUEE_STOPPED = 0x0;
    private static final byte MARQUEE_STARTING = 0x1;
    private static final byte MARQUEE_RUNNING = 0x2;
    private String TAG = MyTextView.class.getSimpleName();
    private OnMarqueeListener onMarqueeListener;
    private boolean isInit;
    private Choreographer.FrameCallback mRestartCallback = new Choreographer.FrameCallback() {
        @Override
        public void doFrame(long frameTimeNanos) {

            try {
                Field marquee = Objects.requireNonNull(MyTextView.this.getClass().getSuperclass()).getDeclaredField("mMarquee");
                marquee.setAccessible(true);
                Object obj = marquee.get(MyTextView.this);
                if (obj != null) {
                    Class cls = obj.getClass();
                    Field field = cls.getDeclaredField("mStatus");
                    field.setAccessible(true);
                    byte mStatus = (Byte) field.get(obj);
                    if (mStatus == MARQUEE_RUNNING) {
                        Field field1 = cls.getDeclaredField("mRepeatLimit");
                        field1.setAccessible(true);
                        int mRepeatLimit = (Integer) field1.get(obj);
                        ;
                        if (mRepeatLimit >= 0) {
                            mRepeatLimit--;
                        }
                        if (onMarqueeListener != null) {
                            onMarqueeListener.onMarqueeRepeatChanged(mRepeatLimit);
                        }
                        Method method = cls.getDeclaredMethod("start", Integer.TYPE);
                        method.setAccessible(true);
                        method.invoke(obj, mRepeatLimit);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public MyTextView(Context context) {
        super(context);

    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context) {

    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
    }

    @Override
    public boolean post(Runnable action) {
        return super.post(action);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        try {
            if (!isInit) {
                Field marquee = TextView.class.getDeclaredField("mMarquee");
                marquee.setAccessible(true);
                Object obj = marquee.get(this);
                if (obj != null) {
                    Class cls = obj.getClass();
                    Field field = cls.getDeclaredField("mStatus");
                    field.setAccessible(true);
                    Field field1 = cls.getDeclaredField("mRestartCallback");
                    isInit = true;
                    field1.setAccessible(true);
                    field1.set(obj, mRestartCallback);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMarqueeListener(OnMarqueeListener onMarqueeListener) {
        this.onMarqueeListener = onMarqueeListener;
    }

    //状态监听接口
    public interface OnMarqueeListener {
        void onMarqueeRepeatChanged(int repeatLimit);
    }

}