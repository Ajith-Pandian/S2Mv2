package com.example.domainlayer.models;

import com.example.domainlayer.Constants;
import com.example.domainlayer.database.CustomDao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

import static com.example.domainlayer.Constants.TABLE_NAME_SECTIONS;
import static com.example.domainlayer.Constants.TABLE_NAME_USERS;

/**
 * Created by thoughtchimp on 12/8/2016.
 */
@DatabaseTable(tableName = TABLE_NAME_USERS, daoClass = CustomDao.class)

public class DbUser {
    User user;
    @DatabaseField(id = true, columnName = Constants.KEY_ID, canBeNull = false, unique = true)
    private int id;
    @DatabaseField(columnName = Constants.KEY_FIRST_NAME)
    private String firstName;
    @DatabaseField(columnName = Constants.KEY_LAST_NAME)
    private String lastName;
    @DatabaseField(columnName = Constants.KEY_EMAIL)
    private String email;
    @DatabaseField(columnName = Constants.KEY_PHONE_NUM)
    private String phoneNum;
    @DatabaseField(columnName = Constants.KEY_ACCESS_TOKEN)
    private String accessToken;
    @DatabaseField(columnName = Constants.KEY_LAST_LOGIN)
    private String lastLogin;
    @DatabaseField(columnName = Constants.KEY_TYPE)
    private String type;
    @DatabaseField(columnName = Constants.KEY_SCHOOL_ID)
    private int schoolId;
    @DatabaseField(columnName = Constants.KEY_MILES)
    private String miles;
    @DatabaseField(columnName = Constants.KEY_SCHOOL_NAME)
    private String schoolName;
    @DatabaseField(columnName = Constants.KEY_WOW)
    private String wow;
    @DatabaseField(columnName = Constants.KEY_AVATAR)
    private String avatar;
    @DatabaseField(columnName = TABLE_NAME_SECTIONS, foreign = true)
    private Sections sections;
    private ArrayList<Sections> sectionsList;
    private ArrayList<SclActs> sclActs;
    private Bulletin bulletin;

    public DbUser() {
        if(null==user)
        user=new User();
    }

    public ArrayList<Sections> getSectionsList() {
        return sectionsList;
    }

    public void setSectionsList(ArrayList<Sections> sectionsList) {
        this.sectionsList = sectionsList;
    }

    public ArrayList<SclActs> getSclActs() {
        return sclActs;
    }

    public void setSclActs(ArrayList<SclActs> sclActs) {
        this.sclActs = sclActs;
    }

    public Bulletin getBulletin() {
        if (this.bulletin != null)
            return bulletin;
        else
            return user.getBulletin();
    }

    public void setBulletin(Bulletin bulletin) {
        this.bulletin = bulletin;
        user.setBulletin(bulletin);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMiles() {
        return miles;
    }

    public void setMiles(String miles) {
        this.miles = miles;
    }

    public String getWow() {
        return wow;
    }

    public void setWow(String wow) {
        this.wow = wow;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Sections getSections() {
        return sections;
    }

    public void setSections(Sections sections) {
        this.sections = sections;
    }
}
