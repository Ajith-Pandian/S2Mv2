package com.example.domainlayer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.domainlayer.models.DbUser;
import com.example.domainlayer.models.Sections;
import com.example.domainlayer.models.User;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by thoughtchimp on 12/6/2016.
 */


public class DataBaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "s2mv2.db";
    private static final int DATABASE_VERSION = 12;

    private Dao<DbUser, Integer> mUserDao = null;
    private Dao<Sections, Integer> mSectionsDao = null;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, DbUser.class);
            TableUtils.createTable(connectionSource, Sections.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, DbUser.class, true);
            TableUtils.dropTable(connectionSource, Sections.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* User */

    public Dao<DbUser, Integer> getUserDao() throws SQLException {
        if (mUserDao == null) {
            mUserDao = getDao(DbUser.class);
        }
        return mUserDao;
    }
    /* Section */

    public Dao<Sections, Integer> getSectionsDao() throws SQLException {
        if (mSectionsDao == null) {
            mSectionsDao = getDao(Sections.class);
        }
        return mSectionsDao;
    }



    @Override
    public void close() {
        mUserDao = null;
        mSectionsDao = null;
        super.close();
    }

}
