package com.example.wowconnect;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.example.wowconnect.domain.Constants;
import com.example.wowconnect.domain.network.VolleySingleton;
import com.example.wowconnect.domain.temp.DataParser;
import com.example.wowconnect.models.DbUser;
import com.example.wowconnect.models.User;
import com.example.wowconnect.models.milestones.TMiles;
import com.example.wowconnect.ui.customUtils.Utils;
import com.example.wowconnect.ui.customUtils.VolleyStringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import static com.example.wowconnect.domain.Constants.ACTIVITIES_URL_SUFFIX;
import static com.example.wowconnect.domain.Constants.ACTIVITY_LIKE_URL_SUFFIX;
import static com.example.wowconnect.domain.Constants.KEY_ARCHIVED;
import static com.example.wowconnect.domain.Constants.KEY_CONTENT;
import static com.example.wowconnect.domain.Constants.KEY_CONTENTS;
import static com.example.wowconnect.domain.Constants.KEY_DELETE;
import static com.example.wowconnect.domain.Constants.KEY_MESSAGE;
import static com.example.wowconnect.domain.Constants.KEY_OPTIONS;
import static com.example.wowconnect.domain.Constants.KEY_SECTIONS;
import static com.example.wowconnect.domain.Constants.KEY_TEACHERS;
import static com.example.wowconnect.domain.Constants.KEY_USERS;
import static com.example.wowconnect.domain.Constants.SCHOOLS_URL;
import static com.example.wowconnect.domain.Constants.SEPERATOR;


/**
 * Created by thoughtchimp on 12/20/2016.
 */

public class NetworkHelper {
    private final Context context;
    private NetworkListener networkListener;
    private LikeListener likeListener;
    private final String TAG = getClass().getSimpleName();

    public NetworkHelper(Context context) {
        this.context = context;
    }


    public void downloadConfiguration() {
        VolleyStringRequest configurationRequest = new VolleyStringRequest(Request.Method.GET, Constants.CONFIGURATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                        Log.d("configurationRequest", "onResponse " + response);
                        SharedPreferenceHelper.storeConfiguration(response);
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("configurationRequest", "onError: " + error);
                    }
                }, new VolleyStringRequest.StatusCodeListener() {
            String TAG = "VolleyStringReq";

            @Override
            public void onBadRequest() {
                Log.d(TAG, "onBadRequest: ");
            }

            @Override
            public void onUnauthorized() {
                Log.d(TAG, "onUnauthorized: ");
            }

            @Override
            public void onNotFound() {
                Log.d(TAG, "onNotFound: ");
            }

            @Override
            public void onConflict() {
                Log.d(TAG, "onConflict: ");
            }

            @Override
            public void onTimeout() {
                Log.d(TAG, "onTimeout: ");
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(configurationRequest);
    }

    public void getDashBoardDetails(NetworkListener networkListener) {
        this.networkListener = networkListener;
        VolleyStringRequest getDashBoardRequest = new VolleyStringRequest(Request.Method.GET, SCHOOLS_URL
                + SharedPreferenceHelper.getSchoolId() + SEPERATOR + Constants.KEY_DASHBOARD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                        Log.d("getDashBoardRequest", "onResponse " + response);
                        //storeResponse(response);
                        NewDataHolder.getInstance(context).saveDashboardDetails(response);
                        NetworkHelper.this.networkListener.onFinish();
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("getDashBoardRequest", "onError: " + error);
                    }
                }, new VolleyStringRequest.StatusCodeListener() {
            String TAG = "VolleyStringReq";

            @Override
            public void onBadRequest() {
                Log.d(TAG, "onBadRequest: ");
            }

            @Override
            public void onUnauthorized() {
                Log.d(TAG, "onUnauthorized: ");
            }

            @Override
            public void onNotFound() {
                Log.d(TAG, "onNotFound: ");
            }

            @Override
            public void onConflict() {
                Log.d(TAG, "onConflict: ");
            }

            @Override
            public void onTimeout() {
                Log.d(TAG, "onTimeout: ");
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(getDashBoardRequest);
    }

    public void getNetworkUsers(NetworkListener networkListener) {
        this.networkListener = networkListener;
        VolleyStringRequest networkRequest = new VolleyStringRequest(Request.Method.GET, SCHOOLS_URL +
                SharedPreferenceHelper.getSchoolId() + SEPERATOR + KEY_USERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("networkRequest", "onResponse: " + response);
                        saveNetworkUsers(response);
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("networkRequest", "onErrorResponse: " + error);

                    }
                }, new VolleyStringRequest.StatusCodeListener() {
            String TAG = "VolleyStringReq";

            @Override
            public void onBadRequest() {
                Log.d(TAG, "onBadRequest: ");
            }

            @Override
            public void onUnauthorized() {
                Log.d(TAG, "onUnauthorized: ");
            }

            @Override
            public void onNotFound() {
                Log.d(TAG, "onNotFound: ");
            }

            @Override
            public void onConflict() {
                Log.d(TAG, "onConflict: ");
            }

            @Override
            public void onTimeout() {
                Log.d(TAG, "onTimeout: ");
            }
        });
        VolleySingleton.getInstance(context).addToRequestQueue(networkRequest);

    }

    private void saveNetworkUsers(String profilesString) {

        ArrayList<DbUser> usersList = new ArrayList<>();
        try {
            JSONObject usersObject = new JSONObject(profilesString);
            JSONArray profilesArray = usersObject.getJSONArray(KEY_USERS);
            for (int i = 0; i < profilesArray.length(); i++) {
                JSONObject userJson = profilesArray.getJSONObject(i);
                DbUser user = new DbUser();
                user.setId(userJson.getInt(Constants.KEY_ID));
                user.setFirstName(userJson.getString(Constants.KEY_FIRST_NAME));
                if (!userJson.isNull(Constants.KEY_LAST_NAME))
                    user.setLastName(userJson.getString(Constants.KEY_LAST_NAME));
                user.setEmail(userJson.getString(Constants.KEY_EMAIL));
                user.setPhoneNum(userJson.getString(Constants.KEY_MOBILE_NO));
                if (!userJson.isNull(Constants.KEY_PROFILE_PICTURE))
                    user.setAvatar(userJson.getString(Constants.KEY_PROFILE_PICTURE));
                user.setWow(userJson.getString(Constants.KEY_WOW_COUNT));
                user.setMiles(userJson.getString(Constants.KEY_MILES_COMPLETION_COUNT));
                user.setTrainings(userJson.getString(Constants.KEY_TRAININGS_COMPLETION_COUNT));

                JSONObject optionsObject = userJson.getJSONObject(KEY_OPTIONS);
                if (!optionsObject.isNull(Constants.KEY_ANNIVERSARY))
                    user.setAnniversary(optionsObject.getString(Constants.KEY_ANNIVERSARY));
                if (!optionsObject.isNull(Constants.KEY_GENDER))
                    user.setGender(optionsObject.getString(Constants.KEY_GENDER));
                if (!optionsObject.isNull(Constants.KEY_DOB))
                    user.setDob(optionsObject.getString(Constants.KEY_DOB));


                user.setWow(userJson.getString(Constants.KEY_WOW_COUNT));
                user.setRoles(userJson.getString(Constants.KEY_ROLES));
                user.setType(userJson.getString(Constants.KEY_USER_TYPE));
                user.setSectionsList(new DataParser().getSectionsListFromJson(userJson.getJSONArray(KEY_SECTIONS), true));
                usersList.add(i, user);
            }
            NewDataHolder.getInstance(context).setNetworkUsers(usersList);
            networkListener.onFinish();
        } catch (JSONException exception) {
            Log.e("NetworkHelper", "saveNetworkUsers: ", exception);
        }
    }

    public void getTeachers(NetworkListener networkListener) {
        this.networkListener = networkListener;
        VolleyStringRequest teacherRequest = new VolleyStringRequest(Request.Method.GET, SCHOOLS_URL + SharedPreferenceHelper.getSchoolId() + SEPERATOR + KEY_TEACHERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("teacherRequest", "onResponse: " + response);
                        saveTeachers(response);
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("teacherRequest", "onErrorResponse: " + error);

                    }
                }, new VolleyStringRequest.StatusCodeListener() {
            String TAG = "VolleyStringReq";

            @Override
            public void onBadRequest() {
                Log.d(TAG, "onBadRequest: ");
            }

            @Override
            public void onUnauthorized() {
                Log.d(TAG, "onUnauthorized: ");
            }

            @Override
            public void onNotFound() {
                Log.d(TAG, "onNotFound: ");
            }

            @Override
            public void onConflict() {
                Log.d(TAG, "onConflict: ");
            }

            @Override
            public void onTimeout() {
                Log.d(TAG, "onTimeout: ");
            }
        });
        VolleySingleton.getInstance(context).addToRequestQueue(teacherRequest);

    }

    private void saveTeachers(String teachersResponse) {
        ArrayList<User> teachersList = new ArrayList<>();
        try {
            JSONObject profileObj = new JSONObject(teachersResponse);
            JSONArray profilesArray = profileObj.getJSONArray(KEY_TEACHERS);
            for (int i = 0; i < profilesArray.length(); i++) {
                JSONObject userJson = profilesArray.getJSONObject(i);
                User user = new User();
                user.setFirstName(userJson.getString(Constants.KEY_FIRST_NAME));
                user.setId(userJson.getInt(Constants.KEY_ID));
                String lastName = userJson.getString(Constants.KEY_LAST_NAME);
                if (lastName != null && !lastName.equals("null"))
                    user.setLastName(lastName);
                String email = userJson.getString(Constants.KEY_EMAIL);
                if (email != null && !email.equals("null"))
                    user.setEmail(email);
                user.setPhoneNum(userJson.getString(Constants.KEY_MOBILE_NO));
                user.setAvatar(userJson.getString(Constants.KEY_PROFILE_PICTURE));

                teachersList.add(i, user);
            }

            //DataHolder.getInstance(getContext()).setTeachersList(teachersList);
            NewDataHolder.getInstance(context).setTeachersList(teachersList);
            this.networkListener.onFinish();

        } catch (JSONException exception) {
            Log.e("DataHolder", "saveUserDetails: ", exception);
        }
    }

    public void deleteTeacher(int teacherId, NetworkListener networkListener) {
        this.networkListener = networkListener;
        VolleyStringRequest teacherRequest = new VolleyStringRequest(Request.Method.POST,
                SCHOOLS_URL + SharedPreferenceHelper.getSchoolId() + SEPERATOR +
                        KEY_USERS + SEPERATOR + teacherId + SEPERATOR + KEY_DELETE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("teacherRequest", "onResponse: " + response);
                        NetworkHelper.this.networkListener.onFinish();
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("teacherRequest", "onErrorResponse: " + error);

                    }
                }, new VolleyStringRequest.StatusCodeListener() {
            String TAG = "VolleyStringReq";

            @Override
            public void onBadRequest() {
                Log.d(TAG, "onBadRequest: ");
            }

            @Override
            public void onUnauthorized() {
                Log.d(TAG, "onUnauthorized: ");
            }

            @Override
            public void onNotFound() {
                Log.d(TAG, "onNotFound: ");
            }

            @Override
            public void onConflict() {
                Log.d(TAG, "onConflict: ");
            }

            @Override
            public void onTimeout() {
                Log.d(TAG, "onTimeout: ");
            }
        });
        VolleySingleton.getInstance(context).addToRequestQueue(teacherRequest);

    }

    public void deleteSection(int sectionId, NetworkListener networkListener) {
        this.networkListener = networkListener;
        VolleyStringRequest teacherRequest = new VolleyStringRequest(Request.Method.POST,
                SCHOOLS_URL + SharedPreferenceHelper.getSchoolId() + SEPERATOR +
                        KEY_SECTIONS + SEPERATOR + sectionId + SEPERATOR + KEY_DELETE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("teacherRequest", "onResponse: " + response);
                        NetworkHelper.this.networkListener.onFinish();
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("teacherRequest", "onErrorResponse: " + error);

                    }
                }, new VolleyStringRequest.StatusCodeListener() {
            String TAG = "VolleyStringReq";

            @Override
            public void onBadRequest() {
                Log.d(TAG, "onBadRequest: ");
            }

            @Override
            public void onUnauthorized() {
                Log.d(TAG, "onUnauthorized: ");
            }

            @Override
            public void onNotFound() {
                Log.d(TAG, "onNotFound: ");
            }

            @Override
            public void onConflict() {
                Log.d(TAG, "onConflict: ");
            }

            @Override
            public void onTimeout() {
                Log.d(TAG, "onTimeout: ");
            }
        });
        VolleySingleton.getInstance(context).addToRequestQueue(teacherRequest);

    }

    public void getUserSections(final NetworkListener networkListener) {
        this.networkListener = networkListener;
        VolleyStringRequest userSectionsRequest = new VolleyStringRequest(Request.Method.GET,
                SCHOOLS_URL + SharedPreferenceHelper.getSchoolId() + SEPERATOR + KEY_SECTIONS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("userSectionsRequest", "onResponse: " + response);

                        try {
                            JSONObject sectionsResponse = new JSONObject(response);
                            NewDataHolder.getInstance(context).saveUserSections(sectionsResponse.getJSONArray(KEY_SECTIONS));
                            networkListener.onFinish();
                        } catch (JSONException ex) {
                            Log.e("userSectionsRequest", "onResponse: ", ex);
                        }
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("userSectionsRequest", "onErrorResponse: " + error);

                    }
                }, new VolleyStringRequest.StatusCodeListener() {
            String TAG = "VolleyStringReq";

            @Override
            public void onBadRequest() {
                Log.d(TAG, "onBadRequest: ");
            }

            @Override
            public void onUnauthorized() {
                Log.d(TAG, "onUnauthorized: ");
            }

            @Override
            public void onNotFound() {
                Log.d(TAG, "onNotFound: ");
                Utils.getInstance().showToast(context.getResources().getString(R.string.er_no_intro_trainings));
            }

            @Override
            public void onConflict() {
                Log.d(TAG, "onConflict: ");
            }

            @Override
            public void onTimeout() {
                Log.d(TAG, "onTimeout: ");
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(userSectionsRequest);

    }

    public void getMilestoneContent(int sectionId, final NetworkListener networkListener) {
        this.networkListener = networkListener;
        VolleyStringRequest milesRequest = new VolleyStringRequest(Request.Method.GET,
                SCHOOLS_URL + SharedPreferenceHelper.getSchoolId() + SEPERATOR
                        + KEY_SECTIONS + SEPERATOR
                        + String.valueOf(sectionId) + SEPERATOR
                        + KEY_CONTENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Miles", "onResponse: " + response);

                        try {
                            JSONObject milesObject = new JSONObject(response);
                            ArrayList<TMiles> milesList = new NewDataParser().getMiles(milesObject.getString(KEY_CONTENTS), false);
                            NewDataHolder.getInstance(context).setMilesList(milesList);
                            networkListener.onFinish();
                        } catch (JSONException ex) {
                            Log.e("Miles", "onResponse:Parsing ", ex);
                        }

                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("Miles", "onErrorResponse: " + error);

                    }
                }, new VolleyStringRequest.StatusCodeListener() {
            String TAG = "VolleyStringReq";

            @Override
            public void onBadRequest() {
                Log.d(TAG, "onBadRequest: ");
            }

            @Override
            public void onUnauthorized() {
                Log.d(TAG, "onUnauthorized: ");
            }

            @Override
            public void onNotFound() {
                Log.d(TAG, "onNotFound: ");
                Utils.getInstance().showToast(context.getResources().getString(R.string.er_no_milestones_in_section));
            }

            @Override
            public void onConflict() {
                Log.d(TAG, "onConflict: ");
            }

            @Override
            public void onTimeout() {
                Log.d(TAG, "onTimeout: ");
            }
        });


        VolleySingleton.getInstance(S2MApplication.getAppContext()).addToRequestQueue(milesRequest);
    }

    public void getArchiveContent(int sectionId, final NetworkListener networkListener) {
        this.networkListener = networkListener;
        VolleyStringRequest archiveRequest = new VolleyStringRequest(Request.Method.GET,

                SCHOOLS_URL + SharedPreferenceHelper.getSchoolId() + SEPERATOR
                        + KEY_SECTIONS + SEPERATOR
                        + String.valueOf(sectionId) + SEPERATOR
                        + KEY_CONTENT + SEPERATOR + KEY_ARCHIVED,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("archiveRequest", "onResponse: " + response);

                        try {
                            JSONObject archiveResponse = new JSONObject(response);

                            ArrayList<TMiles> archiveList =
                                    new NewDataParser().getMiles(archiveResponse.getString(KEY_CONTENTS), true);
                            NewDataHolder.getInstance(context).setArchiveList(archiveList);

                            networkListener.onFinish();
                        } catch (JSONException ex) {
                            Log.e("archiveRequest", "onResponse: ", ex);
                        }
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("introMileDetails", "onErrorResponse: " + error);

                    }
                }, new VolleyStringRequest.StatusCodeListener() {
            String TAG = "VolleyStringReq";

            @Override
            public void onBadRequest() {
                Log.d(TAG, "onBadRequest: ");
            }

            @Override
            public void onUnauthorized() {
                Log.d(TAG, "onUnauthorized: ");
            }

            @Override
            public void onNotFound() {
                Log.d(TAG, "onNotFound: ");
            }

            @Override
            public void onConflict() {
                Log.d(TAG, "onConflict: ");
            }

            @Override
            public void onTimeout() {
                Log.d(TAG, "onTimeout: ");
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(archiveRequest);
    }

    public void sendFirebaseTokenToServer(final String token) {
        VolleyStringRequest tokenRefreshRequest = new VolleyStringRequest(Request.Method.POST, Constants.UPDATE_DEVICE_TOKEN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("tokenRefreshRequest", "onResponse " + response);
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("tokenRefreshRequest", "onResponse: " + error);
                    }
                }, new VolleyStringRequest.StatusCodeListener() {
            String TAG = "VolleyStringReq";

            @Override
            public void onBadRequest() {
                Log.d(TAG, "onBadRequest: ");
            }

            @Override
            public void onUnauthorized() {
                Log.d(TAG, "onUnauthorized: ");
            }

            @Override
            public void onNotFound() {
                Log.d(TAG, "onNotFound: ");
            }

            @Override
            public void onConflict() {
                Log.d(TAG, "onConflict: ");
            }

            @Override
            public void onTimeout() {
                Log.d(TAG, "onTimeout: ");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new ArrayMap<>();
                params.put(Constants.KEY_DEVICE_TOKEN, token);
                return params;
            }

        };
        VolleySingleton.getInstance(S2MApplication.getAppContext()).addToRequestQueue(tokenRefreshRequest);
    }

    public void likeActivity(int activityId, LikeListener listener) {
        likeListener = listener;
        VolleyStringRequest likeRequest = new VolleyStringRequest(Request.Method.POST, SCHOOLS_URL
                + SharedPreferenceHelper.getSchoolId()
                + ACTIVITIES_URL_SUFFIX
                + String.valueOf(activityId)
                + ACTIVITY_LIKE_URL_SUFFIX,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                        Log.d("LikeRequest", "onResponse: " + response);
                        try {
                            JSONObject responseJson = new JSONObject(response);
                            String msg = responseJson.getString(KEY_MESSAGE);
                            if (responseJson.getBoolean(Constants.IS_LIKED))
                                likeListener.onLiked();
                            else
                                likeListener.onUnLiked();
                            Utils.getInstance().showToast(msg);
                        } catch (JSONException e) {
                        }
                        //  showToast("Liked");
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("LikeRequest-error", "onResponse: " + error);
                    }
                }, new VolleyStringRequest.StatusCodeListener() {
            String TAG = "VolleyStringReq";

            @Override
            public void onBadRequest() {
                Log.d(TAG, "onBadRequest: ");
            }

            @Override
            public void onUnauthorized() {
                Log.d(TAG, "onUnauthorized: ");
            }

            @Override
            public void onNotFound() {
                Log.d(TAG, "onNotFound: ");
            }

            @Override
            public void onConflict() {
                Log.d(TAG, "onConflict: ");
            }

            @Override
            public void onTimeout() {
                Log.d(TAG, "onTimeout: ");
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(likeRequest);
    }

    public void removeLikeListener() {
        if (likeListener != null)
            this.likeListener = null;
    }

    public void removeNetworkListener() {
        if (networkListener != null)
            this.networkListener = null;
    }

    public interface NetworkListener {
        void onFinish();
    }

    public interface LikeListener {
        void onLiked();

        void onUnLiked();
    }

}
