package com.sms.app.interphone.util.qrcodeutil;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.sms.app.interphone.R;
import com.sms.app.interphone.ui.activity.WiActivity;

import java.util.Vector;


/**
 * @author VincenT Li
 *	处理照相机接收到的消息，赋予状态
 */
public final class CaptureActivityHandler extends Handler {

  private final WiActivity activity;
  private final DecodeThread decodeThread;
  private State state;


  public CaptureActivityHandler(WiActivity activity, Vector<BarcodeFormat> decodeFormats, String characterSet) {
    this.activity = activity;
    decodeThread = new DecodeThread(activity, decodeFormats, characterSet,new DecodeResultPointCallback(activity.getScanView()));
    decodeThread.start();
    state = State.SUCCESS;//必须先赋值状态，否则不会自动聚焦
    CameraManager.get().startPreview();
    startPreview();
  }

  @Override
  public void handleMessage(Message message) {
    switch (message.what) {
      case R.id.auto_focus:
        if (state == State.PREVIEW) {
          CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
        }
        break;
      case R.id.decode_succeeded:
        state = State.SUCCESS;
        Bundle bundle = message.getData();
        Bitmap barcode = bundle == null ? null :(Bitmap) bundle.getParcelable(DecodeThread.BARCODE_BITMAP);
        activity.handleDecode((Result) message.obj, barcode);
        break;
      case R.id.decode_failed:
        state = State.PREVIEW;
        CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
        break;
        default:
            break;
    }
  }
  /**退出预览窗*/
  public void quitPreview() {
    state = State.DONE;
    CameraManager.get().stopPreview();
    Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
    quit.sendToTarget();
    try {
      decodeThread.join();
    } catch (Exception e) {
    	e.printStackTrace();
    }
    removeMessages(R.id.decode_succeeded);
    removeMessages(R.id.decode_failed);
  }
  	/**开启预览窗*/
	private void startPreview() {
		if (state == State.SUCCESS) {
			state = State.PREVIEW;
			CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
			CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
			activity.startScanView();
    	}
	}
	
	/**
	 * @author VincenT Li
	 *	@param PREVIEW 显示预览窗
	 *	@param SUCCESS 扫描完成
	 *	@param DONE 扫描结束或暂停
	 */
	private enum State {
		PREVIEW,
		SUCCESS,
		DONE
	}
}
