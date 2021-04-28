package com.sms.app.interphone.util.frequentlyutil;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import com.sms.app.interphone.R;
import com.sms.app.interphone.ui.activity.MainActivity;

import static android.app.Notification.PRIORITY_MAX;

//保持后台运行
public class BackGroundService extends Service {
    private static final String CHANNEL_ID = "NFCService";
    Notification notification;
    private Context mContext;
    private static Thread uploadGpsThread;
    private MediaPlayer bgmediaPlayer;
    private boolean isrun = true;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mContext = this;


        if(Build.VERSION.SDK_INT >= 26){
                NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                NotificationChannel Channel = new NotificationChannel(CHANNEL_ID,"主服务",NotificationManager.IMPORTANCE_HIGH);
                Channel.enableLights(false);//设置提示灯
                //Channel.setLightColor(Color.RED);//设置提示灯颜色
                Channel.setShowBadge(true);//显示logo
                //  Channel.setDescription("ytzn");//设置描述
                Channel.setBypassDnd(false);
                Channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC); //设置锁屏可见 VISIBILITY_PUBLIC=可见
                manager.createNotificationChannel(Channel);

                Notification notification = new Notification.Builder(this)
                        .setChannelId(CHANNEL_ID)
                        .setContentTitle("Global Walkie")//标题
                        .setContentText("运行中...")//内容
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.icon_chat)//小图标一定需要设置,否则会报错(如果不设置它启动服务前台化不会报错,但是你会发现这个通知不会启动),如果是普通通知,不设置必然报错
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.icon_chat))
                        .build();
                startForeground(0,notification);//服务前台化只能使用startForeground()方法,不能使用 notificationManager.notify(1,notification); 这个只是启动通知使用的,使用这个方法你只需要等待几秒就会发现报错了

        }else {
                Intent notificationIntent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
                        notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                //1.通知栏占用，不清楚的看官网或者音乐类APP的效果
                notification = new Notification.Builder(mContext)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setWhen(System.currentTimeMillis())
                        .setContentTitle("Global Walkie")
                        .setContentText("运行中...")
                        .setOngoing(true)
                        .setPriority(PRIORITY_MAX)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(false)
                        .build();
                /*使用startForeground,如果id为0，那么notification将不会显示*/
                startForeground(0, notification);
        }
        //2.开启线程（或者需要定时操作的事情）
        if(uploadGpsThread == null){
            uploadGpsThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    //这里用死循环就是模拟一直执行的操作
                    while (isrun){

                        //你需要执行的任务
                        //doSomething();
                        try {
                            Thread.sleep(10000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        //3.最关键的神来之笔，也是最投机的动作，没办法要骗过CPU
        //这就是播放音乐类APP不被杀的做法，自己找个无声MP3放进来循环播放
        if(bgmediaPlayer == null){
            bgmediaPlayer = MediaPlayer.create(this,R.raw.bb);
            bgmediaPlayer.setLooping(true);
            bgmediaPlayer.start();
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        isrun = false;
        stopForeground(true);
        bgmediaPlayer.release();
        stopSelf();
        super.onDestroy();
    }
}
