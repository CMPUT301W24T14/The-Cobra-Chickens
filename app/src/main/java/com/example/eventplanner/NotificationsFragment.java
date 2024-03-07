package com.example.eventplanner;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

/**
 * Represents the fragment that shows users notifications.
 */
public class NotificationsFragment extends Fragment {

    private static final String TAG = "NotificationsFragment";

    private FirebaseFirestore db;
    private CollectionReference notificationsRef;
    private RecyclerView notificationsRecyclerView;
    private NotificationsAdapter notificationsAdapter;
    private ArrayList<com.example.eventplanner.Notification> notificationsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for the notifications fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        // Initialize Firestore and get the reference to notifications
        db = FirebaseFirestore.getInstance();
        notificationsRef = db.collection("notifications");

        // Initialize the RecyclerView
        notificationsRecyclerView = view.findViewById(R.id.recyclerView_notifications);
        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // create a date object
        Date date = new Date();

        // Initialize your notifications list
        notificationsList = new ArrayList<>();

        //creating adapter for recycler view and setting the adapter
        notificationsAdapter = new NotificationsAdapter(getActivity(), notificationsList);
        notificationsRecyclerView.setAdapter(notificationsAdapter);

        // Fetch notifications from Firestore
        notificationsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Assume your Notification object has a constructor that matches your data structure in Firestore
                        String title = document.getString("title");
                        String message = document.getString("message");
                        Date date = document.getDate("date");
                        boolean read = document.getBoolean("read");
                        notificationsList.add(new com.example.eventplanner.Notification(title, message, date, read));
                    }
                    // Notify adapter that data set has changed
                    notificationsAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        // Set up the adapter with the notifications list
        notificationsAdapter = new NotificationsAdapter(getActivity(), notificationsList);
        notificationsRecyclerView.setAdapter(notificationsAdapter);

        return view;
    }
}
