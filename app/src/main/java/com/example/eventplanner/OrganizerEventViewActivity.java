package com.example.eventplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.WriterException;

import org.checkerframework.checker.units.qual.A;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * The displays an activity that shows an organizer details of an event they are managing.
 */
public class OrganizerEventViewActivity extends AppCompatActivity {

    private Button generateCheckinQR;
    private Button generatePromoQR;
    private Button shareCheckInQR;
    private Button sharePromoQR;
    private ImageView checkinQR;
    private ImageView promoQR;

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
    private RecyclerView signedUpRecyclerView;
    private RecyclerView checkedInRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_event_view);

        db = FirebaseFirestore.getInstance();

        TextView eventNameTextView = findViewById(R.id.event_name2);
        TextView eventDateTextView = findViewById(R.id.event_date2);
        TextView eventTimeTextView = findViewById(R.id.event_time2);
        TextView eventLocationTextView = findViewById(R.id.event_location2);
        TextView eventDescriptionTextView = findViewById(R.id.event_description2);

        announcementsRecyclerView = findViewById(R.id.announcements_recyclerView2);
        signedUpRecyclerView = findViewById(R.id.signedUp_recyclerView);
        checkedInRecyclerView = findViewById(R.id.checkedIn_recyclerView);

        announcementsList = new ArrayList<>();
        signedUpList = new ArrayList<>();
        checkedInList = new ArrayList<>();

        generator = new QRCodeGenerator();

        generateCheckinQR = findViewById(R.id.generateCheckInQR);
        generatePromoQR = findViewById(R.id.generatePromoQR);

        shareCheckInQR = findViewById(R.id.shareCheckInQR);
        sharePromoQR = findViewById(R.id.sharePromoQR);

        checkinQR = findViewById(R.id.checkInQR);
        promoQR = findViewById(R.id.promoQR);

        ImageView poster = findViewById(R.id.poster2);

        bundle = getIntent().getExtras();

        Button backButton = findViewById(R.id.button_back_2);

        backButton.setOnClickListener(view -> finish());

        bundle = getIntent().getExtras();

        if (bundle != null && bundle.containsKey("event")) {
            currEvent = bundle.getParcelable("event");
            if (currEvent != null) {

                // Set event details to views
                eventNameTextView.setText("Name: " + currEvent.getEventName());
                eventDateTextView.setText("Date: " + currEvent.getEventDate());
                eventTimeTextView.setText("Time: " + currEvent.getEventTime());
                eventLocationTextView.setText("Location: " + currEvent.getEventLocation());
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
                    generateCheckinQR.setText("Change Checkin QR");
                    Bitmap qrCode = null; // Replace with your method to generate a QR code

                    try {
                        qrCode = QRCodeGenerator.generateQRCode(currEvent.getCheckInCode(), "check", 1000, 1000);
                    } catch (WriterException e) {
                        throw new RuntimeException(e);
                    }

                    // Set the generated QR code as the image for the checkinQR ImageView.
                    checkinQR.setImageBitmap(qrCode);
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
                    promoQR.setImageBitmap(qrCode);
                }

                generateCheckinQR.setOnClickListener(new View.OnClickListener() {
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
                        checkinQR.setImageBitmap(qrCode);
                    }
                });

                generatePromoQR.setOnClickListener(new View.OnClickListener() {
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
                        promoQR.setImageBitmap(qrCode);
                    }
                });

            }

            signedUserRecyclerAdapter = new UserRecyclerAdapter(this, signedUpList, recyclerViewInterface);
            signedUpRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            signedUpRecyclerView.setAdapter(signedUserRecyclerAdapter);

            checkedInUserRecyclerAdapter = new UserRecyclerAdapter(this, checkedInList, recyclerViewInterface);
            checkedInRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            checkedInRecyclerView.setAdapter(checkedInUserRecyclerAdapter);


        }

        shareCheckInQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Convert the image to a bitmap
                Bitmap bitmap = ((BitmapDrawable) checkinQR.getDrawable()).getBitmap();

                // Convert the Bitmap to a byte array
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
                Uri imageUri = Uri.parse(path);

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                share.putExtra(Intent.EXTRA_STREAM, imageUri);
                startActivity(Intent.createChooser(share, "Share Check in QR"));
            }
        });

        sharePromoQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Convert the image to a bitmap
                Bitmap bitmap = ((BitmapDrawable) promoQR.getDrawable()).getBitmap();

                // Convert the Bitmap to a byte array
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
                Uri imageUri = Uri.parse(path);

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                share.putExtra(Intent.EXTRA_STREAM, imageUri);
                startActivity(Intent.createChooser(share, "Share Promo QR"));
            }
        });

        checkinQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a Dialog
                Dialog dialog = new Dialog(OrganizerEventViewActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_qr_code); // You need to create a new layout file 'dialog_qr_code.xml' for this dialog

                // Get the ImageView from the dialog layout and set the QR code to it
                ImageView dialogQrCode = dialog.findViewById(R.id.dialogQrCode);
                dialogQrCode.setImageDrawable(checkinQR.getDrawable());

                // Set the ImageView size to a larger value
                ViewGroup.LayoutParams layoutParams = dialogQrCode.getLayoutParams();
                layoutParams.width = 1000; // Set the width to a larger value
                layoutParams.height = 1000; // Set the height to a larger value
                dialogQrCode.setLayoutParams(layoutParams);

                // Show the dialog
                dialog.show();
            }
        });

        promoQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a Dialog
                Dialog dialog = new Dialog(OrganizerEventViewActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_qr_code); // You need to create a new layout file 'dialog_qr_code.xml' for this dialog

                // Get the ImageView from the dialog layout and set the QR code to it
                ImageView dialogQrCode = dialog.findViewById(R.id.dialogQrCode);
                dialogQrCode.setImageDrawable(promoQR.getDrawable());

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
        getCheckedInUsers();


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
                        ArrayList<String> checkedInUserIds = (ArrayList<String>) documentSnapshot.get("checkedInUsers");

                        if (checkedInUserIds != null) {
                            loadCheckedInUserDocs(checkedInUserIds, checkedInUserRecyclerAdapter);
                        }
                    }
                });
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

    private void loadCheckedInUserDocs(ArrayList<String> userIds, UserRecyclerAdapter userRecyclerAdapter) {

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

                            checkedInList.add(new User(userId, name, homePage, contact, profilePicUrl, usrlocation, signedUpFor, checkedInto, organizing, reusableCodes));

                            userRecyclerAdapter.notifyDataSetChanged();
                        }
                    });

        }
    }
}