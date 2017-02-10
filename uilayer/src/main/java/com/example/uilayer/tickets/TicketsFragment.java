package com.example.uilayer.tickets;


import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.domainlayer.models.Ticket;

import com.example.uilayer.NewDataHolder;
import com.example.uilayer.R;
import com.example.uilayer.customUtils.VerticalSpaceItemDecoration;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.FB_CHILD_TICKET_DETAILS;
import static com.example.domainlayer.Constants.USER_TYPE_S2M_ADMIN;


/**
 * Created by thoughtchimp on 11/7/2016.
 */

public class TicketsFragment extends Fragment {
    @BindView(R.id.recycler_tickets)
    RecyclerView ticketsRecycler;
    @BindView(R.id.button_start)
    Button startButton;
    CreateTicketFragment bottomSheetDialogFragment;
    DatabaseReference ticketDatabaseReference;
    ValueEventListener ticketsAddValueListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            /*ArrayList<Ticket> tickets = new ArrayList<>();
            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                Ticket value = childSnapshot.getValue(Ticket.class);
                value.setId(childSnapshot.getKey());
                if (isUserValid(value.getUserIds()))
                    tickets.add(value);
            }
            ticketsRecycler.setAdapter(new TicketsAdapter(getActivity(), tickets));*/
        }

        @Override
        public void onCancelled(DatabaseError error) {
            // Failed to read value
            Log.w("FB", "Failed to read value.", error.toException());
        }
    };

    public TicketsFragment() {

    }

    boolean isUserValid(Map<String, Boolean> userIds) {
       /* List<Integer> userIdsArray = new ArrayList<>();
        for (int userId : userIds.containsKey()) userIdsArray.add(userId);*/
        return userIds.containsKey(String.valueOf(NewDataHolder.getInstance(getContext()).getUser().getId()));
    }

    @TargetApi(17)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tickets,
                container, false);
        ButterKnife.bind(this, view);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        ticketsRecycler.setLayoutManager(layoutManager);
        ticketsRecycler.addItemDecoration(new VerticalSpaceItemDecoration(5, 1, true));
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isS2m = NewDataHolder.getInstance(getContext()).getUser().getType().equals(USER_TYPE_S2M_ADMIN);
                // openBottomSheet(isS2m);
            }
        });

        //  fetchTickets();

        return view;
    }


    void fetchTickets() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ticketDatabaseReference = database.getReference("firebaseexample")
                .child(String.valueOf(NewDataHolder.getInstance(getContext()).getUser().getSchoolId()))
                .child(FB_CHILD_TICKET_DETAILS);
        ticketDatabaseReference.addValueEventListener(ticketsAddValueListener);
    }

    final void openBottomSheet(boolean isS2m) {
        bottomSheetDialogFragment = CreateTicketFragment.getNewInstance(isS2m);
        bottomSheetDialogFragment.show(getChildFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    @Override
    public void onDestroy() {

        if (bottomSheetDialogFragment != null) {
            bottomSheetDialogFragment = null;
        }
        if (ticketDatabaseReference != null) {

            ticketDatabaseReference.removeEventListener(ticketsAddValueListener);
        }
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}