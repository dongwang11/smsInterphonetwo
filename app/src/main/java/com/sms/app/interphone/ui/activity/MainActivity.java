package com.sms.app.interphone.ui.activity;

import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;
import com.sms.app.framework.RequestCB;
import com.sms.app.framework.communication.localayer.ImCode;
import com.sms.app.framework.communication.localayer.MsgResponse;
import com.sms.app.framework.communication.localayer.bledriver.BluetoothLeService;
import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.framework.communication.localayer.cmd.ATcommand;
import com.sms.app.framework.communication.localayer.cmd.Ble_message;
import com.sms.app.framework.communication.localayer.cmd.Objcode;
import com.sms.app.framework.communication.localayer.cmd.Opcode;
import com.sms.app.framework.dao.bean.DAOApplyMsg;
import com.sms.app.framework.dao.bean.DAOGroup;
import com.sms.app.framework.dao.bean.DAOMesg;
import com.sms.app.framework.dao.bean.DAOmember;
import com.sms.app.framework.dao.bean.commom.DAOApplyMsgDao;
import com.sms.app.framework.dao.bean.commom.DAOGroupDao;
import com.sms.app.framework.dao.bean.commom.DAOMesgDao;
import com.sms.app.framework.dao.bean.commom.DAOService;
import com.sms.app.framework.dao.bean.commom.DAOmemberDao;
import com.sms.app.framework.sms_request;
import com.sms.app.framework.trans.bean.Avatar;
import com.sms.app.framework.trans.bean.Interphone;
import com.sms.app.framework.trans.bean.Precursor;
import com.sms.app.framework.trans.bean.Production;
import com.sms.app.framework.trans.bean.User;
import com.sms.app.interphone.R;
import com.sms.app.interphone.adapter.ChatAdapter;
import com.sms.app.interphone.entity.Filter;
import com.sms.app.interphone.entity.Mesage_entity;
import com.sms.app.interphone.entity.Voice;
import com.sms.app.interphone.services.Open_biz;
import com.sms.app.interphone.services.UpdataService;
import com.sms.app.interphone.services.op_Message;
import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.interphone.util.aesutil.EncryptUtil;
import com.sms.app.interphone.util.aesutil.SMSEncrypt;
import com.sms.app.interphone.util.frequentlyutil.AnimUtil;
import com.sms.app.interphone.util.frequentlyutil.BackGroundService;
import com.sms.app.interphone.util.frequentlyutil.ChargerUtils;
import com.sms.app.interphone.util.frequentlyutil.Constants;
import com.sms.app.interphone.util.frequentlyutil.SearchWatherChatRoom;
import com.sms.app.interphone.util.frequentlyutil.SharePreferenceConstant;
import com.sms.app.interphone.util.frequentlyutil.SharePreferenceUtil;
import com.sms.app.interphone.util.frequentlyutil.getPhotoFromPhotoAlbum;
import com.sms.app.interphone.util.interfaces.NoDoubleClickListener;
import com.sms.app.interphone.util.msnutil.ExceptionUtil;
import com.sms.app.interphone.util.msnutil.SearchWather;
import com.sms.app.interphone.util.msnutil.Tools;
import com.sms.app.interphone.util.openutil.Openfrom;
import com.sms.app.interphone.util.voiceutil.SpeexRecorder.Voice_Read_Thread;
import com.sms.app.interphone.util.voiceutil.voice.SpeexInput;
import com.sms.app.interphone.view.ChatListView;
import com.sms.app.interphone.view.CircleImageView;
import com.sms.app.interphone.view.DragLayout;
import com.sms.app.interphone.view.PowerView;
import com.sms.app.interphone.view.PttRelativeLayout;
import com.sms.app.interphone.view.SlideSwitch;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * 10.22
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks{

    /** ???????????? */
    private final String IMAGE_FILE_NAME = "image.jpg";

    /** ????????? */
    private final int IMAGE_REQUEST_CODE = 0;
    private final int CAMERA_REQUEST_CODE = 1;
    private final int RESULT_REQUEST_CODE = 2;
    private final int PHOTOGRAPH_REQUEST = 3;

    private ImagePicker imagePicker = new ImagePicker();

    private BluetoothAdapter mBluetoothAdapter;

    private String imgPath = null;

    private String path = null;

    private TextView mToolBarTextView;

    private TextView user_name;

    private TextView user_freq;

    private ImageView sms_main_ico_profile;

    private ImageView sms_return_icon;

    private RelativeLayout main_layout_right;
    private RelativeLayout main_layout_left;

    private CircleImageView user_icon;

    private DragLayout dragLayout;

    private LinearLayout shareGroup;
    private LinearLayout newGroup;
    private LinearLayout histruyGroup;
    private LinearLayout joinGroup;
    private LinearLayout leaveGroup;
    private LinearLayout device;

    private ImageView chat_ImageView_newgroup;

    private LinearLayout faq;
    private LinearLayout update;
    private LinearLayout about;

    private UpdataService service;


    private LinearLayout signout;


    private LoginReceiver loginReceiver;


    //????????????ListView

    private ChatListView lv;

    //??????Switch
    private boolean setSwitch = true;

    //??????Switch
    private ImageView img_switch;

    //??????Switch ??????Layout
    private RelativeLayout is_switch;
    private RelativeLayout chat_layout;

    private RelativeLayout erro_hite_layout;
    private Button chatDraglayoutDeviceBtn;



    //??????Button
    private RelativeLayout bt_Voice;

    //??????Button
    private Button bt_send;


    //????????????????????????
    private TextView tv_voice_hint;
    private TextView tv_voice_content;

    //???????????????
    private EditText isEditText;


    private static String TAG = "YanShi...Log - MainActivity";

    private long clickTime = 0; //??????????????????????????????



    //PTT Layout
    private PttRelativeLayout pTT;

    private SlideSwitch ptt_switch;

    private String CurrentRoomName = "";



    //????????????????????????
    private int quality = 2;

    //??????????????????
    private Voice_Read_Thread voice_Read_Thread = null;

    //???????????????
    private List<DAOGroup> group_list;

    //????????????adapter
    private ChatAdapter adapter;

    //DB ?????????
    private DAOGroupDao mGroupDao;

    //DB ??????
    private DAOmemberDao memberDao;

    //DB Msg
    private DAOMesgDao mMesg;

    private DAOGroup group;

    private List<Mesage_entity> msg_erro_list = new ArrayList<>();

    private String from = "NITECORE";

    /**
     * ??????????????????????????????
     */

    private boolean isTouch = false;  //????????????


    private float mTime = 0;   //??????????????????

    private int page = 0;    //????????????????????????

    private int itemClick = 0;   //???????????????Item??????????????????

    private boolean voiceStart = true;

    private int list_item_size = 40;

    private List<Mesage_entity> mesage_entity_list = new ArrayList<Mesage_entity>();


    private final int MIN_CLICK_DELAY_TIME = 800;   //??????item?????????????????????

    private final int MIN_CLICK_HITE_TIME = 1500;   //??????item?????????????????????

    private long lastClickTime = 0;
    private long lastHiteTime = 0;


    private long doClickTime = 0;
    private long upClickTime = 0;


    private String PrecursorString[];


    private int CLICK_DELAY_TIME = 3000;

    private long lastClick = 0;

    private SpeexInput player;

    private List<Byte> voice_pack = new ArrayList();

    private String[] tx_code_data = {"OFF","67.0", "69.3", "71.9", "74.4", "77.0", "79.7", "82.5", "85.4",
            "88.5", "91.5", "94.8", "97.4", "100.0", "103.5", "107.2", "110.9", "114.8", "118.8",
            "123.0", "127.3", "131.8", "136.5", "141.3", "146.2", "151.4", "156.7", "162.2", "167.9",
            "173.8", "179.9", "186.2", "192.8", "203.5", "210.7", "218.1", "225.7", "233.6", "241.8", "250.3","23","25", "26", "31", "32", "43", "47", "51", "54", "65", "71",
            "72", "73", "74", "114", "116", "125", "131", "132", "134", "143",
            "152", "155", "156", "162", "165", "172", "174", "205", "223", "226",
            "243", "244", "245", "251", "261", "263", "265", "271", "306", "311", "315",
            "331", "343", "346", "351", "364", "365", "371", "411", "412", "413", "423",
            "431", "432", "445", "464", "465", "466", "503", "506", "516", "532", "546",
            "565", "606", "612", "624","627", "631", "632", "654","662", "664", "703",
            "712","723", "731", "732", "734","743", "754"};

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.RECORD_AUDIO",
            "android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE"
    };

    private Uri imgUri = null;



    //title
    private LinearLayout title_one_layout;
    private LinearLayout title_to_layout;
    private TextView title_one_group;
    private TextView title_to_group;
    private TextView title_one_freq;
    private TextView title_to_freq;
    private TextView title_mode;
    private TextView title_tc;
    private TextView title_rc;
    private PowerView powerView1,powerView2;
    private TextView tv_electricQuantity,title_online;

    private boolean isTitleSwitch = true;


   //?????????
    private TextView tv_1, tv_2, tv_3, tv_4, tv_5;
    private PopupWindow mPopupWindow;
    private AnimUtil animUtil;
    private float bgAlpha = 1f;
    private boolean bright = false;
    private static final long DURATION = 500;
    private static final float START_ALPHA = 0.7f;
    private static final float END_ALPHA = 1f;


    private  ImageView keyboard_imageView;
    private LinearLayout voice_relativeLayout,keyboard_LinearLayout,speak_relativeLayout;


    private boolean mBackEnable = false;
    private int mIsBtnBack;
    private int rootBottom = Integer.MIN_VALUE;
    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Rect r = new Rect();
            dragLayout.getGlobalVisibleRect(r);
            // ??????Activity??????????????????????????????onGlobalLayout?????????????????????????????????????????????????????????
            if (rootBottom == Integer.MIN_VALUE) {
                rootBottom = r.bottom;
                return;
            }

            // adjustResize????????????????????????????????????
            if (r.bottom < rootBottom) {
                mBackEnable = false;

                if(r.bottom != mIsBtnBack){

                    if (adapter != null) {

                        adapter.notifyDataSetChanged();
                        lv.setSelection(adapter.getCount());

                    }


                    if(setSwitch){


                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(300);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        setSwitch = false;

                                        img_switch.setImageResource(R.mipmap.chat_button_titlehint_false);

                                        //bt_Voice.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }).start();
                    }
                }

            } else {
                mBackEnable = true;
            }

            mIsBtnBack = r.bottom;

        }
    };


    byte voice_code = 0;

    /**
     *
     * ??????????????????
     *
     * */
    private Handler mhandler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){


                case GlobalConsts.VOICE:

                    //??????????????????????????????

                    Ble_message message = (Ble_message) msg.obj;


                    if(isInterphone()){
                        Openfrom openfrom = new Openfrom();

                        openfrom.setType(MsgResponse.Voice);
                        openfrom.setMessage(message.getFireName() + message.getMessage());
                        openfrom.setMessageID(message.getId());
                        openfrom.setCode(Open_biz.SendMessage);
                        openfrom.setUserId(MyApplicatoin.evenUser.getId());


                        Filter filter = new Filter();
                        filter.setPacketID(message.getId());
                        filter.setTime(System.currentTimeMillis());
                        filter.setType(message.getType());
                        filter.setCode(Filter.phone);
                        filter.setUserId(message.getUserId());

                        MsgResponse response = new MsgResponse();

                        response.setPacketID(message.getId());
                        response.setType(message.getType());
                        response.setFormName(Tools.getGroupName(MyApplicatoin.interphone.getImCode().getName()));

                        response.setContent(message.getMessage());
                        response.setTime(new Date(System.currentTimeMillis()));
                        response.setUserId(message.getUserId());
                        response.setFireName(message.getFireName());

                        //?????????????????????
                        MyApplicatoin.startMessageFilter(filter,response);
                    }else{
                        Filter filter = new Filter();
                        filter.setPacketID(message.getId());
                        filter.setTime(System.currentTimeMillis());
                        filter.setType(message.getType());
                        filter.setCode(Filter.phone);
                        filter.setUserId(message.getUserId());

                        MsgResponse response = new MsgResponse();

                        response.setPacketID(message.getId());
                        response.setType(message.getType());
                        LogUtil.i(TAG,"onReceive voice:"+Tools.getGroupName(MyApplicatoin.interphone.getImCode().getName()));

                        response.setFormName(Tools.getGroupName(MyApplicatoin.interphone.getImCode().getName()));

                        response.setContent(message.getMessage());
                        response.setTime(new Date(System.currentTimeMillis()));
                        response.setUserId(22L);
                        response.setFireName(message.getFireName());

                        //?????????????????????
                        MyApplicatoin.startMessageFilter(filter,response);
                    }

                    isEditText.setText(null);
                    break;
                case GlobalConsts.VOICE_SIZE:

                    //????????????????????????

                    int voiceSize = (int) msg.obj;

                    String hint = "Recording";

                    for(int i = 0;i < voiceSize; i++){
                        hint = hint+".";
                    }

                    tv_voice_hint.setText(hint);

                    break;

                case GlobalConsts.VOICE_CODE:

                    //????????????????????????

                    Voice v = (Voice) msg.obj;

                    voice_code = 120;

                   // if(isInterphone()){

                        //???????????????????????????Openfire
                        if(isInterphone()){

                                 newVoiceOpenFire(v);

                                if (MyApplicatoin.interphone.getRf() == null){

                                    return;
                                }


                                if(MyApplicatoin.interphone.getRf() == 1){

                                    return;

                                }

                                boolean isk = false;
                                for(long l : MyApplicatoin.ls){

                                    if(l == v.getPackID()){
                                        isk = true;
                                    }

                                }

                                if(isk){
                                    return;
                                }

                        }


                        if(MyApplicatoin.interphone.getState() != 1){

                            //????????????????????????BLE
                            byte prica = (byte) (4 | 0x80);

                            byte [] voiceData = ATcommand.at_set_file_user_chat_versions(prica,v.getUserID(),v.getPackID(),v.getBytes());
                            Log.d("dddd", EncryptUtil.bytesToHexString(voiceData));
                            BluetoothLeService.le_service.sendWriteByte(Objcode.MESSAGE,voiceData);

                        }
                   // }

                    break;

                case GlobalConsts.VOICE_BLE:

                    //???????????? 45???byte

                    try {

                        Voice voice = (Voice) msg.obj;

                        if(isInterphone()){

                            //???????????????????????????Openfire

                            newVoiceOpenFire(voice);

                            if (MyApplicatoin.interphone.getRf() == null){

                                return;
                            }


                            if(MyApplicatoin.interphone.getRf() == 1){

                                return;

                            }

                            if(MyApplicatoin.interphone.getState() != 1){

                                //????????????????????????BLE

                                newVoiceMessage(voice);

                            }
                        }else{
                            if (MyApplicatoin.interphone.getRf() == null){
                                return;
                            }

                            if(MyApplicatoin.interphone.getRf() == 1){
                                return;

                            }
                            newVoiceMessage(voice);
                        }
                    } catch (Exception e) {

                        ExceptionUtil.handleException(e);

                    }

                    break;
                case 4://??????????????????
                       Bundle bundle=msg.getData();
                       tv_electricQuantity.setText(bundle.getString("electricQuantity"));
                        for(int i = 0;i< mesage_entity_list.size();i++){//??????????????????
                            Mesage_entity mesage = mesage_entity_list.get(i);
                            if(mesage.getUser_id() == MyApplicatoin.evenUser.getId() && mesage.getMesg_type() != MsgResponse.Exit && mesage.getMesg_type() != MsgResponse.Join){
                                if(mesage.getIs_ok() == 2){
                                    title_online.setText("??????");
                                }else{
                                    title_online.setText("??????");
                                }
                            }
                        }

                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //??????????????????????????????
        LogUtil.i(TAG,"onActivityResult:requestCode:"+requestCode+",resultCode:"+resultCode);
        imagePicker.onActivityResult(this, requestCode, resultCode, data);//android 8.0?????????
        if (resultCode != RESULT_CANCELED) {

            switch (requestCode) {

                case IMAGE_REQUEST_CODE :

                    Uri sourceUri = data.getData();
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = this.managedQuery(sourceUri, proj, null, null, null);
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String imgPaths = cursor.getString(columnIndex);
                    File mInputFile = new File(imgPaths);

                    startPhotoZoom(mInputFile);
                    break;
                case CAMERA_REQUEST_CODE :
                    // ???????????????????????????????????????????????????
                    String state = Environment.getExternalStorageState();
                    if (state.equals(Environment.MEDIA_MOUNTED)) {


                        if(path != null){

                            try {
                                File file = new File(path);

                                startPhotoZoom(file);
                            } catch (Exception e) {
                                ExceptionUtil.handleException(e);
                            }

                        }else{

                            LogUtil.i(TAG,"??????????????? null");
                        }

                    } else {

                        Tools.showInfo(MainActivity.this,getResources().getString(R.string.sms_main_icon_hite_toast));

                    }
                    break;
                case RESULT_REQUEST_CODE : // ?????????????????????
                    LogUtil.i(TAG,"?????????????????????:"+mOutputFile);
                    //???????????????????????????
                    /*File tmpFile = new File(path);
                    if (tmpFile.exists()){
                        tmpFile.delete();
                    }*/
                    if (Build.VERSION.SDK_INT > 26||Build.VERSION.SDK_INT==26) {//??????????????????26???????????????????????????????????????????????????
                        mOutputFile = new File(getPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData()));
                    }
                    if (data != null && mOutputFile != null) {
                        getImageToView(mOutputFile);
                    }
                    break;

                case PHOTOGRAPH_REQUEST:

                 /*   try {
                        File file = new File(path);
                        imgUri = FileProvider.getUriForFile(MainActivity.this, "com.sms.app.interphone.fileprovider", file);//7.0
                        startPhotoZoomtwo(imgUri,file);
                    } catch (Exception e) {
                        ExceptionUtil.handleException(e);
                    }*/

                    break;
                default:
                    break;
            }
        }

        if(requestCode == GlobalConsts.INSTALL){

            LogUtil.i(TAG,"?????????????????????");

            installAPK();

        }
    }


    private String android_url;
    private String android_version;
    private String android_description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    setTranslucentStatus();

        setContentView(R.layout.activity_main);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Intent intent = getIntent();

        android_url = intent.getStringExtra(GlobalConsts.PRECURSOR_ANDROID_URL);
        android_version = intent.getStringExtra(GlobalConsts.PRECURSOR_ANDROID_VERSION);
        android_description = intent.getStringExtra(GlobalConsts.PRECURSOR_ANDROID_DESCRIPTION);

        initDragLayout(); //???????????????DragLayout??????

        setView();//???????????????????????????


        addListener();//???????????????????????????????????????

        setOnInputMethodManager();

        String renewal = SharePreferenceUtil.getStringDataByKe(MainActivity.this, SharePreferenceConstant.RENEWAL, "");
        String androidvion = SharePreferenceUtil.getStringDataByKe(MainActivity.this, SharePreferenceConstant.ANDROIDVERSION, "");
        if(renewal.equals("")||renewal==null){
            renewal();//??????
        }else{
            if(!androidvion.equals(android_version)){
                renewal();//??????
            }
        }


        if(loginReceiver  == null){

            loginReceiver = new LoginReceiver();

            this.registerReceiver(loginReceiver,new IntentFilter(GlobalConsts.ACTION_LOGIN));
        }


        if(hasPermission(PERMISSIONS_STORAGE)){

        }else{

            requstPermission(GlobalConsts.ACCESS_FINE_LOCATION,PERMISSIONS_STORAGE);

        }


        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {

        }


        byte[] tx = {1};

        byte[] rx = {1};

        int freq = 400000000;

    //    byte[] f = {(byte) (freq & 0xff),(byte) (freq >> 8),(byte) (freq >> 16),(byte) (freq >> 24)};



        long name = System.currentTimeMillis();

        byte[] bata = Tools.longToBytes(name);

        byte[] data = new byte[8];

        /*data[0] = bata[0];
        data[1] = bata[1];
        data[2] = bata[2];
        data[3] = bata[3];
        data[4] = bata[4];
        data[5] = bata[5];
        data[6] = bata[6];
        data[7] = bata[7];*/


        /*String names = String.valueOf(1)+String.valueOf(1)+String.valueOf(freq)+String.valueOf(name);

        LogUtil.i(TAG,"????????????1:"+names.toString());

        byte[] bys = names.getBytes();

        LogUtil.i(TAG,"????????????2:"+Arrays.toString(bys));

        LogUtil.i(TAG,"????????????3:"+new String(bys));

        byte [] fre = {bys[11],bys[12],bys[13],bys[14],bys[15],bys[16],bys[17],bys[18],bys[19],bys[20],bys[21],bys[22],bys[23]};

        LogUtil.i(TAG,"????????????4:"+new String(fre));

        byte[] bss = {0,1,2,3,4,5,6,7,8,9};

        String ass = Base64.encodeToString(bss,Base64.DEFAULT);

        LogUtil.i(TAG,"UUID:"+ Base64.encodeToString(bss,Base64.DEFAULT));

        byte[] bytez = Base64.decode(ass,Base64.DEFAULT);

        LogUtil.i(TAG,"UUID:"+ Arrays.toString(bytez));*/


        /*String a = "123456789@.000abc";


        LogUtil.i(TAG,"hasPermission4:"+MD5Util.encodeString(a));




        String b = Base64.encodeToString(a.getBytes(),Base64.DEFAULT);

        LogUtil.i(TAG,"hasPermission1:"+Arrays.toString(a.getBytes()));

        LogUtil.i(TAG,"hasPermission2:"+b);

        byte[] c = Base64.decode(b,Base64.DEFAULT);

        LogUtil.i(TAG,"hasPermission3:"+Arrays.toString(c));

        LogUtil.i(TAG,"hasPermission4:"+new String(c));*/





        /*int freq = 409750000;

        byte[] data = new byte[14];

        int i = 0;

        data[i++] = (byte) (1 & 0xff);
        data[i++] = (byte) (1 & 0xff);

        data[i++] = (byte) (freq & 0xff);
        data[i++] = (byte) (freq >> 8);
        data[i++] = (byte) (freq >> 16);
        data[i++] = (byte) (freq >> 24);

        long name = System.currentTimeMillis();

        byte[] bata = Tools.longToBytes(name);

        data[i++] = bata[0];
        data[i++] = bata[1];
        data[i++] = bata[2];
        data[i++] = bata[3];
        data[i++] = bata[4];
        data[i++] = bata[5];
        data[i++] = bata[6];
        data[i++] = bata[7];

        LogUtil.i(TAG, "????????????"+Arrays.toString(data));*/

        /*try {

            LogUtil.i(TAG, "????????????"+name);

            String names = new String(data,"ISO-8859-1");

            byte[] a = names.getBytes("ISO-8859-1");

            LogUtil.i(TAG, "????????????"+Arrays.toString(a));

            String l = new String(a,"ISO-8859-1");

            LogUtil.i(TAG, "????????????"+Arrays.toString(l.getBytes("ISO-8859-1")));
            byte[] lo = {a[6],a[7],a[8],a[9],a[10],a[11],a[12],a[13]};

            LogUtil.i(TAG, "????????????"+Tools.bytesToLong(lo));


            startTime();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/


        /*short[] buffer = new short[100];

        int a = 0,f=0;

        for(int i = 0;i<buffer.length;i++){


            if(a == 4){
                a=0;
                if(f==1)f=0;else f=1;

            }
            a++;
            if(f==0)
            {
                buffer[i] = 0xffff/2;
            }
            else
            {
                buffer[i]=0;
            }


        }*/

        byte[] bytes = {0,1,2,3,4,5,6,7,8,9,10};





       /* byte obj = 5;

        obj = (byte) (obj | 0x80);

        byte a = (byte) (obj & 0x80);

        obj = (byte) (obj & 0x7F);

        byte[] bytess = {0,1,2,3,4,5,6,7,8,9,10};*/

        getFirmware();//????????????


        if(MyApplicatoin.evenUser==null){//??????????????????????????????????????????MyApplicatoin.evenUser
            User user = new User();
            user.setId(Constants.USERIDLONG);//(long)22
            user.setName("dong");
            MyApplicatoin.evenUser=user;
            MyApplicatoin.evenUser.setAvatar_url("http://img2.imgtn.bdimg.com/it/u=1823758607,2323540664&fm=214&gp=0.jpg");

            if(MyApplicatoin.IS_BLE){//???????????????????????????id
                if(MyApplicatoin.interphone.getUserId() != MyApplicatoin.evenUser.getId()){

                    final Interphone inter = new Interphone();
                    inter.setUserId(MyApplicatoin.evenUser.getId());
                    MyApplicatoin.setInterphone(inter);
                    MyApplicatoin.interphone.setUserId(MyApplicatoin.evenUser.getId());
                }

            }
        }


        // ????????????
        imagePicker.setTitle("????????????");
        // ????????????????????????
        imagePicker.setCropImage(true);


        keyboard_imageView.setOnClickListener(new View.OnClickListener() {//???????????????
            @Override
            public void onClick(View view) {
                keyboard_LinearLayout.setVisibility(View.GONE);
                speak_relativeLayout.setVisibility(View.VISIBLE);
                isEditText.setFocusable(true);
                isEditText.setFocusableInTouchMode(true);
                isEditText.requestFocus();
                InputMethodManager inputManager = (InputMethodManager) isEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);//????????????
                inputManager.showSoftInput(isEditText, 0);
            }
        });
        voice_relativeLayout.setOnClickListener(new View.OnClickListener() {//???????????????
            @Override
            public void onClick(View view) {
                keyboard_LinearLayout.setVisibility(View.VISIBLE);
                speak_relativeLayout.setVisibility(View.GONE);
                bt_Voice.setVisibility(View.VISIBLE);
                InputMethodManager inputManager = (InputMethodManager) isEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);//????????????
                inputManager.hideSoftInputFromWindow(isEditText.getWindowToken(), 0);
            }
        });

        if(isInterphone()){
            title_one_group.setVisibility(View.VISIBLE);
            chat_ImageView_newgroup.setVisibility(View.GONE);
        }else{
            title_one_group.setVisibility(View.GONE);
            chat_ImageView_newgroup.setVisibility(View.VISIBLE);
        }

        ignoreBatteryOptimization(MainActivity.this);


       // Intent forgroundService = new Intent(this, BackGroundService.class);//?????????????????????????????????app
       // startService(forgroundService);
    }

    private void setOnInputMethodManager() {

        InputMethodManager manager = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);

        if(manager == null){

            return;
        }

    }

    private void setData() {

        /*mesage_entity_list.clear();
        if(adapter == null){

            adapter = new ChatAdapter(MainActivity.this,mesage_entity_list);

            lv.setAdapter(adapter);
            lv.setSelection(adapter.getCount());

        }else{
            adapter.notifyDataSetChanged();
          //  adapter.notifyData(lv);
            lv.setSelection(adapter.getCount());
        }*/


        if(isInterphone()){

            title_one_group.setText(MyApplicatoin.interphone.getImCode().getHite());
            title_to_group.setText(MyApplicatoin.interphone.getImCode().getHite());
            title_one_group.setVisibility(View.VISIBLE);
            chat_ImageView_newgroup.setVisibility(View.GONE);

            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {

                        mesage_entity_list.clear();


                        mGroupDao =  DAOService.get(getApplication()).getsession().getDAOGroupDao();

                        mMesg =  DAOService.get(getApplication()).getsession().getDAOMesgDao();

                        group_list = mGroupDao.loadAll();

                        memberDao = DAOService.get(MainActivity.this).getsession().getDAOmemberDao();

                        List<DAOmember> member_list = memberDao.loadAll();

                        if (group_list != null) {

                            LogUtil.i(TAG,"setData:"+Tools.getGroupName(MyApplicatoin.interphone.getImCode().getName()));

                            for(DAOGroup groups : group_list){

                                if(String.valueOf(groups.getGroupId()).equals(Tools.getGroupName(MyApplicatoin.interphone.getImCode().getName()))){

                                    group = groups;

                                }

                            }

                            if(group != null && String.valueOf(group.getGroupId()).equals(Tools.getGroupName(MyApplicatoin.interphone.getImCode().getName()))){

                                MyApplicatoin.interphone.getImCode().setHite(group.getName());

                                page = 0;

                                List<DAOMesg> mesg_list = mMesg.queryBuilder()
                                        .orderDesc(DAOMesgDao.Properties.Id)
                                        .offset(page * list_item_size)
                                        .limit(list_item_size)
                                        .where(DAOMesgDao.Properties.From_id.eq(group.getGroupId()))
                                        .list();

                                final List<DAOMesg> mesgs = new ArrayList<>();

                                //??????????????????


                                if(mesg_list != null){

                                    for(int i = mesg_list.size()-1; i>=0; i--){
                                        mesgs.add(mesg_list.get(i));
                                    }

                                    for(DAOMesg m : mesgs){

                                        Mesage_entity msg = new Mesage_entity();

                                        msg.setId(m.getId());
                                        msg.setContent(m.getContent());
                                        msg.setMgid(m.getMgid());
                                        msg.setContent_length(m.getContent_length());
                                        msg.setCreate_time(m.getCreate_time().getTime());
                                        msg.setFrom_id(m.getFrom_id());
                                        msg.setUser_id(m.getUser_id());
                                        msg.setIs_ok((byte) 2);
                                        msg.setMesg_type(m.getMesg_type());

                                        for(DAOmember mem :member_list){

                                            if(mem.getUser_id() == m.getUser_id() && mem.getGroup_name().equals(String.valueOf(m.getFrom_id()))){
                                                msg.setSex(mem.getSex());
                                                msg.setAvatar_url(mem.getAvatar_url());
                                                msg.setUser_neme(mem.getUser_name());
                                            }
                                        }
                                        mesage_entity_list.add(msg);

                                    }
                                }

                            //    LogUtil.i(TAG,"222222222:"+mesage_entity_list.toString());

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(adapter == null){

                                            adapter = new ChatAdapter(MainActivity.this,mesage_entity_list);

                                            lv.setAdapter(adapter);
                                            lv.setSelection(mesage_entity_list.size()-1);

                                        }else{
                                            adapter.notifyDataSetChanged();
                                         //   adapter.notifyData(lv);
                                            lv.setSelection(mesage_entity_list.size()-1);
                                        }
                                    }
                                });
                            }
                        }
                    } catch (Exception e) {
                        ExceptionUtil.handleException(e);
                    }
                }
            }).start();

        }else{
            title_one_group.setVisibility(View.GONE);
            chat_ImageView_newgroup.setVisibility(View.VISIBLE);
        }

        //???????????????
        /*for(int i = 0;i<10;i++){
            int max=20000;
            int min=1000;
            Random random = new Random();

            int s = random.nextInt(max)%(max-min+1) + min;


        }*/

    }



    private void addListener() {


        is_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(setSwitch){

                    setSwitch = false;

                    //    bt_Voice.startAnimation(xAnim);

                    img_switch.setImageResource(R.mipmap.chat_button_titlehint_false);


                    // ????????????
                    /*TranslateAnimation mHiddenAction = new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, -1.0f);
                    mHiddenAction.setDuration(1000);

                    bt_Voice.startAnimation(mHiddenAction);//????????????*/
                 //   chat_layout.startAnimation(mHiddenAction);
                 //   bt_Voice.setVisibility(View.GONE);
                }else{

                    setSwitch = true;


                    //    bt_Voice.startAnimation(yAnim);
                    img_switch.setImageResource(R.mipmap.chat_button_titlehint_true);

                    // ????????????
                    /*TranslateAnimation mShowAction = new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, -1.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f);
                    mShowAction.setRepeatMode(Animation.REVERSE);
                    mShowAction.setDuration(1000);
                //    chat_layout.startAnimation(mShowAction);
                    bt_Voice.startAnimation(mShowAction);*///????????????
                    bt_Voice.setVisibility(View.VISIBLE);
                }
                if (adapter != null) {
                    lv.setAdapter(adapter);
                    lv.setSelection(adapter.getCount());
                }
            }
        });


        mToolBarTextView.setOnClickListener(new NoDoubleClickListener(){
            @Override
            public void onNoDoubleClick(View v) {

               // startActivity(new Intent(MainActivity.this,BlescanActivity.class));
              //  overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });


        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendText();


            }
        });



        lv.setonRefreshListener(new ChatListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new AsyncTask<Void, Void, Integer>() {
                    protected Integer doInBackground(Void... params) {
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            ExceptionUtil.handleException(e);
                        }
                        return setReflashData();
                    }

                    @Override
                    protected void onPostExecute(Integer result) {
                        if(adapter != null){
                            adapter.notifyDataSetChanged();
                        //    lv.smoothScrollToPosition(mesage_entity_list.size()- result + 1);
                        }
                        lv.onRefreshComplete();
                    }
                }.execute(null, null, null);
            }
        });



        /**
         *
         * ????????????
         *
         * */
        bt_Voice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //    LogUtil.i(GlobalConsts.TAG,"button???????????????");

                if(hasPermission(Manifest.permission.RECORD_AUDIO)){

                    int action = event.getAction();

                    switch (action) {

                        case MotionEvent.ACTION_DOWN:

                            MyApplicatoin.isstart = true;

                            if(isInterphone()){

                                startVoice();
                            }else{

                                if(MyApplicatoin.evenUser==null){//??????????????????????????????????????????MyApplicatoin.evenUser
                                    User user = new User();
                                    user.setId(Constants.USERIDLONG);
                                    user.setName("dong");
                                    MyApplicatoin.evenUser=user;
                                    MyApplicatoin.evenUser.setAvatar_url("http://img2.imgtn.bdimg.com/it/u=1823758607,2323540664&fm=214&gp=0.jpg");

                                    if(MyApplicatoin.IS_BLE){//???????????????????????????id
                                        if(MyApplicatoin.interphone.getUserId() != MyApplicatoin.evenUser.getId()){

                                            final Interphone inter = new Interphone();
                                            inter.setUserId(MyApplicatoin.evenUser.getId());
                                            MyApplicatoin.setInterphone(inter);
                                            MyApplicatoin.interphone.setUserId(MyApplicatoin.evenUser.getId());
                                        }

                                    }
                                }
                                startVoice();
                                //stopVoice();

                            }


                            break;
                        case MotionEvent.ACTION_UP:

                            MyApplicatoin.isstart = false;
                           /* if(MyApplicatoin.evenUser.getId().equals(22L)){//?????????????????????????????????????????????????????????MyApplicatoin.evenUser??????
                                MyApplicatoin.evenUser=null;
                            }*/
                            stopVoice();

                            break;

                    }

                }else {
                    //????????????

                    long currentTime = Calendar.getInstance().getTimeInMillis();

                    if (currentTime - lastClick > CLICK_DELAY_TIME) {
                        lastClick = currentTime;
                        requstPermission(GlobalConsts.RECORD_AUDIO, Manifest.permission.RECORD_AUDIO);
                    }

                }
                return false;
            }
        });



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                long currentTime = Calendar.getInstance().getTimeInMillis();

                /*if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                    lastClickTime = currentTime;

                    LogUtil.i(TAG,"position:"+position+",view:"+view.toString());

                    boolean isClick = false;

                    if(itemClick == position){
                        isClick = true;
                    }

                    itemClick = position;

                    Mesage_entity message = mesage_entity_list.get(position - 1);

                    if(message.getMesg_type() == 3){


                        if(isClick && player.isPaused()){

                            player.setPaused(false);

                        }else{

                            if(player != null){

                                if(player.isPaused()){
                                    player.setPaused(false);
                                }
                            }

                            ImageView img = view.findViewById(R.id.imageView_chat_audio);

                        *//*    player = new SpeexInput(message.getUser_id(),message.getContent(),img);

                            Thread thread = new Thread(player);
                            thread.start();*//*

                        }


                    }

                }*/

            }
        });

    }

    /**
     *
     * ????????????
     *
     * */

    private void sendText() {
        try {
            if (isEditText.getText().length() > 0) {

                if(isInterphone()){


                    // openfire Message ??????

                    long s = getRandom();

                    String message = isEditText.getText().toString();

                    Openfrom from = new Openfrom();

                    from.setType(MsgResponse.Text);

                    from.setMessage(message);

                    from.setCode(Open_biz.SendMessage);

                    from.setUserId(MyApplicatoin.evenUser.getId());

                    from.setMessageID(s);

                    MyApplicatoin.setOpenfire(from);


                    LogUtil.i(TAG,"setOpenfire");
                    LogUtil.i(TAG,"sendText");


                    if (MyApplicatoin.interphone.getRf() == null){

                        isEditText.setText(null);

                        return;
                    }

                    if(MyApplicatoin.interphone.getState() == 3 && MyApplicatoin.interphone.getRf() != 1){


                        Interphone interphone = new Interphone();

                        MsgResponse msg = new MsgResponse();
                        msg.setUserId(MyApplicatoin.interphone.getUserId());
                        msg.setContent(message);
                        msg.setType(MsgResponse.Text);
                        LogUtil.i(TAG,"onReceive send:"+Tools.getGroupName(MyApplicatoin.interphone.getImCode().getName()));
                        msg.setFormName(Tools.getGroupName(MyApplicatoin.interphone.getImCode().getName()));
                        msg.setPacketID(s);

                        interphone.setMsgResponse(msg);

                        MyApplicatoin.setInterphone(interphone);

                    }


                    Date time = new Date();
                    Filter filter = new Filter();

                    filter.setPacketID(s);
                    filter.setTime(time.getTime());
                    filter.setType(MsgResponse.Text);
                    filter.setCode(Filter.phone);
                    filter.setUserId(MyApplicatoin.interphone.getUserId());

                    filter.setGroupName(Tools.getGroupName(MyApplicatoin.interphone.getImCode().getName()));

                    MsgResponse response = new MsgResponse();

                    response.setFormName(Tools.getGroupName(MyApplicatoin.interphone.getImCode().getName()));
                    response.setPacketID(s);
                    response.setType(MsgResponse.Text);
                    response.setContent(message);
                    response.setTime(time);
                    response.setUserId(MyApplicatoin.interphone.getUserId());

                    //?????????????????????
                    MyApplicatoin.startMessageFilter(filter,response);

                    isEditText.setText(null);

                }

            }

        } catch (Exception e) {

            ExceptionUtil.handleException(e);

        }
    }


    /**
     * ???????????????
     * */
    private void reset() {

        //????????????
        long currentTime = Calendar.getInstance().getTimeInMillis();

        upClickTime = currentTime;

        mTime = 0;

    }

    private void setUrl() {


        /*new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient httpClient = new OkHttpClient();
                String url = "http://192.168.1.60:8080/acount_server/app/app_sms_android_das_6908031.apk";

                Request request = new Request.Builder()
                        //?????????????????????,????????????,???????????????????????????????????????????????????
                        .url(url)
                        .build();
                okhttp3.Call call = httpClient.newCall(request);



                try {
                    Response response = call.execute();

                    String apkurl = MainActivity.this.getExternalCacheDir().getPath()+"/data/smsApk.apk";

                    File file = new File(apkurl);

                    InputStream is = null;
                    FileOutputStream fileOutputStream = null;
                    try {
                        LogUtil.i(TAG,"length:"+response.body().contentLength());

                        is = response.body().byteStream();
                        fileOutputStream = new FileOutputStream(file, true);
                        byte[] buffer = new byte[2048];//????????????2kB
                        int len;
                        while ((len = is.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, len);
                            LogUtil.i(TAG,"length:"+len);
                        }
                        fileOutputStream.flush();

                        Intent intents = new Intent();

                        intents.setAction("android.intent.action.VIEW");
                        intents.addCategory("android.intent.category.DEFAULT");
                        intents.setType("application/vnd.android.package-archive");
                        intents.setData(Uri.parse(apkurl));
                        intents.setDataAndType(Uri.parse(apkurl),"application/vnd.android.package-archive");
                        intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        // ??????????????????????????????apk???????????????????????????????????????

                        startActivity(intents);
                    } finally {
                        //??????IO???
                        is.close();
                        fileOutputStream.close();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/

        /*Precursor precursor = new Precursor();

        precursor.setSeries("sms");

        precursor.setAndroid_url("http://localhost:8080/acount_server/aap/app_sms_android_dsada_8740860.apk");

        final int versionCode = getVersionCode(MainActivity.this);

        Production production = new Production();

        production.setModle("sms");
        production.setHardware_version("sms1");

        sms_request.dorequest(MainActivity.this, sms_request.GET, precursor, new RequestCB() {
            @Override
            public void respone(int opcode, Object obj) {

                if(opcode == sms_request.GET){


                    LogUtil.i(TAG,"android ????????????"+versionCode);

                }

            }
        });*/

    }



    private void setView() {
        // TODO Auto-generated method stub

    //    ttt = findViewById(R.id.textView8);

        shareGroup = (LinearLayout) findViewById(R.id.chat_draglayout_sharegroup);
        newGroup = (LinearLayout) findViewById(R.id.chat_draglayout_newgroup);
        histruyGroup = (LinearLayout) findViewById(R.id.chat_draglayout_histroy);
        joinGroup = (LinearLayout) findViewById(R.id.chat_draglayout_joingroup);
        leaveGroup = (LinearLayout) findViewById(R.id.chat_draglayout_leavegroup);
    //    locationSharing = (LinearLayout) findViewById(R.id.chat_draglayout_map);
        device = (LinearLayout) findViewById(R.id.chat_draglayout_device);


        chat_ImageView_newgroup= (ImageView) findViewById(R.id.chat_ImageView_newgroup);


    //    manual = (LinearLayout) findViewById(R.id.main_draglayout_manual);
        faq = (LinearLayout) findViewById(R.id.main_draglayout_faq);
    //    feedback = (LinearLayout) findViewById(R.id.main_draglayout_feedback);
        update = (LinearLayout) findViewById(R.id.main_draglayout_update);
        about = (LinearLayout) findViewById(R.id.main_draglayout_about);


        signout = (LinearLayout) findViewById(R.id.main_draglayout_signout);

        main_layout_right = (RelativeLayout) findViewById(R.id.sms_main_layout_right);
        main_layout_left = (RelativeLayout) findViewById(R.id.sms_main_layout_left);
        chat_layout = (RelativeLayout) findViewById(R.id.chat_main_layout);

        erro_hite_layout = (RelativeLayout) findViewById(R.id.chat_hite_layout);
        chatDraglayoutDeviceBtn = (Button) findViewById(R.id.chat_draglayout_device_btn);

        mToolBarTextView = (TextView) findViewById(R.id.sms_main_title);

        user_name = (TextView) findViewById(R.id.sms_main_user_name);

        user_freq = (TextView) findViewById(R.id.sms_main_device_freq);

        user_icon = (CircleImageView) findViewById(R.id.sms_main_icon);

        sms_main_ico_profile = (ImageView) findViewById(R.id.sms_main_ico_profile);

        sms_return_icon = (ImageView) findViewById(R.id.sms_return_icon);


        bt_send = (Button) findViewById(R.id.chat_button_send);

        bt_Voice = (RelativeLayout) findViewById(R.id.chat_voice_button);

        bt_Voice.setLongClickable(true);
        bt_Voice.setBackgroundResource(R.mipmap.yuxinluse);//??????????????????

        isEditText = (EditText) findViewById(R.id.chat_edtext);

       // isEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(60)}); //??????????????????

        lv = (ChatListView) findViewById(R.id.chat_listView);


        /*lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lv.setStackFromBottom(true);*/

        pTT = (PttRelativeLayout) findViewById(R.id.chat_pttlayout);

        ptt_switch = (SlideSwitch) findViewById(R.id.ptt_switch);

        tv_voice_hint = (TextView) findViewById(R.id.chat_voice_textview_hint);

        tv_voice_content = (TextView) findViewById(R.id.chat_voice_textview_content);


        is_switch = (RelativeLayout) findViewById(R.id.chat_button_switch_layout);

        img_switch = (ImageView) findViewById(R.id.chat_button_switch_imageview);

        title_one_layout = (LinearLayout) findViewById(R.id.device_one_layout);
        title_to_layout = (LinearLayout) findViewById(R.id.device_to_layout);
        title_one_group = (TextView) findViewById(R.id.device_title_group_name1);
        title_to_group = (TextView) findViewById(R.id.device_title_group_name2);
        title_one_freq = (TextView) findViewById(R.id.device_title_freq1);
        title_to_freq = (TextView) findViewById(R.id.device_title_freq2);
        tv_electricQuantity = (TextView) findViewById(R.id.tv_electricQuantity);
        title_online = (TextView) findViewById(R.id.title_online);

        voice_relativeLayout= (LinearLayout) findViewById(R.id.voice_relativeLayout);
        keyboard_imageView= (ImageView) findViewById(R.id.keyboard_imageView);
        keyboard_LinearLayout= (LinearLayout) findViewById(R.id.keyboard_LinearLayout);
        speak_relativeLayout= (LinearLayout) findViewById(R.id.speak_relativeLayout);


        title_mode = (TextView) findViewById(R.id.device_title_mode);
        title_tc = (TextView) findViewById(R.id.device_title_tc);
        title_rc = (TextView) findViewById(R.id.device_title_rc);

        powerView1 = (PowerView) findViewById(R.id.device_title_vole1);
        powerView2 = (PowerView) findViewById(R.id.device_title_vole2);

        mPopupWindow = new PopupWindow(this);
        animUtil = new AnimUtil();

        AssetManager assets = getAssets();

        Typeface fromAsset_name = Typeface.createFromAsset(assets, "fonts/Helvetica_LT_Condensed_Bold.ttf");
        Typeface fromAsset_freq = Typeface.createFromAsset(assets, "fonts/Helvetica_CE_Regular.ttf");
        Typeface fromAsset_to = Typeface.createFromAsset(assets, "fonts/Helvetica_LT_Light.ttf");

        title_one_group.setTypeface(fromAsset_name);
        title_one_freq.setTypeface(fromAsset_freq);

        title_to_group.setTypeface(fromAsset_to);
        title_to_freq.setTypeface(fromAsset_to);

        shareGroup.setOnClickListener(this);
        newGroup.setOnClickListener(this);
        histruyGroup.setOnClickListener(this);
        joinGroup.setOnClickListener(this);
        leaveGroup.setOnClickListener(this);
    //    locationSharing.setOnClickListener(this);
        device.setOnClickListener(this);
        chatDraglayoutDeviceBtn.setOnClickListener(this);
        chat_ImageView_newgroup.setOnClickListener(this);

        signout.setOnClickListener(this);

    //    manual.setOnClickListener(this);
        faq.setOnClickListener(this);
    //    feedback.setOnClickListener(this);
        update.setOnClickListener(this);
        about.setOnClickListener(this);
    //    settings.setOnClickListener(this);

        sms_main_ico_profile.setOnClickListener(this);
        sms_return_icon.setOnClickListener(this);
        user_icon.setOnClickListener(this);
        user_name.setOnClickListener(this);


        isEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*??????????????????GO??????*/
                if(actionId == EditorInfo.IME_ACTION_SEND){
                    /*???????????????*/
                    /*InputMethodManager imm = (InputMethodManager) v
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(
                                v.getApplicationWindowToken(), 0);
                    }*/

                    sendText();

                    return true;
                }
                return false;
            }
        });

        ptt_switch.setSlideListener(new SlideSwitch.SlideListener() {
            @Override
            public void open() {
                pTT.setOpen(true);
            }

            @Override
            public void close() {
                pTT.setOpen(false);
            }
        });

        pTT.setOnPttLayoutListener(new PttRelativeLayout.onPttLayoutListener() {
            @Override
            public void onLongStart() {
                MyApplicatoin.isstart = true;
                if(hasPermission(Manifest.permission.RECORD_AUDIO)){

                    if(isInterphone()){

                        startVoice();

                    }else{

                        stopVoice();

                    }

                }else {
                    //????????????

                    long currentTime = Calendar.getInstance().getTimeInMillis();

                    if (currentTime - lastClick > CLICK_DELAY_TIME) {
                        lastClick = currentTime;
                        requstPermission(GlobalConsts.RECORD_AUDIO, Manifest.permission.RECORD_AUDIO);
                    }

                }

            }

            @Override
            public void onLongStop() {
                MyApplicatoin.isstart = false;
                stopVoice();
            }

            @Override
            public void onInterceptTouch() {

                InputMethodManager imm = (InputMethodManager) pTT
                        .getContext().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(
                            pTT.getApplicationWindowToken(), 0);
                }

            }
        });



        title_one_layout.setOnClickListener(new NoDoubleClickListener(){
            @Override
            public void onNoDoubleClick(View v) {

                if(isTitleSwitch){

                    isTitleSwitch = false;

                    title_one_layout.setVisibility(View.GONE);
                    title_to_layout.setVisibility(View.VISIBLE);


                }

            }
        });

        title_to_layout.setOnClickListener(new NoDoubleClickListener(){
            @Override
            public void onNoDoubleClick(View v) {

                if(!isTitleSwitch){

                    isTitleSwitch = true;

                    title_to_layout.setVisibility(View.GONE);
                    title_one_layout.setVisibility(View.VISIBLE);

                }

            }
        });



        if(MyApplicatoin.current_network_type == MyApplicatoin.NETWORK_TYPE_NONE){
            erro_hite_layout.setVisibility(View.VISIBLE);
        }

    }

    private void showLoginDialog(String description) {

        final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
        View view = View.inflate(MainActivity.this, R.layout.dialog_link_layout, null);
        dialog.setView(view);
        dialog.setInverseBackgroundForced(true);
        dialog.setCancelable(false);
        final Button bt_cancel = (Button) view.findViewById(R.id.button_cancel);
        final Button bt_ok = (Button) view.findViewById(R.id.button_ok);
        final TextView tv_content = (TextView) view.findViewById(R.id.content);
        final TextView tv_title = (TextView) view.findViewById(R.id.title);

        tv_title.setText(getResources().getString(R.string.sms_main_updata_apk));

        tv_content.setText(description);

        //dialog????????????

        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.cancel();
            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            //    startActivity(new Intent(MainActivity.this,LoginActivity.class));


                Intent intent = new Intent(MainActivity.this, UpdataService.class);
                intent.setAction(UpdataService.APK);
                bindService(intent, connection, Context.BIND_AUTO_CREATE);

                dialog.cancel();


                if(!MyApplicatoin.isUpdata){

                }else{
                    Tools.showInfo(MainActivity.this,getResources().getString(R.string.isupdata));
                }

               // startActivity(new Intent(Intent.ACTION_VIEW, android.net.Uri.parse("http://smart.nitecore.com/app/app_SMART1_android_31_8197745.apk" )));
                dialog.cancel();
            }
        });
        dialog.show();

    }

    private void showLoginDialogFirst(String description) {

        final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
        View view = View.inflate(MainActivity.this, R.layout.dialog_link_layout, null);
        dialog.setView(view);
        dialog.setInverseBackgroundForced(true);
        dialog.setCancelable(false);
        final Button bt_cancel = (Button) view.findViewById(R.id.button_cancel);
        bt_cancel.setText("????????????");
        final Button bt_ok = (Button) view.findViewById(R.id.button_ok);
        final TextView tv_content = (TextView) view.findViewById(R.id.content);
        final TextView tv_title = (TextView) view.findViewById(R.id.title);

        tv_title.setText(getResources().getString(R.string.sms_main_updata_apk));

        tv_content.setText(description);

        //dialog????????????

        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharePreferenceUtil.saveStringDataToSharePreference(MainActivity.this, SharePreferenceConstant.ANDROIDVERSION, android_version);
                dialog.cancel();
            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //    startActivity(new Intent(MainActivity.this,LoginActivity.class));


                Intent intent = new Intent(MainActivity.this, UpdataService.class);
                intent.setAction(UpdataService.APK);
                bindService(intent, connection, Context.BIND_AUTO_CREATE);

                dialog.cancel();


                if(!MyApplicatoin.isUpdata){

                }else{
                    Tools.showInfo(MainActivity.this,getResources().getString(R.string.isupdata));
                }

                // startActivity(new Intent(Intent.ACTION_VIEW, android.net.Uri.parse("http://smart.nitecore.com/app/app_SMART1_android_31_8197745.apk" )));
                dialog.cancel();
            }
        });
        dialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
    //    dragLayout.close();
        try {
            setUserData();

            if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
                ////???????????????????????????????????????
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        int tsl = 0;

                        while (tsl < 20){

                            tsl++;

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            if(mBluetoothAdapter.isEnabled()){

                                tsl = 50;
                            }
                        }
                    }
                }).start();

            } else  {

                if(MyApplicatoin.IS_BLE  == false){

                    mToolBarTextView.setText(getResources().getString(R.string.sms_ble_scnn_title));

                    Interphone interphone1 = new Interphone();
                    interphone1.setConnect((byte) 1);
                    MyApplicatoin.setInterphone(interphone1);

                }else{

                    setGroup();

                }
            }

            /*if(MyApplicatoin.evenUser != null){
                getFirmware();
            }*/

        } catch (Resources.NotFoundException e) {
            ExceptionUtil.handleException(e);
        }
    }

    private void setGroup() {
        try {

            if (MyApplicatoin.IS_BLE) {


                /*if(MyApplicatoin.interphone.getPlays() != null){
                    ttt.setText(String.valueOf(MyApplicatoin.interphone.getPlays()));
                }*/

                if(MyApplicatoin.interphone.getName() != null){

                    mToolBarTextView.setText(MyApplicatoin.interphone.getName());

                }

                if(isTitleSwitch == false){

                    title_one_layout.setVisibility(View.GONE);
                    title_to_layout.setVisibility(View.VISIBLE);


                }else {

                    title_one_layout.setVisibility(View.VISIBLE);
                    title_to_layout.setVisibility(View.GONE);

                }



                if(MyApplicatoin.interphone != null){

                    if(MyApplicatoin.interphone.getVolt() != 0){

                        powerView1.setPower((short)(Integer.parseInt(MyApplicatoin.interphone.getPower())));//MyApplicatoin.interphone.getVolt()
                        powerView2.setPower((short)(Integer.parseInt(MyApplicatoin.interphone.getPower())));//MyApplicatoin.interphone.getVolt()

                    }

                    if(MyApplicatoin.interphone.getRf() != null){

                        if(MyApplicatoin.interPhoneRF == 1){


                            if(MyApplicatoin.interphone.getRf() == 1){

                                title_mode.setText(getResources().getString(R.string.sms_ble_setting_rf_off));

                            }else if(MyApplicatoin.interphone.getRf() == 2){

                                title_mode.setText(getResources().getString(R.string.sms_ble_setting_rf_on));

                            }

                        }else{

                            if(MyApplicatoin.interPhoneRF == 2){

                                title_mode.setText(getResources().getString(R.string.sms_deivce_title_automation));

                            }

                        }



                    }

                    if(MyApplicatoin.interphone.getFrequne() != null){

                        title_one_freq.setText(getFreq(MyApplicatoin.interphone.getFrequne()));
                        title_to_freq.setText(getFreq(MyApplicatoin.interphone.getFrequne()));
                        tv_electricQuantity.setText(MyApplicatoin.interphone.getPower()+"%");//MyApplicatoin.interphone.getPower()  ????????????


                    }

                    if(MyApplicatoin.interphone.getTxcode() != null){

                        if(MyApplicatoin.interphone.getTxcode() > 0){
                            title_tc.setText(tx_code_data[MyApplicatoin.interphone.getTxcode()-2]);
                        }else{
                            title_tc.setText(tx_code_data[MyApplicatoin.interphone.getTxcode()]);
                        }


                    }
                    if(MyApplicatoin.interphone.getRxcode() != null){

                        if(MyApplicatoin.interphone.getRxcode() > 0){
                            title_rc.setText(tx_code_data[MyApplicatoin.interphone.getRxcode()-2]);
                        }else{
                            title_rc.setText(tx_code_data[MyApplicatoin.interphone.getRxcode()]);
                        }


                    }

                    if(isInterphone()){

                        title_one_group.setText(MyApplicatoin.interphone.getImCode().getHite());
                        title_to_group.setText(MyApplicatoin.interphone.getImCode().getHite());

                    }else{

                        title_one_group.setText(getResources().getString(R.string.sms_deivce_title_group_hite));
                        title_to_group.setText(getResources().getString(R.string.sms_deivce_title_group_hite));

                    }

                }


            }else{

                title_one_layout.setVisibility(View.GONE);
                title_to_layout.setVisibility(View.GONE);

                /*mesage_entity_list.clear();
                if(adapter == null){

                    adapter = new ChatAdapter(MainActivity.this,mesage_entity_list);

                    lv.setAdapter(adapter);
                    lv.setSelection(adapter.getCount());

                }else{
                  //  adapter.notifyDataSetChanged();
                   adapter.notifyData(lv);
                //    lv.setSelection(adapter.getCount());
                }*/

            }

            if(MyApplicatoin.interphone != null){

                if(MyApplicatoin.interphone.getFrequne() != null){

                    user_freq.setText(getFreq(MyApplicatoin.interphone.getFrequne()));
                }else{
                    user_freq.setText(getResources().getString(R.string.device));
                }

            }else{
                user_freq.setText(getResources().getString(R.string.device));
            }


            if(MyApplicatoin.IS_BLE){



                byte[] data = null;
                int txCode = 0;
                int rxCode = 0;
                int freq = 0;
                String fromName = null;

                if(isInterphone()){

                    LogUtil.i(TAG,"wName:"+ Arrays.toString(MyApplicatoin.interphone.getImCode().getName().getBytes()));

                    fromName = Tools.getGroupName(MyApplicatoin.interphone.getImCode().getName());

                    if(fromName == null){
                        return;
                    }
                    if(fromName.equals("0")){

                        return;

                    }

                    if(MyApplicatoin.interphone.getImCode().getHite() != null){

                        title_one_group.setText(MyApplicatoin.interphone.getImCode().getHite());
                        title_to_group.setText(MyApplicatoin.interphone.getImCode().getHite());

                    }else{

                        title_one_group.setText(fromName);
                        title_to_group.setText(fromName);

                    }


                    LogUtil.i(TAG,"fromName:"+fromName);
                    LogUtil.i(TAG,"CurrentRoomName:"+CurrentRoomName);

                    LogUtil.i(TAG,"onResume???"+CurrentRoomName.equals(fromName));


                    if(!CurrentRoomName.equals(fromName)){

                        data = Tools.getDataCode(MyApplicatoin.interphone.getImCode().getName());

                        CurrentRoomName = fromName;

                        //??????RF?????? ??????????????????onc

                        if(MyApplicatoin.interphone.getRf() == null){

                            /*if(MyApplicatoin.current_network_type != MyApplicatoin.NETWORK_TYPE_NONE){

                                if(!MyApplicatoin.IS_OPEN_JOIN){

                                    Openfrom openfrom = new Openfrom();

                                    openfrom.setUserId(String.valueOf(MyApplicatoin.evenUser.getId()));

                                    openfrom.setFromName(fromName);

                                    openfrom.setCode(Open_biz.Joinfrom);

                                    MyApplicatoin.setOpenfire(openfrom);

                                }

                            }*/

                        }else{

                            if(MyApplicatoin.interphone.getRf() == 1){


                                if(data != null){

                                    txCode = data[0] & 0xFF;
                                    rxCode = data[1] & 0xFF;

                                    freq = (data[2] & 0xFF
                                            | (data[3] & 0xFF) << 8
                                            | (data[4] & 0xFF) << 16
                                            | (data[5] & 0xFF) << 24);



                                    LogUtil.i(TAG,"txCode:"+txCode+",rxCode:"+rxCode+",freq:"+freq+",fromName:"+fromName+",data:"+Arrays.toString(data));

                                    if(txCode != MyApplicatoin.interphone.getTxcode()){

                                        /*mesage_entity_list.clear();

                                        if (adapter != null) {

                                        //    adapter.notifyDataSetChanged();
                                            adapter.notifyData(lv);

                                        //    lv.setSelection(adapter.getCount());
                                        }*/

                                        Interphone interphone = new Interphone();
                                        interphone.setTxcode(txCode);

                                        MyApplicatoin.setInterphone(interphone);

                                    }
                                    if(txCode != MyApplicatoin.interphone.getRxcode()){

                                        /*mesage_entity_list.clear();

                                        if (adapter != null) {

                                        //    adapter.notifyDataSetChanged();

                                            adapter.notifyData(lv);
                                        //    lv.setSelection(adapter.getCount());
                                        }*/

                                        Interphone interphone = new Interphone();
                                        interphone.setRxcode(rxCode);
                                        MyApplicatoin.setInterphone(interphone);

                                    }
                                    if(freq != MyApplicatoin.interphone.getFrequne()){

                                        Interphone interphone = new Interphone();
                                        interphone.setFrequne(freq);
                                        MyApplicatoin.setInterphone(interphone);

                                    }
                                }

                            }

                            /*if(MyApplicatoin.current_network_type != MyApplicatoin.NETWORK_TYPE_NONE){

                                if(!MyApplicatoin.IS_OPEN_JOIN){

                                    Openfrom openfrom = new Openfrom();

                                    openfrom.setUserId(String.valueOf(MyApplicatoin.evenUser.getId()));

                                    openfrom.setFromName(fromName);

                                    openfrom.setCode(Open_biz.Joinfrom);

                                    MyApplicatoin.setOpenfire(openfrom);
                                }

                            }*/
                        }
                    }

                    if(!MyApplicatoin.IS_OPEN_JOIN){

                        if(MyApplicatoin.current_network_type != MyApplicatoin.NETWORK_TYPE_NONE){

                            page = 0;

                            Openfrom openfrom = new Openfrom();

                            openfrom.setUserId(MyApplicatoin.evenUser.getId());

                            openfrom.setFromName(fromName);

                            openfrom.setCode(Open_biz.Joinfrom);

                            MyApplicatoin.openfrom = openfrom;

                            MyApplicatoin.setOpenfire(openfrom);

                            MyApplicatoin.IS_OPEN_JOIN = true;

                            SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(GlobalConsts.ACTION_IM_GROUP, Context.MODE_PRIVATE);

                            String id = sharedPreferences.getString("id", null);
                            String name = sharedPreferences.getString("name", null);
                            String b = sharedPreferences.getString("boolean", null);

                            if(name == null){
                                return;
                            }
                            if(id == null){
                                return;
                            }
                            if(b == null){
                                return;
                            }


                            /*if(id.equals(fromName) && b.equals("false")){

                                Openfrom openfroms = new Openfrom();

                                openfroms.setType(MsgResponse.Join);

                                openfroms.setMessageID(getRandom());
                                openfroms.setUserId(Long.valueOf(name));
                                openfroms.setFromName(id);
                                openfroms.setCode(Open_biz.ChatMessage);

                                MyApplicatoin.setOpenfire(openfrom);

                                LogUtil.i(TAG,"??????????????????????????????");
                            }*/
                        }

                    }

                }

            }
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }

    }


    private void setUserData() {

        if(MyApplicatoin.evenUser != null){
            if(MyApplicatoin.evenUser.getId().equals(Constants.USERIDLONG)){//????????????MyApplicatoin.evenUser???????????????
                user_name.setText(getResources().getString(R.string.sms_main_user_name));
                user_icon.setImageResource(R.mipmap.icon_chat_01);
                return;
            }
            user_name.setText(MyApplicatoin.evenUser.getName());

            LogUtil.i(TAG,"Avatar_url:"+MyApplicatoin.evenUser.getAvatar_url());

            try {


                if(MyApplicatoin.evenUser.getAvatar_url() != null){


                    Bitmap bitmap = getLoacalBitmap(MyApplicatoin.evenUser.getAvatar_url()); //??????????????????(???cdcard?????????)

                    user_icon.setImageBitmap(bitmap);


                }else{
                    user_icon.setImageResource(R.mipmap.icon_chat_01);
                }
            } catch (Exception e) {
                ExceptionUtil.handleException(e);
            }

        }else{

            user_name.setText(getResources().getString(R.string.sms_main_user_name));
            user_icon.setImageResource(R.mipmap.icon_chat_01);

        }

    }


    /**
     * ??????????????????
     * http://bbs.3gstdy.com
     * @param url
     * @return
     */
    public Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            ExceptionUtil.handleException(e);
            return null;
        }
    }



    private void setListener() {

        /*ble_Aprs.setSlideListener(new SlideSwitch.SlideListener() {
            @Override
            public void open() {
                if(MyApplicatoin.IS_BLE){
                    MyApplicatoin.BLE_APRS = true;
                }else{
                    ble_Aprs.setState(false);

                    Tools.showInfo(MainActivity.this,getResources().getString(R.string.sms_main_hite_aprs));
                }
            }

            @Override
            public void close() {
                if(MyApplicatoin.IS_BLE){
                    MyApplicatoin.BLE_APRS = false;
                }
            }
        });*/




    }





    /**
     * ???????????????????????????
     */
    /*private void setTranslucentStatus(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        SystemStatusManager tintManager = new SystemStatusManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(0);//??????????????????
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.i(TAG,"MainActivity.onDestroy");
        try {
            if(loginReceiver != null){

                this.unregisterReceiver(loginReceiver);
            }

            if(player != null){

                if(player.isPaused()){
                    player.setPaused(false);
                }
            }


            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
                dragLayout.getViewTreeObserver().removeGlobalOnLayoutListener(mOnGlobalLayoutListener);
            } else {
                dragLayout.getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        finish();
    }

    private void initDragLayout() {
        dragLayout = (DragLayout) findViewById(R.id.sms_main_draglayout);
        dragLayout.setDragListener(new DragLayout.DragListener() {
            @Override
            public void onOpen() {
            //    LogUtil.i(GlobalConsts.TAG,"onOpen");
            }

            @Override
            public void onClose() {
            //    LogUtil.i(GlobalConsts.TAG,"onClose");
            }

            @Override
            public void onDrag(float percent) {
            //    LogUtil.i(GlobalConsts.TAG,"onDrag="+percent);
            }
        });

        dragLayout.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.sms_main_user_name:

                if(MyApplicatoin.evenUser == null||MyApplicatoin.evenUser.getId().equals(Constants.USERIDLONG)){//????????????MyApplicatoin.evenUser??????????????????

                    startActivity(new Intent(MainActivity.this,LoginActivity.class));

                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }

                break;
            case R.id.sms_main_icon:
                if(MyApplicatoin.evenUser == null||MyApplicatoin.evenUser.getId().equals(Constants.USERIDLONG)){//
                    Tools.showInfo(MainActivity.this,getResources().getString(R.string.sms_dialog_login_title));
                    return;
                }

                try {
                    if(MyApplicatoin.evenUser != null){

                        if(hasPermission(Manifest.permission.CAMERA)){


                            // ???????????????????????????????????????????????????

                            chooseIcon(MainActivity.this);

                        }else{
                            requstPermission(GlobalConsts.CAMERA,Manifest.permission.CAMERA);
                        }

                    }
                } catch (Exception e) {
                    ExceptionUtil.handleException(e);
                }


                break;
      /*      case R.id.sms_main_ico_profile:

                try {
                    if(voice_Read_Thread != null){
                        stopRecord(1);
                    }

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(sms_return_icon.getWindowToken(), 0); //??????????????????

                    main_layout_right.setVisibility(View.GONE);
                    main_layout_left.setVisibility(View.VISIBLE);
                    dragLayout.open(true);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;*/
            case R.id.sms_return_icon:


                /**
                 *
                 * ???????????????????????????
                 * */


              /*  try {
                    if(voice_Read_Thread != null){
                        stopRecord(1);
                    }

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(sms_return_icon.getWindowToken(), 0); //??????????????????

                    main_layout_right.setVisibility(View.VISIBLE);
                    main_layout_left.setVisibility(View.GONE);

                    dragLayout.open(false);
                } catch (Exception e) {
                    ExceptionUtil.handleException(e);
                }*/

               //???????????????
                showPop();
                toggleBright();
                break;

            //sign out
            case R.id.main_draglayout_signout:


                if(MyApplicatoin.evenUser != null){

                    if(MyApplicatoin.evenUser.getId().equals(Constants.USERIDLONG)){//????????????MyApplicatoin.evenUser?????????????????????
                        return;
                    }


                    final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
                    View view = View.inflate(MainActivity.this, R.layout.dialog_warning_layout, null);
                    dialog.setView(view);
                    dialog.setInverseBackgroundForced(true);
                    dialog.setCancelable(false);
                    final Button cancel = (Button) view.findViewById(R.id.button_cancel);
                    final Button ok = (Button) view.findViewById(R.id.button_ok);
                    final TextView title = (TextView) view.findViewById(R.id.title);
                    final TextView content = (TextView) view.findViewById(R.id.content);

                    title.setText(getResources().getString(R.string.sms_main_sign_out));
                    content.setText(getResources().getString(R.string.sms_login_dialog_content));

                    //dialog????????????

                    Window window = dialog.getWindow();
                    window.setBackgroundDrawableResource(android.R.color.transparent);

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            dialog.cancel();
                        }
                    });
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {



                            SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(GlobalConsts.USER, Context.MODE_PRIVATE);

                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.remove(GlobalConsts.USER_ID);
                            editor.remove(GlobalConsts.USER_KEY);
                            editor.commit();

                        //    String userString[] = new String[]{sharedPreferences.getString(GlobalConsts.USER_ID, ""), sharedPreferences.getString(GlobalConsts.USER_KEY, "")};

                            mesage_entity_list.clear();

                            if (adapter != null) {

                                adapter.notifyDataSetChanged();

                                //    adapter.notifyData(lv);
                                //   lv.setSelection(adapter.getCount());
                            }

                            Openfrom openfrom = new Openfrom();

                            openfrom.setCode(Open_biz.Esc);
                            openfrom.setUserId(MyApplicatoin.evenUser.getId());


                            MyApplicatoin.setOpenfire(openfrom);

                            MyApplicatoin.evenUser = null;



                            startActivity(new Intent(MainActivity.this,LoginActivity.class));

                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


                            dialog.cancel();

                        }
                    });
                    dialog.show();


                }

                break;

            //manual
            /*case R.id.main_draglayout_manual:

                startActivity(new Intent(MainActivity.this,WebViewActivity.class));

                break;*/
            //faq
            case R.id.main_draglayout_faq:



                Intent intent1 = new Intent(MainActivity.this,HiteActivity.class);

                intent1.setAction(GlobalConsts.ACTION_FAQ);

                startActivity(intent1);

                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                break;
            //feedback
            /*case R.id.main_draglayout_feedback:

                startActivity(new Intent(MainActivity.this,WebViewActivity.class));

                break;*/
            //update
            case R.id.main_draglayout_update:

                try {

                    SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(GlobalConsts.PRECURSOR, Context.MODE_PRIVATE);
                    //android_url = sharedPreferences.getString(GlobalConsts.PRECURSOR_ANDROID_URL,null);
                    //android_version = sharedPreferences.getString(GlobalConsts.PRECURSOR_ANDROID_VERSION, null);
                   // android_description = sharedPreferences.getString(GlobalConsts.PRECURSOR_ANDROID_DESCRIPTION, null);

                   // LogUtil.i("aaaa","android_version:"+android_version+"      ,android_url:"+android_url);
                    if(android_version != null && android_url!= null && android_description != null){

                        final String versionCode = getVersionName(MainActivity.this);

                       // LogUtil.i("aaaa","versionCode:"+versionCode+",code:"+android_version+"              ??????versionCode:"+versionCode);

                        if(!versionCode.equals(android_version)){

                            if(!MyApplicatoin.isUpdata){
                                showLoginDialog(android_description);
                            }else{
                                Tools.showInfo(MainActivity.this,getResources().getString(R.string.isupdata));
                            }


                        }else{
                            Tools.showInfo(MainActivity.this,getResources().getString(R.string.sms_main_toast_hite_updata));
                        }

                    }else{
                        Tools.showInfo(MainActivity.this,getResources().getString(R.string.sms_main_toast_hite_updata));

                    }
                } catch (Resources.NotFoundException e) {
                    ExceptionUtil.handleException(e);
                }

                break;
            //about
            case R.id.main_draglayout_about:


                Intent intent2 = new Intent(MainActivity.this,HiteActivity.class);

                intent2.setAction(GlobalConsts.ACTION_ABOUT);

                startActivity(intent2);

                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.chat_draglayout_sharegroup:
                mPopupWindow.dismiss();//??????????????????
                try {

                    if (isBLE()) {
                        if(isInterphone()){

                            Intent intent = new Intent(MainActivity.this,WsActivity.class);

                            intent.putExtra(GlobalConsts.DATE_FROM_NAME,MyApplicatoin.interphone.getImCode().getName());
                            intent.putExtra(GlobalConsts.DATE_FROM_ID,MyApplicatoin.interphone.getImCode().getId().toString());
                            intent.putExtra(GlobalConsts.DATE_FROM_UUID,MyApplicatoin.interphone.getImCode().getUuid());

                            startActivity(intent);

                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                        }
                    }

                } catch (Exception e) {
                    ExceptionUtil.handleException(e);
                }


                break;
            case R.id.chat_ImageView_newgroup://chat_draglayout_newgroup    chat_ImageView_newgroup


                stopTrace();

                if(isBLE()){

                    if(MyApplicatoin.evenUser != null){


                        final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
                        View view = View.inflate(MainActivity.this, R.layout.dialog_edtext_layout, null);
                        dialog.setView(view);
                        dialog.setInverseBackgroundForced(true);
                        dialog.setCancelable(false);
                        final Button bt_cancel = (Button) view.findViewById(R.id.button_cancel);
                        final Button bt_ok = (Button) view.findViewById(R.id.button_ok);

                        final TextView tv_title = (TextView) view.findViewById(R.id.title);
                        final ImageView img_icon = (ImageView) view.findViewById(R.id.dialog_icon);
                        final EditText editText = (EditText) view.findViewById(R.id.dialog_edtext);
                        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);

                    //    checkBox.setVisibility(View.VISIBLE);
                     //  editText.addTextChangedListener(new SearchWather(editText));
                        editText.addTextChangedListener(new SearchWatherChatRoom(editText));

                        img_icon.setImageResource(R.mipmap.boxicon_new_group);
                        tv_title.setText(getResources().getString(R.string.sms_dialog_edtext_group_title));
                        editText.setHint(getResources().getString(R.string.sms_dialog_edtext_group_hite));

                        //dialog????????????

                        Window window = dialog.getWindow();
                        window.setBackgroundDrawableResource(android.R.color.transparent);

                        bt_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dragLayout.close();

                                dialog.cancel();
                            }
                        });
                        bt_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                page = 0;

                                int max=9999;
                                int min=0;
                                Random random = new Random();

                                int s = random.nextInt(max)%(max-min+1) + min;

                                LogUtil.i(TAG,"????????????"+s);

                                try {

                                    if(isBLE()){

                                        String titleName = editText.getText().toString();

                                        Interphone interphone = new Interphone();

                                        ImCode imCode = new ImCode();

                                        int txCode = MyApplicatoin.interphone.getTxcode();
                                        int rxCode = MyApplicatoin.interphone.getRxcode();

                                        int freq = MyApplicatoin.interphone.getFrequne();


                                        byte[] data = new byte[14];

                                        int i = 0;

                                        data[i++] = (byte) (txCode & 0xff);
                                        data[i++] = (byte) (rxCode & 0xff);

                                        data[i++] = (byte) (freq & 0xff);
                                        data[i++] = (byte) (freq >> 8);
                                        data[i++] = (byte) (freq >> 16);
                                        data[i++] = (byte) (freq >> 24);

                                        long name = System.currentTimeMillis();

                                        boolean ism = true;

                                        String k = "2";

                                        for(int as = 0; as < String.valueOf(name).length()-1;as++){
                                            k+=0;
                                        }

                                        LogUtil.i(TAG,"?????????"+k);

                                        if(checkBox.isChecked()){

                                            //?????????
                                            name = name+Long.valueOf(k);

                                        }

                                        byte[] bata = Tools.longToBytes(name);

                                        data[i++] = bata[0];
                                        data[i++] = bata[1];
                                        data[i++] = bata[2];
                                        data[i++] = bata[3];
                                        data[i++] = bata[4];
                                        data[i++] = bata[5];
                                        data[i++] = bata[6];
                                        data[i++] = bata[7];

                                        if(editText.getText().length() > 1){

                                            imCode.setName(new String(data)+titleName);//new String(data,"ISO-8859-1")+titleName
                                            Log.d("bbbbbbbbbbbbbbbbb","adata9999999999999:     "+new String(data)+titleName+"    "+data);
                                            imCode.setHite(titleName);
                                            MyApplicatoin.imCode.setHite(titleName);
                                        }else{
                                            imCode.setName(new String(data)+String.valueOf(name));//new String(data,"ISO-8859-1")+String.valueOf(name)
                                            imCode.setHite(String.valueOf(name));
                                            MyApplicatoin.imCode.setHite(String.valueOf(name));
                                        }

                                        LogUtil.i(TAG,"??????1???"+Arrays.toString(data));

                                        String n = Base64.encodeToString(data,Base64.DEFAULT);

                                        LogUtil.i(TAG,"??????2???"+n.length()+"-"+n.toString());

                                        LogUtil.i(TAG,"??????3???"+Arrays.toString(Base64.decode(n,Base64.DEFAULT)));


                                        imCode.setId((long) s);
                                        //??????16????????????
                                        imCode.setUuid(MyApplicatoin.Random(16));

                                        interphone.setImCode(imCode);

                                        MyApplicatoin.interphone.setImCode(imCode);

                                        MyApplicatoin.setInterphone(interphone);


                                        Openfrom openfrom = new Openfrom();

                                        openfrom.setUserId(MyApplicatoin.evenUser.getId());

                                        openfrom.setFromName(String.valueOf(name));

                                        openfrom.setCode(Open_biz.Joinfrom);

                                        MyApplicatoin.openfrom = openfrom;

                                        LogUtil.i(TAG,"nweGroup:"+MyApplicatoin.openfrom.toString());

                                        MyApplicatoin.setOpenfire(openfrom);

                                        mesage_entity_list.clear();

                                        if (adapter != null) {

                                            adapter.notifyDataSetChanged();

                                        }





                                    }
                                } catch (Exception e) {
                                    ExceptionUtil.handleException(e);
                                }

                                dragLayout.close();


                                dialog.cancel();

                            }
                        });
                        dialog.show();

                    }else{

                        tooleUser();
                    }

                }

            //    stopTrace();

                break;
            case R.id.chat_draglayout_histroy:
                mPopupWindow.dismiss();//??????????????????
                startActivity(new Intent(MainActivity.this,HistruyGroupActivity.class));

                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                break;
            case R.id.chat_draglayout_joingroup:
                mPopupWindow.dismiss();//??????????????????
                if(isBLE()){


                    startActivity(new Intent(MainActivity.this,WiActivity.class));

                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                }


                break;
            case R.id.chat_draglayout_leavegroup:

                mPopupWindow.dismiss();//??????????????????
                if (isBLE()) {

                    if (isInterphone()) {

                        final AlertDialog dialogLeave = new AlertDialog.Builder(MainActivity.this).create();
                        View viewLeave = View.inflate(MainActivity.this, R.layout.dialog_flock_layout, null);//dialog_warning_layout
                        dialogLeave.setView(viewLeave);
                        dialogLeave.setInverseBackgroundForced(true);

                        dialogLeave.setCancelable(false);

                        final Button bt_cancel_Leave = (Button) viewLeave.findViewById(R.id.button_cancel);
                        final Button bt_ok_Leave = (Button) viewLeave.findViewById(R.id.button_ok);

                        final TextView tv_title_Leave = (TextView) viewLeave.findViewById(R.id.title);

                        tv_title_Leave.setText(getResources().getString(R.string.sms_dialog_edtext_leavegroup_hite));

                        //dialog????????????

                        Window window_Leave = dialogLeave.getWindow();
                        window_Leave.setBackgroundDrawableResource(android.R.color.transparent);

                        bt_cancel_Leave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialogLeave.cancel();
                            }
                        });
                        bt_ok_Leave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                try {

                                    page = 0;

                                    if(isBLE()){

                                        MyApplicatoin.IS_OPEN_JOIN = false;

                                        Interphone interphone = new Interphone();

                                        ImCode imCode = new ImCode();

                                        byte[] name = {0,0,0,0,0,0,0,0,0,0,0,0,0,0};

                                        imCode.setName(new String(name,"ISO-8859-1"));

                                        imCode.setId((long) 0);
                                        //??????16????????????
                                        imCode.setUuid(MyApplicatoin.Random(16));

                                        interphone.setImCode(imCode);

                                        MyApplicatoin.interphone.setImCode(imCode);

                                        MyApplicatoin.setInterphone(interphone);

                                        Openfrom openfrom = new Openfrom();

                                        openfrom.setUserId(MyApplicatoin.evenUser.getId());
                                        openfrom.setFromName(MyApplicatoin.openfrom.getFromName());
                                        openfrom.setCode(Open_biz.Leavefrom);
                                        MyApplicatoin.setOpenfire(openfrom);


                                    }
                                   // SharePreferenceUtil.saveStringDataToSharePreference(MainActivity.this, SharePreferenceConstant.HITEROOM, "");
                                } catch (Exception e) {

                                    ExceptionUtil.handleException(e);

                                }


                             //   mToolBarTextView.setText(getResources().getString(R.string.chat_title_text));

                                mesage_entity_list.clear();

                                if (adapter != null) {

                                    adapter.notifyDataSetChanged();

                                //    adapter.notifyData(lv);
                                 //   lv.setSelection(adapter.getCount());
                                }


                                dragLayout.close();

                                dialogLeave.cancel();
                            }
                        });
                        dialogLeave.show();

                    }
                }


                break;

            /*case R.id.chat_draglayout_map:

                startActivity(new Intent(MainActivity.this,MapActivity.class));

                break;*/
            case R.id.sms_main_ico_profile:


                startActivity(new Intent(MainActivity.this,DeviceActivity.class));


                break;

            case R.id.chat_draglayout_device_btn:
                Interphone mInterphone = new Interphone();
                mInterphone.setFindDevice((byte)0x02);
                MyApplicatoin.setInterphone(mInterphone);
                break;

            default:
                break;



        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        dragLayout.deleteClose();
    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    /**
     * ??????????????????
     */
    private void chooseIcon(final Context context) {
        if(Build.VERSION.SDK_INT >=26){
            startChooser();
            return;
        }
            Dialog dialog = new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.sms_main_hite_icon))
                    .setPositiveButton(getResources().getString(R.string.sms_main_hite_icon_photo_album),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();

                                    String[] PERMISSIONS = {
                                            "android.permission.WRITE_EXTERNAL_STORAGE",
                                            "android.permission.READ_EXTERNAL_STORAGE"
                                    };

                                    if(hasPermission(PERMISSIONS)){


                                        // ???????????????????????????????????????????????????

                                        pickPhoto();

                                    }else{
                                        requstPermission(GlobalConsts.WRITE_EXTERNAL_STORAGE,PERMISSIONS);
                                    }

                                }
                            })
                    .setNegativeButton(getResources().getString(R.string.sms_main_hite_icon_photo_camera),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                    takePhoto();
                                }
                            }).create();
            dialog.show();
    }

    /**
     * ??????????????????
     */
    private void takePhoto() {


        try {
            String state = Environment.getExternalStorageState();

            if (state.equals(Environment.MEDIA_MOUNTED)) {

                imgPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;

                path = imgPath+String.valueOf(System.currentTimeMillis()) + ".jpg";

                File imgFile = new File(path);

                if (!imgFile.exists()) {
                    imgFile.getParentFile().mkdirs();
                }

          if(Build.VERSION.SDK_INT <26){
                if (Build.VERSION.SDK_INT < 24) {
                    // ??????????????????uri
                    imgUri = Uri.fromFile(imgFile);
                } else {
                    //??????android7.0 ???????????????????????????
                    ContentValues contentValues = new ContentValues(1);
                    contentValues.put(MediaStore.Images.Media.DATA, imgFile.getAbsolutePath());
                    imgUri = this.getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                }


                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);

                startActivityForResult(intent, CAMERA_REQUEST_CODE);

           }else{
             /* imgUri = FileProvider.getUriForFile(MainActivity.this, "com.sms.app.interphone.fileprovider", imgFile);//7.0
              Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
              intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
              startActivityForResult(intent, PHOTOGRAPH_REQUEST);*/
              startChooser();
          }

            }
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }

    }



    private void startChooser() {//android8.0???????????????

        // ?????????????????????
        imagePicker.startChooser(this, new ImagePicker.Callback() {
            // ??????????????????
            @Override public void onPickImage(Uri imageUri) {

            }

            // ??????????????????
            @Override public void onCropImage(Uri imageUri) {
               // draweeView.setImageURI(imageUri);
              //  draweeView.getHierarchy().setRoundingParams(RoundingParams.asCircle());
                try {
                    getImageToView(new File(new URI(imageUri.toString())));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }

            // ?????????????????????
            @Override public void cropConfig(CropImage.ActivityBuilder builder) {
                builder
                        // ????????????????????????
                        .setMultiTouchEnabled(false)
                        // ????????????????????????
                        .setGuidelines(CropImageView.Guidelines.OFF)
                        // ??????/??????
                        .setCropShape(CropImageView.CropShape.OVAL)
                        // ????????????????????????????????????
                        .setRequestedSize(150, 150)
                        // ?????????
                        .setAspectRatio(2, 2);
            }

            // ????????????????????????
            @Override public void onPermissionDenied(int requestCode, String[] permissions,
                                                     int[] grantResults) {
            }
        });
    }


    /**
     * ??????7.0??????????????????????????????uri
     */
    private Uri getImageContentUri(Context context, File imageFile) {

        try {
            String filePath = imageFile.getAbsolutePath();

            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.Media._ID},
                    MediaStore.Images.Media.DATA + "=? ",
                    new String[]{filePath}, null);

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.MediaColumns._ID));
                Uri baseUri = Uri.parse("content://media/external/images/media");
                return Uri.withAppendedPath(baseUri, "" + id);
            } else {
                if (imageFile.exists()) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DATA, filePath);
                    return context.getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /***
     * ?????????????????????
     */
    private void pickPhoto() {

        String state = Environment.getExternalStorageState();

        if (Build.VERSION.SDK_INT > 26||Build.VERSION.SDK_INT==26) {//??????????????????26????????????
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");

            // ????????????

            startActivityForResult(intent, RESULT_REQUEST_CODE);//RESULT_REQUEST_CODE    PHOTO_ALBUL
        }else{//????????????26????????????
            if (state.equals(Environment.MEDIA_MOUNTED)) {

                Intent intentTakePic = new Intent();
                intentTakePic.setAction(Intent.ACTION_PICK);
                intentTakePic.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentTakePic, IMAGE_REQUEST_CODE);
        }


        }
    }


    private File mOutputFile;

    /**
     * ????????????????????????
     *
     * @param file
     */
    public void startPhotoZoom(File file) {

        try {

            if (Build.VERSION.SDK_INT < 26) {//????????????26????????????

                Intent intent = new Intent("com.android.camera.action.CROP");

                mOutputFile = new File(generateImgePath());

                File parentFile = mOutputFile.getParentFile();

                LogUtil.i(TAG,"IMGpath:"+file.getAbsolutePath());

                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }

                if(Build.VERSION.SDK_INT < 24){
                    intent.setDataAndType(Uri.fromFile(file), "image/*");
                }else{
                    intent.setDataAndType(getImageContentUri(this, file), "image/*");
                }

                // ????????????
                intent.putExtra("crop", "true");
                // aspectX aspectY ??????????????????
                intent.putExtra("aspectX", 2);
                intent.putExtra("aspectY", 2);
                // outputX outputY ?????????????????????

                //?????????????????????????????????60K ????????????????????????????????????60K??????
                intent.putExtra("outputX", 150);
                intent.putExtra("outputY", 150);
                intent.putExtra("return-data", true);


                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mOutputFile));
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

                startActivityForResult(intent, RESULT_REQUEST_CODE);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * ??????????????????????????????????????????????????????????????????????????????
     */
    private String generateImgePath() {
        return this.getExternalStoragePath() + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg";
     //  return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg";//?????????
    }

    /**
     * ??????SD??????????????????
     */
    private String getExternalStoragePath() {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        sb.append(File.separator);
        String ROOT_DIR = "Android/data/" + this.getPackageName();
        sb.append(ROOT_DIR);
        sb.append(File.separator);
        return sb.toString();
    }

    /**
     * ?????????????????????????????????
     *

     */
    private void getImageToView(File data) {

        final Bitmap bitmap = getLoacaBitmap(data); //??????????????????(???cdcard?????????)

        if(bitmap != null){

            saveBitmap(bitmap);
        }

    }

    /**
     * ??????????????????
     * http://bbs.3gstdy.com
     * @param file
     * @return
     */
    public Bitmap getLoacaBitmap(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            ExceptionUtil.handleException(e);
            return null;
        }
    }


    public void saveBitmap(final Bitmap mBitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        byte[] imageData = byteArrayOutputStream.toByteArray();

        final Avatar avatar = new Avatar();

        avatar.setUser_id(MyApplicatoin.evenUser.getId());
        avatar.setData(imageData);
        Tools.showProgressDialog(MainActivity.this,getResources().getString(R.string.sms_main_hite_please_later));

        sms_request.dorequest(MainActivity.this, sms_request.SET, avatar, new RequestCB() {
            @Override
            public void respone(int opcode, Object obj) {
                if(opcode == sms_request.SET){

                    Tools.closeProgressDialog();
                    Avatar avatars = (Avatar) obj;
                    LogUtil.i(TAG,"respone:Avatar:"+avatar.getData().length);


                    try {
                        String icon_path = Tools.writeToSdcard(MainActivity.this, "user", avatar.getUser_id().toString(),avatar.getData());

                        LogUtil.i(TAG,"icon_path:"+icon_path);

                        MyApplicatoin.evenUser.setAvatar_url(icon_path);

                        final Bitmap bitmap = getLoacalBitmap(MyApplicatoin.evenUser.getAvatar_url()); //??????????????????(???cdcard?????????)

                        if(bitmap != null){

                            user_icon.setImageBitmap(bitmap);

                            user_name.setText(MyApplicatoin.evenUser.getName());

                        }

                    } catch (IOException e) {
                        ExceptionUtil.handleException(e);
                    }

                }

            }
        });

    }



    private TimerTask timelist;
    private Timer tasks = new Timer();
    private int reclist = 15;

    private void startMsgErro(Mesage_entity msg) {

        LogUtil.i(TAG,"33344startMsgErro:");



        if(timelist == null){

            reclist = 15;

            try {

                timelist = new TimerTask() {
                    @Override
                    public void run() {
                        reclist--;
                        if(reclist < 0){
                            timelist.cancel();
                            timelist=null;

                            updataList();
                        }
                    }
                };
                tasks.schedule(timelist, 1000, 1000);

            } catch (Exception e) {

                ExceptionUtil.handleException(e);

            }
        }else{
            reclist = 15;
        }

    }

    private void updataList() {

        for(int i = 0;i< mesage_entity_list.size();i++){

            Mesage_entity msg  = mesage_entity_list.get(i);

            if(msg.getUser_id() == MyApplicatoin.evenUser.getId() && msg.getMesg_type() != MsgResponse.Exit && msg.getMesg_type() != MsgResponse.Join){

                if(msg.getIs_ok() == 1){

                    mesage_entity_list.get(i).setIs_ok((byte) 3);

                }

            }

        }


        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(adapter != null){

                    if(isListViewBottom(lv)){
                        adapter.notifyData(lv);
                    }else{
                        adapter.notifyDataSetChanged();
                    }


                }

                /*if(finalIs_erro){
                    erro_hite_layout.setVisibility(View.VISIBLE);
                }*/

            }
        });

    }

    private void updataItemPlay(Mesage_entity msgs) {

        boolean is_true = false;

        for(int i = 0;i< mesage_entity_list.size();i++){

            Mesage_entity msg  = mesage_entity_list.get(i);

            if(msg.getMesg_type() == MsgResponse.Voice){

                if(msg.getMgid() == msgs.getMgid()){

                    if(msg.isIsplay()){
                        mesage_entity_list.get(i).setIsplay(false);
                        is_true = true;
                    }
                }
            }
        }

        LogUtil.i(TAG,"updataItemPlay:"+is_true);

        if(is_true){
            if(adapter != null){
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void updataListItem(Mesage_entity msgs) {

        if(msgs.getUser_id() == MyApplicatoin.evenUser.getId() ){

            if(msgs.getMesg_type() == MsgResponse.Text ||msgs.getMesg_type() == MsgResponse.Voice){

                boolean is_true = false;

                for(int i = 0;i< mesage_entity_list.size();i++){

                    Mesage_entity msg  = mesage_entity_list.get(i);

                    if(msg.getMgid() == msgs.getMgid()){

                        if(msg.getMesg_type() == MsgResponse.Voice || msg.getMesg_type() == MsgResponse.Text){

                            if(msg.getIs_ok() == 1){

                                mesage_entity_list.get(i).setIs_ok((byte) 2);

                                is_true = true;

                            }
                        }
                    }
                }

                if(is_true){

                    if(adapter != null){

                        adapter.notifyDataSetChanged();

                    }
                }else{
                    msg_erro_list.add(msgs);
                }
            }
        }
    }



    private void updataListView(final Mesage_entity msg) {

        LogUtil.i(TAG,"listViewMessageTime:"+System.currentTimeMillis());

        LogUtil.i(TAG,"listview,show:"+isListViewBottom(lv));

        try {

            if(msg_erro_list.size() > 0){

                for(int i = 0;i < msg_erro_list.size();i++){

                    Mesage_entity msgs = msg_erro_list.get(i);

                    if(msg.getMesg_type() == msgs.getMesg_type() && msg.getUser_id() == msgs.getUser_id() && msg.getMgid() == msgs.getMgid()){
                        msg.setIs_ok((byte) 2);
                    }

                    msg_erro_list.remove(i);

                    break;
                }
            }
            if(msg.getMesg_type() == MsgResponse.Voice){


                boolean isk = false;

                if (MyApplicatoin.packs.size() > 0) {
                    for(int i = 0;i < MyApplicatoin.packs.size();i++){

                        if(MyApplicatoin.packs.get(i) == msg.getMgid()){
                            isk = true;
                            MyApplicatoin.packs.remove(i);
                            break;
                        }

                    }
                }

                LogUtil.i(TAG,"packs:"+msg.getMgid()+"is:"+isk+",lists:"+MyApplicatoin.packs.toString());

                if(isk){
                    msg.setIsplay(false);
                }else{
                    msg.setIsplay(true);
                }


            }
            if(msg.getUser_id() == MyApplicatoin.evenUser.getId() && msg.getMesg_type() != MsgResponse.Exit && msg.getMesg_type() != MsgResponse.Join){

                startMsgErro(msg);

            }
            if(mesage_entity_list.size() > 0 && msg.getFrom_id() != mesage_entity_list.get(0).getFrom_id()){

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mesage_entity_list.clear();

                        mesage_entity_list.add(msg);

                        if(adapter != null){

                            adapter.notifyDataSetChanged();

                        //    adapter.notifyData(lv);
                        //    lv.setSelection(adapter.getCount());
                        }else{
                            adapter = new ChatAdapter(MainActivity.this,mesage_entity_list);
                            lv.setAdapter(adapter);
                        //    lv.setSelection(adapter.getCount());
                        }
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        boolean isMsg = true;
                        for(Mesage_entity m : mesage_entity_list){
                            if(m.getMgid() == msg.getMgid() && m.getUser_id() == msg.getUser_id()){

                                if(m.getMesg_type() != 2){
                                    isMsg = false;
                                }

                            }
                            if(m.getUser_id() == msg.getUser_id() && msg.getMesg_type() == MsgResponse.Join){

                                if(m.getUser_id() == MyApplicatoin.evenUser.getId()){
                                    isMsg = false;
                                }

                            }
                        }
                        if(isMsg){

                            mesage_entity_list.add(msg);

                            if (adapter != null) {

                                if(isListViewBottom(lv)){
                                    adapter.notifyData(lv);
                                }else{
                                    adapter.notifyDataSetChanged();
                                }

                            //    lv.setSelection(adapter.getCount());

                            }else{

                                adapter = new ChatAdapter(MainActivity.this,mesage_entity_list);
                                if (adapter != null) {
                                    lv.setAdapter(adapter);
                                    lv.setSelection(adapter.getCount());
                                }

                            }
                        }
                    }
                });
            }
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }

    public boolean isListViewBottom(final ListView listView) {
        boolean result=false;
        try {
            if (listView.getLastVisiblePosition() == (listView.getCount() - 1)) {
                final View bottomChildView = listView.getChildAt(listView.getLastVisiblePosition() - listView.getFirstVisiblePosition());

                if(bottomChildView != null){

                    LogUtil.i(TAG,"LLL:"+bottomChildView.getBottom());
                    LogUtil.i(TAG,"LLL:"+listView.getHeight());

                    result= ((listView.getHeight()+300)>=bottomChildView.getBottom());

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

           // exit();  ?????????????????????
            againexit();//dialog??????????????????
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }


    private void exit() {

        if ((System.currentTimeMillis() - clickTime) > 2000) {

            Tools.showInfo(MainActivity.this,getResources().getString(R.string.sms_main_hite_key_down));
            clickTime = System.currentTimeMillis();



        } else {

            LogUtil.i(TAG, "exit application");

            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            MyApplicatoin.esc_sms = true;
            if (mBluetoothAdapter.isEnabled()) {
                String mbluetoothstate= SharePreferenceUtil.getStringDataByKe(MainActivity.this, SharePreferenceConstant.MBLUETOOTH, "");
                if ("mBluetooth".equals(mbluetoothstate) || mbluetoothstate == "mBluetooth") {//????????????????????????????????????????????????
                    mBluetoothAdapter.disable();
                    SharePreferenceUtil.saveStringDataToSharePreference(MainActivity.this, SharePreferenceConstant.MBLUETOOTH, "");
                }

            }

            if(timer != null){
                timer.cancel();

                timer = null;
            }


            finish();

        }
    }

    // //?????????????????????
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }




    class LoginReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                int status = intent.getIntExtra(GlobalConsts.ACTION_USER, -1);

                if(status == GlobalConsts.ACTTION_LOGIN_ERRO){


                }else if(status == GlobalConsts.ACTTION_IS_SCAN){

                    setGroup();

                    if(MyApplicatoin.IS_BLE){

                        try {
                            if(MyApplicatoin.interphone.getName() != null){
                                mToolBarTextView.setText(MyApplicatoin.interphone.getName());
                                //??????????????????
                                Message message=new Message();
                                Bundle bundle=new Bundle();
                                bundle.putString("electricQuantity",MyApplicatoin.interphone.getPower()+"%");//ChargerUtils.getNiMHProcessFormValtage((float)(Math.round(MyApplicatoin.interphone.getVolt()))/1000)+"%"
                                message.what=4;
                                message.setData(bundle);
                                mhandler.handleMessage(message);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }else{
                        mToolBarTextView.setText(getResources().getString(R.string.sms_ble_scnn_title));
                    }




                }else if(status == GlobalConsts.ACTTION_START){


                    setData();


                }else if(status == GlobalConsts.ACTTION_ERRO_HITE){


                    erro_hite_layout.setVisibility(View.GONE);


                }else if(status == GlobalConsts.ACTTION_ERRO_SHOW){


                    erro_hite_layout.setVisibility(View.VISIBLE);


                }else if(status > 200){


                    LogUtil.i(TAG,"freq:"+status);
                    user_freq.setText(getFreq(status));


                }else{

                    if(status != -1){
                        setUserData();
                    }

                }

                int type = intent.getIntExtra(GlobalConsts.ACTION_TYPE,0);

                //????????????????????????????????????
                if(type == GlobalConsts.ACTTION_IS_MSG){

                    Mesage_entity mesage_entity = intent.getParcelableExtra(GlobalConsts.ACTION_MSG);

                    if(mesage_entity != null){

                        LogUtil.i(TAG,"ChatReceiver:"+mesage_entity.toString());

                        if(mesage_entity.getMesg_type() == MsgResponse.Result || mesage_entity.getMesg_type() == MsgResponse.Apply){

                            toosApply(mesage_entity);
                        }else{
                            updataListView(mesage_entity);
                        }


                    }else{

                        LogUtil.i(TAG,"ChatReceiver == null");

                    }

                }else if(type == GlobalConsts.ACTTION_IS_MSG_OK){

                    erro_hite_layout.setVisibility(View.GONE);

                    Mesage_entity mesage_entity = intent.getParcelableExtra(GlobalConsts.ACTION_MSG);

                    if(mesage_entity != null){

                    //    LogUtil.i(TAG,"ChatReceiver:"+mesage_entity.toString());

                        updataListItem(mesage_entity);

                    }else{

                    //    LogUtil.i(TAG,"ChatReceiver == null");

                    }


                }else if(type == GlobalConsts.ACTTION_IS_MSG_PLAY){

                    Mesage_entity mesage_entity = intent.getParcelableExtra(GlobalConsts.ACTION_MSG);

                    if(mesage_entity != null){

                    //    LogUtil.i(TAG,"ChatReceiver:"+mesage_entity.toString());

                        updataItemPlay(mesage_entity);

                    }else{

                    //    LogUtil.i(TAG,"ChatReceiver == null");

                    }


                }else if(type == GlobalConsts.ACTTION_IS_OPENFIRE){

                    //??????????????????openfire????????????

                    byte[] open = intent.getByteArrayExtra(GlobalConsts.ACTION_OPENFIRE);

                    StringBuilder builder = new StringBuilder();

                    if(open[0] == Open_biz.Newfrom){

                        if(open[1] == GlobalConsts.YES){

                            builder.append("??????????????????????????????");

                            LogUtil.i(TAG,"??????????????????????????????");
                         //   LogUtil.i(TAG,"onReceive new:"+getGroupName(MyApplicatoin.interphone.getImCode().getName()));

                         //   String name = getGroupName(MyApplicatoin.interphone.getImCode().getName());

                        //    setData();

                            /*if(name != null){

                            //    mToolBarTextView.setText(name);

                            }*/

                        }else{

                            builder.append("?????????????????????");

                        }

                    }else if(open[0] == Open_biz.Joinfrom){

                        if(open[1] == GlobalConsts.YES){

                            builder.append("?????????????????????");
                            LogUtil.i(TAG,"?????????????????????");
    /*
                            LogUtil.i(TAG,"onReceive join:"+Tools.getGroupName(MyApplicatoin.interphone.getImCode().getName()));
                            String name = Tools.getGroupName(MyApplicatoin.interphone.getImCode().getName());*/

                        }else{

                            builder.append("?????????????????????");

                        }

                    }else if(open[0] == Open_biz.Leavefrom){

                        if(open[1] == GlobalConsts.YES){

                            builder.append("?????????????????????");

                        }else{

                            builder.append("?????????????????????");

                        }
                    }else if(open[0] == Open_biz.Ping){

                    //    LogUtil.i(TAG,"????????????");

                        if(open[1] == GlobalConsts.YES){

                            erro_hite_layout.setVisibility(View.GONE);

                        }else{

                            erro_hite_layout.setVisibility(View.VISIBLE);

                        }
                    }


                }else if(type == GlobalConsts.ACTTION_IS_START){

                    //??????????????????openfire????????????

                    byte start  = intent.getByteExtra(GlobalConsts.ACTION_MSG,(byte)10);

                    LogUtil.i(TAG,"????????????:"+start);

                    if(start == 3){

                    //    stopRecord();

                        upVoiceButton(true);

                    }else if(start == 2){

                    //    startRecord();

                        upVoiceButton(false);

                    }else if(start == 1){

                        stopRecord(4);

                        upVoiceButton(false);

                    }else if(start == 4){

                        stopRecord(4);

                       // if(MyApplicatoin.interphone.getState() == 3 && MyApplicatoin.interphone.getmState() == 3){
                            upVoiceButton(true);
                       // }




                    }else {

                        LogUtil.i(TAG,"??????????????????");
                        //    Toast.makeText(ChatActivity.this, "??????????????????", Toast.LENGTH_LONG).show();

                    }

                }

                int statuApk = intent.getIntExtra(GlobalConsts.ACTION_APK, -1);

                if(statuApk == 1){

                    String[] PERMISSIONS_STORAG = {
                            "android.permission.REQUEST_INSTALL_PACKAGES",
                    };


                    if(Build.VERSION.SDK_INT >= 26){

                        boolean b = getPackageManager().canRequestPackageInstalls();

                        LogUtil.i(TAG,"installVersion:"+b);

                        if (b) {

                            //?????????????????????(?????????????????????)

                            installAPK();

                        } else {
                            //???????????????????????????????????????
                            requstPermission(GlobalConsts.INSTALL,PERMISSIONS_STORAG);
                        }

                    }else{
                        installAPK();
                    }

                }


            } catch (Resources.NotFoundException e) {
                ExceptionUtil.handleException(e);
            }


        }
    }

    /**
     *
     * ???????????????????????????
     *
     * */
    private void toosApply(Mesage_entity mesage_entity) {


        DAOApplyMsgDao mApplyDao =  DAOService.get(getApplication()).getsession().getDAOApplyMsgDao();

        List<DAOApplyMsg> applyMsgs = mApplyDao.loadAll();

        boolean exists = false;

        for(DAOApplyMsg m : applyMsgs){

            if(m.getFrom_id() == mesage_entity.getFrom_id() && m.getMsg_id() == mesage_entity.getMgid() && m.getUser_id() == mesage_entity.getUser_id()){
                exists = true;
            }

        }

        LogUtil.i(TAG,"toosApply:"+exists);
        LogUtil.i(TAG,"toosApply:"+mesage_entity.toString());

        if(exists){
            return;
        }

        if(mesage_entity.getUser_id() == MyApplicatoin.evenUser.getId()){

            if(mesage_entity.getMesg_type() == MsgResponse.Apply){

                Tools.showInfo(MainActivity.this,getResources().getString(R.string.sms_main_toast_hite_apply));

                    /*DAOApplyMsg applyMsg = new DAOApplyMsg();

                    applyMsg.setFrom_id(mesage_entity.getFrom_id());
                    applyMsg.setMsg_id(mesage_entity.getMgid());
                    applyMsg.setUser_id(mesage_entity.getUser_id());
                    applyMsg.setType(mesage_entity.getMesg_type());
                    applyMsg.setContent(mesage_entity.getContent());

                    mApplyDao.insert(applyMsg);*/


            }

        }else{

            final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
            View view = View.inflate(MainActivity.this, R.layout.dialog_login_layout, null);
            dialog.setView(view);
            dialog.setInverseBackgroundForced(true);
            dialog.setCancelable(false);
            final Button cancel = (Button) view.findViewById(R.id.button_cancel);
            final Button ok = (Button) view.findViewById(R.id.button_ok);
            final TextView title = (TextView) view.findViewById(R.id.title);
            final TextView content = (TextView) view.findViewById(R.id.content);

            title.setText(getResources().getString(R.string.sms_main_diolog_title));


            if(mesage_entity.getMesg_type() == MsgResponse.Apply){

                content.setText(mesage_entity.getUser_id()+getResources().getString(R.string.sms_main_diolog_content_a));

                DAOApplyMsg applyMsg = new DAOApplyMsg();

                applyMsg.setFrom_id(mesage_entity.getFrom_id());
                applyMsg.setMsg_id(mesage_entity.getMgid());
                applyMsg.setUser_id(mesage_entity.getUser_id());
                applyMsg.setType(mesage_entity.getMesg_type());
                applyMsg.setContent(mesage_entity.getContent());

                mApplyDao.insert(applyMsg);

            }else{

                DAOApplyMsg applyMsg = new DAOApplyMsg();

                applyMsg.setFrom_id(mesage_entity.getFrom_id());
                applyMsg.setMsg_id(mesage_entity.getMgid());
                applyMsg.setUser_id(mesage_entity.getUser_id());
                applyMsg.setType(mesage_entity.getMesg_type());
                applyMsg.setContent(mesage_entity.getContent());


                mApplyDao.insert(applyMsg);


                SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(GlobalConsts.ACTION_IM_GROUP, Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("boolean", "true");

                editor.commit();

                if(mesage_entity.getContent().equals("1")){

                    content.setText(getResources().getString(R.string.sms_main_diolog_content_b));

                }else{

                    content.setText(getResources().getString(R.string.sms_main_diolog_content_c));

                }
            }

            /*if(!mesage_entity.isIsplay()){

                content.setText(mesage_entity.getUser_id()+getResources().getString(R.string.sms_main_diolog_content_a));

                DAOApplyMsg applyMsg = new DAOApplyMsg();

                applyMsg.setFrom_id(mesage_entity.getFrom_id());
                applyMsg.setMsg_id(mesage_entity.getMgid());
                applyMsg.setUser_id(mesage_entity.getUser_id());
                applyMsg.setType(mesage_entity.getMesg_type());
                applyMsg.setContent(mesage_entity.getContent());

                mApplyDao.insert(applyMsg);

            }else{

                DAOApplyMsg applyMsg = new DAOApplyMsg();

                applyMsg.setFrom_id(mesage_entity.getFrom_id());
                applyMsg.setMsg_id(mesage_entity.getMgid());
                applyMsg.setUser_id(mesage_entity.getUser_id());
                applyMsg.setType(mesage_entity.getMesg_type());
                applyMsg.setContent(mesage_entity.getContent());


                mApplyDao.insert(applyMsg);

                SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(GlobalConsts.ACTION_IM_CODE, Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("boolean", "true");

                editor.commit();


            }*/

            //dialog????????????

            Window window = dialog.getWindow();
            window.setBackgroundDrawableResource(android.R.color.transparent);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mesage_entity.getMesg_type() == MsgResponse.Apply){

                        LogUtil.i(TAG,"??????????????????????????????");

                        Openfrom openfrom = new Openfrom();

                        openfrom.setType(MsgResponse.Result);
                        openfrom.setMessage("2");
                        openfrom.setMessageID(getRandom());
                        openfrom.setUserId(mesage_entity.getUser_id());
                        openfrom.setCode(Open_biz.ChatMessage);

                        MyApplicatoin.setOpenfire(openfrom);

                    }

                    /*if(!mesage_entity.isIsplay()){

                        LogUtil.i(TAG,"??????????????????????????????");

                        Openfrom openfrom = new Openfrom();

                        openfrom.setType(MsgResponse.Result);
                        openfrom.setMessage("2");
                        openfrom.setMessageID(getRandom());
                        openfrom.setUserId(MyApplicatoin.evenUser.getId());
                        openfrom.setCode(Open_biz.SendMessage);

                        MyApplicatoin.setOpenfire(openfrom);



                    }*/

                    dialog.cancel();
                }
            });
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mesage_entity.getMesg_type() == MsgResponse.Apply){

                        LogUtil.i(TAG,"???????????????????????????");

                        Openfrom openfrom = new Openfrom();

                        openfrom.setType(MsgResponse.Result);
                        openfrom.setMessage("1");
                        openfrom.setMessageID(getRandom());
                        openfrom.setUserId(mesage_entity.getUser_id());
                        openfrom.setCode(Open_biz.ChatMessage);

                        MyApplicatoin.setOpenfire(openfrom);

                    }else{

                        if(mesage_entity.getContent().equals("1")){

                            LogUtil.i(TAG,"?????????????????????");


                            try {

                                SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(GlobalConsts.ACTION_IM_CODE, Context.MODE_PRIVATE);

                                String id = sharedPreferences.getString("id", null);
                                String name = sharedPreferences.getString("name", null);
                                String uuid = sharedPreferences.getString("uuid", null);
                                String hite = sharedPreferences.getString("hite", null);

                                Interphone interphone1 = new Interphone();

                                ImCode imcode = new ImCode();

                                imcode.setId(Long.parseLong(id));
                                imcode.setName(name);
                                imcode.setUuid(uuid);
                                imcode.setHite(hite);

                                interphone1.setImCode(imcode);

                                MyApplicatoin.interphone.setImCode(imcode);

                                MyApplicatoin.setInterphone(interphone1);

                                byte[] data = Tools.getDataCode(name);

                                if(data != null){

                                    int txCode = data[0] & 0xFF;
                                    int rxCode = data[1] & 0xFF;

                                    int freq = (data[2] & 0xFF
                                            | (data[3] & 0xFF) << 8
                                            | (data[4] & 0xFF) << 16
                                            | (data[5] & 0xFF) << 24);

                                    if(MyApplicatoin.interPhoneRF == 3){

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

                                openfrom.setFromName(Tools.getGroupName(name));

                                openfrom.setCode(Open_biz.Joinfrom);

                                MyApplicatoin.openfrom = openfrom;

                                MyApplicatoin.setOpenfire(openfrom);

                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }

                        }

                    }

                    /*if(!mesage_entity.isIsplay()){

                        LogUtil.i(TAG,"???????????????????????????");

                        Openfrom openfrom = new Openfrom();

                        openfrom.setType(MsgResponse.Result);
                        openfrom.setMessage("1");
                        openfrom.setMessageID(getRandom());
                        openfrom.setUserId(MyApplicatoin.evenUser.getId());
                        openfrom.setCode(Open_biz.SendMessage);

                        MyApplicatoin.setOpenfire(openfrom);

                    }else{

                        if(mesage_entity.getContent().equals("1")){

                            LogUtil.i(TAG,"?????????????????????");


                            try {

                                SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(GlobalConsts.ACTION_IM_CODE, Context.MODE_PRIVATE);

                                String id = sharedPreferences.getString("id", null);
                                String name = sharedPreferences.getString("name", null);
                                String uuid = sharedPreferences.getString("uuid", null);
                                String hite = sharedPreferences.getString("hite", null);

                                Interphone interphone1 = new Interphone();

                                ImCode imcode = new ImCode();

                                imcode.setId(Long.parseLong(id));
                                imcode.setName(name);
                                imcode.setUuid(uuid);
                                imcode.setHite(hite);

                                interphone1.setImCode(imcode);

                                MyApplicatoin.interphone.setImCode(imcode);

                                MyApplicatoin.setInterphone(interphone1);

                                byte[] data = Tools.getDataCode(name);

                                if(data != null){

                                    int txCode = data[0] & 0xFF;
                                    int rxCode = data[1] & 0xFF;

                                    int freq = (data[2] & 0xFF
                                            | (data[3] & 0xFF) << 8
                                            | (data[4] & 0xFF) << 16
                                            | (data[5] & 0xFF) << 24);

                                    if(MyApplicatoin.interPhoneRF == 3){

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

                                openfrom.setFromName(String.valueOf(mesage_entity.getFrom_id()));

                                openfrom.setCode(Open_biz.Joinfrom);

                                MyApplicatoin.openfrom = openfrom;

                                MyApplicatoin.setOpenfire(openfrom);

                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }

                        }
                    }*/

                    dialog.cancel();

                }
            });

            dialog.show();

        }


    }


    /**
     *
     * ???????????????????????????????????????
     *
     * */
    public boolean hasPermission(String... permissions){

        for(String permission : permissions){

            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){

                return false;
            }
        }
        return true;
    }


    public void requstPermission(int code, String... permissions){

        ActivityCompat.requestPermissions(this, permissions, code);

    }


    /**
     *
     * ????????????????????????
     *
     * */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        LogUtil.i(TAG,"onRequestPermissionsResult:"+grantResults[0]);
        imagePicker.onRequestPermissionsResult(this, requestCode, permissions, grantResults);//android 8.0?????????
        switch (requestCode){

            case GlobalConsts.CAMERA:

                LogUtil.i(TAG,"??????:"+grantResults[0]);

                if(grantResults[0] == -1){

                    doLocation(grantResults,getResources().getString(R.string.camera));

                }else{

                    LogUtil.i(TAG,"????????????????????????");

                    chooseIcon(MainActivity.this);

                }

                break;

            case GlobalConsts.ACCESS_FINE_LOCATION:

                LogUtil.i(TAG,"??????:"+grantResults[0]);

                if(grantResults[0] == -1){

                    doLocation(grantResults,getResources().getString(R.string.location));

                }else{

                    LogUtil.i(TAG,"????????????????????????");


                }

                break;

            case GlobalConsts.RECORD_AUDIO:

                LogUtil.i(TAG,"??????:"+grantResults[0]);

                if(grantResults[0] == -1){

                    doLocation(grantResults,getResources().getString(R.string.audio));

                }else{

                    LogUtil.i(TAG,"????????????????????????");


                }

                break;


            case GlobalConsts.WRITE_EXTERNAL_STORAGE:

                LogUtil.i(TAG,"????????????:"+grantResults[0]);

                if(grantResults[0] == -1){

                    doLocation(grantResults,getResources().getString(R.string.write_sd));

                }else{

                    LogUtil.i(TAG,"????????????????????????");


                }

                break;


            case GlobalConsts.INSTALL:

                LogUtil.i(TAG,"????????????APK:"+grantResults[0]);

                try {

                    if(connection != null){
                        unbindService(connection);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(grantResults[0] == -1){

                    Uri packageURI = Uri.parse("package:"+getPackageName());
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,packageURI);
                    startActivityForResult(intent, GlobalConsts.INSTALL);

                }else{

                    LogUtil.i(TAG,"??????????????????APK????????????");

                    installAPK();

                }

                break;
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }

    /**
     *
     * ????????????
     *
     * @param grantResults*/
    public void doLocation(int[] grantResults, final String hite) {


        LogUtil.i(TAG,"doLocation"+grantResults);

        if (grantResults.length > 0) {

            if(grantResults[0] == -1){

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Dialog dialog = new AlertDialog.Builder(MainActivity.this)
                                .setTitle(getResources().getString(R.string.sms_dialog_hite))

                                .setMessage(hite+getResources().getString(R.string.sms_dialog_hite_permission))
                                .setCancelable(false)
                                .setPositiveButton(getResources().getString(R.string.sms_dialog_hite_permission_up),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                dialog.dismiss();

                                            }
                                        })
                                .setNegativeButton(getResources().getString(R.string.sms_dialog_hite_permission_ok),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                dialog.dismiss();
                                                Intent intent = new Intent();
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                if (Build.VERSION.SDK_INT >= 9) {
                                                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                                                } else if (Build.VERSION.SDK_INT <= 8) {
                                                    intent.setAction(Intent.ACTION_VIEW);
                                                    intent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
                                                    intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
                                                }
                                                startActivity(intent);

                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                            }
                                        }).create();
                        dialog.show();
                    }
                });


            }
        }

    }


    private String getFreq(int frequne){


        float b = frequne;
        //    DecimalFormat  df = new DecimalFormat("#.000");

        //   return new String(df.format(frequne/1000));
        return  String .format("%.4f",(b/1000000));

    }




    /**
     * ????????????
     *
     * */
    private void startRecord() {


        //????????????
        long currentTime = Calendar.getInstance().getTimeInMillis();

        if(currentTime - doClickTime > MIN_CLICK_DELAY_TIME){

            doClickTime = currentTime;

            if (voice_Read_Thread == null) {

                String name = Tools.getGroupName(MyApplicatoin.interphone.getImCode().getName());

                //????????????
                voice_Read_Thread = new Voice_Read_Thread(getApplicationContext(),name, mhandler ,quality);
                Thread th = new Thread(voice_Read_Thread);
                th.start();

                tv_voice_content.setVisibility(View.VISIBLE);
                tv_voice_hint.setVisibility(View.VISIBLE);

                voice_Read_Thread.setRecording(true);


                //??????????????????PTT??????
                if(isInterphone()){

                    Openfrom openfrom = new Openfrom();

                    openfrom.setType(MsgResponse.Ptt);
                    openfrom.setMessage("1");
                    openfrom.setMessageID(getRandom());
                    openfrom.setUserId(MyApplicatoin.evenUser.getId());
                    openfrom.setCode(Open_biz.SendMessage);

                    MyApplicatoin.setOpenfire(openfrom);
                }

             //   LogUtil.i(TAG,"???????????????"+System.currentTimeMillis());

            }


        }else{

            toolsHite();

        }



    }


    /**
     * ????????????
     *
     * */
    private void stopRecord(int i) {

        LogUtil.i(TAG,"stopRecord:"+i);

        upDataVoice("00:00",false);

        if (voice_Read_Thread != null) {

            voice_Read_Thread.setRecording(false);

        }




        if(i != 4){
            if(isInterphone()){
                Openfrom openfrom = new Openfrom();

                openfrom.setType(MsgResponse.Ptt);
                openfrom.setMessage("2");
                openfrom.setMessageID(getRandom());
                openfrom.setCode(Open_biz.SendMessage);
                openfrom.setUserId(MyApplicatoin.evenUser.getId());

                MyApplicatoin.setOpenfire(openfrom);
            }

        }




        voice_Read_Thread = null;

        LogUtil.i(TAG,"??????Time="+i);


    }


    public void startVoice() {

        try {

            if (voiceStart) {

                if(!isTouch){

                    startRecord();

                    isTouch = true;

                    upDataVoice("00:00",true);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            // ???
                            int k = 0;
                            while (isTouch){
                                try {

                                    Thread.sleep(25);
                                    mTime++;

                                    if(mTime % 40 == 0){
                                        k++;
                                        final String a;

                                        if(k < 10){

                                            a ="00:0"+String.valueOf(k);

                                            upDataVoice(a,true);


                                        }else if(k >= 10 && k < 60){
                                            a = "00:"+String.valueOf(k);

                                            upDataVoice(a,true);

                                        }else{

                                            isTouch = false;
                                            stopRecord(1);
                                        }
                                    }
                                } catch (InterruptedException e) {
                                    ExceptionUtil.handleException(e);
                                }

                            }

                        }

                    }).start();
                }

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            ExceptionUtil.handleException(e);
        }

    }

    public void stopVoice() {

        try {

            isTouch = false;
            reset();
            stopRecord(1);


        } catch (Exception e) {
            // TODO Auto-generated catch block
            ExceptionUtil.handleException(e);
        }

    }

    private void toolsHite() {

        long currentTime = Calendar.getInstance().getTimeInMillis();

        if(currentTime - lastHiteTime > MIN_CLICK_HITE_TIME){

            lastHiteTime = currentTime;

            Tools.showInfo(MainActivity.this,getResources().getString(R.string.chat_text_hite_button));

        }




    }

    private void upDataVoice(final String a, final boolean b) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (b){

                    if (voice_Read_Thread != null) {

                        if(voice_Read_Thread.isRecording()){

                            tv_voice_content.setText(a);
                            /*bt_Voice.setBackgroundResource(R.drawable.selector_button_voice_true);*/
                            bt_Voice.setBackgroundResource(R.mipmap.yuxinhuise);//????????????

                        }else{

                            bt_Voice.setBackgroundResource(R.mipmap.yuxinluse);//yuxinluse chat_button_voice_true
                            tv_voice_content.setText(null);
                            tv_voice_hint.setText(null);
                            tv_voice_content.setVisibility(View.GONE);
                            tv_voice_hint.setVisibility(View.GONE);

                        }


                    }else{

                        if(!voiceStart){
                            bt_Voice.setBackgroundResource(R.mipmap.yuxinhuise);//yuxinhuise  chat_button_voice_false
                        }else{
                            bt_Voice.setBackgroundResource(R.mipmap.yuxinluse);// yuxinluse chat_button_voice_true
                        }

                        tv_voice_content.setText(null);
                        tv_voice_hint.setText(null);
                        tv_voice_content.setVisibility(View.GONE);
                        tv_voice_hint.setVisibility(View.GONE);

                    }


                }else{

                    LogUtil.i(TAG,"upDataVoice:"+voiceStart);

                    if(!voiceStart){
                       bt_Voice.setBackgroundResource(R.mipmap.yuxinhuise);//yuxinhuise  chat_button_voice_false
                    }else{
                        bt_Voice.setBackgroundResource(R.mipmap.yuxinluse);//yuxinluse chat_button_voice_true
                    }

                    tv_voice_content.setText(null);
                    tv_voice_hint.setText(null);
                    tv_voice_content.setVisibility(View.GONE);
                    tv_voice_hint.setVisibility(View.GONE);


                }
            }
        });

    }



    private void initGPS() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // ??????GPS??????????????????????????????????????????
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(getResources().getString(R.string.sms_dialog_gps_hite));
            dialog.setCancelable(false);
            dialog.setMessage(getResources().getString(R.string.sms_dialog_gps_message));
            dialog.setPositiveButton(getResources().getString(R.string.sms_dialog_hite_permission_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // ???????????????????????????????????????GPS
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, 0); // ???????????????????????????????????????
                }
            });
            dialog.setNeutralButton(getResources().getString(R.string.sms_dialog_hite_permission_up), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            });
            dialog.show();

        } else {

            dragLayout.close();
        //    mapFragment.setDragLayoutClick(1);

        }
    }


    private int setReflashData() {

        int length = 0;

        try {

            DAOmemberDao memberDao = DAOService.get(MainActivity.this).getsession().getDAOmemberDao();

            List<DAOmember> member_list = memberDao.loadAll();

            DAOMesgDao mMesg =  DAOService.get(getApplication()).getsession().getDAOMesgDao();

            List<DAOMesg> mesg_list = null;

            if (!isInterphone()) {

                return length;
            }
            String fromName = Tools.getGroupName(MyApplicatoin.interphone.getImCode().getName());


            page++;
            mesg_list = mMesg.queryBuilder()
                    .orderDesc(DAOMesgDao.Properties.Id)
                    .offset(page * list_item_size)
                    .limit(list_item_size)
                    .where(DAOMesgDao.Properties.From_id.eq(Long.valueOf(fromName)))
                    .list();


            List<DAOMesg> mesgs = new ArrayList<>();

            //??????????????????

            if(mesg_list != null){
                for(int i = mesg_list.size()-2;i>=0;i--){
                    mesgs.add(mesg_list.get(i));
                }

                List<Mesage_entity> mesage_list = new ArrayList<Mesage_entity>();

                for(Mesage_entity a :mesage_entity_list){
                    mesage_list.add(a);
                }

                length = mesage_list.size();

                mesage_entity_list.clear();

                for(DAOMesg m : mesgs){

                    Mesage_entity msg = new Mesage_entity();

                    msg.setId(m.getId());
                    msg.setContent(m.getContent());
                    msg.setMgid(m.getMgid());
                    msg.setContent_length(m.getContent_length());
                    msg.setCreate_time(m.getCreate_time().getTime());
                    msg.setFrom_id(m.getFrom_id());
                    msg.setUser_id(m.getUser_id());
                    msg.setIs_ok((byte) 2);
                    msg.setMesg_type(m.getMesg_type());

                    for(DAOmember mem :member_list){

                        if(mem.getUser_id() == m.getUser_id()){
                            msg.setSex(mem.getSex());
                            msg.setAvatar_url(mem.getAvatar_url());
                            msg.setUser_neme(mem.getUser_name());
                        }
                    }

                    mesage_entity_list.add(msg);
                }


                for(Mesage_entity a :mesage_list){
                    mesage_entity_list.add(a);
                }

            }

            LogUtil.i(TAG,"mesgs:"+mesage_entity_list.size());
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }

        return length;

    }


    private boolean isBLE(){

        if(!MyApplicatoin.IS_BLE){

            tooleInterphone();
            return false;
        }

        if(MyApplicatoin.interphone == null){

            return false;
        }

        if(MyApplicatoin.evenUser == null){

            tooleUser();
            return false;
        }


        return true;

    }

    /**
     * ????????????
     *
     * */
    private boolean isInterphone(){

        if(!MyApplicatoin.IS_BLE){

            tooleBle();
            return false;
        }

        if(MyApplicatoin.interphone == null){

            return false;
        }
        if(MyApplicatoin.interphone.getImCode() != null){

            /*if(MyApplicatoin.interphone.getImCode().getId() != null){

                if(MyApplicatoin.interphone.getImCode().getId() == 0){

                    return false;
                }

            }*/

            if(MyApplicatoin.interphone.getImCode().getName() == null){

                return false;

            }else{

                String fromName = null;

                fromName = Tools.getGroupName(MyApplicatoin.interphone.getImCode().getName());

                if(fromName != null){


                    if(fromName.equals("0")) {

                        return false;

                    }
                }else{
                    return false;
                }
            }


        }else{

            return false;
        }
        if(MyApplicatoin.evenUser == null){

            //tooleUser();
            return false;
        }


        return true;

    }

    private void tooleBle() {

        long currentTime = Calendar.getInstance().getTimeInMillis();

        if(currentTime - lastHiteTime > MIN_CLICK_HITE_TIME){

            Tools.showInfo(MainActivity.this,getResources().getString(R.string.sms_main_toast_hite_connect_device));

        }
        lastHiteTime = currentTime;

    }

    private void tooleUser() {

        long currentTime = Calendar.getInstance().getTimeInMillis();

        if(currentTime - lastHiteTime > MIN_CLICK_HITE_TIME){

            if(!MyApplicatoin.isToos){
                Tools.showInfo(MainActivity.this,getResources().getString(R.string.sms_main_toast_hite_user));
            }


        }
        lastHiteTime = currentTime;

    }


    private void tooleInterphone() {

        long currentTime = Calendar.getInstance().getTimeInMillis();

        if(currentTime - lastHiteTime > MIN_CLICK_HITE_TIME){

            Tools.showInfo(MainActivity.this,getResources().getString(R.string.sms_main_toast_hite_connect_device));

        }
        lastHiteTime = currentTime;


    }


    private void upVoiceButton(boolean start) {

        LogUtil.i(TAG,"upVoiceButton:"+start);

        if(start){

            bt_send.setClickable(true);

       /*     bt_send.setBackgroundResource(R.drawable.chat_button_message_true);*/

            voiceStart = true;

            if(voice_Read_Thread == null){

                bt_Voice.setBackgroundResource(R.mipmap.yuxinluse);//yuxinluse chat_button_voice_true

                tv_voice_content.setText(null);
                tv_voice_hint.setText(null);
                tv_voice_content.setVisibility(View.GONE);
                tv_voice_hint.setVisibility(View.GONE);

            }



        } else {

            bt_send.setClickable(false);

           /* bt_send.setBackgroundResource(R.drawable.chat_button_message_false);*/

            voiceStart = false;

            if(voice_Read_Thread == null){

                bt_Voice.setBackgroundResource(R.mipmap.yuxinhuise);//yuxinhuise  chat_button_voice_false

            }

        }

    }


    private boolean isJoinFrom(){

        if(!MyApplicatoin.IS_BLE){

            return false;
        }

        if(MyApplicatoin.interphone == null){

            return false;
        }
        if(MyApplicatoin.interphone.getImCode() != null){

            /*if(MyApplicatoin.interphone.getImCode().getId() != null){

                if(MyApplicatoin.interphone.getImCode().getId() == 0){

                    return false;
                }

            }*/

            if(MyApplicatoin.interphone.getImCode().getName() == null){

                return false;
            }

        }else{
            return false;
        }
        if(MyApplicatoin.evenUser == null){

            return false;
        }


        return true;

    }

    /**
     *
     * ?????????????????????openfire?????????
     *
     * */
    private void newVoiceOpenFire(Voice voice) {

    //    LogUtil.i(TAG,"111111newVoiceOpenFire:"+voice_code+"-"+voice.getBytes().length+"-"+voice_pack.size());

        try {

            if(voice.getBytes().length > 0){
                for(byte as : voice.getBytes()){
                    voice_pack.add(as);
                }
            }


            if(voice_code < 120){

                if(voice_pack.size() >= MyApplicatoin.voice_size){

                    byte[] bytes = new byte[voice_pack.size()];

                    for(int i=0;i<voice_pack.size();i++){

                        bytes[i] =voice_pack.get(i);

                    }
                    voice_pack.clear();


                    Openfrom openfrom = new Openfrom();

                    openfrom.setType(MsgResponse.Voice);

                    String msg = Base64.encodeToString(bytes, Base64.DEFAULT);

                    LogUtil.i(TAG,"uuid:"+msg);

                    voice_code++;

                    if(voice_code >= 100){
                        voice_code = 0;
                    }
                    byte[] a = {voice_code};

                 //   openfrom.setMessage(new String(a)+voice.getFireName() + msg);
                    openfrom.setMessage(new String(a)+msg);

                    openfrom.setMessageID(voice.getPackID());
                    openfrom.setCode(Open_biz.SendMessage);
                    openfrom.setUserId(MyApplicatoin.evenUser.getId());

                    MyApplicatoin.setOpenfire(openfrom);



                }


            }else if(voice_code >= 120){


                byte[] bytes = new byte[voice_pack.size()];

                for(int i=0;i<voice_pack.size();i++){

                    bytes[i] =voice_pack.get(i);

                }

                voice_pack.clear();


                Openfrom openfrom = new Openfrom();

                openfrom.setType(MsgResponse.Voice);

                String msg = Base64.encodeToString(bytes, Base64.DEFAULT);

                byte[] a = {voice_code};

            //    openfrom.setMessage(new String(a)+voice.getFireName() + msg);

                openfrom.setMessage(new String(a)+msg);

                openfrom.setMessageID(voice.getPackID());
                openfrom.setCode(Open_biz.SendMessage);
                openfrom.setUserId(MyApplicatoin.evenUser.getId());

                MyApplicatoin.setOpenfire(openfrom);


                voice_code = 0;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     *
     * ?????????????????????BLE?????????
     *
     * */

    private void newVoiceMessage(Voice voice) {


        try {

            // ?????????????????????????????????

            boolean isk = false;
            for(long l : MyApplicatoin.ls){

                if(l == voice.getPackID()){
                    isk = true;
                }

            }

            if(isk){
                return;
            }



            if(MyApplicatoin.dataCode == BluetoothLeService.versions_one){

                byte [] voiceData = ATcommand.at_set_file_user_chat(Opcode.SET, Objcode.FILE_VOICE,voice.getUserID(),voice.getPackID(),voice.getBytes());
                Log.d("dddd", EncryptUtil.bytesToHexString(voiceData));
                BluetoothLeService.le_service.sendWriteByte(Objcode.FRIDY,voiceData);

            }else{

                byte prica = (byte) (1 | 0x80);

                byte [] voiceData = ATcommand.at_set_file_user_chat_versions(prica,voice.getUserID(),voice.getPackID(),voice.getBytes());
                Log.d("dddd", EncryptUtil.bytesToHexString(voiceData));
                BluetoothLeService.le_service.sendWriteByte(Objcode.MESSAGE,voiceData);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * ??????????????????apk?????????
     *
     * @param mContext
     * @return
     */
    public static String getVersionName(Context mContext) {
        String versionCode = null;
        try {
            //??????????????????????????????AndroidManifest.xml???android:versionCode
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            ExceptionUtil.handleException(e);
        }
        return versionCode;
    }

    /**
     * ?????????????????????
     *
     * @param context ?????????
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            ExceptionUtil.handleException(e);
        }
        return verName;
    }

    private void stopTrace(){

        File file = new File(MainActivity.this.getFilesDir().getParent(),"/databases/sms.db");

        boolean exists = file.exists();

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

        File toFile = new File(path,"/copysms.db");

        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            if(!toFile.exists()){
                boolean newFile = toFile.createNewFile();
            }

            fis = new FileInputStream(file);
            fos = new FileOutputStream(toFile);

            byte[] buf = new byte[128];

            int len;

            while ((len = fis.read(buf)) != -1){
                fos.write(buf,0,len);
            }
            fos.flush();
        } catch (IOException e) {

            ExceptionUtil.handleException(e);

        }finally {

            try {

                if(fis != null){
                    fis.close();
                }
                if(fos != null){
                    fos.close();
                }


            } catch (IOException e) {
                ExceptionUtil.handleException(e);
            }
        }
    }


    private long getRandom() {

        int s = 0;

        try {
            int max=6535;

            int min=0;

            Random random = new Random();

            s = random.nextInt(max)%(max-min+1) + min;

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return s;
        }

    }


    public void getFirmware() {

        Production production  = null;
        try {
            production = new Production();

            production.setModle("GLOBALWALKIE");
            production.setHardware_version("1");
            production.setState("test");

            SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(GlobalConsts.PRECURSOR, Context.MODE_PRIVATE);

            PrecursorString = new String[]{

                    sharedPreferences.getString(GlobalConsts.PRECURSOR_FIRMWARE_VERSION, ""),
                    sharedPreferences.getString(GlobalConsts.PRECURSOR_FIRMWARE_URL, ""),
                    sharedPreferences.getString(GlobalConsts.PRECURSOR_USER_GUI_VERSION, ""),
                    sharedPreferences.getString(GlobalConsts.PRECURSOR_USER_GUI_URL, ""),
                    sharedPreferences.getString(GlobalConsts.PRECURSOR_QA_VERSION, ""),
                    sharedPreferences.getString(GlobalConsts.PRECURSOR_QA_URL, ""),

            };

            Log.d("resultString","22222GlobalConsts.PRECURSOR_FIRMWARE_VERSION:"+sharedPreferences.getString(GlobalConsts.PRECURSOR_FIRMWARE_VERSION,"'")
                    +"  GlobalConsts.PRECURSOR_FIRMWARE_URL:"+ sharedPreferences.getString(GlobalConsts.PRECURSOR_FIRMWARE_URL,"'")+" getProduct_model:" +MyApplicatoin.interphone.getProduct_model());
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }


        sms_request.dorequest(MainActivity.this, sms_request.GET, production, new RequestCB() {
            @Override
            public void respone(int opcode, Object obj) {

                LogUtil.i(TAG, "getFirmware:" + opcode);

                if (opcode == sms_request.GET) {

                    try {

                        MyApplicatoin.production = (Production) obj;

                        LogUtil.i(TAG,"getFirmware:"+MyApplicatoin.production.toString());

                        LogUtil.i(TAG, "Production:" + MyApplicatoin.production);
                        LogUtil.i(TAG, "PrecursorString:" + Arrays.toString(PrecursorString));


                        if(PrecursorString[0].equals("")){

                            Intent intent = new Intent(MainActivity.this, UpdataService.class);
                            intent.setAction(UpdataService.NITECORE);
                            bindService(intent, connection, Context.BIND_AUTO_CREATE);

                        }else if(!PrecursorString[0].equals("") && !MyApplicatoin.production.getFirmware_version().equals(PrecursorString[0])){

                            Intent intent = new Intent(MainActivity.this, UpdataService.class);
                            intent.setAction(UpdataService.NITECORE);
                            bindService(intent, connection, Context.BIND_AUTO_CREATE);

                        }
                        if(!PrecursorString[2].equals("") && !MyApplicatoin.production.getUser_gui_version().equals(PrecursorString[2])){

                            Intent intent = new Intent(MainActivity.this, UpdataService.class);
                            intent.setAction(UpdataService.NITECORE);
                            bindService(intent, connection, Context.BIND_AUTO_CREATE);

                        }
                        if(!PrecursorString[4].equals("") && !MyApplicatoin.production.getQa_version().equals(PrecursorString[4])){

                            Intent intent = new Intent(MainActivity.this, UpdataService.class);
                            intent.setAction(UpdataService.NITECORE);
                            bindService(intent, connection, Context.BIND_AUTO_CREATE);

                        }
                    } catch (Exception e) {
                        ExceptionUtil.handleException(e);
                    }

                }
            }
        });

    }




    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            service = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder services) {

            service = ((UpdataService.UpdataBinder) services).getService();
            // ??????Service?????????????????????

            if(service != null){

                if(service.UpDataServiceAcoin.equals(UpdataService.APK)){

                    service.excute(UpdataService.apkfile,android_url,"");

                }else if(service.UpDataServiceAcoin.equals(UpdataService.NITECORE)){

                    new AsyncTask<Object, Object, String>() {
                        @Override
                        protected String doInBackground(Object... params) {

                            try {
                                if(PrecursorString[0].equals("")){

                                    PrecursorString[0] = MyApplicatoin.production.getFirmware_version();

                                    PrecursorString[1] = service.excute(UpdataService.firmwarefile,MyApplicatoin.production.getFirmware_url(),"");
                                    Log.d("resultString","333MyApplicatoin.production.getFirmware_url():"+MyApplicatoin.production.getFirmware_url());
                                }else{

                                    if(!MyApplicatoin.production.getFirmware_version().equals(PrecursorString[0])){

                                        PrecursorString[0] = MyApplicatoin.production.getFirmware_version();

                                        PrecursorString[1] = service.excute(UpdataService.firmwarefile,MyApplicatoin.production.getFirmware_url(),"");
                                        Log.d("resultString","444MyApplicatoin.production.getFirmware_url():"+MyApplicatoin.production.getFirmware_url());
                                    }
                                }

                                if(PrecursorString[2].equals("")){

                                    PrecursorString[2] = MyApplicatoin.production.getUser_gui_version();

                                    PrecursorString[3] = service.excute(UpdataService.imgfile,MyApplicatoin.production.getUser_gui_url(),"User_gui");

                                }else{

                                    if(!MyApplicatoin.production.getUser_gui_version().equals(PrecursorString[2])){

                                        PrecursorString[2] = MyApplicatoin.production.getUser_gui_version();

                                        PrecursorString[3] = service.excute(UpdataService.imgfile,MyApplicatoin.production.getUser_gui_url(),"User_gui");
                                    }

                                }

                                if(PrecursorString[4].equals("")){

                                    PrecursorString[4] = MyApplicatoin.production.getQa_version();

                                    PrecursorString[5] = service.excute(UpdataService.imgfile,MyApplicatoin.production.getQa_url(),"qa");
                                }else{

                                    if(!MyApplicatoin.production.getQa_version().equals(PrecursorString[4])){

                                        PrecursorString[4] = MyApplicatoin.production.getQa_version();

                                        PrecursorString[5] = service.excute(UpdataService.imgfile,MyApplicatoin.production.getQa_url(),"qa");
                                    }

                                }

                                SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(GlobalConsts.PRECURSOR, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                editor.putString(GlobalConsts.PRECURSOR_FIRMWARE_VERSION,PrecursorString[0]);
                                editor.putString(GlobalConsts.PRECURSOR_FIRMWARE_URL,PrecursorString[1]);
                                editor.putString(GlobalConsts.PRECURSOR_USER_GUI_VERSION,PrecursorString[2]);
                                editor.putString(GlobalConsts.PRECURSOR_USER_GUI_URL,PrecursorString[3]);
                                editor.putString(GlobalConsts.PRECURSOR_QA_VERSION,PrecursorString[4]);
                                editor.putString(GlobalConsts.PRECURSOR_QA_URL,PrecursorString[5]);

                                LogUtil.i(TAG,PrecursorString[0]);
                                LogUtil.i(TAG,PrecursorString[1]);
                                LogUtil.i(TAG,PrecursorString[2]);
                                LogUtil.i(TAG,PrecursorString[3]);
                                LogUtil.i(TAG,PrecursorString[4]);
                                LogUtil.i(TAG,PrecursorString[5]);

                                editor.commit();


                                MyApplicatoin.production.setUser_gui_url(PrecursorString[3]);
                                MyApplicatoin.production.setQa_url(PrecursorString[5]);
                                Log.d("resultString","1111111GlobalConsts.PRECURSOR_FIRMWARE_VERSION:"+sharedPreferences.getString(GlobalConsts.PRECURSOR_FIRMWARE_VERSION,"'")
                                        +"  GlobalConsts.PRECURSOR_FIRMWARE_URL:"+ sharedPreferences.getString(GlobalConsts.PRECURSOR_FIRMWARE_URL,"'"));
                                LogUtil.i(TAG,"?????????????????????");
                            } catch (Exception e) {
                                ExceptionUtil.handleException(e);
                            }

                            return null;
                        }

                        @Override
                        protected void onPostExecute(String path) {

                            try {
                                if(connection != null){
                                    unbindService(connection);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    }.execute();

                }else if(service.UpDataServiceAcoin.equals(UpdataService.WELCOME)){

                    new AsyncTask<Object, Object, String>() {
                        @Override
                        protected String doInBackground(Object... params) {
                            return service.excute(UpdataService.imgfile,url,"adv");
                        }

                        @Override
                        protected void onPostExecute(String path) {

                            try {
                                if(path != null){

                                    SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(GlobalConsts.PRECURSOR, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();

                                    editor.putString(GlobalConsts.PRECURSOR_ADV_URL,path);
                                    editor.putString(GlobalConsts.PRECURSOR_ADV_VERSION,a_adv_version);

                                    editor.commit();

                                    LogUtil.i(TAG,"?????????????????????");

                                }else{

                                    LogUtil.i(TAG,"?????????????????????");

                                }

                                try {
                                    if(connection != null){
                                        unbindService(connection);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } catch (Exception e) {

                                ExceptionUtil.handleException(e);

                            }
                        }
                    }.execute();

                }else {

                    try {
                        if(connection != null){
                            unbindService(connection);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }else{

                try {
                    if(connection != null){
                        unbindService(connection);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }




        }
    };



    private String url;


    private String a_adv_version;

    private void setWelcome(){

        Precursor precursor = new Precursor();

        precursor.setSeries("GLOBALWALKIE");

        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(GlobalConsts.PRECURSOR, Context.MODE_PRIVATE);
        final String PrecursorString[] = new String[]{sharedPreferences.getString(GlobalConsts.PRECURSOR_ADV_VERSION, ""),
                sharedPreferences.getString(GlobalConsts.PRECURSOR_ADV_URL, ""),
                sharedPreferences.getString(GlobalConsts.PRECURSOR_ANDROID_URL, ""),
                sharedPreferences.getString(GlobalConsts.PRECURSOR_ANDROID_VERSION, ""),
                sharedPreferences.getString(GlobalConsts.PRECURSOR_ANDROID_DESCRIPTION, "")};

        sms_request.dorequest(MainActivity.this, sms_request.GET, precursor, new RequestCB() {
            @Override
            public void respone(int opcode, Object obj) {


                if(opcode == sms_request.GET){
                    try {
                        Precursor precursor = (Precursor) obj;

                        LogUtil.i(TAG,"setWelcome:"+precursor.toString());

                        if (precursor != null) {

                            a_adv_version = precursor.getAdv_version();

                            android_version = precursor.getAndroid_version();
                            android_url = precursor.getAndroid_url();
                            android_description = precursor.getAndroid_description();


                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString(GlobalConsts.PRECURSOR_ANDROID_URL, android_url);
                            editor.putString(GlobalConsts.PRECURSOR_ANDROID_VERSION, android_version);
                            editor.putString(GlobalConsts.PRECURSOR_ANDROID_DESCRIPTION, android_description);
                            editor.commit();


                            if(PrecursorString[0].equals("")){

                                url = precursor.getAdv_url();

                                if(url != null){

                                    Intent intent = new Intent(MainActivity.this, UpdataService.class);
                                    intent.setAction(UpdataService.WELCOME);
                                    bindService(intent, connection, Context.BIND_AUTO_CREATE);

                                }else{

                                    LogUtil.i(TAG,"????????????url = null");
                                }

                            }else{


                                if(!precursor.getAdv_version().equals(PrecursorString[0])){

                                    //???????????????????????????????????????

                                    url = precursor.getAdv_url();

                                    if(url != null){

                                        Intent intent = new Intent(MainActivity.this, UpdataService.class);
                                        intent.setAction(UpdataService.WELCOME);
                                        bindService(intent, connection, Context.BIND_AUTO_CREATE);

                                    }else{

                                        LogUtil.i(TAG,"????????????url = null");
                                    }

                                }
                            }

                        }else{

                            LogUtil.i(TAG,"???????????????precursor = null");

                        }
                    } catch (Exception e) {
                        ExceptionUtil.handleException(e);
                    }
                }

            }
        });

    };
    public static Timer task = new Timer();
    public static TimerTask timer = null;

    private int isLogin = 30;


    private void startTime(){

        timer = new TimerTask() {
            @Override
            public void run() {

                try {

                    if(isLogin > 0){
                        showNetSpeed();
                    }else{
                        timer.cancel();

                    }
                } catch (Exception e) {
                    ExceptionUtil.handleException(e);
                }

            }
        };

        task.schedule(timer,1000,1000);

    };

    private void showNetSpeed() {

        LogUtil.i(TAG,"showNetSpeed:"+isLogin);

        if(MyApplicatoin.evenUser != null && MyApplicatoin.IS_BLE){

            isLogin--;

            if(isLogin == 20){

                if(MyApplicatoin.current_network_type != MyApplicatoin.NETWORK_TYPE_NONE){
                    setWelcome();

                }



            }else if(isLogin == 10){

                if(MyApplicatoin.current_network_type != MyApplicatoin.NETWORK_TYPE_NONE){

                    getFirmware();
                }


            }else if(isLogin == 0){

                timer.cancel();

                if(MyApplicatoin.current_network_type == MyApplicatoin.NETWORK_TYPE_WIFI){

                    if(android_version != null && android_url != null && android_description != null){

                        final String versionCode = getVersionName(MainActivity.this);

                        LogUtil.i(TAG,"versionCode:"+versionCode+",code:"+android_version);

                        if(!versionCode.equals(android_version)){

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(!MyApplicatoin.isUpdata){
                                        showLoginDialog(android_description);
                                    }
                                }
                            });
                        }
                    }
                }

            }
        }
    }


    private void renewal(){
            SharePreferenceUtil.saveStringDataToSharePreference(MainActivity.this, SharePreferenceConstant.RENEWAL, "renewal");
            if(android_version != null && android_url!= null && android_description != null){
                final String versionCode = getVersionName(MainActivity.this);
                if(!versionCode.equals(android_version)){

                    if(!MyApplicatoin.isUpdata){
                        showLoginDialogFirst(android_description);
                    }else{
                        Tools.showInfo(MainActivity.this,getResources().getString(R.string.isupdata));
                    }


                }/*else{
                    Tools.showInfo(MainActivity.this,getResources().getString(R.string.sms_main_toast_hite_updata));
                }*/
            }
    }



    private void showPop() {
        // ??????????????????
        mPopupWindow.setContentView(LayoutInflater.from(this).inflate(R.layout.pop_add, null));
        // ????????????????????????????????????????????????????????????????????????
        mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // ??????pop????????????
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x0000));
        // ??????pop????????????
        mPopupWindow.setAnimationStyle(R.style.pop_add);
        // ??????pop????????????????????????false?????????????????????????????????Activity?????????pop??????Editor?????????focusable????????????true
        mPopupWindow.setFocusable(true);
        // ??????pop???????????????false??????????????????????????????true
        mPopupWindow.setTouchable(true);
        // ????????????pop????????????????????????false??????focusable???true???????????????????????????
        mPopupWindow.setOutsideTouchable(true);
        // ????????? + ??????????????????????????????????????????
        mPopupWindow.showAsDropDown(sms_return_icon, -100, 15);
        // ??????pop??????????????????????????????????????????
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                toggleBright();
            }
        });

        joinGroup = mPopupWindow.getContentView().findViewById(R.id.chat_draglayout_joingroup);
        shareGroup = mPopupWindow.getContentView().findViewById(R.id.chat_draglayout_sharegroup);
        histruyGroup = mPopupWindow.getContentView().findViewById(R.id.chat_draglayout_histroy);
        leaveGroup = mPopupWindow.getContentView().findViewById(R.id.chat_draglayout_leavegroup);


       // ptt_switch= mPopupWindow.getContentView().findViewById(R.id.ptt_switch);


        joinGroup.setOnClickListener(this);
        shareGroup.setOnClickListener(this);
        histruyGroup.setOnClickListener(this);
        leaveGroup.setOnClickListener(this);

      /*  ptt_switch.setSlideListener(new SlideSwitch.SlideListener() {
            @Override
            public void open() {
                pTT.setOpen(true);
            }

            @Override
            public void close() {
                pTT.setOpen(false);
            }
        });*/
    }

    private void toggleBright() {
        // ????????????????????????????????? ????????? ??????????????????????????????????????????????????????0.5f--1f???
        animUtil.setValueAnimator(START_ALPHA, END_ALPHA, DURATION);
        animUtil.addUpdateListener(new AnimUtil.UpdateListener() {
            @Override
            public void progress(float progress) {
                // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                bgAlpha = bright ? progress : (START_ALPHA + END_ALPHA - progress);
                backgroundAlpha(bgAlpha);
            }
        });
        animUtil.addEndListner(new AnimUtil.EndListener() {
            @Override
            public void endUpdate(Animator animator) {
                // ?????????????????????????????????????????????
                bright = !bright;
            }
        });
        animUtil.startAnimator();
    }


    /**
     * ???????????????????????????????????????????????????????????????????????????
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        // 0.0-1.0
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
        // everything behind this window will be dimmed.
        // ???????????????????????????????????????????????????????????????
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }




    public void ignoreBatteryOptimization(Activity activity) {//???????????????????????????

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            //  ????????????APP??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            if (!powerManager.isIgnoringBatteryOptimizations(activity.getPackageName())) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                //??????????????????intent????????????????????????????????????????????????activity
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                }
            } else {
            }
        } else {
        }

    }



    public void againexit(){

        /*final AlertDialog dialogLeave = new AlertDialog.Builder(MainActivity.this).create();
        View viewLeave = View.inflate(MainActivity.this, R.layout.dialog_warning_layout, null);
        dialogLeave.setView(viewLeave);
        dialogLeave.setInverseBackgroundForced(true);
        dialogLeave.setCancelable(false);
        final Button bt_cancel_Leave = (Button) viewLeave.findViewById(R.id.button_cancel);
        final Button bt_ok_Leave = (Button) viewLeave.findViewById(R.id.button_ok);
        final TextView tv_title_Leave = (TextView) viewLeave.findViewById(R.id.title);
        final TextView content = (TextView) viewLeave.findViewById(R.id.content);
        tv_title_Leave.setText("??????????????????");
        content.setText("???????????????????????????????????????");
        //dialog????????????
        Window window_Leave = dialogLeave.getWindow();
        window_Leave.setBackgroundDrawableResource(android.R.color.transparent);
        bt_cancel_Leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogLeave.cancel();
            }
        });
        bt_ok_Leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogLeave.cancel();
                finish();
            }
        });
        dialogLeave.show();*/

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("??????????????????")//????????????
                .setMessage("???????????????????????????????????????")//????????????
                .setCancelable(false)//??????????????????????????????????????????????????????
                .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton("??????",(dialog,i) ->{
            System.exit(0);
            dialog.dismiss();
            finish();
        });

        AlertDialog alertDialog = builder.create();

        alertDialog.show();

    }



}
