package com.example.eventplanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

/**
 * OrganizeEventsFragment is a Fragment that handles showing a user all events that they are
 * currently organizing.
 */
public class OrganizeEventsFragment extends Fragment implements RecyclerViewInterface {

    private FirebaseFirestore db; // the database
    private RecyclerView organizeEventsRecyclerView; // RecyclerView list of the events user is organizing
    private ArrayList<Event> organizeEventsList; // ArrayList that holds all events that the user is organizing
    String userId;
    private Button createEventButton; // Button that takes you to CreateEventActivity

    /**
     * Creates the view for OrganizeEventsFragment, which is contained within HomeFragmentUpdated
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return the view specific to OrganizeEventsFragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_organize_events, container, false);

        // initialize connection to the database
        db = FirebaseFirestore.getInstance();

        // connect organizeEventsRecyclerView to the actual RecyclerView widget in fragment_organize_events.xml
        organizeEventsRecyclerView = view.findViewById(R.id.organize_events_recyclerView_updated);

        // initialize ArrayList to store events user is organizing in
        organizeEventsList = new ArrayList<>();

        // configure myEventsRecyclerView to have a vertical orientation when arranging items *
        organizeEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // initialize organizeEventsRecyclerAdapter and set it as the adapter for organizeEventsRecyclerView
        EventRecyclerAdapterUpdated organizeEventsRecyclerAdapter = new EventRecyclerAdapterUpdated(getContext(), organizeEventsList, this);
        organizeEventsRecyclerView.setAdapter(organizeEventsRecyclerAdapter);

        // initialize createEventButton
        createEventButton = view.findViewById(R.id.button_add_new_event);

        // handle clicks on createEventButton
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateEventActivity();
            }
        });

        // need to fix --> currently testing with a hardcoded user
        // Set up Firebase Authentication and use what's below to get a userId dynamically
        // FirebaseAuth auth = FirebaseAuth.getInstance();
        // FirebaseUser currentUser = auth.getCurrentUser();
        userId = "et9ykXKsNzo3ETU3Vwwg";

        // read specific document for userId given
        db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        // get all events in user's organizing ArrayList and put them in another ArrayList of eventIds
                        ArrayList<String> eventIds = (ArrayList<String>) documentSnapshot.get("organizing");

                        if (eventIds != null) {
                            loadEventDocs(eventIds, organizeEventsRecyclerAdapter);
                        }

                        // handle if the user's organizing Array is null/empty (user is not organizing any events)
                    }
                });

        return view;
    }

    /**
     * Opens up a CreateEventActivity
     */
    public void openCreateEventActivity() {

        // set up a new intent to jump from the current activity to CreateEventActivity
        Intent intent = new Intent(getActivity(), CreateEventActivity.class);

        // start a CreateEventActivity
        startActivity(intent);
    }

    /**
     * Adds all events from user's organizing Array in the database to organizeEventsList
     * TODO: Display list of attendees that are signed up for the event*
     * @param eventIds  The ArrayList of eventIds that are being organized by the user
     * @param organizingEventsRecyclerAdapter The Adapter for organizeEventsRecyclerView
     */
    private void loadEventDocs(ArrayList<String> eventIds, EventRecyclerAdapterUpdated organizingEventsRecyclerAdapter) {

        for (String eventId : eventIds) { // for every eventId in user's organizing Array
            db.collection("events")
                    .document(eventId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            // retrieve all event information associated with the event
                            String eventId = documentSnapshot.getId();
                            String eventName = documentSnapshot.getString("Name");
                            Date eventDate = documentSnapshot.getDate("Date");
                            String eventLocation = documentSnapshot.getString("Location");
                            String eventPoster = documentSnapshot.getString("Poster");
                            ArrayList<String> eventAnnouncements = (ArrayList<String>) documentSnapshot.get("Announcements");

                            // create Event object with retrieved event information and add it to organizeEventsList
                            organizeEventsList.add(new Event(eventId, eventName, eventDate, eventLocation, eventPoster, eventAnnouncements));

                            // tell organizeEventsRecyclerView that the dataset that organizingEventsRecyclerAdapter is responsible for has changed
                            organizingEventsRecyclerAdapter.notifyDataSetChanged();
                        }
                    });

        }
    }

    /**
     * Handles clicks on Event items in organizeEventsRecyclerView.
     * When an Event item is clicked, it takes the user to EventDetailsActivity where that
     * event's information is displayed.
     * @param position The position of the Event item in organizingEventsRecyclerAdapter's dataset
     *                 that was clicked.
     */
    @Override
    public void onItemClick(int position) {

        // set up a new intent to jump from the current activity to EventDetailsActivity
        Intent intent = new Intent(getActivity(), EventDetailsActivity.class);

        // pass parcelable Event object (from whatever was clicked) to EventDetailsActivity
        intent.putExtra("event", organizeEventsList.get(position));

        // start the EventDetailsActivity
        startActivity(intent);
    }
}