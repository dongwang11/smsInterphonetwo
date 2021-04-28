package com.sms.app.framework.dao.bean.commom;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;


/**
 * Created by Administrator on 2017/3/17.
 */

public class DAOService {
    private DaoMaster.DevOpenHelper helper;
    private SQLiteDatabase db;
    private DaoMaster daomaster;
    private DaoSession daosession;
    private Context cnt;
    public static DAOService instance=null;

    public DAOService(Context cnt)
    {
        this.cnt = cnt;
        setDataBase();
    }
    public static DAOService get(Context cnt)
    {
        if(DAOService.instance==null) DAOService.instance =  new DAOService(cnt);
        return DAOService.instance;
    }

    private void setDataBase()
    {
        this.helper =  new DaoMaster.DevOpenHelper(cnt,"sms.db",null);
        this.db =  helper.getWritableDatabase();
        this.daomaster =  new DaoMaster(db);
        this.daosession = this.daomaster.newSession();
    }

    public DaoSession getsession()
    {
        return this.daosession;
    }

    public SQLiteDatabase getdb()
    {
        return this.db;
    }


}
