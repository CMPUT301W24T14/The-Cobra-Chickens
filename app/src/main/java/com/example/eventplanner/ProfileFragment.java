package com.example.eventplanner;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Represents the fragment for displaying and editing a user's profile.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    ImageView profilePic;
    Button editPic;
    TextView userName, userContact, userHomepage;
    Button editDetails;
    Button location;

    User user;

    String userId;

    FirebaseFirestore db;
    CollectionReference usersRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // inflate the layout for the profile fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        getUser();

        //getting all the layout objects
        profilePic = view.findViewById(R.id.profilePic);
        editPic = view.findViewById(R.id.editProfilePic);
        userName = view.findViewById(R.id.profileName);
        userContact = view.findViewById(R.id.profileContact);
        userHomepage = view.findViewById(R.id.profileHomepage);
        location = view.findViewById(R.id.location);
        editDetails = view.findViewById(R.id.editProfile);



        //listening for edit button
        editDetails.setOnClickListener(new View.OnClickListener() {
            EditText editName, editContact, editHomepage;
            Button saveBtn;
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.edit_dialog);
                dialog.show();

                editName = dialog.findViewById(R.id.editName);
                editContact = dialog.findViewById(R.id.editContact);
                editHomepage = dialog.findViewById(R.id.editHomepage);
                saveBtn = dialog.findViewById(R.id.saveBtn);

                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = "";
                        String contact = "";
                        String homePage = "";

                        //getting user input
                        name = editName.getText().toString().trim();
                        contact = editContact.getText().toString().trim();
                        homePage = editHomepage.getText().toString().trim();

                        //updating the user object and the textviews
                        userName.setText("Name:" + name);
                        userContact.setText("Contact:" +contact);
                        userHomepage.setText("Homepage:"+homePage);

                        usersRef = db.collection("users");
                        usersRef.document(userId).update("Name", name, "Contact", contact, "Homepage",homePage);
                        user.setUsername(name);
                        user.setUsercontact(contact);
                        user.setUserhomepage(homePage);

                        dialog.dismiss();
                    }
                });

            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usersRef = db.collection("users");

                if(user.location){
                    location.setText("OFF");
                    usersRef.document(userId).update("Location", false);
                    user.setLocation(false);
                } else {
                    location.setText("ON");
                    usersRef.document(userId).update("Location", true);
                    user.setLocation(true);
                }
            }
        });





        return view;

    }

    private void getUser(){
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("users");

        userId = "et9ykXKsNzo3ETU3Vwwg";

        usersRef.document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String name = document.getString("Name");
                        String contact = document.getString("Contact");
                        String homePage = document.getString("Homepage");
                        boolean usrlocation = document.getBoolean("Location");

                        user = new User(name, contact, homePage, usrlocation);

                        //setting layout objects to the User object's values
                        userName.setText("Name: " + user.username);
                        userContact.setText("Contact: " + user.usercontact);
                        userHomepage.setText("Homepage: " + user.userhomepage);
                        if (user.location) {
                            location.setText("ON");
                        } else {
                            location.setText("OFF");
                        }


                    } else {
                        Log.d(TAG,"No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }
}
