package com.sms.app.interphone.ui.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.interphone.R;
import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.interphone.ui.fragment.Fragment_Map;
import com.sms.app.interphone.util.aesutil.EncryptUtil;
import com.sms.app.interphone.util.interfaces.NoDoubleClickListener;
import com.sms.app.interphone.util.msnutil.ExceptionUtil;
import com.sms.app.interphone.util.msnutil.Tools;
import com.sms.app.interphone.view.PowerView;

import java.util.Arrays;

public class MapActivity extends Activity {

    private ImageView img_back;

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

    private Fragment_Map map = null;

    private boolean isTitleSwitch = true;

    private MapReceiver mapReceiver = null;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        setView();

        if(mapReceiver  == null){

            mapReceiver = new MapReceiver();

            this.registerReceiver(mapReceiver,new IntentFilter(GlobalConsts.ACTION_MAP));
        }
    }

    private void setView() {

        title_one_layout = (LinearLayout) findViewById(R.id.device_one_layout);
        title_to_layout = (LinearLayout) findViewById(R.id.device_to_layout);
        title_one_group = (TextView) findViewById(R.id.device_title_group_name1);
        title_to_group = (TextView) findViewById(R.id.device_title_group_name2);
        title_one_freq = (TextView) findViewById(R.id.device_title_freq1);
        title_to_freq = (TextView) findViewById(R.id.device_title_freq2);


        title_mode = (TextView) findViewById(R.id.device_title_mode);
        title_tc = (TextView) findViewById(R.id.device_title_tc);
        title_rc = (TextView) findViewById(R.id.device_title_rc);

        powerView1 = (PowerView) findViewById(R.id.device_title_vole1);
        powerView2 = (PowerView) findViewById(R.id.device_title_vole2);


        AssetManager assets = getAssets();

        Typeface fromAsset_name = Typeface.createFromAsset(assets, "fonts/Helvetica_LT_Condensed_Bold.ttf");
        Typeface fromAsset_freq = Typeface.createFromAsset(assets, "fonts/Helvetica_CE_Regular.ttf");
        Typeface fromAsset_to = Typeface.createFromAsset(assets, "fonts/Helvetica_LT_Light.ttf");


        /*title_one_group.setTypeface(fromAsset_name);
        title_one_freq.setTypeface(fromAsset_freq);

        title_to_group.setTypeface(fromAsset_to);
        title_to_freq.setTypeface(fromAsset_to);*/

        img_back = (ImageView) findViewById(R.id.chat_title_imageview1);


        FragmentManager fragmentManager=getFragmentManager();

        map = (Fragment_Map) fragmentManager.findFragmentById(R.id.map_fragment);

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


        img_back.setOnClickListener(new NoDoubleClickListener(){
            @Override
            public void onNoDoubleClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        setTitleLayout();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mapReceiver != null){

            this.unregisterReceiver(mapReceiver);
        }

        finish();
    }


    class MapReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            int status = intent.getIntExtra(GlobalConsts.ACTION_USER, -1);

            if(status == GlobalConsts.ACTTION_IS_SCAN){
                setTitleLayout();
            }else if(status == GlobalConsts.ACTTION_IS_MSG){

                if(map != null){
                    map.setMap();
                }

            }
        }
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

                if(isInterphone()){

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


    private String getFreq(int frequne){


        float b = frequne;
        //    DecimalFormat  df = new DecimalFormat("#.000");

        //   return new String(df.format(frequne/1000));
        return  String .format("%.4f",(b/1000000));

    }
    private boolean isInterphone(){

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

            Tools.showInfo(MapActivity.this,getResources().getString(R.string.sms_main_toast_hite_user));
            return false;
        }


        return true;

    }



}
