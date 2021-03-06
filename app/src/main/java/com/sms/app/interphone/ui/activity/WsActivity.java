package com.sms.app.interphone.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.interphone.R;
import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.interphone.util.aesutil.EncryptUtil;
import com.sms.app.interphone.util.msnutil.ExceptionUtil;
import com.sms.app.interphone.util.msnutil.Tools;
import com.sms.app.interphone.util.qrcodeutil.EncodingUtil;
import com.sms.app.interphone.view.CircleImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.Arrays;


/**
 * 生成二维码Activity
 */

public class WsActivity extends BaseActivity {
    private static String TAG = "YanShi...Log - WsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ws);

        Intent intent = getIntent();

        try {
            if (intent != null) {

                final String name = intent.getStringExtra(GlobalConsts.DATE_FROM_NAME);
                final String theNum = intent.getStringExtra(GlobalConsts.DATE_FROM_ID);
                final String UUID = intent.getStringExtra(GlobalConsts.DATE_FROM_UUID);
                final String nameHite = MyApplicatoin.interphone.getImCode().getHite();


                TextView tvName = (TextView) findViewById(R.id.chat_name);

                CircleImageView imgIcon = (CircleImageView) findViewById(R.id.chat_icon);

            //    String aName = name.substring(0,14);

                byte[] code = Tools.getDataCode(name);

                String fromName = Tools.getGroupName(name);

                String bName = name.substring(14,name.length());

             //   String wsName = "SMS-CODE"+name+","+theNum+","+UUID;

                String freq = getCodeString(code);

                String wsName =freq+","+fromName+","+bName+","+theNum+","+UUID;
                //Log.d("dddd","freq       "+freq+ EncryptUtil.bytesToHexString(code)+ "  "+Arrays.toString(code));
                LogUtil.i(TAG,"房间名字2:"+ wsName);

            //    LogUtil.i(TAG,"房间名字3:"+ a.length());

                String[] strArray = wsName.split(",");

                for(int i=0;i<strArray.length;i++)
                {
                    LogUtil.i(TAG,"房间名字4:"+strArray[i]);
                }

                tvName.setText(nameHite);

                try {

                    if(MyApplicatoin.evenUser != null){

                        Bitmap bitmap = getLoacalBitmap(MyApplicatoin.evenUser.getAvatar_url()); //从本地取图片(在cdcard中获取)

                        imgIcon.setImageBitmap(bitmap);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                ImageView image = (ImageView) findViewById(R.id.QRCode_bitmap);

                Bitmap qrCodeBitmap = EncodingUtil.createQRCode(wsName, 800);

                if (qrCodeBitmap != null) {

                    image.setImageBitmap(qrCodeBitmap);

                } else {
                    Toast.makeText(WsActivity.this, "生成二维码失败！", Toast.LENGTH_SHORT).show();
                }



                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                    }
                });
            }
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }


    }

    private String getCodeString(byte[] code) {

        String sCode = null;

        if(code.length >= 6){

            int freq = (int) ((code[2] & 0xFF)
                    | ((code[3] & 0xFF)<<8)
                    | ((code[4] & 0xFF)<<16)
                    | ((code[5] & 0xFF)<<24));

            sCode = String.valueOf(code[0])+","+String.valueOf(code[1])+","+String.valueOf(freq);
        }

        return sCode;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 加载本地图片
     * http://bbs.3gstdy.com
     * @param url
     * @return
     */
    public Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            ExceptionUtil.handleException(e);
            return null;
        }
    }


}






