package com.sms.app.interphone.adapter;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sms.app.interphone.R;
import com.sms.app.interphone.ui.activity.BlescanActivity;

import java.util.List;

public class BleListViewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private BlescanActivity mContext;
    private List<BluetoothDevice> mList;

    public BleListViewAdapter(BlescanActivity mContext, List<BluetoothDevice> dd) {
        // TODO Auto-generated constructor stub
        this.mContext = mContext;
        this.mList = dd;
        this.inflater = LayoutInflater.from(mContext);
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
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.bluetooth_device_item, null, false);
            holder.tv1 = (TextView) convertView.findViewById(R.id.textView3);
            holder.tv2 = (TextView) convertView.findViewById(R.id.textView4);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            if (this.mList != null) {

                String title = null;


                if(mList.get(position).getName().length() > BlescanActivity.NITECORE.length()){

                    title = mList.get(position).getName().substring(BlescanActivity.NITECORE.length(),mList.get(position).getName().length());

                }else{

                    title = mList.get(position).getName();
                }

                holder.tv1.setText(title);

                holder.tv2.setText(mList.get(position).getAddress());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tv1,tv2;
    }

}