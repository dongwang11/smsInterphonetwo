package com.sms.app.framework.communication.localayer.bledriver;

import android.util.Log;

import com.sms.app.interphone.ui.MyApplicatoin;



public class LogUtil {

	public static void i(String tag, Object msg) {

		if (!MyApplicatoin.isRelease) {

			Log.e(tag, String.valueOf(msg));

		}

	}

}
