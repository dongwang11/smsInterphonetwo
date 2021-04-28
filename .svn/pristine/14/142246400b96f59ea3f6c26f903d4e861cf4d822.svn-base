package com.sms.app.framework.dao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;
import com.sms.app.framework.dao.bean.commom.DaoSession;
import com.sms.app.framework.dao.bean.commom.DAOTrajectoryDao;
import com.sms.app.framework.dao.bean.commom.DAOPointDao;

@Entity
//本地保存的GPS采样点
public class DAOPoint {

	@Id
	private Long id;
	private long remote_id;
	private double direction;
	private double longitude;
	private double latitude;
	private double speex;
	private double altitude;//海拔
	private long trajectoryId;
	@ToOne(joinProperty = "trajectoryId")
	private DAOTrajectory trajectory;
	private Date   pcreate_time;
	/** Used to resolve relations */
	@Generated(hash = 2040040024)
	private transient DaoSession daoSession;
	/** Used for active entity operations. */
	@Generated(hash = 1893406489)
	private transient DAOPointDao myDao;
	@Generated(hash = 890360994)
	public DAOPoint(Long id, long remote_id, double direction, double longitude, double latitude,
			double speex, double altitude, long trajectoryId, Date pcreate_time) {
		this.id = id;
		this.remote_id = remote_id;
		this.direction = direction;
		this.longitude = longitude;
		this.latitude = latitude;
		this.speex = speex;
		this.altitude = altitude;
		this.trajectoryId = trajectoryId;
		this.pcreate_time = pcreate_time;
	}
	@Generated(hash = 1250486518)
	public DAOPoint() {
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
	public double getDirection() {
					return this.direction;
	}
	public void setDirection(double direction) {
					this.direction = direction;
	}
	public double getLongitude() {
					return this.longitude;
	}
	public void setLongitude(double longitude) {
					this.longitude = longitude;
	}
	public double getLatitude() {
					return this.latitude;
	}
	public void setLatitude(double latitude) {
					this.latitude = latitude;
	}
	public double getSpeex() {
					return this.speex;
	}
	public void setSpeex(double speex) {
					this.speex = speex;
	}
	public long getTrajectoryId() {
					return this.trajectoryId;
	}
	public void setTrajectoryId(long trajectoryId) {
					this.trajectoryId = trajectoryId;
	}
	public Date getPcreate_time() {
					return this.pcreate_time;
	}
	public void setPcreate_time(Date pcreate_time) {
					this.pcreate_time = pcreate_time;
	}
	@Generated(hash = 1097320144)
	private transient Long trajectory__resolvedKey;
	/** To-one relationship, resolved on first access. */
	@Generated(hash = 2015649685)
	public DAOTrajectory getTrajectory() {
					long __key = this.trajectoryId;
					if (trajectory__resolvedKey == null
													|| !trajectory__resolvedKey.equals(__key)) {
									final DaoSession daoSession = this.daoSession;
									if (daoSession == null) {
													throw new DaoException("Entity is detached from DAO context");
									}
									DAOTrajectoryDao targetDao = daoSession.getDAOTrajectoryDao();
									DAOTrajectory trajectoryNew = targetDao.load(__key);
									synchronized (this) {
													trajectory = trajectoryNew;
													trajectory__resolvedKey = __key;
									}
					}
					return trajectory;
	}
	/** called by internal mechanisms, do not call yourself. */
	@Generated(hash = 1556288160)
	public void setTrajectory(@NotNull DAOTrajectory trajectory) {
					if (trajectory == null) {
									throw new DaoException(
																	"To-one property 'trajectoryId' has not-null constraint; cannot set to-one to null");
					}
					synchronized (this) {
									this.trajectory = trajectory;
									trajectoryId = trajectory.getId();
									trajectory__resolvedKey = trajectoryId;
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
	@Generated(hash = 1078004184)
	public void __setDaoSession(DaoSession daoSession) {
		this.daoSession = daoSession;
		myDao = daoSession != null ? daoSession.getDAOPointDao() : null;
	}
	public double getAltitude() {
		return this.altitude;
	}
	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

}

