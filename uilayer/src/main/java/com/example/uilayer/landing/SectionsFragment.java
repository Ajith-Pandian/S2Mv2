package com.example.uilayer.landing;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.database.DataBaseUtil;
import com.example.domainlayer.models.Sections;
import com.example.domainlayer.models.milestones.TMileData;
import com.example.domainlayer.models.milestones.TMiles;
import com.example.domainlayer.network.VolleySingleton;
import com.example.uilayer.NetworkHelper;
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
import static com.example.domainlayer.Constants.ROLE_SCL_ADMIN;
import static com.example.domainlayer.Constants.ROLE_TEACHER;
import static com.example.domainlayer.Constants.USER_TYPE_S2M_ADMIN;
import static com.example.domainlayer.Constants.TYPE_SCL_ADMIN;


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
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        sectionsGrid.setLayoutManager(layoutManager);
        sectionsGrid.addItemDecoration(new HorizontalSpaceItemDecoration(getActivity(), 3, 3, 2));
        getUserSections();
        String userType = new DataBaseUtil(getContext()).getUser(SharedPreferenceHelper.getUserId()).getType();
        if (!userType.equals(USER_TYPE_S2M_ADMIN) &&
                SharedPreferenceHelper.getUserRoles().contains(ROLE_TEACHER)) {
            cardLayout.setVisibility(View.VISIBLE);
            cardLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getIntroTrainings();
                }
            });
        } else {
            cardLayout.setVisibility(View.GONE);
            setHasOptionsMenu(true);
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

                            ArrayList<TMiles> introTrainingsList = new NewDataParser().getMiles(introResponse.getString(KEY_INTRO_TRAININGS), false);
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
        new NetworkHelper(getContext()).getUserSections(new NetworkHelper.NetworkListener() {
            @Override
            public void onFinish() {
                updateSections();
            }
        });
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
        if (NewDataHolder.getInstance(getActivity()).getSectionsList() != null) {
            sectionDetails = NewDataHolder.getInstance(getActivity()).getSectionsList();
        }
        if (sectionDetails != null && sectionDetails.size() > 0) {
            noSectionsLayout.setVisibility(View.GONE);
            sectionsGrid.setAdapter(new SectionsAdapter(getContext(), sectionDetails, 2, new TeacherOrSectionListener() {
                @Override
                public void onAddOptionSelected(boolean isTeacher) {

                }

                @Override
                public void onEditOptionSelected(boolean isTeacher, int position) {
                    openAddSectionsFragment(true, position);
                }

                @Override
                public void onDeleteOptionSelected(boolean isTeacher, int position) {
                    deleteSection(position);
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
                updateSections();
                sectionsGrid.getAdapter().notifyDataSetChanged();
            }
        });
        bottomSheetDialogFragment.show(getChildFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    void deleteSection(final int position) {
        new NetworkHelper(getContext()).deleteSection(
                NewDataHolder.getInstance(getContext()).getSectionsList().get(position).getId(),
                new NetworkHelper.NetworkListener() {
                    @Override
                    public void onFinish() {
                        new NetworkHelper(getContext()).getUserSections(new NetworkHelper.NetworkListener() {
                            @Override
                            public void onFinish() {
                                Utils.getInstance().showToast("Section Deleted");
                                updateSections();
                                sectionsGrid.getAdapter().notifyDataSetChanged();
                            }
                        });
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.menu_add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                openAddSectionsFragment(false, -1);
                break;
        }
        return true;

    }

}