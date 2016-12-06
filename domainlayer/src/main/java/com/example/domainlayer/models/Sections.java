package com.example.domainlayer.models;

import com.example.domainlayer.Constants;
import com.example.domainlayer.database.CustomDao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import static com.example.domainlayer.Constants.TABLE_NAME_SECTIONS;

/**
 * Created by thoughtchimp on 12/6/2016.
 */
@DatabaseTable(tableName = TABLE_NAME_SECTIONS, daoClass = CustomDao.class)

public class Sections {
    @DatabaseField(columnName = Constants.KEY_ID)
    private int id;

    @DatabaseField(columnName = Constants.KEY_CLASS)
    private String _class;

    @DatabaseField(columnName = Constants.KEY_SECTION)
    private String section;

    @DatabaseField(columnName = Constants.KEY_SCHOOL_ID)
    private String schoolId;

    @DatabaseField(columnName = Constants.KEY_TIMESTAMP)
    private String timestamp;

    @DatabaseField(columnName = Constants.KEY_TOTAL_MILESTONES)
    private int totalMiles;

    @DatabaseField(columnName = Constants.KEY_COMPLETED_MILESTONES)
    private int completedMiles;

    public Sections() {
    }

    public int getTotalMiles() {
        return totalMiles;
    }

    public void setTotalMiles(int totalMiles) {
        this.totalMiles = totalMiles;
    }

    public int getCompletedMiles() {
        return completedMiles;
    }

    public void setCompletedMiles(int completedMiles) {
        this.completedMiles = completedMiles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String get_Class() {
        return _class;
    }

    public void set_Class(String _class) {
        this._class = _class;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
