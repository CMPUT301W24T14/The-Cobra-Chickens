package com.example.eventplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.NotificationsAdapter;
import com.example.eventplanner.R;

import java.util.ArrayList;
import java.util.Date;

/**
 * Represents the fragment that shows users notifications.
 */
public class NotificationsFragment extends Fragment {

    private RecyclerView notificationsRecyclerView;
    private NotificationsAdapter notificationsAdapter;
    private ArrayList<com.example.eventplanner.Notification> notificationsList;
    // This will be your list of notifications

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for the notifications fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        // Initialize the RecyclerView
        notificationsRecyclerView = view.findViewById(R.id.recyclerView_notifications);
        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize your notifications list here with actual data
        notificationsList = new ArrayList<>();
        // Example notifications
        notificationsList.add(new com.example.eventplanner.Notification("Welcome", "Thanks for using our app!", new Date(), false));
        notificationsList.add(new com.example.eventplanner.Notification("Update", "Check out the latest update.", new Date(), false));
        // ... add more items as needed

        // Set up the adapter
        notificationsAdapter = new NotificationsAdapter(getActivity(), notificationsList);
        notificationsRecyclerView.setAdapter(notificationsAdapter);
        // Notify the adapter about the data change
        notificationsAdapter.notifyDataSetChanged();

        return view;
    }
}
