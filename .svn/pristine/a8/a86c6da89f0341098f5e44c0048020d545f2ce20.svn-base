package com.sms.app.interphone.util.frequentlyutil;

import android.util.Log;


public class ChargerUtils {


/*    public static  int getNiMHProcessFormValtage(float vData){
        int process = 0;
        int vDataTem = (int)((vData*10) * 100);
        if( vDataTem >= 0 && vDataTem < 3400 ){                  //0---3.4V    0%----20%
            process = 20* ( vDataTem - 0 )/(3400-0);
        }else if( vDataTem >= 3400 && vDataTem < 3700 ){        //3.4V---3.7V   20%---40%
            process = 20 + 20* ( vDataTem - 3400 )/(3700-3400);
        }else if( vDataTem >= 3700 && vDataTem < 3900 ){        //3.7V---3.9V   40%---60%
            process = 40 + 20* ( vDataTem - 3700 )/(3900-3700);
        }else if( vDataTem >= 3900 && vDataTem < 4100 ){        //3.9V---4.1V   60%---80%
            process = 60 + 20* ( vDataTem - 3900 )/(4100-3900);
        }else if( vDataTem >= 4100 && vDataTem < 4210 ){       //4.1V--4.2V   80%---100%
            process = 80 + 20* ( vDataTem - 4100 )/(4200-4100);
        }else if( vDataTem >= 4210) {
            process = 100;
        }
        Log.d("resultString","vDataTem2222: " +vDataTem+"  process: "+process);
        return process;
    }*/


    public static  int getNiMHProcessFormValtage(float vData){
        int process = 0;
        int vDataTem = (int)((vData*10) * 100);
        if( vDataTem >= 0 && vDataTem < 2500 ){                  //0---2.5V    0%
            process = 20* ( vDataTem - 0 )/(2500-0);
        }else if( vDataTem >= 2500 && vDataTem < 3400 ){         //2.5---3.4V    0%----20%
            process = 20* ( vDataTem - 2500 )/(3400-2500);
        }else if( vDataTem >= 3400 && vDataTem < 3600 ){        //3.4V---3.6V   20%---40%
            process = 20 + 20* ( vDataTem - 3400 )/(3600-3400);
        }else if( vDataTem >= 3600 && vDataTem < 3800 ){        //3.6V---3.8V   40%---60%
            process = 40 + 20* ( vDataTem - 3600 )/(3800-3600);
        }else if( vDataTem >= 3800 && vDataTem < 4000 ){        //3.8V---4.0V   60%---80%
            process = 60 + 20* ( vDataTem - 3800 )/(4000-3800);
        }else if( vDataTem >= 4000 && vDataTem < 4150 ){       //4.0V--4.15V   80%---98%
            process = 80 + 18* ( vDataTem - 4000 )/(4150-4000);
        }else if( vDataTem >= 4150 && vDataTem < 4200 ){       //4.15V--4.2V   99%
            process = 99;
        }else {                                                //4.2V   100%
            process = 100;
        }
        //Log.d("resultString","vDataTem2222: " +vDataTem+"  process: "+process);
        return process;
    }


}
