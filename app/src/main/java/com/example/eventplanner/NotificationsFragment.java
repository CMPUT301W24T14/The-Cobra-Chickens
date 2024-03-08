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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Represents the fragment that shows users notifications.
 */
public class NotificationsFragment extends Fragment {

    private FirebaseFirestore db;
    private CollectionReference notificationsCollectionReference;
    private RecyclerView notificationsRecyclerView;
    private ArrayList<Notification> notificationsList;
    private NotificationsRecyclerAdapter notificationsAdapter;

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        // Initialize Firestore and get reference to notifications/events collection
        db = FirebaseFirestore.getInstance();
        notificationsCollectionReference = db.collection("events");

        // Setup the RecyclerView
        notificationsRecyclerView = view.findViewById(R.id.recyclerView_notifications);
        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the list and adapter
        notificationsList = new ArrayList<>();
        notificationsAdapter = new NotificationsRecyclerAdapter(getContext(), notificationsList);
        notificationsRecyclerView.setAdapter(notificationsAdapter);

        // Fetch and display notifications
        displayNotifications();

        return view;
    }

    /**
     * Fetches notifications from Firestore and updates the RecyclerView.
     */
    private void displayNotifications() {
        notificationsCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    notificationsList.clear();
                    for (DocumentSnapshot doc : task.getResult()) {
                        Notification notification = doc.toObject(Notification.class); // Assuming Notification class exists
                        notificationsList.add(notification);
                    }
                    notificationsAdapter.notifyDataSetChanged();
                } else {
                    Log.e("Firestore Error", task.getException().toString());
                }
            }
        });
    }

    // Optionally, implement onItemClick or any other methods as needed
}
