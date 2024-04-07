// OpenAI, 2024, ChatGPT
package com.example.eventplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
/**
 * The AdminEventsActivity class represents the activity for managing events by the admin in the event planner application.
 * This activity displays a list of events retrieved from Firestore and allows the admin to navigate back to the admin dashboard.
 * If no events are available, it displays a toast message indicating the absence of events.
 *
 */
public class AdminEventsActivity extends AppCompatActivity {
    // Button to navigate back to admin dashboard
    private Button back;
    // RecyclerView to display events
    RecyclerView adminEventsRecyclerView;
    // Firestore instance
    FirebaseFirestore db;
    // Reference to the collection of events in Firestore
    CollectionReference eventsRef;
    // List to store events
    ArrayList<Event> eventsList = new ArrayList<>();

    /**
     * Called when the activity is first created. This is where we'll
     * initialize our RecyclerView, Firestore, and fetch events data.
     *
     * @param savedInstanceState If the activity is being re-initialized
     *                           after previously being shut down then this Bundle
     *                           contains the data it most recently supplied in
     *                           onSaveInstanceState(Bundle). Note: Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_events);
        // Initialize back button
        back = findViewById(R.id.back);
        // Initialize RecyclerView
        adminEventsRecyclerView = findViewById(R.id.adminEventsRecyclerView);
        adminEventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");


        // Retrieve all users from Firestore
        eventsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot document : queryDocumentSnapshots) {
                // Extract event data and add it to the list
                String documentId = document.getId();
                String name = document.getString("eventName");
                String maxAttendees = document.getString("eventMaxAttendees");
                String date = document.getString("eventDate");
                String time = document.getString("eventTime");
                String location = document.getString("eventLocation");
                String poster = document.getString("eventPoster");
                eventsList.add(new Event(documentId, name, maxAttendees,date, time,location, poster));
            }

//            // Display the users in the ListView
            AdminEventsRecyclerAdapter adapter = new AdminEventsRecyclerAdapter(this, eventsList);
            adminEventsRecyclerView.setAdapter(adapter);
        }).addOnFailureListener(e -> {
            // Handle any errors
            Toast.makeText(this, "No events to show", Toast.LENGTH_SHORT).show();
        });
        // Set click listener for back button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to AdminActivity
                Intent intent = new Intent(AdminEventsActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });
    }
}