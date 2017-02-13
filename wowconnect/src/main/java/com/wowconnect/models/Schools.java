package com.wowconnect.models;

import com.wowconnect.domain.Constants;
import com.wowconnect.domain.database.CustomDao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import static com.wowconnect.domain.Constants.TABLE_NAME_SCHOOLS;

/**
 * Created by thoughtchimp on 12/23/2016.
 */
@DatabaseTable(tableName = TABLE_NAME_SCHOOLS, daoClass = CustomDao.class)

public class Schools {
    @DatabaseField(columnName = Constants.KEY_ID, id = true)
    private int id;

    @DatabaseField(columnName = Constants.KEY_NAME)
    private String name;

    @DatabaseField(columnName = Constants.KEY_LOGO)
    private String logo;

    @DatabaseField(columnName = Constants.KEY_LOCALITY)
    private String locality;

    @DatabaseField(columnName = Constants.KEY_CITY)
    private String city;

    @DatabaseField(columnName = Constants.KEY_IS_ACTIVE)
    private boolean isActive;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
