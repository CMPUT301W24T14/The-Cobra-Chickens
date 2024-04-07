// OpenAI, 2024, ChatGPT
package com.example.eventplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
/**
 * The AdminActivity class represents the activity for admin functionalities in the event planner application.
 * This activity allows the admin to navigate to different sections such as events, pictures, posters, and profiles.
 * Admin can also log out from this activity.
 */
public class AdminActivity extends AppCompatActivity {
    // Declare member variables for buttons
    private Button eventsBtn;
    private Button picsBtn;
    private Button postersBtn;
    private Button profilesBtn;
    private Button logout;

    /**
     * Called when the activity is first created. This is where we'll
     * initialize our buttons and set click listeners for each.
     *
     * @param savedInstanceState If the activity is being re-initialized
     *                           after previously being shut down then this Bundle
     *                           contains the data it most recently supplied in
     *                           onSaveInstanceState(Bundle). Note: Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize buttons with corresponding views in layout
        eventsBtn = findViewById(R.id.eventsBtn);
        picsBtn = findViewById(R.id.picsBtn);
        postersBtn = findViewById(R.id.postersBtn);
        profilesBtn = findViewById(R.id.profilesBtn);
        logout = findViewById(R.id.logOutBtn);

        // Set click listeners for buttons to handle user interaction

        // Open AdminEventsActivity when eventsBtn is clicked
        eventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminEventsActivity.class);
                startActivity(intent);

            }
        });

        // Open AdminProPicActivity when picsBtn is clicked
        picsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminProPicActivity.class);
                startActivity(intent);
            }
        });
        // Open AdminPosterActivity when postersBtn is clicked
        postersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminPosterActivity.class);
                startActivity(intent);
            }
        });
        // Open AdminProfilesActivity when profilesBtn is clicked
        profilesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminProfilesActivity.class);
                startActivity(intent);
            }
        });
        // Log out the user and navigate back to MainActivity when logout button is clicked
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate back to MainActivity
                Intent intent = new Intent(AdminActivity.this, MainActivity.class);
                // Clear the back stack to prevent the user from returning to the AdminActivity
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                // Start the MainActivity
                startActivity(intent);
                // Finish the current activity
                finish();
            }
        });

    }
}