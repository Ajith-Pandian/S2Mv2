package com.example.uilayer.landing.message;


import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.domainlayer.models.Ticket;
import com.example.uilayer.R;
import com.example.uilayer.customUtils.HorizontalSpaceItemDecoration;
import com.example.uilayer.customUtils.VerticalSpaceItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by thoughtchimp on 11/7/2016.
 */

public class TicketsFragment extends Fragment {
    final String OPEN = "open", CLOSED = "Closed", SOLVED = "SOLVED";
    @BindView(R.id.recycler_tickets)
    RecyclerView ticketsRecycler;
    @BindView(R.id.button_start)
    Button startButton;

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
        ticketsRecycler.addItemDecoration(new VerticalSpaceItemDecoration(5,3));
        ticketsRecycler.setAdapter(new TicketsAdapter(getActivity(), getTickets()));
        //ticketsRecycler.addItemDecoration(new HorizontalSpaceItemDecoration(getActivity(), 3, 3, 3));
        //  Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user_placeholder);
        // imageViewIntroductory.setImageDrawable(Utils.getInstance().getCirclularImage(getActivity(), imageBitmap));
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBottomSheet(true);
            }
        });
        return view;
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
        ticket1.setStatus("open");
        ticket1.setProfileUrl("http://a1.files.biography.com/image/upload/c_fill,cs_srgb,dpr_1.0,g_face,h_300,q_80,w_300/MTE4MDAzNDEwNTEzMDA0MDQ2.jpg");

        ticket2.setId(2);
        ticket2.setNumber(123456);
        ticket2.setUserName("Michel Jackson");
        ticket2.setCategory("CONTENT");
        ticket2.setDate("22.10.2001");
        ticket2.setStatus("closed");
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

    void openBottomSheet(boolean isS2m) {
        CreateTicketFragment bottomSheetDialogFragment = CreateTicketFragment.getNewInstance(isS2m);
        bottomSheetDialogFragment.show(getChildFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}