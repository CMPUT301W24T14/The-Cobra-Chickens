package com.example.eventplanner;

import android.content.Context;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.eventName.setText(eventsList.get(position).eventName);

        //instead of showing the full date, we just use simple date format
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = sdf.format(eventsList.get(position).eventDate);
        holder.eventDate.setText(formattedDate);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Pass the selected event to the fragment
                EventDetailsFragment fragment = new EventDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("event", eventsList.get(position));
                fragment.setArguments(bundle);

                // Replace current fragment with event detail fragment
                FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView eventName, eventDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eventName = itemView.findViewById(R.id.event_name);
            eventDate = itemView.findViewById(R.id.event_date);
        }
    }
}
