package com.example.eventplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotificationsRecyclerAdapter extends RecyclerView.Adapter<NotificationsRecyclerAdapter.ViewHolder> {

    private ArrayList<Notification> notifications;
    private Context context;
    private RecyclerViewInterface recyclerViewInterface;

    public NotificationsRecyclerAdapter(Context context, ArrayList<Notification> notifications, RecyclerViewInterface recyclerViewInterface) {
        this.notifications = notifications;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification, parent, false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.notificationTitle.setText(notification.getEventName());
        holder.notificationDate.setText(notification.getEventDate());
        holder.notificationTime.setText(notification.getEventTime());

        // If your notification items include more details, set them here as well.
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView notificationTitle;
        TextView notificationDate;
        TextView notificationTime;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            notificationTitle = itemView.findViewById(R.id.notification_title);
            notificationDate = itemView.findViewById(R.id.notification_date);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && recyclerViewInterface != null) {
                    recyclerViewInterface.onItemClick(position);
                }
            });
        }
    }

    public void updateNotificationListItems(ArrayList<Notification> newNotifications) {
        final NotificationDiffCallback diffCallback = new NotificationDiffCallback(this.notifications, newNotifications);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.notifications.clear();
        this.notifications.addAll(newNotifications);
        diffResult.dispatchUpdatesTo(this);
    }

    // You'll need to implement NotificationDiffCallback similar to EventDiffCallback for this to work.

}
