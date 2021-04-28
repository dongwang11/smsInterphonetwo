package com.sms.app.interphone.util.maputil;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class BaiduMapUtil {

    private static final int INSTALL = 10;

    //标注对象经纬度集合
//    private List<LatLng> list = new ArrayList<LatLng>();

    //折线集合
    private List<Polyline> mPolyline = new ArrayList<Polyline>();

    private float[] vertexs;
    private FloatBuffer vertexBuffer;
    //添加地图标注对象
    private MarkerOptions markerOptions = null;


    LatLng ak;

    private Marker marker;

    BaiduMap mBaiduMap;
    Context context;


    public void loading(Activity activity, BaiduMap mBaiduMaptt) {
        this.mBaiduMap = mBaiduMaptt;
        this.context = activity;

        //添加标注对象经纬度对象
    //    list.add(new LatLng(23.13130069218, 113.40921192729));
    //    list.add(new LatLng(23.169684593811, 113.38758430828));

        //设定中心点坐标

        SharedPreferences sharedPreferences = context.getSharedPreferences("latlng", Context.MODE_PRIVATE);
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
        setLineaer();
    }

    private void setLineaer() {

        /**
         * 设置地图标注对象
         * */
        /*if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                MapStatusUpdate uuu = MapStatusUpdateFactory.newLatLng(list.get(i));
                mBaiduMap.animateMapStatus(uuu);
                markerOptions = new MarkerOptions();
                markerOptions.title("第" + i + "Marker被点击了");
                markerOptions.position(list.get(i));
                //            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
                //            mBaiduMap.addOverlay(markerOptions);
                OverlayOptions options = new MarkerOptions()
                        .position(list.get(i))  //设置marker的位置
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.positionred))  //设置marker图标
                        .zIndex(9)  //设置marker所在层级
                        .draggable(true);  //设置手势拖拽
                //将marker添加到地图上
                marker = (Marker) (mBaiduMap.addOverlay(options));
            }
        }


        //调用BaiduMap对象的setOnMarkerDragListener方法设置marker拖拽的监听
        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
                //拖拽中
            }

            public void onMarkerDragEnd(Marker marker) {
                //拖拽结束
            }

            public void onMarkerDragStart(Marker marker) {
                //开始拖拽
            }
        });*/

        /**
         * 标注对象点击事件
         * */

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                if (mPolyline.size() != 0) {
                    for (Polyline activity : mPolyline) {
                        try {
                            activity.remove();
                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        }
                    }
                }

                double a = marker.getPosition().latitude;
                double b = marker.getPosition().longitude;
                double c = ak.latitude;
                double d = ak.longitude;

                List<LatLng> listto = new ArrayList<LatLng>();
                listto.add(new LatLng(a, b));
                listto.add(new LatLng(c, d));

                //画线
                OverlayOptions ooPolyline = new PolylineOptions().width(6).color(Color.RED).points(listto);
                // 添加在地图中
                Polyline mPolylines = (Polyline) mBaiduMap.addOverlay(ooPolyline);
                mPolyline.add(mPolylines);

                double dis = 0;
                if (ak != null) {
                    double aa = DistanceUtil.getDistance(ak, marker.getPosition());
                    int i = (int) aa;
                    dis = Math.round(i / 100d) / 10d;
                }


                return false;
            }
        });

        /**
         *地图长按事件监听回调函数
         * @param point 长按的地理坐标
         * */
       /* mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Toast.makeText(context, "" + latLng.toString(), Toast.LENGTH_SHORT).show();
                MapStatusUpdate uuu = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(uuu);
                markerOptions = new MarkerOptions();
                //两点之间的距离
                    *//*DecimalFormat df = new DecimalFormat("#.");
                    String aa=df.format(DistanceUtil. getDistance(ak, latLng));
                    if(aa.equals(".")){
                       aa="0";
                    }*//*
                double aa = DistanceUtil.getDistance(ak, marker.getPosition());
                int i = (int) aa;
                double dis = Math.round(i / 100d) / 10d;
                markerOptions.title(String.valueOf(dis) + "公里");
                markerOptions.position(latLng);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.positionblack));
                mBaiduMap.addOverlay(markerOptions);

            }
        });*/
    }

    private void setkate() {
        mBaiduMap.setOnMapDrawFrameCallback(new BaiduMap.OnMapDrawFrameCallback() {
            @Override
            public void onMapDrawFrame(GL10 gl10, MapStatus mapStatus) {
                if (mBaiduMap.getProjection() != null) {
                    // 计算折线的 opengl 坐标
                   // calPolylinePoint(mapStatus);
                    // 绘制折线
                   // drawPolyline(gl10, Color.argb(255, 255, 0, 0), vertexBuffer, 10, 3, mapStatus);
                }
            }

            @Override
            public void onMapDrawFrame(MapStatus mapStatus) {

            }


        });

    }




    // 计算折线 OpenGL 坐标
    public void calPolylinePoint(MapStatus mspStatus) {
        /*PointF[] polyPoints = new PointF[list.size()];
        vertexs = new float[3 * list.size()];
        int i = 0;
        for (LatLng xy : list) {
            // 将地理坐标转换成 openGL 坐标
            polyPoints[i] = mBaiduMap.getProjection().toOpenGLLocation(xy, mspStatus);
            vertexs[i * 3] = polyPoints[i].x;
            vertexs[i * 3 + 1] = polyPoints[i].y;
            vertexs[i * 3 + 2] = 0.0f;
            i++;
        }*/

        vertexBuffer = makeFloatBuffer(vertexs);
    }

    //创建OpenGL绘制时的顶点Buffer
    private FloatBuffer makeFloatBuffer(float[] fs) {
        ByteBuffer bb = ByteBuffer.allocateDirect(fs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(fs);
        fb.position(0);
        return fb;
    }

    // 绘制折线
    private void drawPolyline(GL10 gl, int color, FloatBuffer lineVertexBuffer, float lineWidth, int pointSize, MapStatus drawingMapStatus) {

        gl.glEnable(GL10.GL_BLEND);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        float colorA = Color.alpha(color) / 255f;
        float colorR = Color.red(color) / 255f;
        float colorG = Color.green(color) / 255f;
        float colorB = Color.blue(color) / 255f;




        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, lineVertexBuffer);
        gl.glColor4f(colorR, colorG, colorB, colorA);
        gl.glLineWidth(lineWidth);
        gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, pointSize);

        gl.glDisable(GL10.GL_BLEND);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}
