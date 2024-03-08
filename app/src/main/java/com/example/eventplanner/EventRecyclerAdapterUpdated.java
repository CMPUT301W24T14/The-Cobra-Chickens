package com.example.eventplanner;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
public class EventRecyclerAdapterUpdated extends RecyclerView.Adapter<EventRecyclerAdapterUpdated.ViewHolder> {

    private ArrayList<Event> events;
    private Context context;
    private RecyclerViewInterface recyclerViewInterface;

    public EventRecyclerAdapterUpdated(Context context, ArrayList<Event> events, RecyclerViewInterface recyclerViewInterface) {
        this.events = events;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_item, parent,false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.eventName.setText(events.get(position).getEventName());
        holder.eventLocation.setText(events.get(position).getEventLocation());

        //SimpleDateFormat cardViewEventDate = new SimpleDateFormat("MM/dd/yyyy");
        holder.eventDate.setText(events.get(position).getEventDate());
        //String formattedCardViewEventData = cardViewEventDate.format(events.get(position).getEventDate());

        //holder.eventDate.setText(formattedCardViewEventData);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView eventName;
        TextView eventDate;
        TextView eventLocation;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {

            super(itemView);

            eventName = itemView.findViewById(R.id.event_name_cardview);
            eventDate = itemView.findViewById(R.id.event_date_cardview);
            eventLocation = itemView.findViewById(R.id.event_location_cardview);

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

    public void updateEventListItems(ArrayList<Event> events2){

        final EventDiffCallback diffCallback = new EventDiffCallback(this.events, events2);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.events.clear();
        this.events.addAll(events2);
        diffResult.dispatchUpdatesTo(this);
    }
}
