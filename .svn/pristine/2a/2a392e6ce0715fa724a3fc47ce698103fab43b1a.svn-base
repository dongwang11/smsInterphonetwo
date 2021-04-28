package com.sms.app.interphone.ui.activity;


import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.utils.CoordinateConverter;
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
import com.sms.app.interphone.adapter.TrajectoryAdapter;
import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.interphone.util.interfaces.NoDoubleClickListener;
import com.sms.app.interphone.util.msnutil.ExceptionUtil;
import com.sms.app.interphone.util.msnutil.Tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TrackingAvtivity extends BaseActivity {

    private MapView mMapView;

    private BaiduMap mBaiduMap;

    private ListView tracking_lv;

    private ImageView map_switch_icon;

    private ImageView tracking_return;

    private TextView tv_tracking_date;

    private TextView tv_tracking_length;

    private LinearLayout delete_all_layout;

    private TrajectoryAdapter adapter;

    private List<DAOTrajectory> trajectorieList;

    private DAOTrajectoryDao trajectoryDao = null;

    private DAOPointDao pointDao = null;




    protected static MapStatusUpdate msUpdate = null;               //绘制实时轨迹数据


    private static BitmapDescriptor bmStart;                        // 起点图标
    private static BitmapDescriptor bmEnd;                          // 终点图标
    private static MarkerOptions startMarker = null;                // 起点图标覆盖物
    private static MarkerOptions endMarker = null;                  // 终点图标覆盖物
    public static PolylineOptions polyline = null;                  // 路线覆盖物



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_tracking);
        setView();

        setListener();

        setTracking();
    }



    private void setListener() {

    }

    private void setView() {

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);

        tracking_lv = (ListView) findViewById(R.id.tracking_listView);

        map_switch_icon = (ImageView) findViewById(R.id.tracking_image2);

        tracking_return = (ImageView) findViewById(R.id.settings_image1);


        tv_tracking_date = (TextView) findViewById(R.id.tracking_tetx_date);

        tv_tracking_length = (TextView) findViewById(R.id.tracking_tetx_length);

        delete_all_layout = (LinearLayout) findViewById(R.id.delete_linear_layout);

        mBaiduMap = mMapView.getMap();

    }


    private void setTracking() {

        trajectoryDao = DAOService.get(TrackingAvtivity.this).getsession().getDAOTrajectoryDao();

        pointDao = DAOService.get(TrackingAvtivity.this).getsession().getDAOPointDao();

        trajectorieList = trajectoryDao.loadAll();

        /*for(DAOTrajectory trajectory : trajectorieList){

            trajectory.setName("下班回家");

            trajectoryDao.update(trajectory);

        }*/

        if(MyApplicatoin.evenUser != null){

            getTrajectoryThread();

        }




        map_switch_icon.setOnClickListener(new NoDoubleClickListener(){
            @Override
            public void onNoDoubleClick(View v) {
                //切换地图


                final AlertDialog dialog = new AlertDialog.Builder(TrackingAvtivity.this).create();
                View view = View.inflate(TrackingAvtivity.this, R.layout.dialog_mapswitch_layout, null);
                dialog.setView(view);
                dialog.setInverseBackgroundForced(true);
                final Button bt1 = (Button) view.findViewById(R.id.button_cancel);
                final Button bt2 = (Button) view.findViewById(R.id.button_ok);


                //dialog背景透明

                Window window = dialog.getWindow();
                window.setBackgroundDrawableResource(android.R.color.transparent);

                bt1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showMap(true);
                        dialog.cancel();
                    }
                });
                bt2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        showMap(false);
                        dialog.cancel();

                    }
                });
                dialog.show();
            }
        });


        delete_all_layout.setOnClickListener(new NoDoubleClickListener(){
            @Override
            public void onNoDoubleClick(View v) {

                //删除所有轨迹

                final AlertDialog dialog = new AlertDialog.Builder(TrackingAvtivity.this).create();
                View view = View.inflate(TrackingAvtivity.this, R.layout.dialog_warning_layout, null);
                dialog.setView(view);
                dialog.setInverseBackgroundForced(true);
                final Button cancel = (Button) view.findViewById(R.id.button_cancel);
                final Button ok = (Button) view.findViewById(R.id.button_ok);
                final TextView title = (TextView) view.findViewById(R.id.title);
                final TextView content = (TextView) view.findViewById(R.id.content);

                title.setText(getResources().getString(R.string.sms_tracking_delete_title));
                content.setText(getResources().getString(R.string.sms_tracking_delete_content));

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

                        Tools.showProgressDialog(TrackingAvtivity.this,"正在删除");

                        try {

                            trajectoryDao.deleteAll();

                            pointDao.deleteAll();

                            trajectorieList.clear();

                            adapter.notifyDataSetChanged();

                        } catch (Exception e) {
                            ExceptionUtil.handleException(e);
                        }

                        Tools.closeProgressDialog();

                        dialog.cancel();



                    }
                });
                dialog.show();

            }
        });

        tracking_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                try {

                    final DAOTrajectory dao_trajectory = trajectorieList.get(position);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                    if(dao_trajectory.getDate() != null){
                        tv_tracking_date.setText(sdf.format(dao_trajectory.getDate()));

                    }

                    tv_tracking_length.setText(Math.round(dao_trajectory.getLength()/100d)/10d + "KM");

                    DAOPointDao pointDao = DAOService.get(TrackingAvtivity.this).getsession().getDAOPointDao();

                    List<DAOPoint> pointlist = pointDao.queryBuilder().where(DAOPointDao.Properties.TrajectoryId.eq(dao_trajectory.getId())).orderAsc(DAOPointDao.Properties.TrajectoryId).list();

                    if(pointlist != null && pointlist.size() > 0){


                        drawHistoryTrack(pointlist);


                    }


                } catch (Exception e) {
                    ExceptionUtil.handleException(e);
                }

            }
        });




        tracking_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //生成轨迹数据

                finish();

                /*int max=1000;
                int min=9999;
                Random random = new Random();

                int theNum = random.nextInt(max)%(max-min+1) + min;

                DAOTrajectoryDao trajectoryDao = DAOService.get(TrackingAvtivity.this).getsession().getDAOTrajectoryDao();

                DAOPointDao pointDao = DAOService.get(TrackingAvtivity.this).getsession().getDAOPointDao();

                DAOTrajectory trajectory = new DAOTrajectory();

                trajectory.setStatus((byte) 1);
                trajectory.setUserId(5);
                trajectory.setLength(2000+theNum);
                trajectory.setName("TEST"+System.currentTimeMillis());
                trajectory.setRemote_id(System.currentTimeMillis());
                trajectory.setStatus(DAOTrajectory.TRAJ_STATUS_UNFINISH);
                trajectory.setDate(new Date(System.currentTimeMillis()));

                trajectoryDao.insert(trajectory);


                DAOTrajectory updateDAO = trajectoryDao.queryBuilder().where(DAOTrajectoryDao.Properties.Remote_id.eq(trajectory.getRemote_id())).orderAsc(DAOTrajectoryDao.Properties.Remote_id).unique();

                double[] a = {0.767683,0.765828,0.761656,0.759417,0.754470,0.747198,0.740710,0.740944,0.740860,0.741254,0.742042,0.737793,0.733539,0.729676,0.725524,0.721735,0.717790,0.727849,0.736275,0.745322};
                double[] b = {0.198897,0.201043,0.202455,0.202462,0.202376,0.201494,0.204969,0.211140,0.218885,0.226527,0.233374,0.237846,0.243734,0.249404,0.254432,0.260097,0.265045,0.271979,0.274120,0.277539};


                int theNum2 = 400;

                for(int i = 0; i < 20; i++){

                    theNum2 += 5;

                    *//**//*int max2=3000;
                    int min2=500;
                    Random random2 = new Random();

                    int theNum2 = random2.nextInt(max2)%(max2-min2+1) + min2;*//**//*


                    DAOPoint point = new DAOPoint();

                    point.setRemote_id(System.currentTimeMillis());
                    point.setPcreate_time(new Date((System.currentTimeMillis())));
                    point.setLatitude(25.0+a[i]);
                    point.setLongitude(113.0+b[i]);
                    point.setAltitude(theNum2);

                    point.setTrajectoryId(updateDAO.getId());
                    point.setTrajectory(updateDAO);

                    pointDao.insert(point);


                }


                List<DAOPoint> pointlist = pointDao.queryBuilder().where(DAOPointDao.Properties.TrajectoryId.eq(updateDAO.getId())).orderAsc(DAOPointDao.Properties.TrajectoryId).list();

                updateDAO.setPoit_num(pointlist.size());
                updateDAO.setStart_DAO_point(pointlist.get(0));
                updateDAO.setStartPId(pointlist.get(0).getId());
                updateDAO.setEnd_DAO_point(pointlist.get(pointlist.size() - 1));
                updateDAO.setEndPId(pointlist.get(pointlist.size() - 1).getId());

                trajectoryDao.update(updateDAO);

                trajectorieList.add(updateDAO);

                adapter.notifyDataSetChanged();*/


            }
        });

    }

    /**
     * 绘制历史轨迹
     *
     * */
    public void drawHistoryTrack(final List<DAOPoint> pointlist) {
        // 绘制新覆盖物前，清空之前的覆盖物
        mBaiduMap.clear();


        LogUtil.i(TAG,"drawHistoryTrack"+System.currentTimeMillis());

        LogUtil.i(TAG,"point_list:"+pointlist.size());

        if (pointlist.size() > 1) {

            List<LatLng> points = new ArrayList<>();

            for(DAOPoint point : pointlist){

                LatLng latLng =  newLatLng(point);

                points.add(latLng);

            }

            LatLng llC = points.get(0);
            LatLng llD = points.get(points.size() - 1);
            LatLngBounds bounds = new LatLngBounds.Builder().include(llC).include(llD).build();

            msUpdate = MapStatusUpdateFactory.newLatLngBounds(bounds);
            bmStart = BitmapDescriptorFactory.fromResource(R.mipmap.icon_start);
            bmEnd = BitmapDescriptorFactory.fromResource(R.mipmap.icon_end);

            // 添加起点图标
            startMarker = new MarkerOptions().position(points.get(0)).icon(bmStart).zIndex(9).draggable(true);

            // 添加终点图标
            endMarker = new MarkerOptions().position(points.get(points.size() - 1)).icon(bmEnd).zIndex(9).draggable(true);

            double start = 0;
            double stop = 9999;


            //构建分段颜色索引数组

            for(DAOPoint point : pointlist){

                if(point.getAltitude() > start){

                    start = point.getAltitude();

                }
                if(point.getAltitude() < stop){

                    stop = point.getAltitude();

                }

            }


            List<Integer> colors = new ArrayList<>();

            for(DAOPoint point : pointlist){

                colors.add(getPointColor(start,stop,point));

            }

            // 添加路线（轨迹）
            polyline = new PolylineOptions().width(8).colorsValues(colors).points(points);


            addMarker();
        }

    }

    private int getPointColor(double start, double stop, DAOPoint daoPoint) {

    //    LogUtil.i(TAG,"getPointColor"+System.currentTimeMillis());

        double altitude = daoPoint.getAltitude();

        float are = (float) ((altitude - stop) / (start - stop));


    //    LogUtil.i(TAG,"start:"+start+",stop:"+stop+",altitude:"+altitude+",are:"+are);

        if(are >= 0.9){
            return 0xFFFF692A;
        }else if(are >= 0.8){
            return 0xFFFE931D;
        }else if(are >= 0.7){
            return 0xFFFFB315;
        }else if(are >= 0.6){
            return 0xFFFDCE0D;
        }else if(are >= 0.5){
            return 0xFFFDEE05;
        }else if(are >= 0.4){
            return 0xFFE1F609;
        }else if(are >= 0.3){
            return 0xFFB2EC15;
        }else if(are >= 0.2){
            return 0xFF80E221;
        }else if(are >= 0.1){
            return 0xFF50D82E;
        }else{
            return 0xFF50D82E;
        }



        /*if(are >= 0){
            return 0xFF19CB39;
        }else if(are >= 0.1){
            return 0xFF50D82E;
        }else if(are >= 0.2){
            return 0xFF80E221;
        }else if(are >= 0.3){
            return 0xFFB2EC15;
        }else if(are >= 0.4){
            return 0xFFE1F609;
        }else if(are >= 0.5){
            return 0xFFFDEE05;
        }else if(are >= 0.6){
            return 0xFFFDCE0D;
        }else if(are >= 0.7){
            return 0xFFFFB315;
        }else if(are >= 0.8){
            return 0xFFFE931D;
        }else if(are >= 0.9){
            return 0xFFFF692A;
        }*/

    }

    private LatLng newLatLng(DAOPoint point) {

        LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
        LatLng sourceLatLng = latLng;
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        converter.coord(sourceLatLng);
        latLng = converter.convert();

        return latLng;
    }

    /**
     * 添加覆盖物
     */
    public void addMarker() {

        LogUtil.i(TAG,"addMarker"+System.currentTimeMillis());

        if (null != msUpdate) {
            mBaiduMap.animateMapStatus(msUpdate, 2000);
        }

        if (null != startMarker) {
            mBaiduMap.addOverlay(startMarker);
        }

        if (null != endMarker) {
            mBaiduMap.addOverlay(endMarker);
        }

        if (null != polyline) {
            mBaiduMap.addOverlay(polyline);
        }

    }

    /**
     * 显示地图类型
     * */
    public void showMap(boolean isMap) {

        if(isMap){
            //普通地图
            mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        }else{
            //卫星地图
            mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }


    public void getTrajectoryThread() {



        Traject_set traject = new Traject_set();

        traject.setUser_id(MyApplicatoin.evenUser.getId());
        traject.setStart_index((long) 0);
        traject.setEnd_index((long) 5);

        Tools.showProgressDialog(TrackingAvtivity.this,"正在加载");

        sms_request.dorequest(TrackingAvtivity.this, sms_request.GET, traject, new RequestCB() {
            @Override
            public void respone(int opcode, Object obj) {



                if(opcode == sms_request.GET){

                    Traject_set traject = (Traject_set) obj;

                    LogUtil.i(GlobalConsts.TAG,"Object:"+traject.toString());

                    List<Trajectory> trajectorys = traject.getTrajectories();


                    try {

                        for(Trajectory trajectory : trajectorys){

                            LogUtil.i(GlobalConsts.TAG,"trajectory:"+trajectory.getStatus());

                            boolean exist = false;
                            for(DAOTrajectory trajectoryList :trajectorieList){

                                if(trajectoryList.getId() == trajectory.getId()){

                                    exist = true;

                                    trajectoryList.setStatus(DAOTrajectory.TRAJ_STATUS_UPLOADING);

                                    trajectoryDao.update(trajectoryList);

                                }
                            }

                            //如果本地不存在
                            if(!exist){

                                DAOTrajectory daoTrajectory = new DAOTrajectory();

                                daoTrajectory.setId(trajectory.getId());
                                daoTrajectory.setName(trajectory.getName());
                                daoTrajectory.setUsed_time_s(trajectory.getUsed_time_s());
                                daoTrajectory.setStatus(trajectory.getStatus());
                                daoTrajectory.setLength(trajectory.getLength());
                                daoTrajectory.setPoit_num(trajectory.getPoit_num());
                                daoTrajectory.setUserId(trajectory.getUser_id());

                                trajectoryDao.insert(daoTrajectory);

                                for(int i = 0; i < trajectory.getPoints().size();i++){

                                    Point point = trajectory.getPoints().get(i);

                                    DAOPoint daoPoint = new DAOPoint();

                                    daoPoint.setId(point.getId());
                                    daoPoint.setAltitude(point.getAltitude());
                                    daoPoint.setSpeex(point.getSpeex());
                                    daoPoint.setDirection(point.getDirection());
                                    daoPoint.setLatitude(point.getLatitude());
                                    daoPoint.setLongitude(point.getLongitude());
                                    daoPoint.setPcreate_time(point.getPcreate_time());
                                    daoPoint.setTrajectory(daoTrajectory);
                                    daoPoint.setTrajectoryId(daoTrajectory.getId());

                                    if(i == 0){

                                        daoTrajectory.setStart_DAO_point(daoPoint);

                                    }

                                    if(i == trajectory.getPoints().size()-1){

                                        daoTrajectory.setEnd_DAO_point(daoPoint);

                                    }

                                    pointDao.insert(daoPoint);

                                }

                                trajectoryDao.update(daoTrajectory);

                                trajectorieList.add(daoTrajectory);

                            }
                        }


                    } catch (Exception e) {
                        ExceptionUtil.handleException(e);
                    }

                }


                Tools.closeProgressDialog();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {



                        if(trajectorieList != null){

                            if(adapter == null){

                                int trajectorieSize = trajectorieList.size();

                                        /*for(int i = 0; i < trajectorieSize;i++){

                                        trajectorieList.get(i).setName("回家");

                                        trajectoryDao.update(trajectorieList.get(i));

                                            if(trajectorieList.get(i).getStatus() == DAOTrajectory.TRAJ_STATUS_UNFINISH){

                                            trajectorieList.remove(i);

                                            i--;
                                            trajectorieSize--;
                                            }

                                        }*/

                                adapter = new TrajectoryAdapter(TrackingAvtivity.this,trajectorieList);

                                tracking_lv.setAdapter(adapter);

                            }else{

                                adapter.notifyDataSetChanged();

                            }

                        }
                    }
                });

            }
        });
    }
}
