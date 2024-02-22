package com.example.eventplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    Button editUserName, editUserContact, editUserHomepage;
    Button location;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // inflate the layout for the profile fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //initializing a user to show user's details on the profile screen
        User user = new User(R.drawable.p, "A", "a@email.com","www.a.com", true);

        profilePic = view.findViewById(R.id.profilePic);
        userName = view.findViewById(R.id.profileName);
        userContact = view.findViewById(R.id.profileContact);
        userHomepage = view.findViewById(R.id.profileHomepage);
        location = view.findViewById(R.id.editLocation);
        userName.setText(userName.getText()+user.username);
        userContact.setText(userContact.getText()+user.usercontact);
        userHomepage.setText(userHomepage.getText()+user.userhomepage);
        if(user.location){
            location.setText("ON");
        } else {
            location.setText("OFF");
        }

        return view;

    }
}
