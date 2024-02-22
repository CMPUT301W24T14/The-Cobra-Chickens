package com.example.eventplanner;

import android.app.Dialog;
import android.os.Bundle;
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

/**
 * Represents the fragment for displaying and editing a user's profile.
 */
public class ProfileFragment extends Fragment {
    ImageView profilePic;
    Button editPic;
    TextView userName, userContact, userHomepage;
    Button editDetails;
    Button location;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // inflate the layout for the profile fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //initializing a user to show user's details on the profile screen
        User user = new User(R.drawable.p, "A", "a@email.com","www.a.com", true);

        //getting all the layout objects
        profilePic = view.findViewById(R.id.profilePic);
        editPic = view.findViewById(R.id.editProfilePic);
        userName = view.findViewById(R.id.profileName);
        userContact = view.findViewById(R.id.profileContact);
        userHomepage = view.findViewById(R.id.profileHomepage);
        location = view.findViewById(R.id.location);
        editDetails = view.findViewById(R.id.editProfile);

        //setting layout objects to the User object's values
        userName.setText(userName.getText()+user.username);
        userContact.setText(userContact.getText()+user.usercontact);
        userHomepage.setText(userHomepage.getText()+user.userhomepage);
        if(user.location){
            location.setText("ON");
        } else {
            location.setText("OFF");
        }

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
                        userName.setText(name);
                        userContact.setText(contact);
                        userHomepage.setText(homePage);

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
                if(user.location){
                    location.setText("OFF");
                    user.setLocation(false);
                } else {
                    location.setText("ON");
                    user.setLocation(true);
                }
            }
        });

        //profile pic edit listener



        return view;

    }
}
