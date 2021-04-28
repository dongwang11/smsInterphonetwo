package com.sms.app.framework.dao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.sms.app.framework.dao.bean.commom.DaoSession;
import com.sms.app.framework.dao.bean.commom.DAOGroupDao;
import com.sms.app.framework.dao.bean.commom.DAOTrajectoryDao;
import com.sms.app.framework.dao.bean.commom.DAOUserDao;


/**
 * Created by Administrator on 2017/3/27.
 */
@Entity
//本地保存的用户信息
public class DAOUser {


    @Id
    private Long id;
    private long remote_id;
    private String avatar_url;//头像图片的URL
    private long version;
    private String name;
    private Date register_time;
    private Date  last_login_time;
    private byte  sex;//0代表未知，1代表男，2代表女
    private String  number;//电话号码
    private String  e_mail;
    private long dev_type_bmp;
    @ToMany(referencedJoinProperty = "userId")
    @OrderBy("id ASC")
    private List<DAOTrajectory> trajectories;//轨迹的集合
    @ToMany(referencedJoinProperty = "groupId")
    @OrderBy("id ASC")
    private List<DAOGroup> groups;//群的集合
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1361881198)
    private transient DAOUserDao myDao;
    @Generated(hash = 294112161)
    public DAOUser(Long id, long remote_id, String avatar_url, long version, String name,
            Date register_time, Date last_login_time, byte sex, String number, String e_mail,
            long dev_type_bmp) {
        this.id = id;
        this.remote_id = remote_id;
        this.avatar_url = avatar_url;
        this.version = version;
        this.name = name;
        this.register_time = register_time;
        this.last_login_time = last_login_time;
        this.sex = sex;
        this.number = number;
        this.e_mail = e_mail;
        this.dev_type_bmp = dev_type_bmp;
    }
    @Generated(hash = 1708470206)
    public DAOUser() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public long getRemote_id() {
        return this.remote_id;
    }
    public void setRemote_id(long remote_id) {
        this.remote_id = remote_id;
    }
    public String getAvatar_url() {
        return this.avatar_url;
    }
    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
    public long getVersion() {
        return this.version;
    }
    public void setVersion(long version) {
        this.version = version;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Date getRegister_time() {
        return this.register_time;
    }
    public void setRegister_time(Date register_time) {
        this.register_time = register_time;
    }
    public Date getLast_login_time() {
        return this.last_login_time;
    }
    public void setLast_login_time(Date last_login_time) {
        this.last_login_time = last_login_time;
    }
    public byte getSex() {
        return this.sex;
    }
    public void setSex(byte sex) {
        this.sex = sex;
    }
    public String getNumber() {
        return this.number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getE_mail() {
        return this.e_mail;
    }
    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }
    public long getDev_type_bmp() {
        return this.dev_type_bmp;
    }
    public void setDev_type_bmp(long dev_type_bmp) {
        this.dev_type_bmp = dev_type_bmp;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 66138897)
    public List<DAOTrajectory> getTrajectories() {
        if (trajectories == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DAOTrajectoryDao targetDao = daoSession.getDAOTrajectoryDao();
            List<DAOTrajectory> trajectoriesNew = targetDao
                    ._queryDAOUser_Trajectories(id);
            synchronized (this) {
                if (trajectories == null) {
                    trajectories = trajectoriesNew;
                }
            }
        }
        return trajectories;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1001800923)
    public synchronized void resetTrajectories() {
        trajectories = null;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1170306183)
    public List<DAOGroup> getGroups() {
        if (groups == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DAOGroupDao targetDao = daoSession.getDAOGroupDao();
            List<DAOGroup> groupsNew = targetDao._queryDAOUser_Groups(id);
            synchronized (this) {
                if (groups == null) {
                    groups = groupsNew;
                }
            }
        }
        return groups;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 464128061)
    public synchronized void resetGroups() {
        groups = null;
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
    @Generated(hash = 574962444)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDAOUserDao() : null;
    }
    public void setId(long id) {
        this.id = id;
    }



}