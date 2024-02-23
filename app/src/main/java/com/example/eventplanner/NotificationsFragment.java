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

import java.util.ArrayList;
import java.util.Date;

public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerViewNotifications;
    private NotificationsAdapter adapter;
    private ArrayList<Notification> notificationList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        recyclerViewNotifications = view.findViewById(R.id.recyclerView_notifications);
        recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationList = new ArrayList<>();
        populateNotificationList();
        adapter = new NotificationsAdapter(getContext(), notificationList);
        recyclerViewNotifications.setAdapter(adapter);
        return view;
    }

    private void populateNotificationList() {
        // Populate the list with some example data
        notificationList.add(new Notification("Welcome!", "Thank you for using our app.", new Date(), false));
        notificationList.add(new Notification("Update Available", "A new update is now available for download.", new Date(System.currentTimeMillis() - 86400000), false)); // 24 hours ago
        notificationList.add(new Notification("Reminder", "Don't forget your meeting tomorrow at 10 AM.", new Date(System.currentTimeMillis() - 172800000), true)); // 48 hours ago
        adapter.notifyDataSetChanged();
    }
}
