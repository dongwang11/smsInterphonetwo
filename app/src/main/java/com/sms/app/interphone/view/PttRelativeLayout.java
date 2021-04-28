package com.sms.app.interphone.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.sms.app.framework.communication.localayer.bledriver.LogUtil;

public class PttRelativeLayout extends RelativeLayout {

    private static final String TAG="YanShi...Log - PttRelativeLayout";

    private boolean isOpen;

    private boolean isVoice = true;

    private boolean isList = false;

    private float x = 0;
    private float y = 0;


    private float startX = 0;
    private float startY = 0;


    //长按的runnable
    private Runnable mLongPressRunnable;


    private onPttLayoutListener listener;

    public PttRelativeLayout(Context context) {
        super(context);

        init();

    }

    public PttRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PttRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public void setOnPttLayoutListener(onPttLayoutListener listener){
        this.listener = listener;
    }

    private void init() {
        mLongPressRunnable = new Runnable() {

            @Override
            public void run() {

                if(isVoice){

                    isList = true;
                    listener.onLongStart();
                }

            }
        };
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        listener.onInterceptTouch();

        if (isOpen) {

            int action = event.getAction();

            switch (action) {

                case MotionEvent.ACTION_DOWN:

                    isVoice = true;

                    startX = event.getX();
                    startY = event.getY();

                    postDelayed(mLongPressRunnable,200);

                    break;

                case MotionEvent.ACTION_UP:

                    /*LogUtil.i(TAG,"ACTION_UP");
                    LogUtil.i(TAG,"startX"+startX);
                    LogUtil.i(TAG,"startY"+startY);
                    LogUtil.i(TAG,"event.getX()"+event.getX());
                    LogUtil.i(TAG,"event.getY()"+event.getY());
                    LogUtil.i(TAG,"X"+x);
                    LogUtil.i(TAG,"Y"+y);
                    LogUtil.i(TAG,"isList"+isList);*/

                    if(isList){

                        isVoice = true;
                        isList = false;
                        listener.onLongStop();

                        removeCallbacks(mLongPressRunnable);

                        return true;

                    }else{

                        isVoice = true;
                        isList = false;
                        listener.onLongStop();

                        removeCallbacks(mLongPressRunnable);

                    }

                    break;
                case MotionEvent.ACTION_MOVE:
                    /*LogUtil.i(TAG,"ACTION_MOVE");
                    LogUtil.i(TAG,"startX"+startX);
                    LogUtil.i(TAG,"startY"+startY);
                    LogUtil.i(TAG,"event.getX()"+event.getX());
                    LogUtil.i(TAG,"event.getY()"+event.getY());
                    LogUtil.i(TAG,"X"+x);
                    LogUtil.i(TAG,"Y"+y);*/

                    if(startX > event.getX()){
                        x = startX - event.getX();
                    }else{
                        x = event.getX() - startX;
                    }

                    if(startY > event.getY()){
                        y = startY - event.getY();
                    }else{
                        y = event.getY() - startY;
                    }


                    if(y > 20 || x > 20){
                        isVoice = false;
                        removeCallbacks(mLongPressRunnable);
                    }


                    if(isList){

                        return true;

                    }

                    break;

                default:
                    break;
            }

        }

        return super.onInterceptTouchEvent(event);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (isOpen) {

            int action = event.getAction();

            switch (action) {

                case MotionEvent.ACTION_UP:

                    if(isList){

                        isVoice = true;
                        isList = false;
                        listener.onLongStop();

                        removeCallbacks(mLongPressRunnable);

                        return true;

                    }else{

                        isVoice = true;
                        isList = false;
                        listener.onLongStop();

                        removeCallbacks(mLongPressRunnable);

                    }

                    break;

                default:
                    break;
            }

        }

        return super.onTouchEvent(event);
    }



    public interface onPttLayoutListener{

        void onLongStart();
        void onLongStop();
        void onInterceptTouch();

    }
}
