#include <jni.h>
//#include "JNIEncrypt.h"
#include "soft_AES.h"
#include "com_sms_app_interphone_util_aesutil_SMSEncrypt.h"
#include <android/log.h>
#include "string.h"

#define TAG "AES" // 这个是自定义的LOG的标识
#define AES_LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型
/*
JNIEXPORT jbyteArray JNICALL Java_com_sms_app_interphone_util_aesutil_SMSEncrypt_encode(JNIEnv *env, jobject instance, jbyteArray jbyte_data, jbyteArray jbyte_key) {

    jbyte* bytein =(*env)->GetByteArrayElements(env,jbyte_data, 0);
    jbyte* bytekey =(*env)->GetByteArrayElements(env,jbyte_key, 0);
    //static uint8_t AES_KEY[] = {0x00,0x11,0x22,0x33,0x44,0x55,0x66,0x77,0x88,0x99,0xaa,0xbb,0xcc,0xdd,0xee,0xff}; //密钥
    uint8_t buffer[16];//加密后数据
    cipher(bytein, buffer, bytekey);
    memset(buffer, 40, 16);
    int bytelen = 16;
    jbyteArray jarrRV =(*env)->NewByteArray(env,bytelen);
    (*env)->SetByteArrayRegion(env,jarrRV, 0, bytelen, buffer);
   // AES_LOGD("%s\r\n", "aes  encode" );
    return jarrRV;
}


JNIEXPORT jbyteArray JNICALL  Java_com_sms_app_interphone_util_aesutil_SMSEncrypt_decode(JNIEnv *env, jobject instance, jbyteArray jbyte_data, jbyteArray jbyte_key) {

    jbyte* bytein =(*env)->GetByteArrayElements(env,jbyte_data, 0);
    jbyte* bytekey =(*env)->GetByteArrayElements(env,jbyte_key, 0);
    //uint8_t AES_KEY[] = {0x00,0x11,0x22,0x33,0x44,0x55,0x66,0x77,0x88,0x99,0xaa,0xbb,0xcc,0xdd,0xee,0xff}; //密钥
    uint8_t buffer[16];//解密后数据
    inv_cipher(bytein, buffer, bytekey);

    int bytelen = 16;
    jbyteArray jarrRV =(*env)->NewByteArray(env,bytelen);
    (*env)->SetByteArrayRegion(env,jarrRV, 0, bytelen, buffer);

    return jarrRV;
}
*/

JNIEXPORT jbyteArray JNICALL Java_com_sms_app_interphone_util_aesutil_SMSEncrypt_aes_1init(JNIEnv *env, jclass instance, jbyteArray id)
 {
    AES_LOGD("%s \r\n", "aes init");
    uint8 *key;
    jbyte* byteid =(*env)->GetByteArrayElements(env,id, 0);

    key = aes_init((uint8*)byteid);
    jbyteArray jarrRV =(*env)->NewByteArray(env, 16);
    (*env)->SetByteArrayRegion(env,jarrRV, 0, 16, (const signed char *)key);
    return jarrRV;
 }

JNIEXPORT void JNICALL Java_com_sms_app_interphone_util_aesutil_SMSEncrypt_aes_1encode(JNIEnv *env, jclass instance, jbyteArray in, jbyteArray out)
 {
     jbyte* bytein =(*env)->GetByteArrayElements(env,in, 0);

     uint8 buffer[20];
     aes_encode((uint8*)bytein, (uint8*)buffer);
     (*env)->SetByteArrayRegion(env,out, 0, 20, (const signed char*)buffer);
     AES_LOGD("%s \r\n", "aes encode");

     return ;
}


JNIEXPORT void JNICALL Java_com_sms_app_interphone_util_aesutil_SMSEncrypt_aes_1decode(JNIEnv *env, jclass instance, jbyteArray in, jbyteArray out)
{
    AES_LOGD("%s \r\n", "aes decode");
    jbyte* bytein =(*env)->GetByteArrayElements(env,in, 0);

    uint8 buffer[20];
    aes_decode((uint8*)bytein, (uint8*)buffer);
    (*env)->SetByteArrayRegion(env,out, 0, 20, (const signed char*)buffer);
    AES_LOGD("%s \r\n", "aes encode");
   return ;
}


JNIEXPORT jstring JNICALL Java_com_sms_app_interphone_util_aesutil_SMSEncrypt_sayHello
(JNIEnv *env, jobject thiz)
{
     return (*env)->NewStringUTF(env,"我们的歌名");
}