
package com.sms.app.interphone.util.voiceutil.voice;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.widget.ImageView;

import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Gauss
 *
 */
public class SpeexInput implements Runnable{

	private Speex vDecoder;

	private File srcPath = null;

	byte[] header = new byte[2048];

	byte[] payload = new byte[2048];

	final int OGG_HEADERSIZE = 27;
	private boolean paused;

	int sampleRate = 8000;

	private VoicePlayUtil playUtil;
	private AudioTrack audioTrack = null;


	private DataInputStream dis = null;


	/**
	 * 构造方法 获取播放的 Voice文件
	 *
	 * @param user_id
	 * @param text*/
	public SpeexInput(long user_id, String text,ImageView img,Handler handler){

		srcPath = new File(text);

		this.paused = true;

		playUtil = new VoicePlayUtil(handler,img);

		playUtil.setImageView(user_id);
		playUtil.voicePlay();

		vDecoder = new Speex();
		//初始化压缩库
		vDecoder.init(vDecoder.getQuality());

	//	int minBufferSize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);

	//	audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize, AudioTrack.MODE_STREAM);


		if(MyApplicatoin.audioTrack == null){

			int minBufferSize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);

			MyApplicatoin.audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize, AudioTrack.MODE_STREAM);

		}
	}

    @Override
	public void run() {

		try {

			dis = new DataInputStream(new BufferedInputStream(new FileInputStream(srcPath)));


			/*if(MyApplicatoin.audioTrack == null){
				MyApplicatoin.audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize, AudioTrack.MODE_STREAM);

			}*/


			int decsize = 0;


			/*byte[] voice = {19,-98,-29,-93,-64,-18,-18,72,1,119,15,-69,-127,-35,-36,
					17,-117,-6,62,-128,8,-105,-124,-93,43,-27,119,-33,-85,-92,
					17,-117,-6,62,55,-36,-46,-18,-81,-78,-67,-108,13,-14,90,
					20,-58,52,6,-91,93,45,1,110,111,25,1,-59,67,62,
					19,-90,26,6,-125,7,-88,76,-1,-66,106,38,69,-17,80,
					20,110,-24,6,98,-103,10,29,84,-49,-67,-22,-123,51,46,
					20,110,-110,6,32,-88,-103,8,61,71,-90,125,-61,77,50,
					20,110,-88,6,0,-9,-88,20,-49,-66,105,-90,69,-17,80,
					20,110,-48,5,-64,-103,7,21,68,-56,-67,-22,5,50,-114,
					18,-58,88,5,-95,-91,-97,21,84,72,-89,-34,-128,59,-36};*/

			int k = 0;

			while (this.paused){

				short[] decoded = new short[160];

				//由于AudioTrack播放的是流，所以，我们需要一边播放一边读取
				dis.readFully(payload, 0, 15);


				if ((decsize = vDecoder.decode(payload, decoded, 160)) > 0) {

					if(MyApplicatoin.audioTrack != null){

						if(MyApplicatoin.audioTrack.getState() == AudioTrack.STATE_INITIALIZED){

							MyApplicatoin.audioTrack.write(decoded, 0, decsize);

							MyApplicatoin.audioTrack.play();

						}else{
							MyApplicatoin.audioTrack = null;
						}

					}
					/*if(audioTrack != null){

						if(audioTrack.getState() == AudioTrack.STATE_INITIALIZED){

							audioTrack.write(decoded, 0, decsize);

							audioTrack.play();

						}else{
							audioTrack = null;
						}

					}*/
				}
			}

		} catch (EOFException e){

			e.printStackTrace();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();

		} finally {


			try {
				if(MyApplicatoin.audioTrack != null){

					if(MyApplicatoin.audioTrack.getState() == AudioTrack.STATE_INITIALIZED){
						MyApplicatoin.audioTrack.stop();
					}else{
						MyApplicatoin.audioTrack = null;
					}

				}

				/*if(audioTrack != null){

					if(audioTrack.getState() == AudioTrack.STATE_INITIALIZED){

						audioTrack.stop();

					}else{
						audioTrack = null;
					}

				}*/
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}

			try {
				if(dis != null){
					dis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}


			if(playUtil != null){

				playUtil.stopPlay();

			}
			LogUtil.i(GlobalConsts.TAG,"release............");
		}


	}

	public synchronized void setPaused(boolean paused) {
		this.paused = paused;
		try {
			if(MyApplicatoin.audioTrack != null){

				if(MyApplicatoin.audioTrack.getState() == AudioTrack.STATE_INITIALIZED){
					MyApplicatoin.audioTrack.stop();
				}else{
					MyApplicatoin.audioTrack = null;
				}

			}

			/*if(audioTrack != null){

				if(audioTrack.getState() == AudioTrack.STATE_INITIALIZED){

					audioTrack.stop();

				}else{
					audioTrack = null;
				}

			}*/
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	public synchronized boolean isPaused() {
		return paused;
	}


	public static short[] toShortArray(byte[] src) {

		int count = src.length >> 1;
		short[] dest = new short[count];
		for (int i = 0; i < count; i++) {
			dest[i] = (short) (src[i * 2] << 8 | src[2 * i + 1] & 0xff);
		}
		return dest;
	}

}
