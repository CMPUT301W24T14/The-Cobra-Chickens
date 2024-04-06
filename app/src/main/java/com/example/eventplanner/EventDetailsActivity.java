// OpenAI, 2024, ChatGPT
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

public class EventDetailsActivity extends AppCompatActivity {
    private TextView eventNameTextView, eventDateTextView, eventTimeTextView, eventLocationTextView, eventDescriptionTextView, eventOrganizerTextView;
    private RecyclerView announcementsRecyclerView;
    private ArrayList<String> announcements = new ArrayList<>();;
    private ImageView poster;
    private Button signUpOrDeregisterButton;
    private Bundle bundle;
    private FirebaseFirestore db; // the database

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
                eventNameTextView.setText("Name: " + event.getEventName());
                eventDateTextView.setText("Date: " + event.getEventDate());
                eventTimeTextView.setText("Time: " + event.getEventTime());
                eventLocationTextView.setText("Location: " + event.getEventLocation());

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
    private void showSignUpConfirmation() {

        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(this);
        confirmDialog.setTitle("Sign up for this event?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Event event = bundle.getParcelable("event");
                        if (event.getSignedUpUsers().size() >= Integer.valueOf(event.getEventMaxAttendees())) {
                            Toast.makeText(EventDetailsActivity.this, "Event is full!", Toast.LENGTH_SHORT).show();
                        }

                        else {
                            Toast.makeText(EventDetailsActivity.this, "confirmed", Toast.LENGTH_SHORT).show();
                            signUserUp();
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