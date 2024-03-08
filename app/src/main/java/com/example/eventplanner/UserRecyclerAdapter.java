package com.example.eventplanner;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder> {

    private ArrayList<User> users;
    private Context context;
    private RecyclerViewInterface recyclerViewInterface;

    public UserRecyclerAdapter(Context context, ArrayList<User> users, RecyclerViewInterface recyclerViewInterface) {
        this.users = users;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_row_organizer, parent,false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String showName = users.get(position).getName();
        String proPicUrl = users.get(position).getProfilePicture();

        holder.userName.setText(showName);

        if(proPicUrl !=null && proPicUrl.equals("")){
            Glide.with(holder.itemView.getContext())
                    .load("https://www.gravatar.com/avatar/default" + "?d=identicon")
                    .into(holder.userProfilePic);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(proPicUrl)
                    .into(holder.userProfilePic);
        }

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView userName;
        private ImageView userProfilePic;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {

            super(itemView);

            userName = itemView.findViewById(R.id.user_name);
            userProfilePic = itemView.findViewById(R.id.image_user_pro_pic);

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
}
