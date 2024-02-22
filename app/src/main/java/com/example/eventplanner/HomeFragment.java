package com.example.eventplanner;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

/**
 * Represents the fragment for the home page of the app, where users can browse events.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private RecyclerView eventsRecyclerView;

    private RecyclerView allEventsRecyclerView;

    private ArrayList<Event> signedUpEventsList;
    private ArrayList<Event> allEventsList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // inflate the layout for the home fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //getting db instance, getting events ref
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");

        // get recycler view for events and then set layout manager
        eventsRecyclerView = view.findViewById(R.id.signedup_events_recyclerView);
        allEventsRecyclerView = view.findViewById(R.id.all_events_recyclerView);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        allEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // create a date object
        Date date = new Date();

        // creating arrayList
        signedUpEventsList = new ArrayList<>();
        allEventsList = new ArrayList<>();



        // create and add an event to the eventsList
//        allEventsList.add(new Event("A", date));
//        allEventsList.add(new Event("B", date));
//        allEventsList.add(new Event("C", date));
//        allEventsList.add(new Event("D", date));
//        allEventsList.add(new Event("E", date));
//        allEventsList.add(new Event("F", date));

        //creating adapter for recycler view and setting the adapter
        EventsRecyclerAdapter eventsRecyclerAdapter = new EventsRecyclerAdapter(getActivity(), signedUpEventsList);
        EventsRecyclerAdapter allEventsRecyclerAdapter = new EventsRecyclerAdapter(getActivity(),allEventsList);
        eventsRecyclerView.setAdapter(eventsRecyclerAdapter);
        allEventsRecyclerView.setAdapter(allEventsRecyclerAdapter);

        //getting all the events
        eventsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Convert each document to Event object and add to allEventsList
                        String eventName = document.getString("Name");
                        Date eventDate = document.getDate("Date");
                        allEventsList.add(new Event(eventName, eventDate));
                    }
                    // Notify adapter that data set has changed
                    allEventsRecyclerAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        return view;

    }
}
