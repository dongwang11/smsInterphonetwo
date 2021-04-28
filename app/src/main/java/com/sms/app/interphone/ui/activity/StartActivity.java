package com.sms.app.interphone.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sms.app.interphone.R;
import com.sms.app.interphone.util.frequentlyutil.SharePreferenceConstant;
import com.sms.app.interphone.util.frequentlyutil.SharePreferenceUtil;

public class StartActivity extends BaseActivity {
    public static final String TAG = "start";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = View.inflate(this, R.layout.start_layout, null);
        this.setContentView(view);
        redirectTo();
    }

    protected void redirectTo() {

        String welcome = SharePreferenceUtil.getStringDataByKe(StartActivity.this,
                SharePreferenceConstant.WELCOME, "");
        if ("".equals(welcome) || welcome == null) {
            SharePreferenceUtil.saveStringDataToSharePreference(
                    StartActivity.this, SharePreferenceConstant.WELCOME, "welcome");
            Intent intent = new Intent(StartActivity.this, WelcomeViewpageActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(StartActivity.this, SilpLoginActivity.class);
            startActivity(intent);
        }

         finish();
    }


}
