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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The RecyclerViewAdapter for displaying users contained in a list of User objects.
 */
public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder> {

    private ArrayList<User> users; // the list of users
    private Context context;
    private RecyclerViewInterface recyclerViewInterface;
    private String eventId;
    private FirebaseFirestore db;

    public UserRecyclerAdapter(Context context, String eventId, ArrayList<User> users, RecyclerViewInterface recyclerViewInterface) {
        this.users = users;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
        this.eventId = eventId;
    }

    /**
     * Inflates the view for each user item in the Recycler view.
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return The view that displays a user item.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_row_organizer, parent,false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    /**
     * Binds the appropriate data to the ViewHolder.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String showName = users.get(position).getName();
        String proPicUrl = users.get(position).getProfilePicture();

        db = FirebaseFirestore.getInstance();
        db.collection("events").document(eventId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        HashMap<String, String> checkedInUsersFromDB = (HashMap<String, String>) documentSnapshot.get("checkedInUsers");

                        if (checkedInUsersFromDB != null && checkedInUsersFromDB.containsKey(users.get(holder.getAdapterPosition()).getUserId())) {

                            String myNum = checkedInUsersFromDB.get(users.get(holder.getAdapterPosition()).getUserId());

                            holder.numberOfCheckins.setText("Check-in count: " + myNum);

                        }
                    }
                });

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

    /**
     * Returns the size of the list of users.
     * @return The size of users.
     */
    @Override
    public int getItemCount() {
        return users.size();
    }

    /**
     * View Holder that holds the views of users found in the appropriate RecyclerView.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView userName;
        private ImageView userProfilePic;
        private TextView numberOfCheckins;

        /**
         * Constructor of the ViewHolder.
         * @param itemView The view of the user item in the RecyclerView.
         * @param recyclerViewInterface The interface that handles clicks on user items --> not yet implemented.
         */
        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {

            super(itemView);

            userName = itemView.findViewById(R.id.tv_user_name);
            userProfilePic = itemView.findViewById(R.id.image_user_pro_pic);
            numberOfCheckins = itemView.findViewById(R.id.tv_number_checkins);

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
