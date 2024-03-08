package com.example.eventplanner;

import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;


public class NotificationDiffCallback extends DiffUtil.Callback {

        private final ArrayList<Notification> oldNotificationsList;
        private final ArrayList<Notification> newNotificationsList;

        public NotificationDiffCallback(ArrayList<Notification> oldNotificationsList, ArrayList<Notification> newNotificationsList) {
            this.oldNotificationsList = oldNotificationsList;
            this.newNotificationsList = newNotificationsList;
        }

        @Override
        public int getOldListSize() {
            return oldNotificationsList.size();
        }

        @Override
        public int getNewListSize() {
            return newNotificationsList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            // Notification IDs (or another unique identifier) should be compared here
            return oldNotificationsList.get(oldItemPosition).getEventId().equals(newNotificationsList.get(newItemPosition).getEventId());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            // Compare the content of old and new notification items. This is an example using eventName.
            Notification oldNotification = oldNotificationsList.get(oldItemPosition);
            Notification newNotification = newNotificationsList.get(newItemPosition);

            // This can be expanded to compare more fields if needed.
            return oldNotification.getEventName().equals(newNotification.getEventName())
                    && oldNotification.getEventDate().equals(newNotification.getEventDate())
                    && oldNotification.getEventTime().equals(newNotification.getEventTime());
        }
    }

