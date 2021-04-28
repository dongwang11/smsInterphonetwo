package com.sms.app.framework.dao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Transient;
import com.sms.app.framework.dao.bean.commom.DaoSession;
import com.sms.app.framework.dao.bean.commom.DAOPointDao;
import com.sms.app.framework.dao.bean.commom.DAOUserDao;
import com.sms.app.framework.dao.bean.commom.DAOTrajectoryDao;

@Entity
//本地保存的轨迹信息
public class DAOTrajectory {
	@Id
	private Long  id;
	private long remote_id;
	private long version;
	private byte	 status;	//状态
	private int   poit_num;	//点数
	private String name;//		名字
	private long startPId;

	private double length;//		长度s

	@ToOne(joinProperty = "startPId")
	private DAOPoint start_DAO_point;//		起始点
	private long endPId;
	@ToOne(joinProperty = "endPId")
	private DAOPoint end_DAO_point;//	结束点
	private int used_time_s;//用时
	private Date date; //日期
	@ToMany(referencedJoinProperty = "trajectoryId")
	@OrderBy("id ASC")
	private List<DAOPoint> points;//轨迹点的集合
	private long userId;
	@ToOne(joinProperty = "userId")
	private DAOUser user;

	@Transient
	public static final byte TRAJ_STATUS_UNFINISH=1;//未完成
	@Transient
	public static final byte TRAJ_STATUS_FINISH=2;//完成
	@Transient
	public static final byte TRAJ_STATUS_UPLOADING=3;//上传
	@Transient
	public static final byte TRAJ_STATUS_UPLOADED=4;//删除
	/** Used to resolve relations */
	@Generated(hash = 2040040024)
	private transient DaoSession daoSession;
	/** Used for active entity operations. */
	@Generated(hash = 1768077425)
	private transient DAOTrajectoryDao myDao;
	@Generated(hash = 1108712324)
	public DAOTrajectory(Long id, long remote_id, long version, byte status,
									int poit_num, String name, long startPId, double length, long endPId,
									int used_time_s, Date date, long userId) {
					this.id = id;
					this.remote_id = remote_id;
					this.version = version;
					this.status = status;
					this.poit_num = poit_num;
					this.name = name;
					this.startPId = startPId;
					this.length = length;
					this.endPId = endPId;
					this.used_time_s = used_time_s;
					this.date = date;
					this.userId = userId;
	}
	@Generated(hash = 328248802)
	public DAOTrajectory() {
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
	public long getVersion() {
					return this.version;
	}
	public void setVersion(long version) {
					this.version = version;
	}
	public byte getStatus() {
					return this.status;
	}
	public void setStatus(byte status) {
					this.status = status;
	}
	public int getPoit_num() {
					return this.poit_num;
	}
	public void setPoit_num(int poit_num) {
					this.poit_num = poit_num;
	}
	public String getName() {
					return this.name;
	}
	public void setName(String name) {
					this.name = name;
	}
	public long getStartPId() {
					return this.startPId;
	}
	public void setStartPId(long startPId) {
					this.startPId = startPId;
	}
	public double getLength() {
					return this.length;
	}
	public void setLength(double length) {
					this.length = length;
	}
	public long getEndPId() {
					return this.endPId;
	}
	public void setEndPId(long endPId) {
					this.endPId = endPId;
	}
	public int getUsed_time_s() {
					return this.used_time_s;
	}
	public void setUsed_time_s(int used_time_s) {
					this.used_time_s = used_time_s;
	}
	public Date getDate() {
					return this.date;
	}
	public void setDate(Date date) {
					this.date = date;
	}
	public long getUserId() {
					return this.userId;
	}
	public void setUserId(long userId) {
					this.userId = userId;
	}
	@Generated(hash = 896208672)
	private transient Long start_DAO_point__resolvedKey;
	/** To-one relationship, resolved on first access. */
	@Generated(hash = 883726353)
	public DAOPoint getStart_DAO_point() {
					long __key = this.startPId;
					if (start_DAO_point__resolvedKey == null
													|| !start_DAO_point__resolvedKey.equals(__key)) {
									final DaoSession daoSession = this.daoSession;
									if (daoSession == null) {
													throw new DaoException("Entity is detached from DAO context");
									}
									DAOPointDao targetDao = daoSession.getDAOPointDao();
									DAOPoint start_DAO_pointNew = targetDao.load(__key);
									synchronized (this) {
													start_DAO_point = start_DAO_pointNew;
													start_DAO_point__resolvedKey = __key;
									}
					}
					return start_DAO_point;
	}
	/** called by internal mechanisms, do not call yourself. */
	@Generated(hash = 911813981)
	public void setStart_DAO_point(@NotNull DAOPoint start_DAO_point) {
					if (start_DAO_point == null) {
									throw new DaoException(
																	"To-one property 'startPId' has not-null constraint; cannot set to-one to null");
					}
					synchronized (this) {
									this.start_DAO_point = start_DAO_point;
									startPId = start_DAO_point.getId();
									start_DAO_point__resolvedKey = startPId;
					}
	}
	@Generated(hash = 2099401547)
	private transient Long end_DAO_point__resolvedKey;
	/** To-one relationship, resolved on first access. */
	@Generated(hash = 318086060)
	public DAOPoint getEnd_DAO_point() {
					long __key = this.endPId;
					if (end_DAO_point__resolvedKey == null
													|| !end_DAO_point__resolvedKey.equals(__key)) {
									final DaoSession daoSession = this.daoSession;
									if (daoSession == null) {
													throw new DaoException("Entity is detached from DAO context");
									}
									DAOPointDao targetDao = daoSession.getDAOPointDao();
									DAOPoint end_DAO_pointNew = targetDao.load(__key);
									synchronized (this) {
													end_DAO_point = end_DAO_pointNew;
													end_DAO_point__resolvedKey = __key;
									}
					}
					return end_DAO_point;
	}
	/** called by internal mechanisms, do not call yourself. */
	@Generated(hash = 1884578330)
	public void setEnd_DAO_point(@NotNull DAOPoint end_DAO_point) {
					if (end_DAO_point == null) {
									throw new DaoException(
																	"To-one property 'endPId' has not-null constraint; cannot set to-one to null");
					}
					synchronized (this) {
									this.end_DAO_point = end_DAO_point;
									endPId = end_DAO_point.getId();
									end_DAO_point__resolvedKey = endPId;
					}
	}
	@Generated(hash = 251390918)
	private transient Long user__resolvedKey;
	/** To-one relationship, resolved on first access. */
	@Generated(hash = 1720493076)
	public DAOUser getUser() {
					long __key = this.userId;
					if (user__resolvedKey == null || !user__resolvedKey.equals(__key)) {
									final DaoSession daoSession = this.daoSession;
									if (daoSession == null) {
													throw new DaoException("Entity is detached from DAO context");
									}
									DAOUserDao targetDao = daoSession.getDAOUserDao();
									DAOUser userNew = targetDao.load(__key);
									synchronized (this) {
													user = userNew;
													user__resolvedKey = __key;
									}
					}
					return user;
	}
	/** called by internal mechanisms, do not call yourself. */
	@Generated(hash = 103800039)
	public void setUser(@NotNull DAOUser user) {
					if (user == null) {
									throw new DaoException(
																	"To-one property 'userId' has not-null constraint; cannot set to-one to null");
					}
					synchronized (this) {
									this.user = user;
									userId = user.getId();
									user__resolvedKey = userId;
					}
	}
	/**
	 * To-many relationship, resolved on first access (and after reset).
	 * Changes to to-many relations are not persisted, make changes to the target entity.
	 */
	@Generated(hash = 851770479)
	public List<DAOPoint> getPoints() {
					if (points == null) {
									final DaoSession daoSession = this.daoSession;
									if (daoSession == null) {
													throw new DaoException("Entity is detached from DAO context");
									}
									DAOPointDao targetDao = daoSession.getDAOPointDao();
									List<DAOPoint> pointsNew = targetDao._queryDAOTrajectory_Points(id);
									synchronized (this) {
													if (points == null) {
																	points = pointsNew;
													}
									}
					}
					return points;
	}
	/** Resets a to-many relationship, making the next get call to query for a fresh result. */
	@Generated(hash = 1076404248)
	public synchronized void resetPoints() {
					points = null;
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
	@Generated(hash = 62060785)
	public void __setDaoSession(DaoSession daoSession) {
					this.daoSession = daoSession;
					myDao = daoSession != null ? daoSession.getDAOTrajectoryDao() : null;
	}

}
