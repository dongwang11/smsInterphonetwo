package com.sms.app.interphone.ui.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sms.app.framework.communication.localayer.MsgResponse;
import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.framework.trans.bean.Interphone;
import com.sms.app.interphone.R;
import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.interphone.util.msnutil.ExceptionUtil;
import com.sms.app.interphone.util.msnutil.ImageCompress;
import com.sms.app.interphone.view.DragLayout;

import java.io.ByteArrayOutputStream;


/**
 * open fire 通讯Activity
 */

public class ChatActivity extends BaseActivity implements View.OnClickListener {

    private static String[] PERMISSIONS_STORAGE = {

    };





    //侧滑Layout
    private DragLayout dragLayout;




    //返回
    private ImageView img_return;

    //菜单
    private ImageView img_menu;



    private TextView tvGroupName;








    /*private LinearLayout unbundling;
    private LinearLayout empty;*/






    private static String TAG = "YanShi...Log - ChatActivity";





    /**
     * 图库选择照片后回调解析
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 100 && resultCode == this.RESULT_OK) {
                Uri imageUri = data.getData();
                ImageCompress.CompressOptions options = new ImageCompress.CompressOptions();
                options.maxHeight = 480;
                options.maxWidth = 480;
                options.uri = imageUri;

                ImageCompress imageCompress = new ImageCompress();
                Bitmap bitmap = imageCompress.compressFromUri(this, options);

                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

                byte[] imageData = byteArrayOutputStream.toByteArray();

                String body = Base64.encodeToString(imageData, Base64.DEFAULT);

                if(MyApplicatoin.IS_BLE){

                    Interphone interphone = new Interphone();

                    MsgResponse msg = new MsgResponse();

                    msg.setUserId(MyApplicatoin.evenUser.getId());
                    msg.setContent(body);
                    msg.setType(MsgResponse.Img);
                    msg.setFormName("5555");
                    msg.setPacketID(System.currentTimeMillis());

                    interphone.setMsgResponse(msg);

                    MyApplicatoin.setInterphone(interphone);

                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            ExceptionUtil.handleException(e);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        try {

            setContentView(R.layout.activity_chat);

            setupView();


            if(hasPermission(PERMISSIONS_STORAGE)){

            }else{

                requstPermissions(GlobalConsts.WRITE_EXTERNAL_STORAGE,PERMISSIONS_STORAGE);
            }

            LogUtil.i(TAG,"hasPermission:"+hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));

        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }







    private void setTextData() {
    //    stopTrace();
    }



    private void setupView() {



        dragLayout = (DragLayout) findViewById(R.id.chat_draglayout);

        dragLayout.setDragListener(new DragLayout.DragListener() {
            @Override
            public void onOpen() {

            }

            @Override
            public void onClose() {

            }

            @Override
            public void onDrag(float percent) {

            }
        });



        /*unbundling = (LinearLayout) findViewById(R.id.chat_draglayout_unbundling);
        empty = (LinearLayout) findViewById(R.id.chat_draglayout_empty);*/



        /*unbundling.setOnClickListener(this);
        empty.setOnClickListener(this);*/

    //    lv.setInterface(this);



       /* ptt_switch.setSlideListener(new SlideSwitch.SlideListener() {
            @Override
            public void open() {
                pTT.setOpen(true);
            }

            @Override
            public void close() {
                pTT.setOpen(false);
            }
        });

        pTT.setOnLongLayoutListener(new PttRelativeLayout.onLongLayoutListener() {
            @Override
            public void onLongLayout(MotionEvent event) {

            //    LogUtil.i(GlobalConsts.TAG,"Layout消费掉事件");
                if(isInterphone()){
                    setVoice(event);
                }else{

                    reset();
                    isTouch = false;

                    stopRecord();

                }

            }
        });*/



    }




    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LogUtil.i(TAG,"onDestroy");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.i(TAG,"onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();

        LogUtil.i(TAG,"onResume");


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){



        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.i(TAG,"onPause");
    }





    /*@Override
    public void downPullRefresh() {
        new AsyncTask<Void,Void,Void>(){


            @Override
            protected Void doInBackground(Void... params) {

                SystemClock.sleep(500);


                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);


            }

        }.execute(new Void[]{});
    }*/



    /*@Override
    public void onReflash() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                //获取最新数据
                setReflashData();
                //通知listview 刷新数据完毕；
                lv.reflashComplete();
            }
        }, 2000);
    }*/



    class ChatReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            LogUtil.i(TAG,"ChatReceiver"+intent.getAction());


        }
    }




    private String getFreq(int frequne){


        float b = frequne;
        //    DecimalFormat  df = new DecimalFormat("#.000");

        //   return new String(df.format(frequne/1000));
        return  String .format("%.3f",(b/1000000));

    }


    private String[] code_data = {"OFF","50.0", "55.5", "67.0", "69.3", "71.9", "74.4", "77.0", "79.7", "82.5", "85.4",
            "88.5", "91.5", "94.8", "97.4", "100.0", "103.5", "107.2", "110.9", "114.8", "118.8",
            "123.0", "127.3", "131.8", "136.5", "141.3", "146.2", "151.4", "156.7", "162.2", "167.9",
            "173.8", "179.9", "186.2", "192.8", "203.5", "210.7", "218.1", "225.7", "233.6", "241.8", "250.3","50.0", "55.5", "67.0", "69.3", "71.9", "74.4", "77.0", "79.7", "82.5", "85.4",
            "88.5", "91.5", "94.8", "97.4", "100.0", "103.5", "107.2", "110.9", "114.8", "118.8",
            "123.0", "127.3", "131.8", "136.5", "141.3", "146.2", "151.4", "156.7", "162.2", "167.9",
            "173.8", "179.9", "186.2", "192.8", "203.5", "210.7", "218.1", "225.7", "233.6", "241.8", "250.3"};


    private void playVoice(){

        /*new Thread(new Runnable() {
            @Override
            public void run() {


                Speex vDecoder = new Speex();
                vDecoder.init(vDecoder.getQuality());

                int sampleRate = 8000;

                int minBufferSize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
                AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize, AudioTrack.MODE_STREAM);

                int decsize = 0;

                byte[] payload = {19,-98,-30,11,-96,-18,-18,0,0,-12,79,-120,-63,-41,-36,
                        17,-117,-6,62,-123,106,-102,-92,35,69,-63,35,-35,-85,-72,
                        17,-117,-6,62,38,-46,-36,-18,-50,-78,-67,-21,77,-14,26,
                        20,-58,52,6,-91,93,45,1,110,111,25,1,-59,67,62,
                        19,-90,26,6,-125,7,-88,76,-1,-66,106,38,69,-17,80,
                        20,110,-24,6,98,-103,10,29,84,-49,-67,-22,-123,51,46,
                        20,110,-110,6,32,-88,-103,8,61,71,-90,125,-61,77,50,
                        20,110,-88,6,0,-9,-88,20,-49,-66,105,-90,69,-17,80,
                        20,110,-48,5,-64,-103,7,21,68,-56,-67,-22,5,50,-114,
                        18,-58,88,5,-95,-91,-97,21,84,72,-89,-34,-128,59,-36,
                };


                int i = 0;

                byte[] voice = new byte[15];

                while (true){
                    if(i >= payload.length-1){

                        i = 0;
                    }

                    for(int k = 0;k < 15;k++){

                        i++;

                        voice[k] = payload[i];


                    }
                    short[] decoded = new short[160];
                    //由于AudioTrack播放的是流，所以，我们需要一边播放一边读取


                    if ((decsize = vDecoder.decode(voice, decoded, 160)) > 0) {

                        //	LogUtil.i(GlobalConsts.TAG,"decoded:"+ Arrays.toString(decoded));

                        track.write(decoded, 0, decsize);
                        track.play();
                    }

                }
            }
        }).start();*/
    }
}
