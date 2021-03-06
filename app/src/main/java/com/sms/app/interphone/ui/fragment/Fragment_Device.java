package com.sms.app.interphone.ui.fragment;

import android.animation.Animator;
import android.app.AlertDialog;
import android.app.Fragment;
import android.bluetooth.BluetoothProfile;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sms.app.framework.RequestCB;
import com.sms.app.framework.communication.localayer.Interphone_notify_listenner;
import com.sms.app.framework.communication.localayer.attr_listenner;
import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.framework.communication.localayer.cmd.Opcode;
import com.sms.app.framework.dao.bean.DAOGroup;
import com.sms.app.framework.dao.bean.DAOMesg;
import com.sms.app.framework.dao.bean.DAOmember;
import com.sms.app.framework.sms_request;
import com.sms.app.framework.trans.bean.Interphone;
import com.sms.app.framework.trans.bean.Precursor;
import com.sms.app.framework.trans.sms_fw_api;
import com.sms.app.interphone.R;
import com.sms.app.interphone.services.UpdataService;
import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.interphone.ui.activity.BlescanActivity;
import com.sms.app.interphone.ui.activity.HistruyGroupActivity;
import com.sms.app.interphone.ui.activity.HiteActivity;
import com.sms.app.interphone.ui.activity.LoginActivity;
import com.sms.app.interphone.ui.activity.MainActivity;
import com.sms.app.interphone.ui.activity.ModificationMessageActivity;
import com.sms.app.interphone.ui.activity.SilpLoginActivity;
import com.sms.app.interphone.ui.activity.UpDataActivity;
import com.sms.app.interphone.ui.activity.WelcomeActivity;
import com.sms.app.interphone.util.frequentlyutil.Constants;
import com.sms.app.interphone.util.frequentlyutil.SharePreferenceConstant;
import com.sms.app.interphone.util.frequentlyutil.SharePreferenceUtil;
import com.sms.app.interphone.util.interfaces.NoDoubleClickListener;
import com.sms.app.interphone.util.msnutil.ExceptionUtil;
import com.sms.app.interphone.util.msnutil.Tools;
import com.sms.app.interphone.view.CircleImageView;
import com.sms.app.interphone.view.PowerView;
import com.sms.app.interphone.view.SlideSwitch;
import com.sms.app.interphone.view.SwitchButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2016/7/12.
 */
public class Fragment_Device extends Fragment implements View.OnClickListener,RadioGroup.OnCheckedChangeListener {

    private LinearLayout linearLayout;

    private static String TAG = "YanShi...Log - Fragment_Device";


    private int switchInt = 0;
    private TextView tv_ble_name;
    private TextView tv_ble_tx;
    private TextView tv_ble_rx;
    private TextView tv_ble_freq;
    private TextView tv_ble_freq_code;

    private TextView tv_ble_sq;
    private TextView tv_ble_vox;
    private TextView tv_ble_rf;
    private TextView tv_ble_tx_power;

    private TextView tv_ble_volt;

    private LinearLayout ble_Name;
    private LinearLayout ble_Tx_Code;
    private LinearLayout ble_Rx_Code;

    private LinearLayout ble_Freq;
    private LinearLayout ble_Freq_code;

    private LinearLayout ble_Sq;
    private LinearLayout ble_Vox;
    private LinearLayout ble_Rf;
    private LinearLayout ble_Tx_power;

    private LinearLayout ble_Volt;


    private LinearLayout ble_factory_reset;
    private LinearLayout ble_updata,app_updata_layout;

    private LinearLayout ll_Device_reset;

    /*private SwitchButton ble_Bw;
    private SlideSwitch ble_Bluetooth;
    private SwitchButton ble_Mode;*/

    private SwitchButton ble_Tx_hite;
    private SwitchButton ble_Stop_hite;

    private SwitchButton ble_Bw;
    private SwitchButton ble_Disconnec;
    private SwitchButton ble_Voice;
    private LinearLayout ll_Looking_equipment;

    private String android_url;
    private String android_version;
    private String android_description;


    private String[] freq_code_data = {"1. 409.7500 MHZ","2. 409.7625 MHZ",
            "3. 409.7750 MHZ","4. 409.7875 MHZ",
            "5. 409.8000 MHZ","6. 409.8125 MHZ",
            "7. 409.8250 MHZ","8. 409.8375 MHZ",
            "9. 409.8500 MHZ","10. 409.8625 MHZ",
            "11. 409.8750 MHZ","12. 409.8875 MHZ",
            "13. 409.9000 MHZ","14. 409.9125 MHZ",
            "15. 409.9250 MHZ","16. 409.9375 MHZ",
            "17. 409.9500 MHZ","18. 409.9625 MHZ",
            "19. 409.9750 MHZ","20. 409.9875 MHZ"};


    private String[] tx_code_data = {"OFF", "67.0", "69.3", "71.9", "74.4", "77.0", "79.7", "82.5", "85.4",
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


    private String[] vox_data = {"OFF","NO:1","NO:2","NO:3","NO:4","NO:5","NO:6","NO:7","NO:8","NO:9",};

    private String[] rf_data = {MyApplicatoin.mContext.getResources().getString(R.string.sms_ble_setting_rf_off),MyApplicatoin.mContext.getResources().getString(R.string.sms_ble_setting_rf_on),MyApplicatoin.mContext.getResources().getString(R.string.sms_ble_setting_rf_automate)};

    private String[] tx_power_data = {MyApplicatoin.mContext.getResources().getString(R.string.sms_ble_setting_tx_power_low),MyApplicatoin.mContext.getResources().getString(R.string.sms_ble_setting_tx_power_tall)};


    private int[] c = {409750000, 409762500,
            409775000,409787500,
            409800000,409812500,
            409825000,409837500,
            409850000,409862500,
            409875000,409887500,
            409900000,409912500,
            409925000,409937500,
            409950000,409962500,
            409975000,409987500,

    };


    private String[] bw = {"0","1"};


    private UpdataService service;

    private String url;

    private String PrecursorString[];

    private long lastHiteTime = 0;

    View view;

    private TextView[] tvArray = new TextView[12];
    private TextView[] tvhiteArray = new TextView[7];

    private List<Integer> freq = new ArrayList<>();


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

    private boolean isTitleSwitch = true;


    private ImageView iv_quit;
    private TextView user_name;
    private TextView tv_sex;
    private TextView e_mail;

    private ImageView iv_name;
    CircleImageView sms_main_icon;

    private LinearLayout ll_specification,ll_common_issue;

    private  ImageView iv_seek_device;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            view = View.inflate(getActivity(), R.layout.fragment_device, null);
            setUi();
            setView();
            setLinener();
            setUserData();

            /*String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

            File toFile = new File(path,"/SMSException.txt");

            FileOutputStream fos = null;

            try {


                if(!toFile.exists()){
                    boolean newFile = toFile.createNewFile();
                }

                String string = "SMS ????????????";

                byte[] data = string.getBytes();

                fos = new FileOutputStream(toFile);

                fos.write(data,0,data.length);

                fos.flush();

            } catch (IOException e) {

                ExceptionUtil.handleException(e);

            }finally {

                try {
                    fos.close();
                } catch (IOException e) {
                    ExceptionUtil.handleException(e);
                }
            }*/


        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
        return view;
    }

    /**
     * ?????????UI???????????????????????????
     * */
    private void setView() {

        linearLayout = (LinearLayout) view.findViewById(R.id.ble_scrollView);

        ble_Name = (LinearLayout) view.findViewById(R.id.ble_set_name_layout);

        ble_Tx_Code = (LinearLayout) view.findViewById(R.id.ble_set_tx_layout);
        ble_Rx_Code = (LinearLayout) view.findViewById(R.id.ble_set_rx_layout);
        ble_Freq = (LinearLayout) view.findViewById(R.id.ble_set_freq_layout);
        ble_Freq_code = (LinearLayout) view.findViewById(R.id.ble_set_freq_code_layout);
        ll_Device_reset= (LinearLayout) view.findViewById(R.id.ll_Device_reset);


        ble_Sq = (LinearLayout) view.findViewById(R.id.ble_set_sq_layout);
        ble_Vox = (LinearLayout) view.findViewById(R.id.ble_set_vox_layout);
        ble_Rf = (LinearLayout) view.findViewById(R.id.ble_set_rf_layout);
        ble_Tx_power = (LinearLayout) view.findViewById(R.id.radioGroup_set_tx_power);

        ble_Volt = (LinearLayout) view.findViewById(R.id.ble_set_volt_layout);


        ble_factory_reset = (LinearLayout) view.findViewById(R.id.ble_set_factory_reset_layout);

        ble_updata = (LinearLayout) view.findViewById(R.id.ble_set_updata_layout);
        app_updata_layout = (LinearLayout) view.findViewById(R.id.app_updata_layout);


        ble_Tx_hite = (SwitchButton) view.findViewById(R.id.ble_set_swit_tx_hite);

        ble_Stop_hite = (SwitchButton) view.findViewById(R.id.ble_set_swit_stop_hite);

        ble_Bw = (SwitchButton) view.findViewById(R.id.ble_set_swit_bw);

        ble_Disconnec = (SwitchButton) view.findViewById(R.id.ble_set_swit_disconnect);
        ble_Voice = (SwitchButton) view.findViewById(R.id.ble_set_swit_voice);





        /*ble_Bw = (SwitchButton) view.findViewById(R.id.ble_set_swit_bw);

        ble_Mode = (SwitchButton) view.findViewById(R.id.ble_set_swit_mode);

        ble_Bluetooth = (SlideSwitch) view.findViewById(R.id.ble_set_swit_bluetooth);

        LogUtil.i(GlobalConsts.TAG,"ble_Bw:"+ble_Bw.isChecked());*/

        tv_ble_name = (TextView) view.findViewById(R.id.sms_ble_setting_device_name);

        tv_ble_tx = (TextView) view.findViewById(R.id.sms_ble_setting_tx_code);
        tv_ble_rx = (TextView) view.findViewById(R.id.sms_ble_setting_rx_code);
        tv_ble_freq = (TextView) view.findViewById(R.id.sms_ble_setting_freq);
        tv_ble_freq_code = (TextView) view.findViewById(R.id.sms_ble_setting_freq_code);

        tv_ble_sq = (TextView) view.findViewById(R.id.sms_ble_setting_sq);
        tv_ble_vox = (TextView) view.findViewById(R.id.sms_ble_setting_vox);
        tv_ble_rf = (TextView) view.findViewById(R.id.sms_ble_setting_rf);
        tv_ble_tx_power = (TextView) view.findViewById(R.id.sms_ble_setting_tx_power);

        tv_ble_volt = (TextView) view.findViewById(R.id.sms_ble_setting_volt);



        /*radioGroup_rf = (RadioGroup) view.findViewById(R.id.radioGroup_set_rf);
        radioGroup_tx_power = (RadioGroup) view.findViewById(R.id.radioGroup_set_tx_power);

        radioButtons_rf[0] = (RadioButton) view.findViewById(R.id.rf_radioButton1);
        radioButtons_rf[1] = (RadioButton) view.findViewById(R.id.rf_radioButton2);
        radioButtons_rf[2] = (RadioButton) view.findViewById(R.id.rf_radioButton3);

        radioButtons_tx_power[0] = (RadioButton) view.findViewById(R.id.tx_radioButton1);
        radioButtons_tx_power[1] = (RadioButton) view.findViewById(R.id.tx_radioButton2);*/


        title_one_layout = (LinearLayout) view.findViewById(R.id.device_one_layout);
        title_to_layout = (LinearLayout) view.findViewById(R.id.device_to_layout);
        title_one_group = (TextView) view.findViewById(R.id.sms_main_title);//
        title_to_group = (TextView) view.findViewById(R.id.device_title_group_name2);
        title_one_freq = (TextView) view.findViewById(R.id.device_title_freq1);
        title_to_freq = (TextView) view.findViewById(R.id.device_title_freq2);


        title_mode = (TextView) view.findViewById(R.id.device_title_mode);
        title_tc = (TextView) view.findViewById(R.id.device_title_tc);
        title_rc = (TextView) view.findViewById(R.id.device_title_rc);

        powerView1 = (PowerView) view.findViewById(R.id.device_title_vole1);
        powerView2 = (PowerView) view.findViewById(R.id.device_title_vole2);

        user_name = (TextView)view.findViewById(R.id.sms_main_user_name);
        tv_sex= (TextView)view.findViewById(R.id.tv_sex);
        e_mail= (TextView)view.findViewById(R.id.tv_e_mail);
        iv_quit= (ImageView) view.findViewById(R.id.iv_quit);
        iv_name= (ImageView) view.findViewById(R.id.iv_name);
        sms_main_icon = (CircleImageView)view.findViewById(R.id.sms_main_icon);
        ll_specification= (LinearLayout) view.findViewById(R.id.ll_specification);
        ll_common_issue= (LinearLayout) view.findViewById(R.id.ll_common_issue);
        ll_Looking_equipment= (LinearLayout) view.findViewById(R.id.ll_Looking_equipment);
        iv_seek_device= (ImageView) view.findViewById(R.id.iv_seek_device);


        //tv_sex.setText(MyApplicatoin.evenUser.getSex());
       // e_mail.setText(MyApplicatoin.evenUser.getE_mail());

        AssetManager assets = getActivity().getAssets();

        Typeface fromAsset_name = Typeface.createFromAsset(assets, "fonts/Helvetica_LT_Condensed_Bold.ttf");
        Typeface fromAsset_freq = Typeface.createFromAsset(assets, "fonts/Helvetica_CE_Regular.ttf");
        Typeface fromAsset_to = Typeface.createFromAsset(assets, "fonts/Helvetica_LT_Light.ttf");


        title_one_group.setTypeface(fromAsset_name);
        title_one_freq.setTypeface(fromAsset_freq);

        title_to_group.setTypeface(fromAsset_to);
        title_to_freq.setTypeface(fromAsset_to);

        ble_Name.setOnClickListener(this);

        ble_Tx_Code.setOnClickListener(this);
        ble_Rx_Code.setOnClickListener(this);
        ble_Freq.setOnClickListener(this);
        ble_Freq_code.setOnClickListener(this);
        ll_Device_reset.setOnClickListener(this);
        ble_Sq.setOnClickListener(this);
        ble_Vox.setOnClickListener(this);
        ble_Rf.setOnClickListener(this);
        ble_Tx_power.setOnClickListener(this);

        ble_Volt.setOnClickListener(this);

        ble_factory_reset.setOnClickListener(this);
        ble_updata.setOnClickListener(this);
        sms_main_icon.setOnClickListener(this);
        iv_seek_device.setOnClickListener(this);
        app_updata_layout.setOnClickListener(this);



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


    }

    private void setLinener() {


        ble_Tx_hite.setOnClickCheckedListener(new SwitchButton.onClickCheckedListener() {
            @Override
            public void onClick() {


                if(MyApplicatoin.IS_BLE){

                //    Tools.showProgressDialog(getActivity(),"????????????...");
                    Interphone interphone = new Interphone();

                    if(ble_Tx_hite.isChecked()){

                        interphone.setTx_hite((byte)1);

                    }else{

                        interphone.setTx_hite((byte)2);

                    }
                //    set_ble(sms_request.SET,interphone);
                    MyApplicatoin.setInterphone(interphone);

                }

            }
        });

        ble_Stop_hite.setOnClickCheckedListener(new SwitchButton.onClickCheckedListener() {
            @Override
            public void onClick() {


                if(MyApplicatoin.IS_BLE){

                //    Tools.showProgressDialog(getActivity(),"????????????...");

                    Interphone interphone = new Interphone();

                    if(ble_Stop_hite.isChecked()){

                        interphone.setStop_hite((byte)1);

                    }else{

                        interphone.setStop_hite((byte)2);

                    }
                 //   set_ble(sms_request.SET,interphone);
                    MyApplicatoin.setInterphone(interphone);

                }

            }
        });


        ble_Bw.setOnClickCheckedListener(new SwitchButton.onClickCheckedListener() {
            @Override
            public void onClick() {

                LogUtil.i(GlobalConsts.TAG,"ble_bw");

                if(MyApplicatoin.IS_BLE){

                //    Tools.showProgressDialog(getActivity(),"????????????...");
                    Interphone interphone = new Interphone();

                    if(ble_Bw.isChecked()){

                        interphone.setBw((byte)1);

                    }else{

                        interphone.setBw((byte)2);

                    }

                    MyApplicatoin.setInterphone(interphone);

                }

            }
        });

        ble_Disconnec.setOnClickCheckedListener(new SwitchButton.onClickCheckedListener() {
            @Override
            public void onClick() {


                if(MyApplicatoin.IS_BLE){

                //    Tools.showProgressDialog(getActivity(),"????????????...");

                    Interphone interphone = new Interphone();

                    if(ble_Disconnec.isChecked()){

                        interphone.setDisconnec_hite((byte)1);

                    }else{

                        interphone.setDisconnec_hite((byte)2);

                    }
                    MyApplicatoin.setInterphone(interphone);

                }

            }
        });
        ble_Voice.setOnClickCheckedListener(new SwitchButton.onClickCheckedListener() {
            @Override
            public void onClick() {


                if(MyApplicatoin.IS_BLE){

                //    Tools.showProgressDialog(getActivity(),"????????????...");

                    Interphone interphone = new Interphone();

                    if(ble_Voice.isChecked()){

                        interphone.setIsplay((byte)1);

                    }else{

                        interphone.setIsplay((byte)2);

                    }
                    MyApplicatoin.setInterphone(interphone);

                }

            }
        });



        /*ble_Bluetooth.setSlideListener(new SlideSwitch.SlideListener() {
            @Override
            public void open() {
                if(MyApplicatoin.IS_BLE){

                //    Tools.showProgressDialog(getActivity(),"????????????...");

                    Interphone interphone = new Interphone();

                    interphone.setBluetooth((byte)2);

                    MyApplicatoin.setInterphone(interphone);
                }
            }

            @Override
            public void close() {

                if(MyApplicatoin.IS_BLE){


                    Interphone interphone = new Interphone();

                    interphone.setBluetooth((byte)1);

                    MyApplicatoin.setInterphone(interphone);
                }

            }
        });*/


        /*radioGroup_rf.setOnCheckedChangeListener(this);

        radioGroup_tx_power.setOnCheckedChangeListener(this);*/
        iv_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_Device.this.getActivity().finish();
            }
        });

        iv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Intent intent=new Intent(Fragment_Device.this.getActivity(), ModificationMessageActivity.class);
                 startActivity(intent);*/
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });



        ll_specification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(Fragment_Device.this.getActivity(), HiteActivity.class);
                intent2.setAction(GlobalConsts.ACTION_ABOUT);
                startActivity(intent2);
            }
        });

        ll_common_issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(Fragment_Device.this.getActivity(), HiteActivity.class);
                intent2.setAction(GlobalConsts.ACTION_FAQ);
                startActivity(intent2);
            }
        });
        ll_Looking_equipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharePreferenceUtil.saveStringDataToSharePreference(Fragment_Device.this.getActivity(), SharePreferenceConstant.BLESCAN, "blescan");
                startActivity(new Intent(Fragment_Device.this.getActivity(), BlescanActivity.class));
                Fragment_Device.this.getActivity().finish();
            }
        });

    }




    @Override
    public void onResume() {
        super.onResume();

        user_name.setText(MyApplicatoin.evenUser.getName());
        e_mail.setText(MyApplicatoin.evenUser.getE_mail());

        setTitleLayout();
        if(!MyApplicatoin.IS_BLE){


        }else{

            if(MyApplicatoin.interphone != null){

                try {

                    LogUtil.i(GlobalConsts.TAG,"????????????:"+MyApplicatoin.interphone.toString());

                    final Interphone interphone = MyApplicatoin.interphone;


                    if(interphone.getName() != null){

                        tv_ble_name.setText(interphone.getName());
                    }
                    if(interphone.getTxcode() != null){

                        if(interphone.getTxcode() > 0){
                            tv_ble_tx.setText(tx_code_data[interphone.getTxcode()-2]);
                        }else{
                            tv_ble_tx.setText(tx_code_data[interphone.getTxcode()]);
                        }

                    }
                    if(interphone.getRxcode() != null){

                        if(interphone.getRxcode() > 0){
                            tv_ble_rx.setText(tx_code_data[interphone.getRxcode()-2]);
                        }else{
                            tv_ble_rx.setText(tx_code_data[interphone.getRxcode()]);
                        }

                    }
                    if(interphone.getFrequne() != null){
                        tv_ble_freq.setText(getFreq(interphone.getFrequne()));
                        if(getFreqCode(interphone.getFrequne()) != 100){
                            tv_ble_freq_code.setText(String.valueOf(getFreqCode(interphone.getFrequne())+1));
                        }else{
                            tv_ble_freq_code.setText(getActivity().getResources().getString(R.string.other));
                        }
                    }
                    if(interphone.getSq() != null){
                        tv_ble_sq.setText(vox_data[interphone.getSq()]);
                    }
                    if(interphone.getVox() != null){
                        tv_ble_vox.setText(vox_data[interphone.getVox()]);
                    }
                    if(interphone.getVolt() != 0){
                        float a = interphone.getVolt();
                        tv_ble_volt.setText(String .format("%.1f",(a/1000)));
                    }


                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    try {
                                        if(interphone.getRf() != null){

                                         //   radioGroup_rf.setOnCheckedChangeListener(null);


                                            if(MyApplicatoin.interPhoneRF == 2){

                                            //    radioButtons_rf[2].setChecked(true);

                                                tv_ble_rf.setText(rf_data[2]);

                                            }else{

                                                tv_ble_rf.setText(rf_data[interphone.getRf() - 1]);
                                            //    radioButtons_rf[interphone.getRf() - 1].setChecked(true);

                                            }

                                        //    radioGroup_rf.setOnCheckedChangeListener(Fragment_Device.this);

                                        }
                                        if(interphone.getTx_power() != null){

                                            tv_ble_tx_power.setText(tx_power_data[interphone.getTx_power() - 1]);

                                           /* radioGroup_tx_power.setOnCheckedChangeListener(null);

                                            radioButtons_tx_power[interphone.getTx_power() - 1].setChecked(true);

                                            radioGroup_tx_power.setOnCheckedChangeListener(Fragment_Device.this);*/

                                        }

                                        if(interphone.getStop_hite() != null){

                                            if (interphone.getStop_hite() == 1) {

                                                ble_Stop_hite.setChecked(switchInt, true);

                                            } else {

                                                ble_Stop_hite.setChecked(switchInt, false);
                                            }

                                        }
                                        if(interphone.getTx_hite() != null){

                                            if (interphone.getTx_hite() == 1) {

                                                ble_Tx_hite.setChecked(switchInt, true);

                                            } else {

                                                ble_Tx_hite.setChecked(switchInt, false);
                                            }

                                        }
                                        if(interphone.getBw() != null){

                                            if (interphone.getBw() == 1) {

                                                ble_Bw.setChecked(switchInt, true);

                                            } else {

                                                ble_Bw.setChecked(switchInt, false);
                                            }

                                        }
                                        if(interphone.getDisconnec_hite() != null){

                                            if (interphone.getDisconnec_hite() == 1) {

                                                ble_Disconnec.setChecked(switchInt, true);

                                            } else {

                                                ble_Disconnec.setChecked(switchInt, false);
                                            }

                                        }
                                        if(interphone.getIsplay() != null){

                                            if (interphone.getIsplay() == 1) {

                                                ble_Voice.setChecked(switchInt, true);

                                            } else {

                                                ble_Voice.setChecked(switchInt, false);
                                            }

                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    /*if(interphone.getBw() != null){

                    if (interphone.getBw() == 1) {

                        if(!ble_Bw.isChecked()){
                            ble_Bw.setChecked(switchInt, true);
                        }

                    } else {

                        if(ble_Bw.isChecked()){
                            ble_Bw.setChecked(switchInt, false);
                        }
                    }

                }
                if(interphone.getBluetooth() != null){

                    if(interphone.getBluetooth() == 2){
                        ble_Bluetooth.setState(true);
                    }else{
                        ble_Bluetooth.setState(false);
                    }

                }
                if(interphone.getMode() != null){

                    if (interphone.getMode() == 1) {

                        if(!ble_Mode.isChecked()){
                            ble_Mode.setChecked(switchInt, true);
                        }

                    } else {

                        if(ble_Mode.isChecked()){
                            ble_Mode.setChecked(switchInt, false);
                        }
                    }

                }*/
                                }
                            });

                        }
                    }).start();



                } catch (Exception e) {
                    ExceptionUtil.handleException(e);
                }
            }

        }

        Interphone interphone = new Interphone();

        set_ble(sms_request.SET,interphone);


    }



    private void setTitleLayout() {

        try {
            if (MyApplicatoin.IS_BLE) {

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

                }

                if(getInterphone()){


                    title_one_group.setText(MyApplicatoin.interphone.getImCode().getHite());
                    title_to_group.setText(MyApplicatoin.interphone.getImCode().getHite());

                }else{

                    title_one_group.setText(getResources().getString(R.string.sms_deivce_title_group_hite));
                    title_to_group.setText(getResources().getString(R.string.sms_deivce_title_group_hite));

                }

            }else{

                title_one_layout.setVisibility(View.GONE);
                title_to_layout.setVisibility(View.GONE);

            }
        } catch (Resources.NotFoundException e) {
            ExceptionUtil.handleException(e);
        }

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    //    long currentTime = Calendar.getInstance().getTimeInMillis();

    //    LogUtil.i(TAG,"onCheckedChanged:"+String.valueOf(currentTime - lastHiteTime));

        /*switch (group.getId()) {


            case R.id.radioGroup_set_rf:

                try {

                    if(isInterphone()){



                        for(int i = 0;i < radioButtons_rf.length;i++){

                            if(checkedId == radioButtons_rf[i].getId()){

                                //???????????????????????????
                                if(i == 2){

                                    MyApplicatoin.interPhoneRF = 2;

                                    MyApplicatoin.setRF();

                                    *//*if(MyApplicatoin.interphone.getRf() == 1){

                                        Interphone interphone = new Interphone();

                                        interphone.setRf((byte)2);

                                        //    set_ble(sms_request.SET,interphone);

                                        MyApplicatoin.setInterphone(interphone);



                                    }*//*


                                    byte[] data = null;
                                    String fromName = null;


                                    if(getInterphone()){


                                        fromName = Tools.getGroupName(MyApplicatoin.interphone.getImCode().getName());

                                        data = Tools.getDataCode(MyApplicatoin.interphone.getImCode().getName());


                                        if(data != null){

                                            int txCode = data[0] & 0xFF;
                                            int rxCode = data[1] & 0xFF;

                                            int freq = (data[2] & 0xFF
                                                    | (data[3] & 0xFF) << 8
                                                    | (data[4] & 0xFF) << 16
                                                    | (data[5] & 0xFF) << 24);


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

                                            LogUtil.i(GlobalConsts.TAG,"txCode:"+txCode+",rxCode:"+rxCode+",freq:"+freq+",fromName:"+fromName+",data:"+Arrays.toString(data));

                                        }
                                    }

                                }else{

                                    MyApplicatoin.interPhoneRF = 1;

                                    Interphone interphone = new Interphone();

                                    interphone.setRf((byte)(i+1));

                                    //    set_ble(sms_request.SET,interphone);

                                    MyApplicatoin.setInterphone(interphone);

                                }

                                LogUtil.i(TAG,"??????RF????????????:"+MyApplicatoin.interPhoneRF);

                            }

                        }



                    }

                } catch (Exception e) {
                    ExceptionUtil.handleException(e);
                }


                break;

            case R.id.radioGroup_set_tx_power:


                try {
                    if(isInterphone()){

                        for(int i = 0;i < radioButtons_tx_power.length;i++){

                            if(checkedId == radioButtons_tx_power[i].getId()){

                                Interphone interphone = new Interphone();

                                interphone.setTx_power((byte)(i+1));

                                //    set_ble(sms_request.SET,interphone);

                                MyApplicatoin.setInterphone(interphone);
                            }

                        }

                    }

                } catch (Exception e) {
                    ExceptionUtil.handleException(e);
                }

                break;

            default:
                break;

        }*/

        /*if(currentTime - lastHiteTime < 111500){

            lastHiteTime = currentTime;

        }else{

            Tools.showProgressDialog(getActivity(),"????????????");

            lastHiteTime = currentTime;



        }*/


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.ble_set_name_layout:

                if(isInterphone()){

                    final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                    View view = View.inflate(getActivity(), R.layout.dialog_edtext_layout, null);
                    dialog.setView(view);
                    dialog.setInverseBackgroundForced(true);
                    dialog.setCancelable(false);


                    final Button cancel = (Button) view.findViewById(R.id.button_cancel);
                    final Button ok = (Button) view.findViewById(R.id.button_ok);
                    final ImageView icon = (ImageView) view.findViewById(R.id.dialog_icon);
                    final TextView title = (TextView) view.findViewById(R.id.title);
                    final EditText editText = (EditText) view.findViewById(R.id.dialog_edtext);

                    title.setText(getResources().getString(R.string.sms_ble_setting_hite_name_title));
                    editText.setHint(getResources().getString(R.string.sms_ble_setting_hite_name_content));

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

                            String  ble_name= editText.getText().toString();

                            // ?????????????????????????????????
                            StringBuilder builder = new StringBuilder();

                            if (TextUtils.isEmpty(ble_name)) {
                                builder.append(getResources().getString(R.string.sms_ble_setting_hite_edtext));
                            }

                            if (TextUtils.isEmpty(builder.toString())) {

                                Tools.showProgressDialog(getActivity(),getResources().getString(R.string.sms_ble_setting_hite_dialog));

                                Interphone interphone = new Interphone();

                                interphone.setName(ble_name);


                                MyApplicatoin.setInterphone(interphone);

                            } else {
                                Toast.makeText(getActivity(), builder.toString(),
                                        Toast.LENGTH_LONG).show();
                            }

                            dialog.cancel();

                        }
                    });
                    dialog.show();
                }

                break;

            case R.id.ble_set_freq_code_layout:


                if(isInterphone()){


                    if (MyApplicatoin.interphone.getRf() == null){

                        return;
                    }


                    if(MyApplicatoin.interPhoneRF == 2){

                        Tools.showInfo(getActivity(),getResources().getString(R.string.sms_ble_setting_hite_toast));

                        return;
                    }



                    final AlertDialog dialog_tx = new AlertDialog.Builder(getActivity()).create();
                    View view_tx = View.inflate(getActivity(), R.layout.dialog_ble_set_ctcss, null);
                    dialog_tx.setView(view_tx);
                    dialog_tx.setInverseBackgroundForced(true);
                    dialog_tx.setCancelable(false);

                    final GridView gv_tx = (GridView) view_tx.findViewById(R.id.ble_set_gridview);

                    gv_tx.setNumColumns(2);

                    final Button bt_tx = (Button) view_tx.findViewById(R.id.button);
                    ArrayAdapter<String> adapter_tx = new ArrayAdapter<String>(getActivity(),R.layout.grid_item_ble_set_ctcss,R.id.textView,freq_code_data);
                    gv_tx.setAdapter(adapter_tx);


                    gv_tx.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Tools.showProgressDialog(getActivity(),getResources().getString(R.string.sms_ble_setting_hite_dialog));


                            Interphone interphone = new Interphone();

                            interphone.setFrequne(c[position]);

                            MyApplicatoin.setInterphone(interphone);

                            tv_ble_freq_code.setText(String.valueOf(position+1));

                            LogUtil.i(GlobalConsts.TAG,"AlertDialog:"+position);
                            dialog_tx.cancel();
                        }
                    });

                    bt_tx.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog_tx.cancel();
                        }
                    });

                    dialog_tx.show();

                }


                //  startActivity(new Intent(getActivity(), Ble_set_ctcss_Activity.class));

                break;
            case R.id.ll_Device_reset://??????
                Interphone interphone = new Interphone();
                interphone.setDevice_reset("LJ1d8m3ioGW45d8o");
                MyApplicatoin.setInterphone(interphone);
                break;
            case R.id.ble_set_tx_layout:


                if(isInterphone()){


                    if (MyApplicatoin.interphone.getRf() == null){

                        return;
                    }


                    if(MyApplicatoin.interPhoneRF == 2){

                        Tools.showInfo(getActivity(),getResources().getString(R.string.sms_ble_setting_hite_toast));

                        return;
                    }


                    final AlertDialog dialog_tx = new AlertDialog.Builder(getActivity()).create();
                    View view_tx = View.inflate(getActivity(), R.layout.dialog_ble_set_ctcss, null);
                    dialog_tx.setView(view_tx);
                    dialog_tx.setInverseBackgroundForced(true);
                    dialog_tx.setCancelable(false);

                    final GridView gv_tx = (GridView) view_tx.findViewById(R.id.ble_set_gridview);
                    gv_tx.setNumColumns(3);
                    final Button bt_tx = (Button) view_tx.findViewById(R.id.button);
                    ArrayAdapter<String> adapter_tx = new ArrayAdapter<String>(getActivity(),R.layout.grid_item_ble_set_ctcss,R.id.textView,tx_code_data);
                    gv_tx.setAdapter(adapter_tx);


                    gv_tx.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Tools.showProgressDialog(getActivity(),getResources().getString(R.string.sms_ble_setting_hite_dialog));


                            Interphone interphone = new Interphone();

                            if(position > 0){
                                interphone.setTxcode(position+2);
                            }else{
                                interphone.setTxcode(position);
                            }


                            MyApplicatoin.setInterphone(interphone);

                            LogUtil.i(GlobalConsts.TAG,"AlertDialog:"+position);
                            dialog_tx.cancel();
                        }
                    });

                    bt_tx.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog_tx.cancel();
                        }
                    });

                    dialog_tx.show();

                }


            //  startActivity(new Intent(getActivity(), Ble_set_ctcss_Activity.class));

                break;

            case R.id.ble_set_rx_layout:

                if(isInterphone()){

                    if (MyApplicatoin.interphone.getRf() == null){

                        return;
                    }

                    if(MyApplicatoin.interPhoneRF == 2){

                        Tools.showInfo(getActivity(),getResources().getString(R.string.sms_ble_setting_hite_toast));

                        return;
                    }


                    final AlertDialog dialog_rx = new AlertDialog.Builder(getActivity()).create();
                    View view_rx = View.inflate(getActivity(), R.layout.dialog_ble_set_ctcss, null);
                    dialog_rx.setView(view_rx);
                    dialog_rx.setInverseBackgroundForced(true);
                    dialog_rx.setCancelable(false);

                    final GridView gv_rx = (GridView) view_rx.findViewById(R.id.ble_set_gridview);
                    gv_rx.setNumColumns(3);
                    final Button bt_rx = (Button) view_rx.findViewById(R.id.button);
                    ArrayAdapter<String> adapter_rx = new ArrayAdapter<String>(getActivity(),R.layout.grid_item_ble_set_ctcss,R.id.textView,tx_code_data);
                    gv_rx.setAdapter(adapter_rx);

                    gv_rx.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Tools.showProgressDialog(getActivity(),getResources().getString(R.string.sms_ble_setting_hite_dialog));

                            Interphone interphone = new Interphone();

                            if(position > 0){
                                interphone.setRxcode(position+2);
                            }else{
                                interphone.setRxcode(position);
                            }


                            MyApplicatoin.setInterphone(interphone);

                            LogUtil.i(GlobalConsts.TAG,"AlertDialog:"+position);
                            dialog_rx.cancel();
                        }
                    });

                    bt_rx.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog_rx.cancel();
                        }
                    });

                    dialog_rx.show();

                    break;

                }

            case R.id.ble_set_freq_layout:

                if(isInterphone()){

                    if (MyApplicatoin.interphone.getRf() == null){

                        return;
                    }

                    if(MyApplicatoin.interPhoneRF == 2){

                        Tools.showInfo(getActivity(),getResources().getString(R.string.sms_ble_setting_hite_toast));

                        return;
                    }




                    final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                    View view = View.inflate(getActivity(), R.layout.dialog_warning_layout, null);
                    dialog.setView(view);
                    dialog.setInverseBackgroundForced(true);
                    dialog.setCancelable(false);
                    final Button cancel = (Button) view.findViewById(R.id.button_cancel);
                    final Button ok = (Button) view.findViewById(R.id.button_ok);
                    final TextView title = (TextView) view.findViewById(R.id.title);
                    final TextView content = (TextView) view.findViewById(R.id.content);

                    title.setText(getResources().getString(R.string.sms_freq_delete_title));
                    content.setText(getResources().getString(R.string.sms_freq_delete_content));

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


                            final AlertDialog dialog_freq = new AlertDialog.Builder(getActivity()).create();
                            View view_freq = View.inflate(getActivity(), R.layout.dialog_ble_set_freq, null);
                            dialog_freq.setView(view_freq);
                            dialog_freq.setInverseBackgroundForced(true);


                            //dialog????????????

                            Window window = dialog_freq.getWindow();
                            window.setBackgroundDrawableResource(android.R.color.transparent);

                            freq.clear();

                            tvArray[0] = (TextView) view_freq.findViewById(R.id.button0);
                            tvArray[1] = (TextView) view_freq.findViewById(R.id.button1);
                            tvArray[2] = (TextView) view_freq.findViewById(R.id.button2);
                            tvArray[3] = (TextView) view_freq.findViewById(R.id.button3);
                            tvArray[4] = (TextView) view_freq.findViewById(R.id.button4);
                            tvArray[5] = (TextView) view_freq.findViewById(R.id.button5);
                            tvArray[6] = (TextView) view_freq.findViewById(R.id.button6);
                            tvArray[7] = (TextView) view_freq.findViewById(R.id.button7);
                            tvArray[8] = (TextView) view_freq.findViewById(R.id.button8);
                            tvArray[9] = (TextView) view_freq.findViewById(R.id.button9);
                            tvArray[10] = (TextView) view_freq.findViewById(R.id.button_delete);
                            tvArray[11] = (TextView) view_freq.findViewById(R.id.button_ok);


                            tvhiteArray[0] = (TextView) view_freq.findViewById(R.id.textview1);
                            tvhiteArray[1] = (TextView) view_freq.findViewById(R.id.textview2);
                            tvhiteArray[2] = (TextView) view_freq.findViewById(R.id.textview3);
                            tvhiteArray[3] = (TextView) view_freq.findViewById(R.id.textview4);
                            tvhiteArray[4] = (TextView) view_freq.findViewById(R.id.textview5);
                            tvhiteArray[5] = (TextView) view_freq.findViewById(R.id.textview6);
                            tvhiteArray[6] = (TextView) view_freq.findViewById(R.id.textview7);


                            setListener(dialog_freq);

                            dialog_freq.show();

                            dialog.cancel();
                        }
                    });
                    dialog.show();



                }




                break;

            case R.id.ble_set_sq_layout:

                if(isInterphone()){

                    final AlertDialog dialog_sq = new AlertDialog.Builder(getActivity()).create();
                    View view_sq = View.inflate(getActivity(), R.layout.dialog_ble_set_vox, null);
                    dialog_sq.setView(view_sq);
                    dialog_sq.setInverseBackgroundForced(true);
                    dialog_sq.setCancelable(false);

                    final ListView gv_sq = (ListView) view_sq.findViewById(R.id.ble_set_gridview);
                    final Button bt_sq = (Button) view_sq.findViewById(R.id.button);
                    ArrayAdapter<String> adapter_sq = new ArrayAdapter<String>(getActivity(),R.layout.grid_item_ble_set_ctcss,R.id.textView,vox_data);
                    gv_sq.setAdapter(adapter_sq);


                    gv_sq.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Tools.showProgressDialog(getActivity(),getResources().getString(R.string.sms_ble_setting_hite_dialog));

                            Interphone interphone = new Interphone();

                            interphone.setSq((byte)(position));
                            MyApplicatoin.setInterphone(interphone);
                            LogUtil.i(GlobalConsts.TAG,"AlertDialog:"+(position+1));
                            dialog_sq.cancel();
                        }
                    });

                    bt_sq.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog_sq.cancel();
                        }
                    });

                    dialog_sq.show();

                }

                break;
            case R.id.ble_set_vox_layout:

                if(isInterphone()){

                    final AlertDialog dialog_vox = new AlertDialog.Builder(getActivity()).create();
                    View view_vox = View.inflate(getActivity(), R.layout.dialog_ble_set_vox, null);

                    dialog_vox.setView(view_vox);
                    dialog_vox.setInverseBackgroundForced(true);
                    dialog_vox.setCancelable(false);

                    final ListView gv_vox = (ListView) view_vox.findViewById(R.id.ble_set_gridview);
                    final Button bt_vox = (Button) view_vox.findViewById(R.id.button);
                    ArrayAdapter<String> adapter_vox = new ArrayAdapter<String>(getActivity(),R.layout.grid_item_ble_set_ctcss,R.id.textView,vox_data);
                    gv_vox.setAdapter(adapter_vox);

                    gv_vox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Tools.showProgressDialog(getActivity(),getResources().getString(R.string.sms_ble_setting_hite_dialog));

                            Interphone interphone = new Interphone();

                            interphone.setVox((byte)position);
                            MyApplicatoin.setInterphone(interphone);
                            LogUtil.i(GlobalConsts.TAG,"AlertDialog:"+(position+1));
                            dialog_vox.cancel();

                        }
                    });

                    bt_vox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog_vox.cancel();
                        }
                    });

                    dialog_vox.show();

                }


                break;
            case R.id.ble_set_rf_layout:

                if(isInterphone()){

                    final AlertDialog dialog_vox = new AlertDialog.Builder(getActivity()).create();
                    View view_vox = View.inflate(getActivity(), R.layout.dialog_ble_set_vox, null);

                    dialog_vox.setView(view_vox);
                    dialog_vox.setInverseBackgroundForced(true);
                    dialog_vox.setCancelable(false);

                    final ListView gv_vox = (ListView) view_vox.findViewById(R.id.ble_set_gridview);
                    final Button bt_vox = (Button) view_vox.findViewById(R.id.button);
                    ArrayAdapter<String> adapter_vox = new ArrayAdapter<String>(getActivity(),R.layout.grid_item_ble_set_ctcss,R.id.textView,rf_data);
                    gv_vox.setAdapter(adapter_vox);

                    gv_vox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Tools.showProgressDialog(getActivity(),getResources().getString(R.string.sms_ble_setting_hite_dialog));
                            if(position == 2){//????????????

                                MyApplicatoin.interPhoneRF = 2;

                                MyApplicatoin.setRF();

                                if(MyApplicatoin.interphone.getRf() == 1) {

                                    Interphone interphone = new Interphone();

                                    interphone.setRf((byte) 2);

                                    MyApplicatoin.setInterphone(interphone);

                                }

                                byte[] data = null;
                                String fromName = null;


                                if(getInterphone()){


                                    fromName = Tools.getGroupName(MyApplicatoin.interphone.getImCode().getName());

                                    data = Tools.getDataCode(MyApplicatoin.interphone.getImCode().getName());


                                    if(data != null){

                                        int txCode = data[0] & 0xFF;
                                        int rxCode = data[1] & 0xFF;

                                        int freq = (data[2] & 0xFF
                                                | (data[3] & 0xFF) << 8
                                                | (data[4] & 0xFF) << 16
                                                | (data[5] & 0xFF) << 24);


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

                                        LogUtil.i(GlobalConsts.TAG,"txCode:"+txCode+",rxCode:"+rxCode+",freq:"+freq+",fromName:"+fromName+",data:"+Arrays.toString(data));

                                    }
                                }


                            }else{

                                MyApplicatoin.interPhoneRF = 1;

                                //if(MyApplicatoin.interphone.getRf() != position+1)
                                {

                                    Interphone interphone = new Interphone();

                                    interphone.setRf((byte)(position+1));

                                    MyApplicatoin.setInterphone(interphone);

                                }


                            }


                            LogUtil.i(TAG,"??????RF????????????:"+MyApplicatoin.interPhoneRF);
                            dialog_vox.cancel();

                        }
                    });

                    bt_vox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog_vox.cancel();
                        }
                    });

                    dialog_vox.show();

                }


                break;
            case R.id.radioGroup_set_tx_power:

                if(isInterphone()){

                    final AlertDialog dialog_vox = new AlertDialog.Builder(getActivity()).create();
                    View view_vox = View.inflate(getActivity(), R.layout.dialog_ble_set_vox, null);

                    dialog_vox.setView(view_vox);
                    dialog_vox.setInverseBackgroundForced(true);
                    dialog_vox.setCancelable(false);

                    final ListView gv_vox = (ListView) view_vox.findViewById(R.id.ble_set_gridview);
                    final Button bt_vox = (Button) view_vox.findViewById(R.id.button);
                    ArrayAdapter<String> adapter_vox = new ArrayAdapter<String>(getActivity(),R.layout.grid_item_ble_set_ctcss,R.id.textView,tx_power_data);
                    gv_vox.setAdapter(adapter_vox);

                    gv_vox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Tools.showProgressDialog(getActivity(),getResources().getString(R.string.sms_ble_setting_hite_dialog));

                            if (MyApplicatoin.interphone.getTx_power() != position+1) {

                                Interphone interphone = new Interphone();

                                interphone.setTx_power((byte)(position+1));

                                MyApplicatoin.setInterphone(interphone);
                            }

                            LogUtil.i(GlobalConsts.TAG,"AlertDialog:"+(position+1));
                            dialog_vox.cancel();

                        }
                    });

                    bt_vox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog_vox.cancel();
                        }
                    });

                    dialog_vox.show();

                }


                break;
            case R.id.ble_set_factory_reset_layout:

                if(isInterphone()){


                    final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                    View view = View.inflate(getActivity(), R.layout.dialog_warning_layout, null);
                    dialog.setView(view);
                    dialog.setInverseBackgroundForced(true);
                    final Button cancel = (Button) view.findViewById(R.id.button_cancel);
                    final Button ok = (Button) view.findViewById(R.id.button_ok);
                    final TextView title = (TextView) view.findViewById(R.id.title);
                    final TextView content = (TextView) view.findViewById(R.id.content);

                    title.setText(getResources().getString(R.string.sms_dialog_factory_reset_title));
                    content.setText(getResources().getString(R.string.sms_dialog_factory_reset_content));

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

                            Interphone interphone = new Interphone();
                            interphone.setFactory_reset((byte) 2);

                            MyApplicatoin.setInterphone(interphone);


                            dialog.cancel();



                        }
                    });
                    dialog.show();

                    /*final AlertDialog dialog_updata = new AlertDialog.Builder(getActivity()).create();
                    View view_dialog_updata = View.inflate(getActivity(), R.layout.dialog_ble_updata_layout, null);

                    dialog_updata.setView(view_dialog_updata);
                    dialog_updata.setInverseBackgroundForced(true);
                    dialog_updata.setCancelable(false);


                    //dialog????????????

                    Window window_updata = dialog_updata.getWindow();
                    window_updata.setBackgroundDrawableResource(android.R.color.transparent);


                    final Button bt_ok = (Button) view_dialog_updata.findViewById(R.id.button_ok);
                    final Button bt_cancel = (Button) view_dialog_updata.findViewById(R.id.button_cancel);

                    final TextView tv_hard_version = (TextView) view_dialog_updata.findViewById(R.id.textView4);
                    final TextView tv_soft_version = (TextView) view_dialog_updata.findViewById(R.id.textView5);
                    final TextView tv_version = (TextView) view_dialog_updata.findViewById(R.id.textView6);


                    String firePath = getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath()+"/data/sms_file/OTA.bin";

                    final File file = new File(firePath);

                    if(MyApplicatoin.IS_BLE){

                        tv_hard_version.setText(getResources().getString(R.string.sms_ble_setting_hite_updata_Firmware_hard)+MyApplicatoin.interphone.getHard_version());

                        tv_soft_version.setText(getResources().getString(R.string.sms_ble_setting_hite_updata_Firmware_soft)+MyApplicatoin.interphone.getSoft_version());


                        if (file == null || !file.exists())
                        {
                            tv_version.setText(getResources().getString(R.string.sms_ble_setting_hite_updata_Firmware));

                        }else{

                            tv_version.setText("?????????????????????"+ file.getName());

                        }

                    }

                    bt_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            *//*if (file == null || !file.exists())
                            {
                                tv_version.setText(getResources().getString(R.string.sms_ble_setting_hite_updata_Firmware));

                            }else{

                                startActivity(new Intent(getActivity(), UpDataActivity.class));

                            }*//*


                            Interphone interphone = new Interphone();
                            interphone.setFactory_reset((byte) 2);

                            MyApplicatoin.setInterphone(interphone);


                        }
                    });



                    bt_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog_updata.cancel();
                        }
                    });



                    dialog_updata.show();*/
                }

                break;

            case R.id.app_updata_layout:
                try {

                    SharedPreferences sharedPreferences = Fragment_Device.this.getActivity().getSharedPreferences(GlobalConsts.PRECURSOR, Context.MODE_PRIVATE);

                    if(android_version != null && android_url!= null && android_description != null){

                        final String versionCode = getVersionName(Fragment_Device.this.getActivity());
                        if(!versionCode.equals(android_version)){

                            if(!MyApplicatoin.isUpdata){
                                showLoginDialog(android_description);
                            }else{
                                Tools.showInfo(Fragment_Device.this.getActivity(),getResources().getString(R.string.isupdata));
                            }


                        }else{
                            Tools.showInfo(Fragment_Device.this.getActivity(),getResources().getString(R.string.sms_main_toast_hite_updata));
                        }

                    }else{
                        Tools.showInfo(Fragment_Device.this.getActivity(),getResources().getString(R.string.sms_main_toast_hite_updata));

                    }
                } catch (Resources.NotFoundException e) {
                    ExceptionUtil.handleException(e);
                }
                break;
            case R.id.ble_set_updata_layout:
               // LogUtil.i("aaaaaaaaaaaa","android_version:"+android_version+",android_url:"+android_url+"              ??????android_description:"+android_description);


                try {
                    if(isInterphone()){

                        final AlertDialog dialog_updata = new AlertDialog.Builder(getActivity()).create();
                        View view_dialog_updata = View.inflate(getActivity(), R.layout.dialog_ble_updata_layout, null);

                        dialog_updata.setView(view_dialog_updata);
                        dialog_updata.setInverseBackgroundForced(true);
                        dialog_updata.setCancelable(false);


                        //dialog????????????

                        Window window_updata = dialog_updata.getWindow();
                        window_updata.setBackgroundDrawableResource(android.R.color.transparent);


                        final Button bt_ok = (Button) view_dialog_updata.findViewById(R.id.button_ok);
                        final Button bt_cancel = (Button) view_dialog_updata.findViewById(R.id.button_cancel);

                        final TextView tv_hard_version = (TextView) view_dialog_updata.findViewById(R.id.textView4);
                        final TextView tv_soft_version = (TextView) view_dialog_updata.findViewById(R.id.textView5);
                        final TextView tv_version = (TextView) view_dialog_updata.findViewById(R.id.textView6);


                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(GlobalConsts.PRECURSOR, Context.MODE_PRIVATE);

                    //    String firePath = getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath()+"/data/sms_firmware/intercom_app.zip";

                        String firePath  = sharedPreferences.getString(GlobalConsts.PRECURSOR_FIRMWARE_URL,null);
                        String fireVersion  = sharedPreferences.getString(GlobalConsts.PRECURSOR_FIRMWARE_VERSION,null);

                        final File file = new File(firePath);

                        if(MyApplicatoin.IS_BLE){

                            tv_hard_version.setText(getResources().getString(R.string.sms_ble_setting_hite_updata_Firmware_hard)+MyApplicatoin.interphone.getHard_version());

                            tv_soft_version.setText(getResources().getString(R.string.sms_ble_setting_hite_updata_Firmware_soft)+MyApplicatoin.interphone.getSoft_version());


                            if (file == null || !file.exists())
                            {
                                tv_version.setText(getResources().getString(R.string.sms_ble_setting_hite_updata_Firmware));

                            }else{

                                tv_version.setText("?????????????????????"+ fireVersion);

                            }

                        }
                       // Log.d("resultString","???????????????"+fireVersion+"     "+MyApplicatoin.interphone.getSoft_version().toString());
                        if(fireVersion.equals(MyApplicatoin.interphone.getSoft_version().toString())){//???????????????????????????
                            Tools.showInfo(getActivity(),getResources().getString(R.string.sms_main_toast_hite_updata));
                            return;
                        }
                        bt_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                if (file == null || !file.exists())
                                {
                                    tv_version.setText(getResources().getString(R.string.sms_ble_setting_hite_updata_Firmware));

                                }else{
                                    startActivity(new Intent(getActivity(), UpDataActivity.class));

                                }



                                dialog_updata.cancel();

                            }
                        });



                        bt_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog_updata.cancel();
                            }
                        });



                        dialog_updata.show();
                    }
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.sms_main_icon:
                SharePreferenceUtil.saveStringDataToSharePreference(getActivity(), SharePreferenceConstant.LOGIN, "");
              /*  startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();*/
                Intent intent=new Intent(Fragment_Device.this.getActivity(), ModificationMessageActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_seek_device:
                Interphone mInterphone = new Interphone();
                mInterphone.setFindDevice((byte)0x02);
                MyApplicatoin.setInterphone(mInterphone);
                break;
            default:
                break;
        }

    }

    private void set_ble(int objcode, final Interphone phone) {

        LogUtil.i(TAG,"Interphone:"+phone.toString());

        if (isInterphone()) {
            sms_fw_api.do_request(getActivity(), objcode, phone, new RequestCB() {
                @Override
                public void respone(int opcode, Object obj) {


                }
            });

            sms_fw_api.add_attr_listenner(getActivity(), new attr_listenner() {
                @Override
                public void onAttributeChang(int code, Object object) {

                  LogUtil.i(GlobalConsts.TAG,"code:"+code);

                    if(code == Interphone_notify_listenner.ATTR){

                        final Interphone interphone = (Interphone) object;
                        if(interphone.getState() == BluetoothProfile.STATE_CONNECTED){
                            LogUtil.i(GlobalConsts.TAG,"Fragment_????????????");
                            GlobalConsts.connect(true,interphone);

                        }else{
                            LogUtil.i(GlobalConsts.TAG,"Fragment_????????????");
                            GlobalConsts.connect(false,interphone);

                        //    MyApplicatoin.interPhoneRF = 1;
                            if(getActivity() != null){

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        try {

                                            setTitleLayout();

                                            tv_ble_name.setText(getResources().getString(R.string.device));
                                            tv_ble_tx.setText(getResources().getString(R.string.device));
                                            tv_ble_rx.setText(getResources().getString(R.string.device));
                                            tv_ble_freq.setText(getResources().getString(R.string.device));


                                            tv_ble_freq_code.setText(getResources().getString(R.string.other));

                                            tv_ble_sq.setText(getResources().getString(R.string.device));
                                            tv_ble_vox.setText(getResources().getString(R.string.device));
                                            tv_ble_volt.setText(getResources().getString(R.string.device));

                                            ble_Tx_hite.setChecked(switchInt, false);
                                            ble_Stop_hite.setChecked(switchInt, false);
                                            ble_Bw.setChecked(switchInt, false);
                                            ble_Disconnec.setChecked(switchInt, false);
                                            ble_Voice.setChecked(switchInt, false);




                                        /*ble_Bw.setChecked(switchInt, false);
                                        ble_Bluetooth.setState(false);
                                        ble_Mode.setChecked(switchInt, false);*/
                                        } catch (Resources.NotFoundException e) {
                                            ExceptionUtil.handleException(e);
                                        }
                                    }
                                });

                            }


                        }
                    }else {
                        if (code == Interphone_notify_listenner.RESULT) {

                            LogUtil.i(TAG,"Opcode.RESULT");

                            /*final Result result = (Result) object;

                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Interphone phone = MyApplicatoin.interphone;

                                //    LogUtil.i(TAG,"Opcode.RESULT:"+ phone.toString());

                                    byte[] data = result.getObjcode();

                                    byte code = result.getCode();

                                    try {
                                        if (result.getResult() == GlobalConsts.YES) {


                                            if (phone.getName() != null) {

                                                tv_ble_name.setText(phone.getName());

                                            }
                                            if(phone.getTxcode() != null){

                                                tv_ble_tx.setText(code_data[phone.getTxcode()]);

                                            }
                                            if(phone.getRxcode() != null){

                                                tv_ble_rx.setText(code_data[phone.getRxcode()]);

                                            }
                                            if(phone.getFrequne() != null){

                                                tv_ble_freq.setText(getFreq(phone.getFrequne()));

                                            }
                                            if(phone.getSq() != null){

                                                tv_ble_sq.setText(vox_data[phone.getSq()]);

                                            }
                                            if(phone.getVox() != null){

                                                tv_ble_vox.setText(vox_data[phone.getVox()]);

                                            }

                                            if(phone.getBluetooth() != null){


                                                if(phone.getBluetooth() == 1){

                                                    ble_Bluetooth.setState(true);
                                                }else{
                                                    ble_Bluetooth.setState(false);
                                                }

                                            }


                                        } else if (result.getResult() == GlobalConsts.NO) {

                                            LogUtil.i(GlobalConsts.TAG,"????????????");

                                            if(phone.getBw() != null){

                                                ble_Bw.setChecked(switchInt, !ble_Bw.isChecked());


                                            }else if(phone.getMode() != null){

                                                ble_Mode.setChecked(switchInt, !ble_Mode.isChecked());


                                            }else if(phone.getBluetooth() != null){

                                                if(phone.getBluetooth() == 1){

                                                    ble_Bluetooth.setState(true);
                                                }else{
                                                    ble_Bluetooth.setState(false);
                                                }

                                            }
                                        }
                                    } catch (Exception e) {
                                        ExceptionUtil.handleException(e);
                                    }

                                    //??????ble????????????????????????
                                    if(code == Objcode.FRIDY){


                                    }else{
                                        LogUtil.i(GlobalConsts.TAG, "fragment_?????????????????????");
                                    }

                                }
                            });*/

                        } else if (code == Interphone_notify_listenner.MESG) {

                            LogUtil.i(GlobalConsts.TAG, "fragment_??????BLE??????");


                        } else if (code == Interphone_notify_listenner.NOYIFY) {

                            Tools.closeProgressDialog();

                            LogUtil.i(GlobalConsts.TAG, "fragment_??????BLE_NOYIFY??????");

                        //    LogUtil.i(GlobalConsts.TAG,"MyApplicatoin.interphone:"+MyApplicatoin.interphone);

                            if (MyApplicatoin.interphone != null) {

                                final Interphone interphone = MyApplicatoin.interphone;

                                //????????????

                                if(getActivity() != null){


                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            try {

                                                if(interphone.getName() != null){


                                                    tv_ble_name.setText(interphone.getName());

                                                }
                                                if(interphone.getTxcode() != null){

                                                    if(interphone.getTxcode() > 0){
                                                        tv_ble_tx.setText(tx_code_data[interphone.getTxcode()-2]);
                                                    }else{
                                                        tv_ble_tx.setText(tx_code_data[interphone.getTxcode()]);
                                                    }

                                                    setTitleLayout();

                                                }
                                                if(interphone.getRxcode() != null){

                                                    if(interphone.getRxcode() > 0){
                                                        tv_ble_rx.setText(tx_code_data[interphone.getRxcode()-2]);
                                                    }else{
                                                        tv_ble_rx.setText(tx_code_data[interphone.getRxcode()]);
                                                    }

                                                    setTitleLayout();
                                                }
                                                if(interphone.getFrequne() != null){

                                                    tv_ble_freq.setText(getFreq(interphone.getFrequne()));
                                                    if(getFreqCode(interphone.getFrequne()) != 100){
                                                        tv_ble_freq_code.setText(String.valueOf(getFreqCode(interphone.getFrequne())+1));
                                                    }else{
                                                        tv_ble_freq_code.setText(getActivity().getResources().getString(R.string.other));
                                                    }

                                                    setTitleLayout();

                                                    Tools.closeProgressDialog();
                                                }
                                                if(interphone.getSq() != null){

                                                    tv_ble_sq.setText(vox_data[interphone.getSq()]);

                                                }
                                                if(interphone.getRf() != null){

                                                    LogUtil.i(TAG,"getRf"+(interphone.getRf() - 1));

                                                    setTitleLayout();


                                                    LogUtil.i(TAG,"getinterPhoneRF:"+MyApplicatoin.interPhoneRF);

                                                //    radioGroup_rf.setOnCheckedChangeListener(null);

                                                    /*if(MyApplicatoin.interPhoneRF == 2){

                                                        radioButtons_rf[2].setChecked(true);

                                                    }else{

                                                        radioButtons_rf[interphone.getRf() - 1].setChecked(true);

                                                    }
                                                    radioGroup_rf.setOnCheckedChangeListener(Fragment_Device.this);*/

                                                    if(MyApplicatoin.interPhoneRF == 2){

                                                        tv_ble_rf.setText(rf_data[2]);

                                                    }else{

                                                        tv_ble_rf.setText(rf_data[interphone.getRf() - 1]);

                                                    }

                                                }
                                                if(interphone.getTx_power() != null){

                                                    LogUtil.i(TAG,"getTx_power"+(interphone.getTx_power() - 1));

                                                    tv_ble_tx_power.setText(tx_power_data[interphone.getTx_power() - 1]);

                                                //    radioButtons_tx_power[(int)(interphone.getTx_power() - 1)].setChecked(true);
                                                    //    radioGroup_tx_power.check(radioButtons_tx_power[interphone.getTx_power() - 1].getId());

                                                    Tools.closeProgressDialog();

                                                }
                                                if(interphone.getVox() != null){

                                                    tv_ble_vox.setText(vox_data[interphone.getVox()]);

                                                }


                                                if(phone.getStop_hite() != null){

                                                    if (interphone.getStop_hite() == 1) {

                                                        ble_Stop_hite.setChecked(switchInt, true);


                                                    } else {

                                                        ble_Stop_hite.setChecked(switchInt, false);

                                                    }

                                                }

                                                if(phone.getTx_hite() != null){

                                                    if (interphone.getTx_hite() == 1) {

                                                        ble_Tx_hite.setChecked(switchInt, true);


                                                    } else {

                                                        ble_Tx_hite.setChecked(switchInt, false);

                                                    }

                                                }

                                                if(phone.getBw() != null){

                                                    if (interphone.getBw() == 1) {

                                                        ble_Bw.setChecked(switchInt, true);


                                                    } else {

                                                        ble_Bw.setChecked(switchInt, false);

                                                    }

                                                }

                                                if(phone.getDisconnec_hite() != null){

                                                    if (interphone.getDisconnec_hite() == 1) {

                                                        ble_Disconnec.setChecked(switchInt, true);


                                                    } else {

                                                        ble_Disconnec.setChecked(switchInt, false);

                                                    }

                                                }
                                                if(phone.getIsplay() != null){

                                                    if (interphone.getIsplay() == 1) {

                                                        ble_Voice.setChecked(switchInt, true);


                                                    } else {

                                                        ble_Voice.setChecked(switchInt, false);

                                                    }

                                                }




                                                if(interphone.getVolt() != 0){

                                                    float a = interphone.getVolt();

                                                    tv_ble_volt.setText(String .format("%.1f",(a/1000)));

                                                    setTitleLayout();

                                                }
                                        /*if(interphone.getBw() != null){

                                            if (interphone.getBw() == 1) {

                                                if(!ble_Bw.isChecked()){
                                                    ble_Bw.setChecked(switchInt, true);
                                                }

                                            } else {

                                                if(ble_Bw.isChecked()){
                                                    ble_Bw.setChecked(switchInt, false);
                                                }
                                            }

                                        }
                                        if(interphone.getBluetooth() != null){

                                            if (interphone.getBluetooth() == 2) {

                                                ble_Bluetooth.setState(true);

                                            } else {

                                                ble_Bluetooth.setState(false);

                                            }

                                        }
                                        if(interphone.getMode()!= null){

                                            if (interphone.getMode() == 1) {

                                                if(!ble_Mode.isChecked()){

                                                    ble_Mode.setChecked(switchInt, true);
                                                }

                                            } else {

                                                if(ble_Mode.isChecked()){

                                                    ble_Mode.setChecked(switchInt, false);

                                                }

                                            }

                                        }*/


                                            } catch (Exception e) {
                                                ExceptionUtil.handleException(e);
                                            }
                                        }
                                    });


                                }



                            }

                        }
                    }
                }
            });
        } else {
            LogUtil.i(GlobalConsts.TAG,"BLE????????????????????????????????????");
        }
    }



    private String getFreq(int frequne){


        float b = frequne;
    //    DecimalFormat  df = new DecimalFormat("#.000");
   // Log.d("dddd","b       "+b);
    //   return new String(df.format(frequne/1000));
          return  String .format("%.4f",(b/1000000));

    }

    private int getFreqCode(Integer frequne) {

        int k = 100;

        if(frequne != null){

            for(int i = 0;i<c.length;i++){

                if(frequne == c[i]){

                    k = i;

                }
            }

        }

        return k;

    }



    public void setEditTextClick(int editTextClick) {

        if(freq.size() < 7){

            freq.add(editTextClick);

            for(int i = 0; i < freq.size();i++){

                tvhiteArray[i].setText(freq.get(i).toString());

            }
        }


    }

    private void setListener(final AlertDialog dialog_freq) {

        for (TextView tv : tvArray){

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    switch (v.getId()){

                        case R.id.button0:

                            setEditTextClick(0);

                            break;
                        case R.id.button1:

                            setEditTextClick(1);

                            break;
                        case R.id.button2:

                            setEditTextClick(2);

                            break;
                        case R.id.button3:

                            setEditTextClick(3);

                            break;
                        case R.id.button4:

                            setEditTextClick(4);

                            break;
                        case R.id.button5:

                            setEditTextClick(5);

                            break;
                        case R.id.button6:

                            setEditTextClick(6);

                            break;
                        case R.id.button7:

                            setEditTextClick(7);

                            break;
                        case R.id.button8:

                            setEditTextClick(8);

                            break;
                        case R.id.button9:

                            setEditTextClick(9);

                            break;
                        case R.id.button_delete:

                            if(freq.size() > 0){

                                tvhiteArray[freq.size() - 1].setText("");
                                freq.remove(freq.size() - 1);
                            }

                            break;
                        case R.id.button_ok:

                        for(int i=0;i<=6;i++){
                            if(freq.size() == i+1){

                                String freqs = "";

                                for(Integer integer : freq){

                                    freqs += String.valueOf(integer);

                                }
                                LogUtil.i(TAG,freqs);

                                int a = Integer.parseInt(freqs);

                                LogUtil.i(TAG,a);

                                Interphone interphone = new Interphone();
                                int[] str={100000000,10000000,1000000,100000,10000,1000,100};
                                interphone.setFrequne(a * str[i]);
                            //    set_ble(sms_request.SET,interphone);
                                MyApplicatoin.setInterphone(interphone);

                                dialog_freq.cancel();
                            }
                        }

                            break;

                    }

                }
            });

        }

    }

    private void tooleBle() {

        long currentTime = Calendar.getInstance().getTimeInMillis();

        if(currentTime - lastHiteTime > 1500){

            Tools.showInfo(getActivity(),getResources().getString(R.string.sms_main_toast_hite_connect_device));

        }
        lastHiteTime = currentTime;

    }


    private boolean isInterphone(){

        if(!MyApplicatoin.IS_BLE){

            tooleBle();
            return false;
        }

        if(MyApplicatoin.interphone == null){

            return false;
        }


        return true;

    }


    private boolean getInterphone(){

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
            return false;
        }


        return true;

    }

    private void setUserData() {//????????????

        if(MyApplicatoin.evenUser != null){
            if(MyApplicatoin.evenUser.getId().equals(Constants.USERIDLONG)){//????????????MyApplicatoin.evenUser???????????????
                user_name.setText(getResources().getString(R.string.sms_main_user_name));
                sms_main_icon.setImageResource(R.mipmap.icon_chat_01);
                return;
            }
            user_name.setText(MyApplicatoin.evenUser.getName());
            e_mail.setText(MyApplicatoin.evenUser.getE_mail());
            LogUtil.i(TAG,"Avatar_url:"+MyApplicatoin.evenUser.getAvatar_url());
            try {

                if(MyApplicatoin.evenUser.getAvatar_url() != null){
                    Bitmap bitmap =getLoacalBitmap(MyApplicatoin.evenUser.getAvatar_url()); //??????????????????(???cdcard?????????)
                    sms_main_icon.setImageBitmap(bitmap);
                }else{
                    sms_main_icon.setImageResource(R.mipmap.icon_chat_01);
                }
            } catch (Exception e) {
                ExceptionUtil.handleException(e);
            }
        }else{

            user_name.setText(getResources().getString(R.string.sms_main_user_name));
            sms_main_icon.setImageResource(R.mipmap.icon_chat_01);

        }

    }

   //??????????????????
    public Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            ExceptionUtil.handleException(e);
            return null;
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



    private void showLoginDialog(String description) {

        final AlertDialog dialog = new AlertDialog.Builder( Fragment_Device.this.getActivity()).create();
        View view = View.inflate( Fragment_Device.this.getActivity(), R.layout.dialog_link_layout, null);
        dialog.setView(view);
        dialog.setInverseBackgroundForced(true);
        dialog.setCancelable(false);
        final Button bt_cancel = (Button) view.findViewById(R.id.button_cancel);
        final Button bt_ok = (Button) view.findViewById(R.id.button_ok);
        final TextView tv_content = (TextView) view.findViewById(R.id.content);
        final TextView tv_title = (TextView) view.findViewById(R.id.title);

       // tv_title.setText(getResources().getString(R.string.sms_main_updata_apk));
        tv_title.setText("???????????????"+getVersionName(Fragment_Device.this.getActivity())  +"\n???????????????"+android_version);

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


                Intent intent = new Intent( Fragment_Device.this.getActivity(), UpdataService.class);
                intent.setAction(UpdataService.APK);
                Fragment_Device.this.getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);

                dialog.cancel();


                if(!MyApplicatoin.isUpdata){

                }else{
                    Tools.showInfo( Fragment_Device.this.getActivity(),getResources().getString(R.string.isupdata));
                }

                // startActivity(new Intent(Intent.ACTION_VIEW, android.net.Uri.parse("http://smart.nitecore.com/app/app_SMART1_android_31_8197745.apk" )));
                dialog.cancel();
            }
        });
        dialog.show();

    }




    private String a_adv_version;
    private void setUi() {


        Precursor precursor = new Precursor();

        precursor.setSeries("GLOBALWALKIE");

        sms_request.dorequest(Fragment_Device.this.getActivity(), sms_request.GET, precursor, new RequestCB() {
            @Override
            public void respone(int opcode, Object obj) {

                LogUtil.i(TAG,"RequestCB:"+opcode);

                if(opcode == sms_request.GET){
                    try {
                        Precursor precursor = (Precursor) obj;


                        if (precursor != null) {

                            a_adv_version = precursor.getAdv_version();

                            android_version = precursor.getAndroid_version();
                            android_url = precursor.getAndroid_url();
                            android_description = precursor.getAndroid_description();

                            if(PrecursorString[0].equals("")){

                                url = precursor.getAdv_url();

                                if(url != null){

                                    Intent intent = new Intent(Fragment_Device.this.getActivity(), UpdataService.class);
                                    intent.setAction(UpdataService.WELCOME);
                                    Fragment_Device.this.getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);

                                }else{

                                    LogUtil.i(TAG,"????????????url = null");
                                }

                            }else{


                                if(!precursor.getAdv_version().equals(PrecursorString[0])){

                                    //???????????????????????????????????????

                                    url = precursor.getAdv_url();

                                    if(url != null){

                                        Intent intent = new Intent(Fragment_Device.this.getActivity(), UpdataService.class);
                                        intent.setAction(UpdataService.WELCOME);
                                        Fragment_Device.this.getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);

                                    }else{

                                        LogUtil.i(TAG,"????????????url = null");
                                    }

                                }else{

                                    //???????????????????????????????????????

                                    Bitmap bitmap = getLoacalBitmap(PrecursorString[1]); //??????????????????(???cdcard?????????)

                                   // imageView.setImageResource(R.mipmap.sms_advtwo);
                                }
                            }

                        }else{

                            LogUtil.i(TAG,"???????????????precursor = null");


                        }
                    } catch (Exception e) {
                        ExceptionUtil.handleException(e);
                    }


                }else{

                    //?????????????????????????????????????????????

                    /*Bitmap bitmap = getLoacalBitmap(PrecursorString[1]); //??????????????????(???cdcard?????????)*/

                   // imageView.setImageResource(R.mipmap.sms_advtwo);

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

                                }else{

                                    if(!MyApplicatoin.production.getFirmware_version().equals(PrecursorString[0])){

                                        PrecursorString[0] = MyApplicatoin.production.getFirmware_version();

                                        PrecursorString[1] = service.excute(UpdataService.firmwarefile,MyApplicatoin.production.getFirmware_url(),"");
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

                                SharedPreferences sharedPreferences = Fragment_Device.this.getActivity().getSharedPreferences(GlobalConsts.PRECURSOR, Context.MODE_PRIVATE);
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
                                    Fragment_Device.this.getActivity().unbindService(connection);
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

                                    SharedPreferences sharedPreferences = Fragment_Device.this.getActivity().getSharedPreferences(GlobalConsts.PRECURSOR, Context.MODE_PRIVATE);
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
                                        Fragment_Device.this.getActivity().unbindService(connection);
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
                            Fragment_Device.this.getActivity().unbindService(connection);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }else{

                try {
                    if(connection != null){
                        Fragment_Device.this.getActivity().unbindService(connection);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }




        }
    };



    //??????????????????
   /* public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        user_name.setText(MyApplicatoin.evenUser.getName());
        return super.onCreateAnimator(transit, enter, nextAnim);
    }*/

}
