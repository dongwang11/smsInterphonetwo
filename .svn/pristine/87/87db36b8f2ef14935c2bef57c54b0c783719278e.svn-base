package com.sms.app.interphone.util.voiceutil.SpeexRecorder;

import android.os.Handler;
import android.os.Message;
import android.util.Base64;

import com.sms.app.framework.communication.localayer.MsgResponse;
import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.interphone.util.msnutil.ExceptionUtil;
import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.framework.communication.localayer.cmd.Ble_message;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 *
 * 将录音数据写入文件
 * */
public class SpeexOut implements Runnable {

	private final Object mutex = new Object();

	private String fileName;

	private OutputStream out;

	private long packId = System.currentTimeMillis();
	private volatile boolean isRecording;
	private processedData pData;
	private List<processedData> list;
	private List<Byte> voice_list = new ArrayList<Byte>();
	private Handler mHandler = null;

	private long startTime = 0;



	public SpeexOut(Handler mhandler, long packId, String fileName) throws FileNotFoundException {
		super();
		this.mHandler = mhandler;
		this.fileName = fileName;
		this.packId = packId;

		list = Collections.synchronizedList(new LinkedList<processedData>());
		open(new File(this.fileName));

	}

	private void open(File file) throws FileNotFoundException {
		out = new FileOutputStream(file);
	}

	public void run() {
		LogUtil.i(GlobalConsts.TAG,"SpeexOut");


		while (this.isRecording() || list.size() > 0) {

			if(startTime + 1000 < System.currentTimeMillis()){

				try {
					if (list.size() > 0) {
						pData = list.remove(0);
						for(byte b : pData.processed){
							voice_list.add(b);
						}
						out.write(pData.processed);
					} else {
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							ExceptionUtil.handleException(e);
						}
					}

				} catch (IOException e) {
					ExceptionUtil.handleException(e);
				}

			}else{

				if(!this.isRecording()){
					list.clear();
				}

				File file = new File(fileName);
				if (file.isFile() && file.exists()) {
					file.delete();
				}


			}

		}
		stop();
	}

	public void putData(final byte[] buf, int size, long time) {

//		log.debug("after convert.size=====================[640]:" + size);

		processedData data = new processedData();
		//data.ts = ts;
		data.size = size;
		data.processed=new byte[buf.length];

		System.arraycopy(buf, 0, data.processed, 0, size);
		list.add(data);
		this.startTime = time;
	}

	public void stop() {
		LogUtil.i(GlobalConsts.TAG,"stop");
		try {
			if(voice_list.size() != 0){
				byte [] fileData=new byte[voice_list.size()];
				for(int i = 0; i <voice_list.size(); i++){
					fileData[i]=voice_list.get(i);
				}

				String body= Base64.encodeToString(fileData, Base64.DEFAULT);

				Ble_message ble_message = new Ble_message();

				ble_message.setMessage(body);
				ble_message.setTime(System.currentTimeMillis());
				ble_message.setType(MsgResponse.Voice);
				if(MyApplicatoin.evenUser != null){
					ble_message.setUserId(MyApplicatoin.evenUser.getId());
				}else{
					//ble_message.setUserId(22L);
				}
				ble_message.setId(packId);
				ble_message.setFireName(fileName);

				Message mes = new Message();
				mes.what = GlobalConsts.VOICE;
				mes.obj = ble_message;

				mHandler.sendMessage(mes);

				out.close();
				voice_list.clear();

			}
		} catch (IOException e) {
			ExceptionUtil.handleException(e);
		}
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

	class processedData {
		//private long ts;
		private int size;
		private byte[] processed;
	}


}
