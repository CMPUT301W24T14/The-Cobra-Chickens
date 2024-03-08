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

public class AnnouncementsRecyclerAdapter extends RecyclerView.Adapter<AnnouncementsRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> announcementsList;

    public AnnouncementsRecyclerAdapter(Context context, ArrayList<String> announcementsList) {
        this.context = context;
        this.announcementsList = announcementsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.announcements, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String announcement = announcementsList.get(position);
        holder.announcementTextView.setText(announcement);
    }

    @Override
    public int getItemCount() {
        return announcementsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView announcementTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            announcementTextView = itemView.findViewById(R.id.announcement);
        }
    }
}
