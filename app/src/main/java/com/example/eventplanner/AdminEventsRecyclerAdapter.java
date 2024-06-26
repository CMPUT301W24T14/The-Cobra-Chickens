package com.example.eventplanner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
/**
 * The AdminEventsRecyclerAdapter class is responsible for managing the RecyclerView
 * that displays events in the admin events activity.
 * It handles the creation of view holders, binding data to views, and event handling.
 * Admin can delete events by clicking on them.
 * It also interacts with Firestore to delete events from the database.
 */
public class AdminEventsRecyclerAdapter extends RecyclerView.Adapter<AdminEventsRecyclerAdapter.ViewHolder>{
    Context context;
    ArrayList<Event> eventsList;
    /**
     * Constructor for AdminEventsRecyclerAdapter
     *
     * @param context The context of the calling activity
     * @param eventsList The list of events to be displayed in the RecyclerView
     */
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
        String posterUrl = eventsList.get(position).getEventPoster();
        holder.name.setText(showName);
        holder.maxAttendees.setText(showMaxAttendees);
        holder.date.setText(showDate);
        holder.time.setText(showTime);
        holder.location.setText(showLocation);

        if (posterUrl != null && !posterUrl.equals("")){
            Glide.with(holder.itemView.getContext()).load(posterUrl).into(holder.poster);
        }
        else {
            holder.poster.setImageResource(R.drawable.a);
        }

        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Delete Event")
                        .setMessage("Are you sure you want to delete this event?")
                        .setIcon(R.drawable.del)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int newPosition = holder.getAdapterPosition(); // Use getAdapterPosition to get the current item position
                                deleteEventFromDatabase(newPosition); // Assuming there's an 'id' field and a method to delete the profile from the database
                                eventsList.remove(newPosition);
                                notifyItemRemoved(newPosition);
                                notifyItemRangeChanged(newPosition, eventsList.size());
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, maxAttendees, date, time, location;
        ImageView poster;

        LinearLayout row;
        /**
         * Constructor for ViewHolder
         *
         * @param itemView The view corresponding to each item in the RecyclerView
         */
        public ViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.name);
            maxAttendees = itemView.findViewById(R.id.maxAttendees);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            location = itemView.findViewById(R.id.location);
            row = itemView.findViewById(R.id.row);
            poster = itemView.findViewById(R.id.Poster);

        }
    }
    /**
     * Deletes an event from the Firestore database.
     *
     * @param position The position of the event to be deleted in the events list
     */
    private void deleteEventFromDatabase(int position) {
        FirebaseFirestore db =  FirebaseFirestore.getInstance();
        // Get the user ID from the list
        String eventId = eventsList.get(position).getEventId();

        // Get a reference to the user document in Firestore
        DocumentReference eventsRef = db.collection("events").document(eventId);

        // Delete the user document from Firestore
        eventsRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Document successfully deleted
                        Toast.makeText(context, "Event deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                        Toast.makeText(context, "Failed to delete event", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
