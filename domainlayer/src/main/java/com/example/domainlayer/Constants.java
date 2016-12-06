package com.example.domainlayer;

/**
 * Created by thoughtchimp on 11/29/2016.
 */

public class Constants {

    private static final String BASE_URL = "http://192.168.0.103:8000/api";
    public static final String LOGIN_URL = BASE_URL + "/login";
    public static final String OTP_VERIFY_URL = BASE_URL + "/verify";

    public static final String EMAIL_TEMP = "manit@gmail.com";




    // Request/Response TAGs
    public static final String KEY_ID = "id";
    public static final String KEY_USER_ID = "userId";

    public static final String KEY_NAME = "name";
    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_LAST_NAME = "lastName";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE_NUM = "phone";
    public static final String KEY_MOBILE= "mobile";

    public static final String KEY_TYPE = "type";
    public static final String KEY_DEVICE_TYPE = "deviceType";
    public static final String KEY_DEVICE_TOKEN = "deviceToken";
    public static final String KEY_ACCESS_TOKEN = "accessToken";

    public static final String KEY_IS_ACTIVE= "isActive";
    public static final String KEY_OTP= "otp";
    public static final String KEY_AVATAR= "avatar";
    public static final String KEY_LAST_LOGIN= "lastLogin";
    public static final String VALUE= "null";

    public static final String KEY_MILES= "miles";
    public static final String KEY_WOW= "wows";


    public static final String KEY_SCHOOL_ID= "schoolId";
    public static final String KEY_SECTIONS= "sections";
    public static final String KEY_SECTION= "section";
    public static final String KEY_CLASS= "class";

    public static final String KEY_ACTIVITIES= "activities";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TITLE= "title";
    public static final String KEY_BODY= "body";
    public static final String KEY_IMAGE= "image";




    //School
    public static final String KEY_LOGO= "logo";
    public static final String KEY_LOGO_SMALL= "logo";
    public static final String KEY_LOCATION= "location";
    public static final String KEY_ADDRESS= "address";
    public static final String KEY_TIMESTAMP= "timestamp";
    public static final String KEY_PENDING_MILESTONES="pending_milestones";
    public static final String KEY_COMPLETED_MILESTONES="completed";
    public static final String KEY_TOTAL_MILESTONES="total";


    //Database Table Names
    public static final String TABLE_NAME_USERS = "users";
    public static final String TABLE_NAME_SECTIONS = "sections";


    //type constants
    //activity-bulletin
    public static final String TYPE_BULLETIN= "bulletin_board";
    public static final String TYPE_ACTIVITY= "activity";


}
