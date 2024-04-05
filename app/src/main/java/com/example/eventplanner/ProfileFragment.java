// OpenAI, 2024, ChatGPT


package com.example.eventplanner;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
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
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.lang.reflect.Array;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 * Represents the fragment for displaying and editing a user's profile.
 */
public class ProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "ProfileFragment";
    private ImageView profilePic;
    private Button editPic;
    private Button delPic;
    private TextView userName, userContact, userHomepage;
    private Button editDetails;
    private Button location;
    private Button adminLogin;
    private User user;
    private String userId;
    private FirebaseFirestore db;
    private CollectionReference usersRef;
    private FirebaseUser user_test;
    private FirebaseAuth auth_test;

    // location stuff
    private TextView latitude, longitude;
    private SwitchCompat locationSwitch;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // inflate the layout for the profile fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //getting all the layout objects
        profilePic = view.findViewById(R.id.profilePic);
        editPic = view.findViewById(R.id.editProfilePic);
        delPic = view.findViewById(R.id.deleteProfilePic);
        userName = view.findViewById(R.id.profileName);
        userContact = view.findViewById(R.id.profileContact);
        userHomepage = view.findViewById(R.id.profileHomepage);
        location = view.findViewById(R.id.location);
        editDetails = view.findViewById(R.id.editProfile);
        adminLogin = view.findViewById(R.id.adminLoginBtn);

        auth_test = FirebaseAuth.getInstance();
        user_test = auth_test.getCurrentUser();
        getUser();

        // location stuff
        latitude = view.findViewById(R.id.textview_lat);
        longitude = view.findViewById(R.id.textview_long);
        locationSwitch = view.findViewById(R.id.switch_locationtracking);

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
                        user.setName(name);
                        user.setContactInformation(contact);
                        user.setHomepage(homePage);

                        if (name != null && name.equals("")) {
                            Glide.with(requireContext()).load("https://www.gravatar.com/avatar/" + userId + "?d=identicon").into(profilePic);
                        }
                        else {
                            Glide.with(requireContext()).load("https://www.gravatar.com/avatar/" + name + "?d=identicon").into(profilePic);
                        }

                        dialog.dismiss();
                    }
                });

            }
        });

        // location button now only displays on or off based on the geolocation switch, not clickable
//        location.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                usersRef = db.collection("users");
//
//                if(user.getGeolocationTrackingEnabled()){
//                    location.setText("OFF");
//                    usersRef.document(userId).update("Location", false);
//                    user.setGeolocationTrackingEnabled(false);
//                } else {
//                    location.setText("ON");
//                    usersRef.document(userId).update("Location", true);
//                    user.setGeolocationTrackingEnabled(true);
//                }
//            }
//        });

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


        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AdminActivity.class);
                startActivity(intent);
            }
        });

        // handle location stuff
        usersRef = db.collection("users");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // handle setting the Location field to the correct boolean and the switch on or off based on current location perms
        if (isLocationPermissionGranted() && isLocationEnabled()) {
            locationSwitch.setChecked(true);

            location.setText("ON");
            usersRef.document(currentUser.getUid()).update("Location", true);
        }
        else {
            locationSwitch.setChecked(false);

            location.setText("OFF");
            usersRef.document(currentUser.getUid()).update("Location", false);
        }

        // Google's API for accessing location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        // get user's location if the switch is currently on
        if (locationSwitch.isChecked()) {
            getLocation(); // for testing only*
        }

        locationSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if the switch is checked on
                if (locationSwitch.isChecked()) {

                    // get user's location if device Location services are on and location perms for app are granted
                    if (isLocationEnabled() && isLocationPermissionGranted()) {
                        getLocation(); // for testing only*
                    }

                    // if one isn't enabled, check which isn't
                    else {
                        if (!isLocationEnabled()) { // no apps can track this device's location
                            showEnableDeviceLocationDialog();
                        }

                        else if (!isLocationPermissionGranted()) { // this app can't track this device's location
                            requestPermission();
                        }
                    }
                }

                // if the switch is checked off
                else {
                    showDisableLocationDialog();
                }

                locationSwitch.setChecked(isLocationEnabled() && isLocationPermissionGranted());
            }
        });

        return view;
    }

    /**
     * Displays the alert dialog that prompts the user to enable "Use location" for the device in
     * settings.
     */
    private void showEnableDeviceLocationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("This device does not currently use your location. Do you want to enable this in settings?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // take the user to location settings
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                locationSwitch.setChecked(false);
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Displays the alert dialog that prompt the user to disable location permissions in settings
     * for the app if they no longer want the app to track their location.
     */
    private void showDisableLocationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Geolocation tracking for event verification is currently turned on. Do you want to disable this in settings?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // take the user to location settings
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                locationSwitch.setChecked(true);
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Checks whether location services are enabled on the current device.
     * @return A boolean representing whether or not location services are enabled.
     */
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {

            // location data is tracked using the device's GPS hardware
            boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // location data is tracked using wifi or cellular data
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            // if at least one is true, location is being tracked
            return isGpsEnabled || isNetworkEnabled;
        }

        return false;
    }

    /**
     * Checks whether the app has been granted the permissions that allow for access to the device's
     * location.
     * @return A boolean representing whether or not the app has location permissions.
     */
    private boolean isLocationPermissionGranted() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Retrieves the device's last location via fusedLocationClient.
     */
    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) { // last location has been successfully retrieved
                    if (location != null) {
                        updateLatAndLong(location); // for testing only*
                    }
                }
            });
        }
        else {
            // request permissions if none are granted
            requestPermission();
        }
    }

    /**
     * Handles displaying a system permissions dialog when the user wants to give the app
     * permissions to track location.
     */
    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // permissions are already granted
            getLocation(); // for testing only*
        }

        else {
            // launch a new system permissions dialog and ask for permissions
            locationPermissionRequest.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION});
        }
    }

    // FOR TESTING PURPOSES ONLY (does it show the correct user location if the switch is on?)
    private void updateLatAndLong(Location location) {
        latitude.setText(String.valueOf(location.getLatitude()));
        longitude.setText(String.valueOf(location.getLongitude()));
    }

    /* How to launch system permissions when the app requests location permissions.
    Reference:
        Author        : Google
        Date Accessed : 3/28/2024
        URL           : https://developer.android.com/develop/sensors-and-location/location/permissions
        Used in       : Lines 420 - 453
    */
    private ActivityResultLauncher<String[]> locationPermissionRequest = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {

        Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
        Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION,false); // having issues, this always stays false

        if (fineLocationGranted != null && fineLocationGranted) {
            // precise location access granted
            locationSwitch.setChecked(true);
            user.setGeolocationTrackingEnabled(true);
        }

        else if (coarseLocationGranted != null && coarseLocationGranted) {
            //  approximate location access granted
            locationSwitch.setChecked(true);
            user.setGeolocationTrackingEnabled(true);
        }

        else {
            locationSwitch.setChecked(false);
            user.setGeolocationTrackingEnabled(false);

            Toast.makeText(getContext(), "Location permissions have been denied. Please change permissions in settings.", Toast.LENGTH_LONG).show();
        }

        // refresh the fragment by detaching and reattaching it to the UI
        getParentFragmentManager().beginTransaction().detach(getActivity().getSupportFragmentManager().findFragmentByTag("profile_fragment")).commit();
        getParentFragmentManager().beginTransaction().attach(getActivity().getSupportFragmentManager().findFragmentByTag("profile_fragment")).commit();

    });

    private void deleteProfilePic() {

        userId = user_test.getUid();
        usersRef.document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String ProfilePicUrl = document.getString("ProfilePic");
                        String usrname = document.getString("Name");
                        if(ProfilePicUrl != null && !ProfilePicUrl.equals("")){
                            usersRef.document(userId).update("ProfilePic", "");


                            if (usrname != null && usrname.equals("")) {
                                Glide.with(requireContext()).load("https://www.gravatar.com/avatar/" + userId + "?d=identicon").into(profilePic);
                            }
                            else {
                                Glide.with(requireContext()).load("https://www.gravatar.com/avatar/" + usrname + "?d=identicon").into(profilePic);
                            }


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
                                user.setProfilePicture(uri.toString());
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

        userId = user_test.getUid();

        usersRef.document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String name = document.getString("Name");
                        String contact = document.getString("Contact");
                        String homePage = document.getString("Homepage");
                        String profilePicUrl = document.getString("ProfilePic");
                        boolean usrlocation = document.getBoolean("Location");

                        ArrayList<String> checkedInto = (ArrayList<String>) document.get("checkedInto");
                        ArrayList<String> signedUpFor = (ArrayList<String>) document.get("myEvents");
                        ArrayList<String> organizing = (ArrayList<String>) document.get("checkedInto");

                        ArrayList<String> reusableCodes = (ArrayList<String>) document.get("reusableCodes");

                        if(profilePicUrl != null && !profilePicUrl.equals("")){
                            String profilePicURI = document.getString("ProfilePic");
                            user = new User(userId, name, homePage, contact, profilePicURI, usrlocation, signedUpFor, checkedInto, organizing, reusableCodes);
                            Glide.with(requireContext()).load(profilePicURI).into(profilePic);
                        } else {

                            if (name != null && name.equals("")) {
                                user = new User(userId, name, homePage, contact, null, usrlocation, signedUpFor, checkedInto, organizing, reusableCodes);
                                Glide.with(requireContext()).load("https://www.gravatar.com/avatar/" + userId + "?d=identicon").into(profilePic);
                            }
                            else {
                                user = new User(userId, name, homePage, contact, null, usrlocation, signedUpFor, checkedInto, organizing, reusableCodes);
                                Glide.with(requireContext()).load("https://www.gravatar.com/avatar/" + user.getName() + "?d=identicon").into(profilePic);
                            }
                        }
                        //setting layout objects to the User object's values
                        userName.setText("Name: " + user.getName());
                        userContact.setText("Contact: " + user.getContactInformation());
                        userHomepage.setText("Homepage: " + user.getHomepage());
                        if (user.getGeolocationTrackingEnabled()) {
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