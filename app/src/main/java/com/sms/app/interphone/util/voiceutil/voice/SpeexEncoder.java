package com.sms.app.interphone.util.voiceutil.voice;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.interphone.util.msnutil.ExceptionUtil;
import com.sms.app.interphone.util.voiceutil.SpeexRecorder.SpeexOut;
import com.sms.app.interphone.util.voiceutil.SpeexRecorder.SpeexSendVoice;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Gauss 语音压缩线程
 * 
 */
public class SpeexEncoder implements Runnable {

	private final Object mutex = new Object();

	private Speex v = new Speex();

	private List<ReadData> list = null;
	private volatile boolean isRecording;

	private long time = 0;

	private String fileName;

	private long packId;

	//压缩后的语音数据
	private byte[] processedData = new byte[15];


	private int length=0;
	private List<Byte> list_data = new ArrayList<Byte>();

	private Handler mHandler = null;

	private Context context = null;


	public SpeexEncoder(Context context, Handler mhandler, long packId, String fileName, int quality) {
		this.mHandler = mhandler;
 		this.context = context;
		v.init(quality);
		list = Collections.synchronizedList(new LinkedList<ReadData>());
		this.fileName = fileName;
		this.packId = packId;
		time = System.currentTimeMillis();
	}
	

	public void run() {

		//录音数据写入文件
		SpeexOut fileWriter = null;
		try {
			fileWriter = new SpeexOut(this.mHandler,packId,fileName);
			Log.i("TAG","文件名字="+fileName);
			Thread consumerThread = new Thread((Runnable) fileWriter);
			fileWriter.setRecording(true);
			consumerThread.start();

			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
		} catch (FileNotFoundException e) {
			ExceptionUtil.handleException(e);
		}

		//录音数据封装回调
		SpeexSendVoice sendThread = null;
		try {
			sendThread = new SpeexSendVoice(this.mHandler,packId,fileName);
			Thread consumerThread = new Thread((Runnable) sendThread);
			sendThread.setRecording(true);
			consumerThread.start();

			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
		} catch (FileNotFoundException e) {
			ExceptionUtil.handleException(e);
		}


		int getSize = 0;
		while (this.isRecording()) {

			if (list.size() == 0) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}

			if (list.size() > 0) {


				try {
					synchronized (mutex) {

                        ReadData rawdata = list.remove(0);

                        //压缩
                        getSize = v.encode(rawdata.ready, 0, processedData, rawdata.size);

                    }

					if (getSize > 0) {


                        fileWriter.putData(processedData, getSize,time);

                        byte[] data = new byte[processedData.length];
                        System.arraycopy(processedData, 0, data, 0, getSize);

                        for(byte a : data){
                            list_data.add(a);
                        }
                        processedData = new byte[15];

                        length++;

                        if(length == 3){
                            if(MyApplicatoin.IS_BLE){

                            	//组3包回调
                                final byte [] byte_data=new byte[list_data.size()];

                                for(int i = 0; i <list_data.size(); i++){
                                    byte_data[i]=list_data.get(i);
                                }

                                //回调线程
                                sendThread.putData(byte_data,time);

                                list_data.clear();
                                length = 0;
                            }
                        }
                    }

				} catch (Exception e) {

					ExceptionUtil.handleException(e);

				}

			}
		}
		fileWriter.setRecording(false);
		sendThread.setRecording(false);
	}

	public void putData(short[] data, int size, long time) {

		synchronized (mutex) {
			ReadData rd = new ReadData();
			rd.size = size;
			rd.ready = new short[data.length];
			System.arraycopy(data, 0, rd.ready, 0, size);
			list.add(rd);
			this.time = time;
		}
	}

	public void setRecording(boolean isRecording) {
		synchronized (mutex) {
			this.isRecording = isRecording;
		}
	}

	public boolean isRecording() {
		synchronized (mutex) {
			return isRecording;
		}
	}

	class ReadData {
		private int size;
		private short[] ready;
	}

	public static short[] toShortArray(byte[] src) {

		int count = src.length >> 1;
		short[] dest = new short[count];
		for (int i = 0; i < count; i++) {
			dest[i] = (short) (src[i * 2] << 8 | src[2 * i + 1] & 0xff);
		}
		return dest;
	}

	public static byte[] toByteArray(short[] src) {

		int count = src.length;
		byte[] dest = new byte[count << 1];
		for (int i = 0; i < count; i++) {
			dest[i * 2] = (byte) (src[i] >> 8);
			dest[i * 2 + 1] = (byte) (src[i] >> 0);
		}

		return dest;
	}

}
