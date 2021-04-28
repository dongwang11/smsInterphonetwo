package com.sms.app.interphone.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import com.sms.app.interphone.util.msnutil.ExceptionUtil;

import java.io.File;
import java.util.List;

public class HistruyGroupAdapter extends BaseAdapter {

    private Context mContext;
    private List<DAOGroup> mList;
    private DAOGroupDao groupDao;
    private DAOMesgDao mesgDao;
    private DAOmemberDao omemberDao;


    public HistruyGroupAdapter(Context context, List<DAOGroup> mList) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.mList = mList;
        this.groupDao = DAOService.get(context).getsession().getDAOGroupDao();;
        this.mesgDao = DAOService.get(context).getsession().getDAOMesgDao();;
        this.omemberDao = DAOService.get(context).getsession().getDAOmemberDao();;

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
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.histruy_group_item, null, false);
            holder.tv1 = (TextView) convertView.findViewById(R.id.textView1);
            holder.img = (ImageView) convertView.findViewById(R.id.imageView_delete);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (this.mList != null) {
            final DAOGroup group = mList.get(position);

            holder.tv1.setText(group.getName());


            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
                    View view = View.inflate(mContext, R.layout.dialog_warning_layout, null);
                    dialog.setView(view);
                    dialog.setInverseBackgroundForced(true);
                    dialog.setCancelable(false);
                    final Button cancel = (Button) view.findViewById(R.id.button_cancel);
                    final Button ok = (Button) view.findViewById(R.id.button_ok);
                    final TextView title = (TextView) view.findViewById(R.id.title);
                    final TextView content = (TextView) view.findViewById(R.id.content);

                    title.setText(mContext.getResources().getString(R.string.sms_tracking_delete_title));
                    content.setText(mContext.getResources().getString(R.string.sms_tracking_delete_content));

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


                            if(groupDao != null)
                            {

                                new AsyncTask<Void,Void,Boolean>(){

                                    @Override
                                    protected Boolean doInBackground(Void... params) {

                                        try {
                                            mList.remove(position);

                                            groupDao.deleteByKey(group.getId());

                                            List<DAOMesg> mesgList = mesgDao.loadAll();

                                            for(DAOMesg mesg : mesgList)
                                            {
                                                if(mesg.getFrom_id() == group.getGroupId())
                                                {
                                                    mesgDao.deleteByKey(mesg.getId());
                                                }
                                            }


                                            List<DAOmember> memberList = omemberDao.loadAll();

                                            for(DAOmember member : memberList)
                                            {

                                                if (member.getGroup_name().equals(group.getName()))
                                                {
                                                    omemberDao.deleteByKey(member.getId());
                                                }
                                            }


                                            File file = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS).getPath()+"/data/"+ group.getName());

                                            deleteDirWihtFile(file);

                                            LogUtil.i(GlobalConsts.TAG,"删除记录成功");
                                        } catch (Exception e)
                                        {
                                            ExceptionUtil.handleException(e);
                                            return false;
                                        }

                                        return true;
                                    }

                                    @Override
                                    protected void onPostExecute(Boolean b) {
                                        notifyDataSetChanged();
                                    }
                                }.execute();

                            }




                            dialog.cancel();

                        }
                    });
                    dialog.show();


                }
            });
        }
        return convertView;
    }


    private class ViewHolder {
        TextView tv1;
        ImageView img;
    }


    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
        {
            return;
        }

        for (File file : dir.listFiles())
        {
            if (file.isFile())
            {
                file.delete(); // 删除所有文件
                LogUtil.i(GlobalConsts.TAG,"删除所有文件");

            } else if (file.isDirectory())
            {
                deleteDirWihtFile(file); // 递规的方式删除文件夹
                LogUtil.i(GlobalConsts.TAG,"递规的方式删除文件夹");
            }

        }
        dir.delete();// 删除目录本身
        LogUtil.i(GlobalConsts.TAG,"删除目录本身");
    }
}
