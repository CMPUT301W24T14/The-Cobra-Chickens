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

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
    private NotificationsRecyclerAdapter notificationsAdapter;
    private ArrayList<Notification> notificationsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        // Initialize the RecyclerView
        notificationsRecyclerView = view.findViewById(R.id.recyclerView_notifications);
        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize Firestore and get reference to notifications collection
        db = FirebaseFirestore.getInstance();
        notificationsRef = db.collection("notifications");

        // Fetch notifications from Firestore
        notificationsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                notificationsList = new ArrayList<>();
                for (Notification notification : task.getResult().toObjects(Notification.class)) {
                    notificationsList.add(notification);
                }
                // Initialize the adapter with the fetched data
                notificationsAdapter = new NotificationsRecyclerAdapter(getActivity(), notificationsList);
                notificationsRecyclerView.setAdapter(notificationsAdapter);
            } else {
                Log.e(TAG, "Error getting documents: ", task.getException());
            }
        });

        return view;
    }
}
