package com.example.eventplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity {
    Button eventsBtn;
    Button picsBtn;
    Button profilesBtn;

    Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        eventsBtn = findViewById(R.id.eventsBtn);
        picsBtn = findViewById(R.id.picsBtn);
        profilesBtn = findViewById(R.id.profilesBtn);
        logout = findViewById(R.id.logOutBtn);

        eventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminEventsActivity.class);
                startActivity(intent);

            }
        });

        picsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminPicsActivity.class);
                startActivity(intent);
            }
        });

        profilesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminProfilesActivity.class);
                startActivity(intent);
            }
        });

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