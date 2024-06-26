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

// not in use
/**
 * Represents the fragment that shows users notifications.
 */
public class NotificationsFragment extends Fragment {

    private static final String TAG = "NotificationsFragment";

    private FirebaseFirestore db;
    private CollectionReference notificationsRef;
    private RecyclerView notificationsRecyclerView;
    private NotificationsRecyclerAdapter notificationsAdapter;

    private ArrayList<MyNotification> notificationsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        db = FirebaseFirestore.getInstance();

        // connect notificationsRecyclerView to the actual RecyclerView widget in fragment_notifications.xml
        notificationsRecyclerView = view.findViewById(R.id.recyclerView_notifications);

        notificationsList = new ArrayList<>();

        // configure myEventsRecyclerView to have a vertical orientation when arranging items *
        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // initialize notificationsRecyclerAdapter and set it as the adapter for notificationsRecyclerView
        notificationsAdapter = new NotificationsRecyclerAdapter(getContext(), notificationsList);
        notificationsRecyclerView.setAdapter(notificationsAdapter);

        // Initialize Firestore and get reference to notifications collection
        notificationsRef = db.collection("events");

        // Fetch notifications from Firestore
        notificationsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                notificationsList = new ArrayList<>();
                for (MyNotification notification : task.getResult().toObjects(MyNotification.class)) {
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
