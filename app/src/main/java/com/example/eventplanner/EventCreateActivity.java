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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.annotation.NonNull;
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
    private TextInputEditText editTextEventName, editTextMaxAttendees, editTextEventLocation;
    private DocumentReference key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");

        backButton = findViewById(R.id.button_back);

        editTextEventName = findViewById(R.id.event_name);
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
        eventCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /// Null checks for date and time.
                if (date_day == null || date_month == null || date_year == null) {
                    Toast.makeText(EventCreateActivity.this, "Please Enter a Valid Date!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (time_hour == null || time_minute == null) {
                    Toast.makeText(EventCreateActivity.this, "Please Enter a Valid Time!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String event_name, guests, location;
                event_name = String.valueOf(editTextEventName.getText());
                guests = String.valueOf(editTextMaxAttendees.getText());
                location = String.valueOf(editTextEventLocation.getText());
                // Create a map of items to be put into the database
                Map<String, Object> doc_event = new HashMap<>();
                doc_event.put("eventName", event_name);
                doc_event.put("eventMaxAttendees", guests);
                doc_event.put("eventDate", date_year+"/"+date_month+"/"+date_day);
                doc_event.put("eventTime", time_hour+":"+time_minute+" "+time_am_pm);
                doc_event.put("eventLocation", location);
                doc_event.put("eventOrganizer", event_creator);
                doc_event.put("eventPoster", "test value");

                doc_event.put("checkInCode", "");
                doc_event.put("promoCode", "");

                doc_event.put("eventAnnouncements", new ArrayList<>());
                doc_event.put("signedUpUsers", new ArrayList<>());
                doc_event.put("checkedInUsers", new ArrayList<>());


                //DocumentReference key;
                key = db.collection("events").document();
                key.set(doc_event).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Log.d("TESTING", "added this event id:" + (key.getId()));

                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DocumentReference userRef = db.collection("users").document(userId);

                        userRef.update("Organizing", FieldValue.arrayUnion(key.getId()))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("TESTING", "SUCCESS added  " + (key.getId()));

                                    }
                                });
                    }
                });
                finish();
            }
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