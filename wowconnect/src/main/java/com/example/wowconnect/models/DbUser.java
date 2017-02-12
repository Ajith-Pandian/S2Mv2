package com.example.wowconnect.models;

import com.example.wowconnect.domain.Constants;
import com.example.wowconnect.domain.database.CustomDao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

import static com.example.wowconnect.domain.Constants.TABLE_NAME_USERS;


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
    @DatabaseField(columnName = Constants.KEY_IS_FIRST_LOGIN)
    private boolean isFirstLogin;
    @DatabaseField(columnName = Constants.KEY_TYPE)
    private String type;
    @DatabaseField(columnName = Constants.KEY_SCHOOL_ID)
    private int schoolId;
    @DatabaseField(columnName = Constants.KEY_MILES)
    private String miles;
    @DatabaseField(columnName = Constants.KEY_TRAINING)
    private String trainings;
    @DatabaseField(columnName = Constants.KEY_SCHOOL_NAME)
    private String schoolName;
    @DatabaseField(columnName = Constants.KEY_WOW)
    private String wow;
    @DatabaseField(columnName = Constants.KEY_AVATAR)
    private String avatar;
    @DatabaseField(columnName = Constants.KEY_DOB)
    private String dob;
    @DatabaseField(columnName = Constants.KEY_ANNIVERSARY)
    private String anniversary;
    @DatabaseField(columnName = Constants.KEY_GENDER)
    private String gender;
    @DatabaseField(columnName = Constants.KEY_ROLES)
    private String roles;

    private ArrayList<Sections> sectionsList;
    private ArrayList<Schools> schoolsList;
    private ArrayList<SclActs> sclActs;
    private SclActs bulletin;

    public DbUser() {
    }

    public ArrayList<Sections> getSectionsList() {
        return sectionsList;
    }

    public void setSectionsList(ArrayList<Sections> sectionsList) {
        this.sectionsList = sectionsList;
    }

    public ArrayList<Schools> getSchoolsList() {
        return schoolsList;
    }

    public void setSchoolsList(ArrayList<Schools> schoolsList) {
        this.schoolsList = schoolsList;
    }

    public ArrayList<SclActs> getSclActs() {
        return sclActs;
    }

    public void setSclActs(ArrayList<SclActs> sclActs) {
        this.sclActs = sclActs;
    }

    public SclActs getBulletin() {
        return bulletin;
    }

    public void setBulletin(SclActs bulletin) {
        this.bulletin = bulletin;
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

    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        isFirstLogin = firstLogin;
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

    public String getTrainings() {
        return trainings;
    }

    public void setTrainings(String trainings) {
        this.trainings = trainings;
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

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAnniversary() {
        return anniversary;
    }

    public void setAnniversary(String anniversary) {
        this.anniversary = anniversary;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


}
