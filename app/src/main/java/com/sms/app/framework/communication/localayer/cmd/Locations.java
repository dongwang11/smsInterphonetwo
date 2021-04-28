package com.sms.app.framework.communication.localayer.cmd;

import android.location.Location;

import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/3/7.
 */

public class Locations {


    /**
     *
     * 将百度坐标转换成度分秒格式
     *
     * */
    public static byte[] new_byte_Locations(int usre , Location location) {

        /**
         *
         * 假设你有百度坐标：longitude(经度)=116.397428，latitude(纬度)=39.90923
         * 把这个坐标当成GPS坐标，通过接口获得他的百度坐标：x2=116.41004950566，y2=39.916979519873
         * 通过计算就可以得到GPS的坐标：
         * x = 2*x1-x2，y = 2*y1-y2
         * x=116.38480649434001
         * y=39.901480480127
         * */
        /*LatLng sourceLatLng = latLng;
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        converter.coord(sourceLatLng);
        latLng = converter.convert();

        double gps_latitude = 2 * latLng.latitude - latLng.latitude;
        double gps_longitude = 2 * latLng.longitude - latLng.longitude;*/

        double gps_latitude = location.getLatitude();
        double gps_longitude = location.getLongitude();

    //    LogUtil.i(TAG,"测试经纬度转换="+gps_longitude+","+gps_latitude);


        /**
         * double类型获取小数点前的整数
         *
         * 经度
         * 度
         * */

        //小数部分
        double longitude_b = gps_longitude % 1.0;
        //整数部分
        int longitude_c = (int)(gps_longitude - longitude_b);

        /**
         * 分
         * */

        double longitude_fen = longitude_b * 60;

        //小数部分
        double longitude_m = longitude_fen % 1.0;
        //整数部分
        int longitude_f_en = (int)(longitude_fen - longitude_m);


        /**
         * 秒
         * */
        double longitude_miao = longitude_m * 60;

        //小数部分
        double longitude_h = longitude_miao % 1.0;
        //整数部分
        int longitude_mi_ao = (int)(longitude_miao - longitude_h);


        /**
         * 秒点
         * */
        double longitude_miao_dian = longitude_h * 60;


        int longitude_mi_ao_dian = (int) Math.round(longitude_miao_dian);


        LogUtil.i(GlobalConsts.TAG,"度："+longitude_c+",分："+longitude_f_en+",秒："+longitude_mi_ao+",秒点："+longitude_mi_ao_dian);
        /**
         * double类型获取小数点前的整数
         *
         * 纬度
         * 度
         * */

        //小数部分
        double latitude_b = gps_latitude % 1.0;
        //整数部分
        int latitude_c = (int)(gps_latitude - latitude_b);
        /**
         * 分
         * */

        double latitude_fen = latitude_b * 60;

        //小数部分
        double latitude_m = latitude_fen % 1.0;
        //整数部分
        int latitude_f_en = (int)(latitude_fen - latitude_m);


        /**
         * 秒
         * */
        double latitude_miao = latitude_m * 60;

        //小数部分
        double latitude_h = latitude_miao % 1.0;
        //整数部分
        int latitude_mi_ao = (int)(latitude_miao - latitude_h);


        /**
         * 秒点
         * */
        double latitude_miao_dian = latitude_h * 60;

        //整数部分
        int latitude_mi_ao_dian = (int)Math.round(latitude_miao_dian);




        LogUtil.i(GlobalConsts.TAG,"度："+latitude_c+",分："+latitude_f_en+",秒："+latitude_mi_ao+",面点："+latitude_mi_ao_dian);



        int longitude_type = 0;

        int latitude_type = 0;

        if(longitude_c < 0){
            longitude_type = 1;
        }

        if(latitude_c < 0){
            latitude_type = 0;
        }

        int i = 0;
        byte [] array = new byte[4+7];

        byte [] arrays = aprs_enc_dmss(longitude_type,longitude_c,longitude_f_en,longitude_mi_ao,longitude_mi_ao_dian,latitude_type,latitude_c,latitude_f_en,latitude_mi_ao,latitude_mi_ao_dian);

        LogUtil.i(GlobalConsts.TAG,"位置数据："+ Arrays.toString(arrays));
        LogUtil.i(GlobalConsts.TAG,"Location："+location.toString());
        LogUtil.i(GlobalConsts.TAG,"usre："+usre);


        array[i++] = (byte) (usre & 0xff);
        array[i++] = (byte) (usre >> 8);
        array[i++] = (byte) (usre >> 16);
        array[i++] = (byte) (usre >> 24);

        for(byte bas : arrays){
            array[i++] = bas;
        }

        return array;
    }


    public static int get_bits(int value,int start,int end){

        return (int)(((0xff << start) & (0xff >> (7 - end))) & value);
    }

    public static byte[] aprs_enc_dmss(int lon_type, int lon_d, int lon_m, int lon_s, int lon_ss, int lat_type, int lat_d, int lat_m, int lat_s, int lat_ss){

        int  second = 0;
        byte[] array = new byte[7];
        if(array == null) {
            return null;
        }
        second = lon_d*3600 + lon_m*60 + lon_s;
        array[0] = (byte) (lon_type << 7);
        array[0] |= (byte) ((second >>(20 -7)) & 0x7f);
        array[1] = (byte)(second>>(20 - 7 - 8));
        array[2] = (byte)((second & 0x1f) << 3);
        array[2] |= (byte)(lon_ss >> 4);
        array[3] = (byte)((lon_ss & 0x0f)<<4);
        array[3] |= (byte) (lat_type << 3);
        second = lat_d*3600 + lat_m*60 + lat_s;
        array[3] |= (byte)( (second>>(20 - 3))&0x07);
        array[4] = (byte) (second >> (20 - 3 - 8));
        array[5] = (byte) (second >> (20 - 3 - 8 -8));
        array[6] = (byte) ((second & 0x01) << 7);
        array[6] |= (byte)(lat_ss&0x7f);
        return array;
    }

    public static Location aprs_dec_dmss(byte array[]) {
        int x,y;
        char lon_type,lat_type;
        byte lon_ss,lat_ss;
        int lon_second,lat_second;

        double f_lon = 0.0f, f_lat = 0.0f;

        if(array == null ) {
            return null;
        }

        x = get_bits(array[0],7,7);
        lon_type = (char) (x >> 7);

        x = get_bits(array[0],0,6);
        y = get_bits(array[1],7,7);
        lon_second = ((x << 1)|(y >> 7));
        x = get_bits(array[1],0,6);
        y = get_bits(array[2],7,7);
        lon_second = (lon_second << 8)|((x << 1)|(y >> 7));
        x = get_bits(array[2],3,6);
        lon_second = (lon_second << 4)|(x >> 3);
        x = get_bits(array[2],0,2);
        y = get_bits(array[3],4,7);
        lon_ss = (byte) ((x << 4) | ( y >> 4));
        x = get_bits(array[3],3,3);
        lat_type = (char) (x >> 3);
        x = get_bits(array[3],0,2);
        y = get_bits(array[4],3,7);
        lat_second = (x << 5) | (y >> 3);
        x = get_bits(array[4],0,2);
        y = get_bits(array[5],3,7);
        lat_second = (lat_second << 8) | ((x << 5)|(y >> 3));
        x = get_bits(array[5],0,2);
        y = get_bits(array[6],7,7);
        lat_second = (lat_second << 4) | ((x << 1)|(y>>7));
        x = get_bits(array[6],0,6);
        lat_ss = (byte) x;


        LogUtil.i(GlobalConsts.TAG,"lon_second:"+lon_second+",lon_ss:"+lon_ss+",lat_second:"+lat_second+",lat_ss:"+lat_ss);


        f_lon = ((lon_second + (lon_ss + 0.0)/100 )/3600) ;

        f_lat = ((lat_second + (lat_ss + 0.0)/100 )/3600 );

        lon_type = (char) ((lon_type == 0)?('E'):('W'));
        lat_type = (char) ((lat_type == 1)?('S'):('N'));
        LogUtil.i(GlobalConsts.TAG,"f_lon:"+f_lon+",f_lat:"+f_lat+",lon_type"+lon_type+",lat_type"+lat_type);
        // 0  = 正  1 = 负
        if(lon_type == 1){
            f_lon = unAbs(f_lon);
        }

        if(lat_type == 1 ){
            f_lat = unAbs(f_lat);
        }

        Location location = new Location("");

        location.setLongitude(f_lon);

        location.setLatitude(f_lat);

    //    new_byte_Locations(5,location);

        return location;
    }

    public static double unAbs(double a) {
        return (a > 0) ? -a : a;
    }
}
