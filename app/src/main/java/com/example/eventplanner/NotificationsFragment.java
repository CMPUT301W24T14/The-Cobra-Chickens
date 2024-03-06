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

/**
 * Represents the fragment that shows users notifications.
 */
public class NotificationsFragment extends Fragment {

    private static final String TAG = "NotificationsFragment";

    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private RecyclerView notificationsRecyclerView;
    private NotificationsAdapter notificationsAdapter;
    private ArrayList<Notification> notificationsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        // Initialize Firestore and get the reference to events
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");

        // Initialize the RecyclerView
        notificationsRecyclerView = view.findViewById(R.id.recyclerView_notifications);
        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize your notifications list
        notificationsList = new ArrayList<>();
        notificationsAdapter = new NotificationsAdapter(getActivity(), notificationsList);
        notificationsRecyclerView.setAdapter(notificationsAdapter);

        // Fetch events and their notifications from Firestore
        eventsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot eventDocument : task.getResult()) {
                        // Get the notifications list from the event document
                        ArrayList<String> eventNotifications = (ArrayList<String>) eventDocument.get("Notifications");
                        if (eventNotifications != null) {
                            for (String notificationMessage : eventNotifications) {
                                // Create a Notification object for each notification string
                                notificationsList.add(new Notification(notificationMessage));
                            }
                        }
                    }
                    // Notify adapter that data set has changed
                    notificationsAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        return view;
    }
}
