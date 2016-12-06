package com.example.domainlayer.models;

import com.example.domainlayer.Constants;
import com.example.domainlayer.database.CustomDao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import static com.example.domainlayer.Constants.TABLE_NAME_SECTIONS;
import static com.example.domainlayer.Constants.TABLE_NAME_USERS;

/**
 * Created by thoughtchimp on 12/6/2016.
 */
@DatabaseTable(tableName = TABLE_NAME_USERS, daoClass = CustomDao.class)
public class User {


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

    @DatabaseField(columnName = Constants.KEY_MILES)
    private String miles;

    @DatabaseField(columnName = Constants.KEY_WOW)
    private String wow;

    // One-to-one
    @DatabaseField(columnName = TABLE_NAME_SECTIONS, foreign = true)
    private Sections sections;

    public User() {
        // Don't forget the empty constructor, needed by ORMLite.
    }

    public String getWow() {
        return wow;
    }

    public void setWow(String wow) {
        this.wow = wow;
    }

    public String getMiles() {
        return miles;
    }

    public void setMiles(String miles) {
        this.miles = miles;
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

    public Sections getSections() {
        return sections;
    }

    public void setSections(Sections sections) {
        this.sections = sections;
    }
}
