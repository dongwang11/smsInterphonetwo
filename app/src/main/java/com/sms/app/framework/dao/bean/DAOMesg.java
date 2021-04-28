package com.sms.app.framework.dao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Transient;

import java.util.Date;
import com.sms.app.framework.dao.bean.commom.DaoSession;
import com.sms.app.framework.dao.bean.commom.DAOGroupDao;
import com.sms.app.framework.dao.bean.commom.DAOMesgDao;

/**
 * Created by Administrator on 2017/3/27.
 */
@Entity
public class DAOMesg {
    @Id
    private Long id;
    private long from_id;  //房间ID
    private long user_id;  //用户ID

    private Date create_time;
    private int content_length;
    
    private byte mesg_type;
    private String content;//文本或者语音图片的文件路劲
    private long mgid;  //包ID



    @ToOne(joinProperty = "from_id")
    private DAOGroup group;

    @Transient
    public static final byte MESG_TYPE_TEXT = 1;
    @Transient
    public static final byte MESG_TYPE_VOIVE = 2;
    @Transient
    public static final byte MESG_TYPE_IMG = 3;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 533628621)
    private transient DAOMesgDao myDao;
    @Generated(hash = 2066672117)
    public DAOMesg(Long id, long from_id, long user_id, Date create_time, int content_length,
            byte mesg_type, String content, long mgid) {
        this.id = id;
        this.from_id = from_id;
        this.user_id = user_id;
        this.create_time = create_time;
        this.content_length = content_length;
        this.mesg_type = mesg_type;
        this.content = content;
        this.mgid = mgid;
    }
    @Generated(hash = 606747753)
    public DAOMesg() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public long getFrom_id() {
        return this.from_id;
    }
    public void setFrom_id(long from_id) {
        this.from_id = from_id;
    }
    public long getUser_id() {
        return this.user_id;
    }
    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }
    public Date getCreate_time() {
        return this.create_time;
    }
    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }
    public byte getMesg_type() {
        return this.mesg_type;
    }
    public void setMesg_type(byte mesg_type) {
        this.mesg_type = mesg_type;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public long getMgid() {
        return this.mgid;
    }
    public void setMgid(long mgid) {
        this.mgid = mgid;
    }
    @Generated(hash = 201187923)
    private transient Long group__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1024411093)
    public DAOGroup getGroup() {
        long __key = this.from_id;
        if (group__resolvedKey == null || !group__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DAOGroupDao targetDao = daoSession.getDAOGroupDao();
            DAOGroup groupNew = targetDao.load(__key);
            synchronized (this) {
                group = groupNew;
                group__resolvedKey = __key;
            }
        }
        return group;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1852582741)
    public void setGroup(@NotNull DAOGroup group) {
        if (group == null) {
            throw new DaoException(
                    "To-one property 'from_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.group = group;
            from_id = group.getId();
            group__resolvedKey = from_id;
        }
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 146048690)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDAOMesgDao() : null;
    }
    public int getCreate_length() {
        return this.content_length;
    }
    public void setCreate_length(int create_length) {
        this.content_length = create_length;
    }
    public int getContent_length() {
        return this.content_length;
    }
    public void setContent_length(int content_length) {
        this.content_length = content_length;
    }



}
