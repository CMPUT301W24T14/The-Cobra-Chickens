package com.example.eventplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class profilesRecyclerAdapter extends RecyclerView.Adapter<profilesRecyclerAdapter.ViewHolder> {

    Context context;
    ArrayList<User> profilesList;
    profilesRecyclerAdapter(Context context, ArrayList<User> profilesList){
        this.context = context;
        this.profilesList = profilesList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.profiles_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String showName = "Name:"+profilesList.get(position).getName();
        String showContact = "Contact:"+profilesList.get(position).getContactInformation();
        String showHomepage = "HomePage:"+profilesList.get(position).getHomepage();
        String proPicUrl = profilesList.get(position).getProfilePicture();
        holder.name.setText(showName);
        holder.contact.setText(showContact);
        holder.homepage.setText(showHomepage);

        if(proPicUrl !=null && proPicUrl.equals("")){
            Glide.with(holder.itemView.getContext())
                    .load("https://www.gravatar.com/avatar/default" + "?d=identicon")
                    .into(holder.proPic);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(proPicUrl)
                    .into(holder.proPic);
        }
    }

    @Override
    public int getItemCount() {
        return profilesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, contact, homepage;
        ImageView proPic;
        public ViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.name);
            contact = itemView.findViewById(R.id.contact);
            homepage = itemView.findViewById(R.id.homepage);
            proPic = itemView.findViewById(R.id.profilePic);

        }
    }
}
