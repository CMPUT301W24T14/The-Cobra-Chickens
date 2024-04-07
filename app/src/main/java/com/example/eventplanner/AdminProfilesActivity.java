/**
 * The AdminProfilesActivity class manages the profile display for admin in the event planner application.
 * It retrieves user profiles from Firestore and displays them in a RecyclerView.
 * Admin can navigate back to the admin dashboard from this activity.
 * If no profiles are available, it displays a toast message indicating the absence of profiles.
 * It initializes views, retrieves data from Firestore, and handles navigation back to the admin dashboard.
 */
// OpenAI, 2024, ChatGPT, creating back button, changing intent

package com.example.eventplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdminProfilesActivity extends AppCompatActivity {
    // Button to navigate back to admin dashboard
    private Button back;
    // RecyclerView to display user profiles
    private RecyclerView profilesRecyclerView;
    // Firestore instance
    private FirebaseFirestore db;
    // Reference to the collection of users in Firestore
    private CollectionReference usersRef;
    // List to store user profiles
    private ArrayList<User> profilesList = new ArrayList<>();

    /**
     * Called when the activity is first created. This is where we'll
     * initialize our RecyclerView, Firestore, and fetch user profiles data.
     *
     * @param savedInstanceState If the activity is being re-initialized
     *                           after previously being shut down then this Bundle
     *                           contains the data it most recently supplied in
     *                           onSaveInstanceState(Bundle). Note: Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profiles);
        // Initialize back button
        back = findViewById(R.id.back);
        // Initialize RecyclerView
        profilesRecyclerView = findViewById(R.id.adminProfilesRecyclerView);
        profilesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("users");


        // Retrieve all users from Firestore
        usersRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot document : queryDocumentSnapshots) {
                // Extract user data and add it to the list
                String documentId = document.getId();
                String name = document.getString("Name");
                String contact = document.getString("Contact");
                String homepage = document.getString("Homepage");
                String proPic = document.getString("ProfilePic");
                profilesList.add(new User(documentId,name, homepage,contact,proPic));
            }

            // Display the users in the ListView
            profilesRecyclerAdapter adapter = new profilesRecyclerAdapter(this, profilesList);
            profilesRecyclerView.setAdapter(adapter);
        }).addOnFailureListener(e -> {
            // Handle any errors
            Toast.makeText(this, "No profiles to show", Toast.LENGTH_SHORT).show();
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminProfilesActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });
    }
}