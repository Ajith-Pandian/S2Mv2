package com.wowconnect.domain;

/**
 * Created by thoughtchimp on 11/29/2016.
 */

public class Constants {
    //public static final String  SERVER_ADDRESS= "http://192.168.0.104:8000/";
    public static final String SERVER_ADDRESS = "http://35.154.101.44/";
    public static final String BASE_URL = SERVER_ADDRESS + "api/";
    public static final String CONFIGURATION_URL = BASE_URL + "configuration";
    public static final String USER_URL = BASE_URL + "user/";
    public static final String UPDATE_PROFILE_URL = BASE_URL + "user/update_profile";
    public static final String USER_SECTIONS_URL = BASE_URL + "user/sections";
    public static final String SCHOOLS_URL = BASE_URL + "schools/";
    public static final String REGISTER_URL = BASE_URL + "register_requests/create";

    public static final String KEY_INTRO_TRAININGS = "intro_trainings";
    public static final String INTRO_TRAININGS_URL = BASE_URL + KEY_INTRO_TRAININGS;

    public static final String VALIDATE_URL = USER_URL + "validate";
    public static final String AUTHENTICATE_URL = USER_URL + "authenticate";
    public static final String RESEND_OTP_URL = USER_URL + "resend_otp";
    public static final String UPDATE_DEVICE_TOKEN = USER_URL + "update_device_token";


    public static final String USER_DETAILS_URL = BASE_URL + "users/";
    public static final String MILES_URL = BASE_URL + "milestones/";
    public static final String MILESTONES_URL = BASE_URL + "milestones";
    public static final String MILES_URL_SUFFIX = "miles";
    public static final String TRAININGS_URL = BASE_URL + "milestones/";
    public static final String MILES_TRAININGS_URL = BASE_URL + "section/4/milesTrainings";
    public static final String MCQ_URL = TRAININGS_URL + "/6/mcqs";
    public static final String KEY_MCQS = "mcqs";
    public static final String MCQ_RESULT_URL = TRAININGS_URL + "1/trainings/15/submitMcqResults";
    public static final String SECTIONS_URL = SCHOOLS_URL + "2/sections";
    public static final String DETAILED_SECTIONS_URL = SCHOOLS_URL + "3/detailedSections";
    public static final String ARCHIVE_URL = BASE_URL + "section/4/archiveMilesTrainings";
    public static final String FEEDBACK_CREATE_URL = BASE_URL + "feedback/create";
    public static final String FEEDBACK_UNDO_URL = BASE_URL + "feedback/undo";
    public static final String ACTIVITIES_URL_SUFFIX = "/activities/";
    public static final String NETWORK_URL_SUFFIX = "/network";
    public static final String GET_TEACHERS_URL_SUFFIX = "/getTeachers";
    public static final String ADD_TEACHERS_URL_SUFFIX = "/addTeacher";
    public static final String DELETE_TEACHERS_URL_SUFFIX = "/deleteTeacher";
    public static final String ADD_SECTIONS_URL_SUFFIX = "/create";
    public static final String ACTIVITY_LIKE_URL_SUFFIX = "/like";
    public static final String TRAININGS_SUFFIX = "/trainings";
    public static final String MCQS_SUFFIX = "/mcqs";
    public static final String SEPERATOR = "/";
    public static final String MILE_TRAININGS_SUFFIX = "/milesTrainings";
    public static final String NETWORK_SECTIONS_URL_SUFFIX = "/networkSections";
    public static final String ADD_SECTIONS_URL = SECTIONS_URL + ADD_SECTIONS_URL_SUFFIX;
    public static final String NETWORK_SECTIONS_URL = SCHOOLS_URL + "2" + NETWORK_SECTIONS_URL_SUFFIX;


    public static final String CREATE_TICKET_URL = BASE_URL + "createTicket";
    public static final String CREATE_MSG_URL = BASE_URL + "sendMsg";
    public static final String MANAGE_TICKET_URL = BASE_URL + "manageTicket";


    public static final String KEY_CREATOR_ID = "creatorId";
    public static final String KEY_CREATE = "create";
    public static final String KEY_CREATED_AT = "created_at";
    public static final String KEY_UPDATE = "update";
    public static final String KEY_ASSIGN = "assign";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_RECEIVER_ID = "receiverId";
    public static final String KEY_SUBJECT = "subject";
    public static final String KEY_USERS = "users";
    public static final String KEY_DASHBOARD = "dashboard";


    public static final String TEMP_EMAIL = "manit@gmail.com";
    public static final String TEMP_ACCESS_TOKEN = "1234567890/qwertyuiop";
    // public static final String TEMP_ACCESS_TOKEN = "1234567899/qwertyuiop";
    public static final String TEMP_OTP = "123456";
    public static final String TEMP_DEVICE_TYPE = "android";//Android or Ios
    public static final String TEMP_DEVICE_TOKEN = "abcdefghijklmnopqrstuvwxyz";
    public static final String KEY_STUDENT_COUNT = "students_count";
    public static final String KEY_TEACHER_ID = "teacher_id";
    public static final String KEY_TEACHER_NAME = "teacher_name";
    public static final String KEY_TEACHERS = "teachers";

    // Request/Response TAGs
    public static final String KEY_DB_ID = "db_id";
    public static final String KEY_ID = "id";
    public static final String KEY_USER_ID = "userId";

    public static final String KEY_USER_NAME = "username";
    public static final String COUNTRY_CODE = "91";

    public static final String KEY_NAME = "name";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_EMAIL = "email";
    public static final String TEXT_EMAIL = "Email";
    public static final String KEY_PHONE_NUM = "phone";
    public static final String TEXT_PHONE_NUMBER = "Phone Number";
    public static final String KEY_MOBILE_NO = "mobile_no";

    public static final String KEY_TYPE = "type";
    public static final String KEY_DATA = "data";
    public static final String KEY_HAS_READ = "has_read";
    public static final String KEY_DEVICE_TYPE = "device_type";
    public static final String KEY_DEVICE_TOKEN = "device_token";
    public static final String KEY_ACCESS_TOKEN = "access_token";
    public static final String KEY_CONFIGURATION = "configuration";
    public static final String KEY_FEEDBACK_QUESTIONS = "feedback_questions";

    public static final String KEY_IS_ACTIVE = "isActive";
    public static final String KEY_IS_TRAINING = "isTraining";
    public static final String KEY_IS_COMPLETED = "is_completed";
    public static final String KEY_OTP = "otp";
    public static final String KEY_AVATAR = "avatar";
    public static final String KEY_DOB = "dob";
    public static final String TEXT_DOB = "D.O.B";
    public static final String KEY_ANNIVERSARY = "anniversary";
    public static final String TEXT_ANNIVERSARY = "Anniversary";
    public static final String KEY_GENDER = "gender";
    public static final String TEXT_GENDER = "Gender";
    public static final String KEY_PROFILE_PICTURE = "profile_picture";
    public static final String KEY_USER_TYPE = "user_type";
    public static final String KEY_ROLES = "roles";
    public static final String KEY_ROLES_ARRAY = "roles[]";
    public static final String KEY_LAST_LOGIN = "lastLogin";
    public static final String KEY_IS_FIRST_LOGIN = "is_first_time_login";
    public static final String KEY_IS_LOGGED_IN = "is_logged_in";
    public static final String VALUE = "null";

    public static final String KEY_MILES = "miles";
    public static final String KEY_MILE = "mile";
    public static final String KEY_MILES_TRAININGS = "miles_trainings";
    public static final String KEY_WOW = "wows";
    public static final String KEY_WOW_COUNT = "wow_count";
    public static final String KEY_TRAINING = "trainings";
    public static final String TYPE_TRAINING = "training";
    public static final String TYPE_MILE = "mile";
    public static final String KEY_MILESTONE_NAME = "milestone_name";
    public static final String KEY_NUM_OF_STUDS = "student_count";
    public static final String KEY_NOTIFICATIONS = "notifications";
    public static final String KEY_MILESTONE = "milestone";
    public static final String KEY_MILESTONES = "milestones";
    public static final String KEY_MILESTONE_PREFIX = "Milestone";
    public static final String KEY_MILESTONE_ID = "milestone_id";
    public static final String KEY_MILE_ID = "mileId";
    public static final String KEY_CONTENT_INDEX = "content_index";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_DELETE = "delete";
    public static final String KEY_LIKES_COUNT = "likes_count";
    public static final String KEY_COUNTRY_CODES = "country_codes";
    public static final String KEY_COUNTRY_CODE = "country_code";
    public static final String KEY_COMMENT = "comment";
    public static final String KEY_COLOR_SCHEME = "color_scheme";

    public static final String KEY_ANSWER = "answer";
    public static final String KEY_QUESTION = "question";
    public static final String KEY_RESULT = "result";
    public static final String KEY_CHOICES = "choices";
    public static final String KEY_OPTIONS = "options";
    public static final String KEY_FEEDBACK_ID = "feedbackId";


    public static final String KEY_SCHOOL_ID = "school_id";
    public static final String KEY_SCHOOL_NAME = "school_name";
    public static final String TEXT_SCHOOL_NAME = "School Name";
    public static final String KEY_SCHOOLS = "schools";
    public static final String KEY_SCHOOL = "school";
    public static final String KEY_SECTIONS = "sections";
    public static final String KEY_SECTION = "section";
    public static final String KEY_SECTION_NAME = "section_name";
    public static final String KEY_BULLETIN_BOARD= "bulletin_board";
    public static final String KEY_SECTION_ID = "sectionId";
    public static final String KEY_CLASS = "class";
    public static final String KEY_CLASS_NAME = "class_name";
    public static final String KEY_INTRO_CONTENT = "contentMetaSequence";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_CONTENTS = "contents";
    public static final String KEY_COMPLETE = "complete";
    public static final String KEY_UNDO= "undo";
    public static final String KEY_ARCHIVED = "archived";
    public static final String KEY_CONTENT_DATA = "content_data";
    public static final String KEY_ARCHIVE = "archive";
    public static final String KEY_UNDOABLE_ID = "undoableId";
    public static final String KEY_REASON = "reason";

    public static final String KEY_THUMBS_UP = "thumbs_up";
    public static final String KEY_THUMBS = "thumbs";
    public static final String KEY_THUMBS_DOWN = "thumbs_down";
    public static final String KEY_UP = "up";
    public static final String KEY_DOWN = "down";


    public static final String PREFIX_CLASS = "Class ";
    public static final String PREFIX_SECTION = "Section ";
    public static final String KEY_TICKET_ID = "ticketId";
    public static final String KEY_NEW_STATUS = "newStatus";

    public static final String KEY_ACTIVITIES = "activities";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TITLE = "title";
    public static final String KEY_BODY = "body";
    public static final String KEY_AUDIO_URL = "audio_url";
    public static final String KEY_AUDIO_POSTER = "audio_poster";
    public static final String KEY_VIDEO_ID = "video_id";
    public static final String KEY_VIDEO_POSTER = "video_poster";
    public static final String KEY_VIDEO_POSTER_HD = "video_poster_hd";
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
    public static final String KEY_LOCALITY = "locality";
    public static final String KEY_LOGO_SMALL = "logo";
    public static final String KEY_CITY = "city";
    public static final String KEY_ADDRESS = "address";


    public static final String KEY_TIMESTAMP = "created_at";
    public static final String KEY_ICON = "icon";
    public static final String IS_LIKED = "is_liked";
    public static final String KEY_PENDING_MILESTONES = "pending_milestones";
    public static final String KEY_COMPLETED = "completed";
    public static final String KEY_MILES_COMPLETION_COUNT = "miles_completion_count";
    public static final String KEY_MILES_COMPLETED_COUNT = "miles_completed_count";
    public static final String KEY_TRAININGS_COMPLETION_COUNT = "trainings_completion_count";
    public static final String KEY_MILES_COUNT = "miles_count";
    public static final String KEY_COMPLETED_MILES = "completedMiles";
    public static final String KEY_TOTAL = "total";
    public static final String KEY_TOTAL_MILES = "totalMiles";


    //Database Table Names
    public static final String TABLE_NAME_USERS = "users";
    public static final String TABLE_NAME_SECTIONS = "sections";
    public static final String TABLE_NAME_SCHOOLS = "schools";
    public static final String TABLE_NAME_SCHOOL_ACTIVITIES = "school_activities";


    //type constants
    //activity-bulletin
    public static final String TYPE_BULLETIN = "bulletin_board";
    public static final String TYPE_ACTIVITY = "activity";
    public static final String LIKED = "Liked!";
    public static final String UNLIKED = "Unliked!";

    //User types based on roles
    public static final String TYPE_SCL_ADMIN = "school_admin";
    public static final String TYPE_T_SCL_ADMIN = "t_school_admin";

    //User types based on roles
    public static final String TEXT_TEACHER = "Teacher";
    public static final String TEXT_S2M_ADMIN = "S2M Admin";
    public static final String TEXT_SCL_ADMIN = "School Admin";
    public static final String TEXT_COORDINATOR = "Coordinator";

    //User types
    public static final String USER_TYPE_SCHOOL = "school";
    public static final String USER_TYPE_S2M_ADMIN = "s2m_admin";

    //User roles

    public static final String ROLE_TEACHER = "teacher";
    public static final String ROLE_SCL_ADMIN  = "school_admin";
    public static final String ROLE_COORDINATOR  = "coordinator";

    public static final String ROLE_S2M_CONTENT = "s2m_content";
    public static final String ROLE_S2M_HEAD = "s2m_head";
    public static final String ROLE_S2M_RELATION  = "s2m_relation";

    public static final String SHARED_PREFERENCE = "s2mSharedPreference";


    public static final String PLACE_HOLDER_AUDIO = "http://www.iconsdb.com/icons/preview/green/dj-xxl.png";

    //Firebase constants
    public static final String FB_CHILD_TICKET_DETAILS = "details";
    public static final String FB_CHILD_TICKET = "ticket";
    public static final String FB_CHILD_CONVERSATIONS = "conversations";
    public static final String FB_CHILD_MESSAGE = "message";


    public static final String STATUS_OPEN = "open";
    public static final String STATUS_CLOSED = "closed";
    public static final String STATUS_RESOLVED = "resolved";

    public static final int AUDIO_RECORD_CODE = 123;


    public static final String HEADER_ACCEPT = "application/vnd.wowconnect.v1+json";
    public static final String KEY_ACCEPT = "Accept";
    public static final String KEY_AUTHORIZATION = "Authorization";



    public static final String FB_DATA_TYPE_USER_JOINED_SCHOOL = "user_joined_school";
    public static final String FB_DATA_TYPE_USER_UPDATED = "user_updated";
    public static final String FB_DATA_TYPE_USER_LEFT_SCHOOL = "user_left_school";
    public static final String FB_DATA_SECTION_ADDED = "section_added";
    public static final String FB_DATA_SECTION_DELETED = "section_deleted";
    public static final String FB_DATA_BULLETIN_BOARD= "bulletin_board";
    public static final String FB_DATA_SCHOOL_UPDATED= "school_updated";
    public static final String FB_DATA_WOW_GIVEN= "wow_given";
    public static final String FB_DATA_GOT_WOW= "got_wow";
    public static final String FB_DATA_MILES_COMPLETED= "miles_completed";
    public static final String FB_DATA_INTRO_TRAININGS_COMPLETED= "intro_trainings_completed";
    public static final String FB_DATA_TRAININGS_COMPLETED= "training_completed";
    public static final String FB_DATA_USER_JOINED_SCHOOL= "user_joined_school";
    public static final String FB_DATA_SECTION_ASSIGNED= "section_assigned";
    public static final String FB_DATA_BIRTHDAY= "birthday";
    public static final String FB_DATA_ANNIVERSARY= "anniversary";
    public static final String FB_NOTIFICATION_BROADCAST= "broadcast";

}
