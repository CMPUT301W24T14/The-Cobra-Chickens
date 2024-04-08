package com.example.eventplanner;
// WsCube Tech, 2022, Youtube, Recycler View in Android Studio Explained with Example | Android Recycler View Tutorial, https://www.youtube.com/watch?v=FEqF1_jDV-A
// WsCube Tech, 2022, Youtube, How to Add, Delete, and Update Items in Android RecyclerView | Android Studio Tutorial #26, https://www.youtube.com/watch?v=AUow1zsO6mg
// OpenAI, 2024, ChatGPT

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
/**
 * Adapter class for populating events in a RecyclerView.
 * This adapter binds event data to the views displayed in the RecyclerView.
 */
public class EventsRecyclerAdapter extends RecyclerView.Adapter<EventsRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Event> eventsList;
    /**
     * Constructor for the EventsRecyclerAdapter.
     * @param context The context in which the adapter is created.
     * @param eventsList The list of events to be displayed.
     */
    EventsRecyclerAdapter(Context context, ArrayList<Event> eventsList){
        this.context = context;
        this.eventsList = eventsList;
    }

    /**
     * Inflates the layout for individual event items in the RecyclerView.
     * @param parent The ViewGroup that the View gets added to after it is bound to an certain adapter position.
     * @param viewType The type of the new View.
     * @return A ViewHolder that holds a View..
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * Binds data to the ViewHolder.
     * @param holder The ViewHolder that data will be bound to.
     * @param position The position of the item in the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.eventName.setText(eventsList.get(position).getEventName());

        //instead of showing the full date, we just use simple date format
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = sdf.format(eventsList.get(position).getEventDate());
        holder.eventDate.setText(formattedDate);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Pass the selected event to the fragment
                Intent intent = new Intent(context, EventDetailsActivity.class);
                intent.putExtra("event", eventsList.get(position));
                context.startActivity(intent);
            }
        });


    }

    /**
     * Returns the total number of events in the data is the list the adapter is using.
     * @return The total number of events in the data set.
     */
    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    /**
     * ViewHolder class to hold the views for each event item in the RecyclerView.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView eventName, eventDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eventName = itemView.findViewById(R.id.event_name);
            eventDate = itemView.findViewById(R.id.event_date);
        }
    }
}
