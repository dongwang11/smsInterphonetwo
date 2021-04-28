package com.sms.app.framework.communication.localayer.cmd;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/3/27.
 */

public class Files {
    private int size;
    private String version;
    private String name;
    private byte[] bytes;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public String toString() {
        return "Files{" +
                "size=" + size +
                ", version='" + version + '\'' +
                ", name='" + name + '\'' +
                ", bytes=" + Arrays.toString(bytes) +
                '}';
    }
}
