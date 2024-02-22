package com.example.eventplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

/**
 * Represents the fragment for the home page of the app, where users can browse events.
 */
public class HomeFragment extends Fragment {

    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private RecyclerView eventsRecyclerView;

    private ArrayList<Event> eventsList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // inflate the layout for the home fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //getting db instance, getting events ref
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");

        // get recycler view for events and then set layout manager
        eventsRecyclerView = view.findViewById(R.id.events_recyclerView);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // create a date object
        Date date = new Date();

        // creating arrayList
        eventsList = new ArrayList<>();

        // create and add an event to the eventsList
        eventsList.add(new Event("A", date));
        eventsList.add(new Event("B", date));
        eventsList.add(new Event("C", date));
        eventsList.add(new Event("D", date));
        eventsList.add(new Event("E", date));
        eventsList.add(new Event("F", date));

        //creating adapter for recycler view and setting the adapter
        EventsRecyclerAdapter eventsRecyclerAdapter = new EventsRecyclerAdapter(getActivity(), eventsList);
        eventsRecyclerView.setAdapter(eventsRecyclerAdapter);

        return view;

    }
}
