package com.example.uilayer.landing;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.database.DataBaseUtil;
import com.example.domainlayer.models.DbUser;
import com.example.domainlayer.models.Sections;
import com.example.domainlayer.models.milestones.TMileData;
import com.example.domainlayer.models.milestones.TMiles;
import com.example.domainlayer.network.VolleySingleton;
import com.example.uilayer.NewDataHolder;
import com.example.uilayer.NewDataParser;
import com.example.uilayer.SharedPreferenceHelper;
import com.example.uilayer.customUtils.Utils;
import com.example.uilayer.customUtils.VolleyStringRequest;
import com.example.uilayer.R;
import com.example.uilayer.adapters.SectionsAdapter;
import com.example.uilayer.customUtils.HorizontalSpaceItemDecoration;
import com.example.uilayer.manage.AddOrUpdateListener;
import com.example.uilayer.manage.AddSectionsFragment;
import com.example.uilayer.manage.ManageTeachersActivity;
import com.example.uilayer.manage.TeacherOrSectionListener;
import com.example.uilayer.milestones.MilestonesActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.example.domainlayer.Constants.INTRO_TRAININGS_URL;
import static com.example.domainlayer.Constants.KEY_INTRO_TRAININGS;
import static com.example.domainlayer.Constants.KEY_SECTIONS;
import static com.example.domainlayer.Constants.SCHOOLS_URL;
import static com.example.domainlayer.Constants.SEPERATOR;
import static com.example.domainlayer.Constants.SHARED_PREFERENCE;
import static com.example.domainlayer.Constants.TYPE_S2M_ADMIN;
import static com.example.domainlayer.Constants.TYPE_SCL_ADMIN;
import static com.example.domainlayer.Constants.TYPE_TEACHER;
import static com.example.domainlayer.Constants.TYPE_T_SCL_ADMIN;
import static com.example.domainlayer.Constants.USER_SECTIONS_URL;


/**
 * Created by thoughtchimp on 11/7/2016.
 */

public class SectionsFragment extends Fragment {
    @BindView(R.id.sections_recycle_grid)
    RecyclerView sectionsGrid;
    @BindView(R.id.layout_no_sections)
    RelativeLayout noSectionsLayout;
    @BindView(R.id.imageIntroductory)
    ImageView imageViewIntroductory;
    @BindView(R.id.card)
    CardView introCardView;
    @BindView(R.id.layout_intro_training)
    LinearLayout cardLayout;
    List<Sections> sectionDetails;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sections,
                container, false);
        ButterKnife.bind(this, view);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        sectionsGrid.setLayoutManager(layoutManager);
        sectionsGrid.addItemDecoration(new HorizontalSpaceItemDecoration(getActivity(), 3, 3, 3));
        getUserSections();
        String userType = new DataBaseUtil(getContext()).getUser().getType();
        if ((!userType.equals(TYPE_SCL_ADMIN) && !userType.equals(TYPE_S2M_ADMIN))) {
            cardLayout.setVisibility(View.VISIBLE);
            cardLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getIntroTrainings();
                }
            });
        } else {
            cardLayout.setVisibility(View.GONE);
        }
        return view;
    }


    void getIntroTrainings() {

        VolleyStringRequest introTrainingsRequest = new VolleyStringRequest(Request.Method.GET, INTRO_TRAININGS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("introMileDetails", "onResponse: " + response);

                        try {
                            JSONObject introResponse = new JSONObject(response);

                            ArrayList<TMiles> introTrainingsList = new NewDataParser().getMiles(introResponse.getString(KEY_INTRO_TRAININGS));
                            if (introTrainingsList.size() > 0)
                                openMilestonesActivity(introTrainingsList);
                            else
                                Utils.getInstance().showToast("No Intro Trainings");

                        } catch (JSONException ex) {
                            Log.e("introMileDetails", "onResponse: ", ex);
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
                Utils.getInstance().showToast(getResources().getString(R.string.er_no_intro_trainings));
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

        VolleySingleton.getInstance(getContext()).addToRequestQueue(introTrainingsRequest);
    }

    void getUserSections() {
        String sectionsUrl;
        if (new DataBaseUtil(getContext()).getUser().getType().equals(TYPE_TEACHER)) {
            sectionsUrl = USER_SECTIONS_URL;
        } else
            sectionsUrl = SCHOOLS_URL + SharedPreferenceHelper.getSchoolId() + SEPERATOR + KEY_SECTIONS;

        VolleyStringRequest userSectionsRequest = new VolleyStringRequest(Request.Method.GET,
                sectionsUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("userSectionsRequest", "onResponse: " + response);

                        try {
                            JSONObject sectionsResponse = new JSONObject(response);
                            NewDataHolder.getInstance(getContext()).saveUserSections(sectionsResponse.getJSONArray(KEY_SECTIONS));
                            updateSections();
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
                Utils.getInstance().showToast(getResources().getString(R.string.er_no_intro_trainings));
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

        VolleySingleton.getInstance(getContext()).addToRequestQueue(userSectionsRequest);
    }

    private void openMilestonesActivity(ArrayList<TMiles> introTrainings) {
        NewDataHolder holder = NewDataHolder.getInstance(getContext());
        final Intent intent = new Intent(getActivity(), MilestonesActivity.class);
        holder.setIntroTrainingsList(introTrainings);
        holder.setMilesDataList(new ArrayList<TMileData>());
        holder.setCurrentClassName("Intro");
        holder.setCurrentSectionName("Trainings");
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("class_name", "Intro");
        intent.putExtra("section_name", "Trainings");
        intent.putExtra("is_intro", true);
        getActivity().startActivity(intent);

    }


    void updateSections() {
        //ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (NewDataHolder.getInstance(getActivity()).getSectionsList() != null) {
            sectionDetails = NewDataHolder.getInstance(getActivity()).getSectionsList();
        }
        //sectionDetails = new ArrayList<>();
        if (sectionDetails != null && sectionDetails.size() > 0) {
            noSectionsLayout.setVisibility(View.GONE);
            sectionsGrid.setAdapter(new SectionsAdapter(getContext(), sectionDetails, 3, new TeacherOrSectionListener() {
                @Override
                public void onAddOptionSelected(boolean isTeacher) {

                }

                @Override
                public void onEditOptionSelected(boolean isTeacher, int position) {
                    openAddSectionsFragment(true, position);
                }

                @Override
                public void onDeleteOptionSelected(boolean isTeacher, int position) {

                }
            }, false));
        } else {
            noSectionsLayout.setVisibility(View.VISIBLE);
            sectionsGrid.setVisibility(View.GONE);
        }
    }

    final void openAddSectionsFragment(boolean isUpdate, int position) {
        AddSectionsFragment bottomSheetDialogFragment = AddSectionsFragment.getNewInstance(isUpdate, position, new AddOrUpdateListener() {
            @Override
            public void onFinish(boolean isTeacher) {
                sectionsGrid.getAdapter().notifyDataSetChanged();
            }
        });
        bottomSheetDialogFragment.show(getChildFragmentManager(), bottomSheetDialogFragment.getTag());
    }
}