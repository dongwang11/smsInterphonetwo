package com.sms.app.interphone.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.sms.app.framework.communication.localayer.ImCode;
import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.framework.trans.bean.Interphone;
import com.sms.app.interphone.R;
import com.sms.app.interphone.services.Open_biz;
import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.interphone.util.frequentlyutil.SharePreferenceConstant;
import com.sms.app.interphone.util.frequentlyutil.SharePreferenceUtil;
import com.sms.app.interphone.util.msnutil.ExceptionUtil;
import com.sms.app.interphone.util.msnutil.Tools;
import com.sms.app.interphone.util.openutil.Openfrom;
import com.sms.app.interphone.util.qrcodeutil.CameraManager;
import com.sms.app.interphone.util.qrcodeutil.CaptureActivityHandler;
import com.sms.app.interphone.util.qrcodeutil.RGBLuminanceSource;
import com.sms.app.interphone.view.CustomScanView;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;

/**
 * ?????????????????????????????????Activity
 * <p>
 * ?????????
 */

public class WiActivity extends BaseActivity implements Callback, View.OnClickListener {
    private Vector<BarcodeFormat> decodeFormats;
    private CaptureActivityHandler handler;
    private CustomScanView customScanView;
//    private MediaPlayer mediaPlayer;
    private boolean hasSurface = false;
//    private boolean playBeep;
    private boolean vibrate;
    private String characterSet;
    private String photoPath;
    private Bitmap scanBitmap;
    private ImageView back, album, light, qrcode, streets;
    private static final int REQUEST_QRCODE = 100;
    private static final int DECODE_SUCCESS = 300;
    private static final int DECODE_FAILURE = 301;
    private static final int DECODE_YES = 10;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
//    private AudioManager audioService;
    private boolean isOpen;
    private Context context;

    public final byte Joinfrom = 4;

    public final int YES = 1;
    public final int NO = 0;


    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE",
    };


    private String url = "@conference.iz94owcwnrdz";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wi);
        CameraManager.init(getApplication());
    //    audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        surfaceView = (SurfaceView) findViewById(R.id.capture_camera);
        customScanView = (CustomScanView) findViewById(R.id.capture_ScanView);
        back = (ImageView) findViewById(R.id.capture_back);
        light = (ImageView) findViewById(R.id.capture_light);
        album = (ImageView) findViewById(R.id.capture_album);
        back.setOnClickListener(this);
        light.setOnClickListener(this);
        album.setOnClickListener(this);

    }

    /**
     * ??????????????????????????????
     */
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DECODE_SUCCESS:
                    onResultHandler((String) msg.obj, scanBitmap);
                    break;
                case DECODE_FAILURE:
                    Toast.makeText(WiActivity.this, (String) msg.obj, Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        }

    };

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.capture_back:
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.capture_light:
                if (!isOpen) {
                    ((TextView) findViewById(R.id.capture_status)).setText("ON");
                    CameraManager.get().enableFlash();
                    isOpen = true;
                } else {  // ??????
                    ((TextView) findViewById(R.id.capture_status)).setText("OFF");
                    CameraManager.get().disableFlash();
                    isOpen = false;
                }
                break;
            case R.id.capture_album:


                if(hasPermission(PERMISSIONS_STORAGE)){

                    String state = Environment.getExternalStorageState();

                    if (state.equals(Environment.MEDIA_MOUNTED)) {

                        Intent intentTakePic = new Intent();
                        intentTakePic.setAction(Intent.ACTION_PICK);
                        intentTakePic.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        this.startActivityForResult(intentTakePic, REQUEST_QRCODE);

                    }

                }else{

                    requstPermissions(GlobalConsts.WRITE_EXTERNAL_STORAGE,PERMISSIONS_STORAGE);

                }



                /*Intent albumIntent = new Intent(Intent.ACTION_PICK);
                albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(albumIntent, REQUEST_QRCODE);*/

                /*Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_QRCODE);//??????????????????*/

                break;
//		case R.id.capture_qrcode:
//			cleanButton();
//			CameraManager.get().setFrameWidthAndHeight(650,650);
//			customScanView.setText(getResources().getString(R.string.scan_qrcode));
//			CustomScanView.isFirst = false;
//			startScanView();
//			qrcode.setBackgroundResource(R.drawable.icon_capture_qrcode_selected);
//			break;
//		case R.id.capture_streets:
//			cleanButton();
//			CameraManager.get().setFrameWidthAndHeight(750,900);
//			customScanView.setText(getResources().getString(R.string.scan_streets));
//			CustomScanView.isFirst = false;
//			startScanView();
//			streets.setBackgroundResource(R.drawable.icon_capture_streets_selected);
//			locationService.start();
//			locationService.getRequestLocation();
//			break;

            default:
                break;
        }

    }

    /**
     * ????????????????????????????????????
     *
     * @param path ????????????
     * @return
     */
    private Result scanningImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();

        hints.put(DecodeHintType.CHARACTER_SET, "UTF8");//???????????????????????????

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false;
        int sampleSize = (int) (options.outHeight / (float) 200);
        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);
        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(bitmap1, hints);
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
        return null;
    }

    /**
     * ?????????????????????????????????
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_QRCODE) {

            try {

                Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);

                if(cursor != null){

                    if (cursor.moveToFirst()) {
                        photoPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    }
                    LogUtil.i(GlobalConsts.TAG,"???????????????"+photoPath);
                    cursor.close();
                    //????????????????????????????????????
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            Result result = null;
                            Message msg = mHandler.obtainMessage();

                            try {
                                result = scanningImage(photoPath);
                            } catch (Exception e) {
                                ExceptionUtil.handleException(e);
                            }

                            if (result != null) {
                                msg.what = DECODE_SUCCESS;
                                msg.obj = result.getText();
                            } else {
                                msg.what = DECODE_FAILURE;
                                msg.obj = getResources().getString(R.string.sms_chat_wi_hite_erro);
                            }
                            mHandler.sendMessage(msg);
                        }
                    }).start();

                }else{

                    Tools.showInfo(WiActivity.this,getResources().getString(R.string.sms_chat_wi_hite_erro));

                }
            } catch (Exception e) {
                ExceptionUtil.handleException(e);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
//			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        if (isOpen) {
            ((TextView) findViewById(R.id.capture_status)).setText("OFF");
            CameraManager.get().disableFlash();
            isOpen = false;
        }

        /*playBeep = true;
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }*/

        decodeFormats = null;
        characterSet = null;
        CustomScanView.isFirst = false;
     //   initBeepSound();
        vibrate = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitPreview();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    //	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		CameraManager.get().setFrameWidthAndHeight(650, 650);
//		locationService.unregisterListener(listener);
//		locationService.stop();
//	}

    /**
     * CaptureActivityHandler?????????????????????????????????
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        //    playBeepSoundAndVibrate();
        String resultString = result.getText();
        onResultHandler(resultString, barcode);
    }

    /**
     * ?????????????????????????????????????????????????????????????????????
     */
    private void onResultHandler(final String resultString, Bitmap bitmap) {
        try {
            if (TextUtils.isEmpty(resultString)) {
                Toast.makeText(WiActivity.this, getResources().getString(R.string.sms_chat_wi_hite_erro), Toast.LENGTH_SHORT).show();
                return;
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        /*resultIntent.putExtra("bitmap", baos.toByteArray());
        resultIntent.putExtra("result", resultString);
        this.setResult(RESULT_OK, resultIntent);*/

            LogUtil.i(GlobalConsts.TAG,"?????????????????????"+resultString.length()+"????????????"+Arrays.toString(resultString.getBytes()));

            String[] strings = null;

            try {
                strings = resultString.split(",");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(strings != null && strings.length == 7){

                String fromNum = null;
                String Numcode = null;
                String UUIDNum = null;

                String title = null;

                byte[] data = null;

                String fromName = null;


                fromName = strings[3];

                fromNum = getName(strings[0],strings[1],strings[2],strings[3])+fromName;

                title = strings[4];

                Numcode = strings[5];

                UUIDNum = strings[6];


                if (MyApplicatoin.evenUser != null && MyApplicatoin.interphone != null) {


                    if(fromName.substring(0,1).equals("3")){

                        LogUtil.i(TAG,"?????????");

                        SharedPreferences sharedPreferences = WiActivity.this.getSharedPreferences(GlobalConsts.ACTION_IM_CODE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString("id", Numcode);
                        editor.putString("name", fromNum);
                        editor.putString("uuid", UUIDNum);
                        editor.putString("hite", title);
                        editor.putString("boolean", "false");


                        editor.commit();

                        Openfrom openfrom = new Openfrom();
                        openfrom.setUserId(MyApplicatoin.evenUser.getId());
                        openfrom.setFromName(fromName);
                        openfrom.setCode(Open_biz.Apply);

                        MyApplicatoin.openfrom = openfrom;

                        MyApplicatoin.setOpenfire(openfrom);

                    }else{

                        LogUtil.i(TAG,"?????????");

                        Interphone interphone1 = new Interphone();

                        ImCode imcode = new ImCode();

                        imcode.setId(Long.parseLong(Numcode));
                        imcode.setName(fromNum.substring(0,14)+title);//fromNum
                        imcode.setUuid(UUIDNum);
                        imcode.setHite(title);

                        interphone1.setImCode(imcode);

                        MyApplicatoin.interphone.setImCode(imcode);

                        MyApplicatoin.setInterphone(interphone1);
                       // SharePreferenceUtil.saveStringDataToSharePreference(WiActivity.this, SharePreferenceConstant.HITEROOM, title);
                       // SharePreferenceUtil.saveStringDataToSharePreference(WiActivity.this, SharePreferenceConstant.HITERRONAME, fromName);

                        if(data != null){

                            int txCode = Integer.parseInt(strings[0]);
                            int rxCode = Integer.parseInt(strings[1]);

                            int freq = Integer.parseInt(strings[2]);


                            if(MyApplicatoin.interPhoneRF == 2){

                                if(MyApplicatoin.interphone.getRxcode() != rxCode){

                                    Interphone interphone = new Interphone();
                                    interphone.setRxcode(rxCode);
                                    MyApplicatoin.setInterphone(interphone);
                                }

                                if(MyApplicatoin.interphone.getTxcode() != txCode){

                                    Interphone interphone = new Interphone();
                                    interphone.setTxcode(txCode);
                                    MyApplicatoin.setInterphone(interphone);

                                }

                                if(MyApplicatoin.interphone.getFrequne() != freq){

                                    Interphone interphone = new Interphone();
                                    interphone.setFrequne(freq);
                                    MyApplicatoin.setInterphone(interphone);

                                }

                            }


                        }


                        Openfrom openfrom = new Openfrom();
                        openfrom.setUserId(MyApplicatoin.evenUser.getId());
                        openfrom.setFromName(fromName);
                        openfrom.setCode(Joinfrom);

                        MyApplicatoin.openfrom = openfrom;

                        MyApplicatoin.setOpenfire(openfrom);
                    }

                    //   Tools.showInfo(WiActivity.this,resultString);
                } else {
                    Tools.showInfo(WiActivity.this,getResources().getString(R.string.sms_chat_wi_hite_erro));
                }


                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }else{

                Tools.showInfo(WiActivity.this,getResources().getString(R.string.sms_chat_wi_hite_erro));

            }


        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            Tools.showInfo(WiActivity.this,getResources().getString(R.string.sms_chat_wi_hite_erro));
        }
    }

    private String getName(String tx, String rx, String fq, String names) {

        String name = null;

        try {
            byte[] data = new byte[14];

            int i = 0;

            int txCode = Integer.parseInt(tx);
            int rxCode = Integer.parseInt(tx);

            int freq = Integer.parseInt(fq);

            long fName = Long.parseLong(names);


            data[i++] = (byte) (txCode & 0xff);
            data[i++] = (byte) (rxCode & 0xff);

            data[i++] = (byte) (freq & 0xff);
            data[i++] = (byte) (freq >> 8);
            data[i++] = (byte) (freq >> 16);
            data[i++] = (byte) (freq >> 24);

            byte[] bata = Tools.longToBytes(fName);

            data[i++] = bata[0];
            data[i++] = bata[1];
            data[i++] = bata[2];
            data[i++] = bata[3];
            data[i++] = bata[4];
            data[i++] = bata[5];
            data[i++] = bata[6];
            data[i++] = bata[7];

            name = new String(data,"ISO-8859-1");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return name;

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * ??????????????????
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }


    public CustomScanView getScanView() {
        return customScanView;
    }

    /**
     * ????????????handler
     */
    public Handler getHandler() {
        return handler;
    }

    /**
     * ??????????????????
     */
    public void startScanView() {
        customScanView.drawScanView();
    }

    /**
     * ?????????mediaPlayer??????????????????
     */
    /*private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);
            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(0.50f, 0.50f);
                mediaPlayer.prepare();
            } catch (IOException e) {
                ExceptionUtil.handleException(e);
                mediaPlayer = null;
            }
        }
    }*/

    /**
     * ?????????????????????
     */
    /*private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {//??????Tapplicantion?????????
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(1000);//??????200??????
        }
    }*/

    /**
     * ????????????????????????mediaPlayer
     */
    /*private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };*/

}
