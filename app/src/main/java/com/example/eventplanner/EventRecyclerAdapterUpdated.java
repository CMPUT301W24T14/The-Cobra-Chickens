package com.example.eventplanner;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import org.checkerframework.checker.signature.qual.CanonicalName;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * The RecyclerViewAdapter for displaying events contained in a list of Event objects.
 */
public class EventRecyclerAdapterUpdated extends RecyclerView.Adapter<EventRecyclerAdapterUpdated.ViewHolder> {

    /* General information on how to implement an adapter for a recycler view.
   Reference:
        Author        : Practical Coding
        Date Accessed : 3/8/2024
        License       : Creative Commons
        URL           : https://www.youtube.com/watch?v=Mc0XT58A1Z4&t=317s&ab_channel=PracticalCoding
        Used in       : Throughout entire class.
    */

    private ArrayList<Event> events; // the ArrayList of Event objects
    private Context context; // context
    private RecyclerViewInterface recyclerViewInterface;

    public EventRecyclerAdapterUpdated(Context context, ArrayList<Event> events, RecyclerViewInterface recyclerViewInterface) {
        this.events = events;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    /* How to filter items in a recyclerView.
        Reference:
            Author        : CodingSTUFF
            Date Accessed : 4/5/2024
            License       : Creative Commons
            URL           : https://www.youtube.com/watch?v=tQ7V7iBg5zE&ab_channel=CodingSTUFF
        Used in lines:
                EventRecyclerAdapterUpdated: 53-55
                AllEventsFragment: 80-95, 117-135
                MyEventsFragment: 77-92, 114-132
                OrganizeEventsFragment: 87-102, 124-144
     */

    /**
     * Sets the list of events that are to be displayed to the filtered list.
     *
     * @param filteredList The ArrayList of Event objects that represents the list of filtered
     *                     events that match the search bar query.
     */
    public void setFilteredList(ArrayList<Event> filteredList) {
        this.events = filteredList;
        notifyDataSetChanged();
    }

    /**
     * Inflates the view for each event item in the Recycler view.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return The view that displays an event item.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    /**
     * Binds the appropriate data to the ViewHolder.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // grab event details, only displaying name, date, and location
        holder.eventName.setText(events.get(position).getEventName());
        holder.eventLocation.setText(events.get(position).getEventLocation());
        holder.eventDate.setText(events.get(position).getEventDate());

        Event event = events.get(position);

        //Log.d("TESTING8", event.getEventName());

        if (holder.eventPoster != null && event.getEventPoster() != null && !event.getEventPoster().isEmpty()) {
            Glide.with(context)
                    .load(event.getEventPoster())
                    .into(holder.eventPoster);
        }


        ArrayList<CheckedInUser> checkedInUsers = event.getCheckedInUsers();

        // display that the user has checked in if they are in the event's checkedInUsers list
        if (checkedInUsers != null) {
            for (CheckedInUser user : checkedInUsers) {

                String userId = user.getUserId();

                Log.d("TESTING8", "user id in list: " + userId);
                Log.d("TESTING8", "device user id: " + FirebaseAuth.getInstance().getCurrentUser().getUid());

                if (userId != null && userId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    Log.d("TESTING8", "setting " + event.getEventName());
                    holder.checkInStatus.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /**
     * Returns the size of the list of events.
     * @return The size of events.
     */
    @Override
    public int getItemCount() {
        return events.size();
    }

    /**
     * View Holder that holds the views of items found in the appropriate RecyclerView.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView eventName;
        TextView eventDate;
        TextView eventLocation;
        ImageView eventPoster;
        TextView checkInStatus;

        /**
         * Constructor of the ViewHolder.
         * @param itemView The view of the event item in the RecyclerView.
         * @param recyclerViewInterface The interface that handles clicks on event items.
         */
        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {

            super(itemView);

            eventName = itemView.findViewById(R.id.event_name_cardview);
            eventDate = itemView.findViewById(R.id.event_date_cardview);
            eventLocation = itemView.findViewById(R.id.event_location_cardview);
            eventPoster = itemView.findViewById(R.id.cardView_event_poster);

            checkInStatus = itemView.findViewById(R.id.tv_check_in_status);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (recyclerViewInterface != null) {

                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
