package com.example.eventplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * The displays an activity that shows an organizer details of an event they are managing.
 */
public class OrganizerEventViewActivity extends AppCompatActivity {

    private ArrayList<String> announcementsList;
    private ArrayList<User> signedUpList;
    private ArrayList<User> checkedInList;
    private ImageView eventPoster;
    private FirebaseFirestore db; // the database
    private Bundle bundle;
    private RecyclerViewInterface recyclerViewInterface;
    private Event currEvent;
    private UserRecyclerAdapter signedUserRecyclerAdapter;
    private UserRecyclerAdapter checkedInUserRecyclerAdapter;
    private AnnouncementsRecyclerAdapter announcementsRecyclerAdapter;
    private RecyclerView announcementsRecyclerView;
    private RecyclerView signedUpRecyclerView;
    private RecyclerView checkedInRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_event_view);

        db = FirebaseFirestore.getInstance();

        TextView eventNameTextView = findViewById(R.id.event_name2);
        TextView eventDateTextView = findViewById(R.id.event_date2);
        TextView eventTimeTextView = findViewById(R.id.event_time2);

        announcementsRecyclerView = findViewById(R.id.announcements_recyclerView2);
        signedUpRecyclerView = findViewById(R.id.signedUp_recyclerView);
        checkedInRecyclerView = findViewById(R.id.checkedIn_recyclerView);

        announcementsList = new ArrayList<>();
        signedUpList = new ArrayList<>();
        checkedInList = new ArrayList<>();


        ImageView poster = findViewById(R.id.poster2);

        bundle = getIntent().getExtras();

        Button backButton = findViewById(R.id.button_back_2);

        backButton.setOnClickListener(view -> finish());

        bundle = getIntent().getExtras();


        if (bundle != null && bundle.containsKey("event")) {
            currEvent = bundle.getParcelable("event");
            if (currEvent != null) {

                // Set event details to views
                eventNameTextView.setText("Name: " + currEvent.getEventName());
                eventDateTextView.setText("Date: " + currEvent.getEventDate());
                eventTimeTextView.setText("Time: " + currEvent.getEventTime());

                if (currEvent.getEventPoster() != null && !currEvent.getEventPoster().isEmpty()) {
                    Glide.with(this)
                            .load(currEvent.getEventPoster())
                            .into(poster);
                } else {
                    poster.setVisibility(View.GONE);
                }

                if (currEvent.getEventAnnouncements() != null && !currEvent.getEventAnnouncements().isEmpty()) {
                    announcementsList = currEvent.getEventAnnouncements();
                }

                announcementsRecyclerAdapter = new AnnouncementsRecyclerAdapter(this, announcementsList);
                announcementsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                announcementsRecyclerView.setAdapter(announcementsRecyclerAdapter);

            }

            signedUserRecyclerAdapter = new UserRecyclerAdapter(this, signedUpList, recyclerViewInterface);
            signedUpRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            signedUpRecyclerView.setAdapter(signedUserRecyclerAdapter);

            checkedInUserRecyclerAdapter = new UserRecyclerAdapter(this, checkedInList, recyclerViewInterface);
            checkedInRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            checkedInRecyclerView.setAdapter(checkedInUserRecyclerAdapter);


        }

        getSignedUpUsers();
        getCheckedInUsers();

    }

    private void getSignedUpUsers() {

        db.collection("events")
                .document(currEvent.getEventId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        // get all events in user's organizing ArrayList and put them in another ArrayList of eventIds
                        ArrayList<String> signedUpUserIds = (ArrayList<String>) documentSnapshot.get("signedUpUsers");

                        if (signedUpUserIds != null) {
                            loadSignedUpUserDocs(signedUpUserIds, signedUserRecyclerAdapter);
                        }
                    }
                });
    }

    private void getCheckedInUsers() {

        db.collection("events")
                .document(currEvent.getEventId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        // get all events in user's organizing ArrayList and put them in another ArrayList of eventIds
                        ArrayList<String> checkedInUserIds = (ArrayList<String>) documentSnapshot.get("checkedInUsers");

                        if (checkedInUserIds != null) {
                            loadCheckedInUserDocs(checkedInUserIds, checkedInUserRecyclerAdapter);
                        }
                    }
                });
    }

    private void loadSignedUpUserDocs(ArrayList<String> userIds, UserRecyclerAdapter userRecyclerAdapter) {

        for (String userId : userIds) { // for every eventId in user's organizing Array
            db.collection("users")
                    .document(userId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            String userId = documentSnapshot.getId();
                            String name = documentSnapshot.getString("Name");
                            String contact = documentSnapshot.getString("Contact");
                            String homePage = documentSnapshot.getString("Homepage");
                            String profilePicUrl = documentSnapshot.getString("ProfilePic");
                            boolean usrlocation = documentSnapshot.getBoolean("Location");

                            ArrayList<String> checkedInto = (ArrayList<String>) documentSnapshot.get("checkedInto");
                            ArrayList<String> signedUpFor = (ArrayList<String>) documentSnapshot.get("myEvents");
                            ArrayList<String> organizing = (ArrayList<String>) documentSnapshot.get("Organizing");

                            signedUpList.add(new User(userId, name, homePage, contact, profilePicUrl, usrlocation, signedUpFor, checkedInto, organizing));

                            userRecyclerAdapter.notifyDataSetChanged();
                        }
                    });

        }
    }

    private void loadCheckedInUserDocs(ArrayList<String> userIds, UserRecyclerAdapter userRecyclerAdapter) {

        for (String userId : userIds) { // for every eventId in user's organizing Array
            db.collection("users")
                    .document(userId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            String userId = documentSnapshot.getId();
                            String name = documentSnapshot.getString("Name");
                            String contact = documentSnapshot.getString("Contact");
                            String homePage = documentSnapshot.getString("Homepage");
                            String profilePicUrl = documentSnapshot.getString("ProfilePic");
                            boolean usrlocation = documentSnapshot.getBoolean("Location");

                            ArrayList<String> checkedInto = (ArrayList<String>) documentSnapshot.get("checkedInto");
                            ArrayList<String> signedUpFor = (ArrayList<String>) documentSnapshot.get("myEvents");
                            ArrayList<String> organizing = (ArrayList<String>) documentSnapshot.get("Organizing");

                            checkedInList.add(new User(userId, name, homePage, contact, profilePicUrl, usrlocation, signedUpFor, checkedInto, organizing));

                            userRecyclerAdapter.notifyDataSetChanged();
                        }
                    });

        }
    }
}