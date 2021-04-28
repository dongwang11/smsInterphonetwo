package com.sms.app.framework.dao.bean;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import com.sms.app.framework.dao.bean.commom.DaoSession;
import com.sms.app.framework.dao.bean.commom.DAOMesgDao;
import com.sms.app.framework.dao.bean.commom.DAOFriendDao;
import com.sms.app.framework.dao.bean.commom.DAOGroupDao;


/**
 * Created by Administrator on 2017/3/28.
 */
@Entity
public class DAOGroup {
    @Id
    private Long id;
    private String name;
    private long version;
    private long remote_id;
    private boolean is_avtive;//群的当前状态，是失效还是激活
    private long groupId;
    @ToMany(referencedJoinProperty = "gid")
    @OrderBy("id ASC")
    private List<DAOFriend> friends;

    @ToMany(referencedJoinProperty = "mgid")
    @OrderBy("id ASC")
    private List<DAOMesg> mesgs;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 875701171)
    private transient DAOGroupDao myDao;

    @Generated(hash = 1493078723)
    public DAOGroup(Long id, String name, long version, long remote_id,
            boolean is_avtive, long groupId) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.remote_id = remote_id;
        this.is_avtive = is_avtive;
        this.groupId = groupId;
    }

    @Generated(hash = 558099215)
    public DAOGroup() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean getIs_avtive() {
        return this.is_avtive;
    }

    public void setIs_avtive(boolean is_avtive) {
        this.is_avtive = is_avtive;
    }

    public long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 836466117)
    public List<DAOFriend> getFriends() {
        if (friends == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DAOFriendDao targetDao = daoSession.getDAOFriendDao();
            List<DAOFriend> friendsNew = targetDao._queryDAOGroup_Friends(id);
            synchronized (this) {
                if (friends == null) {
                    friends = friendsNew;
                }
            }
        }
        return friends;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1638260638)
    public synchronized void resetFriends() {
        friends = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 42670266)
    public List<DAOMesg> getMesgs() {
        if (mesgs == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DAOMesgDao targetDao = daoSession.getDAOMesgDao();
            List<DAOMesg> mesgsNew = targetDao._queryDAOGroup_Mesgs(id);
            synchronized (this) {
                if (mesgs == null) {
                    mesgs = mesgsNew;
                }
            }
        }
        return mesgs;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1824344907)
    public synchronized void resetMesgs() {
        mesgs = null;
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
    @Generated(hash = 700935619)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDAOGroupDao() : null;
    }


    @Override
    public String toString() {
        return "DAOGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", version=" + version +
                ", remote_id=" + remote_id +
                ", is_avtive=" + is_avtive +
                ", groupId=" + groupId +
                ", friends=" + friends +
                ", mesgs=" + mesgs +
                ", daoSession=" + daoSession +
                ", myDao=" + myDao +
                '}';
    }
}
