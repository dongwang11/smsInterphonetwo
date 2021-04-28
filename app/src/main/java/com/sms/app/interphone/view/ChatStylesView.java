package com.sms.app.interphone.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.sms.app.interphone.R;


/**
 * Created by Administrator on 2018/3/28.
 */

public class ChatStylesView extends RelativeLayout{

    private Paint paint;

    private Paint paintContent;

    private boolean isOpen;

    int contentColor;

    public ChatStylesView(Context context) {
        super(context);
        setWillNotDraw(false) ;
        paint = new Paint();

        paintContent = new Paint();
        //设置空心
        paintContent.setStyle(Paint.Style.FILL);
        //设置画笔宽度
        paintContent.setStrokeWidth(4);
        //设置抗锯齿
        paintContent.setAntiAlias(true);

        paint.setColor(context.getResources().getColor(R.color.gray));


        if(isOpen){
            contentColor = context.getResources().getColor(R.color.nitecore);
        }else{
            contentColor = Color.WHITE;
        }
    }


    public ChatStylesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false) ;
        paint = new Paint();

        paintContent = new Paint();
        //设置空心
        paintContent.setStyle(Paint.Style.FILL);
        //设置画笔宽度
        paintContent.setStrokeWidth(2);
        //设置抗锯齿
        paintContent.setAntiAlias(true);


        paint.setColor(context.getResources().getColor(R.color.gray));


        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.chatStyles);
        isOpen = typedArray.getBoolean(R.styleable.chatStyles_messageStyles,isOpen);
        typedArray.recycle();

        if(isOpen){
            contentColor = context.getResources().getColor(R.color.nitecore);
        }else{
            contentColor = Color.WHITE;
        }




    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //设置空心
        paint.setStyle(Paint.Style.FILL);
        //设置画笔宽度
        paint.setStrokeWidth(2);
        //设置抗锯齿
        paint.setAntiAlias(true);

        Rect r = new Rect();

        r.bottom =getHeight();
        r.right =getWidth();
        r.top = 0;
        r.left = 0;

        //补足边框
        float rectWidth = 1.0f;

        float contentWidth = 2.0f;

        int radius = 10;

        int contentRaius = 8;


        if(isOpen){

            Path pathRect = new Path();

            Path pathPolygon = new Path();

            RectF rectF = new RectF();

            rectF.left = r.left + rectWidth;
            rectF.right = r.right - rectWidth - 20;
            rectF.top = r.top + rectWidth ;
            rectF.bottom = r.bottom - rectWidth;

            //添加一个圆角矩形
            pathRect.addRoundRect(rectF,radius,radius,Path.Direction.CCW);

            //画一个多边形
            pathPolygon.moveTo(rectF.right, 20);// 此点为多边形的起点
            pathPolygon.lineTo(rectF.right + 20, 30);
            pathPolygon.lineTo(rectF.right, 40);

            //使重叠部分隐藏
            pathRect.op(pathPolygon,Path.Op.XOR);

            canvas.drawPath(pathRect, paint);


            Path path1 = new Path();

            Path path2 = new Path();


            RectF rect = new RectF();

            rect.left = rectF.left + contentWidth;
            rect.right =rectF.right - contentWidth;
            rect.top =rectF.left + contentWidth;
            rect.bottom = rectF.bottom - contentWidth;

            //添加一个圆角矩形
            path1.addRoundRect(rect,contentRaius,contentRaius,Path.Direction.CCW);

            //画一个多边形
            path2.moveTo(rect.right, 20+2);// 此点为多边形的起点
            path2.lineTo(rect.right + 20, 30);
            path2.lineTo(rect.right, 40-2);


            //使重叠部分隐藏
            path1.op(path2,Path.Op.XOR);

            paintContent.setColor(contentColor);

            canvas.drawPath(path1, paintContent);



        }else{


            Path pathRect = new Path();

            Path pathPolygon = new Path();

            RectF rectF = new RectF();

            rectF.left = r.left + rectWidth + 20;
            rectF.right = r.right - rectWidth;
            rectF.top = r.top + rectWidth ;
            rectF.bottom = r.bottom - rectWidth;

            //添加一个圆角矩形
            pathRect.addRoundRect(rectF,radius,radius,Path.Direction.CCW);

            //画一个多边形
            pathPolygon.moveTo(rectF.left, 20);// 此点为多边形的起点
            pathPolygon.lineTo(rectF.left - 20, 30);
            pathPolygon.lineTo(rectF.left, 40);

            //使重叠部分隐藏
            pathRect.op(pathPolygon,Path.Op.XOR);

            canvas.drawPath(pathRect, paint);



            Path path1 = new Path();

            Path path2 = new Path();


            RectF rect = new RectF();

            rect.left = rectF.left + contentWidth;
            rect.right =rectF.right - contentWidth;
            rect.top =rectF.top + contentWidth;
            rect.bottom = rectF.bottom - contentWidth;

            //添加一个圆角矩形
            path1.addRoundRect(rect,contentRaius,contentRaius,Path.Direction.CCW);

            //画一个多边形
            path2.moveTo(rect.left, 20+2);// 此点为多边形的起点
            path2.lineTo(rect.left - 20, 30);
            path2.lineTo(rect.left, 40-2);


            //使重叠部分隐藏
            path1.op(path2,Path.Op.XOR);

            paintContent.setColor(contentColor);

            canvas.drawPath(path1, paintContent);


        }




    }


}
