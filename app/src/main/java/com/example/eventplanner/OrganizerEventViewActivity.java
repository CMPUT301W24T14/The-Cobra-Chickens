package com.example.eventplanner;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventplanner.databinding.FragmentPushNotificationsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.zxing.WriterException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * The displays an activity that shows an organizer the details of an event they are managing.
 */
public class OrganizerEventViewActivity extends AppCompatActivity {
    private Button generateCheckinQRButton;
    private Button generatePromoQRButton;

    private Button shareCheckInQRButton;
    private Button sharePromoQRButton;
    private Button makeNotificationButton;
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
    private UserRecyclerAdapter guestListRecyclerAdapter;
    private UserRecyclerAdapter checkedInUserRecyclerAdapter;
    private AnnouncementsRecyclerAdapter announcementsRecyclerAdapter;
    private RecyclerView announcementsRecyclerView;
    private RecyclerView guestListRecyclerView;
    private RecyclerView checkedInRecyclerView;
    private Button addAnnouncementsButton;
    private Boolean geolocationTracking = false;

    /**
     * This method is called when the activity is starting.
     * It initializes the activity's view and variables, sets up the RecyclerViews, and loads the event data.
     * It retrieves the current event from the intent extras, checks if geolocation tracking is turned on for the event,
     * updates the attendee count and event details, and displays the event poster if it exists.
     * It also sets up the announcements RecyclerView with the event's announcements, and if a check-in QR code has already been generated for the event,
     * it retrieves the QR code and displays it on the screen.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_view_updated);

        db = FirebaseFirestore.getInstance();

        //Get the text views for all displayed event attributes
        TextView eventNameTextView = findViewById(R.id.tv_event_name);
        TextView eventDateTextView = findViewById(R.id.tv_event_date);
        TextView eventTimeTextView = findViewById(R.id.tv_event_time);
        TextView eventLocationTextView = findViewById(R.id.tv_event_location);
        TextView eventDescriptionTextView = findViewById(R.id.tv_event_description);
        TextView numberOfAttendees = findViewById(R.id.tv_number_attendees);

        //Assign Variables
        announcementsRecyclerView = findViewById(R.id.rv_announcements);
        guestListRecyclerView = findViewById(R.id.rv_guest_list);

        announcementsList = new ArrayList<>();
        signedUpList = new ArrayList<>();
        checkedInList = new ArrayList<>();

        generator = new QRCodeGenerator();

        //Buttons for generating QR codes for the event
        generateCheckinQRButton = findViewById(R.id.generateCheckInQR);
        generatePromoQRButton = findViewById(R.id.generatePromoQR);


        shareCheckInQRButton = findViewById(R.id.shareCheckInQR);
        sharePromoQRButton = findViewById(R.id.sharePromoQR);
        makeNotificationButton = findViewById(R.id.add_push_notification_button);
        makeNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to show the push notification dialog
                showPushNotificationDialog();
            }
        });

        //QR code image itself
        checkinQRImageView = findViewById(R.id.checkInQR);
        promoQRImageView = findViewById(R.id.promoQR);

        //Share buttons for both promo and checkin qr
        Button shareCheckInQRButton = findViewById(R.id.shareCheckInQR);
        Button sharePromoQRButton = findViewById(R.id.sharePromoQR);

        //Delete event button
        deleteEventButton = findViewById(R.id.button_delete_event);

        //The image view of the poster for the event
        ImageView poster = findViewById(R.id.iv_poster);

        //Get intent from the organizer events fragment
        bundle = getIntent().getExtras();

        //Return to Organize Events fragment when user presses back button
        Button backButton = findViewById(R.id.button_back_2);
        backButton.setOnClickListener(view -> finish());

        //Check that the bundle is valid and then access the event information
        if (bundle != null && bundle.containsKey("event")) {
            currEvent = bundle.getParcelable("event");
            if (currEvent != null) {

                //Checks data base to see if geo location tracking for
                // the current event is turned on
                db.collection("events")
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (Objects.equals(currEvent.getEventId(), document.getId())){
                                        geolocationTracking = (Boolean) document.get("geolocationTracking");
                                    }
                                }
                            }
                        });

                //Updates event attendee count
                String attendeeCount = String.valueOf(currEvent.getCheckedInUsers().size());
                numberOfAttendees.setText(String.format("Number of checked-in attendees: %s", attendeeCount));

                // Updates text views to show event details
                eventNameTextView.setText(String.format("Name: %s", currEvent.getEventName()));
                eventDateTextView.setText(String.format("Date: %s", currEvent.getEventDate()));
                eventTimeTextView.setText(String.format("Time: %s", currEvent.getEventTime()));
                eventLocationTextView.setText(String.format("Location: %s", currEvent.getEventLocation()));
                eventDescriptionTextView.setText(currEvent.getEventDescription());

                //If the poster exists display it, otherwise remove it from the event
                if (currEvent.getEventPoster() != null && !currEvent.getEventPoster().isEmpty()) {
                    Glide.with(this)
                            .load(currEvent.getEventPoster())
                            .into(poster);
                } else {
                    poster.setVisibility(View.GONE);
                }

                //If there are events add them to the event list
                if (currEvent.getEventAnnouncements() != null && !currEvent.getEventAnnouncements().isEmpty()) {
                    announcementsList = currEvent.getEventAnnouncements();
                }

                //Create announcementsRecyclerAdapter
                announcementsRecyclerAdapter = new AnnouncementsRecyclerAdapter(this, announcementsList);
                announcementsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                announcementsRecyclerView.setAdapter(announcementsRecyclerAdapter);

                //If a check in qr has already been generated fetch that qr from the database and add
                //it to the to screen
                if (!Objects.equals(currEvent.getCheckInCode(), "")) {

                    Bitmap qrCode = null;

                    try {
                        qrCode = QRCodeGenerator.generateQRCode(currEvent.getCheckInCode(), "check", 1000, 1000);
                    } catch (WriterException e) {
                        throw new RuntimeException(e);
                    }

                    // Set the generated QR code as the image for the checkinQR ImageView.
                    checkinQRImageView.setImageBitmap(qrCode);
                    checkinQRImageView.setVisibility(View.VISIBLE);
                    shareCheckInQRButton.setVisibility(View.VISIBLE);
                }

                //If a promo qr has already been generated fetch that qr from the database and add
                //it to the to screen
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
                    sharePromoQRButton.setVisibility(View.VISIBLE);
                }

                //Creates a pop up that allows the user to choose to reuse an existing qr code
                //or generate a new qr
                generateCheckinQRButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                        db.collection("users").document(userId).get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            ArrayList<String> reusableCodes = (ArrayList<String>) documentSnapshot.get("reusableCodes");
                                            if (reusableCodes != null) {
                                                reusableCodes.removeIf(code -> code == null || code.equals(""));
                                                if (reusableCodes.size() > 15) {
                                                    reusableCodes.subList(0, reusableCodes.size() - 15).clear();
                                                }
                                            }

                                            db.collection("users").document(userId).update("reusableCodes", reusableCodes);

                                            // Create a pop up shows all past codes as qr codes and allows the user to click and choose which one they want
                                            AlertDialog.Builder builder = new AlertDialog.Builder(OrganizerEventViewActivity.this);
                                            builder.setTitle("Reuse a QR Code");

                                            // Convert the reusableCodes into an array of Bitmaps
                                            ArrayList<Bitmap> qrCodes = new ArrayList<>();
                                            if (reusableCodes != null) {
                                                for (String code : reusableCodes) {
                                                    try {
                                                        Bitmap qr = QRCodeGenerator.generateQRCode(code, "check", 1000, 1000);
                                                        qrCodes.add(qr);
                                                    } catch (WriterException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }
                                            }

                                            // Create a custom ArrayAdapter to display the QR codes
                                            ArrayAdapter<Bitmap> arrayAdapter = new ArrayAdapter<Bitmap>(OrganizerEventViewActivity.this, android.R.layout.select_dialog_singlechoice, qrCodes) {
                                                @NonNull
                                                @Override
                                                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                                                    ImageView imageView = new ImageView(OrganizerEventViewActivity.this);
                                                    imageView.setImageBitmap(getItem(position));
                                                    return imageView;
                                                }
                                            };

                                            builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Set the selected QR code as the image for the checkinQR ImageView.
                                                    checkinQRImageView.setImageBitmap(qrCodes.get(which));
                                                    checkinQRImageView.setVisibility(View.VISIBLE);
                                                    shareCheckInQRButton.setVisibility(View.VISIBLE);
                                                    currEvent.setCheckInCode(reusableCodes.get(which));
                                                    db.collection("events").document(currEvent.getEventId()).update("checkInCode", currEvent.getCheckInCode());
                                                }
                                            });

                                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });

                                            builder.setPositiveButton("Generate New QR", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Generate a new QR code
                                                    String newCode = generateRandomCode(25);
                                                    currEvent.setCheckInCode(newCode);
                                                    Bitmap newQRCode;
                                                    try {
                                                        newQRCode = QRCodeGenerator.generateQRCode(newCode, "check", 1000, 1000);
                                                    } catch (WriterException e) {
                                                        throw new RuntimeException(e);
                                                    }

                                                    // Set the new QR code as the image for the checkinQR ImageView.
                                                    checkinQRImageView.setImageBitmap(newQRCode);
                                                    checkinQRImageView.setVisibility(View.VISIBLE);
                                                    shareCheckInQRButton.setVisibility(View.VISIBLE);
                                                    db.collection("events").document(currEvent.getEventId()).update("checkInCode", currEvent.getCheckInCode());
                                                    dialog.dismiss();
                                                }
                                            });

                                            // Use a Handler to post the action of showing the dialog to the message queue of the UI thread
                                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    builder.show();
                                                }
                                            });

                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("TAG", "Error getting documents.", e);
                                    }
                                });
                    }
                });

                //Generates a new random promo qr code
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
                        sharePromoQRButton.setVisibility(View.VISIBLE);
                    }
                });

                //Deletes the event
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

            //Creates guest list recycler adapter that updates whos signed up and whos checked in
            guestListRecyclerAdapter = new UserRecyclerAdapter(this, currEvent.getEventId(), signedUpList, recyclerViewInterface);
            guestListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            guestListRecyclerView.setAdapter(guestListRecyclerAdapter);

            addAnnouncementsButton = findViewById(R.id.add_announcement_button);

            addAnnouncementsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAddAnnouncementDialog();
                }
            });
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

        Button mapButton = findViewById(R.id.button_organizer_map);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (geolocationTracking){
                    Intent myIntent = new Intent(OrganizerEventViewActivity.this, OrganizerMapActivity.class);
                    myIntent.putExtra("event", currEvent); //Optional parameters
                    OrganizerEventViewActivity.this.startActivity(myIntent);
                } else {
                    Toast.makeText(OrganizerEventViewActivity.this, "Geolocation tracking is disabled for this event.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void showPushNotificationDialog() {
        // Inflate the dialog with custom view
        LayoutInflater inflater = this.getLayoutInflater();
        FragmentPushNotificationsBinding binding = FragmentPushNotificationsBinding.inflate(inflater, null, false);

        // Initialize ViewModel
        PushnotificationViewModel pushnotificationViewModel = new ViewModelProvider(this).get(PushnotificationViewModel.class);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(binding.getRoot());

        // Setup the dialog
        AlertDialog alertDialog = dialogBuilder.create();

        // Automatically subscribe to the event topic
        String topic = currEvent.getEventName(); // Use event name as the topic
        pushnotificationViewModel.subscribeToNewTopic(topic, new TopicCallback() {
            @Override
            public void onSubscribed() {
                // Setting the Text View
                binding.textviewCurrentTopic.setText(topic);
                // Saving Topic in SharedPref (optional)
                // SharedPreferencesHelper.setCurrentTopic(getApplicationContext(), topic);
            }
        });

        binding.buttonSend.setOnClickListener(v -> {
            String title = binding.titleEdittext.getText().toString();
            String message = binding.messageEdittext.getText().toString();

            if (!title.isEmpty() && !message.isEmpty() && !topic.isEmpty()) {
                PushNotification pushNotification = new PushNotification(
                        new NotificationData(title, message),
                        "/topics/" + topic
                );
                pushnotificationViewModel.sendNewMessage(pushNotification);
                alertDialog.dismiss(); // Close the dialog after sending the message
            } else {
                Toast.makeText(this, "MISSING INFORMATION", Toast.LENGTH_SHORT).show();
            }
        });

        // Show the dialog
        alertDialog.show();
    }


    /**
     * This method creates and displays a dialog to add a new announcement.
     * The dialog contains an EditText for input and two buttons: "Add" and "Cancel".
     * When the "Add" button is clicked, the text from the EditText is retrieved and
     * added to the "eventAnnouncements" field of the current event document in the
     * Firestore "events" collection.
     */
    private void showAddAnnouncementDialog() {

        EditText newAnnouncementEditText = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder
                .setTitle("New Announcement")
                .setView(newAnnouncementEditText)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String editTextInput = newAnnouncementEditText.getText().toString();

                        db.collection("events").document(currEvent.getEventId()).update("eventAnnouncements", FieldValue.arrayUnion(editTextInput))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                });
                    }

                })
                .create()
                .show();

    }

    /**
     * This method retrieves the list of users signed up for the current event from the Firestore database.
     * It accesses the "events" collection, gets the document corresponding to the current event's ID, and retrieves the "signedUpUsers" field.
     * This field is expected to be an ArrayList of Strings, where each String is a user ID.
     * If the list of signed up user IDs is not null, it calls the method loadSignedUpUserDocs() with the list and the guestListRecyclerAdapter as arguments.
     */
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
                            loadSignedUpUserDocs(signedUpUserIds, guestListRecyclerAdapter);
                        }
                    }
                });
    }

    /**
     * This method retrieves the list of users checked in for the current event from the Firestore database.
     * It accesses the "events" collection, gets the document corresponding to the current event's ID, and retrieves the "checkedInUsers" field.
     * This field is expected to be a HashMap where each key-value pair represents a user ID and its corresponding check-in status.
     * The HashMap is then converted into an ArrayList of CheckedInUser objects using the convertCheckedInUsersMapToArrayList() method.
     * If the ArrayList of CheckedInUser objects is not null, it calls the method loadCheckedInUserDocs() with the list and the checkedInUserRecyclerAdapter as arguments.
     */
    private void getCheckedInUsers() {

        db.collection("events")
                .document(currEvent.getEventId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        // get all events in user's organizing ArrayList and put them in another ArrayList of eventIds
                        HashMap<String, String> checkedInUsersFromDB = (HashMap<String, String>) documentSnapshot.get("checkedInUsers");

                        assert checkedInUsersFromDB != null;
                        ArrayList<CheckedInUser> checkedInUserIds = convertCheckedInUsersMapToArrayList(checkedInUsersFromDB);

                        if (checkedInUserIds != null) {
                            loadCheckedInUserDocs(checkedInUserIds, checkedInUserRecyclerAdapter);
                        }
                    }
                });
    }

    /**
     * This method converts a HashMap of checked-in users into an ArrayList of CheckedInUser objects.
     * Each key-value pair in the HashMap represents a user ID and its corresponding number of check-ins.
     * For each entry in the HashMap, a new CheckedInUser object is created with the user ID and number of check-ins, and added to the ArrayList.
     * @param checkedInUsersFromDB The HashMap of checked-in users to be converted. Each key-value pair represents a user ID and its corresponding number of check-ins.
     * @return An ArrayList of CheckedInUser objects representing the checked-in users.
     */
    private ArrayList<CheckedInUser> convertCheckedInUsersMapToArrayList(HashMap<String, String> checkedInUsersFromDB) {

        ArrayList<CheckedInUser> checkedInUsers = new ArrayList<>();

        for (Map.Entry<String, String> entry : checkedInUsersFromDB.entrySet()) {

            String userId = entry.getKey();
            String numberOfCheckins = entry.getValue();
            checkedInUsers.add(new CheckedInUser(userId, numberOfCheckins));
        }

        return checkedInUsers;
    }

    /**
     * This method generates a random alphanumeric string of a specified length.
     * It uses a string of all uppercase and lowercase letters and digits as the character set.
     * A new random character from this set is appended to the result for each iteration up to the specified length.
     * @param codeLength The length of the random code to be generated.
     * @return A random alphanumeric string of the specified length.
     */
    private String generateRandomCode(int codeLength) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(codeLength);

        for (int i = 0; i < codeLength; i++) {
            sb.append(characters.charAt(rnd.nextInt(characters.length())));
        }

        return sb.toString();
    }

    /**
     * This method loads the documents of users who have signed up for an event.
     * It iterates over a list of user IDs, and for each ID, it retrieves the corresponding user document from the Firestore database.
     * The user document contains various fields such as Name, Contact, Homepage, ProfilePic, Location, checkedInto, myEvents, Organizing, and reusableCodes.
     * A new User object is created with these fields and added to the signedUpList.
     * The UserRecyclerAdapter is then notified that the dataset has changed.
     * @param userIds The list of user IDs for which to load documents.
     * @param userRecyclerAdapter The UserRecyclerAdapter to be notified of dataset changes.
     */
    private void loadSignedUpUserDocs(ArrayList<String> userIds, UserRecyclerAdapter userRecyclerAdapter) {
        for (String userId : userIds) {
            if (userId != null) {
                db.collection("users")
                        .document(userId)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    String userId = documentSnapshot.getId();
                                    String name = documentSnapshot.getString("Name");
                                    String contact = documentSnapshot.getString("Contact");
                                    String homePage = documentSnapshot.getString("Homepage");
                                    String profilePicUrl = documentSnapshot.getString("ProfilePic");
                                    Boolean usrlocation = documentSnapshot.getBoolean("Location");

                                    ArrayList<String> checkedInto = (ArrayList<String>) documentSnapshot.get("checkedInto");
                                    ArrayList<String> signedUpFor = (ArrayList<String>) documentSnapshot.get("myEvents");
                                    ArrayList<String> organizing = (ArrayList<String>) documentSnapshot.get("Organizing");
                                    ArrayList<String> reusableCodes = (ArrayList<String>) documentSnapshot.get("reusableCodes");

                                    // Check if any of the retrieved fields are null before using them
                                    if (userId != null && name != null && contact != null && homePage != null && profilePicUrl != null && usrlocation != null && checkedInto != null && signedUpFor != null && organizing != null && reusableCodes != null) {
                                        signedUpList.add(new User(userId, name, homePage, contact, profilePicUrl, usrlocation, signedUpFor, checkedInto, organizing, reusableCodes));
                                        userRecyclerAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        });
            }
        }
    }

    /**
     * This method loads the documents of users who have checked in for an event.
     * It iterates over a list of CheckedInUser objects, and for each object, it retrieves the corresponding user document from the Firestore database.
     * The user document contains various fields such as Name, Contact, Homepage, ProfilePic, Location, checkedInto, myEvents, Organizing, and reusableCodes.
     * A new User object is created with these fields and added to the checkedInList.
     * The UserRecyclerAdapter is then notified that the dataset has changed.
     * @param checkedInUsers The list of CheckedInUser objects for which to load documents.
     * @param userRecyclerAdapter The UserRecyclerAdapter to be notified of dataset changes.
     */
    private void loadCheckedInUserDocs(ArrayList<CheckedInUser> checkedInUsers, UserRecyclerAdapter userRecyclerAdapter) {
        for (CheckedInUser user : checkedInUsers) {
            if (user != null) {
                db.collection("users")
                        .document(user.getUserId())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    String userId = documentSnapshot.getId();
                                    String name = documentSnapshot.getString("Name");
                                    String contact = documentSnapshot.getString("Contact");
                                    String homePage = documentSnapshot.getString("Homepage");
                                    String profilePicUrl = documentSnapshot.getString("ProfilePic");
                                    Boolean usrlocation = documentSnapshot.getBoolean("Location");

                                    ArrayList<String> checkedInto = (ArrayList<String>) documentSnapshot.get("checkedInto");
                                    ArrayList<String> signedUpFor = (ArrayList<String>) documentSnapshot.get("myEvents");
                                    ArrayList<String> organizing = (ArrayList<String>) documentSnapshot.get("Organizing");
                                    ArrayList<String> reusableCodes = (ArrayList<String>) documentSnapshot.get("reusableCodes");

                                    // Check if any of the retrieved fields are null before using them
                                    if (userId != null && name != null && contact != null && homePage != null && profilePicUrl != null && usrlocation != null && checkedInto != null && signedUpFor != null && organizing != null && reusableCodes != null) {
                                        checkedInList.add(new User(userId, name, homePage, contact, profilePicUrl, usrlocation, signedUpFor, checkedInto, organizing, reusableCodes));
                                        userRecyclerAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        });
            }
        }
    }

}