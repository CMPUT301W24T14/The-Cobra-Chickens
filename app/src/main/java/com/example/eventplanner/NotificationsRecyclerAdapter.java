package com.example.eventplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotificationsRecyclerAdapter extends RecyclerView.Adapter<NotificationsRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Notification> notificationsList;

    public NotificationsRecyclerAdapter(Context context, ArrayList<Notification> notificationsList) {
        this.context = context;
        this.notificationsList = notificationsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notificationsList.get(position);
        holder.notificationTitleTextView.setText(notification.getTitle());
        holder.notificationMessageTextView.setText(notification.getMessage());
        if (notification.getDate() != null) {
            holder.notificationDateTextView.setText(notification.getDate().toString());
        } else {
            holder.notificationDateTextView.setText("No date available");
        }
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView notificationTitleTextView;
        TextView notificationMessageTextView;
        TextView notificationDateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationTitleTextView = itemView.findViewById(R.id.notification_title);
            notificationMessageTextView = itemView.findViewById(R.id.notification_message);
            notificationDateTextView = itemView.findViewById(R.id.notification_date);
        }
    }
}
