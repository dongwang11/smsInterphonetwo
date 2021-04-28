package com.sms.app.framework.dao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;
import com.sms.app.framework.dao.bean.commom.DaoSession;
import com.sms.app.framework.dao.bean.commom.DAOGroupDao;
import com.sms.app.framework.dao.bean.commom.DAOFriendDao;

/**
 * Created by Administrator on 2017/3/27.
 */
@Entity
public class DAOFriend {
    @Id
    private Long id;
    private long version;
    private long remote_id;//用户id
    private String name;
    private String avatar_url;//头像图片的URL
    private long gid;
    private Date enter_time;//进群时间
    @ToOne(joinProperty = "gid")
    private DAOGroup group;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 2023471165)
    private transient DAOFriendDao myDao;
    @Generated(hash = 1776697407)
    public DAOFriend(Long id, long version, long remote_id, String name,
            String avatar_url, long gid, Date enter_time) {
        this.id = id;
        this.version = version;
        this.remote_id = remote_id;
        this.name = name;
        this.avatar_url = avatar_url;
        this.gid = gid;
        this.enter_time = enter_time;
    }
    @Generated(hash = 1724998991)
    public DAOFriend() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public long getVersion() {
        return this.version;
    }
    public void setVersion(long version) {
        this.version = version;
    }
    public long getRemote_id() {
        return this.remote_id;
    }
    public void setRemote_id(long remote_id) {
        this.remote_id = remote_id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAvatar_url() {
        return this.avatar_url;
    }
    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
    public long getGid() {
        return this.gid;
    }
    public void setGid(long gid) {
        this.gid = gid;
    }
    public Date getEnter_time() {
        return this.enter_time;
    }
    public void setEnter_time(Date enter_time) {
        this.enter_time = enter_time;
    }
    @Generated(hash = 201187923)
    private transient Long group__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1707226131)
    public DAOGroup getGroup() {
        long __key = this.gid;
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
    @Generated(hash = 733837582)
    public void setGroup(@NotNull DAOGroup group) {
        if (group == null) {
            throw new DaoException(
                    "To-one property 'gid' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.group = group;
            gid = group.getId();
            group__resolvedKey = gid;
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
    @Generated(hash = 517440151)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDAOFriendDao() : null;
    }

}
