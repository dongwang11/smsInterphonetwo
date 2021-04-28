package com.sms.app.interphone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.sms.app.interphone.R;
import com.sms.app.interphone.util.msnutil.Tools;

import java.util.ArrayList;
import java.util.List;

public class OffilneMapAdapter extends BaseAdapter {

    private Context mContext;
    private List<MKOLSearchRecord> mList;
    private MKOfflineMap offline;



    public OffilneMapAdapter(MKOfflineMap mOffline, Context context, List<MKOLSearchRecord> list) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.mList = list;
        this.offline = mOffline;
    }


    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        } else {
            return this.mList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (mList == null) {
            return null;
        } else {
            return this.mList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();

            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.offilne_one_item, null, false);

            holder.tv1 = (TextView) convertView.findViewById(R.id.textView3);
            holder.tv2 = (TextView) convertView.findViewById(R.id.textView1);
            holder.tv3 = (TextView) convertView.findViewById(R.id.textView7);

            holder.bt = (ImageView) convertView.findViewById(R.id.button6);



            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (this.mList != null) {
            final MKOLSearchRecord ak = mList.get(position);
            holder.tv1.setText(ak.cityName);
            holder.tv2.setText(formatDataSize(ak.dataSize));

            ArrayList<MKOLUpdateElement> localMapList = offline.getAllUpdateInfo();

            if(localMapList != null){
                for (MKOLUpdateElement mk :localMapList){
                    if(ak.cityID == mk.cityID){


                        if(mk.ratio < 100){

                         //   holder.tv3.setText(mk.ratio+"%");

                        }else{

                            holder.tv3.setVisibility(View.VISIBLE);

                            holder.tv3.setText("（已下载）");
                        }

                        break;
                    }else{
                        holder.tv3.setVisibility(View.GONE);
                    }
                }
            }

            holder.bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    offline.start(ak.cityID);
                    notifyDataSetChanged();
                    Tools.showInfo(mContext,"正在下载，请稍后");
                }
            });
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tv1, tv2,tv3;
        ImageView bt;
    }

    public String formatDataSize(long size) {
        String ret = "";
        if (size < (1024 * 1024)) {
            ret = String.format("%dK", size / 1024);
        } else {
            ret = String.format("%.1fM", size / (1024 * 1024.0));
        }
        return ret;
    }
}
