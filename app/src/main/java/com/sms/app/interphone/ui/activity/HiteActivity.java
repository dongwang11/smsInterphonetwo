package com.sms.app.interphone.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.interphone.R;
import com.sms.app.interphone.util.msnutil.ExceptionUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class HiteActivity extends BaseActivity {

    private ImageView imageIcon;
    private ImageView imageView;
    private String PrecursorString[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hite);

        Intent intent = getIntent();

        String acion = intent.getAction();

        imageView = (ImageView) findViewById(R.id.hite_imageview);
        imageIcon = (ImageView) findViewById(R.id.hite_main_icon);




        imageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });



        SharedPreferences sharedPreferences = HiteActivity.this.getSharedPreferences(GlobalConsts.PRECURSOR, Context.MODE_PRIVATE);

        PrecursorString = new String[]{
                sharedPreferences.getString(GlobalConsts.PRECURSOR_FIRMWARE_VERSION, ""),
                sharedPreferences.getString(GlobalConsts.PRECURSOR_FIRMWARE_URL, ""),
                sharedPreferences.getString(GlobalConsts.PRECURSOR_USER_GUI_VERSION, ""),
                sharedPreferences.getString(GlobalConsts.PRECURSOR_USER_GUI_URL, ""),
                sharedPreferences.getString(GlobalConsts.PRECURSOR_QA_VERSION, ""),
                sharedPreferences.getString(GlobalConsts.PRECURSOR_QA_URL, ""),

        };

        if(acion != null){

            if(acion.equals(GlobalConsts.ACTION_ABOUT)){

                Bitmap bitmap = getLoacalBitmap(PrecursorString[3]); //从本地取图片(在cdcard中获取)

                imageView.setImageBitmap(bitmap);

            }else if(acion.equals(GlobalConsts.ACTION_FAQ)){

                Bitmap bitmap = getLoacalBitmap(PrecursorString[5]); //从本地取图片(在cdcard中获取)

                imageView.setImageBitmap(bitmap);

            }

        }

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
