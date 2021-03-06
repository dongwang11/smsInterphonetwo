package com.sms.app.framework.dao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import org.greenrobot.greendao.annotation.NotNull;
import com.sms.app.framework.dao.bean.commom.DaoSession;
import com.sms.app.framework.dao.bean.commom.CustomerDao;
import com.sms.app.framework.dao.bean.commom.OrderDao;

/**
 * Created by Administrator on 2017/3/23.
 */

@Entity
public class Order {
    @Id
   private Long id;
    private long remote_id;
   private String name;
   private long customerId;
   @ToOne(joinProperty = "customerId")
   private Customer customer;
/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;
/** Used for active entity operations. */
@Generated(hash = 949219203)
private transient OrderDao myDao;
@Generated(hash = 1117974490)
public Order(Long id, long remote_id, String name, long customerId) {
    this.id = id;
    this.remote_id = remote_id;
    this.name = name;
    this.customerId = customerId;
}
@Generated(hash = 1105174599)
public Order() {
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
public String getName() {
    return this.name;
}
public void setName(String name) {
    this.name = name;
}
public long getCustomerId() {
    return this.customerId;
}
public void setCustomerId(long customerId) {
    this.customerId = customerId;
}
@Generated(hash = 8592637)
private transient Long customer__resolvedKey;
/** To-one relationship, resolved on first access. */
@Generated(hash = 941511332)
public Customer getCustomer() {
    long __key = this.customerId;
    if (customer__resolvedKey == null || !customer__resolvedKey.equals(__key)) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        CustomerDao targetDao = daoSession.getCustomerDao();
        Customer customerNew = targetDao.load(__key);
        synchronized (this) {
            customer = customerNew;
            customer__resolvedKey = __key;
        }
    }
    return customer;
}
/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 625323961)
public void setCustomer(@NotNull Customer customer) {
    if (customer == null) {
        throw new DaoException(
                "To-one property 'customerId' has not-null constraint; cannot set to-one to null");
    }
    synchronized (this) {
        this.customer = customer;
        customerId = customer.getId();
        customer__resolvedKey = customerId;
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
@Generated(hash = 965731666)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getOrderDao() : null;
}


}
