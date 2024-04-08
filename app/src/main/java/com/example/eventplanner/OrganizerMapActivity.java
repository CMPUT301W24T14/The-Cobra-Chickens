package com.example.eventplanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * The activity for displaying the map view of checked-in attendees to the organizer.
 */
public class OrganizerMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView eventMap;
    private GoogleMap googleMap;
    private Boolean isActive = true;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private Event event = null;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * The method that creates the activity after a user has clicked the button to see the map.
     * @param savedInstanceState A Bundle that contains the data that was most recently passed to it.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_event_map_view);

        Button mapButton = findViewById(R.id.back_button_organizer_map);


        String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissionsIfNecessary(permissions);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                this.event = bundle.getParcelable("event");
            }
        }


        eventMap = findViewById(R.id.organizer_map);

        if (isActive) {
            eventMap.onCreate(savedInstanceState);

            eventMap.getMapAsync(this);
        }


        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrganizerMapActivity.this.isActive = false;
                eventMap.setVisibility(View.GONE);
                finish();
            }
        });
    }

    /**
     * Called when the map is ready to viewed and interacted with by the user.
     * @param googleMap A non-null instance of GoogleMap associated with the MapView.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (isActive) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            googleMap.setMyLocationEnabled(true);
            if (event != null) {
                db.collection("events")
                        .document(event.getEventId())
                            .get()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Map<String, Object> data = task.getResult().getData();
                                        Map<String, GeoPoint> geopoints = (Map<String, GeoPoint>) data.get("checkedInGeopoints");
                                        if (geopoints != null) {
                                            for (Map.Entry<String, GeoPoint> entry : geopoints.entrySet()) {
                                                GeoPoint geopoint = entry.getValue();
                                                LatLng latlng = new LatLng(geopoint.getLatitude(), geopoint.getLongitude());
                                                googleMap.addMarker(new MarkerOptions()
                                                        .position(latlng)
                                                        .title("Checked-in Attendee"));
                                            }
                                        }
                                    }
                                });
            }

            LatLng edmonton = new LatLng(53.5461, -113.4937);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(edmonton, 9));
            //eventMap.onResume();
        }
    }

    /**
     * Saves the current state of the activity.
     * @param outState Bundle in that is put in the saved state.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        eventMap.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume(){
        super.onResume();
        if (isActive) {
            eventMap.onResume();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        if (isActive) {
            eventMap.onStart();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        eventMap.onStop();

    }

    @Override
    public void onPause(){
        super.onPause();
        eventMap.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        eventMap.onDestroy();
    }

    @Override
    public void onLowMemory(){
        super.onLowMemory();
        if (isActive) {
            eventMap.onLowMemory();
        }
    }

    /**
     * Result after permissions have been requested.
     * @param requestCode  The request code passed into requestPermissions(android.app.Activity, String[], int)
     * @param permissions  The permissions that were requested.
     * @param grantResults The results for whatever permissions were granted (PackageManager.PERMISSION_GRANTED or PackageManager.PERMISSION_DENIED)
     */
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
                    permissionsToRequest.toArray(new String[0]), REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Requests permissions if the user has not already granted them.
     * @param permissions An array of strings which are permissions to request.
     */
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
                    permissionsToRequest.toArray(new String[0]), REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}
