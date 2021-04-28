package com.sms.app.interphone.ui.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.sms.app.interphone.R;
import com.sms.app.interphone.adapter.OffilneMapAdapter;
import com.sms.app.interphone.view.ListViewLinreLayout;

import java.util.ArrayList;


/**
 * 百度地图离线缓存Activity
 */

public class OffilneActivity extends BaseActivity implements MKOfflineMapListener,View.OnClickListener{

    private LinearLayout linearLayout_one,linearLayout_to;



    private Button[] btnArray= new Button[2];

    int currentIndex = 0;
    int selectedIndex = 0;

    private ListViewLinreLayout lv_one,lv_to;

    private LinearLayout delete_all;
    private OffilneMapAdapter adapter_one;
    private OffilneMapAdapter adapter_to;

    //one
    private MKOfflineMap mOffline = null;
    private ListView lv;

    private ImageView icon_return;

    /**
     * 已下载的离线地图信息列表
     */
    private ArrayList<MKOLUpdateElement> localMapList = null;

    private Offilne_Map_To_Adapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);

        mOffline = new MKOfflineMap();
        mOffline.init(this);
        setView();

        setListener();
    }

    private void setListener() {


        for (Button button : btnArray) {
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.offilne_button_1:

                            selectedIndex = 0;

                            if(selectedIndex != currentIndex){

                                linearLayout_one.setVisibility(View.VISIBLE);
                                linearLayout_to.setVisibility(View.GONE);

                                setIndex(selectedIndex);

                            }



                            break;
                        case R.id.offilne_button_2:

                            selectedIndex = 1;

                            if(selectedIndex != currentIndex){
                                linearLayout_one.setVisibility(View.GONE);
                                linearLayout_to.setVisibility(View.VISIBLE);

                                setIndex(selectedIndex);
                            }

                            break;

                        default:
                            break;
                    }

                }
            });
        }

    }


    private void setView() {

        btnArray[0] = (Button) findViewById(R.id.offilne_button_1);
        btnArray[1] = (Button) findViewById(R.id.offilne_button_2);

        btnArray[currentIndex].setSelected(true);

        linearLayout_one = (LinearLayout) findViewById(R.id.offilne_one);
        linearLayout_to = (LinearLayout) findViewById(R.id.offilne_to);

        delete_all = (LinearLayout) findViewById(R.id.delete_linear_layout);

        icon_return = (ImageView) findViewById(R.id.offilne_image_return);

        //one
        lv = (ListView) findViewById(R.id.open_listView);
        //to

        lv_one = (ListViewLinreLayout)findViewById(R.id.offilne_listView_1);
        lv_to= (ListViewLinreLayout)findViewById(R.id.offilne_listView_2);

        // 获取已下过的离线地图信息
        localMapList = mOffline.getAllUpdateInfo();
        if (localMapList == null) {
            localMapList = new ArrayList<MKOLUpdateElement>();
        }

        adapter = new Offilne_Map_To_Adapter();

        lv.setAdapter(adapter);

        // 获取热闹城市列表
        ArrayList<MKOLSearchRecord> records_one = mOffline.getHotCityList();

        ArrayList<MKOLSearchRecord> records_to = mOffline.getOfflineCityList();

        if(records_one != null){

            adapter_one = new OffilneMapAdapter(mOffline,OffilneActivity.this,records_one);

            lv_one.setAdapter(adapter_one);
        }

        if(records_to != null){

            adapter_to = new OffilneMapAdapter(mOffline,OffilneActivity.this,records_to);

            lv_to.setAdapter(adapter_to);
        }


        delete_all.setOnClickListener(this);
        icon_return.setOnClickListener(this);

    }

    /**
     * 更新状态显示
     */
    public void updateView() {
        localMapList = mOffline.getAllUpdateInfo();

        if (localMapList == null) {
            localMapList = new ArrayList<MKOLUpdateElement>();
        }

        adapter.notifyDataSetChanged();
        /*adapter_one.notifyDataSetChanged();
        adapter_to.notifyDataSetChanged();*/

    }

    @Override
    public void onGetOfflineMapState(int type, int state) {
        switch (type) {
            case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
                MKOLUpdateElement update = mOffline.getUpdateInfo(state);
                // 处理下载进度更新提示
                if (update != null) {

                     updateView();
                }
            }
            break;
            case MKOfflineMap.TYPE_NEW_OFFLINE:
                // 有新离线地图安装
                // LogUtil.i(GlobalConsts.TAG, String.format("add offlinemap num:%d", state));
                break;
            case MKOfflineMap.TYPE_VER_UPDATE:
                // 版本更新提示
                MKOLUpdateElement e = mOffline.getUpdateInfo(state);
                break;
            default:
                break;
        }
    }

    public void setIndex(int selectedIndex) {
        btnArray[selectedIndex].setSelected(true);
        btnArray[currentIndex].setSelected(false);

        currentIndex = selectedIndex;
    }




    /**
     * 离线地图管理列表适配器
     */
    public class Offilne_Map_To_Adapter extends BaseAdapter {


        @Override
        public int getCount() {
            return localMapList.size();
        }

        @Override
        public Object getItem(int index) {
            return localMapList.get(index);
        }

        @Override
        public long getItemId(int index) {
            return index;
        }


        @Override
        public View getView(int index, View view, ViewGroup arg2) {
            MKOLUpdateElement e = localMapList.get(index);
            view = View.inflate(OffilneActivity.this, R.layout.offline_localmap_item, null);
            initViewItem(view, e);
            return view;
        }

        void initViewItem(View view, final MKOLUpdateElement e) {

            ImageView remove = (ImageView) view.findViewById(R.id.remove);
            ImageView updatebutton = (ImageView) view.findViewById(R.id.updatebutton);

            TextView title = (TextView) view.findViewById(R.id.title);
            TextView update = (TextView) view.findViewById(R.id.update);
            TextView ratio = (TextView) view.findViewById(R.id.ratio);
            ratio.setText(e.ratio + "%");
            title.setText(e.cityName);

            if (e.update) {

                update.setText("点击可更新");
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOffline.update(e.cityID);
                        updateView();
                    }
                });
            } else {
                update.setText("最新");
            }

            if(e.status == 3){

                updatebutton.setVisibility(View.VISIBLE);

                updatebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOffline.start(e.cityID);
                        updateView();
                    }
                });
            }else{

                updatebutton.setVisibility(View.GONE);
            }

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    mOffline.remove(e.cityID);
                    updateView();
                }
            });
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.offilne_image_return:

                finish();

                break;

            case R.id.delete_linear_layout:

                if(localMapList != null){

                    final AlertDialog dialog = new AlertDialog.Builder(OffilneActivity.this).create();
                    View view = View.inflate(OffilneActivity.this, R.layout.dialog_warning_layout, null);
                    dialog.setView(view);
                    dialog.setInverseBackgroundForced(true);
                    final Button cancel = (Button) view.findViewById(R.id.button_cancel);
                    final Button ok = (Button) view.findViewById(R.id.button_ok);
                    final TextView title = (TextView) view.findViewById(R.id.title);
                    final TextView content = (TextView) view.findViewById(R.id.content);

                    title.setText(getResources().getString(R.string.sms_map_delete_offline_maps_title));
                    content.setText(getResources().getString(R.string.sms_map_delete_offline_maps_content));

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

                            for(MKOLUpdateElement cities : localMapList){
                                mOffline.remove(cities.cityID);
                                updateView();
                            }

                            dialog.cancel();

                        }
                    });
                    dialog.show();
                }

                break;

            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        /**
         * 退出时，销毁离线地图模块
         */
        mOffline.destroy();
        super.onDestroy();

    }
}
