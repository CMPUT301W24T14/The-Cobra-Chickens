package com.example.eventplanner;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.ArrayContainsFilter;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Activity for creating events.
 */
public class EventCreateActivity extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private FirebaseFirestore db; // the database
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Button dateButton;
    private Button timeButton;
    private FloatingActionButton backButton;
    private Event event;
    private CollectionReference eventsRef;
    private Button eventCreateButton;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView eventPosterImageView;
    private Button imageUploadButton;
    private Uri imageUri;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private String event_creator;
    private String event_name;
    private String event_max_attendees;
    private String date_month;
    private String date_day;
    private String date_year;
    private String time_hour;
    private String time_minute;
    private String time_am_pm;
    private String event_poster;
    private String event_location;
    private TextInputEditText editTextEventName, editTextEventDescription, editTextMaxAttendees, editTextEventLocation;
    private DocumentReference key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");

        backButton = findViewById(R.id.button_back);

        editTextEventName = findViewById(R.id.event_name);
        editTextEventDescription = findViewById(R.id.event_description);
        editTextMaxAttendees = findViewById(R.id.event_max_attendees);
        editTextEventLocation = findViewById(R.id.event_location);

        dateButton = findViewById(R.id.buttonDatePicker);
        timeButton = findViewById(R.id.buttonTimePicker);

        imageUploadButton = findViewById(R.id.btn_upload_img);
        eventPosterImageView = findViewById(R.id.posterImageView);

        eventCreateButton = findViewById(R.id.btn_create_event);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        assert user != null;
        event_creator = user.getUid();

        // Check if the user is null, if so, return to splash to initialize.
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
            startActivity(intent);
            finish();
        }

        // Return to previous page if user does not wish to create an event
        backButton.setOnClickListener(view -> finish());

        // Initialize the ActivityResultLauncher
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        imageUri = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            eventPosterImageView.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        // Check for click on image upload button.
        imageUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        // Date button pressed -> Open date picker dialog.
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDateDialog();
            }
        });

        // Time button pressed -> open time picker dialog.
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimeDialog();
            }
        });

        // Check for click on event creation button, and handle backend logic.
        // Check for click on event creation button, and handle backend logic.
        eventCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ensure an image is selected before creating the event
                if (imageUri == null) {
                    Toast.makeText(EventCreateActivity.this, "Please select an image for the event poster", Toast.LENGTH_SHORT).show();
                    return;
                }

                /// Null checks for date and time.
                if (date_day == null || date_month == null || date_year == null) {
                    Toast.makeText(EventCreateActivity.this, "Please Enter a Valid Date!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (time_hour == null || time_minute == null) {
                    Toast.makeText(EventCreateActivity.this, "Please Enter a Valid Time!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String event_name, description, guests, location;
                event_name = String.valueOf(editTextEventName.getText());
                description = String.valueOf(editTextEventDescription.getText());
                guests = String.valueOf(editTextMaxAttendees.getText());
                location = String.valueOf(editTextEventLocation.getText());

                Boolean geolocation;
                SwitchCompat geolocation_switch = findViewById(R.id.organizer_switch_locationtracking);
                geolocation = geolocation_switch.isChecked();

                // Upload the image to Firebase Storage
                uploadImageAndCreateEvent(event_name, description, guests, location, geolocation);
            }
        });
    }

    // Function to upload image to Firebase Storage and create the event
    private void uploadImageAndCreateEvent(String eventName, String description, String guests, String location, Boolean geolocation) {
        // Generate a unique filename using UUID
        String filename = UUID.randomUUID().toString();

        // Create a reference to the location in Firebase Storage where the image will be uploaded
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + filename);

        // Upload image to Firebase Storage
        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Get the download URL
                        String imageUrl = uri.toString();

                        // Code to store imageUrl in Firestore under "Event" item
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Map<String, Object> eventData = new HashMap<>();
                        eventData.put("eventName", eventName);
                        eventData.put("eventDescription", description);
                        eventData.put("eventMaxAttendees", guests);
                        eventData.put("eventDate", date_year+"/"+date_month+"/"+date_day);
                        eventData.put("eventTime", time_hour+":"+time_minute+" "+time_am_pm);
                        eventData.put("eventLocation", location);
                        eventData.put("eventOrganizer", event_creator);
                        eventData.put("eventPoster", imageUrl); // Set the event poster URL
                        eventData.put("checkInCode", "");
                        eventData.put("promoCode", "");
                        eventData.put("eventAnnouncements", new ArrayList<>());
                        eventData.put("signedUpUsers", new ArrayList<>());
                        eventData.put("geolocationTracking", geolocation);
                        eventData.put("checkedInGeopoints", new HashMap<>());
                        eventData.put("checkedInUsers", new HashMap<>());

                        // Add the event data to Firestore
                        db.collection("events")
                                .add(eventData)
                                .addOnSuccessListener(documentReference -> {
                                    // Event added successfully
                                    Log.d(TAG, "Event added with ID: " + documentReference.getId());
                                    // Add the event ID to the organizer's list of events
                                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    DocumentReference userRef = db.collection("users").document(userId);
                                    userRef.update("Organizing", FieldValue.arrayUnion(documentReference.getId()))
                                            .addOnSuccessListener(aVoid -> {
                                                Log.d(TAG, "Event ID added to user's list of organizing events");
                                                // Finish the activity
                                                finish();
                                            })
                                            .addOnFailureListener(e -> {
                                                // Handle errors
                                                Log.e(TAG, "Error updating user document", e);
                                                // Finish the activity
                                                finish();
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    // Handle errors
                                    Log.e(TAG, "Error adding event", e);
                                });
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                    Log.e(TAG, "Error uploading image", e);
                });
    }

    // Function for opening the gallery on user device.
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }

    // Function to open the calendar to select a date.
    private void openDateDialog() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            // When user confirms the date, grab those values.
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                dateButton.setText(String.valueOf(year)+"/"+String.valueOf(month + 1)+"/"+String.valueOf(day)); // add one to month because month starts at 0 here.
                date_year = String.valueOf(year);
                date_month = String.valueOf(month + 1);
                date_day = String.valueOf(day);
            }
        }, 2024, 0, 1);

        dialog.show();
    }

    // Function to open the clock to select a time.
    private void openTimeDialog() {
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            // When user confirms the time, grab those values.
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Some logic to properly format time for our uses. IE AM/PM, 12:05 vs 12:5, etc.
                time_am_pm = (hourOfDay < 12) ? "am" : "pm";
                time_hour = String.valueOf((hourOfDay <= 12) ? hourOfDay : hourOfDay - 12);
                if (minute < 10) {
                    timeButton.setText(time_hour +":"+"0"+ minute +" "+time_am_pm);
                    time_minute = "0"+ minute;
                }
                else {
                    timeButton.setText(time_hour +":"+ minute +" "+time_am_pm);
                    time_minute = String.valueOf(minute);
                };
            }
        }, 12, 0, false);

        dialog.show();
    }
}