package com.sms.app.interphone.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sms.app.interphone.R;
import com.sms.app.interphone.util.interfaces.NoDoubleClickListener;

public class DeviceActivity extends BaseActivity {

//    private ImageView img_connect;
    private ImageView img_back;
    private TextView tvTitle;
    private RelativeLayout chat_title_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        setView();

    }

    private void setView() {
    //    img_connect = (ImageView) findViewById(R.id.chat_title_imageview2);
        img_back = (ImageView) findViewById(R.id.chat_title_imageview1);
        tvTitle = (TextView) findViewById(R.id.chat_title_text);
        chat_title_layout= (RelativeLayout)findViewById(R.id.chat_title_layout);
        chat_title_layout.setVisibility(View.GONE);

        tvTitle.setOnClickListener(new NoDoubleClickListener(){
            @Override
            public void onNoDoubleClick(View v) {
                startActivity(new Intent(DeviceActivity.this,BlescanActivity.class));
            }
        });

        /*img_connect.setOnClickListener(new NoDoubleClickListener(){
            @Override
            public void onNoDoubleClick(View v) {
                startActivity(new Intent(DeviceActivity.this,BlescanActivity.class));
            }
        });*/
        img_back.setOnClickListener(new NoDoubleClickListener(){
            @Override
            public void onNoDoubleClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        return super.onKeyDown(keyCode, event);
    }

}
