package com.example.eventplanner;

import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * MyEventsFragment is a Fragment that handles showing a user all events that they have signed up
 * for.
 * A user has the ability to interact with events by clicking on them, and can search events using
 * the search bar (not yet implemented).
 */
public class MyEventsFragment extends Fragment implements RecyclerViewInterface {

    private FirebaseFirestore db; // the database
    private RecyclerView myEventsRecyclerView; // RecyclerView list of user's singed-up-for events
    private ArrayList<Event> myEventsList; // ArrayList that holds all events that the user has signed up for
    private EventRecyclerAdapterUpdated myEventsRecyclerAdapter; // EventRecyclerAdapter for myEventsRecyclerView
    private CollectionReference userRef;
    private SharedViewModel sharedViewModel;
    private SearchView myEventsSearchBar;

    /**
     * Creates the view for MyEventsFragment, which is contained within HomeFragmentUpdated
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return The view specific to MyEventsFragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_events, container, false);

        // initialize connection to the database
        db = FirebaseFirestore.getInstance();

        // connect myEventsRecyclerView to the actual RecyclerView widget in fragment_my_events.xml
        myEventsRecyclerView = view.findViewById(R.id.my_events_recyclerView_updated);

        // initialize ArrayList to store user's events in
        myEventsList = new ArrayList<>();

        // configure myEventsRecyclerView to have a vertical orientation when arranging items *
        myEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // initialize myEventsRecyclerAdapter and set it as the adapter for myEventsRecyclerView
        myEventsRecyclerAdapter = new EventRecyclerAdapterUpdated(getContext(), myEventsList, this);
        myEventsRecyclerView.setAdapter(myEventsRecyclerAdapter);

        // initialize search bar
        myEventsSearchBar = view.findViewById(R.id.my_events_search_view);
        myEventsSearchBar.clearFocus(); // do this so cursor doesn't start in the search bar in lower APIs

        // listen for when the user enters something in the search bar and filter
        myEventsSearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
     * Displays the events a user has signed up for.
     * This method is invoked once when this fragment is created, and every time the user returns
     * to it after leaving it (either to another fragment or a different activity).
     * Ensures that the UI is refreshed whenever the fragment becomes visible again.
     */
    @Override
    public void onResume() {
        super.onResume();
        getMyEvents();
    }

    /**
     * Sets the event RecyclerAdapter to display events from a filtered list based on if an event's
     * name or location contains the string in searchInput.
     * @param searchInput The String the user inputs into the search bar and queries
     */
    private void filterRecyclerView(String searchInput) {

        ArrayList<Event> filteredEvents = new ArrayList<>();

        String lowerCaseSearchInput = searchInput.toLowerCase();

        for (Event event : myEventsList) {
            // if the event name or location contains the string the user input
            if (event.getEventName().toLowerCase().contains(lowerCaseSearchInput)
                    || event.getEventLocation().toLowerCase().contains(lowerCaseSearchInput)) {
                filteredEvents.add(event);
            }
        }

        if (filteredEvents.isEmpty()) {
            Toast.makeText(getContext(), "No matches found", Toast.LENGTH_SHORT).show();
        }

        myEventsRecyclerAdapter.setFilteredList(filteredEvents);
    }

    /**
     * Retrieves all events from user's myEvents list in the database and populates myEventsList
     * with them.
     */
    private void getMyEvents() {

        // clear list first to ensure no event duplication in the RecyclerView
        myEventsList.clear();

        // read specific document for userId given
        db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        // get all events in user's myEvents ArrayList and put them in another ArrayList of eventIds
                        ArrayList<String> eventIds = (ArrayList<String>) documentSnapshot.get("myEvents");

                        if (eventIds != null) {
                            loadEventDocs(eventIds, myEventsRecyclerAdapter);
                        }
                    }
                });
    }

    /**
     * Adds all events from user's myEvents Array in the database to myEventsList
     * @param eventIds The ArrayList of eventIds from user's myEvents ArrayList
     * @param myEventsRecyclerAdapter The Adapter for myEventsRecyclerView
     */
    private void loadEventDocs(ArrayList<String> eventIds, EventRecyclerAdapterUpdated myEventsRecyclerAdapter) {

        for (String eventId : eventIds) { // for every eventId in user's myEvents Array
            db.collection("events")
                    .document(eventId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            // retrieve all event information associated with the event
                            String eventId = documentSnapshot.getId();
                            String eventName = documentSnapshot.getString("eventName");
                            String eventDescription = documentSnapshot.getString("eventDescription");
                            String eventMaxAttendees = documentSnapshot.getString("eventMaxAttendees");
                            String eventDate = documentSnapshot.getString("eventDate");
                            String eventTime = documentSnapshot.getString("eventTime");
                            String eventLocation = documentSnapshot.getString("eventLocation");
                            String eventPoster = documentSnapshot.getString("eventPoster");

                            String checkInCode = documentSnapshot.getString("checkInCode");
                            String promoCode = documentSnapshot.getString("promoCode");

                            ArrayList<String> eventAnnouncements = (ArrayList<String>) documentSnapshot.get("eventAnnouncements");

                            HashMap<String, String> checkedInUsersFromDB = (HashMap<String, String>) documentSnapshot.get("checkedInUsersTest");

                            assert checkedInUsersFromDB != null;
                            ArrayList<CheckedInUser> checkedInUsers = convertCheckedInUsersMapToArrayList(checkedInUsersFromDB);

                            ArrayList<String> signedUpUsers = (ArrayList<String>) documentSnapshot.get("signedUpUsers");

                            // create Event object with retrieved event information and add it to myEventsList
                            myEventsList.add(new Event(eventId, eventName, eventDescription, eventMaxAttendees, eventDate, eventTime, eventLocation, eventPoster, checkInCode, promoCode, eventAnnouncements, checkedInUsers, signedUpUsers));

                            // tell myEventsRecyclerView that the dataset that myEventsRecyclerAdapter is responsible for has changed
                            myEventsRecyclerAdapter.notifyDataSetChanged();
                        }
                    });

        }
    }

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
     * Handles clicks on Event items in myEventsRecyclerView.
     * When an Event item is clicked, it takes the user to EventDetailsActivity where that
     * event's information is displayed.
     * @param position The position of the Event item in myEventsRecyclerAdapter's dataset
     *                 that was clicked.
     */
    @Override
    public void onItemClick(int position) {

        // set up a new intent to jump from the current activity to EventDetailsActivity
        Intent intent = new Intent(getActivity(), EventDetailsActivity.class);

        // pass parcelable Event object (from whatever was clicked) to EventDetailsActivity
        intent.putExtra("event", myEventsList.get(position));

        // pass fragment name to EventDetailsActivity so the sign up / deregister button can be set accordingly
        intent.putExtra("fragment name", getClass().getSimpleName());

        // start the EventDetailsActivity
        startActivity(intent);
    }
}

