package com.example.eventplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EventCreateActivity extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth;
    FirebaseUser user;
    private Button dateButton;
    private Button timeButton;
    private FloatingActionButton backButton;
    private Button imageUploadButton;
    private Button eventCreateButton;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        editTextEventName = findViewById(R.id.event_name);
        editTextMaxAttendees = findViewById(R.id.event_max_attendees);
        editTextEventLocation = findViewById(R.id.event_location);

        dateButton = findViewById(R.id.buttonDatePicker);
        timeButton = findViewById(R.id.buttonTimePicker);

        backButton = findViewById(R.id.button_back);
        imageUploadButton = findViewById(R.id.btn_upload_img);
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

        eventCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String event_name, guests, location;
                event_name = String.valueOf(editTextEventName.getText());
                guests = String.valueOf(editTextMaxAttendees.getText());
                location = String.valueOf(editTextEventLocation.getText());
                Map<String, Object> doc_event = new HashMap<>();
                doc_event.put("eventName", event_name);
                doc_event.put("eventMaxAttendees", guests);
                doc_event.put("eventDate", date_year+"/"+date_month+"/"+date_day);
                doc_event.put("eventTime", time_hour+":"+time_minute+" "+time_am_pm);
                doc_event.put("eventLocation", location);
                doc_event.put("eventOrganizer", event_creator);
                doc_event.put("eventPoster", "test value");
                db.collection("events").document()
                        .set(doc_event);
                finish();
            }
        });
    }

    private void openDateDialog() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                dateButton.setText(String.valueOf(year)+"/"+String.valueOf(month + 1)+"/"+String.valueOf(day));
                date_year = String.valueOf(year);
                date_month = String.valueOf(month + 1);
                date_day = String.valueOf(day);
            }
        }, 2024, 0, 1);

        dialog.show();
    }

    private void openTimeDialog() {
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
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