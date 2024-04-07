// WsCube Tech, 2022, Youtube, Recycler View in Android Studio Explained with Example | Android Recycler View Tutorial, https://www.youtube.com/watch?v=FEqF1_jDV-A
// WsCube Tech, 2022, Youtube, How to Add, Delete, and Update Items in Android RecyclerView | Android Studio Tutorial #26, https://www.youtube.com/watch?v=AUow1zsO6mg
// OpenAI, 2024, ChatGPT

package com.example.eventplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
/**
 * The AnnouncementsRecyclerAdapter class is responsible for managing the RecyclerView
 * that displays announcements in the event planner application.
 * It inflates the announcement layout, binds data to views, and determines the number of items in the RecyclerView.
 * This adapter is used to populate the RecyclerView with announcements.
 */
public class AnnouncementsRecyclerAdapter extends RecyclerView.Adapter<AnnouncementsRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> announcementsList;
    /**
     * Constructor for AnnouncementsRecyclerAdapter.
     *
     * @param context The context of the calling activity
     * @param announcementsList The list of announcements to be displayed in the RecyclerView
     */

    public AnnouncementsRecyclerAdapter(Context context, ArrayList<String> announcementsList) {
        this.context = context;
        this.announcementsList = announcementsList;
    }
    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position
     * @param viewType The view type of the new View
     * @return A new ViewHolder that holds a View of the given view type
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.announcements, parent, false);
        return new ViewHolder(view);
    }
    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position
     * @param position The position of the item within the adapter's data set
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String announcement = announcementsList.get(position);
        holder.announcementTextView.setText(announcement);
    }
    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter
     */
    @Override
    public int getItemCount() {
        return announcementsList.size();
    }

    /**
     * The ViewHolder class represents each item in the RecyclerView.
     * It holds references to the views that will be populated with data.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView announcementTextView;
        /**
         * Constructor for ViewHolder.
         *
         * @param itemView The view corresponding to each item in the RecyclerView
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            announcementTextView = itemView.findViewById(R.id.announcement);
        }
    }
}
