package com.sms.app.interphone.ui.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.sms.app.framework.RequestCB;
import com.sms.app.framework.communication.localayer.MsgResponse;
import com.sms.app.framework.communication.localayer.attr_listenner;
import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.framework.communication.localayer.cmd.Ble_message;
import com.sms.app.framework.communication.localayer.cmd.Opcode;
import com.sms.app.framework.dao.bean.DAOmember;
import com.sms.app.framework.dao.bean.commom.DAOService;
import com.sms.app.framework.dao.bean.commom.DAOmemberDao;
import com.sms.app.framework.sms_request;
import com.sms.app.framework.trans.bean.Interphone;
import com.sms.app.framework.trans.sms_fw_api;
import com.sms.app.interphone.R;
import com.sms.app.interphone.services.MonitorService;
import com.sms.app.interphone.services.Open_biz;
import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.interphone.ui.activity.OffilneActivity;
import com.sms.app.interphone.ui.activity.TrackingAvtivity;
import com.sms.app.interphone.util.maputil.Map_intercface;
import com.sms.app.interphone.util.maputil.MyOrientationListener;
import com.sms.app.interphone.util.msnutil.ExceptionUtil;
import com.sms.app.interphone.util.msnutil.Tools;
import com.sms.app.interphone.util.openutil.Openfrom;
import com.sms.app.interphone.view.CircleImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/7/12.
 */

public class Fragment_Map extends Fragment implements View.OnClickListener{
    private boolean isMap;                                          //当前显示的地图类型
    private MonitorService monitorService;                                  //GPS轨迹功能
    private static BitmapDescriptor realtimeBitmap;                 //绘制轨迹数据时当前自定义图标
    private static Overlay overlay = null;                          //百度地图覆盖物
    protected static OverlayOptions overlayOptions;                 //百度地图覆盖物
    private MyLocationConfiguration.LocationMode mCurrentMode;      //当前定位显示类型
    private BitmapDescriptor mCurrentMarker;                        //当前定位显示类型
    /*private TextView tv_trace_time;
    private TextView tv_trace_km;
    private Button bt_trace_start;
    private Button bt_trace_stop;*/

    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;

    protected static MapStatusUpdate msUpdate = null;               //绘制实时轨迹数据

//    private float currentZoomLevel;                                 //当前地图显示的大小

    private MapView mMapView;

    private BaiduMap mBaiduMap;

    private LatLng latLng;                                          //当前位置

    private static BitmapDescriptor bmStart;                        // 起点图标
    private static BitmapDescriptor bmEnd;                          // 终点图标
    private static MarkerOptions startMarker = null;                // 起点图标覆盖物
    private static MarkerOptions endMarker = null;                  // 终点图标覆盖物
    public static PolylineOptions polyline = null;                  // 路线覆盖物
    private static MarkerOptions markerOptions = null;
    private float mCurrentx = 0;                                    //当前方向传感信息
    private MyOrientationListener mOrientationListener;

    private boolean isFirstLocation = true;                         //是否第一次定位，如果是第一次定位的话要将自己的位置显示在地图 中间
//    private Button more_map;                                        // 放大地图button
//    private Button less_map;                                        // 放大地图
    private ImageButton location_img_button;                          // 定位按钮


    private MyLocationListenner myListener;                         //定位广播接收器


    private LocationClient mLocClient;                              //定位初始化


    private static String[] PERMISSIONS_MAIN = {
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.CAMERA"};


    //DB 好友
    private DAOmemberDao memberDao;

    List<Location> locations = new ArrayList<Location>();
    View view;

    private boolean isTime = true;

//    TimeThread timeThread = null;

    private int newtime = 1;
    private int start = 2;
    private int end = 3;
    private int stop = 4;



    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:

                    /*if(tv_trace_time != null){
                        tv_trace_time.setText(msg.obj.toString());
                    }*/

                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try {
            view = View.inflate(getActivity(), R.layout.fragment_map, null);
            setView();
            setListener();

        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
        return view;
    }

    private void setView() {



        location_img_button = (ImageButton) view.findViewById(R.id.location_img_button);

        location_img_button.setOnClickListener(this);

        // 地图初始化
        mMapView = (MapView) view.findViewById(R.id.bmapView);

      //  mMapView.addView();
        mBaiduMap = mMapView.getMap();

        startLocation();

        UiSettings uiSettings = mBaiduMap.getUiSettings();
        //设置是否允许旋转手势
        uiSettings.setRotateGesturesEnabled(false);

        //设置比例尺
        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("latlng", Context.MODE_PRIVATE);
        String str[] = new String[]{sharedPreferences.getString("longitude", ""), sharedPreferences.getString("latitude", "")};
        MapStatus mMapStatus;
        if(str != null && !str[0].equals("")){
            LogUtil.i(GlobalConsts.TAG,"本地储存有位置信息");
            LatLng cenpt = new LatLng(Double.valueOf(str[1]),Double.valueOf(str[0]));
            //定义地图状态
            mMapStatus = new MapStatus.Builder().target(cenpt).zoom(20).build();
        }else{
            mMapStatus = new MapStatus.Builder().target(null).zoom(20).build();
        }
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);


    }


    /**
     * 将View转换成Bitmap
     * @param addViewContent
     * @return
     */

    private Bitmap getViewBitmap(View addViewContent) {

        addViewContent.setDrawingCacheEnabled(true);

        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0,
                addViewContent.getMeasuredWidth(),
                addViewContent.getMeasuredHeight());

        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        return bitmap;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.location_img_button:
                isFirstLocation = true;

                break;
            /*case R.id.more_map:
                currentZoomLevel = mBaiduMap.getMapStatus().zoom;
                if (currentZoomLevel <= 20) {
//                  MapStatusUpdateFactory.zoomIn();
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());
                    less_map.setEnabled(true);
                } else {
                    Toast.makeText(getActivity(), "已经放至最大！", Toast.LENGTH_SHORT).show();
                    more_map.setEnabled(false);
                }

                break;
            case R.id.less_map:
                currentZoomLevel = mBaiduMap.getMapStatus().zoom;
                if (currentZoomLevel > 3) {
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomOut());
                    more_map.setEnabled(true);
                } else {
                    less_map.setEnabled(false);
                    Toast.makeText(getActivity(), "已经缩至最小！", Toast.LENGTH_SHORT).show();
                }

                break;*/
            /*case R.id.button_trace_start:

                if(isTime){
                    if(timeThread != null){

                        timeThread.setCode(end);

                    }

                    isTime = false;

                    bt_trace_start.setText("start");

                    bt_trace_start.setSelected(true);

                }else{

                    if(timeThread != null){

                        timeThread.setCode(start);

                    }

                    isTime = true;

                    bt_trace_start.setText("stop");

                    bt_trace_start.setSelected(false);
                }


                break;
            case R.id.button_trace_stop:

                startLocation();

                final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                View view = View.inflate(getActivity(), R.layout.dialog_edtext_layout, null);
                dialog.setView(view);
                dialog.setInverseBackgroundForced(true);
                dialog.setCancelable(false);

                final Button cancel = (Button) view.findViewById(R.id.button_cancel);
                final Button ok = (Button) view.findViewById(R.id.button_ok);
                final ImageView icon = (ImageView) view.findViewById(R.id.dialog_icon);
                final TextView title = (TextView) view.findViewById(R.id.title);
                final EditText editText = (EditText) view.findViewById(R.id.dialog_edtext);

                title.setText("请设置轨迹名称");
                editText.setHint("请输入名字");

                //dialog背景透明

                Window window = dialog.getWindow();

                window.setBackgroundDrawableResource(android.R.color.transparent);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        MonitorService.trajectoryName = "默认"+System.currentTimeMillis();


                        MonitorService.NOW_TRACE = MonitorService.END_TRACE;


                        Tools.showInfo(getActivity(),"结束轨迹");

                    //    getActivity().stopService(new Intent(getActivity(),MonitorService.class));

                        if(timeThread != null){

                            timeThread.setCode(stop);

                            timeThread = null;
                        }

                        linearLayoutTrace.setVisibility(View.GONE);

                        mBaiduMap.clear();

                        if(monitorService != null){

                            monitorService.stopSelf();
                        }

                        dialog.cancel();
                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        MonitorService.trajectoryName = editText.getText().toString();


                        MonitorService.NOW_TRACE = MonitorService.END_TRACE;


                        Tools.showInfo(getActivity(),"结束轨迹");

                //        getActivity().stopService(new Intent(getActivity(),MonitorService.class));

                        if(timeThread != null){

                            timeThread.setCode(stop);

                            timeThread = null;
                        }

                        linearLayoutTrace.setVisibility(View.GONE);

                        mBaiduMap.clear();

                        if(monitorService != null){

                            monitorService.stopSelf();
                        }

                        dialog.cancel();

                    }
                });
                dialog.show();


                break;*/

            default:
                break;
        }
    }

    private void setListener() {

        /**
         * 方向传感器监听事件
         * */
        /*mOrientationListener = new MyOrientationListener(getActivity());

        mOrientationListener.setmOnOrientationListener(new MyOrientationListener.OnOrientationListener() {

            @Override
            public void onOrientationChanged(float x) {
                mCurrentx = x;
            //    LogUtil.i(GlobalConsts.TAG,""+x);
                if(latLng != null){
                    MyLocationData locData = new MyLocationData.Builder()
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                            .direction(x).latitude(latLng.latitude)
                            .longitude(latLng.longitude).build();
                    mBaiduMap.setMyLocationData(locData);
                }
            }
        });*/

        mBaiduMap.setOnMyLocationClickListener(new BaiduMap.OnMyLocationClickListener() {
            @Override
            public boolean onMyLocationClick() {
                LogUtil.i(GlobalConsts.TAG,"点击定位图标");
                return false;
            }
        });
    }


    /**
     * 开启轨迹服务
     */
    private void startTrace() {
        // 通过轨迹服务客户端client开启轨迹服务
        monitorService = MonitorService.get_map_service(getActivity(), new Map_intercface() {
            @Override
            public void on_Location(final Location location, final double length, MonitorService map_service) {

                /*if(!isclick){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Tools.showInfo(getActivity(),"室内定位失败，请去宽敞地方");
                        }
                    });
                }*/

                /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date((System.currentTimeMillis()));
                String time = simpleDateFormat.format(date);*/

                monitorService = map_service;


                /*getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            tv_trace_km.setText(Math.round(length/100d)/10d+"KM");
                        } catch (Exception e) {
                            ExceptionUtil.handleException(e);
                        }
                    }
                });*/


                if(location != null){

                    showRealtimeTrack(location);

                }


            }
        });
    }

    /**
     * copy sms.db 用来查询本地数据库数据
     */
    private void stopTrace(){

        File file = new File(getActivity().getFilesDir().getParent(),"/databases/sms.db");

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

        File toFile = new File(path,"/copysms.db");

        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            if( ! toFile.exists()){
                toFile.createNewFile();
            }

            fis = new FileInputStream(file);
            fos = new FileOutputStream(toFile);

            byte[] buf = new byte[128];

            int len;

            while ((len = fis.read(buf)) != -1){
                fos.write(buf,0,len);
            }
            fos.flush();
        } catch (IOException e) {
            ExceptionUtil.handleException(e);
        }finally {

            try {
                fis.close();
                fos.close();
            } catch (IOException e) {
                ExceptionUtil.handleException(e);
            }
        }
    }


    /**
     * 显示实时轨迹
     *
     * @param location
     */
    protected void showRealtimeTrack(final Location location) {


        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        LatLng TrackingLatLng = new LatLng(latitude, longitude);

        /*getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Tools.showInfo(getActivity(),"定位数据="+location.toString()+",海拔："+location.getAltitude());
            }
        });*/

        LatLng sourceLatLng = TrackingLatLng;
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        converter.coord(sourceLatLng);
        TrackingLatLng = converter.convert();

        // 绘制实时点
        drawRealtimePoint(TrackingLatLng);

    }

    /**
     * 绘制实时点
     *
     * @param point
     */
    private void drawRealtimePoint(final LatLng point) {


        //

        MapStatus mMapStatus = new MapStatus.Builder().target(point).zoom(14).build();

        msUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);


        if (realtimeBitmap == null) {

            //自定义定位图标

            View popMarker = View.inflate(getActivity(), R.layout.baidumap_item_icon, null);

            CircleImageView img = (CircleImageView) popMarker.findViewById(R.id.icon);

            if(MyApplicatoin.evenUser != null){

                if(MyApplicatoin.evenUser.getAvatar_url() != null){

                    byte[] icon_date = Tools.readFileFromSdcard(new File(MyApplicatoin.evenUser.getAvatar_url()));

                    if(icon_date != null){

                        Bitmap bitmap = BitmapFactory.decodeByteArray(icon_date, 0, icon_date.length);

                        img.setImageBitmap(bitmap);
                    }
                }
            }

            Bitmap bitmap1 = getViewBitmap(popMarker);

            realtimeBitmap = BitmapDescriptorFactory.fromBitmap(bitmap1);
        }

        MarkerOptions overlayOptions = new MarkerOptions().position(point).icon(realtimeBitmap).zIndex(9).draggable(true);


        //将points集合中的点绘制轨迹线条图层，显示在地图上

        if (MyApplicatoin.pointList.size() >= 2) {
            // 添加路线（轨迹）
            polyline = new PolylineOptions().width(10).color(0xFF50D82E).points(MyApplicatoin.pointList);

            if(MonitorService.NOW_TRACE == MonitorService.END_TRACE){

                mBaiduMap.clear();

                mBaiduMap.setMyLocationEnabled(true);

            }else{

                mBaiduMap.clear();

                mBaiduMap.setMyLocationEnabled(false);

                addMarkerkk(overlayOptions);

            }

        }


    }

    /**
     * 添加地图覆盖物
     * @param overlayOptions
     */
    public void addMarkerkk(MarkerOptions overlayOptions) {

        if (null != msUpdate) {
            mBaiduMap.setMapStatus(msUpdate);
        }

        // 路线覆盖物
        if (null != polyline) {
            mBaiduMap.addOverlay(polyline);
        }

        // 实时点覆盖物
        if (null != overlayOptions) {
            mBaiduMap.addOverlay(overlayOptions);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        // 当不需要定位图层时关闭定位图层

        if(mLocClient !=null){
            mLocClient.stop();
        }

        if(mBaiduMap != null){
            mBaiduMap.setMyLocationEnabled(false);
        }

        if(mOrientationListener != null){
            mOrientationListener.stop();
        }

        if(mMapView != null){
            mMapView.onDestroy();
        }

    }


    /**
     * 绘制历史轨迹
     *
     * @param points
     */
    public void drawHistoryTrack(final List<LatLng> points) {
        // 绘制新覆盖物前，清空之前的覆盖物
        mBaiduMap.clear();

        if (points.size() > 1) {
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

            // 添加路线（轨迹）
            polyline = new PolylineOptions().width(10).color(0xFFFF692A).points(points);

            markerOptions = new MarkerOptions();
            markerOptions.flat(true);
            markerOptions.anchor(0.5f, 0.5f);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_gcoding));
            markerOptions.position(points.get(points.size() - 1));

            addMarker();
        }

    }

    /**
     * 添加覆盖物
     */
    public void addMarker() {

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
    public void onResume() {
        super.onResume();

    }


    /**
     *
     * 开启定位
     * */
    public void startLocation() {

        LogUtil.i(GlobalConsts.TAG,"startLocation");

    //    mBaiduMap.clear();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 定位初始化
        mLocClient = new LocationClient(getActivity());
        myListener = new MyLocationListenner();
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开GPS
        option.setNeedDeviceDirect(true);//在网络定位时，是否需要设备方向 true:需要 ; false:不需要。
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(1000);// 设置发起定位请求的间隔时间为1000ms
        option.disableCache(false);// 禁止启用缓存定位
        option.setIsNeedAddress(true);//设置是否需要地址信息，默认为无地址
        option.setIsNeedAltitude(true);
        mLocClient.setLocOption(option);// 使用设置
        mLocClient.start();// 开启定位SDK


    }


    int time = 0;

    /**
     * 定位结果回调监听
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation location) {

            if (location != null) {

                /*String addr = location.getAddrStr();    //获取详细地址信息
                String country = location.getCountry();    //获取国家
                String province = location.getProvince();    //获取省份
                String city = location.getCity();    //获取城市
                String district = location.getDistrict();    //获取区县
                String street = location.getStreet();    //获取街道信息*/


                /*if(String.valueOf(location.getLatitude()).equals("4.9E-324")){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Tools.showInfo(getActivity(),"GPS信号弱,请检查权限，GPS及网络是否打开,请去室外宽阔地方，");
                            LogUtil.i(GlobalConsts.TAG,"定位失败");
                            return ;
                        }

                    });
                }*/


                latLng = new LatLng(location.getLatitude(), location.getLongitude());


                mCurrentx = location.getDirection();


                MyLocationData locData = new MyLocationData.Builder().accuracy(0).direction(0).
                        latitude(latLng.latitude).
                        longitude(latLng.longitude)
                        .build();



                final Location l = new Location("");

                /*//自定义定位图标

                View popMarker = View.inflate(getActivity(), R.layout.baidumap_item_icon_to, null);
                CircleImageView img = (CircleImageView) popMarker.findViewById(R.id.icon);

                byte[] icon_date = Tools.readFileFromSdcard(new File(MyApplicatoin.evenUser.getAvatar_url()));

                if(icon_date != null){

                    Bitmap bitmap = BitmapFactory.decodeByteArray(icon_date, 0, icon_date.length);

                    img.setImageBitmap(bitmap);

                }
                Bitmap bitmap1 = getViewBitmap(popMarker);

                mCurrentMarker = BitmapDescriptorFactory.fromBitmap(bitmap1);
                MyLocationConfiguration config = new MyLocationConfiguration(null, true, mCurrentMarker);
                mBaiduMap.setMyLocationConfigeration(config);*/




                // 发送APRS定位数据
                if(MyApplicatoin.IS_BLE){

                    time++;

                    if(time > 20){

                        LatLng sourceLatLng = latLng;
                        CoordinateConverter converter = new CoordinateConverter();
                        converter.from(CoordinateConverter.CoordType.GPS);
                        converter.coord(sourceLatLng);
                        LatLng latlng = converter.convert();


                        double latitude = 2 * latLng.latitude - latlng.latitude;

                        double longitude = 2 * latLng.longitude - latlng.longitude;


                        l.setTime(MyApplicatoin.evenUser.getId());

                        l.setLatitude(latitude);
                        l.setLongitude(longitude);


                        Interphone interphone = new Interphone();

                        interphone.setLocation(l);

                        start_ble(sms_request.SET,interphone);



                        Openfrom from = new Openfrom();

                        long s = getRandom();

                        from.setType(MsgResponse.Aprs);

                        from.setMessage(latitude+","+longitude);

                        from.setCode(Open_biz.SendMessage);

                        from.setMessageID(s);

                        MyApplicatoin.setOpenfire(from);

                        time = 0;
                    }

                }


                LatLng sourceLatLng = latLng;
                CoordinateConverter converter = new CoordinateConverter();
                converter.from(CoordinateConverter.CoordType.GPS);
                converter.coord(sourceLatLng);
                LatLng latlng = converter.convert();


                double latitude = 2 * latLng.latitude - latlng.latitude;

                double longitude = 2 * latLng.longitude - latlng.longitude;

                Location locion = new Location("");

                locion.setLatitude(latitude);
                locion.setLongitude(longitude);


                boolean isExist  = false;

                for(Long key : MyApplicatoin.mapList.keySet()){

                    if(MyApplicatoin.evenUser.getId() == key){

                        isExist = true;
                        MyApplicatoin.mapList.get(key).set(locion);

                    }

                }

                if(!isExist){

                    MyApplicatoin.mapList.put(MyApplicatoin.evenUser.getId(),locion);

                }


                /*mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker, accuracyCircleFillColor, accuracyCircleStrokeColor));*/
                //   mCurrentMarker = BitmapDescriptorFactory.fromResource(R.mipmap.r8);
            //    mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));
            //    mBaiduMap.setMyLocationData(locData);

                //将定位数据存储起来方便下次启动时直接显示。
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("latlng", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("longitude", String.valueOf(location.getLongitude()));
                editor.putString("latitude", String.valueOf(location.getLatitude()));
                editor.commit();

                //开启方向传感器
            //    mOrientationListener.start();

                if(isFirstLocation){
                    //获取经纬度

                    LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
                    MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(ll);
                    //mBaiduMap.setMapStatus(status);//直接到中间
                    mBaiduMap.animateMapStatus(status);//动画的方式到中间
                    isFirstLocation = false;
                    setMap();
                }
            }
        }


    }


    private long getRandom() {

        int s = 0;

        try {
            int max=6535;

            int min=0;

            Random random = new Random();

            s = random.nextInt(max)%(max-min+1) + min;

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return s;
        }

    }

    public void setMap(){

        mBaiduMap.clear();

        DAOmemberDao daOmemberDao =  DAOService.get(getActivity()).getsession().getDAOmemberDao();

        List<DAOmember> memberList = daOmemberDao.loadAll();

        for(Long key : MyApplicatoin.mapList.keySet()){



            Location lociona = MyApplicatoin.mapList.get(key);

            //将GPS 坐标转换成百度坐标

            LatLng latLng = new LatLng(lociona.getLatitude(), lociona.getLongitude());
            LatLng sourceLatLng = latLng;
            CoordinateConverter converter = new CoordinateConverter();
            converter.from(CoordinateConverter.CoordType.GPS);
            converter.coord(sourceLatLng);
            latLng = converter.convert();



            DAOmember menber = null;

            for(DAOmember m : memberList){

                if(m.getUser_id() == key){
                    menber = m;
                }

            }

            View popMarker = View.inflate(getActivity(), R.layout.baidumap_item_icon, null);
            TextView tv = (TextView) popMarker.findViewById(R.id.text);
            ImageView imageView = popMarker.findViewById(R.id.image);

            if(key != MyApplicatoin.evenUser.getId()){
                imageView.setImageResource(R.mipmap.sms_main_map_location2);

            }

            CircleImageView img = (CircleImageView) popMarker.findViewById(R.id.icon);

            if(menber != null){

                if(menber.getAvatar_url() != null){


                    byte[] icon_date = Tools.readFileFromSdcard(new File(menber.getAvatar_url()));

                    if(icon_date != null){

                        Bitmap bitmap = BitmapFactory.decodeByteArray(icon_date, 0, icon_date.length);

                        img.setImageBitmap(bitmap);

                    }

                }

            }


            tv.setText(String.valueOf(key));
        //    tv.setVisibility(View.VISIBLE);
            Bitmap bitmap1 = getViewBitmap(popMarker);


                               /* OverlayOptions options = new MarkerOptions()
                                        .position(latLng)  //设置marker的位置
                                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap1))  //设置marker图标
                                        .zIndex(9)  //设置marker所在层级
                                        .draggable(false);  //设置手势拖拽*/
            //将marker添加到地图上

            MarkerOptions ooD = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(bitmap1))
                    .zIndex(0).period(10);

            // 生长动画
            ooD.animateType(MarkerOptions.MarkerAnimateType.grow);

            Marker mMarkerD = (Marker) (mBaiduMap.addOverlay(ooD));

            //    mBaiduMap.addOverlay(options);
        }

    }

    private void start_ble(int set, Interphone interphone) {


        if (MyApplicatoin.IS_BLE) {

            sms_fw_api.do_request(getActivity(), set, interphone, new RequestCB() {
                @Override
                public void respone(int opcode, Object obj) {


                }
            });

            sms_fw_api.add_attr_listenner(getActivity(), new attr_listenner() {
                @Override
                public void onAttributeChang(int code, Object object) {

                    //    LogUtil.i(GlobalConsts.TAG,"add_attr_listenner:"+code);

                    if(code == Opcode.MESG){

                        LogUtil.i(GlobalConsts.TAG,"fragment_map收到BLE消息");

                        final Ble_message message = (Ble_message) object;

                        if(message.getType() == Ble_message.aprs) {



                            mBaiduMap.clear();

                            DAOmemberDao daOmemberDao =  DAOService.get(getActivity()).getsession().getDAOmemberDao();

                            List<DAOmember> memberList = daOmemberDao.loadAll();


                            Location locion = new Location("");

                            locion.setLongitude(message.getLongitude());
                            locion.setLatitude(message.getLatitude());

                         /*   if( MyApplicatoin.mapList.get(message.getUserId()) == null){

                                MyApplicatoin.mapList.put(message.getUserId(),locion);

                            }else{

                                MyApplicatoin.mapList.get(message.getUserId()).set(locion);

                            }*/

                            boolean isExist  = false;

                            for(Long key : MyApplicatoin.mapList.keySet()){

                                if(message.getUserId() == key){

                                    isExist = true;
                                    MyApplicatoin.mapList.get(key).set(locion);

                                }

                            }

                            if(!isExist){

                                MyApplicatoin.mapList.put(message.getUserId(),locion);

                            }

                            for(Long key : MyApplicatoin.mapList.keySet()){



                                Location lociona = MyApplicatoin.mapList.get(key);

                                //将GPS 坐标转换成百度坐标

                                LatLng latLng = new LatLng(lociona.getLatitude(), lociona.getLongitude());
                                LatLng sourceLatLng = latLng;
                                CoordinateConverter converter = new CoordinateConverter();
                                converter.from(CoordinateConverter.CoordType.GPS);
                                converter.coord(sourceLatLng);
                                latLng = converter.convert();



                                DAOmember menber = null;

                                for(DAOmember m : memberList){

                                    if(m.getUser_id() == key){
                                        menber = m;
                                    }

                                }

                                View popMarker = View.inflate(getActivity(), R.layout.baidumap_item_icon, null);
                                TextView tv = (TextView) popMarker.findViewById(R.id.text);
                                CircleImageView img = (CircleImageView) popMarker.findViewById(R.id.icon);

                                if(menber != null){

                                    if(menber.getAvatar_url() != null){


                                        byte[] icon_date = Tools.readFileFromSdcard(new File(MyApplicatoin.evenUser.getAvatar_url()));

                                        if(icon_date != null){

                                            Bitmap bitmap = BitmapFactory.decodeByteArray(icon_date, 0, icon_date.length);

                                            img.setImageBitmap(bitmap);

                                        }

                                    }

                                }


                                tv.setText(String.valueOf(message.getUserId()));
                                tv.setVisibility(View.VISIBLE);
                                Bitmap bitmap1 = getViewBitmap(popMarker);


                               /* OverlayOptions options = new MarkerOptions()
                                        .position(latLng)  //设置marker的位置
                                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap1))  //设置marker图标
                                        .zIndex(9)  //设置marker所在层级
                                        .draggable(false);  //设置手势拖拽*/
                                //将marker添加到地图上

                                MarkerOptions ooD = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(bitmap1))
                                        .zIndex(0).period(10);

                                // 生长动画
                                ooD.animateType(MarkerOptions.MarkerAnimateType.grow);

                                Marker mMarkerD = (Marker) (mBaiduMap.addOverlay(ooD));

                            //    mBaiduMap.addOverlay(options);
                            }

                        }
                    }
                }
            });
        } else {
            LogUtil.i(GlobalConsts.TAG,"BLE设备未连接，或连接断开！");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode){

            case GlobalConsts.ACCESS_FINE_LOCATION:

                LogUtil.i(GlobalConsts.TAG,"onRequestPermissionsResult:"+grantResults[0]);
                if(grantResults[0] == 0){
                    startLocation();
                }else{
                    LogUtil.i(GlobalConsts.TAG,"用户不给权限，跳转到设置");
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", "com.example.ys.androidapplication", null);
                    intent.setData(uri);
                    startActivity(intent);
                }
                break;

            default:
                break;
        }
    }



    public void setDragLayoutClick(int code){

        switch (code) {
            case 1:

                //开始轨迹
                if(MonitorService.NOW_TRACE == MonitorService.START_TRACE){

                    Tools.showInfo(getActivity(),"轨迹已经开启。");

                } else if (MonitorService.NOW_TRACE == MonitorService.STOP_TRACE) {

                    MonitorService.NOW_TRACE = MonitorService.STOP_TRACE;
                    Tools.showInfo(getActivity(),"正在恢复轨迹。");

                } else {
                    MonitorService.NOW_TRACE = MonitorService.START_TRACE;
                    startTrace();
                    Tools.showInfo(getActivity(),"正在开启轨迹，请稍后...");

                   /* if(timeThread != null){

                        timeThread.setCode(start);

                    }else{

                        timeThread = new TimeThread(handler);
                        Thread thread = new Thread(timeThread);
                        timeThread.setCode(start);
                        thread.start();
                    }*/
                }

                break;
            case 2:

                //离线地图
                Intent intent = new Intent(getActivity(),OffilneActivity.class);
                startActivity(intent);

                break;
            case 3:

                //切换地图


                final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                View view = View.inflate(getActivity(), R.layout.dialog_mapswitch_layout, null);
                dialog.setView(view);
                dialog.setInverseBackgroundForced(true);
                dialog.setCancelable(false);

                final Button bt1 = (Button) view.findViewById(R.id.button_cancel);
                final Button bt2 = (Button) view.findViewById(R.id.button_ok);


                //dialog背景透明

                Window window = dialog.getWindow();
                window.setBackgroundDrawableResource(android.R.color.transparent);

                bt1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        isMap = true;
                        showMap(isMap);
                        dialog.cancel();
                    }
                });
                bt2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        isMap = false;
                        showMap(isMap);
                        dialog.cancel();

                    }
                });
                dialog.show();


                break;
            case 4:

                startActivity(new Intent(getActivity(), TrackingAvtivity.class));


                break;
            case 5:




                break;

            default:
                break;

        }
    }


}
