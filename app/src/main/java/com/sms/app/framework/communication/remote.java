package com.sms.app.framework.communication;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.google.gson.Gson;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.framework.respon;
import com.sms.app.framework.sms_request;
import com.sms.app.framework.trans.Post_obj;
import com.sms.app.framework.trans.bean.User;
import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.interphone.util.openutil.MD5Util;

import java.io.IOException;
import java.util.Arrays;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2017/3/14.
 */

public class remote {


  //  private String host= "http://192.168.1.60:8080/acount_server/";
    private String host= "http://smart.nitecore.com/";
  //    private String host= "http://47.75.81.85/";


  //  private String host= "http://120.25.125.223:8080/";

    private static String TAG = "smsfw_remote";
    public static remote remote_interface = null;
    private Context cnt = null;

    private OkHttpClient httpClient = new OkHttpClient();

    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    // Gson gson = new Gson();

    // private User remote_user = null;

    public  static String ERRO_AUTH = "com.sms.app.framework.trans.bean.Erro";

    public respon dorequest(final Post_obj obj)
    {

        if(obj==null)return null;
        final  respon rsp = new respon();
        String url = this.host+"User_login";

        //在remote层User是特别的java bean。需要根据opcode的内容来辨别是登录操作还是普通操作，非登录操作需要对passKey已经验证
        if( obj.getObj() instanceof User &&
                (sms_request.LOGIN == obj.getOpcode() ||
                        sms_request.REGISTER == obj.getOpcode()||
                        sms_request.RESET_PASSWD == obj.getOpcode()))
        {//如果是属于User bean，则代表进行登录操作，需要判断是账户密码登录还是passkey登录

            switch( obj.getOpcode())
            {
                case sms_request.LOGIN:

                    url = this.host+"User_login.action";

                    //登录注册将密码MD5加密

                    ((User) obj.getObj()).setPasswd(MD5Util.encodeString(((User) obj.getObj()).getPasswd()));


                    break;

                case sms_request.REGISTER:

                    url = this.host+"User_register.action";

                    //登录注册将密码MD5加密

                   ((User) obj.getObj()).setPasswd(MD5Util.encodeString(((User) obj.getObj()).getPasswd()));

                    break;

                case sms_request.RESET_PASSWD:

                    url = this.host+"User_forget_psswd.action";
                    break;

                default:
                    break;
            }
        }
        else
        {//根据对象自动生成请求的url地址
            String uri[]  = obj.getObj().getClass().toString().split("\\.");
            url = this.host+uri[uri.length-1]+".action";
        }

        Log.e(remote.TAG,"dorequest:"+url);

        RequestBody body = null;
        if((obj.getOpcode()!= sms_request.LOGIN  &&
                obj.getOpcode()!= sms_request.REGISTER &&
                obj.getOpcode()!= sms_request.RESET_PASSWD))
        {//非登录操作的请求需要在每次post中携带passkey

            SharedPreferences sharedPreferences = cnt.getSharedPreferences("user", Context.MODE_PRIVATE);
            String userString[] = new String[]{sharedPreferences.getString("id", ""), sharedPreferences.getString("passkey", "")};

            LogUtil.i(GlobalConsts.TAG, "passkey:"+Arrays.toString(userString));

            body = new FormBody.Builder()
                    .add("id", userString[0]+"")
                    .add("passkey", userString[1])
                    .add("post_obj", gson.toJson(obj))
                    .build();
        }else{

            body = new FormBody.Builder()
                    .add("post_obj", gson.toJson(obj))
                    .build();
        }



        if(body != null){

            Log.e(TAG,"post_obj:" + body.toString());

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            final Call call = httpClient.newCall(request);

            try {

                Response response = call.execute();

                ResponseBody reBody = response.body();

                String s = reBody.string();

                Log.e(remote.TAG,"Response:"+s);

                if (response.code() == 200) {

                    Post_obj obj_response = (Post_obj) gson.fromJson(s, Post_obj.class);

                    if(obj_response == null)
                    {
                        Log.e(remote.TAG,"onResponse is null");
                    }
                //    Log.e(remote.TAG,"onResponse:"+obj_response);

                    String class_str = obj_response.getClass_str();
                    if(remote.ERRO_AUTH.equals(class_str))
                    {//说明，账户登录信息过期或者密码错误。
                        rsp.setOpcode(sms_request.ERRO);//初始化返回上层的值
                        rsp.setObj(1);
                        /*Object sobj = null;

                        try {
                            String sub_obj =  gson.toJson(obj_response.getObj());

                            sobj = gson.fromJson(sub_obj, Class.forName(obj_response.getClass_str()));//把子对象json转成实例，这样做是借助gson的内部反射创建实例，如果有必要也可以改成自己创建
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                        rsp.setObj(sobj);*/
                        Log.e(remote.TAG,"remote return erro");
                    }
                    else
                    {
                        String sub_obj =  gson.toJson(obj_response.getObj());//把post对象里面的子对象转成json
                        Object sobj = null;

                        try {
                            sobj = gson.fromJson(sub_obj, Class.forName(obj_response.getClass_str()));//把子对象json转成实例，这样做是借助gson的内部反射创建实例，如果有必要也可以改成自己创建
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        rsp.setOpcode(obj_response.getOpcode());//初始化返回上层的值
                        rsp.setObj(sobj);

                        //如果返回User对象，更新本地缓存Passkey

                        if(sobj.getClass().getName().equals("com.sms.app.framework.trans.bean.User")){

                            Log.e(TAG,"如果返回User对象，更新本地缓存Passkey");
                            User user = (User) sobj;

                            if(user.getPasskey() != null){

                                if(obj.getOpcode() != sms_request.REGISTER){

                                    // 存入本地SharedPreferences
                                    SharedPreferences sharedPreferences = cnt.getSharedPreferences("user", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();

                                    editor.putString("id", String.valueOf(user.getId()));
                                    editor.putString("passkey", user.getPasskey());
                                    editor.commit();

                                }


                            }

                        }

                    }

                }else{

                    Log.e(remote.TAG,"OkHttpError request.ERRO:");

                    rsp.setOpcode(sms_request.ERRO);//初始化返回上层的值
                    rsp.setObj(1);

                }

            } catch (IOException e) {

                Log.e(remote.TAG,"OkHttpError sms_request.ERRO:");

                rsp.setOpcode(sms_request.ERRO);//初始化返回上层的值
                rsp.setObj(2);

                e.printStackTrace();

            }

        }


            /*//初始化volley开源框架的请求队列

            RequestQueue requestQueue = Volley.newRequestQueue(cnt);

            StringRequest stringRequest =  new StringRequest( Request.Method.POST,url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    Gson gson =  new Gson();
                    Log.e(remote.TAG,"volley onResponse："+s);
                    Post_obj obj = (Post_obj) gson.fromJson(s, Post_obj.class);
                    if(obj == null)
                    {
                        Log.e(remote.TAG,"onResponse is null");
                    }
                    String class_str = obj.getClass_str();
                    if(remote.ERRO_AUTH.equals(class_str))
                    {//说明，账户登录信息过期或者密码错误。
                        rsp.setOpcode(sms_request.ERRO);//初始化返回上层的值
                        rsp.setObj(new Object());
                        Log.e(remote.TAG,"remote return erro");
                    }
                    else
                    {
                        String sub_obj =  gson.toJson(obj.getObj());//把post对象里面的子对象转成json
                       Object sobj = null;
                        try {
                            sobj = gson.fromJson(sub_obj, Class.forName(obj.getClass_str()));//把子对象json转成实例，这样做是借助gson的内部反射创建实例，如果有必要也可以改成自己创建
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        rsp.setOpcode(obj.getOpcode());//初始化返回上层的值
                        rsp.setObj(sobj);
                        //如果是登录操作返回成功，则本地保存一个远程对象的备份
                        *//*if(((obj.getOpcode()) == sms_request.LOGIN ) &&(User.class.toString().substring(6)).equals(obj.getClass_str()))
                        {//保留一个远程账户信息的备份，用于后面自动传输passkey
                            remote_user =  gson.fromJson(gson.toJson(sobj),User.class);
                            Log.e(remote.TAG,remote_user.toString());
                        }*//*
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyError.printStackTrace();
                    Log.e(remote.TAG,"VolleyError sms_request.ERRO:");
                    rsp.setOpcode(sms_request.ERRO);//初始化返回上层的值
                    rsp.setObj(new Object());
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> param = null;
                    param = new HashMap<String,String>();

                    if((obj.getOpcode()!= sms_request.LOGIN  &&
                        obj.getOpcode()!= sms_request.REGISTER &&
                        obj.getOpcode()!= sms_request.RESET_PASSWD))
                    {//非登录操作的请求需要在每次post中携带passkey

                        SharedPreferences sharedPreferences = cnt.getSharedPreferences("user", Context.MODE_PRIVATE);
                        String userString[] = new String[]{sharedPreferences.getString("id", ""), sharedPreferences.getString("passkey", "")};

                        LogUtil.i(GlobalConsts.TAG, Arrays.toString(userString));

                        param.put("id",userString[0]+"");
                        param.put("passkey",userString[1]);
                    }
                    param.put("post_obj",new Gson().toJson(obj));

                    Log.e(remote.TAG,"param:"+param.toString());
                    return param;
                }
            };



            requestQueue.add(stringRequest);*/
        /*try {
            while(rsp.getObj() == null) {
                Thread.sleep(50);
            }

        } catch (InterruptedException e) {
            Log.e(remote.TAG,"excetion");
            e.printStackTrace();
        }*/
        Log.e(remote.TAG,"return");
        return rsp;
    }

    public remote(Context cnt) {
        this.cnt = cnt;
    }

    public remote() {

    }

    public static remote get_remote(Context cnt)
    {
        if(remote.remote_interface==null)
        {
            remote.remote_interface = new remote(cnt);
        }

        return remote.remote_interface;
    }

}
