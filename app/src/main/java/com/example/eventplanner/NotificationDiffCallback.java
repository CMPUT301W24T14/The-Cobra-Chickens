package com.example.eventplanner;

import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;

/**
 * Callback class for calculating the difference between two lists of notifications.
 */
public class NotificationDiffCallback extends DiffUtil.Callback {

    private final ArrayList<Notification> oldNotificationsList;
    private final ArrayList<Notification> newNotificationsList;

    /**
     * Constructs a NotificationDiffCallback with the old and new lists of notifications.
     *
     * @param oldNotificationsList The old list of notifications.
     * @param newNotificationsList The new list of notifications.
     */
    public NotificationDiffCallback(ArrayList<Notification> oldNotificationsList, ArrayList<Notification> newNotificationsList) {
        this.oldNotificationsList = oldNotificationsList;
        this.newNotificationsList = newNotificationsList;
    }

    /**
     * Returns the size of the old list.
     *
     * @return The size of the old list.
     */
    @Override
    public int getOldListSize() {
        return oldNotificationsList.size();
    }

    /**
     * Returns the size of the new list.
     *
     * @return The size of the new list.
     */
    @Override
    public int getNewListSize() {
        return newNotificationsList.size();
    }

    /**
     * Determines whether the items in the old and new lists are the same.
     *
     * @param oldItemPosition The position of the item in the old list.
     * @param newItemPosition The position of the item in the new list.
     * @return True if the items are the same; otherwise, false.
     */
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        // Notification IDs (or another unique identifier) should be compared here
        return oldNotificationsList.get(oldItemPosition).getEventId().equals(newNotificationsList.get(newItemPosition).getEventId());
    }

    /**
     * Determines whether the contents of the items in the old and new lists are the same.
     *
     * @param oldItemPosition The position of the item in the old list.
     * @param newItemPosition The position of the item in the new list.
     * @return True if the contents of the items are the same; otherwise, false.
     */
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        // Compare the content of old and new notification items.
        Notification oldNotification = oldNotificationsList.get(oldItemPosition);
        Notification newNotification = newNotificationsList.get(newItemPosition);


        return oldNotification.getEventName().equals(newNotification.getEventName())
                && oldNotification.getEventDate().equals(newNotification.getEventDate())
                && oldNotification.getEventTime().equals(newNotification.getEventTime());
    }
}
