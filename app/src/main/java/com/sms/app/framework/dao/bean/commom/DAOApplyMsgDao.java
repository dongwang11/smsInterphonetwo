package com.sms.app.framework.dao.bean.commom;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.sms.app.framework.dao.bean.DAOApplyMsg;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DAOAPPLY_MSG".
*/
public class DAOApplyMsgDao extends AbstractDao<DAOApplyMsg, Long> {

    public static final String TABLENAME = "DAOAPPLY_MSG";

    /**
     * Properties of entity DAOApplyMsg.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property User_id = new Property(1, long.class, "user_id", false, "USER_ID");
        public final static Property Msg_id = new Property(2, long.class, "msg_id", false, "MSG_ID");
        public final static Property From_id = new Property(3, long.class, "from_id", false, "FROM_ID");
        public final static Property Type = new Property(4, byte.class, "type", false, "TYPE");
        public final static Property Content = new Property(5, String.class, "content", false, "CONTENT");
    }


    public DAOApplyMsgDao(DaoConfig config) {
        super(config);
    }
    
    public DAOApplyMsgDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DAOAPPLY_MSG\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"USER_ID\" INTEGER NOT NULL ," + // 1: user_id
                "\"MSG_ID\" INTEGER NOT NULL ," + // 2: msg_id
                "\"FROM_ID\" INTEGER NOT NULL ," + // 3: from_id
                "\"TYPE\" INTEGER NOT NULL ," + // 4: type
                "\"CONTENT\" TEXT);"); // 5: content
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DAOAPPLY_MSG\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, DAOApplyMsg entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getUser_id());
        stmt.bindLong(3, entity.getMsg_id());
        stmt.bindLong(4, entity.getFrom_id());
        stmt.bindLong(5, entity.getType());
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(6, content);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, DAOApplyMsg entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getUser_id());
        stmt.bindLong(3, entity.getMsg_id());
        stmt.bindLong(4, entity.getFrom_id());
        stmt.bindLong(5, entity.getType());
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(6, content);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public DAOApplyMsg readEntity(Cursor cursor, int offset) {
        DAOApplyMsg entity = new DAOApplyMsg( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // user_id
            cursor.getLong(offset + 2), // msg_id
            cursor.getLong(offset + 3), // from_id
            (byte) cursor.getShort(offset + 4), // type
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // content
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, DAOApplyMsg entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUser_id(cursor.getLong(offset + 1));
        entity.setMsg_id(cursor.getLong(offset + 2));
        entity.setFrom_id(cursor.getLong(offset + 3));
        entity.setType((byte) cursor.getShort(offset + 4));
        entity.setContent(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(DAOApplyMsg entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(DAOApplyMsg entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(DAOApplyMsg entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
