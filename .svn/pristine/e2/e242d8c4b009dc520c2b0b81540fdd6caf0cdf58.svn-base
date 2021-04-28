package com.sms.app.interphone.util.voiceutil.voice;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.interphone.R;
import com.sms.app.interphone.ui.MyApplicatoin;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Administrator on 2018/2/11.
 */

public class VoicePlayUtil {


    private Handler handler;

    private ImageView imageView;

    private Timer timer = new Timer();
    private TimerTask timerTask;

    private int i;

    private Drawable path;

    private int modelType = 1;//类型

    private long userID;

    private int[] leftVoiceBg = new int[] { R.mipmap.voice_1_left, R.mipmap.voice_2_left, R.mipmap.voice_3_left };
    private int[] rigthVoiceBg = new int[] { R.mipmap.voice_1_right, R.mipmap.voice_2_right, R.mipmap.voice_3_right };

    public VoicePlayUtil(Handler handler,ImageView img) {
        super();
        this.handler = handler;
        this.imageView = img;
        this.path = imageView.getDrawable();
    }

    public void voicePlay() {
        if (imageView == null) {
            return;
        }
        i = 0;
        timerTask = new TimerTask() {

            @Override
            public void run() {
                LogUtil.i(GlobalConsts.TAG,"IMGID:voicePlay:"+imageView.toString());
                if (imageView != null) {

                    if(userID == MyApplicatoin.evenUser.getId()){
                        changeBg(rigthVoiceBg[i % 3], false,2);
                    }else{
                        changeBg(leftVoiceBg[i % 3], false,1);
                    }


                }
                else {
                    return;
                }
                i++;
            }
        };
        timer.schedule(timerTask, 0, 300);
    }

    public void stopPlay() {

        if (imageView != null) {
            LogUtil.i(GlobalConsts.TAG,"IMGID:stopPlay:"+imageView.toString());
            if(userID == MyApplicatoin.evenUser.getId()){
                changeBg(rigthVoiceBg[0], true,2);
            }else{
                changeBg(leftVoiceBg[0], true,1);
            }

            if (timerTask != null) {
                timerTask.cancel();
            }
        }
    }

    private void changeBg(final int id, final boolean isStop,int k) {

        Message message = new Message();
        message.what = 1;
        message.obj = imageView;
        message.arg1 = id;
        message.sendingUid = k;
        if(isStop){
            message.arg2 = 1;
        }else{
            message.arg2 = 2;
        }
        handler.sendMessage(message);

        /*handler.post(new Runnable() {
            @Override
            public void run() {
                if (isStop) {

                    imageView.setImageDrawable(path);

                } else {

                    imageView.setImageResource(id);

                }
            }
        });*/
    }

    public void setImageView(long user_id) {
        this.userID = user_id;

    }

}
