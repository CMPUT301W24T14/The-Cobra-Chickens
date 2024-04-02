package com.example.eventplanner;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class NotificationsActivity extends AppCompatActivity {

    private static final String TAG = "NotificationsActivity";

    private FirebaseFirestore db;
    private CollectionReference notificationsRef;
    private RecyclerView notificationsRecyclerView;
    private NotificationsRecyclerAdapter notificationsAdapter;
    private ArrayList<MyNotification> notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        // Initialize the RecyclerView
        notificationsRecyclerView = findViewById(R.id.recyclerView_notifications);
        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Firestore and get reference to notifications collection
        db = FirebaseFirestore.getInstance();
        notificationsRef = db.collection("events");

        // Fetch notifications from Firestore
        notificationsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                notifications = new ArrayList<>();
                for (MyNotification notification : task.getResult().toObjects(MyNotification.class)) {
                    notifications.add(notification);
                }
                // Initialize the adapter with the fetched data
                notificationsAdapter = new NotificationsRecyclerAdapter(this, notifications);
                notificationsRecyclerView.setAdapter(notificationsAdapter);
            } else {
                Log.e(TAG, "Error getting documents: ", task.getException());
            }
        });
    }
}
