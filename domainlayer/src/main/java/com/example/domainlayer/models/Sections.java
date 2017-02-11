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
    @DatabaseField(columnName = Constants.KEY_ID, id = true)
    private int id;

    @DatabaseField(columnName = Constants.KEY_CLASS)
    private String _class;

    @DatabaseField(columnName = Constants.KEY_SECTION)
    private String section;

    @DatabaseField(columnName = Constants.KEY_SCHOOL_ID)
    private int schoolId;

    @DatabaseField(columnName = Constants.KEY_TIMESTAMP)
    private String timestamp;

    @DatabaseField(columnName = Constants.KEY_TOTAL)
    private int totalMiles;

    @DatabaseField(columnName = Constants.KEY_COMPLETED)
    private int completedMiles;

    @DatabaseField(columnName = Constants.KEY_MILESTONE_NAME)
    private String milestoneName;
    @DatabaseField(columnName = Constants.KEY_TEACHER_NAME)
    private String teacherName;

    @DatabaseField(columnName = Constants.KEY_TEACHER_ID)
    private int teacherId;

    @DatabaseField(columnName = Constants.KEY_NUM_OF_STUDS)
    private int numOfStuds;

    public int getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(int milestoneId) {
        this.milestoneId = milestoneId;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public void setMilestoneName(String milestoneName) {
        this.milestoneName = milestoneName;
    }

    @DatabaseField(columnName = Constants.KEY_MILESTONE_ID)
    private int milestoneId;

    public Sections() {
    }

    public Sections(int id, String _class, String section) {
        this.id = id;
        this._class = _class;
        this.section = section;
    }

    public Sections(int id, String _class, String section, int comp_ms, int tot_ms) {
        this.id = id;
        this.completedMiles = comp_ms;
        this.totalMiles = tot_ms;
        this._class = _class;
        this.section = section;
    }

    public Sections(int id, String _class, String section,
                    int completedMiles,
                    int totalMiles,
                    String milestoneName, int milestoneId,
                   int numOfStuds) {
        this.section = section;
        this.id = id;
        this._class = _class;
        this.totalMiles = totalMiles;
        this.completedMiles = completedMiles;
        this.milestoneName = milestoneName;
        this.milestoneId = milestoneId;
        this.numOfStuds = numOfStuds;
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

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getNumOfStuds() {
        return numOfStuds;
    }

    public void setNumOfStuds(int numOfStuds) {
        this.numOfStuds = numOfStuds;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }
}
