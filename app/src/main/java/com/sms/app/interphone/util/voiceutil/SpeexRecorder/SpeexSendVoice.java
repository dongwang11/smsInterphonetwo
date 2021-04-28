package com.sms.app.interphone.util.voiceutil.SpeexRecorder;

import android.os.Handler;
import android.os.Message;

import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.interphone.entity.Voice;
import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.interphone.util.msnutil.ExceptionUtil;
import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * 录音数据回调线程
 *
 * */

public class SpeexSendVoice implements Runnable {

	private final Object mutex = new Object();

	private volatile boolean isRecording;

	private List<byte[]> list = new ArrayList<>();

	private long startTime = 0;

	private long packId = 0;

	private Handler mHandler = null;
	int ak = 0;

	private String fileName;
	private boolean stop;





	public SpeexSendVoice(Handler mHandler, long packId,String fileName) throws FileNotFoundException {
		this.mHandler = mHandler;
		this.packId = packId;
		this.fileName = fileName;

	}

	public void run() {
	//	LogUtil.i(GlobalConsts.TAG,"SpeexSendVoice");

		while (this.isRecording() || list.size() > 0) {



			if (list.size() > 0) {

				//录音时间大于600毫秒

				if(startTime + 600 < System.currentTimeMillis()){

					stop = true;

					byte[] pData = list.remove(0);

					//	BluetoothLeService.le_service.sendWriteByte(Objcode.FRIDY,pData);

						/*byte prica = (byte) (1 | 0x80);

						byte [] voiceData = ATcommand.at_set_file_user_chat_versions(prica,MyApplicatoin.evenUser.getId(),packId,pData);

						BluetoothLeService.le_service.sendWriteByte(Objcode.MESSAGE,voiceData);*/


					Message mes = new Message();

					mes.what = GlobalConsts.VOICE_BLE;

					Voice voice = new Voice();

					voice.setPackID(packId);
					if(MyApplicatoin.evenUser != null){
						voice.setUserID(MyApplicatoin.evenUser.getId());
					}else{
						//voice.setUserID(22L);
					}

					voice.setBytes(pData);
					voice.setFireName(fileName);
					mes.obj = voice;

					mHandler.sendMessage(mes);



				}else{

					if(!this.isRecording()){
						list.clear();
					}

				}

			} else {
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					ExceptionUtil.handleException(e);
				}
			}

		}

		if(stop){

			Message mes = new Message();

			mes.what = GlobalConsts.VOICE_CODE;

			Voice voice = new Voice();

			voice.setPackID(packId);
			if(MyApplicatoin.evenUser != null){
				voice.setUserID(MyApplicatoin.evenUser.getId());
			}else{
				//voice.setUserID(22L);
			}
			voice.setFireName(fileName);
			voice.setBytes(new byte[0]);

			mes.obj = voice;

			mHandler.sendMessage(mes);

			stop = false;


		}



	}

	public void putData(final byte[] buf, long time) {
		list.add(buf);
		this.startTime = time;
	}

	public void setRecording(boolean isRecording) {
		synchronized (mutex) {
			this.isRecording = isRecording;
			if (this.isRecording) {
				mutex.notify();
			}
		}
	}

	public boolean isRecording() {
		synchronized (mutex) {
			return isRecording;
		}
	}

}
