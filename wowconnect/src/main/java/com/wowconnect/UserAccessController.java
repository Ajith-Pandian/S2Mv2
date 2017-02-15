package com.wowconnect;

import com.wowconnect.domain.Constants;
import com.wowconnect.domain.database.DataBaseUtil;
import com.wowconnect.models.DbUser;

import java.util.ArrayList;

/**
 * Created by Ajit on 15-02-2017.
 */

public class UserAccessController {
    private int userId;
    private boolean hasNavigationMenu, hasAddSectionMenu,
            hasIntroTrainings, hasProfileSections, isTeacher, isSchoolAdmin, isS2mAdmin;
    private int navigationMenu;
    private String designation;

    public UserAccessController(int userId) {
        this.userId = userId;
        validateUser();
    }

    private void validateUser() {
        DbUser user = new DataBaseUtil(S2MApplication.getAppContext()).getUser(userId);
        String userType = user.getType();
        ArrayList<String> userRoles =
                new NewDataParser().getUserRoles(
                        S2MApplication.getAppContext(), user.getId());
        String designation = "";

        switch (userType) {
            case Constants.USER_TYPE_SCHOOL:
                if (userRoles.contains(Constants.ROLE_SCL_ADMIN) || userRoles.contains(Constants.ROLE_COORDINATOR)) {
                    setHasNavigationMenu(true);
                    setNavigationMenu(R.menu.activity_landing_drawer_school_admin);
                    setHasAddSectionMenu(true);
                    setSchoolAdmin(true);
                } else {
                    setHasNavigationMenu(false);
                    setHasAddSectionMenu(false);
                    setTeacher(true);
                }
                if (userRoles.contains(Constants.ROLE_COORDINATOR))
                    designation = Constants.TEXT_COORDINATOR;
                else if (userRoles.contains(Constants.ROLE_SCL_ADMIN))
                    designation = Constants.TEXT_SCL_ADMIN;
                else designation = Constants.TEXT_TEACHER;

                break;
            case Constants.USER_TYPE_S2M_ADMIN:
                setHasNavigationMenu(true);
                setNavigationMenu(R.menu.activity_landing_drawer_s2m_admin);
                setHasAddSectionMenu(true);
                designation = Constants.TEXT_S2M_ADMIN;
                setS2mAdmin(true);
                break;
        }
        setDesignation(designation);
        if (userRoles.contains(Constants.ROLE_TEACHER)) {
            setHasIntroTrainings(true);
            setHasProfileSections(true);
        } else {
            setHasIntroTrainings(false);
            setHasProfileSections(false);
        }
    }

    public boolean hasNavigationMenu() {
        return hasNavigationMenu;
    }

    private void setHasNavigationMenu(boolean hasNavigationMenu) {
        this.hasNavigationMenu = hasNavigationMenu;
    }

    public boolean hasAddSectionMenu() {
        return hasAddSectionMenu;
    }

    private void setHasAddSectionMenu(boolean hasAddSectionMenu) {
        this.hasAddSectionMenu = hasAddSectionMenu;
    }

    public int getNavigationMenu() {
        return navigationMenu;
    }

    private void setNavigationMenu(int navigationMenu) {
        this.navigationMenu = navigationMenu;
    }

    public boolean hasIntroTrainings() {
        return hasIntroTrainings;
    }

    private void setHasIntroTrainings(boolean hasIntroTrainings) {
        this.hasIntroTrainings = hasIntroTrainings;
    }

    public boolean hasProfileSections() {
        return hasProfileSections;
    }

    private void setHasProfileSections(boolean hasProfileSections) {
        this.hasProfileSections = hasProfileSections;
    }

    public String getDesignation() {
        return designation;
    }

    private void setDesignation(String designation) {
        this.designation = designation;
    }

    public boolean isTeacher() {
        return isTeacher;
    }

    public void setTeacher(boolean teacher) {
        isTeacher = teacher;
    }

    public boolean isSchoolAdmin() {
        return isSchoolAdmin;
    }

    public void setSchoolAdmin(boolean schoolAdmin) {
        isSchoolAdmin = schoolAdmin;
    }

    public boolean isS2mAdmin() {
        return isS2mAdmin;
    }

    public void setS2mAdmin(boolean s2mAdmin) {
        isS2mAdmin = s2mAdmin;
    }
}
