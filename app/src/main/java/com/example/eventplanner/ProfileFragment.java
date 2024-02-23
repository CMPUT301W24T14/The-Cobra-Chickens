package com.example.eventplanner;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Represents the fragment for displaying and editing a user's profile.
 */
public class ProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "ProfileFragment";
    ImageView profilePic;
    Button editPic;
    Button delPic;
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
        delPic = view.findViewById(R.id.deleteProfilePic);
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

        editPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickImageLauncher.launch(intent);

            }
        });

        delPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProfilePic();
            }
        });





        return view;

    }

    private void deleteProfilePic() {

        userId = "et9ykXKsNzo3ETU3Vwwg";
        usersRef.document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if(document.contains("ProfilePic")){
                            usersRef.document(userId).update("ProfilePic", FieldValue.delete());
                            Glide.with(requireContext()).load("https://www.gravatar.com/avatar/"+userId+"?d=identicon").into(profilePic);

                        } else {
                            Toast toast = Toast.makeText(getContext(), "You can't delete default Profile Picture", Toast.LENGTH_LONG);
                            toast.show();
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


    ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            Uri imageuri = result.getData().getData();
            uploadImage(imageuri);
        }
    }
    );

    private void uploadImage(Uri imageUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profile_images/" + userId + ".png");

        UploadTask uploadTask = storageRef.putFile(imageUri);

        uploadTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Image upload successful
                // Get the download URL of the uploaded image
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Update the user document with the profile picture URI
                    usersRef.document(userId).update("ProfilePic", uri.toString())
                            .addOnSuccessListener(aVoid -> {
                                // Update the user object with the new profile picture URI
                                user.setImg(uri.toString());
//                                // Update the ImageView with the new profile picture
                                Glide.with(requireContext()).load(uri).into(profilePic);

                            })
                            .addOnFailureListener(e -> {
                                // Handle errors updating the user document
                                Log.e(TAG, "Error updating user document", e);
                                Toast.makeText(requireContext(), "Failed to update profile picture", Toast.LENGTH_SHORT).show();
                            });
                }).addOnFailureListener(e -> {
                    // Handle errors getting the download URL
                    Log.e(TAG, "Error getting download URL", e);
                    Toast.makeText(requireContext(), "Failed to get download URL", Toast.LENGTH_SHORT).show();
                });
            } else {
                // Image upload failed
                Log.e(TAG, "Image upload failed", task.getException());
                Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(snapshot -> {
            // You can track the progress of the upload here if needed
            double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
            Log.d(TAG, "Upload is " + progress + "% done");
        });
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

                        if(document.contains("ProfilePic")){
                            String profilePicURI = document.getString("ProfilePic");
                            user = new User(profilePicURI, name, contact, homePage, usrlocation);
                            Glide.with(requireContext()).load(profilePicURI).into(profilePic);
                        } else {
                            user = new User(name, contact, homePage, usrlocation);
                            Glide.with(requireContext()).load("https://www.gravatar.com/avatar/"+userId+"?d=identicon").into(profilePic);
                        }
                        //setting layout objects to the User object's values
                        userName.setText("Name: " + user.getUsername());
                        userContact.setText("Contact: " + user.getUsercontact());
                        userHomepage.setText("Homepage: " + user.getUserhomepage());
                        if (user.getLocation()) {
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
