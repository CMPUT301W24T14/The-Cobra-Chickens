package com.example.eventplanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private SearchView allEventsSearchBar;
    private Boolean isListFiltered = false;
    private ArrayList<Event> filteredEvents;

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

        // initialize search bar
        allEventsSearchBar = view.findViewById(R.id.all_events_search_view);
        allEventsSearchBar.clearFocus(); // do this so cursor doesn't start in the search bar in lower APIs

        // listen for when the user enters something in the search bar and filter
        allEventsSearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String searchInput) {

                filterRecyclerView(searchInput);
                return true;
            }
        });

        return view;
    }

    /**
     * Displays all events shown in the database.
     * This method is invoked once when this fragment is created, and every time the user returns
     * to it after leaving it (either to another fragment or a different activity).
     * Ensures that the UI is refreshed whenever the fragment becomes visible again.
     */
    @Override
    public void onResume() {
        super.onResume();
        displayAllEvents();
    }

    /**
     * Sets the event RecyclerAdapter to display events from a filtered list based on if an event's
     * name or location contains the string in searchInput.
     * @param searchInput The String the user inputs into the search bar and queries
     */
    private void filterRecyclerView(String searchInput) {

        filteredEvents = new ArrayList<>();

        String lowerCaseSearchInput = searchInput.toLowerCase();

        for (Event event : allEventsList) {
            // if the event name or location contains the string the user input
            if (event.getEventName().toLowerCase().contains(lowerCaseSearchInput)
                    || event.getEventLocation().toLowerCase().contains(lowerCaseSearchInput)) {
                filteredEvents.add(event);
            }
        }

        if (filteredEvents.isEmpty()) {
            Toast.makeText(getContext(), "No matches found", Toast.LENGTH_SHORT).show();
        }

        allEventsRecyclerAdapter.setFilteredList(filteredEvents);
        allEventsRecyclerAdapter.notifyDataSetChanged();

        isListFiltered = true;
    }

    /**
     * Retrieves all events from the events collection in the database and populates allEventsList
     * with them.
     */
    private void displayAllEvents() {

        // clear list first to ensure no event duplication in the RecyclerView
        allEventsList.clear();

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

                                HashMap<String, String> checkedInUsersFromDB = (HashMap<String, String>) doc.get("checkedInUsers");

                                assert checkedInUsersFromDB != null;
                                ArrayList<CheckedInUser> checkedInUsers = convertCheckedInUsersMapToArrayList(checkedInUsersFromDB);

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
     * Converts a HashMap of checked-in users from the database into an ArrayList of CheckedInUser objects.
     * An each entry in the HashMap has key: userId, value: number of time's that user has checked in.
     * @param checkedInUsersFromDB The HashMap with user IDs that are mapped to the respective number of check-ins.
     * @return An ArrayList of CheckedInUser objects representing the checked-in users.
     */
    private ArrayList<CheckedInUser> convertCheckedInUsersMapToArrayList(HashMap<String, String> checkedInUsersFromDB) {

        ArrayList<CheckedInUser> checkedInUsers = new ArrayList<>();

        for (Map.Entry<String, String> entry : checkedInUsersFromDB.entrySet()) {

            String userId = entry.getKey();
            String numberOfCheckins = entry.getValue();
            checkedInUsers.add(new CheckedInUser(userId, numberOfCheckins));
        }

        return checkedInUsers;
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

        if (!isListFiltered) { // if user clicks on an event normally
            // pass parcelable Event object (from whatever was clicked) to EventDetailsActivity
            intent.putExtra("event", allEventsList.get(position));
        }
        else { // if user clicks on an event in a filtered list
            intent.putExtra("event", filteredEvents.get(position));
        }

        // pass fragment name to EventDetailsActivity so the sign up / deregister button can be set accordingly
        intent.putExtra("fragment name", getClass().getSimpleName());

        // start the EventDetailsActivity
        startActivity(intent);
    }
}
