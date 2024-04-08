package com.example.eventplanner;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class ScanFragment extends Fragment {
    private CompoundBarcodeView barcodeView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private SharedViewModel sharedViewModel;
    private Boolean userHasGeolocationOn;
    private Boolean eventRequiresGeolocation;
    private Boolean successfulCheckIn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        barcodeView = view.findViewById(R.id.barcode_scanner);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        if (ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, 1);
        }

        barcodeView.decodeContinuous(result -> {
            barcodeView.pause();
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            if (result.getResult() != null) {
                String[] parts = String.valueOf(result.getResult()).split(":");
                String checkInCodeFromQR = parts[0];
                String userId = auth.getCurrentUser().getUid();

                db.collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        userHasGeolocationOn = (Boolean) documentSnapshot.get("Location");
                    }
                });


                final String[] eventId = new String[1];

                if (Objects.equals(parts[1], "check")) {

                    db.collection("events").whereEqualTo("checkInCode", checkInCodeFromQR)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        eventId[0] = document.getId();

                                        // Retrieve the checkedInUsers array from the document
                                        HashMap<String, String> checkedInUsersFromDB = (HashMap<String, String>) document.get("checkedInUsers");

                                        HashMap<String, GeoPoint> checkedInGeoPoints = (HashMap<String, GeoPoint>) document.get("checkedInGeopoints");

                                        eventRequiresGeolocation = (Boolean) document.get("geolocationTracking");

                                        // Check if userId exists in checkedInUsers array
                                        if (checkedInUsersFromDB != null) {
                                            // if the user has already checked in and location requirements match up
                                            if ((checkedInUsersFromDB.containsKey(userId) && (eventRequiresGeolocation != null && eventRequiresGeolocation) && (userHasGeolocationOn != null && userHasGeolocationOn))) {

                                                for (Map.Entry<String, String> entry : checkedInUsersFromDB.entrySet()) {
                                                    if (Objects.equals(entry.getKey(), userId)) {

                                                        // HashMap<String, String> oldMap = new HashMap<>();

                                                        int numberOfCheckins = Integer.parseInt(entry.getValue());

                                                        // oldMap.put(userId, String.valueOf(numberOfCheckins));

                                                        numberOfCheckins += 1;

                                                        // HashMap<String, String> newMap = new HashMap<>();

                                                        checkedInUsersFromDB.put(userId, String.valueOf(numberOfCheckins));

                                                        db.collection("events").document(eventId[0]).update("checkedInUsers", checkedInUsersFromDB);

                                                        successfulCheckIn = true;
                                                        Log.d("TESTING", "got here 1");
                                                        getUserLocation(eventId[0], checkedInGeoPoints);

                                                    }
                                                }

                                            }
                                            // if the user has already checked in to an event that requires their geolocation and is trying to check in again but now has their location off
                                            // or if the user is trying to check in for the first time but has their location off
                                            else if ((checkedInUsersFromDB.containsKey(userId) && (eventRequiresGeolocation != null && eventRequiresGeolocation) && (userHasGeolocationOn != null && !userHasGeolocationOn))
                                                    || (!checkedInUsersFromDB.containsKey(userId) && (eventRequiresGeolocation != null && eventRequiresGeolocation) && (userHasGeolocationOn != null && !userHasGeolocationOn))) {

                                                builder.setTitle("Check-in Failed");
                                                builder.setMessage("This event requires geolocation tracking to be enabled in order to check in. Please enable this in your profile and/or settings.");
                                                builder.setPositiveButton("OK", (dialog, which) -> {
                                                    dialog.dismiss();
                                                    barcodeView.resume();
                                                }).show();

                                                Log.d("TESTING", "got here 2");
                                                successfulCheckIn = false;
                                            }
                                            // first time check in for event that requires location and they have it turned on
                                            else if ((!checkedInUsersFromDB.containsKey(userId) && (eventRequiresGeolocation != null && eventRequiresGeolocation) && (userHasGeolocationOn != null && userHasGeolocationOn))) {

                                                HashMap<String, String> map = new HashMap<>();
                                                checkedInUsersFromDB.put(userId, "1");

                                                db.collection("events").document(eventId[0]).update("checkedInUsers", checkedInUsersFromDB);
                                                successfulCheckIn = true;
                                                Log.d("TESTING", "got here 3");
                                                getUserLocation(eventId[0], checkedInGeoPoints);


                                            }
                                            // first time check in for the first time that doesn't require their location
                                            else if (!checkedInUsersFromDB.containsKey(userId) && (eventRequiresGeolocation != null && !eventRequiresGeolocation)) {

                                                Log.d("TESTING", "got here 4");
                                                HashMap<String, String> map = new HashMap<>();
                                                checkedInUsersFromDB.put(userId, "1");

                                                db.collection("events").document(eventId[0]).update("checkedInUsers", checkedInUsersFromDB);
                                                successfulCheckIn = true;
                                            }
                                            // if the user has already checked into and event and is checking in again and the event doesn't track location
                                            else if (checkedInUsersFromDB.containsKey(userId) && (eventRequiresGeolocation != null && !eventRequiresGeolocation)) {

                                                for (Map.Entry<String, String> entry : checkedInUsersFromDB.entrySet()) {
                                                    if (Objects.equals(entry.getKey(), userId)) {

                                                        // HashMap<String, String> oldMap = new HashMap<>();

                                                        int numberOfCheckins = Integer.parseInt(entry.getValue());

                                                        // oldMap.put(userId, String.valueOf(numberOfCheckins));

                                                        numberOfCheckins += 1;

                                                        HashMap<String, String> newMap = new HashMap<>();

                                                        checkedInUsersFromDB.put(userId, String.valueOf(numberOfCheckins));

                                                        db.collection("events").document(eventId[0]).update("checkedInUsers", checkedInUsersFromDB);
                                                        Log.d("TESTING", "got here 5");
                                                        successfulCheckIn = true;

                                                    }
                                                }

                                            }

                                            else {
                                                AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());

                                                successfulCheckIn = false;
                                                Log.d("TESTING", "got here 6");
                                                builder.setTitle("Check-in Failed");
                                                builder.setMessage("Your location was not found. Please try again.");
                                                builder.setPositiveButton("OK", (dialog, which) -> {
                                                    dialog.dismiss();
                                                    barcodeView.resume();
                                                }).show();;
                                            }

                                        }
                                    }

                                    if (successfulCheckIn) {

                                        Log.d("EVENT ID", eventId[0]);
                                        db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                                            if (documentSnapshot.exists() && eventId[0] != null) {
                                                db.collection("events").document(eventId[0]).update("signedUpUsers", FieldValue.arrayUnion(userId));
                                                db.collection("users").document(userId).update("myEvents", FieldValue.arrayUnion(eventId[0]));

                                                //db.collection("events").document(eventId[0]).update("checkedInUsers", FieldValue.arrayUnion(userId));
                                                db.collection("users").document(userId).update("checkedInto", FieldValue.arrayUnion(eventId[0]));

                                                sharedViewModel.setEventUpdated(true);

                                                builder.setTitle("Success!");
                                                builder.setMessage("You have successfully checked into the event!");
//                                            builder.setMessage("You have successfully signed up for the event!");
                                            } else {
                                                builder.setTitle("Failure!");
                                                if (eventId[0] == null) {
                                                    builder.setMessage("Event Id is empty");
                                                } else {
                                                    builder.setMessage("User does not exist!");
                                                }
                                            }
                                            builder.setPositiveButton("OK", (dialog, which) -> {
                                                dialog.dismiss();
                                                barcodeView.resume();
                                            }).show();
                                        });

                                    }
                                }
                            });

                } else if (Objects.equals(parts[1], "promo")) {
                    db.collection("events").whereEqualTo("promoCode", checkInCodeFromQR)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        eventId[0] = document.getId();
                                    }
                                    Log.d("EVENT ID", eventId[0]);
                                    db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                                        Dialog dialog = new Dialog(requireContext());
                                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialogInterface) {
                                                // This code will be called as soon as the dialog is no longer showing on screen
                                                barcodeView.resume();
                                            }
                                        });

                                        if (documentSnapshot.exists() && eventId[0] != null) {

                                            dialog.setContentView(R.layout.activity_event_details);

                                            ImageView poster = dialog.findViewById(R.id.poster);
                                            poster.setVisibility(View.GONE);
                                            db.collection("events").document(eventId[0]).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if (documentSnapshot.exists()) {
                                                        // Get the field from the document
                                                        String eventName = documentSnapshot.getString("eventName");
                                                        String eventTime = documentSnapshot.getString("eventTime");
                                                        String eventDate = documentSnapshot.getString("eventDate");
                                                        String eventLocation = documentSnapshot.getString("eventLocation");
                                                        String eventDescription = documentSnapshot.getString("eventDescription");

                                                        // Find the TextView and set the text

                                                        TextView eventNameTextView = dialog.findViewById(R.id.event_name);
                                                        TextView eventTimeTextView = dialog.findViewById(R.id.event_time);
                                                        TextView eventDateTextView = dialog.findViewById(R.id.event_date);
                                                        TextView eventLocationTextView = dialog.findViewById(R.id.event_location);
                                                        TextView eventDescriptionTextView = dialog.findViewById(R.id.event_description);

                                                        eventNameTextView.setText(eventName);
                                                        eventDateTextView.setText(eventDate);
                                                        eventTimeTextView.setText(eventTime);
                                                        eventDescriptionTextView.setText(eventDescription);
                                                        eventLocationTextView.setText(eventLocation);
                                                    }
                                                }
                                            });

                                            Button signUpButton = dialog.findViewById(R.id.button_signup_or_deregister);

                                            signUpButton.setOnClickListener(v -> {
                                                Toast.makeText(requireActivity(), "Your event is in My Events", Toast.LENGTH_SHORT).show();
                                                db.collection("events").document(eventId[0]).update("signedUpUsers", FieldValue.arrayUnion(userId));
                                                db.collection("users").document(userId).update("myEvents", FieldValue.arrayUnion(eventId[0]));
                                                dialog.dismiss();
                                            });


                                            Window window = dialog.getWindow();
                                            if (window != null) {
                                                // Set the dimensions of the dialog
                                                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                                                layoutParams.copyFrom(window.getAttributes());

                                                // Set the width and height of the dialog to match the screen size
                                                DisplayMetrics displayMetrics = new DisplayMetrics();
                                                ((Activity) requireContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                                                layoutParams.width = displayMetrics.widthPixels;
                                                layoutParams.height = (int) (displayMetrics.heightPixels * .60);

                                                window.setAttributes(layoutParams);
                                            }
                                            dialog.show();
                                        } else {
                                            builder.setTitle("Failure!");
                                            if (eventId[0] == null) {
                                                builder.setMessage("Event Id is empty");
                                            } else {
                                                builder.setMessage("User does not exist!");
                                            }
                                        }

                                    });
                                }
                            });
                } else if (Objects.equals(parts[1], "admin")) {
                    Intent intent = new Intent(getContext(), AdminActivity.class);
                    startActivity(intent);
                }
            } else {
                builder.setTitle("Failure!");
                builder.setMessage("result is null!");
                builder.setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    barcodeView.resume();
                }).show();
            }
        });

        return view;
    }

    private void getUserLocation(String eventId, HashMap<String, GeoPoint> checkedInGeoPoints) {

        // Get the user's current location
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            successfulCheckIn = false;
            Log.d("TESTING", "got here 7");
            builder.setTitle("Check-in Failed");
            builder.setMessage("Your location was not found. Please try again.");
            builder.setPositiveButton("OK", (dialog, which) -> {
                dialog.dismiss();
                barcodeView.resume();
            }).show();;
        }
        else {

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                // Once you have the location, store it in Firestore
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                String userId = auth.getCurrentUser().getUid();

                                // Create a GeoPoint object with latitude and longitude
                                GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());

                                // Create a map to store user data
                                HashMap<String, GeoPoint> userData = new HashMap<>();
                                checkedInGeoPoints.put(userId, geoPoint);

                                // Update Firestore document with user's location
                                db.collection("events").document(eventId).update("checkedInGeopoints", checkedInGeoPoints);

                                successfulCheckIn = true;
                            }
                        }
                    });

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    public void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
