package com.sms.app.interphone.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.framework.dao.bean.DAOMesg;
import com.sms.app.framework.dao.bean.DAOmember;
import com.sms.app.framework.dao.bean.commom.DAOMesgDao;
import com.sms.app.framework.dao.bean.commom.DAOService;
import com.sms.app.framework.dao.bean.commom.DAOmemberDao;
import com.sms.app.interphone.R;
import com.sms.app.interphone.adapter.MessageAdapter;
import com.sms.app.interphone.entity.Mesage_entity;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends BaseActivity {


    private ListView listView;

    private ImageView img_return;

    private TextView titleText;

    private MessageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Intent intent = getIntent();
        String guoupName = intent.getStringExtra(GlobalConsts.GROUP_NAME);
        long groupId = intent.getLongExtra(GlobalConsts.GROUP_ID,0);
        LogUtil.i(GlobalConsts.TAG,"GROUP_ID:"+groupId);

        setView();
        setListViewData(groupId,guoupName);
        setListener();
    }

    private void setView() {

        listView = (ListView) findViewById(R.id.message_listView);

        titleText = (TextView) findViewById(R.id.message_title_text);

        img_return = (ImageView) findViewById(R.id.message_title_imageview1);
    }

    private void setListener() {

        img_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

    }


    public void setListViewData(long groupId, String guoupName) {

        try {
            List<Mesage_entity>  mesgList = new ArrayList<>();

            DAOMesgDao mMesg =  DAOService.get(getApplication()).getsession().getDAOMesgDao();

            List<DAOMesg> mesgs = mMesg.loadAll();

            DAOmemberDao memberDao = DAOService.get(getApplication()).getsession().getDAOmemberDao();

            List<DAOmember> member_list = memberDao.loadAll();


            titleText.setText(guoupName);

            for(DAOMesg m :mesgs){

                if(m.getFrom_id() == groupId){
                    Mesage_entity msg = new Mesage_entity();

                    msg.setId(m.getId());
                    msg.setContent(m.getContent());
                    msg.setMgid(m.getMgid());
                    msg.setContent_length(m.getContent_length());
                    msg.setCreate_time(m.getCreate_time().getTime());
                    msg.setFrom_id(m.getFrom_id());
                    msg.setUser_id(m.getUser_id());
                    msg.setMesg_type(m.getMesg_type());

                    for(DAOmember mem :member_list){

                        if(mem.getUser_id() == m.getUser_id()){
                            msg.setSex(mem.getSex());
                            msg.setAvatar_url(mem.getAvatar_url());
                            msg.setUser_neme(mem.getUser_name());
                        }
                    }

                    mesgList.add(msg);
                }

            }

            if(mesgList.size() > 0){

                adapter = new MessageAdapter(GroupActivity.this,mesgList);

                listView.setAdapter(adapter);

                listView.setSelection(adapter.getCount());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
