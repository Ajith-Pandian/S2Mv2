package com.wowconnect.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by thoughtchimp on 12/6/2016.
 */
public class User implements Serializable {
    private int id;
    private String firstName;
    private String lastName;
    private String name;
    private String email;
    private String phoneNum;
    private String schoolName;
    private String accessToken;
    private String lastLogin;
    private int schoolId;
    private String miles;
    private String wow;
    private String trainings;
    private String avatar;
    private String type;
    private Sections sections;
    private ArrayList<Sections> sectionsList;
    private ArrayList<SclActs> sclActs;
    private Bulletin bulletin;

    public User() {
        // Don't forget the empty constructor, needed by ORMLite.
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
        return lastName = (lastName != null && !lastName.equals("null")) ? lastName : "";
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
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

    public String getTrainings() {
        return trainings;
    }

    public void setTrainings(String trainings) {
        this.trainings = trainings;
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
        return bulletin;
    }

    public void setBulletin(Bulletin bulletin) {
        this.bulletin = bulletin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
