package com.sms.app.interphone.services;

import android.app.Activity;

import com.sms.app.interphone.ui.activity.UpDataActivity;

import no.nordicsemi.android.dfu.DfuBaseService;

public class DfuService extends DfuBaseService {

    @Override
    protected Class<? extends Activity> getNotificationTarget() {
        return UpDataActivity.class;
    }

    @Override
    protected boolean isDebug() {
        // return BuildConfig.DEBUG;
        return true;
    }
}
