package com.example.eventplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.WriterException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * The displays an activity that shows an organizer details of an event they are managing.
 */
public class OrganizerEventViewActivity extends AppCompatActivity {

    private Button generateCheckinQRButton;
    private Button generatePromoQRButton;
    private Button shareCheckInQRButton;
    private Button sharePromoQRButton;
    private Button deleteEventButton;
    private ImageView checkinQRImageView;
    private ImageView promoQRImageView;

    private QRCodeGenerator generator;

    private ArrayList<String> announcementsList;
    private ArrayList<User> signedUpList;
    private ArrayList<User> checkedInList;
    private ImageView eventPoster;
    private FirebaseFirestore db; // the database
    private Bundle bundle;
    private RecyclerViewInterface recyclerViewInterface;
    private Event currEvent;
    private UserRecyclerAdapter signedUserRecyclerAdapter;
    private UserRecyclerAdapter checkedInUserRecyclerAdapter;
    private AnnouncementsRecyclerAdapter announcementsRecyclerAdapter;
    private RecyclerView announcementsRecyclerView;
    private RecyclerView guestListRecyclerView;
    private RecyclerView checkedInRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_view_updated);

        db = FirebaseFirestore.getInstance();

        TextView eventNameTextView = findViewById(R.id.tv_event_name);
        TextView eventDateTextView = findViewById(R.id.tv_event_date);
        TextView eventTimeTextView = findViewById(R.id.tv_event_time);
        TextView eventLocationTextView = findViewById(R.id.tv_event_location);
        TextView eventDescriptionTextView = findViewById(R.id.tv_event_description);

        announcementsRecyclerView = findViewById(R.id.rv_announcements);
        guestListRecyclerView = findViewById(R.id.rv_guest_list);
//        checkedInRecyclerView = findViewById(R.id.checkedIn_recyclerView);

        announcementsList = new ArrayList<>();
        signedUpList = new ArrayList<>();
        checkedInList = new ArrayList<>();

        generator = new QRCodeGenerator();

        generateCheckinQRButton = findViewById(R.id.generateCheckInQR);
        generatePromoQRButton = findViewById(R.id.generatePromoQR);

        shareCheckInQRButton = findViewById(R.id.shareCheckInQR);
        sharePromoQRButton = findViewById(R.id.sharePromoQR);

        checkinQRImageView = findViewById(R.id.checkInQR);
        promoQRImageView = findViewById(R.id.promoQR);

        deleteEventButton = findViewById(R.id.button_delete_event);

        ImageView poster = findViewById(R.id.iv_poster);

        bundle = getIntent().getExtras();

        Button backButton = findViewById(R.id.button_back_2);

        backButton.setOnClickListener(view -> finish());

        bundle = getIntent().getExtras();

        if (bundle != null && bundle.containsKey("event")) {
            currEvent = bundle.getParcelable("event");
            if (currEvent != null) {

                // Set event details to views
                eventNameTextView.setText(String.format("Name: %s", currEvent.getEventName()));
                eventDateTextView.setText(String.format("Date: %s", currEvent.getEventDate()));
                eventTimeTextView.setText(String.format("Time: %s", currEvent.getEventTime()));
                eventLocationTextView.setText(String.format("Location: %s", currEvent.getEventLocation()));
                eventDescriptionTextView.setText(currEvent.getEventDescription());

                if (currEvent.getEventPoster() != null && !currEvent.getEventPoster().isEmpty()) {
                    Glide.with(this)
                            .load(currEvent.getEventPoster())
                            .into(poster);
                } else {
                    poster.setVisibility(View.GONE);
                }

                if (currEvent.getEventAnnouncements() != null && !currEvent.getEventAnnouncements().isEmpty()) {
                    announcementsList = currEvent.getEventAnnouncements();
                }

                announcementsRecyclerAdapter = new AnnouncementsRecyclerAdapter(this, announcementsList);
                announcementsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                announcementsRecyclerView.setAdapter(announcementsRecyclerAdapter);

                if (!Objects.equals(currEvent.getCheckInCode(), "")) {
                    generateCheckinQRButton.setText("Change Checkin QR");
                    Bitmap qrCode = null; // Replace with your method to generate a QR code

                    try {
                        qrCode = QRCodeGenerator.generateQRCode(currEvent.getCheckInCode(), "check", 1000, 1000);
                    } catch (WriterException e) {
                        throw new RuntimeException(e);
                    }

                    // Set the generated QR code as the image for the checkinQR ImageView.
                    checkinQRImageView.setImageBitmap(qrCode);
                    checkinQRImageView.setVisibility(View.VISIBLE);
                }

                if (!Objects.equals(currEvent.getPromoCode(), "")) {
                    // Generate a new QR code. You'll need to replace "qrCodeData" with the actual data you want to encode in the QR code.
                    Bitmap qrCode = null; // Replace with your method to generate a QR code
                    try {
                        qrCode = QRCodeGenerator.generateQRCode(currEvent.getPromoCode(), "promo", 1000, 1000);
                    } catch (WriterException e) {
                        throw new RuntimeException(e);
                    }

                    // Set the generated QR code as the image for the checkinQR ImageView.
                    promoQRImageView.setImageBitmap(qrCode);
                    promoQRImageView.setVisibility(View.VISIBLE);
                }

                generateCheckinQRButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Generate a new QR code. You'll need to replace "qrCodeData" with the actual data you want to encode in the QR code.
                        Bitmap qrCode = null; // Replace with your method to generate a QR code

                        currEvent.setCheckInCode(generateRandomCode(25));
                        db.collection("events").document(currEvent.getEventId()).update("checkInCode", currEvent.getCheckInCode());
                        try {
                            qrCode = QRCodeGenerator.generateQRCode(currEvent.getCheckInCode(), "check", 1000, 1000);
                        } catch (WriterException e) {
                            throw new RuntimeException(e);
                        }

                        // Set the generated QR code as the image for the checkinQR ImageView.
                        checkinQRImageView.setImageBitmap(qrCode);
                        checkinQRImageView.setVisibility(View.VISIBLE);
                    }
                });

                generatePromoQRButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Generate a new QR code. You'll need to replace "qrCodeData" with the actual data you want to encode in the QR code.
                        Bitmap qrCode = null; // Replace with your method to generate a QR code

                        currEvent.setPromoCode(generateRandomCode(25));
                        db.collection("events").document(currEvent.getEventId()).update("promoCode", currEvent.getPromoCode());
                        try {
                            qrCode = QRCodeGenerator.generateQRCode(currEvent.getPromoCode(),"promo", 1000, 1000);
                        } catch (WriterException e) {
                            throw new RuntimeException(e);
                        }

                        // Set the generated QR code as the image for the checkinQR ImageView.
                        promoQRImageView.setImageBitmap(qrCode);
                        promoQRImageView.setVisibility(View.VISIBLE);
                    }
                });

                deleteEventButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Confirm with the user before deleting the event
                        new AlertDialog.Builder(v.getContext())
                                .setTitle("Delete event")
                                .setMessage("Are you sure you want to delete this event?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        db.collection("events").document(currEvent.getEventId())
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(v.getContext(), "Event deleted", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(v.getContext(), "Error deleting event", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        db.collection("users").document(userId)
                                                .update("reusableCodes", FieldValue.arrayUnion(currEvent.getCheckInCode()))
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("TAG", "DocumentSnapshot successfully updated!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w("TAG", "Error updating document", e);
                                                    }
                                                });

                                        db.collection("users").document(userId)
                                                .update("Organizing", FieldValue.arrayRemove(currEvent.getEventId()))
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("TAG", "DocumentSnapshot successfully updated!");
                                                        finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w("TAG", "Error updating document", e);
                                                        finish();
                                                    }
                                                });

                                    }
                                })
                                .setNegativeButton(android.R.string.no, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
            }

            signedUserRecyclerAdapter = new UserRecyclerAdapter(this, signedUpList, recyclerViewInterface);
            guestListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            guestListRecyclerView.setAdapter(signedUserRecyclerAdapter);

            //checkedInUserRecyclerAdapter = new UserRecyclerAdapter(this, checkedInList, recyclerViewInterface);
            //checkedInRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            //checkedInRecyclerView.setAdapter(checkedInUserRecyclerAdapter);
        }

        shareCheckInQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Convert the image to a bitmap
                Drawable drawable = checkinQRImageView.getDrawable();
                if (drawable instanceof BitmapDrawable) {
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

                    // Convert the Bitmap to a byte array
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
                    if (path != null) {
                        Uri imageUri = Uri.parse(path);

                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/jpeg");
                        share.putExtra(Intent.EXTRA_STREAM, imageUri);
                        startActivity(Intent.createChooser(share, "Share Check in QR"));
                    }
                }
            }
        });

        sharePromoQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Convert the image to a bitmap
                Drawable drawable = promoQRImageView.getDrawable();
                if (drawable instanceof BitmapDrawable) {
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

                    // Convert the Bitmap to a byte array
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
                    if (path != null) {
                        Uri imageUri = Uri.parse(path);

                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/jpeg");
                        share.putExtra(Intent.EXTRA_STREAM, imageUri);
                        startActivity(Intent.createChooser(share, "Share Promo QR"));
                    }
                }
            }
        });


        checkinQRImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a Dialog
                Dialog dialog = new Dialog(OrganizerEventViewActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_qr_code); // You need to create a new layout file 'dialog_qr_code.xml' for this dialog

                // Get the ImageView from the dialog layout and set the QR code to it
                ImageView dialogQrCode = dialog.findViewById(R.id.dialogQrCode);
                dialogQrCode.setImageDrawable(checkinQRImageView.getDrawable());

                // Set the ImageView size to a larger value
                ViewGroup.LayoutParams layoutParams = dialogQrCode.getLayoutParams();
                layoutParams.width = 1000; // Set the width to a larger value
                layoutParams.height = 1000; // Set the height to a larger value
                dialogQrCode.setLayoutParams(layoutParams);

                // Show the dialog
                dialog.show();
            }
        });

        promoQRImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a Dialog
                Dialog dialog = new Dialog(OrganizerEventViewActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_qr_code); // You need to create a new layout file 'dialog_qr_code.xml' for this dialog

                // Get the ImageView from the dialog layout and set the QR code to it
                ImageView dialogQrCode = dialog.findViewById(R.id.dialogQrCode);
                dialogQrCode.setImageDrawable(promoQRImageView.getDrawable());

                // Set the ImageView size to a larger value
                ViewGroup.LayoutParams layoutParams = dialogQrCode.getLayoutParams();
                layoutParams.width = 1000; // Set the width to a larger value
                layoutParams.height = 1000; // Set the height to a larger value
                dialogQrCode.setLayoutParams(layoutParams);

                // Show the dialog
                dialog.show();
            }
        });

        getSignedUpUsers();
//        getCheckedInUsers();


        Button mapButton = findViewById(R.id.button_organizer_map);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(OrganizerEventViewActivity.this, OrganizerMapActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                OrganizerEventViewActivity.this.startActivity(myIntent);
            }
        });
    }

    private void getSignedUpUsers() {

        db.collection("events")
                .document(currEvent.getEventId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        // get all events in user's organizing ArrayList and put them in another ArrayList of eventIds
                        ArrayList<String> signedUpUserIds = (ArrayList<String>) documentSnapshot.get("signedUpUsers");

                        if (signedUpUserIds != null) {
                            loadSignedUpUserDocs(signedUpUserIds, signedUserRecyclerAdapter);
                        }
                    }
                });
    }

    private void getCheckedInUsers() {

        db.collection("events")
                .document(currEvent.getEventId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        // get all events in user's organizing ArrayList and put them in another ArrayList of eventIds
                        HashMap<String, String> checkedInUsersFromDB = (HashMap<String, String>) documentSnapshot.get("checkedInUsersTest");

                        assert checkedInUsersFromDB != null;
                        ArrayList<CheckedInUser> checkedInUserIds = convertCheckedInUsersMapToArrayList(checkedInUsersFromDB);

                        if (checkedInUserIds != null) {
                            loadCheckedInUserDocs(checkedInUserIds, checkedInUserRecyclerAdapter);
                        }
                    }
                });
    }

    private ArrayList<CheckedInUser> convertCheckedInUsersMapToArrayList(HashMap<String, String> checkedInUsersFromDB) {

        ArrayList<CheckedInUser> checkedInUsers = new ArrayList<>();

        for (Map.Entry<String, String> entry : checkedInUsersFromDB.entrySet()) {

            String userId = entry.getKey();
            String numberOfCheckins = entry.getValue();
            checkedInUsers.add(new CheckedInUser(userId, numberOfCheckins));
        }

        return checkedInUsers;
    }

    private String generateRandomCode(int codeLength) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(codeLength);

        for (int i = 0; i < codeLength; i++) {
            sb.append(characters.charAt(rnd.nextInt(characters.length())));
        }

        return sb.toString();
    }

    private void loadSignedUpUserDocs(ArrayList<String> userIds, UserRecyclerAdapter userRecyclerAdapter) {

        for (String userId : userIds) { // for every eventId in user's organizing Array
            db.collection("users")
                    .document(userId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            String userId = documentSnapshot.getId();
                            String name = documentSnapshot.getString("Name");
                            String contact = documentSnapshot.getString("Contact");
                            String homePage = documentSnapshot.getString("Homepage");
                            String profilePicUrl = documentSnapshot.getString("ProfilePic");
                            boolean usrlocation = documentSnapshot.getBoolean("Location");

                            ArrayList<String> checkedInto = (ArrayList<String>) documentSnapshot.get("checkedInto");
                            ArrayList<String> signedUpFor = (ArrayList<String>) documentSnapshot.get("myEvents");
                            ArrayList<String> organizing = (ArrayList<String>) documentSnapshot.get("Organizing");

                            ArrayList<String> reusableCodes = (ArrayList<String>) documentSnapshot.get("reusableCodes");

                            signedUpList.add(new User(userId, name, homePage, contact, profilePicUrl, usrlocation, signedUpFor, checkedInto, organizing, reusableCodes));

                            userRecyclerAdapter.notifyDataSetChanged();
                        }
                    });

        }
    }

    private void loadCheckedInUserDocs(ArrayList<CheckedInUser> checkedInUsers, UserRecyclerAdapter userRecyclerAdapter) {

        for (CheckedInUser user : checkedInUsers) { // for every eventId in user's organizing Array
            db.collection("users")
                    .document(user.getUserId())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            String userId = documentSnapshot.getId();
                            String name = documentSnapshot.getString("Name");
                            String contact = documentSnapshot.getString("Contact");
                            String homePage = documentSnapshot.getString("Homepage");
                            String profilePicUrl = documentSnapshot.getString("ProfilePic");
                            boolean usrlocation = documentSnapshot.getBoolean("Location");

                            ArrayList<String> checkedInto = (ArrayList<String>) documentSnapshot.get("checkedInto");
                            ArrayList<String> signedUpFor = (ArrayList<String>) documentSnapshot.get("myEvents");
                            ArrayList<String> organizing = (ArrayList<String>) documentSnapshot.get("Organizing");

                            ArrayList<String> reusableCodes = (ArrayList<String>) documentSnapshot.get("reusableCodes");

                            checkedInList.add(new User(userId, name, homePage, contact, profilePicUrl, usrlocation, signedUpFor, checkedInto, organizing, reusableCodes));

                            userRecyclerAdapter.notifyDataSetChanged();
                        }
                    });

        }
    }
}