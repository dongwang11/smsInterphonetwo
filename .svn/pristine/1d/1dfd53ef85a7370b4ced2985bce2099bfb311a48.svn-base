package com.sms.app.interphone.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sms.app.framework.RequestCB;
import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.framework.dao.bean.DAOPoint;
import com.sms.app.framework.dao.bean.DAOTrajectory;
import com.sms.app.framework.dao.bean.commom.DAOPointDao;
import com.sms.app.framework.dao.bean.commom.DAOService;
import com.sms.app.framework.dao.bean.commom.DAOTrajectoryDao;
import com.sms.app.framework.sms_request;
import com.sms.app.framework.trans.bean.Point;
import com.sms.app.framework.trans.bean.Traject_set;
import com.sms.app.framework.trans.bean.Trajectory;
import com.sms.app.interphone.R;
import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.interphone.util.msnutil.ExceptionUtil;
import com.sms.app.interphone.util.msnutil.Tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TrajectoryAdapter extends BaseAdapter {
    //	private ImageLoader imageLoader = ImageLoader.getInstance();
    private LayoutInflater inflater;
    private Context mContext;
    private List<DAOTrajectory> mList;

    private DAOPointDao pointDao;
    private DAOTrajectoryDao trajectoryDao;

    public TrajectoryAdapter(Activity context, List<DAOTrajectory> mContext) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.mList = mContext;
        this.inflater = LayoutInflater.from(context);
        this.trajectoryDao = DAOService.get(context).getsession().getDAOTrajectoryDao();
        this.pointDao = DAOService.get(context).getsession().getDAOPointDao();

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
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.trjectory_item, null, false);
            holder.tv1 = (TextView) convertView.findViewById(R.id.textView1);
            holder.tv2 = (TextView) convertView.findViewById(R.id.textView2);
            holder.img_delete = (ImageView) convertView.findViewById(R.id.imageView_delete);
            holder.img_upload = (ImageView) convertView.findViewById(R.id.imageview_upload);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (this.mList != null) {

            final DAOTrajectory trajectory = mList.get(position);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


            /*if(trajectory.getDate() != null){
                holder.tv1.setText(sdf.format(trajectory.getDate()));
            }*/


            if(trajectory.getName() != null){
                holder.tv1.setText(trajectory.getName());
            }

            LogUtil.i(GlobalConsts.TAG,"name:"+trajectory.getName());

            holder.tv2.setText(Math.round(trajectory.getLength()/100d)/10d+"KM");

            /*if(trajectory.getStatus() == 2 ){
                holder.img_upload.setVisibility(View.VISIBLE);
            }*/

            holder.img_upload.setVisibility(View.VISIBLE);

            final Activity activity = (Activity) convertView.getContext();

            //点击删除轨迹按钮

            holder.img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
                    View view = View.inflate(mContext, R.layout.dialog_warning_layout, null);
                    dialog.setView(view);
                    dialog.setInverseBackgroundForced(true);
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

                            Tools.showProgressDialog(activity,"正在删除");

                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    try {

                                        trajectoryDao.deleteByKey(trajectory.getId());

                                        List<DAOPoint> pointlist = pointDao.queryBuilder().where(DAOPointDao.Properties.TrajectoryId.eq(trajectory.getId())).orderAsc(DAOPointDao.Properties.TrajectoryId).list();

                                        for (DAOPoint point : pointlist){

                                            pointDao.deleteByKey(point.getId());

                                        }

                                        mList.remove(position);

                                        Tools.closeProgressDialog();

                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                notifyDataSetChanged();
                                            }
                                        });

                                    } catch (Exception e) {

                                        ExceptionUtil.handleException(e);

                                    }


                                }
                            }).start();

                            dialog.cancel();

                        }
                    });
                    dialog.show();

                }
            });

            //点击上传按钮

            holder.img_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Tools.showProgressDialog(activity,"正在上传");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Traject_set traject_set = new Traject_set();

                            traject_set.setTotal_num((long) 1);

                            traject_set.setUser_id(MyApplicatoin.evenUser.getId());

                            List<Trajectory> trajectoryList = new ArrayList<Trajectory>();

                            Trajectory trajectorys = new Trajectory();

                            //不能上传轨迹ID  由服务器后台生成

                        //    trajectorys.setId(trajectory.getId());
                            trajectorys.setName(trajectory.getName());
                            trajectorys.setUser_id(MyApplicatoin.evenUser.getId());
                            trajectorys.setPoit_num(trajectory.getPoit_num());
                            trajectorys.setStatus(DAOTrajectory.TRAJ_STATUS_UPLOADING);

                            trajectorys.setUsed_time_s(trajectory.getUsed_time_s());
                            trajectorys.setLength((int) trajectory.getLength());


                            Point startPoint = new Point();

                            startPoint.setLongitude(trajectory.getStart_DAO_point().getLongitude());
                            startPoint.setLatitude(trajectory.getStart_DAO_point().getLatitude());
                            startPoint.setAltitude(trajectory.getStart_DAO_point().getAltitude());
                            startPoint.setDirection(trajectory.getStart_DAO_point().getDirection());
                            startPoint.setPcreate_time(trajectory.getStart_DAO_point().getPcreate_time());
                            startPoint.setSpeex(trajectory.getStart_DAO_point().getSpeex());


                            trajectorys.setStart_point(startPoint);



                            List<Point> points = new ArrayList<Point>();

                            List<DAOPoint> pointlist_all = pointDao.loadAll();

                            List<DAOPoint> pointlist = new ArrayList<DAOPoint>();

                            for(DAOPoint point : pointlist_all){

                                if(point.getTrajectoryId() == trajectory.getId()){

                                    pointlist.add(point);

                                }

                            }

                            for(DAOPoint p : pointlist){

                                Point point = new Point();
                                point.setLatitude(p.getLatitude());
                                point.setLongitude(p.getLongitude());
                                point.setAltitude(p.getAltitude());
                                point.setPcreate_time(p.getPcreate_time());
                                point.setDirection(p.getDirection());
                                point.setSpeex(p.getSpeex());

                                points.add(point);
                            }


                            Point endPoint = new Point();

                            if(trajectory.getEnd_DAO_point() != null){

                                endPoint.setLongitude(trajectory.getEnd_DAO_point().getLongitude());
                                endPoint.setLatitude(trajectory.getEnd_DAO_point().getLatitude());
                                endPoint.setAltitude(trajectory.getEnd_DAO_point().getAltitude());
                                endPoint.setDirection(trajectory.getEnd_DAO_point().getDirection());
                                endPoint.setPcreate_time(trajectory.getEnd_DAO_point().getPcreate_time());
                                endPoint.setSpeex(trajectory.getEnd_DAO_point().getSpeex());

                            }else{

                                endPoint = points.get(points.size()-1);
                            }




                            trajectorys.setEnd_point(endPoint);




                            trajectorys.setPoints(points);

                            trajectoryList.add(trajectorys);

                            traject_set.setTrajectories(trajectoryList);

                            LogUtil.i(GlobalConsts.TAG,"traject_set:"+traject_set.toString());

                            sms_request.dorequest(mContext, sms_request.SET, traject_set, new RequestCB() {
                                @Override
                                public void respone(int opcode, Object obj) {

                                    LogUtil.i(GlobalConsts.TAG,"opcode:"+opcode);

                                    if(opcode == sms_request.SET){

                                        mList.get(position).setStatus(DAOTrajectory.TRAJ_STATUS_UPLOADING);

                                        try {
                                            trajectoryDao.update(mList.get(position));
                                        } catch (Exception e) {
                                            ExceptionUtil.handleException(e);
                                        }

                                        Tools.closeProgressDialog();

                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                notifyDataSetChanged();
                                            }
                                        });



                                    }

                                }
                            });
                        }
                    }).start();

                }
            });


        }
        return convertView;
    }


    private class ViewHolder {
        TextView tv1;
        TextView tv2;
        ImageView img_delete;
        ImageView img_upload;

    }

}
