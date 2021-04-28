/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sms.app.framework.communication.localayer.cmd;


import com.sms.app.framework.communication.localayer.bledriver.BluetoothLeService;

/**
 * @author Administrator
 */

public class ATcommand {


    static final String AT = "AT+";

    /**
     * byte opcode    操作
     * byte objcede   类型
     * byte param     数据
     */
    public static byte[] at_cmd(byte opcode, byte objcode, byte[] param) {
        int i = 0;
        byte[] cmd = new byte[ATcommand.AT.length() + param.length + 4 + 1];
        cmd[0] = 'A';
        cmd[1] = 'T';
        cmd[2] = '+';
        cmd[3] = opcode;
        cmd[4] = objcode;
        cmd[5] = (byte) (param.length & 0xff);
        cmd[6] = (byte) (param.length >> 8);

        for (i = 0; i < param.length; i++) {
            cmd[7 + i] = param[i];
        }
        int crc = CRC.crc8(cmd, cmd.length - 1);
        cmd[7 + i] = (byte) (crc & 0xff);
        //  cmd[7+i+1] =(byte)(crc>>8);
        return cmd;
    }


    public static byte[] at_cmd_versions(byte objcode, byte[] param) {
        int i = 0;
        byte[] cmd = new byte[param.length + 1];
        cmd[0] = objcode;

        for (i = 0; i < param.length; i++) {
            cmd[1 + i] = param[i];
        }
        return cmd;
    }

    public static byte[] at_cmd_versions_crc(byte objcode, byte[] param) {
        int i = 0;
        byte[] cmd = new byte[ATcommand.AT.length() + param.length + 3 + 1];
        cmd[0] = 'A';
        cmd[1] = 'T';
        cmd[2] = '+';
        cmd[3] = objcode;
        cmd[4] = (byte) (param.length & 0xff);
        cmd[5] = (byte) (param.length >> 8);

        for (i = 0; i < param.length; i++) {
            cmd[6 + i] = param[i];
        }
        int crc = CRC.crc8(cmd, cmd.length - 1);
        cmd[6 + i] = (byte) (crc & 0xff);
        //  cmd[7+i+1] =(byte)(crc>>8);
        return cmd;
    }


    /**
     * 发送文件之间调用该函数，用来发送文件的基本信息（也可以再用OTA升级发送固件基本信息）
     *
     * 报文格式：AT+SET FILE_INFO NAME,VERSION,SIZE(4BYTE 小端格式)+ED+CRC16,
     */
    public static byte[] at_set_file_info(String name, String version, int size) {
        int i = 0;
        byte param[] = new byte[name.getBytes().length + version.getBytes().length + 6];
        byte[] name_byte = name.getBytes();
        for (int j = 0; j < name_byte.length; j++, i++) {
            param[i] = name_byte[j];
        }
        param[i++] = ',';
        byte[] version_byte = version.getBytes();
        for (int j = 0; j < version_byte.length; j++, i++) {
            param[i] = version_byte[j];
        }
        param[i++] = ',';
        param[i++] = (byte) (size & 0xff);
        param[i++] = (byte) (size >> 8);
        param[i++] = (byte) (size >> 16);
        param[i++] = (byte) (size >> 24);

        return at_cmd(Opcode.SET, Objcode.FILE_INFO, param);
    }

    /**
     * 发送文件之间调用该函数，用来发送文件的基本信息（也可以再用OTA升级发送固件基本信息）
     *
     * 报文格式：AT+SET FILE_INFO NAME,VERSION,SIZE(4BYTE 小端格式)+ED+CRC16,
     */
    public static byte[] at_set_file_info_versions(byte code,String name, String version, int size) {
        int i = 0;
        byte param[] = new byte[name.getBytes().length + version.getBytes().length + 4];
        byte[] name_byte = name.getBytes();
        for (int j = 0; j < name_byte.length; j++, i++) {
            param[i] = name_byte[j];
        }
        param[i++] = ',';
        byte[] version_byte = version.getBytes();
        for (int j = 0; j < version_byte.length; j++, i++) {
            param[i] = version_byte[j];
        }
        param[i++] = ',';
        param[i++] = (byte) (size & 0xff);
        param[i++] = (byte) (size >> 8);

        return at_cmd_versions_crc(code, param);
    }


/**
 * 一份比较大的文件需要分开多个部分发送，该函数用来发送部分的文件，原则上采用1024byte大小进行文件分割
 报文格式：AT+SET+FILE_PART+part_num(2byte 第几部分的文件块)+size(2byte 该部分文件片段的大小)+文件片段的数据+ED+CRC16
 */

    /**
     * part_num  第几包
     * size      大小
     * file_part 数据
     */
    public static byte[] at_send_order_versions(byte code, short part_num, short size, byte[] file_part) {
        int i = 0;
        byte param[] = new byte[4 + file_part.length];
        param[i++] = (byte) (part_num & 0xff);
        param[i++] = (byte) (part_num >> 8);
        param[i++] = (byte) (size & 0xff);
        param[i++] = (byte) (size >> 8);
        for (int j = 0; j < file_part.length; j++, i++) {
            param[i] = file_part[j];
        }

        return at_cmd_versions_crc(code, param);
    }

    /**
     * part_num  第几包
     * size      大小
     * file_part 数据
     */
    public static byte[] at_send_order(byte objcode, short part_num, short size, byte[] file_part) {
        int i = 0;
        byte param[] = new byte[4 + file_part.length];
        param[i++] = (byte) (part_num & 0xff);
        param[i++] = (byte) (part_num >> 8);
        param[i++] = (byte) (size & 0xff);
        param[i++] = (byte) (size >> 8);
        for (int j = 0; j < file_part.length; j++, i++) {
            param[i] = file_part[j];
        }

        return at_cmd(Opcode.SET, objcode, param);
    }

    /**
     *
     * AT命令类型
     *
     * opcode    命令类型
     * objcode   命令对象
     * code      对象属性
     * file_part    数值
     */
    public static byte[] at_set_file_part(byte opcode, byte objcode, byte code , byte[] file_part) {
        int i = 0;
        byte param[] = new byte[1 + file_part.length];
        param[i++] =code;

        for (int j = 0; j < file_part.length; j++, i++) {
            param[i] = file_part[j];
        }

        return at_cmd(opcode, objcode, param);
    }

    public static byte[] at_set_file_user_part(byte opcode, byte objcode, int package_id, int user_id, byte[] file_part) {
        int i = 0;
        byte param[] = new byte[9 + file_part.length];
        param[i++] =objcode;
        param[i++] = (byte) (package_id & 0xff);
        param[i++] = (byte) (package_id >> 8);
        param[i++] = (byte) (package_id >> 16);
        param[i++] = (byte) (package_id >> 24);

        param[i++] = (byte) (user_id & 0xff);
        param[i++] = (byte) (user_id >> 8);
        param[i++] = (byte) (user_id >> 16);
        param[i++] = (byte) (user_id >> 24);

        for (int j = 0; j < file_part.length; j++, i++) {
            param[i] = file_part[j];
        }

        return at_cmd(opcode, opcode, param);
    }

    public static byte[] at_set_file_user_chat(byte opcode, byte objcode,long user_id,long package_id, byte[] file_part) {
        int i = 0;
        byte param[] = new byte[6 + file_part.length];

        param[i++] = (byte) (user_id & 0xff);
        param[i++] = (byte) (user_id >> 8);
        param[i++] = (byte) (user_id >> 16);
        param[i++] = (byte) (user_id >> 24);

        param[i++] = (byte) (package_id & 0xff);
        param[i++] = (byte) (package_id >> 8);


        for (int j = 0; j < file_part.length; j++, i++) {
            param[i] = file_part[j];
        }

        return at_cmd(opcode, objcode, param);

    }

    public static byte[] at_set_file_user_chat_versions(byte objcode,long user_id,long package_id, byte[] file_part) {
        int i = 0;
        byte param[] = new byte[8 + file_part.length];

        param[i++] = (byte) (user_id & 0xff);
        param[i++] = (byte) (user_id >> 8);
        param[i++] = (byte) (user_id >> 16);
        param[i++] = (byte) (user_id >> 24);

        param[i++] = (byte) (package_id & 0xff);
        param[i++] = (byte) (package_id >> 8);
        param[i++] = (byte) (package_id >> 16);
        param[i++] = (byte) (package_id >> 24);


        for (int j = 0; j < file_part.length; j++, i++) {
            param[i] = file_part[j];
        }

        return at_cmd_versions_crc(objcode, param);

    }

    /**
     * AT命令类型
     *
     * get 类型
     * objcode      类型
     * result       数值
     */

    public static byte[] at_get_file_result(byte opcode,byte objcode,byte result) {
        int i = 0;
        byte param[] = new byte[1];
        param[i] = result;


        return at_cmd(opcode, objcode, param);
    }

    public static void main(String args[]) {
        byte by = (byte) 0xf8;
    }

}


