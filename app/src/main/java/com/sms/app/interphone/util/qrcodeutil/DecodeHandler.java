/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sms.app.interphone.util.qrcodeutil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.sms.app.interphone.R;
import com.sms.app.interphone.ui.activity.WiActivity;

import java.util.Hashtable;

/**
 * @author VincenT Li
 *	解码线程handler
 */
final class DecodeHandler extends Handler {


	private final WiActivity activity;
	private final MultiFormatReader multiFormatReader;

	DecodeHandler(WiActivity activity, Hashtable<DecodeHintType, Object> hints) {
		multiFormatReader = new MultiFormatReader();
		multiFormatReader.setHints(hints);
		this.activity = activity;
	}

	@Override
	public void handleMessage(Message message) {
		switch (message.what) {
		case R.id.decode:
			Log.i("MSG", "开始"+ String.valueOf(System.currentTimeMillis()));
			decode((byte[]) message.obj, message.arg1, message.arg2);
			break;
		case R.id.quit:
			Looper.myLooper().quit();
			break;

			default:
				break;
		}
	}

	/**
	 * 解码取景器矩形内数据。为了提高效率，将同一个读写器对象从一个解码到下一个解码。
	 * @param data The YUV preview frame.
	 * @param width The width of the preview frame.
	 * @param height The height of the preview frame.
	 */
	private void decode(byte[] data, int width, int height) {
		Result rawResult = null;
		byte[] rotatedData = new byte[data.length];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++)
				rotatedData[x * height + height - y - 1] = data[x + y * width];
		}
		int tmp = width;
		width = height;
		height = tmp;
		YUVLuminanceSource source = CameraManager.get().getLuminanceSource(rotatedData, width, height);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		try {
			rawResult = multiFormatReader.decodeWithState(bitmap);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			multiFormatReader.reset();
		}
		//返回解码结果
		if (rawResult != null) {
//			activity.showLoading();
			Log.i("MSG", rawResult.toString());
			Message message = Message.obtain(activity.getHandler(),R.id.decode_succeeded, rawResult);
			Bundle bundle = new Bundle();
			bundle.putParcelable(DecodeThread.BARCODE_BITMAP,source.renderCroppedGreyscaleBitmap());
			message.setData(bundle);
			message.sendToTarget();//发送至CaptureActivityHandler
		} else {
			Message message = Message.obtain(activity.getHandler(),R.id.decode_failed);
			message.sendToTarget();//发送至CaptureActivityHandler-decode_failed继续解码
		}
	}

}
