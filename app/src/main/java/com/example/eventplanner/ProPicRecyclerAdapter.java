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

public class ProPicRecyclerAdapter extends RecyclerView.Adapter<ProPicRecyclerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<User> profilesList;
    ProPicRecyclerAdapter(Context context, ArrayList<User> profilesList){
        this.context = context;
        this.profilesList = profilesList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.propic_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String showName = "Name:"+profilesList.get(position).getName();
        String proPicUrl = profilesList.get(position).getProfilePicture();
        holder.name.setText(showName);
        if(proPicUrl !=null && !proPicUrl.equals("")){
            Glide.with(holder.itemView.getContext())
                    .load(proPicUrl)
                    .into(holder.proPic);
        }

        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Delete Profile Pic")
                        .setMessage("Are you sure you want to delete this profile pic?")
                        .setIcon(R.drawable.del)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                profilesList.remove(position);
//                                deleteProfileFromDatabase(position);
//                                notifyItemRemoved(position);
                                int newPosition = holder.getAdapterPosition(); // Use getAdapterPosition to get the current item position
                                deleteProPicFromDatabase(newPosition); // Assuming there's an 'id' field and a method to delete the profile from the database
                                profilesList.remove(newPosition);
                                notifyItemRemoved(newPosition);
                                notifyItemRangeChanged(newPosition, profilesList.size());
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
        return profilesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private ImageView proPic;
        LinearLayout row;
        public ViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.userName);
            proPic = itemView.findViewById(R.id.ProPicAdmin);
            row = itemView.findViewById(R.id.row);

        }
    }

    private void deleteProPicFromDatabase(int position) {
        FirebaseFirestore db =  FirebaseFirestore.getInstance();
        // Get the user ID from the list
        String userId = profilesList.get(position).getUserId();

        // Get a reference to the user document in Firestore
        DocumentReference userRef = db.collection("users").document(userId);

        // Delete the user document from Firestore
        userRef.update("ProfilePic", "")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Document successfully deleted
                        Toast.makeText(context, "Profile Pic deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                        Toast.makeText(context, "Failed to delete profile pic", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
