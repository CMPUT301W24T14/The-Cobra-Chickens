// WsCube Tech, 2022, Youtube, Recycler View in Android Studio Explained with Example | Android Recycler View Tutorial, https://www.youtube.com/watch?v=FEqF1_jDV-A
// WsCube Tech, 2022, Youtube, How to Add, Delete, and Update Items in Android RecyclerView | Android Studio Tutorial #26, https://www.youtube.com/watch?v=AUow1zsO6mg
// OpenAI, 2024, ChatGPT
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

public class EventsRecyclerAdapter extends RecyclerView.Adapter<EventsRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Event> eventsList;
    EventsRecyclerAdapter(Context context, ArrayList<Event> eventsList){
        this.context = context;
        this.eventsList = eventsList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

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

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView eventName, eventDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eventName = itemView.findViewById(R.id.event_name);
            eventDate = itemView.findViewById(R.id.event_date);
        }
    }
}
