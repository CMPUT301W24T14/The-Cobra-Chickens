// OpenAI, 2024, ChatGPT

package com.example.eventplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
/**
 * The AdminPosterActivity class represents the activity for managing event posters by the admin in the event planner application.
 * This activity displays a list of posters retrieved from Firestore.
 * Admin can navigate back to the admin dashboard from this activity.
 * If no events with posters are available, it displays a toast message indicating the absence of events.
 */
public class AdminPosterActivity extends AppCompatActivity {
    // Button to navigate back to admin dashboard
    private Button back;
    // RecyclerView to display events with posters
    RecyclerView adminPostersRecyclerView;
    // Firestore instance
    FirebaseFirestore db;
    // Reference to the collection of events in Firestore
    CollectionReference eventsRef;
    // List to store events with posters
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
        setContentView(R.layout.activity_admin_posters);
        // Initialize back button
        back = findViewById(R.id.back);
        // Initialize RecyclerView
        adminPostersRecyclerView = findViewById(R.id.adminPostersRecyclerView);
        adminPostersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                if(poster != null && !poster.equals("")) {
                    eventsList.add(new Event(documentId, name, maxAttendees, date, time, location, poster));
                }
            }

            // Display the users in the ListView
            AdminPostersRecyclerAdapter adapter = new AdminPostersRecyclerAdapter(this, eventsList);
            adminPostersRecyclerView.setAdapter(adapter);
        }).addOnFailureListener(e -> {
            // Handle any errors
            Toast.makeText(this, "No events to show", Toast.LENGTH_SHORT).show();
        });
        // Set click listener for back button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to AdminActivity
                Intent intent = new Intent(AdminPosterActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });
    }
}