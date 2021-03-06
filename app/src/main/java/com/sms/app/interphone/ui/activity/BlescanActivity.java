package com.sms.app.interphone.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sms.app.framework.RequestCB;
import com.sms.app.framework.communication.localayer.Interphone_notify_listenner;
import com.sms.app.framework.communication.localayer.attr_listenner;
import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.framework.communication.localayer.cmd.Opcode;
import com.sms.app.framework.communication.localayer.cmd.Result;
import com.sms.app.framework.sms_request;
import com.sms.app.framework.trans.bean.Interphone;
import com.sms.app.framework.trans.sms_fw_api;
import com.sms.app.interphone.R;
import com.sms.app.interphone.adapter.BleListViewAdapter;
import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.interphone.ui.fragment.Fragment_Device;
import com.sms.app.interphone.util.frequentlyutil.ProgressDialogUniversal;
import com.sms.app.interphone.util.frequentlyutil.SharePreferenceConstant;
import com.sms.app.interphone.util.frequentlyutil.SharePreferenceUtil;
import com.sms.app.interphone.util.interfaces.NoDoubleClickListener;
import com.sms.app.interphone.util.msnutil.ExceptionUtil;
import com.sms.app.interphone.util.msnutil.Tools;
import com.sms.app.interphone.view.RippleBackground;

import java.util.ArrayList;
import java.util.List;

/**
 * ???????????????????????????Activity
 *
 *
 * */

public class BlescanActivity extends BaseActivity{


    private static String TAG = "YanShi...Log - BlescanActivity";


    private ListView lv;

    private TextView ble_device_name;

    private RippleBackground mRippleView;

    private ImageView imageView;

    private ImageView imageView_huitui;

    private Button bt;

    private boolean isToos = false;

    private List<BluetoothDevice> is_device = new ArrayList<BluetoothDevice>();             //????????????BLE????????????

    private BluetoothAdapter mBluetoothAdapter;

    private BleListViewAdapter ble_Adapter = null;

    private RelativeLayout search_device_layout;

    private static final int REQUEST_ENABLE_BT = 1;

    public static final String NITECORE = "GW-";
 //   private final String NITECORE = "NITECORE-";


    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION"
    };

    private Dialog dialog;
    private Window window;
    private ImageView imageView_blescan_device_icon,imageView_refresh;
    private TextView textViewbottom;
    private String android_version;
    private String android_url;
    private String android_description;
    private int GPS_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blescan);



        Intent intent = getIntent();

        android_url = intent.getStringExtra(GlobalConsts.PRECURSOR_ANDROID_URL);
        android_version = intent.getStringExtra(GlobalConsts.PRECURSOR_ANDROID_VERSION);
        android_description = intent.getStringExtra(GlobalConsts.PRECURSOR_ANDROID_DESCRIPTION);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "?????????BLE", Toast.LENGTH_SHORT).show();
            finish();
        }
        // ??????????????????????????????
        //??????BluetoothManager BluetoothAdapter
    /*    final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

        mBluetoothAdapter = bluetoothManager.getAdapter();*/

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // ??????????????????????????????
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "???????????????", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        BlescanSearchDevice();
        imageView_blescan_device_icon = (ImageView) window.findViewById(R.id.imageView_blescan_device_icon);
        imageView_refresh = (ImageView) window.findViewById(R.id.imageView_refresh);
        textViewbottom = (TextView) window.findViewById(R.id.textViewbottom);
        textViewbottom.setText("????????????????????????");
        setView();

        if(hasPermission(PERMISSIONS_STORAGE)){

            if (!mBluetoothAdapter.isEnabled()) {


            } else{

                Interphone interphone = new Interphone();
                interphone.setConnect((byte) 3);
                start_le_service(sms_request.GET,interphone);


            }

        }else{

            requstPermissions(GlobalConsts.WRITE_EXTERNAL_STORAGE,PERMISSIONS_STORAGE);

        }

        if (Build.VERSION.SDK_INT > 26||Build.VERSION.SDK_INT==26){//android8.0?????????????????????????????????
            openGPSSEtting();
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //startActivity(new Intent(BlescanActivity.this, MainActivity.class));
            finish();

           // overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setView() {

        bt = (Button) findViewById(R.id.ble_scan_button);

        lv = (ListView) findViewById(R.id.listView);

        imageView = (ImageView) findViewById(R.id.imageView_ble_icon);
        imageView_huitui = (ImageView) findViewById(R.id.imageView_ble_huitui);

        ble_device_name = (TextView) findViewById(R.id.ble_device_name);

        search_device_layout = (RelativeLayout) findViewById(R.id.sms_ble_search_device_layout);

        mRippleView = (RippleBackground) findViewById(R.id.sms_ble_search_device);

        //??????listview???????????????
    //    lv.setOverScrollMode(View.OVER_SCROLL_NEVER);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (is_device != null) {

                    try {

                        Tools.showProgressDialog(BlescanActivity.this,"?????????????????????");

                        Interphone interphone = new Interphone();
                        interphone.setAddress(is_device.get(position).getAddress());
                        start_le_service(sms_request.GET,interphone);

                        isToos = true;

                    } catch (Exception e) {

                        ExceptionUtil.handleException(e);

                    }
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mRippleView.startRippleAnimation();
                is_device.clear();

                if(ble_Adapter != null){
                    ble_Adapter.notifyDataSetChanged();
                }else{
                    ble_Adapter = new BleListViewAdapter(BlescanActivity.this, is_device);
                    lv.setAdapter(ble_Adapter);
                }

            }
        });

        imageView_huitui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mRippleView.stopRippleAnimation();
                Intent intent=new Intent(BlescanActivity.this,MainActivity.class);
                intent.putExtra(GlobalConsts.PRECURSOR_ANDROID_URL,android_url);
                intent.putExtra(GlobalConsts.PRECURSOR_ANDROID_VERSION,android_version);
                intent.putExtra(GlobalConsts.PRECURSOR_ANDROID_DESCRIPTION,android_description);
                startActivity(intent);
                finish();

               // overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });


        bt.setOnClickListener(new NoDoubleClickListener(){
            @Override
            public void onNoDoubleClick(View v) {
                isToos = false;
                Interphone interphone = new Interphone();
                interphone.setConnect((byte) 2);
                start_le_service(sms_request.GET,interphone);

            }
        });

        imageView_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                mRippleView.startRippleAnimation();
                is_device.clear();

                if(ble_Adapter != null){
                    ble_Adapter.notifyDataSetChanged();
                }else{
                    ble_Adapter = new BleListViewAdapter(BlescanActivity.this, is_device);
                    lv.setAdapter(ble_Adapter);
                }
            }
        });


    }

    private void start_le_service(int code,final Interphone interphone) {

    //    LogUtil.i(TAG,"Interphone:"+interphone.toString());

        sms_fw_api.do_request(BlescanActivity.this, code, interphone, new RequestCB() {
            @Override
            public void respone(int opcode, Object obj) {

              //  LogUtil.i(GlobalConsts.TAG,"BlescanActivity.do_request1:"+opcode);

            }
        });

        sms_fw_api.add_attr_listenner(BlescanActivity.this, new attr_listenner() {
            @Override
            public void onAttributeChang(int opcode, Object object) {

                LogUtil.i(TAG,"sms_fw_api:"+opcode);



                try {
                    if(opcode == Interphone_notify_listenner.SCAN){

                        BluetoothDevice interphone = (BluetoothDevice) object;

                        LogUtil.i(TAG,"interphone:"+interphone.toString());
                        if(interphone != null){

                            if(is_device.size() > 0 ){

                                boolean is_ble = false;

                                for (BluetoothDevice for_device : is_device) {
                                    if (for_device.equals(interphone)) {
                                        is_ble = true;
                                    }
                                }
                                if (!is_ble) {

                                    if(interphone.getName() != null){



                                        /*is_device.add(interphone);

                                        updateListView(is_device);*/

                                        if(interphone.getName().length() >= NITECORE.length()){

                                            String title = interphone.getName().substring(0,NITECORE.length());

                                            if(title.equals(NITECORE)){

                                                is_device.add(interphone);

                                                updateListView(is_device);

                                            }
                                        }
                                    }
                                }

                            }else{

                                if(interphone.getName() != null){

                                    /*is_device.add(interphone);

                                    updateListView(is_device);*/

                                    if(interphone.getName().length() >= NITECORE.length()){

                                        String title = interphone.getName().substring(0,NITECORE.length());

                                        if(title.equals(NITECORE)){

                                            is_device.add(interphone);

                                            updateListView(is_device);

                                        }

                                    }

                                }

                            }

                        }

                    }else if(opcode == Interphone_notify_listenner.ATTR){

                        Tools.closeProgressDialog();

                        Interphone interphone = (Interphone) object;

                        if(interphone.getState() == BluetoothProfile.STATE_CONNECTED){

                            LogUtil.i(TAG,"????????????"+interphone.toString());

                            GlobalConsts.connect(true,interphone);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        imageView_blescan_device_icon.setImageDrawable(getResources().getDrawable(R.mipmap.searchdevicesucceed));
                                        textViewbottom.setText("Entering the program...");
                                        imageView_refresh.setVisibility(View.GONE);
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    Tools.showInfo(BlescanActivity.this,getResources().getString(R.string.sms_deivce_connection_successful));
                                }
                            });

                            /*search_device_layout.setVisibility(View.VISIBLE);

                            if(MyApplicatoin.interphone.getName() != null){
                                ble_device_name.setText(MyApplicatoin.interphone.getName());
                            }*/
                            Intent intent=new Intent(BlescanActivity.this,MainActivity.class);
                            intent.putExtra(GlobalConsts.PRECURSOR_ANDROID_URL,android_url);
                            intent.putExtra(GlobalConsts.PRECURSOR_ANDROID_VERSION,android_version);
                            intent.putExtra(GlobalConsts.PRECURSOR_ANDROID_DESCRIPTION,android_description);
                            startActivity(intent);
                            finish();

                            //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                        }else{

                            GlobalConsts.connect(false,interphone);

                            LogUtil.i(TAG,"????????????");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                  /*  try {
                                        imageView_blescan_device_icon.setImageDrawable(getResources().getDrawable(R.mipmap.searchdevicefailed));
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }*/
                                    Tools.showInfo(BlescanActivity.this,getResources().getString(R.string.sms_deivce_disconnect));

                                    search_device_layout.setVisibility(View.GONE);
                                }
                            });

                        }


                      /*  try {
                            Thread.sleep(5000);
                            ProgressDialogUniversal.jdtdialog.dismiss();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                    }else if(opcode == Interphone_notify_listenner.RESULT){

                        LogUtil.i(TAG,"Opcode.RESULT");


                        final Result result = (Result) object;

                        byte resu = result.getResult();

                        if(resu == GlobalConsts.YES){

                        //    finish();

                        }else{

                            LogUtil.i(TAG,"?????????????????????");

                        }

                    }else if(opcode == Interphone_notify_listenner.NOYIFY){

                        //??????????????????
                        LogUtil.i(TAG,"Opcode.NOYIFY");

                        /*if(MyApplicatoin.interphone != null){

                            if(MyApplicatoin.interphone.getImCode() != null){

                                Tools.closeProgressDialog();



                            }


                        }*/

                    }
                   // ProgressDialogUniversal.jdtdialog.dismiss();
                } catch (Exception e) {
                    ExceptionUtil.handleException(e);
                }
            }
        });
    }
    private void updateListView(final List<BluetoothDevice> is_devices) {


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ble_Adapter != null) {
                    ble_Adapter.notifyDataSetChanged();
                } else {
                    ble_Adapter = new BleListViewAdapter(BlescanActivity.this, is_devices);
                    lv.setAdapter(ble_Adapter);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // ????????????????????????
        try {
            if(MyApplicatoin.IS_BLE){

                search_device_layout.setVisibility(View.VISIBLE);

                if(MyApplicatoin.interphone.getName() != null){
                    ble_device_name.setText(MyApplicatoin.interphone.getName());
                }


            }else{
                search_device_layout.setVisibility(View.GONE);
            }


            LogUtil.i(TAG,"mBluetoothAdapter.isEnabled():"+mBluetoothAdapter.isEnabled());
            if (!mBluetoothAdapter.isEnabled()) {
                SharePreferenceUtil.saveStringDataToSharePreference(BlescanActivity.this, SharePreferenceConstant.MBLUETOOTH, "mBluetooth");//??????????????????????????????
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

            } else{

                Interphone interphone = new Interphone();
                interphone.setConnect((byte) 3);
                start_le_service(sms_request.GET,interphone);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mRippleView.startRippleAnimation();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.i(TAG,"onDestroy()");

        Interphone interphone = new Interphone();
        interphone.setConnect((byte) 0);
        MyApplicatoin.setInterphone(interphone);
       // ProgressDialogUniversal.jdtdialog.dismiss();
    }

    /**
     * ????????????activity ??????
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // ???????????????????????????.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_CANCELED) {
            //finish();
            return;
        }

        if (requestCode ==GPS_REQUEST_CODE){
            openGPSSEtting();
        }
    }


    public void BlescanSearchDevice(){
        dialog = new Dialog(BlescanActivity.this, R.style.fullDialog);
        dialog.show();
        window = dialog.getWindow();
        window.setContentView(R.layout.blescan_search_device);
        window.setGravity(Gravity.FILL);
        //ProgressDialogUniversal.jdtdialog= new ProgressDialog(window.getContext());
       // ProgressDialogUniversal.jdtProgressDialoga("??????????????????......");
        // dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        String blescan = SharePreferenceUtil.getStringDataByKe(BlescanActivity.this, SharePreferenceConstant.BLESCAN, "");
        if ("blescan".equals(blescan) || blescan == "blescan") {
            SharePreferenceUtil.saveStringDataToSharePreference(BlescanActivity.this, SharePreferenceConstant.BLESCAN, "");
            dialog.cancel();
            //ProgressDialogUniversal.jdtdialog.dismiss();
        }

    }



    private void openGPSSEtting() {//????????????
        if (!checkGpsIsOpen()){
            new AlertDialog.Builder(this).setTitle("????????????????????????")
                    .setMessage("????????????")
                    //  ????????????
                    .setNegativeButton("??????",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // ??????dialog
                            dialogInterface.dismiss();
                        }
                    })
                    //  ????????????
                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //?????????????????????????????????
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent,GPS_REQUEST_CODE);
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }

    private boolean checkGpsIsOpen() {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isOpen;
    }


}
