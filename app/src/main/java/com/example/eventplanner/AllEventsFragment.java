package com.example.eventplanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

/**
 * AllEventsFragment is a Fragment that handles showing a user all events that are on the application.
 * A user has the ability to interact with events by clicking on them, and can search events using
 * the search bar (not yet implemented).
 */
public class AllEventsFragment extends Fragment implements RecyclerViewInterface {

    private FirebaseFirestore db; // the database
    private CollectionReference eventsCollectionReference; // reference to events collection in db
    private RecyclerView allEventsRecyclerView; // RecyclerView list of all events
    private ArrayList<Event> allEventsList; // ArrayList that holds all events
    private EventRecyclerAdapterUpdated allEventsRecyclerAdapter; // EventRecyclerAdapter for allEventsRecyclerView

    /**
     * Creates the view for AllEventsFragment, which is contained within HomeFragmentUpdated.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return The view specific to AllEventsFragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_events, container, false);

        // initialize connection to the databse
        db = FirebaseFirestore.getInstance();

        // reference the "events" collection in the database
        eventsCollectionReference = db.collection("events");

        // connect allEventsRecyclerView to the actual RecyclerView widget in fragment_all_events.xml
        allEventsRecyclerView = view.findViewById(R.id.all_events_recyclerView_updated);

        // initialize ArrayList to store all events in
        allEventsList = new ArrayList<>();

        // configure allEventsRecyclerView to have a vertical orientation when arranging items *
        allEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // initialize allEventsRecyclerAdapter and set it as the adapter for allEventsRecyclerView
        allEventsRecyclerAdapter = new EventRecyclerAdapterUpdated(getContext(), allEventsList, this);
        allEventsRecyclerView.setAdapter(allEventsRecyclerAdapter);

        displayAllEvents();

        return view;
    }

    /**
     * Retrieves all events from the events collection in the database and populates allEventsList
     * with them.
     */
    private void displayAllEvents() {

        // retrieve documents from events collection in the database
        eventsCollectionReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        // if the documents from the events collection were not successfully retrieved
                        if (!task.isSuccessful()) {
                            Log.e("Firestore", task.getException().toString());

                        }
                        else {
                            for (DocumentSnapshot doc : task.getResult()) {

                                // retrieve all event information associated with the event
                                String eventId = doc.getId();
                                String eventName = doc.getString("eventName");
                                String eventDescription = doc.getString("eventDescription");
                                String eventMaxAttendees = doc.getString("eventMaxAttendees");
                                String eventDate = doc.getString("eventDate");
                                String eventTime = doc.getString("eventTime");
                                String eventLocation = doc.getString("eventLocation");
                                String eventPoster = doc.getString("eventPoster");

                                String checkInCode = doc.getString("checkInCode");
                                String promoCode = doc.getString("promoCode");

                                ArrayList<String> eventAnnouncements = (ArrayList<String>) doc.get("eventAnnouncements");
                                ArrayList<String> checkedInUsers = (ArrayList<String>) doc.get("checkedInUsers");
                                ArrayList<String> signedUpUsers = (ArrayList<String>) doc.get("signedUpUsers");

                                // create the Event object with retrieved event information and add it to allEventsList
                                allEventsList.add(new Event(eventId, eventName, eventDescription, eventMaxAttendees, eventDate, eventTime, eventLocation, eventPoster, checkInCode, promoCode, eventAnnouncements, checkedInUsers, signedUpUsers));
                            }

                            // tell allEventsRecyclerView that the dataset that allEventsRecyclerAdapter is responsible for has changed
                            allEventsRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    /**
     * Handles clicks on Event items in allEventsRecyclerView.
     * When an Event item is clicked, it takes the user to EventDetailsActivity where that
     * event's information is displayed.
     * @param position The position of the Event item in allEventsRecyclerAdapter's dataset
     *                 that was clicked.
     */
    @Override
    public void onItemClick(int position) {

        // set up a new intent to jump from the current activity to EventDetailsActivity
        Intent intent = new Intent(getActivity(), EventDetailsActivity.class);

        // pass parcelable Event object (from whatever was clicked) to EventDetailsActivity
        intent.putExtra("event", allEventsList.get(position));

        // start the EventDetailsActivity
        startActivity(intent);
    }
}
