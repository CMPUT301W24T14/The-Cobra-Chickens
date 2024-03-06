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

public class EventCreateActivity extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    FirebaseAuth mAuth;
    private Button dateButton;
    private Button timeButton;
    private FloatingActionButton backButton;
    private Button imageUploadButton;
    private Button eventCreateButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        //FirebaseUser currentUser = mAuth.getCurrentUser();

        dateButton = findViewById(R.id.buttonDatePicker);
        timeButton = findViewById(R.id.buttonTimePicker);

        backButton = findViewById(R.id.button_back);
        imageUploadButton = findViewById(R.id.btn_upload_img);
        eventCreateButton = findViewById(R.id.btn_create_event);


        backButton.setOnClickListener(view -> finish());

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDateDialog();
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimeDialog();
            }
        });


    }

    private void openDateDialog() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                dateButton.setText(String.valueOf(year)+"/"+String.valueOf(month + 1)+"/"+String.valueOf(day));
            }
        }, 2024, 0, 1);

        dialog.show();
    }

    private void openTimeDialog() {
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String am_pm = (hourOfDay < 12) ? "am" : "pm";
                Integer hour = (hourOfDay <= 12) ? hourOfDay : hourOfDay - 12;
                if (minute < 10) {
                    timeButton.setText(hour +":"+"0"+ minute +" "+am_pm);
                }
                else timeButton.setText(hour +":"+ minute +" "+am_pm);
            }
        }, 12, 0, false);

        dialog.show();
    }

}