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

/**
 * RecyclerView adapter for displaying notifications.
 */
public class NotificationsRecyclerAdapter extends RecyclerView.Adapter<NotificationsRecyclerAdapter.ViewHolder> {

    private ArrayList<Notification> notifications;
    private Context context;
    private RecyclerViewInterface recyclerViewInterface;

    /**
     * Constructor for the adapter.
     *
     * @param context               The context of the calling component.
     * @param notifications        The list of notifications to be displayed.
     * @param recyclerViewInterface The interface for handling item clicks.
     */
    public NotificationsRecyclerAdapter(Context context, ArrayList<Notification> notifications, RecyclerViewInterface recyclerViewInterface) {
        this.notifications = notifications;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public NotificationsRecyclerAdapter(Context context, ArrayList<Notification> notificationsList) {
    }

    /**
     * View holder for the RecyclerView adapter.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView notificationTitle;
        TextView notificationDate;
        TextView notificationTime;

        /**
         * Constructor for the view holder.
         *
         * @param itemView               The view for each item in the RecyclerView.
         * @param recyclerViewInterface The interface for handling item clicks.
         */
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

    /**
     * Inflates the item layout and creates the ViewHolder object.
     *
     * @param parent   The parent ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification, parent, false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    /**
     * Updates the contents of the ViewHolder to reflect the item at the given position.
     *
     * @param holder   The ViewHolder that should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.notificationTitle.setText(notification.getEventName());
        holder.notificationDate.setText(notification.getEventDate());
        holder.notificationTime.setText(notification.getEventTime());


    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return notifications.size();
    }

    /**
     * Updates the list of notifications with new data.
     *
     * @param newNotifications The updated list of notifications.
     */
    public void updateNotificationListItems(ArrayList<Notification> newNotifications) {
        final NotificationDiffCallback diffCallback = new NotificationDiffCallback(this.notifications, newNotifications);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.notifications.clear();
        this.notifications.addAll(newNotifications);
        diffResult.dispatchUpdatesTo(this);
    }
}
