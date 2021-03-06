package com.sms.app.interphone.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.framework.dao.bean.DAOGroup;
import com.sms.app.framework.dao.bean.DAOMesg;
import com.sms.app.framework.dao.bean.DAOmember;
import com.sms.app.framework.dao.bean.commom.DAOGroupDao;
import com.sms.app.framework.dao.bean.commom.DAOMesgDao;
import com.sms.app.framework.dao.bean.commom.DAOService;
import com.sms.app.framework.dao.bean.commom.DAOmemberDao;
import com.sms.app.interphone.R;
import com.sms.app.interphone.adapter.HistruyGroupAdapter;
import com.sms.app.interphone.util.interfaces.NoDoubleClickListener;
import com.sms.app.interphone.util.msnutil.ExceptionUtil;
import com.sms.app.interphone.util.msnutil.Tools;

import java.util.List;

public class HistruyGroupActivity extends  BaseActivity  {

    private static String TAG = "YanShi...Log - HistruyGroupActivity";

    private ListView lv;

    private DAOGroupDao groupDao = null;

    private DAOMesgDao mesgDao = null;

    private DAOmemberDao omemberDao = null;


    private LinearLayout delete_layout;


    private HistruyGroupAdapter adapter = null;

    private List<DAOGroup>  groupList = null;
    //返回
    private ImageView img_return;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histruy_group);

        setView();

        setListener();


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setView() {

        lv = (ListView) findViewById(R.id.histrny_group_listView);

        delete_layout = (LinearLayout) findViewById(R.id.delete_linear_layout);

        img_return = (ImageView) findViewById(R.id.settings_image1);


    }

    private void setListener() {


        delete_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog dialog = new AlertDialog.Builder(HistruyGroupActivity.this).create();
                View view = View.inflate(HistruyGroupActivity.this, R.layout.dialog_warning_layout, null);
                dialog.setView(view);
                dialog.setInverseBackgroundForced(true);
                dialog.setCancelable(false);
                final Button cancel = (Button) view.findViewById(R.id.button_cancel);
                final Button ok = (Button) view.findViewById(R.id.button_ok);
                final TextView title = (TextView) view.findViewById(R.id.title);
                final TextView content = (TextView) view.findViewById(R.id.content);

                title.setText(getResources().getString(R.string.sms_group_delete_title));
                content.setText(getResources().getString(R.string.sms_group_delete_content));

                //dialog背景透明

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

                        Tools.showProgressDialog(HistruyGroupActivity.this,"正在删除");

                        new AsyncTask<Void,Void,Boolean>(){

                            @Override
                            protected Boolean doInBackground(Void... params) {

                                try {

                                    long from_id = 0;

                                    if(groupDao != null){

                                        List<DAOGroup> groupLists = groupDao.loadAll();

                                        for (DAOGroup group : groupLists){

                                            if(group.getIs_avtive()){

                                                from_id = group.getGroupId();

                                            }else{

                                                groupDao.deleteByKey(group.getId());

                                            }
                                        }
                                    }

                                    if(omemberDao != null){

                                        List<DAOmember>  memberList = omemberDao.loadAll();

                                        String a = String.valueOf(from_id);

                                        if(from_id != 0){

                                            for(DAOmember m : memberList){


                                                if(!m.getGroup_name().equals(a)){

                                                    omemberDao.deleteByKey(m.getId());

                                                }
                                            }
                                        }
                                    }



                                    if(mesgDao != null){

                                        List<DAOMesg>  mesgList = mesgDao.loadAll();

                                        if(from_id != 0){

                                            for(DAOMesg m : mesgList){

                                                if(m.getFrom_id() != from_id){

                                                    mesgDao.deleteByKey(m.getId());

                                                }
                                            }
                                        }
                                    }

                                } catch (Exception e) {
                                    ExceptionUtil.handleException(e);
                                }

                                return true;
                            }

                            @Override
                            protected void onPostExecute(Boolean b) {

                                Tools.closeProgressDialog();

                                if(adapter != null){

                                    groupList.clear();

                                    adapter.notifyDataSetChanged();

                                }

                            }

                        }.execute();

                        dialog.cancel();

                    }
                });
                dialog.show();

            }
        });




        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(groupList != null){

                    Intent intent = new Intent(HistruyGroupActivity.this,GroupActivity.class);

                    intent.putExtra(GlobalConsts.GROUP_NAME,groupList.get(position).getName());
                    intent.putExtra(GlobalConsts.GROUP_ID,groupList.get(position).getGroupId());


                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                }
            }
        });

        img_return.setOnClickListener(new NoDoubleClickListener(){
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

        setDate();
    }

    private void setDate() {

        if(groupDao == null)
        {
            groupDao =  DAOService.get(getApplication()).getsession().getDAOGroupDao();

        }


        if(mesgDao == null){

            mesgDao = DAOService.get(getApplication()).getsession().getDAOMesgDao();
        }

        if(omemberDao == null){

            omemberDao = DAOService.get(getApplication()).getsession().getDAOmemberDao();
        }

        groupList = groupDao.loadAll();

        for(DAOGroup group : groupList){

            if(group.getIs_avtive()){

                groupList.remove(group);

            }

        }

        if(adapter == null)
        {
            adapter = new HistruyGroupAdapter(this,groupList);
            lv.setAdapter(adapter);
        }
        else
        {
            adapter.notifyDataSetChanged();
        }


    }
}
