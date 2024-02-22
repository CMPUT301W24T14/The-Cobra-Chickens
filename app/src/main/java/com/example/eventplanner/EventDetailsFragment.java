package com.example.eventplanner;

import android.media.Image;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class EventDetailsFragment extends Fragment {

    private TextView eventNameTextView, eventDateTextView, eventTimeTextView, eventOrganizerTextView;
    private RecyclerView announcementsRecyclerView;
    private ArrayList<String> announcements = new ArrayList<>();;

    private ImageView poster;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);


        eventNameTextView = view.findViewById(R.id.event_name);
        eventDateTextView = view.findViewById(R.id.event_date);
        eventTimeTextView = view.findViewById(R.id.event_time);
        announcementsRecyclerView = view.findViewById(R.id.announcements_recyclerView);
        poster = view.findViewById(R.id.poster);

        // Set up the toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_black_24);

        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Event Details");

        // Handle back button click
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to previous fragment or activity
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Get event details from arguments
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("event")) {
            Event event = bundle.getParcelable("event");
            if (event != null) {
                // Set event details to views
                eventNameTextView.setText("Name:"+event.getEventName());
                // Assuming you have a method to format the date as string
                eventDateTextView.setText("Date:"+formatDate(event.getEventDate()));
                eventTimeTextView.setText("Time:"+formatTime(event.getEventDate()));

                if (event.getPoster() != null && !event.getPoster().isEmpty()) {
                    Glide.with(requireContext())
                            .load(event.getPoster())
                            .into(poster);
                } else {
                    poster.setVisibility(View.GONE);
                }

                if (event.getAnnouncements()!=null && !event.getAnnouncements().isEmpty()){
                    announcements = event.getAnnouncements();
                }

                AnnouncementsRecyclerAdapter adapter = new AnnouncementsRecyclerAdapter(requireContext(), announcements);
                announcementsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                announcementsRecyclerView.setAdapter(adapter);

            }
        }

        return view;

    }

    private String formatDate(Date eventDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = sdf.format(eventDate);
        return formattedDate;
    }

    private String formatTime(Date eventDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mma");
        String formattedTime = sdf.format(eventDate);
        return formattedTime.toUpperCase();
    }
}