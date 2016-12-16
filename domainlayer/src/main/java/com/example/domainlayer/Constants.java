package com.example.domainlayer;

/**
 * Created by thoughtchimp on 11/29/2016.
 */

public class Constants {

    private static final String BASE_URL = "http://192.168.0.104:8000/api/";
    public static final String LOGIN_URL = BASE_URL + "login";
    public static final String OTP_VERIFY_URL = BASE_URL + "verify";
    public static final String MILES_URL = BASE_URL + "milestones/";
    public static final String MILES_URL_SUFFIX = "miles";
    public static final String TRAININGS_URL = BASE_URL + "milestones/";
    public static final String MILES_TRAININGS_URL = BASE_URL + "section/4/milesTrainings";
    public static final String MCQ_URL = TRAININGS_URL + "1/trainings/5/mcqs";
    public static final String MCQ_RESULT_URL = TRAININGS_URL + "1/trainings/15/submitMcqResults";
    public static final String SECTIONS_URL = BASE_URL + "schools/2/sections";
    public static final String INTRO_TRAINING_URL = BASE_URL+"introTraining";
    public static final String ARCHIVE_URL = BASE_URL+"section/4/archiveMilesTrainings";

    public static final String TRAININGS_URL_SUFFIX = "/trainings";


    public static final String TEMP_EMAIL = "manit@gmail.com";
    public static final String TEMP_ACCESS_TOKEN = "1234567890/qwertyuiop";
    public static final String TEMP_OTP = "1111";
    public static final String TEMP_DEVICE_TYPE = "Android";//Android or Ios
    public static final String TEMP_DEVICE_TOKEN = "abcdefghijklmnopqrstuvwxyz";

    // Request/Response TAGs
    public static final String KEY_ID = "id";
    public static final String KEY_USER_ID = "userId";

    public static final String KEY_NAME = "name";
    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_LAST_NAME = "lastName";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE_NUM = "phone";
    public static final String KEY_MOBILE = "mobile";

    public static final String KEY_TYPE = "type";
    public static final String KEY_DEVICE_TYPE = "deviceType";
    public static final String KEY_DEVICE_TOKEN = "deviceToken";
    public static final String KEY_ACCESS_TOKEN = "accessToken";

    public static final String KEY_IS_ACTIVE = "isActive";
    public static final String KEY_IS_TRAINING = "isTraining";
    public static final String KEY_OTP = "otp";
    public static final String KEY_AVATAR = "avatar";
    public static final String KEY_LAST_LOGIN = "lastLogin";
    public static final String VALUE = "null";

    public static final String KEY_MILES = "miles";
    public static final String KEY_WOW = "wows";
    public static final String KEY_MILESTONE_NAME = "milestoneName";
    public static final String KEY_MILESTONE_ID = "milestoneId";
    public static final String KEY_MILE_INDEX = "mileIndex";
    public static final String KEY_NOTE = "note";
    public static final String KEY_LIKES_COUNT = "likes_count";

    public static final String KEY_ANSWER = "answer";
    public static final String KEY_QUESTION = "question";
    public static final String KEY_OPTIONS= "options";


    public static final String KEY_SCHOOL_ID = "schoolId";
    public static final String KEY_SCHOOL_NAME = "schoolName";
    public static final String KEY_SECTIONS = "sections";
    public static final String KEY_SECTION = "section";
    public static final String KEY_SECTION_ID = "sectionId";
    public static final String KEY_CLASS = "class";
    public static final String KEY_INTRO_CONTENT = "contentMetaSequence";
    public static final String KEY_ARCHIVE = "archive";
    public static final String KEY_UNDOABLE_ID= "undoableId";


    public static final String PREFIX_CLASS = "Class ";
    public static final String PREFIX_SECTION = "Section ";

    public static final String KEY_ACTIVITIES = "activities";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TITLE = "title";
    public static final String KEY_BODY = "body";
    public static final String KEY_IMAGE = "image";
    public static final String SPACE = " ";
    public static final String SUFFIX_WOWS = SPACE + "Wows";
    public static final String SUFFIX_MILES = SPACE + "Miles";


    //Mile types
    public static final String TYPE_AUDIO = "audio";
    public static final String TYPE_VIDEO = "video";
    public static final String TYPE_TEXT = "text";
    public static final String TYPE_IMAGE = "image";


    //School
    public static final String KEY_LOGO = "logo";
    public static final String KEY_LOGO_SMALL = "logo";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_TIMESTAMP = "created_at";
    public static final String KEY_PENDING_MILESTONES = "pending_milestones";
    public static final String KEY_COMPLETED_MILESTONES = "completed";
    public static final String KEY_TOTAL_MILESTONES = "total";


    //Database Table Names
    public static final String TABLE_NAME_USERS = "users";
    public static final String TABLE_NAME_SECTIONS = "sections";


    //type constants
    //activity-bulletin
    public static final String TYPE_BULLETIN = "bulletin_board";
    public static final String TYPE_ACTIVITY = "activity";
    public static final String TYPE_TEACHER = "1";
    public static final String TYPE_SCL_ADMIN = "2";
    public static final String TYPE_S2M_ADMIN = "3";


    public static final String SHARED_PREFERENCE = "s2mSharedPreference";


    public static final String PLACE_HOLDER_AUDIO = "http://www.iconsdb.com/icons/preview/green/dj-xxl.png";





}
