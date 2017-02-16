package com.wowconnect.domain.database;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;
import com.wowconnect.SharedPreferenceHelper;
import com.wowconnect.models.DbUser;
import com.wowconnect.models.Schools;
import com.wowconnect.models.SclActs;
import com.wowconnect.models.Sections;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by thoughtchimp on 12/6/2016.
 */

public class DataBaseUtil {
    final String TAG = "DataBaseUtil";
    private Context context;
    private DataBaseHelper helper;

    public DataBaseUtil(Context context) {
        this.context = context.getApplicationContext();
    }

    private Dao<DbUser, Integer> getLocalUserDao() {
        try {
            if (helper == null)
                helper = new DataBaseHelper(context);
            return helper.getUserDao();
        } catch (SQLException ex) {
            Log.e(context.getClass().getSimpleName(), "getLocalUserDao: ", ex);
            //throw new RuntimeException("Cannot get user Dao");
            return null;
        }
    }

    private Dao<Sections, Integer> getLocalSectionDao() {
        try {
            if (helper == null)
                helper = new DataBaseHelper(context);
            return helper.getSectionsDao();
        } catch (SQLException ex) {
            Log.e(context.getClass().getSimpleName(), "getLocalSectionDao: ", ex);
            //throw new RuntimeException("Cannot get user Dao");
            return null;
        }
    }

    private Dao<SclActs, Integer> getLocalSclActsDao() {
        try {
            if (helper == null)
                helper = new DataBaseHelper(context);
            return helper.getSclActsDao();
        } catch (SQLException ex) {
            Log.e(context.getClass().getSimpleName(), "getLocalSclActsDao: ", ex);
            //throw new RuntimeException("Cannot get user Dao");
            return null;
        }
    }


    private Dao<Schools, Integer> getLocalSchoolsDao() {
        try {
            if (helper == null)
                helper = new DataBaseHelper(context);
            return helper.getSchoolsDao();
        } catch (SQLException ex) {
            Log.e(context.getClass().getSimpleName(), "getLocalSchoolsDao: ", ex);
            //throw new RuntimeException("Cannot get user Dao");
            return null;
        }
    }

    public DbUser getUser(int userId) {
        try {
            Dao<DbUser, Integer> userDao = getLocalUserDao();
            if (userDao != null)
                return userDao.queryForId(userId);
            else return null;


        } catch (SQLException ex) {
            Log.e(context.getClass().getSimpleName(), "getUser: ", ex);
            throw new RuntimeException("No users in table");
        }

    }

    public void setUser(DbUser user) {
        Dao<DbUser, Integer> userDao = getLocalUserDao();
        if (userDao != null)
            try {
                userDao.createOrUpdate(user);
                Log.d(context.getClass().getSimpleName(), "setUser:");
            } catch (SQLException ex) {
                Log.e(context.getClass().getSimpleName(), "getUser: ", ex);
                throw new RuntimeException("Cannot create user");
            }
    }

    public void updateUser(DbUser user) {
        Dao<DbUser, Integer> userDao = getLocalUserDao();
        if (userDao != null)
            try {
                userDao.update(user);
                Log.d(context.getClass().getSimpleName(), "updateUser:");
            } catch (SQLException ex) {
                Log.e(context.getClass().getSimpleName(), "updateUser: ", ex);
                throw new RuntimeException("Cannot update user");
            }
    }

    public ArrayList<Sections> getUserSections() {
        try {
            Dao<Sections, Integer> userDao = getLocalSectionDao();
            if (userDao != null) {
                final List<Sections> sectionsList = userDao.queryForAll();
                if (sectionsList.size() > 0)
                    return new ArrayList<>(sectionsList);
                else {
                    return null;
                }
            } else throw new RuntimeException("No users in table");

        } catch (SQLException ex) {
            Log.e(context.getClass().getSimpleName(), "getNetworkUsers: ", ex);
            throw new RuntimeException("No sections in table");
        }


    }

    public void setUserSections(ArrayList<Sections> sectionsList) {
        updateSectionsDb();
        try {
            for (Sections user : sectionsList) {
                Dao<Sections, Integer> sectionDao = getLocalSectionDao();
                if (sectionDao != null) {
                    sectionDao.createOrUpdate(user);
                }
            }
            Log.d(context.getClass().getSimpleName(), "setUserSections:");
        } catch (SQLException ex) {
            Log.e(context.getClass().getSimpleName(), "setUserSections: ", ex);
            throw new RuntimeException("Cannot create section");
        }
    }

    public ArrayList<DbUser> getNetworkUsers() {
        try {
            Dao<DbUser, Integer> userDao = getLocalUserDao();
            if (userDao != null) {
                final List<DbUser> userList = userDao.queryForAll();
                if (userList.size() > 0)
                    return new ArrayList<>(userList);
                else {
                    return null;
                }
            } else throw new RuntimeException("No users in table");

        } catch (SQLException ex) {
            Log.e(context.getClass().getSimpleName(), "getNetworkUsers: ", ex);
            throw new RuntimeException("No users in table");
        }
    }

    public void setNetworkUsers(ArrayList<DbUser> usersList) {

        try {
            for (DbUser user : usersList) {
                Dao<DbUser, Integer> userDao = getLocalUserDao();
                if (userDao != null) {
                    userDao.createOrUpdate(user);
                }
            }
            Log.d(context.getClass().getSimpleName(), "setNetworkUsers:");
        } catch (SQLException ex) {
            Log.e(context.getClass().getSimpleName(), "setNetworkUsers: ", ex);
            throw new RuntimeException("Cannot create user");
        }
    }

    public ArrayList<SclActs> getSchoolActivities() {
        try {
            Dao<SclActs, Integer> sclActsDao = getLocalSclActsDao();
            if (sclActsDao != null) {
                final List<SclActs> sclActsList = sclActsDao.queryForAll();
                if (sclActsList.size() > 0)
                    return new ArrayList<>(sclActsList);
                else {
                    return null;
                }
            } else throw new RuntimeException("No activities in table");

        } catch (SQLException ex) {
            Log.e(context.getClass().getSimpleName(), "getSchoolActivities: ", ex);
            throw new RuntimeException("No activities in table");
        }
    }

    public void setSchoolActivities(ArrayList<SclActs> sclActsList) {
        try {
            updateSclActsDb();
            for (SclActs sclAct : sclActsList) {
                Dao<SclActs, Integer> sclActsDao = getLocalSclActsDao();
                if (sclActsDao != null) {
                    sclActsDao.createOrUpdate(sclAct);
                }
            }
            Log.d(context.getClass().getSimpleName(), "setSchoolActivities:");
        } catch (SQLException ex) {
            Log.e(context.getClass().getSimpleName(), "setSchoolActivities: ", ex);
            throw new RuntimeException("Cannot create school activities");
        }
    }


    public void setSchools(ArrayList<Schools> schoolsList) {
        try {
            for (Schools school : schoolsList) {
                Dao<Schools, Integer> schoolsDao = getLocalSchoolsDao();
                if (schoolsDao != null) {
                    schoolsDao.createOrUpdate(school);
                }
            }
            Log.d(context.getClass().getSimpleName(), "setSchools:");
        } catch (SQLException ex) {
            Log.e(context.getClass().getSimpleName(), "setSchools: ", ex);
            throw new RuntimeException("Cannot create schools");
        }
    }

    public Schools getSchoolById(int schoolId) {
        try {
            Dao<Schools, Integer> sclActsDao = getLocalSchoolsDao();
            if (sclActsDao != null) {
                return sclActsDao.queryForId(schoolId);
            } else throw new RuntimeException("No schools in table");

        } catch (SQLException ex) {
            Log.e(context.getClass().getSimpleName(), "getSchoolById: ", ex);
            throw new RuntimeException("No schools in table");
        }
    }

    public void updateSchool(Schools school) {
        try {
            Dao<Schools, Integer> schoolsDao = getLocalSchoolsDao();
            if (schoolsDao != null) {
                schoolsDao.update(school);
            }
            Log.d(context.getClass().getSimpleName(), "updateSchool:");
        } catch (SQLException ex) {
            Log.e(context.getClass().getSimpleName(), "updateSchool: ", ex);
            throw new RuntimeException("Cannot update schools");
        }
    }

    public void updateSchoolActivity(SclActs schoolActivity) {
        try {
            Dao<SclActs, Integer> sclActsDao = getLocalSclActsDao();
            if (sclActsDao != null) {
                sclActsDao.update(schoolActivity);
            }
            Log.d(context.getClass().getSimpleName(), "updateSchoolActivity:");
        } catch (SQLException ex) {
            Log.e(context.getClass().getSimpleName(), "updateSchoolActivity: ", ex);
            throw new RuntimeException("Cannot update school activity");
        }
    }

    public void refreshDb() {
        deleteOtherUsersInDb();
        new DataBaseHelper(context).refreshAllTables();
    }

    private void deleteOtherUsersInDb() {
        try {
            if (getLocalUserDao() != null) {
                DbUser ownUser = getLocalUserDao().queryForId(SharedPreferenceHelper.getUserId());
                TableUtils.dropTable(getLocalUserDao(), false);
                TableUtils.createTable(getLocalUserDao());
                getLocalUserDao().createOrUpdate(ownUser);
            }
        } catch (SQLException e) {
            Log.e(context.getClass().getSimpleName(), "deleteOtherUsersInDb: ", e);

        }
    }

    private void updateSectionsDb() {
        try {
            if (getLocalSectionDao() != null) {
                TableUtils.dropTable(getLocalSectionDao(), false);
                TableUtils.createTable(getLocalSectionDao());
            }
        } catch (SQLException e) {
            Log.e(context.getClass().getSimpleName(), "updateSectionsDb: ", e);

        }
    }  private void updateSclActsDb() {
        try {
            if (getLocalSclActsDao() != null) {
                TableUtils.dropTable(getLocalSclActsDao(), false);
                TableUtils.createTable(getLocalSclActsDao());
            }
        } catch (SQLException e) {
            Log.e(context.getClass().getSimpleName(), "updateSclActsDb: ", e);

        }
    }
}