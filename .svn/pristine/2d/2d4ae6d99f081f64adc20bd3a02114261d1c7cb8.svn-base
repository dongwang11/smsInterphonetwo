#include <jni.h>

#include <string.h>
#include <unistd.h>

#include <speex/speex.h>
#include <speex/speex_preprocess.h>
#include <android/log.h>

#include "wedrtc/signal_processing_library.h"
#include "wedrtc/noise_suppression_x.h"
#include "wedrtc/noise_suppression.h"
#include "wedrtc/gain_control.h"

static int codec_open = 0;

static int dec_frame_size;
static int enc_frame_size;

static SpeexBits ebits, dbits;
void *enc_state;
void *dec_state;

static JavaVM *gJavaVM;
//SpeexPreprocessState *state;

void *agcHandle = NULL;

#define TAG "speex" // 这个是自定义的LOG的标识
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型

#define WEDRTC  0

extern "C"
JNIEXPORT jint JNICALL Java_com_sms_app_interphone_util_voiceutil_voice_Speex_open
  (JNIEnv *env, jobject obj, jint compression) {
	int tmp;

	if (codec_open++ != 0)
		return (jint)0;

	speex_bits_init(&ebits);
	speex_bits_init(&dbits);

	enc_state = speex_encoder_init(&speex_nb_mode); 
	dec_state = speex_decoder_init(&speex_nb_mode); 
	tmp = compression;
	speex_encoder_ctl(enc_state, SPEEX_SET_QUALITY, &tmp);
	speex_encoder_ctl(enc_state, SPEEX_GET_FRAME_SIZE, &enc_frame_size);
	speex_decoder_ctl(dec_state, SPEEX_GET_FRAME_SIZE, &dec_frame_size);

#if(WEDRTC)
    WebRtcAgc_Create(&agcHandle);
    WebRtcAgc_Init(agcHandle, 0, 255, kAgcModeFixedDigital, 8000);
    WebRtcAgc_config_t agcConfig;
    agcConfig.compressionGaindB = 10;
    agcConfig.limiterEnable     = 0;
    agcConfig.targetLevelDbfs   = 3;
    WebRtcAgc_set_config(agcHandle, agcConfig);
#endif
/*
    int agc = 1, agc_lvl = 8000;
    state= speex_preprocess_state_init(160, 8000);
    speex_preprocess_ctl(state, SPEEX_PREPROCESS_SET_VAD, &agc);
    speex_preprocess_ctl(state, SPEEX_PREPROCESS_SET_AGC, &agc);
    speex_preprocess_ctl(state, SPEEX_PREPROCESS_GET_VAD, &agc);
    speex_preprocess_ctl(state, SPEEX_PREPROCESS_SET_AGC_LEVEL, &agc_lvl);
    //speex_preprocess_ctl(state, SPEEX_PREPROCESS_SET_AGC_MAX_GAIN, &agc_lvl);
    //LOGD("%s\r\n", "laoshiji  open" );
    //LOGD("%s,agc:%d\r\n", TAG,agc );
*/

	return (jint)0;
}

extern "C"
JNIEXPORT jint JNICALL Java_com_sms_app_interphone_util_voiceutil_voice_Speex_encode
    (JNIEnv *env, jobject obj, jshortArray lin, jint offset, jbyteArray encoded, jint size) {

        jshort buffer[enc_frame_size];
        jbyte output_buffer[enc_frame_size];
	int nsamples = (size-1)/enc_frame_size + 1;
	int i, tot_bytes = 0;

    jshort wedrtc_buf[enc_frame_size];

	if (!codec_open)
		return 0;
#if(WEDRTC)
    uint8_t saturationWarning;
    int outMicLevel;
#endif
	speex_bits_reset(&ebits);
	for (i = 0; i < nsamples; i++) {

		env->GetShortArrayRegion(lin, offset + i*enc_frame_size, enc_frame_size, buffer);
        //int ret = speex_preprocess_run(state, buffer);
#if(WEDRTC)
        WebRtcAgc_Process(agcHandle, buffer, NULL, enc_frame_size, wedrtc_buf,NULL, 0, &outMicLevel, 0, &saturationWarning);
        speex_encode_int(enc_state, wedrtc_buf, &ebits);
#else
		speex_encode_int(enc_state, buffer, &ebits);
#endif
	}

	tot_bytes = speex_bits_write(&ebits, (char *)output_buffer,
				     enc_frame_size);
	env->SetByteArrayRegion(encoded, 0, tot_bytes,
				output_buffer);
        return (jint)tot_bytes;
}

extern "C"
JNIEXPORT jint Java_com_sms_app_interphone_util_voiceutil_voice_Speex_decode
    (JNIEnv *env, jobject obj, jbyteArray encoded, jshortArray lin, jint size) {

        jbyte buffer[dec_frame_size];
        jshort output_buffer[dec_frame_size];
        jsize encoded_length = size;

	if (!codec_open)
		return 0;
	env->GetByteArrayRegion(encoded, 0, encoded_length, buffer);
	speex_bits_read_from(&dbits, (char *)buffer, encoded_length);
	speex_decode_int(dec_state, &dbits, output_buffer);
	env->SetShortArrayRegion(lin, 0, dec_frame_size,
				 output_buffer);

	return (jint)dec_frame_size;
}

extern "C"
JNIEXPORT jint JNICALL Java_com_sms_app_interphone_util_voiceutil_voice_Speex_getFrameSize
    (JNIEnv *env, jobject obj) {

	if (!codec_open)
		return 0;
	return (jint)enc_frame_size;

}

extern "C"
JNIEXPORT void JNICALL Java_com_sms_app_interphone_util_voiceutil_voice_Speex_close
    (JNIEnv *env, jobject obj) {

	if (--codec_open != 0)
		return;
//  speex_preprocess_state_destroy(state);
#if(WEDRTC)
    WebRtcAgc_Free(agcHandle);
#endif
	speex_bits_destroy(&ebits);
	speex_bits_destroy(&dbits);
	speex_decoder_destroy(dec_state); 
	speex_encoder_destroy(enc_state); 
}

/*
extern "C"
JNIEXPORT void JNICALL Java_com_sms_app_interphone_util_voiceutil_voice_Speex_agc
    (JNIEnv *env, jobject obj,jshortArray in, jshortArray out, jint size) {
     WebRtcAgc_Create(&agcHandle);

    WebRtcAgc_Init(agcHandle, 0, 255, kAgcModeFixedDigital, 8000);
    WebRtcAgc_config_t agcConfig;
    agcConfig.compressionGaindB = 50;
    agcConfig.limiterEnable     = 0;
    agcConfig.targetLevelDbfs   = 3;
    WebRtcAgc_set_config(agcHandle, agcConfig);

    uint8_t saturationWarning;
    int outMicLevel;
    jshort buffer[160];
    jshort wedrtc_buf[160];

	env->GetShortArrayRegion(in, 0, 160, buffer);
    WebRtcAgc_Process(agcHandle, buffer, NULL, enc_frame_size, wedrtc_buf,NULL, 0, &outMicLevel, 0, &saturationWarning);
    env->SetByteArrayRegion(out, 0, 160, (jbyte)*wedrtc_buf);
    WebRtcAgc_Free(agcHandle);

}

*/