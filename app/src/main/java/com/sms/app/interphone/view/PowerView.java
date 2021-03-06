package com.sms.app.interphone.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.interphone.R;

public class PowerView extends View {

    private static String TAG = "YanShi...Log - PowerView";

    private Paint paintVolt;

    private Paint paintContent;

    private Paint paintHite;

    private int volt = 0;

    private short leng = 4200-3000;


    public PowerView(Context context) {
        super(context);
        init(context);
    }

    public PowerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

        setWillNotDraw(false) ;

        paintVolt = new Paint();

        paintVolt.setStyle(Paint.Style.STROKE);
        //设置画笔宽度
        paintVolt.setStrokeWidth(3);
        //设置抗锯齿
        paintVolt.setAntiAlias(true);

        paintVolt.setColor(getResources().getColor(R.color.henqianhuise));


        paintHite = new Paint();

        paintHite.setStyle(Paint.Style.FILL);
        //设置画笔宽度
        paintHite.setStrokeWidth(10);
        //设置抗锯齿
        paintHite.setAntiAlias(true);

        paintHite.setColor(getResources().getColor(R.color.henqianhuise));



        paintContent = new Paint();
        //设置实心
        paintContent.setStyle(Paint.Style.FILL);
        //设置画笔宽度
        paintContent.setStrokeWidth(1);
        //设置抗锯齿
        paintContent.setAntiAlias(true);

        paintContent.setColor(getResources().getColor(R.color.henqianhuise));


    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect r = new Rect();


        r.bottom =getHeight()-2;
        r.right = getWidth() - ((getWidth()/8));
        r.top = 2;
        r.left = 2;
        canvas.drawRect(r,paintVolt);

        Rect rectHite = new Rect();

        rectHite.top = r.bottom/4;
        rectHite.bottom = r.bottom - (r.bottom/4);
        rectHite.left = r.right + 8;
        rectHite.right = getWidth();

        canvas.drawRect(rectHite, paintHite);


        Rect rectVolt = new Rect();

        int distance = r.bottom/6;

        int lenth = r.right - distance;

        rectVolt.top = r.top + distance;
        rectVolt.bottom = r.bottom - distance;
        rectVolt.left = r.left + distance;

        int rate = 0;

        if(this.volt > 0){

            rate =(int)(lenth*((double)this.volt/100));
        }

    //    LogUtil.i(TAG,"rectVolt:"+rate);

        if(rate > 0){

            if(rate < 20){
                paintContent.setColor(getResources().getColor(R.color.red));
            }else{
                paintContent.setColor(getResources().getColor(R.color.chengse));
            }

            rectVolt.right = rate;
        }else{
            rectVolt.right = lenth;
        }


        canvas.drawRect(rectVolt, paintContent);

    }

    public void setPower(Short vo){

       /* if(vo < 3000){

            vo = 3000;

        }
        if(vo > 4200){

            vo = 4200;

        }
        float vData = (float)vo /1000;
        int valtate = 0;
        int vDataTem = (int)((vData*10) * 100);

        if( vData >= 0 && vData < 3.4 ){          //0-3.4 ;20%
            valtate = 20* ( vDataTem - 0 )/(3400-0);
        }else if( vData >= 3.4 && vData < 3.7 ){  //3.4v---3.7v 20%--40%
            valtate = 20 + 20* ( vDataTem - 3400 )/(3700-3400);
        }else if( vData >= 3.7 && vData < 3.9 ){  //3.7v---3.9v 40%--60%
            valtate = 40 + 20* ( vDataTem - 3700 )/(3900-3700);
        }else if( vData >= 3.9 && vData < 4.1 ){  //3.9v---4.1v 60%--80%
            valtate = 60 + 20* ( vDataTem - 3900 )/(4100-3900);
        }else if( vData >= 4.1 && vData < 4.21 ){  //4.1v---4.2v 80%--100%
            valtate = 80 + 20* ( vDataTem - 4100 )/(4200-4100);
        }else if(vData >= 4.21){
            valtate = 100;
        }

        this.volt = valtate;*/

        this.volt =(int)(vo);
        //Log.d("resultString","this.volt :     "+ this.volt );
        // 更新界面
        postInvalidate();
    }

    /*public static  int getLionProcessFormValtage(IntValue setupV, float vData){
        int valtate = 0;
        int vDataTem = (int)((vData*10) * 100);
        if(setupV != null){
            switch (setupV.getData()){
                case 4200:
                    if( vData >= 0 && vData < 3.4 ){    //0-3.4 ;20%
                        valtate = 20* ( vDataTem - 0 )/(3400-0);
                    }else if( vData >= 3.4 && vData < 3.7 ){  //3.4v---3.7v 20%--40%
                        valtate = 20 + 20* ( vDataTem - 3400 )/(3700-3400);
                    }else if( vData >= 3.7 && vData < 3.9 ){  //3.7v---3.9v 40%--60%
                        valtate = 40 + 20* ( vDataTem - 3700 )/(3900-3700);
                    }else if( vData >= 3.9 && vData < 4.1 ){  //3.9v---4.1v 60%--80%
                        valtate = 60 + 20* ( vDataTem - 3900 )/(4100-3900);
                    }else if( vData >= 4.1 && vData < 4.21 ){  //4.1v---4.2v 80%--100%
                        valtate = 80 + 20* ( vDataTem - 4100 )/(4200-4100);
                    }
                    break;
                case 4350:
                    if( vData >= 0 && vData < 3.55 ){    //0-3.55 ;20%
                        valtate = 20* ( vDataTem - 0 )/(3550-0);
                    }else if( vData >= 3.55 && vData < 3.85 ){  //3.55v---3.85vΪ20%--40%
                        valtate = 20 + 20* ( vDataTem - 3550 )/(3850-3550);
                    }else if( vData >= 3.85 && vData < 4.05 ){  //3.85v---4.05vΪ40%--60%
                        valtate = 40 + 20* ( vDataTem - 3850 )/(4050-3850);
                    }else if( vData >= 4.05 && vData < 4.25 ){  //4.05v---4.25vΪ60%--80%
                        valtate = 60 + 20* ( vDataTem - 4050 )/(4250-4050);
                    }else if( vData >= 4.25 && vData < 4.351 ){  //4.25v---4.35vΪ80%--100%
                        valtate = 80 + 20* ( vDataTem - 4250 )/(4350-4250);
                    }
                    break;
                case 3700:
                    if( vData >= 0 && vData < 3.2 ){    //0-3.2 ;20%
                        valtate = 20* ( vDataTem - 0 )/(3200-0);
                    }else if( vData >= 3.2 && vData < 3.4 ){  //3.2v---3.4vΪ20%--40%
                        valtate = 20 + 20* ( vDataTem - 3200 )/(3400-3200);
                    }else if( vData >= 3.4 && vData < 3.6 ){  //3.4v---3.6vΪ40%--60%
                        valtate = 40 + 20* ( vDataTem - 3400 )/(3600-3400);
                    }else if( vData >= 3.6 && vData < 3.7 ){  //3.6v---3.7vΪ60%--80%
                        valtate = 60 + 20* ( vDataTem - 3600 )/(3700-3600);
                    }else if( vData >= 3.7 && vData < 3.751 ){  //3.7v---3.75vΪ80%--100%
                        valtate = 80 + 20* ( vDataTem - 3700 )/(3750-3700);
                    }
                    break;
                default:
                    break;
            }
        }
        Log.i("LionProcess", setupV.getData() + " LionProcess " + vData +" "+ vDataTem + " valtate:" + valtate);
        return valtate;
    }

    public static  int getNiMHProcessFormValtage(float vData){
        if( vData >= 0 && vData < 1.2 ){    //0-1.2 ;20%
            return (int)(20*(vData-0)/(1.2-0));
        }else if( vData >= 1.2 && vData < 1.35 ){  //1.2v---1.35vΪ20%--40%
            return (int)(20 + 20*(vData-1.2)/(1.35-1.2));
        }else if( vData >= 1.35 && vData < 1.4 ){  //1.35v---1.4vΪ40%--60%
            return (int)(40 + 20*(vData-1.35)/(1.4-1.35));
        }else if( vData >= 1.4 && vData < 1.45 ){  //1.4v---1.45vΪ60%--80%
            return (int)(60 + 20*(vData-1.4)/(1.45-1.4));
        }else if( vData >= 1.45 && vData < 1.481 ){  //1.45v---1.48vΪ80%--100%
            return (int)(80 + 20*(vData-1.45)/(1.48-1.45));
        }else {
            return 0;
        }
    }*/
}
