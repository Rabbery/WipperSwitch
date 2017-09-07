package com.yumpkimx.wipperswitchlib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by YumpkinMX on 2017/9/4.
 * Email: Loren0628@163.com
 */

public class WipperSwitchView extends View {

    //默认宽度
    private int DEFAULTWIDTH = 120;
    //默认高度
    private int DEFAULTHIGHT = 60;
    //缝隙大小
    private int GAPSIZE = 5;
    //状态改变监听器
    private OnSwitchStateChangeListener listener;
    //开关开启时背景色
    private int frontcolor;
    //开关关闭时背景色
    private int backgroudcolor;
    //开关滑块颜色
    private int wippercolor;
    //是否打开状态
    private boolean isopen;
    private Paint p;
    private int alpha;
    private Rect bgRect;
    private int wipper_min_left;
    private int wipper_max_left;
    private int wipper_left;
    private int wipper_start_pos = GAPSIZE;
    private Rect foreRect;
    private int startX;
    private int lastX;


    public WipperSwitchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        p = new Paint();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.wipperswitch);
        backgroudcolor = a.getColor(R.styleable.wipperswitch_backgroundColor, Color.GRAY);
        frontcolor = a.getColor(R.styleable.wipperswitch_frontColor, Color.parseColor("#ffff6e40"));
        wippercolor = a.getColor(R.styleable.wipperswitch_wipperColor, Color.WHITE);
        isopen = a.getBoolean(R.styleable.wipperswitch_isOpen, false);
        a.recycle();
    }

    public WipperSwitchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WipperSwitchView(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
        initParams();
    }

    private void initParams() {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        bgRect = new Rect(0, 0, width, height);
        foreRect = new Rect();
        wipper_min_left = GAPSIZE;
        wipper_max_left = width - (height - GAPSIZE * 2) - GAPSIZE;
        if (isopen) {
            wipper_left = wipper_max_left;
            alpha = 255;
        } else {
            wipper_left = wipper_min_left;
            alpha = 0;
        }
        wipper_start_pos = wipper_left;
    }


    private int measureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int result = 0;

        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                result = DEFAULTHIGHT;
                break;
            case MeasureSpec.EXACTLY:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(size, DEFAULTHIGHT);
                break;
        }
        return result;

    }

    private int measureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int result = 0;

        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                result = DEFAULTWIDTH;
                break;
            case MeasureSpec.EXACTLY:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(size, DEFAULTWIDTH);
                break;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int radius;
        radius = bgRect.height() / 2;
        p.setColor(backgroudcolor);
        canvas.drawRoundRect(new RectF(bgRect), radius, radius, p);
        p.setColor(frontcolor);
        p.setAlpha(alpha);
        canvas.drawRoundRect(new RectF(bgRect), radius, radius, p);

        foreRect.set(wipper_left, GAPSIZE, wipper_left + bgRect.height() - 2 * GAPSIZE, bgRect.height() - GAPSIZE);
        p.setColor(wippercolor);
        canvas.drawRoundRect(new RectF(foreRect), radius, radius, p);
    }

    private void smoothScroll(final boolean toRight) {
        ValueAnimator animator = ValueAnimator.ofInt(wipper_left, toRight ? wipper_max_left : wipper_min_left);
        animator.setDuration(500);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                wipper_left = (int) valueAnimator.getAnimatedValue();
                alpha = (int) (255 * (float) wipper_left / (float) wipper_max_left);
                invalidateDraw();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (toRight) {
                    isopen = true;
                    wipper_start_pos = wipper_max_left;
                    if (listener != null) {
                        listener.open();
                    }
                } else {
                    isopen = false;
                    wipper_start_pos = wipper_min_left;
                    if (listener != null) {
                        listener.close();
                    }
                }
            }
        });

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                lastX = (int) event.getX();
                int offsetX = lastX - startX;
                int distanceX = offsetX + wipper_start_pos;
                distanceX = distanceX > wipper_max_left ? wipper_max_left : distanceX;
                distanceX = distanceX < wipper_min_left ? wipper_min_left : distanceX;
                if (distanceX >= wipper_min_left && distanceX <= wipper_max_left) {
                    wipper_left = distanceX;
                    alpha = (int) (255 * (float) distanceX / (float) wipper_max_left);
                    invalidateDraw();
                }

                break;
            case MotionEvent.ACTION_UP:
                int wholeX = (int) (event.getX() - startX);
                wipper_start_pos = wipper_left;
                boolean toRight;
                toRight = wipper_start_pos > wipper_max_left / 2 ? true : false;
                if (Math.abs(wholeX) < 3) {
                    toRight = !toRight;
                }
                smoothScroll(toRight);
                break;
            default:
                break;
        }
        return true;
    }

    private void invalidateDraw() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    public void addOnSwitchStateChangeListener(OnSwitchStateChangeListener listener) {
        this.listener = listener;
    }

    public interface OnSwitchStateChangeListener {
        void open();

        void close();
    }


}
