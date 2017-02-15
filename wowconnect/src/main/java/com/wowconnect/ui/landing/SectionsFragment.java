package com.wowconnect.ui.landing;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wowconnect.NetworkHelper;
import com.wowconnect.NewDataHolder;
import com.wowconnect.R;
import com.wowconnect.SharedPreferenceHelper;
import com.wowconnect.UserAccessController;
import com.wowconnect.domain.database.DataBaseUtil;
import com.wowconnect.models.Sections;
import com.wowconnect.models.milestones.TMileData;
import com.wowconnect.models.milestones.TMiles;
import com.wowconnect.ui.adapters.SectionsAdapter;
import com.wowconnect.ui.customUtils.HorizontalSpaceItemDecoration;
import com.wowconnect.ui.customUtils.Utils;
import com.wowconnect.ui.helpers.AlertDialogListener;
import com.wowconnect.ui.helpers.DialogHelper;
import com.wowconnect.ui.manage.AddOrUpdateListener;
import com.wowconnect.ui.manage.AddSectionsFragment;
import com.wowconnect.ui.manage.TeacherOrSectionListener;
import com.wowconnect.ui.milestones.MilestonesActivity;

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
        sectionsGrid.setNestedScrollingEnabled(false);

        getUserSections();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        UserAccessController userAccessController = new UserAccessController(SharedPreferenceHelper.getUserId());
        setHasOptionsMenu(userAccessController.hasAddSectionMenu());
        if (userAccessController.hasIntroTrainings()) {
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
    }

    void getIntroTrainings() {
        NewDataHolder holder = NewDataHolder.getInstance(getContext());
        ArrayList<TMiles> introTrainingsList = holder.getIntroTrainingsList();
        if (introTrainingsList != null && introTrainingsList.size() > 0)
            openMilestonesActivity();
        else
            Utils.getInstance().showToast("No Intro Trainings");
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

    private void openMilestonesActivity() {
        NewDataHolder holder = NewDataHolder.getInstance(getContext());
        final Intent intent = new Intent(getActivity(), MilestonesActivity.class);
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
                public void onDeleteOptionSelected(boolean isTeacher, final int position) {
                    DialogHelper.createAlertDialog(getActivity(),
                            getString(R.string.alert_delete_section),
                            getString(R.string.yes),
                            getString(R.string.no),
                            new AlertDialogListener() {
                                @Override
                                public void onPositive() {
                                    deleteSection(position);

                                }

                                @Override
                                public void onNegative() {
                                    DialogHelper.getCurrentDialog().dismiss();
                                }
                            }
                    );

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