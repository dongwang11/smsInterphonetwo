package com.sms.app.interphone.ui.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sms.app.framework.RequestCB;
import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.framework.sms_request;
import com.sms.app.framework.trans.bean.Info_check;
import com.sms.app.framework.trans.bean.User;
import com.sms.app.framework.trans.bean.Verification;
import com.sms.app.interphone.R;
import com.sms.app.interphone.util.interfaces.NoDoubleClickListener;
import com.sms.app.interphone.util.msnutil.ExceptionUtil;
import com.sms.app.interphone.util.msnutil.Tools;

import java.util.Random;
import java.util.regex.Pattern;


public class RegisterActivity extends BaseActivity implements View.OnFocusChangeListener{

    public final byte YES = 1;
    public final byte NO = 0;


    private EditText ed_name,ed_code, ed_E_mail, ed_pass, ed_pass_confirm;

    private String name = "SMS-";

    private Button bt_code;

    private TextView tv_login;

    private static final int RETRY_INTERVAL = 60; //设置一个倒计时时间
    private int time = RETRY_INTERVAL;

    private Verification verification = null;

    private LinearLayout bt_Register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setView();
        setListener();
    }

    private void setView() {
        ed_name = (EditText) findViewById(R.id.chat_register_edtext_name);
        ed_E_mail = (EditText) findViewById(R.id.chat_register_edtext_email);
        ed_code = (EditText) findViewById(R.id.chat_register_edtext_verification);
        ed_pass = (EditText) findViewById(R.id.chat_register_edtext_password);
        ed_pass_confirm = (EditText) findViewById(R.id.chat_register_edtext_confirm_password);

        tv_login = (TextView) findViewById(R.id.chat_register_text_login);

        tv_login.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线

        //监听输入框，只允许输入字符及数字
    //    ed_name.addTextChangedListener(new SearchWather(ed_name));


        ed_code.setOnFocusChangeListener(this);
        ed_name.setOnFocusChangeListener(this);
        ed_E_mail.setOnFocusChangeListener(this);
        ed_pass.setOnFocusChangeListener(this);
        ed_pass_confirm.setOnFocusChangeListener(this);

        bt_Register = (LinearLayout) findViewById(R.id.ll_chat_register_button_register);

        bt_code = (Button) findViewById(R.id.chat_register_button_verification);

    }

    /**
     *
     * 子类去具体实现打电话的业务逻辑
     *
     * */



    private void setListener() {

        tv_login.setOnClickListener(new NoDoubleClickListener(){
            @Override
            public void onNoDoubleClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });
        bt_code.setOnClickListener(new NoDoubleClickListener(){
            @Override
            public void onNoDoubleClick(View v) {

                if(!matchPhone(ed_E_mail.getText().toString())){
                      Tools.showInfo(RegisterActivity.this,getResources().getString(R.string.chat_register_text_no_email));
                    return;
                }

                if (!TextUtils.isEmpty(ed_E_mail.getText().toString())) {
                    Tools.showProgressDialog(RegisterActivity.this,getResources().getString(R.string.chat_register_text_sending_verification));

                    Info_check check = new Info_check();
                    check.setE_mail(ed_E_mail.getText().toString());

                    check_E_mail(check);

                }else{
                    Tools.showInfo(RegisterActivity.this,getResources().getString(R.string.chat_register_text_email_erro));
                }
            }

        });

        bt_Register.setOnClickListener(new NoDoubleClickListener(){
            @Override
            public void onNoDoubleClick(View v) {

                regiser();
            }
        });

    }

    private void regiser() {

        String username = ed_name.getText().toString();

        String VerificationCode = ed_code.getText().toString();

        String userE_mail = ed_E_mail.getText().toString();

        String password = ed_pass.getText().toString();

        // 验证用户的输入是否合法
        final StringBuilder builder = new StringBuilder();

        /*if (TextUtils.isEmpty(username)) {

            builder.append(getResources().getString(R.string.chat_register_text_account_null));

        }else*/ if (TextUtils.isEmpty(userE_mail)) {

            builder.append(getResources().getString(R.string.chat_register_text_email_erro));

        }else if (verification == null || !verification.getE_mail().equals(userE_mail)) {

            builder.append(getResources().getString(R.string.chat_register_text_email_erro_hite));

        }else if (TextUtils.isEmpty(VerificationCode)) {

            builder.append(getResources().getString(R.string.chat_register_text_verification));

        }else if (verification == null || !verification.getVerification_code().equals(VerificationCode)){

            builder.append(getResources().getString(R.string.chat_register_text_verification_erro));

        }else if (TextUtils.isEmpty(password)) {

            builder.append(getResources().getString(R.string.chat_register_text_password_erro));

        }else if (!password.equals(ed_pass_confirm.getText().toString())) {

            builder.append(getResources().getString(R.string.chat_register_text_password_erro_hite));

        }/*else if (!username.equals(name)) {

            final Info_check inCheck = new Info_check();
            inCheck.setName(username);

            LogUtil.i(GlobalConsts.TAG,inCheck.toString());

            getUserName(inCheck);

        }*/


        if (TextUtils.isEmpty(builder.toString())) {
            // 验证通过
            LogUtil.i(TAG,"验证通过:");

            Tools.showProgressDialog(RegisterActivity.this,getResources().getString(R.string.chat_register_text_hint_register));

            User user = new User();

            int max=99999;
            int min=10000;
            Random random = new Random();

            int s = random.nextInt(max)%(max-min+1) + min;

            LogUtil.i(TAG,"随机数："+s);

            if(username == null ){
                name = name+s;
            }else if(username.length() < 1){
                name = name+s;
            }else {
                name = username;
            }


            user.setName(name);
            user.setE_mail(userE_mail);
            user.setPasswd(password);

            RegisterUser(user);

        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RegisterActivity.this, builder.toString(), Toast.LENGTH_LONG).show();

                }
            });
        }

    }

    private void RegisterUser(User user) {

        sms_request.dorequest(RegisterActivity.this, sms_request.REGISTER, user, new RequestCB() {
            @Override
            public void respone(int opcode, final Object obj) {

                if(opcode == sms_request.REGISTER){

                    User user = (User) obj;

                    Log.e(GlobalConsts.TAG,"User:"+user.toString());

                    Tools.closeProgressDialog();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Tools.showInfo(RegisterActivity.this,getResources().getString(R.string.chat_register_text_hint_register_successful));
                        }
                    });
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                }else if(opcode == sms_request.ERRO){
                    Tools.closeProgressDialog();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Tools.showInfo(RegisterActivity.this,getResources().getString(R.string.chat_register_text_hint_register_erro));

                        }
                    });
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        return super.onKeyDown(keyCode, event);
    }


    private void check_E_mail(Info_check check) {

        sms_request.dorequest(RegisterActivity.this, sms_request.GET, check, new RequestCB() {
            @Override
            public void respone(final int opcode, final Object obj) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(opcode == sms_request.GET){

                            Log.e(GlobalConsts.TAG,"Info_check:"+obj.getClass().getName().toString());

                            Info_check info_check = (Info_check) obj;

                            if(info_check.getE_mail() == null){

                                //发送验证码
                                Verification ation = new Verification();
                                ation.setE_mail(ed_E_mail.getText().toString());

                                sms_request.dorequest(RegisterActivity.this, sms_request.GET, ation, new RequestCB() {
                                    @Override
                                    public void respone(final int opcode, final Object obj) {

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                Tools.closeProgressDialog();

                                                if(opcode == sms_request.GET){

                                                    Tools.showInfo(RegisterActivity.this,getResources().getString(R.string.chat_register_text_hint_register_ok));

                                                    verification = (Verification) obj;

                                                    Log.e(GlobalConsts.TAG,"Verification:"+verification.toString());

                                                    countDown();

                                                }
                                            }
                                        });
                                    }
                                });

                            }else{
                                Tools.closeProgressDialog();
                                Tools.showInfo(RegisterActivity.this,getResources().getString(R.string.chat_register_text_hint_register_erro_is));
                            }

                        }else if(opcode == sms_request.ERRO){
                         //   Erro erro = (Erro) obj;
                            Tools.showInfo(RegisterActivity.this,getResources().getString(R.string.chat_register_networkinfo_erro));
                        }
                    }
                });
            }
        });
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        switch (v.getId()){

            /*case R.id.chat_register_edtext_name:

                if(!hasFocus && !TextUtils.isEmpty(ed_name.getText().toString())){

                    final Info_check inCheck = new Info_check();
                    inCheck.setName(ed_name.getText().toString());

                    LogUtil.i(GlobalConsts.TAG,inCheck.toString());


                    sms_request.dorequest(RegisterActivity.this, sms_request.GET, inCheck,new RequestCB() {
                        @Override
                        public void respone(final int opcode,final Object obj) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(opcode == sms_request.GET){

                                        Info_check check = (Info_check)obj;

                                        if(check.getName() != null){

                                            Tools.showInfo(RegisterActivity.this,getResources().getString(R.string.chat_register_text_account_erro));

                                        }else{

                                            name = inCheck.getName();

                                        }
                                    }else if(opcode == sms_request.ERRO){
                                        Tools.showInfo(RegisterActivity.this,getResources().getString(R.string.chat_register_networkinfo_erro));
                                    }
                                }
                            });
                        }
                    });
                }

                break;*/

            default:
                break;
        }
    }


    //倒计时方法
    private void countDown(){
        new Thread(new  Runnable() {
            public void run() {
                while(time-- > 0){

                //    final String unReceive = RegisterActivity.this.getResources().getString(R.string.chat_register_button_verification_hint, time);

                    final String unReceive = String.valueOf(time);

                    runOnUiThread(new Runnable() {
                        public void run() {
                            bt_code.setText(Html.fromHtml(unReceive));
                            bt_code.setEnabled(false);
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        // TODO: handle exception
                        ExceptionUtil.handleException(e);
                    }
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        bt_code.setText(getResources().getString(R.string.chat_register_button_verification_erro));
                        bt_code.setEnabled(true);
                    }
                });

                time = RETRY_INTERVAL;
            }
        }).start();
    }

    public void getUserName(final Info_check inCheck) {


        sms_request.dorequest(RegisterActivity.this, sms_request.GET, inCheck,new RequestCB() {
            @Override
            public void respone(final int opcode,final Object obj) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(opcode == sms_request.GET){

                            Info_check check = (Info_check)obj;

                            if(check.getName() != null){

                                Tools.showInfo(RegisterActivity.this,getResources().getString(R.string.chat_register_text_account_erro));

                            }else{

                                name = inCheck.getName();

                                regiser();


                            }
                        }else if(opcode == sms_request.ERRO){
                            Tools.showInfo(RegisterActivity.this,getResources().getString(R.string.chat_register_networkinfo_erro));
                        }
                    }
                });
            }
        });
    }



    private boolean matchPhone(String text) {
        if (Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*").matcher(text).matches()) {
            return true;
        }
        return false;
    }
}
