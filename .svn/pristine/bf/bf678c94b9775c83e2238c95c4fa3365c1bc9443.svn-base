package com.sms.app.interphone.view;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.nineoldandroids.view.ViewHelper;

public class DragLayout extends FrameLayout {

    private boolean isShowShadow = true;

    private GestureDetectorCompat gestureDetector;
    private ViewDragHelper dragHelper;
    private DragListener dragListener;
    private int range;
    private int width;
    private int height;
    private int mainLeft;

    private boolean animate;

    private Context context;
    private ImageView iv_shadow;
    private RelativeLayout vg_left;
    private MyRelativeLayout vg_main;
    private Status status = Status.Close;

    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        gestureDetector = new GestureDetectorCompat(context, new YScrollDetector());
        dragHelper = ViewDragHelper.create(this, dragHelperCallback);
    }

    class YScrollDetector extends SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy) {
            return Math.abs(dy) <= Math.abs(dx);
        }
    }

    private ViewDragHelper.Callback dragHelperCallback = new ViewDragHelper.Callback() {

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {

            return 0;
        }

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return width;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {

            super.onViewReleased(releasedChild, xvel, yvel);

        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top,
                int dx, int dy) {

            mainLeft = left;

            if (mainLeft > range) {
                mainLeft = range;
            }


        //    Log.e("TAG","mainLeft:"+mainLeft+",range"+range+",dx"+dx+",dy"+dy+",top"+top);
            /*if(animate){
                if (isShowShadow) {
                    iv_shadow.layout(0, 0, mainLeft + width, height);
                }
                if (changedView == vg_left) {
                    vg_left.layout(0, 0, width, height);
                    vg_main.layout(mainLeft, 0, mainLeft + width, height);
                }
            }else{

                vg_left.layout(0, 0, width, height);

                vg_main.layout(mainLeft, 0, mainLeft + width, height);

            }*/


            dispatchDragEvent(mainLeft);
        }
    };

    public interface DragListener {
        public void onOpen();

        public void onClose();

        public void onDrag(float percent);
    }

    public void setDragListener(DragListener dragListener) {
        this.dragListener = dragListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isShowShadow) {
            iv_shadow = new ImageView(context);
         //   iv_shadow.setImageResource(R.drawable.shadow);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            addView(iv_shadow, 1, lp);
        }
        vg_left = (RelativeLayout) getChildAt(0);
        vg_main = (MyRelativeLayout) getChildAt(isShowShadow ? 2 : 1);
        vg_main.setDragLayout(this);
        vg_left.setClickable(true);
        vg_main.setClickable(true);
    }

    public ViewGroup getVg_main() {
        return vg_main;
    }

    public ViewGroup getVg_left() {
        return vg_left;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = vg_left.getMeasuredWidth();
        height = vg_left.getMeasuredHeight();


    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        try {
            vg_left.layout(0, 0, width, height);
            vg_main.layout(mainLeft, 0, mainLeft + width, height);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*@Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return dragHelper.shouldInterceptTouchEvent(ev) && gestureDetector.onTouchEvent(ev);
    }*/

    /*@Override
    public boolean onTouchEvent(MotionEvent e) {
        try {
            dragHelper.processTouchEvent(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }*/

    private void dispatchDragEvent(int mainLeft) {
        if (dragListener == null) {
            return;
        }

        float percent = mainLeft / (float) range;
    //    Log.e("animateView","mainLeft"+mainLeft+",range"+range+",percent"+percent);
        //加载View的方式；
        /*if (this.animate) {
            animateView(percent,mainLeft);
        }*/
        dragListener.onDrag(percent);
        Status lastStatus = status;
        if (lastStatus != getStatus() && status == Status.Close) {
                dragListener.onClose();
            } else if (lastStatus != getStatus() && status == Status.Open) {
                dragListener.onOpen();
        }
    }

    private void animateView(float percent,float mainLeft) {

        float f1 = 1 - percent * 0.1f;

        float f2 =  percent * (vg_main.getHeight() * 0.05f);

     //   Log.e("animateView","isShowShadow"+vg_main.getHeight() * 0.05f+",percent:"+percent+",mainLeft:"+mainLeft+",range"+range+",f2:"+f2);
        //控制View X Y 轴的缩小程度。
        ViewHelper.setScaleX(vg_main, f1);
        ViewHelper.setScaleY(vg_main, f1);

        ViewHelper.setTranslationY(vg_main, f2);

    //   ViewHelper.setTranslationX(vg_main, 0);
    //    ViewHelper.setTranslationY(vg_main, vg_main.getHeight() * 0.05f);

        /*if(f1 == 1.0f){
            ViewHelper.setTranslationY(vg_main, 0);
        }else{

        }*/

    //    ViewHelper.setAlpha(vg_main, percent);

       /* ViewHelper.setScaleX(vg_main, 0.5f + 0.5f * percent);
        ViewHelper.setScaleY(vg_main, 0.5f + 0.5f * percent);*/
    //    ViewHelper.setAlpha(vg_main, percent);



    //  getBackground().setColorFilter(evaluate(percent, Color.BLACK, Color.TRANSPARENT), Mode.SRC_OVER);


    }

    private Integer evaluate(float fraction, Object startValue, Integer endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;
        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;
        return (int) ((startA + (int) (fraction * (endA - startA))) << 24)
                | (int) ((startR + (int) (fraction * (endR - startR))) << 16)
                | (int) ((startG + (int) (fraction * (endG - startG))) << 8)
                | (int) ((startB + (int) (fraction * (endB - startB))));
    }

    @Override
    public void computeScroll() {
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public enum Status {
        Drag, Open, Close
    }

    public Status getStatus() {
        if (mainLeft == 0) {
            status = Status.Close;
        } else if (mainLeft == range) {
            status = Status.Open;
        } else {
            status = Status.Drag;
        }
        return status;
    }

    public void open() {
        open(true);
    }

    public void open(boolean animate) {

        this.animate = animate;

        Log.e("TAG","open:"+range);

        if (animate) {
            range = (int) (width * 0.6f);
            if (dragHelper.smoothSlideViewTo(vg_main, range, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            range = (int) (width * 0.4f);

            if (dragHelper.smoothSlideViewTo(vg_main, -range, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        //    vg_main.layout(range, 0, range * 2, height);
            /*vg_main.layout(range, 0, range * 2, height);
            dispatchDragEvent(range);*/
        }
    }

    public void close() {

        close(true);

    }

    public void deleteClose() {

        /*if(!this.animate){
            close(true);
        }*/
        close(true);

    }

    public void close(boolean animate) {
        if (dragHelper.smoothSlideViewTo(vg_main, 0, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        /*if (this.animate) {
            if (dragHelper.smoothSlideViewTo(vg_main, 0, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            vg_main.layout(0, 0, width, height);
            dispatchDragEvent(0);
        }*/
    }

}
