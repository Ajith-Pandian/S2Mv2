package com.example.wowconnect.domain.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.wowconnect.models.DbUser;
import com.example.wowconnect.models.Schools;
import com.example.wowconnect.models.SclActs;
import com.example.wowconnect.models.Sections;
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
    private static final int DATABASE_VERSION = 21;

    private Dao<DbUser, Integer> mUserDao = null;
    private Dao<Sections, Integer> mSectionsDao = null;
    private Dao<SclActs, Integer> mSclActsDao = null;
    private Dao<Schools, Integer> mSchoolsDao = null;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, DbUser.class);
            TableUtils.createTable(connectionSource, Sections.class);
            TableUtils.createTable(connectionSource, SclActs.class);
            TableUtils.createTable(connectionSource, Schools.class);
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
            TableUtils.dropTable(connectionSource, SclActs.class, true);
            TableUtils.dropTable(connectionSource, Schools.class, true);
            TableUtils.clearTable(getConnectionSource(), DbUser.class);
            TableUtils.clearTable(getConnectionSource(), Sections.class);
            TableUtils.clearTable(getConnectionSource(), SclActs.class);
            TableUtils.clearTable(getConnectionSource(), Schools.class);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void refreshAllTables() {
        try {
            TableUtils.dropTable(getSectionsDao(), false);
            TableUtils.dropTable(getSclActsDao(), false);

            TableUtils.createTable(getSectionsDao());
            TableUtils.createTable(getSclActsDao());
        } catch (SQLException e) {
            Log.d("DataBaseHelper", "deleteAllFieldsInTable: " + e.toString());
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

    /* School Activities */
    public Dao<SclActs, Integer> getSclActsDao() throws SQLException {
        if (mSclActsDao == null) {
            mSclActsDao = getDao(SclActs.class);
        }
        return mSclActsDao;
    }

    /* Schools */
    public Dao<Schools, Integer> getSchoolsDao() throws SQLException {
        if (mSchoolsDao == null) {
            mSchoolsDao = getDao(Schools.class);
        }
        return mSchoolsDao;
    }


    @Override
    public void close() {
        if (mUserDao != null)
            mUserDao = null;
        if (mSectionsDao != null)
            mSectionsDao = null;
        if (mSclActsDao != null)
            mSclActsDao = null;
        if (mSchoolsDao != null)
            mSchoolsDao = null;
        super.close();
    }

}
