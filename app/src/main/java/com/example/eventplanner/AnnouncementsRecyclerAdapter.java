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
