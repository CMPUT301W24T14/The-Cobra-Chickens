// WsCube Tech, 2022, Youtube, Recycler View in Android Studio Explained with Example | Android Recycler View Tutorial, https://www.youtube.com/watch?v=FEqF1_jDV-A
// WsCube Tech, 2022, Youtube, How to Add, Delete, and Update Items in Android RecyclerView | Android Studio Tutorial #26, https://www.youtube.com/watch?v=AUow1zsO6mg
// OpenAI, 2024, ChatGPT

package com.example.eventplanner;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
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

        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Delete Profile")
                        .setMessage("Are you sure you want to delete this profile?")
                        .setIcon(R.drawable.del)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                profilesList.remove(position);
                                deleteProfileFromDatabase(position);
                                notifyItemRemoved(position);
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
        TextView name, contact, homepage;
        ImageView proPic;

        LinearLayout row;
        public ViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.name);
            contact = itemView.findViewById(R.id.contact);
            homepage = itemView.findViewById(R.id.homepage);
            proPic = itemView.findViewById(R.id.profilePic);
            row = itemView.findViewById(R.id.row);

        }
    }

    private void deleteProfileFromDatabase(int position) {
        FirebaseFirestore db =  FirebaseFirestore.getInstance();
        // Get the user ID from the list
        String userId = profilesList.get(position).getUserId();

        // Get a reference to the user document in Firestore
        DocumentReference userRef = db.collection("users").document(userId);

        // Delete the user document from Firestore
        userRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Document successfully deleted
                        Toast.makeText(context, "Profile deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                        Toast.makeText(context, "Failed to delete profile", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
