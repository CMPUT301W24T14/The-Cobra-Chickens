package com.example.eventplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
// not in use
/**
 * Adapter class for populating notifications in a RecyclerView.
 * This adapter binds notification data to the views displayed in the RecyclerView.
 */
public class NotificationsRecyclerAdapter extends RecyclerView.Adapter<NotificationsRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<MyNotification> notifications;
    /**
     * Constructor for the NotificationsRecyclerAdapter.
     * @param context The context in which the adapter is created.
     * @param notifications The list of notifications to be displayed.
     */
    public NotificationsRecyclerAdapter(Context context, ArrayList<MyNotification> notifications) {
        this.notifications = notifications;
        this.context = context;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyNotification notification = notifications.get(position);
        if (notification.getTitle() != null) {
            holder.notificationTitleTextView.setText(notification.getTitle());
        } else {
            holder.notificationDateTextView.setText("Unable to Get Notification name");
        }
        if (notification.getMessage() != null) {
            holder.notificationMessageTextView.setText(notification.getMessage());
        } else {
            holder.notificationDateTextView.setText("No Message to Display !");
        }
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView notificationTitleTextView;
        TextView notificationMessageTextView;
        TextView notificationDateTextView;
        /**
         * ViewHolder class to hold the views for each notification item in the RecyclerView.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationTitleTextView = itemView.findViewById(R.id.notification_title);
            notificationMessageTextView = itemView.findViewById(R.id.notification_message);
            notificationDateTextView = itemView.findViewById(R.id.notification_date);
        }
    }
}
