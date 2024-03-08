package com.example.eventplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdminEventsRecyclerAdapter extends RecyclerView.Adapter<AdminEventsRecyclerAdapter.ViewHolder>{
    Context context;
    ArrayList<Event> eventsList;

    AdminEventsRecyclerAdapter(Context context,ArrayList<Event> eventsList ){
        this.context = context;
        this.eventsList = eventsList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.events_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String showName = "Name:"+eventsList.get(position).getEventName();
        String showMaxAttendees = "Max Attendees:"+eventsList.get(position).getEventMaxAttendees();
        String showDate = "Date:"+eventsList.get(position).getEventDate();
        String showTime = "Time:"+eventsList.get(position).getEventTime();
        String showLocation = "Location:"+eventsList.get(position).getEventLocation();
        holder.name.setText(showName);
        holder.maxAttendees.setText(showMaxAttendees);
        holder.date.setText(showDate);
        holder.time.setText(showTime);
        holder.location.setText(showLocation);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, maxAttendees, date, time, location;
        ImageView poster;

        LinearLayout row;
        public ViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.name);
            maxAttendees = itemView.findViewById(R.id.maxAttendees);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            location = itemView.findViewById(R.id.location);
            row = itemView.findViewById(R.id.row);

        }
    }
}
