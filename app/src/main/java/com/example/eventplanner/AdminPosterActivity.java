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

public class AdminPosterActivity extends AppCompatActivity {
    private Button back;
    RecyclerView adminPostersRecyclerView;

    FirebaseFirestore db;

    CollectionReference eventsRef;

    ArrayList<Event> eventsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_posters);

        back = findViewById(R.id.back);

        adminPostersRecyclerView = findViewById(R.id.adminPostersRecyclerView);
        adminPostersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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

//            // Display the users in the ListView
            AdminPostersRecyclerAdapter adapter = new AdminPostersRecyclerAdapter(this, eventsList);
            adminPostersRecyclerView.setAdapter(adapter);
        }).addOnFailureListener(e -> {
            // Handle any errors
            Toast.makeText(this, "No events to show", Toast.LENGTH_SHORT).show();
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminPosterActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });
    }
}