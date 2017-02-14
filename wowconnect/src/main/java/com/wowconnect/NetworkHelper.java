package com.wowconnect;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wowconnect.domain.Constants;
import com.wowconnect.domain.database.DataBaseUtil;
import com.wowconnect.domain.network.VolleySingleton;
import com.wowconnect.domain.temp.DataParser;
import com.wowconnect.models.DbUser;
import com.wowconnect.models.Schools;
import com.wowconnect.models.milestones.TMiles;
import com.wowconnect.ui.customUtils.Utils;
import com.wowconnect.ui.customUtils.VolleyStringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import static com.wowconnect.domain.Constants.ACTIVITIES_URL_SUFFIX;
import static com.wowconnect.domain.Constants.ACTIVITY_LIKE_URL_SUFFIX;
import static com.wowconnect.domain.Constants.KEY_ARCHIVED;
import static com.wowconnect.domain.Constants.KEY_CONTENT;
import static com.wowconnect.domain.Constants.KEY_CONTENTS;
import static com.wowconnect.domain.Constants.KEY_DELETE;
import static com.wowconnect.domain.Constants.KEY_MESSAGE;
import static com.wowconnect.domain.Constants.KEY_OPTIONS;
import static com.wowconnect.domain.Constants.KEY_SECTIONS;
import static com.wowconnect.domain.Constants.KEY_USERS;
import static com.wowconnect.domain.Constants.SCHOOLS_URL;
import static com.wowconnect.domain.Constants.SEPERATOR;


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
        networkRequest = new VolleyStringRequest(Request.Method.GET, Constants.CONFIGURATION_URL,
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

        VolleySingleton.getInstance(context).addToRequestQueue(networkRequest);
    }

    public void getDashBoardDetails(NetworkListener networkListener) {
        this.networkListener = networkListener;
        networkRequest = new VolleyStringRequest(Request.Method.GET, SCHOOLS_URL
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

        VolleySingleton.getInstance(context).addToRequestQueue(networkRequest);
    }

    private VolleyStringRequest networkRequest;

    public void getNetworkUsers(NetworkListener networkListener) {
        this.networkListener = networkListener;
        networkRequest = new VolleyStringRequest(Request.Method.GET, SCHOOLS_URL +
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
                user.setWow(userJson.getInt(Constants.KEY_WOW_COUNT));
                user.setMiles(userJson.getString(Constants.KEY_MILES_COMPLETION_COUNT));
                user.setTrainings(userJson.getString(Constants.KEY_TRAININGS_COMPLETION_COUNT));

                JSONObject optionsObject = userJson.getJSONObject(KEY_OPTIONS);
                if (!optionsObject.isNull(Constants.KEY_ANNIVERSARY))
                    user.setAnniversary(optionsObject.getString(Constants.KEY_ANNIVERSARY));
                if (!optionsObject.isNull(Constants.KEY_GENDER))
                    user.setGender(optionsObject.getString(Constants.KEY_GENDER));
                if (!optionsObject.isNull(Constants.KEY_DOB))
                    user.setDob(optionsObject.getString(Constants.KEY_DOB));


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

    public void getIntroTrainings(final NetworkListener networkListener) {
        this.networkListener = networkListener;
        networkRequest = new VolleyStringRequest(Request.Method.GET, Constants.INTRO_TRAININGS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("introTrainingsRequest", "onResponse: " + response);

                        try {
                            JSONObject introResponse = new JSONObject(response);
                            ArrayList<TMiles> introTrainingsList =
                                    new NewDataParser().getMiles(introResponse.getString(Constants.KEY_INTRO_TRAININGS), false);
                            if (introTrainingsList != null)
                                NewDataHolder.getInstance(context).setIntroTrainingsList(introTrainingsList);
                            NetworkHelper.this.networkListener.onFinish();
                        } catch (JSONException ex) {
                            Log.e("introTrainingsRequest", "onResponse: ", ex);
                        }
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("introTrainingsRequest", "onErrorResponse: " + error);

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

        VolleySingleton.getInstance(context).addToRequestQueue(networkRequest);
    }

    public void deleteTeacher(int teacherId, NetworkListener networkListener) {
        this.networkListener = networkListener;
        networkRequest = new VolleyStringRequest(Request.Method.POST,
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
        VolleySingleton.getInstance(context).addToRequestQueue(networkRequest);

    }

    public void deleteSection(int sectionId, NetworkListener networkListener) {
        this.networkListener = networkListener;
        networkRequest = new VolleyStringRequest(Request.Method.POST,
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
        VolleySingleton.getInstance(context).addToRequestQueue(networkRequest);

    }

    public void getUserSections(final NetworkListener networkListener) {
        this.networkListener = networkListener;
        networkRequest = new VolleyStringRequest(Request.Method.GET,
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

        VolleySingleton.getInstance(context).addToRequestQueue(networkRequest);

    }

    public void getMilestoneContent(int sectionId, final NetworkListener networkListener) {
        this.networkListener = networkListener;
        networkRequest = new VolleyStringRequest(Request.Method.GET,
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


        VolleySingleton.getInstance(S2MApplication.getAppContext()).addToRequestQueue(networkRequest);
    }

    public void getArchiveContent(int sectionId, final NetworkListener networkListener) {
        this.networkListener = networkListener;
        networkRequest = new VolleyStringRequest(Request.Method.GET,

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
                            Collections.reverse(archiveList);
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
                        Log.d("archiveRequest", "onErrorResponse: " + error);

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

    public void sendFirebaseTokenToServer(final String token) {
        networkRequest = new VolleyStringRequest(Request.Method.POST, Constants.UPDATE_DEVICE_TOKEN,
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
        VolleySingleton.getInstance(S2MApplication.getAppContext()).addToRequestQueue(networkRequest);
    }

    public void likeActivity(int activityId, LikeListener listener) {
        likeListener = listener;
        networkRequest = new VolleyStringRequest(Request.Method.POST, SCHOOLS_URL
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

        VolleySingleton.getInstance(context).addToRequestQueue(networkRequest);
    }

    public void refreshSchoolInformation() {
        networkRequest = new VolleyStringRequest(Request.Method.GET,
                SCHOOLS_URL + SharedPreferenceHelper.getSchoolId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("getSchoolInfoRequest", "onResponse: " + response);

                        try {
                            JSONObject schoolResponse = new JSONObject(response);
                            Schools school = new Schools();
                            school.setId(schoolResponse.getInt(Constants.KEY_ID));
                            school.setName(schoolResponse.getString(Constants.KEY_NAME));
                            school.setLogo(schoolResponse.getString(Constants.KEY_LOGO));
                            school.setLocality(schoolResponse.getString(Constants.KEY_LOCALITY));
                            school.setCity(schoolResponse.getString(Constants.KEY_CITY));
                            new DataBaseUtil(context).updateSchool(school);
                        } catch (JSONException ex) {
                            Log.e("getSchoolInfoRequest", "onResponse: ", ex);
                        }
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("getSchoolInfoRequest", "onErrorResponse: " + error);

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

    public void removeLikeListener() {
        if (likeListener != null)
            this.likeListener = null;
        if (networkRequest != null) {
            networkRequest.removeStatusListener();
            networkRequest = null;
        }
    }

    public void removeNetworkListener() {
        if (networkListener != null)
            this.networkListener = null;
        if (networkRequest != null) {
            networkRequest.removeStatusListener();
            networkRequest = null;
        }
    }

    public interface NetworkListener {
        void onFinish();
    }

    public interface LikeListener {
        void onLiked();

        void onUnLiked();
    }

}
