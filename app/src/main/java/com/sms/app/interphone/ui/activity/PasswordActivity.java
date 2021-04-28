package com.sms.app.interphone.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sms.app.framework.RequestCB;
import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.framework.sms_request;
import com.sms.app.framework.trans.bean.User;
import com.sms.app.interphone.R;
import com.sms.app.interphone.util.interfaces.NoDoubleClickListener;
import com.sms.app.interphone.util.msnutil.Tools;

public class PasswordActivity extends BaseActivity {

    private EditText ed_E_mail;
    private Button bt_send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        setView();
    }

    private void setView() {

        ed_E_mail = (EditText) findViewById(R.id.chat_password_edtext_email);

        bt_send = (Button) findViewById(R.id.chat_password_button_ok);

        bt_send.setOnClickListener(new NoDoubleClickListener(){
            @Override
            public void onNoDoubleClick(View v) {
            String user_E_mail = ed_E_mail.getText().toString();

                if (!TextUtils.isEmpty(ed_E_mail.getText().toString())) {

                    Tools.showProgressDialog(PasswordActivity.this,getResources().getString(R.string.chat_login_text_password));

                    User user = new User();

                    user.setE_mail(user_E_mail);

                    sms_request.dorequest(getApplicationContext(), sms_request.RESET_PASSWD, user, new RequestCB() {
                        @Override
                        public void respone(int opcode, Object obj) {
                            LogUtil.i(GlobalConsts.TAG,"opcode:"+opcode);

                            Tools.closeProgressDialog();
                            if(opcode == sms_request.RESET_PASSWD){

                                User user = (User) obj;

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Tools.showInfo(PasswordActivity.this,getResources().getString(R.string.chat_login_text_hint_Login_password));
                                    }
                                });

                                LogUtil.i(GlobalConsts.TAG,"User:"+user.toString());
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Tools.showInfo(PasswordActivity.this,getResources().getString(R.string.chat_login_text_hint_erro));
                                    }
                                });
                            }
                        }
                    });

                }else{

                    Tools.showInfo(PasswordActivity.this,getResources().getString(R.string.chat_register_text_email_erro));

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
}
