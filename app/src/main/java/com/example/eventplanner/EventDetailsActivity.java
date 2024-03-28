// OpenAI, 2024, ChatGPT
package com.example.eventplanner;

import android.content.DialogInterface;
import android.health.connect.datatypes.units.Length;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class EventDetailsActivity extends AppCompatActivity {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private TextView eventNameTextView, eventDateTextView, eventTimeTextView, eventOrganizerTextView;
    private RecyclerView announcementsRecyclerView;
    private ArrayList<String> announcements = new ArrayList<>();;
    private ImageView poster;
    private Button signUpButton;
    private Bundle bundle;
    private FirebaseFirestore db; // the database
    private MapView eventMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        eventNameTextView = findViewById(R.id.event_name);
        eventDateTextView = findViewById(R.id.event_date);
        eventTimeTextView = findViewById(R.id.event_time);
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
                eventNameTextView.setText("Name:" + event.getEventName());
                eventDateTextView.setText("Date:" + event.getEventDate());
                eventTimeTextView.setText("Time:" + event.getEventTime());

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

        signUpButton = findViewById(R.id.signupBtn);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUpConfirmation();
            }
        });

        eventMap = (MapView) findViewById(R.id.event_map);
        eventMap.setTileSource(TileSourceFactory.OpenTopo);
        eventMap.setUseDataConnection(false);
        String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissionsIfNecessary(permissions);
        eventMap.setMultiTouchControls(true);

        IMapController mapController = eventMap.getController();
        mapController.setZoom(12);
        GeoPoint startPoint = new GeoPoint(53.5461, -113.4937);
        mapController.setCenter(startPoint);


    }

    @Override
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        eventMap.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        eventMap.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
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