package com.example.eventplanner;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;

/**
 * This Activity displays the details of a certain event to users who are not organizing said event.
 * Users can sign up for events here.
 */
public class EventDetailsActivity extends AppCompatActivity {
    private TextView eventNameTextView, eventDateTextView, eventTimeTextView, eventLocationTextView, eventDescriptionTextView, eventOrganizerTextView;
    private RecyclerView announcementsRecyclerView;
    private ArrayList<String> announcements = new ArrayList<>();;
    private ImageView poster;
    private Button signUpOrDeregisterButton;
    private Bundle bundle;
    private FirebaseFirestore db; // the database

    /**
     * Initializes the activity and connects the it to the UI.
     * Gets event details from the intent.
     * @param savedInstanceState A Bundle that contains the activity's previous state.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        eventNameTextView = findViewById(R.id.event_name);
        eventDateTextView = findViewById(R.id.event_date);
        eventTimeTextView = findViewById(R.id.event_time);
        eventLocationTextView = findViewById(R.id.event_location);
        eventDescriptionTextView = findViewById(R.id.event_description);

        announcementsRecyclerView = findViewById(R.id.announcements_recyclerView);
        poster = findViewById(R.id.poster);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_black_24);
        getSupportActionBar().setTitle("Event Details");

        //Handle back button click
        toolbar.setNavigationOnClickListener(view -> finish());

        // Get event details from intent
        bundle = getIntent().getExtras();


        if (bundle != null && bundle.containsKey("event")) {
            Event event = bundle.getParcelable("event");
            if (event != null) {
                // Set event details to views
                eventNameTextView.setText(String.format("Name: %s", event.getEventName()));
                eventDateTextView.setText(String.format("Date: %s", event.getEventDate()));
                eventTimeTextView.setText(String.format("Time: %s", event.getEventTime()));
                eventLocationTextView.setText(String.format("Location: %s", event.getEventLocation()));

                eventDescriptionTextView.setText(event.getEventDescription());

                if (event.getEventPoster() != null && !event.getEventPoster().isEmpty()) {
                    Glide.with(this)
                            .load(event.getEventPoster())
                            .into(poster);
                } else {
                    poster.setVisibility(View.GONE);
                }

                if (event.getEventAnnouncements() != null && !event.getEventAnnouncements().isEmpty()) {
                    announcements = event.getEventAnnouncements();
                }

                AnnouncementsRecyclerAdapter adapter = new AnnouncementsRecyclerAdapter(this, announcements);
                announcementsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                announcementsRecyclerView.setAdapter(adapter);

            }
        }

        db = FirebaseFirestore.getInstance();

        signUpOrDeregisterButton = findViewById(R.id.button_signup_or_deregister);

        Event event = bundle.getParcelable("event");
        if (event != null) {

            ArrayList<String> signedUpUsers = event.getSignedUpUsers();
            ArrayList<CheckedInUser> checkedInUsers = event.getCheckedInUsers();

            boolean alreadySignedUp = false;
            boolean alreadyCheckedIn = false;

            if (signedUpUsers != null) {
                for (String userId : signedUpUsers) {
                    if (signedUpUsers.contains(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        alreadySignedUp = true;
                    }
                }
            }

            if (checkedInUsers != null) {
                for (CheckedInUser user : checkedInUsers) {
                    String userId = user.getUserId();

                    if (userId != null && userId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        alreadyCheckedIn = true;
                    }
                }
            }

            if (bundle != null && bundle.containsKey("fragment name")) {

                Log.d("TESTING4", bundle.getString("fragment name"));
                Log.d("TESTING4", String.valueOf(alreadyCheckedIn));
                Log.d("TESTING4", String.valueOf(alreadySignedUp));

                if (bundle.getString("fragment name").equals("MyEventsFragment")) {
                    signUpOrDeregisterButton.setText("Deregister");
                }

                else if (bundle.getString("fragment name").equals("AllEventsFragment") && (!alreadySignedUp && !alreadyCheckedIn)) {
                    signUpOrDeregisterButton.setText("Sign up");
                }

                else if (bundle.getString("fragment name").equals("AllEventsFragment") && (alreadySignedUp || alreadyCheckedIn)) {
                    signUpOrDeregisterButton.setText("Deregister");
                }
            }
        }

        signUpOrDeregisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (signUpOrDeregisterButton.getText() == "Sign up") {

                    showSignUpConfirmation();
                }

                else {
                    Toast.makeText(EventDetailsActivity.this, "Have not implemented deregister yet", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * This method creates an AlertDialog prompting the confirm whether or not they want to sign
     * up to the vent they clicked on.
     * If the user clicks confirm, it checks that the event is not full it signs them up if it's not.
     * It registers the user for the event by calling the signUserUp() method.
     * A toast is displayed if sign-up was not successful.
     */
    private void showSignUpConfirmation() {

        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(this);
        confirmDialog.setTitle("Sign up for this event?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Event event = bundle.getParcelable("event");
                        int maxAttendees;
                        try {
                            maxAttendees = Integer.valueOf(event.getEventMaxAttendees());
                            if (event.getSignedUpUsers().size() >= maxAttendees) {
                                Toast.makeText(EventDetailsActivity.this, "Event is full!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EventDetailsActivity.this, "confirmed", Toast.LENGTH_SHORT).show();
                                signUserUp();
                            }
                        } catch (Exception e) {
                            try {
                                Toast.makeText(EventDetailsActivity.this, "confirmed", Toast.LENGTH_SHORT).show();
                                signUserUp();
                            } catch (Exception f) {
                                Toast.makeText(EventDetailsActivity.this, "An error occured. We are unable to register you for this event at this time.", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(EventDetailsActivity.this, "cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
        confirmDialog.create().show();
    }

    /**
     * Signs the current user up for the event by updating the database with user and event information.
     * The user's document in Firestore is updated here to include the event ID in the "myEvents" array field.
     * The current event's document in Firestore is also updated here to include the user's ID in the "signedUpUsers" array field.
     */
    private void signUserUp() {

        Event event = bundle.getParcelable("event");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference userRef = db.collection("users").document(userId);

        userRef.update("myEvents", FieldValue.arrayUnion(event.getEventId()))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TESTING", "successfully added eventId " + event.getEventId() + " to myEvents");
                    }
                });

        DocumentReference eventRef = db.collection("events").document(event.getEventId());

        eventRef.update("signedUpUsers", FieldValue.arrayUnion(userId))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TESTING", "successfully added userId " + userId + " to this event's signedUpUsers");
                    }
                });

    }

}