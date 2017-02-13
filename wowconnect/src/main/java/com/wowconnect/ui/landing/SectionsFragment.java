package com.wowconnect.ui.landing;


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

import com.wowconnect.NetworkHelper;
import com.wowconnect.NewDataHolder;
import com.wowconnect.NewDataParser;
import com.wowconnect.R;
import com.wowconnect.SharedPreferenceHelper;
import com.wowconnect.domain.Constants;
import com.wowconnect.domain.database.DataBaseUtil;
import com.wowconnect.domain.network.VolleySingleton;
import com.wowconnect.models.Sections;
import com.wowconnect.models.milestones.TMileData;
import com.wowconnect.models.milestones.TMiles;
import com.wowconnect.ui.adapters.SectionsAdapter;
import com.wowconnect.ui.customUtils.HorizontalSpaceItemDecoration;
import com.wowconnect.ui.customUtils.Utils;
import com.wowconnect.ui.customUtils.VolleyStringRequest;
import com.wowconnect.ui.manage.AddOrUpdateListener;
import com.wowconnect.ui.manage.AddSectionsFragment;
import com.wowconnect.ui.manage.TeacherOrSectionListener;
import com.wowconnect.ui.milestones.MilestonesActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;



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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String userType = new DataBaseUtil(getContext()).getUser(SharedPreferenceHelper.getUserId()).getType();
        if (!userType.equals(Constants.USER_TYPE_S2M_ADMIN) &&
                new NewDataParser().getUserRoles(getContext(),
                        SharedPreferenceHelper.getUserId()).contains(Constants.ROLE_TEACHER)) {
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
    }

    void getIntroTrainings() {

        VolleyStringRequest introTrainingsRequest = new VolleyStringRequest(Request.Method.GET, Constants.INTRO_TRAININGS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("introMileDetails", "onResponse: " + response);

                        try {
                            JSONObject introResponse = new JSONObject(response);

                            ArrayList<TMiles> introTrainingsList =
                                    new NewDataParser().getMiles(introResponse.getString(Constants.KEY_INTRO_TRAININGS), false);
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
        if (NewDataHolder.getInstance(getActivity()).getSectionsList() != null &&
                NewDataHolder.getInstance(getActivity()).getSectionsList().size() > 0)
            updateSections();
        else
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