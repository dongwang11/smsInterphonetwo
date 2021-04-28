package com.sms.app.interphone.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import com.sms.app.interphone.R;
import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.interphone.util.frequentlyutil.SharePreferenceConstant;
import com.sms.app.interphone.util.frequentlyutil.SharePreferenceUtil;


public class SilpLoginActivity extends BaseActivity implements View.OnClickListener{

    private LinearLayout ll_login,ll_skip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_silplogin);//activity_login  activity_referrer
        inview();
    }

    public void inview() {
        ll_login = (LinearLayout) findViewById(R.id.ll_login);
        ll_skip = (LinearLayout) findViewById(R.id.ll_skip);
        ll_login.setOnClickListener(this);
        ll_skip.setOnClickListener(this);

        String login = SharePreferenceUtil.getStringDataByKe(SilpLoginActivity.this, SharePreferenceConstant.LOGINSTATE, "");
        if ("loginstate".equals(login) || login == "loginstate") {

            if(MyApplicatoin.IS_BLE){
                Intent intent = new Intent(SilpLoginActivity.this, DeviceActivity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(SilpLoginActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }
            finish();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_login:
                SharePreferenceUtil.saveStringDataToSharePreference(SilpLoginActivity.this, SharePreferenceConstant.LOGIN, "login");
                startActivity(new Intent(SilpLoginActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.ll_skip:
                SharePreferenceUtil.saveStringDataToSharePreference(SilpLoginActivity.this, SharePreferenceConstant.LOGINUSER, "loginuser");
                startActivity(new Intent(SilpLoginActivity.this, WelcomeActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

}