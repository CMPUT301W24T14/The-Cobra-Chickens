package com.example.eventplanner;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;

public class EventDiffCallback extends DiffUtil.Callback {

    private final ArrayList<Event> mOldEventList;
    private final ArrayList<Event> mNewEventList;

    public EventDiffCallback(ArrayList<Event> mOldEventList, ArrayList<Event> mNewEventList) {
        this.mOldEventList = mOldEventList;
        this.mNewEventList = mNewEventList;
    }

    @Override
    public int getOldListSize() {
        return mOldEventList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewEventList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldEventList.get(oldItemPosition).getEventId() == mNewEventList.get(newItemPosition).getEventId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Event oldEvent = mOldEventList.get(oldItemPosition);
        final Event newEvent = mNewEventList.get(newItemPosition);

        return oldEvent.getEventName().equals(newEvent.getEventName());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {

        // for item animations
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
