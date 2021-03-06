package com.sms.app.interphone.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sms.app.framework.RequestCB;
import com.sms.app.framework.communication.localayer.attr_listenner;
import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.framework.dao.bean.DAOUser;
import com.sms.app.framework.dao.bean.commom.DAOService;
import com.sms.app.framework.dao.bean.commom.DAOUserDao;
import com.sms.app.framework.sms_request;
import com.sms.app.framework.trans.bean.Avatar;
import com.sms.app.framework.trans.bean.Interphone;
import com.sms.app.framework.trans.bean.User;
import com.sms.app.framework.trans.sms_fw_api;
import com.sms.app.interphone.R;
import com.sms.app.interphone.services.Op_request;
import com.sms.app.interphone.services.Open_biz;
import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.interphone.util.frequentlyutil.NetWorkUtil;
import com.sms.app.interphone.util.frequentlyutil.SharePreferenceConstant;
import com.sms.app.interphone.util.frequentlyutil.SharePreferenceUtil;
import com.sms.app.interphone.util.interfaces.NoDoubleClickListener;
import com.sms.app.interphone.util.msnutil.ExceptionUtil;
import com.sms.app.interphone.util.msnutil.Tools;
import com.sms.app.interphone.util.openutil.Openfrom;

import java.io.IOException;

/**
 *
 * open fire 登录Activity
 *
 */

public class LoginActivity extends  BaseActivity  {

    private EditText ed_name, ed_password;
    private LinearLayout ll_login, ll_registered;
    private TextView forgot_password;
    private ImageView iv_login;

//    private DAOmemberDao memberDao;
    private DAOUserDao userDao;

    private static String TAG = "YanShi...Log - LoginActivity:";


    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.RECORD_AUDIO",
            "android.permission.CAMERA"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setView();
        setListener();

        userDao = DAOService.get(getApplication()).getsession().getDAOUserDao();




    }

    private void setView() {

        ed_name = (EditText) findViewById(R.id.chat_login_edtext_name);
        ed_password = (EditText) findViewById(R.id.chat_login_edtext_passwd);
        forgot_password = (TextView) findViewById(R.id.chat_login_forgot_password);

        //监听输入框，只允许输入字符及数字
    //    ed_name.addTextChangedListener(new SearchWather(ed_name));

        ll_login = (LinearLayout) findViewById(R.id.ll_login);
        ll_registered = (LinearLayout) findViewById(R.id.ll_registered);
        iv_login= (ImageView) findViewById(R.id.iv_login);


        ed_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    ed_password.setText(null);
                } else {
                    // 此处为失去焦点时的处理内容

                }
            }
        });


        String loginname = SharePreferenceUtil.getStringDataByKe(LoginActivity.this, SharePreferenceConstant.LOGINNAME, "");
        ed_name.setText(loginname);
        String loginpassword = SharePreferenceUtil.getStringDataByKe(LoginActivity.this, SharePreferenceConstant.LOGINPASSWORD, "");
        ed_password.setText(loginpassword);

    }



    private void setListener() {


        ll_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if(NetWorkUtil.isNetworkAvailable(LoginActivity.this)){//网络判断
                    Toast.makeText(LoginActivity.this, "请连接有效网络", Toast.LENGTH_LONG).show();
                    return;
                }*/
                final String username = ed_name.getText().toString();
                String password = ed_password.getText().toString();

                // 验证用户的输入是否合法
                StringBuilder builder = new StringBuilder();
                if (TextUtils.isEmpty(username)) {
                    builder.append(getResources().getString(R.string.chat_login_text_hint_account));
                }
                if (TextUtils.isEmpty(password)) {
                    builder.append(getResources().getString(R.string.chat_login_text_hint_password));
                }



                if (TextUtils.isEmpty(builder.toString())) {
                    // 验证通过
                    // 不能再单击

                    Tools.showProgressDialog(LoginActivity.this,getResources().getString(R.string.chat_login_text_hint_login));

                    final User user = new User();

                    user.setE_mail(username);
                    user.setPasswd(password);


                    sms_request.dorequest(getApplicationContext(), sms_request.LOGIN, user, new RequestCB() {
                        @Override
                        public void respone(int opcode, final Object obj) {

                            try {

                                if(opcode == sms_request.LOGIN){

                                    MyApplicatoin.evenUser = (User) obj;

                                    Log.e(GlobalConsts.TAG,"思脉时服务器登录成功："+MyApplicatoin.evenUser.toString());
                                    Log.e(GlobalConsts.TAG,"思脉时服务器登录成功："+MyApplicatoin.evenUser.getIm().toString());


                                    final DAOUser daoUser = userDao.queryBuilder().where(DAOUserDao.Properties.Remote_id.eq(MyApplicatoin.evenUser.getId())).orderAsc(DAOUserDao.Properties.Remote_id).unique();

                                    //如果本地数据库查询为空
                                    if (daoUser == null) {

                                        final DAOUser users = new DAOUser();
                                        users.setRemote_id(MyApplicatoin.evenUser.getId());
                                        users.setName(MyApplicatoin.evenUser.getName());
                                        users.setNumber(MyApplicatoin.evenUser.getNumber());
                                        users.setE_mail(MyApplicatoin.evenUser.getE_mail());
                                        users.setSex(MyApplicatoin.evenUser.getSex());
                                        users.setVersion(MyApplicatoin.evenUser.getVersion());
                                        users.setLast_login_time(MyApplicatoin.evenUser.getLast_login_time());
                                        users.setRegister_time(MyApplicatoin.evenUser.getRegister_time());

                                        Avatar avatar = new Avatar();
                                        avatar.setUser_id(MyApplicatoin.evenUser.getId());

                                        LogUtil.i(TAG,"evenUserID:"+MyApplicatoin.evenUser.getId());

                                        sms_request.dorequest(LoginActivity.this, sms_request.GET, avatar, new RequestCB() {
                                            @Override
                                            public void respone(int opcode, Object obj) {
                                                if (opcode == sms_request.GET) {
                                                    Avatar s = (Avatar) obj;
                                                    LogUtil.i(TAG,"Avatar:"+s.toString());

                                                    try {
                                                        if (s.getUrl() != null && s.getData() != null) {

                                                            String icon_path = Tools.writeToSdcard(LoginActivity.this, "user", s.getUser_id().toString(),s.getData());

                                                            MyApplicatoin.evenUser.setAvatar_url(icon_path);

                                                            users.setAvatar_url(icon_path);

                                                        }

                                                        userDao.insert(users);

                                                                /*DAOmember member = new DAOmember();

                                                                member.setUser_name(MyApplicatoin.evenUser.getName());
                                                                member.setUser_id(MyApplicatoin.evenUser.getId());
                                                                member.setVersion(MyApplicatoin.evenUser.getVersion());
                                                                member.setSex(MyApplicatoin.evenUser.getSex());
                                                                member.setAvatar_url(MyApplicatoin.evenUser.getAvatar_url());

                                                                memberDao.insert(member);*/

                                                        Login_Openfire();

                                                    } catch (IOException e) {

                                                        ExceptionUtil.handleException(e);

                                                    }

                                                }else{

                                                    Login_Openfire();

                                                }
                                            }
                                        });


                                    }else if(daoUser != null && daoUser.getVersion() != MyApplicatoin.evenUser.getVersion()){

                                        //如果本地数据库不为空，并且版本不一致

                                        final DAOUser User = new DAOUser();

                                        User.setId(daoUser.getId());
                                        User.setRemote_id(MyApplicatoin.evenUser.getId());
                                        User.setName(MyApplicatoin.evenUser.getName());
                                        User.setNumber(MyApplicatoin.evenUser.getNumber());
                                        User.setE_mail(MyApplicatoin.evenUser.getE_mail());
                                        User.setSex(MyApplicatoin.evenUser.getSex());
                                        User.setVersion(MyApplicatoin.evenUser.getVersion());
                                        User.setLast_login_time(MyApplicatoin.evenUser.getLast_login_time());
                                        User.setRegister_time(MyApplicatoin.evenUser.getRegister_time());

                                        //发请求，获取头像

                                        Avatar avatar = new Avatar();
                                        avatar.setUser_id(MyApplicatoin.evenUser.getId());

                                        LogUtil.i(TAG,"evenUserID:"+MyApplicatoin.evenUser.getId());

                                        sms_request.dorequest(LoginActivity.this, sms_request.GET, avatar, new RequestCB() {
                                            @Override
                                            public void respone(int opcode, Object obj) {
                                                if (opcode == sms_request.GET) {
                                                    Avatar s = (Avatar) obj;
                                                    LogUtil.i(TAG,"Avatar:"+s.toString());

                                                    try {
                                                        if (s.getUrl() != null && s.getData() != null) {

                                                            String icon_path = Tools.writeToSdcard(LoginActivity.this, "user", s.getUser_id().toString(),s.getData());

                                                            MyApplicatoin.evenUser.setAvatar_url(icon_path);
                                                            User.setAvatar_url(icon_path);

                                                        }

                                                        userDao.update(User);

                                                                /*DAOmember member = new DAOmember();

                                                                member.setUser_name(MyApplicatoin.evenUser.getName());
                                                                member.setUser_id(MyApplicatoin.evenUser.getId());
                                                                member.setVersion(MyApplicatoin.evenUser.getVersion());
                                                                member.setSex(MyApplicatoin.evenUser.getSex());
                                                                member.setAvatar_url(MyApplicatoin.evenUser.getAvatar_url());

                                                                memberDao.insert(member);*/

                                                        Login_Openfire();

                                                    } catch (IOException e) {
                                                        ExceptionUtil.handleException(e);
                                                    }
                                                }
                                            }
                                        });

                                    }else{

                                        // 查询本地数据库不为空，并且版本一致

                                        MyApplicatoin.evenUser.setAvatar_url(daoUser.getAvatar_url());
                                        LogUtil.i(TAG,"Avatar:"+daoUser.getAvatar_url());

                                        // 登录openfire 服务器
                                        Login_Openfire();

                                    }

                                    //处理两次登录跳转
                                    String loginstate = SharePreferenceUtil.getStringDataByKe(LoginActivity.this, SharePreferenceConstant.LOGINSTATE, "");
                                    String loginuser = SharePreferenceUtil.getStringDataByKe(LoginActivity.this, SharePreferenceConstant.LOGINUSER, "");
                                    if ("loginstate".equals(loginstate) || loginstate == "loginstate") {
                                        finish();
                                    } else {
                                        SharePreferenceUtil.saveStringDataToSharePreference(LoginActivity.this, SharePreferenceConstant.LOGINSTATE, "loginstate");//记录是否已经登录状态
                                        if ("loginuser".equals(loginuser) || loginuser == "loginuser") {
                                            SharePreferenceUtil.saveStringDataToSharePreference(LoginActivity.this, SharePreferenceConstant.LOGINUSER, "");
                                            String login = SharePreferenceUtil.getStringDataByKe(LoginActivity.this, SharePreferenceConstant.LOGIN, "");
                                            if("login".equals(login) || login == "login"){
                                                Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                                                startActivity(intent);
                                            }
                                        }else{
                                            SharePreferenceUtil.saveStringDataToSharePreference(LoginActivity.this, SharePreferenceConstant.LOGIN, "login");
                                            Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                                    SharePreferenceUtil.saveStringDataToSharePreference(LoginActivity.this, SharePreferenceConstant.LOGINNAME, ed_name.getText().toString());//保存名字
                                    SharePreferenceUtil.saveStringDataToSharePreference(LoginActivity.this, SharePreferenceConstant.LOGINPASSWORD, ed_password.getText().toString());
                                    finish();
                                }else if(opcode == sms_request.ERRO){

                                    Tools.closeProgressDialog();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Tools.showInfo(LoginActivity.this,getResources().getString(R.string.chat_login_text_hint_login_erro));
                                        }
                                    });


                                            /*int code = (int) obj;

                                            // 如果是连接异常
                                            if(code == 2){

                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Tools.showInfo(LoginActivity.this,getResources().getString(R.string.chat_login_text_hint_login_erro));
                                                    }
                                                });

                                            }else if(code == 1){

                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Tools.showInfo(LoginActivity.this,getResources().getString(R.string.chat_login_text_hint_login_erro_hite));
                                                    }
                                                });

                                            }*/


                                }
                            } catch (Exception e) {

                                ExceptionUtil.handleException(e);

                            }
                        }
                    });

                } else {
                    Toast.makeText(LoginActivity.this, builder.toString(),
                            Toast.LENGTH_LONG).show();
                }


            }

        });


        //进入注册页面

        ll_registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });


        //进入找回密码页面
        forgot_password.setOnClickListener(new NoDoubleClickListener(){
            @Override
            public void onNoDoubleClick(View v) {
                startActivity(new Intent(LoginActivity.this, PasswordActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });

        iv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = SharePreferenceUtil.getStringDataByKe(LoginActivity.this, SharePreferenceConstant.LOGIN, "");
                if("login".equals(login) || login == "login"){
                    Intent intent = new Intent(LoginActivity.this, SilpLoginActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(LoginActivity.this, DeviceActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String login = SharePreferenceUtil.getStringDataByKe(LoginActivity.this, SharePreferenceConstant.LOGIN, "");
            if ("login".equals(login) || login == "login") {
                Intent intent = new Intent(LoginActivity.this, SilpLoginActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(LoginActivity.this, DeviceActivity.class);
                startActivity(intent);
            }
            finish();

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     *
     * 将从服务器获取的账号密码登录至openfire 服务器
     *
     * */

    int code = 0;

    private void Login_Openfire() {

        code = 0;

        try {
            Openfrom openuser = new Openfrom();

            openuser.setUserId(MyApplicatoin.evenUser.getIm().getUser_id());
            openuser.setPassword(MyApplicatoin.evenUser.getIm().getIm_passwd());
            openuser.setCode(Open_biz.Login);

            if(MyApplicatoin.interphone != null){

                if(MyApplicatoin.interphone.getImCode() != null){

                    if(MyApplicatoin.interphone.getImCode().getId() != 0){

                        openuser.setFromName(Tools.getGroupName(MyApplicatoin.interphone.getImCode().getName()));

                    }


                }
            }

            Log.e(GlobalConsts.TAG,"Login_Openfire:"+openuser.toString());

            sms_fw_api.do_request(LoginActivity.this,Open_biz.Login, openuser, new RequestCB() {
                @Override
                public void respone(int opcode, Object obj) {

                }

            });
            sms_fw_api.add_open_listenner(LoginActivity.this, new attr_listenner() {
                @Override
                public void onAttributeChang(int objcode, Object object) {

                    if(objcode == Open_biz.Login){

                        byte type = (byte) object;

                        Log.e(GlobalConsts.TAG,"openfire_respone_objcode:"+objcode+",type:"+type);

                        if(type == Open_biz.YES){


                            code = 1;

                        }else{
                            LogUtil.i(TAG,"登录失败");
                        }
                    }else if(objcode == Open_biz.Register){
                        byte type = (byte) object;

                        if(type == Open_biz.NO){
                            LogUtil.i(TAG,"注册失败");

                        //    MyApplicatoin.evenUser = null;
                        }

                    }else if(objcode == Open_biz.Connection){

                        byte type = (byte) object;

                        if(type == Open_biz.NO){
                            LogUtil.i(TAG,"连接失败");
                        //    MyApplicatoin.evenUser = null;
                        }

                    }


                }
            });


            Tools.closeProgressDialog();

            if(MyApplicatoin.IS_BLE){

                if(MyApplicatoin.interphone.getUserId() != MyApplicatoin.evenUser.getId()){


                    final Interphone inter = new Interphone();

                    inter.setUserId(MyApplicatoin.evenUser.getId());

                    MyApplicatoin.setInterphone(inter);

                    MyApplicatoin.interphone.setUserId(MyApplicatoin.evenUser.getId());

                }


            }


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Tools.showInfo(LoginActivity.this,getResources().getString(R.string.chat_login_text_hint_Login_successful));
                    MyApplicatoin.isToos = false;

                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });

            /*Open_biz.dorequest(LoginActivity.this, openuser, new Op_request() {
                @Override
                public void respone(byte objcode, Object obj) {


                    if(objcode == Open_biz.Login){

                        byte type = (byte) obj;

                        Log.e(GlobalConsts.TAG,"openfire_respone_objcode:"+objcode+",type:"+type);

                        if(type == Open_biz.YES){


                            Tools.closeProgressDialog();

                            if(MyApplicatoin.IS_BLE){

                                if(MyApplicatoin.interphone.getUserId() != MyApplicatoin.evenUser.getId()){

                                    *//**
                                     * 设置对讲机用户ID
                                     * *//*

                                    final Interphone inter = new Interphone();

                                    inter.setUserId(MyApplicatoin.evenUser.getId());

                                    MyApplicatoin.setInterphone(inter);

                                    MyApplicatoin.interphone.setUserId(MyApplicatoin.evenUser.getId());

                                }


                            }


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Tools.showInfo(LoginActivity.this,getResources().getString(R.string.chat_login_text_hint_Login_successful));
                                    finish();
                                }
                            });

                            code = 1;

                        }else{
                            LogUtil.i(TAG,"登录失败");
                        }
                    }else if(objcode == Open_biz.Register){

                        byte type = (byte) obj;

                        if(type == Open_biz.NO){
                            LogUtil.i(TAG,"注册失败");

                            MyApplicatoin.evenUser = null;
                        }

                    }else if(objcode == Open_biz.Connection){

                        byte type = (byte) obj;

                        if(type == Open_biz.NO){
                            LogUtil.i(TAG,"连接失败");
                            MyApplicatoin.evenUser = null;
                        }

                    }


                }
            });*/
        } catch (Resources.NotFoundException e) {
            ExceptionUtil.handleException(e);
        }

    }

    @Override
    protected void onDestroy() {
        Tools.closeProgressDialog();
        super.onDestroy();
    }




}
