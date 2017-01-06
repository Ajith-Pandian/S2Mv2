package com.example.uilayer.landing.message;


import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.domainlayer.models.Ticket;
import com.example.uilayer.R;
import com.example.uilayer.customUtils.HorizontalSpaceItemDecoration;
import com.example.uilayer.customUtils.VerticalSpaceItemDecoration;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.FB_CHILD_TICKET;
import static com.example.domainlayer.Constants.FB_CHILD_TICKET_DETAILS;


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
            ArrayList<Ticket> tickets = new ArrayList<>();
            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                Ticket value = childSnapshot.getValue(Ticket.class);
                tickets.add(value);
            }
            ticketsRecycler.setAdapter(new TicketsAdapter(getActivity(), tickets));
        }

        @Override
        public void onCancelled(DatabaseError error) {
            // Failed to read value
            Log.w("FB", "Failed to read value.", error.toException());
        }
    };

    public TicketsFragment() {

    }

    @TargetApi(17)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages,
                container, false);
        ButterKnife.bind(this, view);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        ticketsRecycler.setLayoutManager(layoutManager);
        ticketsRecycler.addItemDecoration(new VerticalSpaceItemDecoration(5, 1, true));
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBottomSheet(true);
            }
        });

        //ticketsRecycler.setAdapter(new TicketsAdapter(getActivity(), getTickets()));
        //ticketsRecycler.addItemDecoration(new HorizontalSpaceItemDecoration(getActivity(), 3, 3, 3));
        //Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user_placeholder);
        // imageViewIntroductory.setImageDrawable(Utils.getInstance().getCirclularImage(getActivity(), imageBitmap));
        fetchTickets();

        return view;
    }

    void fetchTickets() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ticketDatabaseReference = database.getReference(FB_CHILD_TICKET_DETAILS);
        ticketDatabaseReference.addValueEventListener(ticketsAddValueListener);
    }

    ArrayList<Ticket> getTickets() {
        ArrayList<Ticket> tickets = new ArrayList<>();
        Ticket ticket1 = new Ticket();
        Ticket ticket2 = new Ticket();
        ticket1.setId(1);
        ticket1.setNumber(123456);
        ticket1.setUserName("Michel Jackson");
        ticket1.setCategory("CONTENT");
        ticket1.setDate("22.10.2001");
        ticket1.setContent(getActivity().getResources().getString(R.string.school_msg_one));
        ticket1.setStatus("OPEN");
        ticket1.setProfileUrl("http://a1.files.biography.com/image/upload/c_fill,cs_srgb,dpr_1.0,g_face,h_300,q_80,w_300/MTE4MDAzNDEwNTEzMDA0MDQ2.jpg");

        ticket2.setId(2);
        ticket2.setNumber(123456);
        ticket2.setUserName("Michel Jackson");
        ticket2.setCategory("CONTENT");
        ticket2.setDate("22.10.2001");
        ticket2.setStatus("CLOSED");
        ticket2.setProfileUrl("http://a1.files.biography.com/image/upload/c_fill,cs_srgb,dpr_1.0,g_face,h_300,q_80,w_300/MTE4MDAzNDEwNTEzMDA0MDQ2.jpg");
        ticket2.setContent(getActivity().getResources().getString(R.string.school_msg_two) + getActivity().getResources().getString(R.string.school_msg_two));
        tickets.add(ticket1);
        tickets.add(ticket1);
        tickets.add(ticket1);
        tickets.add(ticket1);
        tickets.add(ticket2);
        tickets.add(ticket1);
        tickets.add(ticket1);
        tickets.add(ticket2);
        tickets.add(ticket1);
        tickets.add(ticket2);

        return tickets;
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

        ticketDatabaseReference.removeEventListener(ticketsAddValueListener);
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}