package com.sms.app.interphone.services;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.DistanceUtil;
import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.interphone.util.maputil.Map_intercface;
import com.sms.app.interphone.util.msnutil.ExceptionUtil;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.framework.dao.bean.DAOPoint;
import com.sms.app.framework.dao.bean.DAOTrajectory;
import com.sms.app.framework.dao.bean.commom.DAOPointDao;
import com.sms.app.framework.dao.bean.commom.DAOService;
import com.sms.app.framework.dao.bean.commom.DAOTrajectoryDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *
 * 定位业务相关服务类
 *
 */

public class MonitorService extends Service {

    private final static int SERVICE_ID = 1000;

    private static String TAG = "YanShi...Log - MonitorService";

    private static MonitorService mon_service = null;

    private static Map_intercface map_cb = null;

    private double length = 0.0;

    private DAOTrajectory trajectory = null;

    private DAOTrajectoryDao trajectoryDao = null;

    private DAOPointDao pointDao = null;

//    private DAOTrajectory updateDAO = null;

    public static LocationManager locationManager = null;

    public Context context;

    private List<Location> locations = new ArrayList<>();

    private List<DAOPoint> points = new ArrayList<>();



    public static byte START_TRACE = 1;
    public static byte STOP_TRACE = 2;
    public static byte END_TRACE = 3;

    public static byte NOW_TRACE = 0;

    public static String trajectoryName = "NITECORE";

    private String provider;

    private int startTraject = 0;


    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        Log.e("TAGKK", "MonitorService onCreate");

        Intent intent = new Intent();
        intent.setAction("android.intent.action.BOOST_DOWNLOADING");
        intent.putExtra("package_name", "com.example.ys.androidapplication");
        intent.putExtra("enabled", true);
        MonitorService.this.sendBroadcast(intent);

        super.onCreate();

    }

    /**
     * 告诉系统如何重新启动service
     * 保证service不被杀死
     * */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Log.e("TAGKK", "MonitorService onStartCommand" + "Intent" + intent);

        context = this;

        trajectoryDao = DAOService.get(getApplication()).getsession().getDAOTrajectoryDao();

        pointDao = DAOService.get(getApplication()).getsession().getDAOPointDao();

        setLocation();

        startForeground(SERVICE_ID, new Notification());

        return START_REDELIVER_INTENT;
    }

    private void setLocation() {

        // 获得LocationManager实例
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();//Criteria类是设置定位的标准信息（系统会根据你的要求，匹配最适合你的定位供应商），一个定位的辅助信息的类

        criteria.setAltitudeRequired(true);//设置需要海拔
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//设置低精度
        criteria.setBearingRequired(true);// 设置是否需要方位信息 Bearing
        // 设置是否允许运营商收费
        criteria.setCostAllowed(true);


        String provider = locationManager.getBestProvider(criteria,true);
        // 传入 true 就表示只有启用的位置提供器才会被返回。

        if (provider.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        }


        if (provider != null) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location location = locationManager.getLastKnownLocation(provider);
                locationManager.requestLocationUpdates(provider,1000,0,locationListener);
                updateWithNewLocation(location);
            }

        }

    }

    public final LocationListener locationListener = new LocationListener() {


        @Override
        public void onLocationChanged(Location location) {
            LogUtil.i(TAG,"onLocationChanged="+location.toString());
            updateWithNewLocation(location);
        }

        //Provider禁用时触发此函数，如GPS被关闭.
        @Override
        public void onProviderDisabled(String provider) {
            LogUtil.i(TAG,"onProviderDisabled="+provider);
            //    updateWithNewLocation(null);
        }

        //Provider启用是触发此函数，如GPS被打开.
        @Override
        public void onProviderEnabled(String provider) {
            LogUtil.i(TAG,"onProviderEnabled="+provider);

        }

        //Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数。
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            LogUtil.i(TAG,"onStatusChanged="+provider);
        }

    };
    private void updateWithNewLocation(Location location) {

            /*NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(6);
            String latLongString="";
            if (location != null) {
                isClick = true;
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                lat = Double.parseDouble(nf.format(lat).replaceAll(",", ""));
                lng = Double.parseDouble(nf.format(lng).replaceAll(",", ""));
                latLongString = "纬度:" + lat + "\n经度:" + lng;

            } else {

                *//*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    locationManager.requestLocationUpdates(provider,2000,0,locationListener);
                    //    updateWithNewLocation(location);
                }*//*

                return;
                *//*if (location != null) {
                    isClick = true;
                    double lat = location.getLatitude();
                    double lng = location.getLongitude();
                    lat = Double.parseDouble(nf.format(lat).replaceAll(",", ""));
                    lng = Double.parseDouble(nf.format(lng).replaceAll(",", ""));
                    latLongString = "纬度:" + lat + "\n经度:" + lng;
                } else {
                    isClick = false;
                    latLongString = "无法获取地理信息";
                }*//*
            }*/

            if(location != null){

                locations.add(location);

                LogUtil.i(TAG,"locations="+locations.size());

            }
            if(locations.size() >= 5){

                double Latitude = 0;
                double Longitude = 0;
                double Altitude = 0;

                for(Location locetion : locations){
                    Latitude += locetion.getLatitude();
                    Longitude += locetion.getLongitude();
                    Altitude += locetion.getAltitude();
                }

                Location locetion_yes = new Location("");
                locetion_yes.setLatitude(Latitude/5);
                locetion_yes.setLongitude(Longitude/5);
                locetion_yes.setAltitude(Altitude/5);

                locetion_yes.setSpeed(location.getSpeed());
                locetion_yes.setBearing(location.getBearing());

                if(MonitorService.NOW_TRACE != STOP_TRACE){


                    startTraject++;

                    if(startTraject > 2){

                        //写入数据库

                        try {
                            updateDB(locetion_yes);
                        } catch (Exception e) {
                            ExceptionUtil.handleException(e);
                        }

                        double latitude = locetion_yes.getLatitude();
                        double longitude = locetion_yes.getLongitude();

                        LatLng latLng = new LatLng(latitude, longitude);

                        LatLng sourceLatLng = latLng;
                        CoordinateConverter converter = new CoordinateConverter();
                        converter.from(CoordinateConverter.CoordType.GPS);
                        converter.coord(latLng);
                        latLng = converter.convert();

                        MyApplicatoin.pointList.add(latLng);

                        //回调定位结果
                        MonitorService.map_cb.on_Location(locetion_yes,length,mon_service);

                    }

                }else if(MonitorService.NOW_TRACE == END_TRACE){

                    startTraject = 0;
                }

                locations.clear();
            }

    }

    private void updateDB(Location locetion_yes) {

        if(trajectoryDao != null && pointDao != null){

            if(trajectory == null && MonitorService.NOW_TRACE == START_TRACE){

                trajectory = new DAOTrajectory();

                trajectory.setRemote_id(System.currentTimeMillis());
                trajectory.setName(trajectoryName);
                trajectory.setStatus(DAOTrajectory.TRAJ_STATUS_UNFINISH);
                trajectory.setPoit_num(1);
                trajectory.setDate(new Date(System.currentTimeMillis()));

                trajectoryDao.insert(trajectory);

            }

        //    long remoteId = 0;

            if(trajectory != null){

            //    remoteId = trajectory.getRemote_id();

            //    updateDAO = trajectoryDao.queryBuilder().where(DAOTrajectoryDao.Properties.Remote_id.eq(remoteId)).orderAsc(DAOTrajectoryDao.Properties.Remote_id).unique();

                DAOPoint point = new DAOPoint();
                point.setRemote_id(System.currentTimeMillis());
                point.setPcreate_time(new Date((System.currentTimeMillis())));
                point.setLatitude(locetion_yes.getLatitude());
                point.setLongitude(locetion_yes.getLongitude());
                point.setAltitude(locetion_yes.getAltitude());
                point.setTrajectoryId(trajectory.getId());
                point.setTrajectory(trajectory);

                pointDao.insert(point);

                points.add(point);

                trajectory.setPoit_num(points.size());

                if(points.size() > 10){

                    length = trajectory.getLength()+getDistance(points.get(points.size() - 2),points.get(points.size() - 1));
                    trajectory.setLength(length);

                }


                if(MonitorService.NOW_TRACE == START_TRACE){

                    if(trajectory != null){

                        if(points.size() == 1){
                            trajectory.setStart_DAO_point(points.get(0));
                            trajectory.setStartPId(points.get(0).getId());
                        }
                        trajectoryDao.update(trajectory);

                    }

                }else if(MonitorService.NOW_TRACE == END_TRACE){

                    //servoce 自杀
                    stopSelf();

                }

            }

        }

    }

    //计算两点在百度地图上的距离
    private double getDistance(DAOPoint start_point, DAOPoint stop_point) {

        Location start_locetion = new Location("");

        start_locetion.setLongitude(start_point.getLongitude());
        start_locetion.setLatitude(start_point.getLatitude());

        Location stop_locetion = new Location("");

        stop_locetion.setLongitude(stop_point.getLongitude());
        stop_locetion.setLatitude(stop_point.getLatitude());

        return DistanceUtil.getDistance(getLatLng(start_locetion),getLatLng(stop_locetion));
    }

    //GPS坐标转百度坐标
    private LatLng getLatLng(Location locetion) {

        LatLng latLng = new LatLng(locetion.getLatitude(), locetion.getLongitude());
        LatLng sourceLatLng = latLng;
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        converter.coord(sourceLatLng);
        latLng = converter.convert();

        return latLng;
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        /*final Intent intent = new Intent();
        intent.setAction("android.intent.action.BOOST_DOWNLOADING");
        intent.putExtra("package_name", "com.example.ys.androidapplication");
        intent.putExtra("enabled", false);
        MonitorService.this.sendBroadcast(intent);*/


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(locationListener);
        }


        if(trajectory != null && trajectoryDao != null && points.size() > 2){

            trajectory.setEnd_DAO_point(points.get(points.size() - 1));

            trajectory.setName(trajectoryName);

            trajectory.setStatus(DAOTrajectory.TRAJ_STATUS_FINISH);

            trajectory.setEndPId(points.get(points.size() - 1).getId());

            trajectoryDao.update(trajectory);

            trajectory = null;

            length = 0.0;
        }

        MyApplicatoin.pointList.clear();

        points.clear();

        LogUtil.i(TAG,"MonitorService onDestroy");

    }


    public static MonitorService get_map_service(Context context, Map_intercface map_if) {

        if (mon_service == null) {
            Intent intent = new Intent(context, MonitorService.class);
            context.startService(intent);
        }

        LogUtil.i(TAG,"mon_service="+mon_service);

        MonitorService.map_cb = map_if;
        return MonitorService.mon_service;
    }


}
