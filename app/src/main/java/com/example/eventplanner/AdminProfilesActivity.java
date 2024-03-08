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
    private Button back;
    private RecyclerView profilesRecyclerView;
    private FirebaseFirestore db;
    private CollectionReference usersRef;
    private ArrayList<User> profilesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profiles);

        back = findViewById(R.id.back);
        profilesRecyclerView = findViewById(R.id.adminProfilesRecyclerView);
        profilesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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

//            // Display the users in the ListView
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